package cc.vergence.injections.mixins.chat;

import cc.vergence.Vergence;
import cc.vergence.features.commands.Command;
import cc.vergence.util.render.utils.Render2DUtil;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ChatInputSuggestor.class)
public abstract class MixinChatInputSuggester {
	@Final
	@Shadow
	TextFieldWidget textField;
	@Shadow
	private CompletableFuture<Suggestions> pendingSuggestions;
	@Final
	@Shadow
	private List<OrderedText> messages;

	@Shadow
	public abstract void show(boolean narrateFirstSuggestion);
	@Unique
	private boolean showOutline = false;

	@Inject(at = {@At(value = "HEAD")}, method = "render")
	private void onRender(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
		if (showOutline) {
			//Up
			//context.fill(textField.getX() - 1, textField.getY() - 2, textField.getX() + textField.getWidth(), textField.getY() - 1, Past.GameInfo.getColor().getRGB());

			//Down
			//context.fill(textField.getX() - 1, textField.getY() - 2 + textField.getHeight(), textField.getX() + textField.getWidth() - 1, textField.getY() + textField.getHeight() - 1, Past.GameInfo.getColor().getRGB());

			//Left
			//context.fill(textField.getX() - 2, textField.getY() - 2, textField.getX() - 1, textField.getY() - 3 + textField.getHeight(), Past.GameInfo.getColor().getRGB());

			//Right
			//context.fill(textField.getX() + textField.getWidth() - 3, textField.getY() - 1, textField.getX() + textField.getWidth() - 2, textField.getY() + textField.getHeight(), Past.GameInfo.getColor().getRGB());

			int x = textField.getX() - 3;
			int y = textField.getY() - 3;
			//Up
			Render2DUtil.drawRect(context, x, y, textField.getWidth() + 1, 1, Vergence.THEME.getTheme().getMainColor());

			//Down
			Render2DUtil.drawRect(context, x, y + textField.getHeight() + 1, textField.getWidth() + 1, 1, Vergence.THEME.getTheme().getMainColor());

			//Left
			Render2DUtil.drawRect(context, x, y, 1, textField.getHeight() + 1, Vergence.THEME.getTheme().getMainColor());

			//Right
			Render2DUtil.drawRect(context, x + textField.getWidth() + 1, y, 1, textField.getHeight() + 2, Vergence.THEME.getTheme().getMainColor());
		}
	}

	@Inject(at = {
			@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;getCursor()I", ordinal = 0)}, method = "refresh()V")
	private void onRefresh(CallbackInfo ci) {
		String prefix = Vergence.PREFIX;
		String string = this.textField.getText();

		showOutline = string.startsWith(prefix);

		if (!string.isEmpty()) {
			int cursorPos = this.textField.getCursor();
			String string2 = string.substring(0, cursorPos);

			if (prefix.startsWith(string2) || string2.startsWith(prefix)) {
				int j = 0;
				Matcher matcher = Pattern.compile("(\\s+)").matcher(string2);
				while (matcher.find()) {
					j = matcher.end();
				}

				SuggestionsBuilder builder = new SuggestionsBuilder(string2, j);
				if (string2.length() < prefix.length()) {
					if (prefix.startsWith(string2)) {
						builder.suggest(prefix);
					} else {
						return;
					}
				} else if (string2.startsWith(prefix)) {
					int count = StringUtils.countMatches(string2, " ");
					List<String> seperated = Arrays.asList(string2.split(" "));
					if (count == 0) {
						for (Object strObj : Vergence.COMMAND.getCommands().keySet().toArray()) {
							String str = (String) strObj;
							builder.suggest(Vergence.PREFIX + str + " ");
						}
					} else {
						if (seperated.isEmpty()) return;
						Command c = Vergence.COMMAND.getCommandBySyntax(seperated.get(0).substring(prefix.length()));
						if (c == null) {
							messages.add(Text.of(Vergence.TEXT.get("COMMANDS.MESSAGE.COMMAND_NOT_FOUND") + seperated.get(0).substring(prefix.length())).asOrderedText());
							return;
						}

						String[] suggestions = c.getAutocorrect(count, seperated);

						if (suggestions == null || suggestions.length == 0) return;
						for (String str : suggestions) {
							builder.suggest(str + " ");
						}
					}
				} else {
					return;
				}

				this.pendingSuggestions = builder.buildFuture();
				this.show(false);
			}
		}
	}
}
