package cc.vergence.injections.mixins.chat;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.ReceiveMessageEvent;
import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.modules.client.BetterChat;
import cc.vergence.util.color.ColorUtil;
import cc.vergence.util.interfaces.IChatHud;
import cc.vergence.util.interfaces.IChatHudLine;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.utils.FadeUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;

@Mixin(ChatHud.class)
public abstract class MixinChatHud implements IChatHud, Wrapper {
    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;
    @Shadow
    @Final
    private List<ChatHudLine> messages;
    @Unique
    private int vergenceNext = 0;
    @Shadow
    public abstract void addMessage(Text message);

    @Override
    public void vergence$add(Text message, int id) {
        vergenceNext = id;
        addMessage(message);
        vergenceNext = 0;
    }



    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At(value = "HEAD", ordinal = 0, shift = At.Shift.AFTER))
    private void onAddMessageAfterNewChatHudLineVisible(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        if (!visibleMessages.isEmpty()) {
            ((IChatHudLine) (Object) visibleMessages.get(0)).vergence$setId(vergenceNext);
        }
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At(value = "RETURN", ordinal = 0, shift = At.Shift.AFTER))
    private void onAddMessageAfterNewChatHudLine(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        if (!messages.isEmpty()) {
            ((IChatHudLine) (Object) messages.get(0)).vergence$setId(vergenceNext);
        }
    }

    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", cancellable = true)
    private void onAddMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        ReceiveMessageEvent event = new ReceiveMessageEvent(message.getString());
        Vergence.EVENTBUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Redirect(method = {"render"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"))
    private int drawStringWithShadow(DrawContext context, TextRenderer textRenderer, OrderedText orderedText, int x, int y, int color) {
        if (BetterChat.INSTANCE == null || !BetterChat.INSTANCE.getStatus()) {
            return context.drawTextWithShadow(textRenderer, orderedText, x, y, color);
        }

        Text original = (Text) MessageManager.messages.get(orderedText);
        if (original == null || mc.player == null) {
            return context.drawTextWithShadow(textRenderer, orderedText, x, y, color);
        }

        String raw = original.getString();
        String playerName = mc.player.getName().getString();
        String prefix = "<";
        String suffix = ">";
        String chatPrefix = BetterChat.INSTANCE.chatPrefix.getValue();

        int alpha = (color >> 24) & 0xFF;
        int defaultColor = color;
        int nameColor = BetterChat.INSTANCE.customNameColor.getValue()
                ? ColorUtil.setAlpha(BetterChat.INSTANCE.playerNameColor.getValue(), alpha).getRGB()
                : defaultColor;
        int chatColor = BetterChat.INSTANCE.customChatColor.getValue()
                ? ColorUtil.setAlpha(BetterChat.INSTANCE.playerChatColor.getValue(), alpha).getRGB()
                : defaultColor;
        String namePattern1 = prefix + playerName + suffix;
        if (raw.startsWith(namePattern1)) {
            String chatContent = raw.substring(namePattern1.length());
            int nameWidth = context.drawTextWithShadow(textRenderer, Text.literal(namePattern1).asOrderedText(), x, y, nameColor);
            context.drawTextWithShadow(textRenderer, Text.literal(chatContent).asOrderedText(), x + nameWidth, y, chatColor);
            return nameWidth + textRenderer.getWidth(chatContent);
        }
        String namePattern2 = playerName + chatPrefix;
        if (raw.startsWith(namePattern2)) {
            String chatContent = raw.substring(namePattern2.length());
            int nameWidth = context.drawTextWithShadow(textRenderer, Text.literal(namePattern2).asOrderedText(), x, y, nameColor);
            context.drawTextWithShadow(textRenderer, Text.literal(chatContent).asOrderedText(), x + nameWidth, y, chatColor);
            return nameWidth + textRenderer.getWidth(chatContent);
        }
        if (raw.startsWith(MessageManager.SYNC)) {
            return context.drawTextWithShadow(
                    textRenderer,
                    orderedText,
                    x,
                    y,
                    ColorUtil.setAlpha(BetterChat.INSTANCE.prefixColor.getValue(), alpha).getRGB()
            );
        }
        return context.drawTextWithShadow(textRenderer, orderedText, x, y, color);
    }

    @Unique
    private final HashMap<ChatHudLine.Visible, FadeUtil> map = new HashMap<>();
    @Unique
    private ChatHudLine.Visible last;

    @ModifyArg(method = {"render"}, at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0, remap = false))
    private int get(int i) {
        if (BetterChat.INSTANCE != null && BetterChat.INSTANCE.getStatus()) {
            last = visibleMessages.get(i);
            if (last != null && !map.containsKey(last)) {
                map.put(last, new FadeUtil((long) BetterChat.INSTANCE.animationTime.getValue().floatValue()).reset());
            }
        }
        return i;
    }

    @Inject(method = {"render"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I", ordinal = 0, shift = At.Shift.BEFORE)})
    private void translate(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        if (map.containsKey(last) && BetterChat.INSTANCE != null && BetterChat.INSTANCE.getStatus()) {
            context.getMatrices().translate(BetterChat.INSTANCE.animationOffset.getValue() * (1 - map.get(last).getQuad((FadeUtil.Quad) BetterChat.INSTANCE.animationQuadType.getValue())), 0.0, 0.0f);
        }
    }
}