package cc.vergence.modules.client;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.SendMessageEvent;
import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.modules.Module;
import cc.vergence.util.render.utils.FadeUtil;
import cc.vergence.util.render.utils.NewRender2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Objects;

public class BetterChat extends Module {
    public static BetterChat INSTANCE;

    public BetterChat() {
        super("BetterChat", Category.CLIENT);
        INSTANCE = this;
    }

    public Option<Enum<?>> prefix = addOption(new EnumOption("Prefix", MessagePrefixTypes.Default));
    public Option<Color> prefixColor = addOption(new ColorOption("PrefixColor", Vergence.THEME.getTheme().getMainColor()));
    public Option<Boolean> keepHistory = addOption(new BooleanOption("KeepHistory", true));
    public Option<Boolean> blur = addOption(new BooleanOption("Blur", true));
    public Option<String> chatPrefix = addOption(new TextOption("ChatPrefix", ""));
    public Option<String> chatSuffix = addOption(new TextOption("ChatSuffix", " | Vergence"));
    public Option<Boolean> customNameColor = addOption(new BooleanOption("CustomNameColor", true));
    public Option<Color> playerNameColor = addOption(new ColorOption("PlayerNameColor", new Color(162, 77, 255), v -> customNameColor.getValue()));
    public Option<Boolean> customChatColor = addOption(new BooleanOption("CustomChatColor", true));
    public Option<Color> playerChatColor = addOption(new ColorOption("PlayerChatColor", new Color(212, 95, 232), v -> customChatColor.getValue()));
    public Option<Double> animationTime = addOption(new DoubleOption("AnimationTime", 100, 700, 450));
    public Option<Double> animationOffset = addOption(new DoubleOption("AnimationOffset", -150, 150, -30));
    public Option<Enum<?>> animationQuadType = addOption(new EnumOption("AnimationQuadType", FadeUtil.Quad.QuadOut));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDrawSkia(DrawContext context, float tickDelta) {
        if (!blur.getValue() || !(mc.currentScreen instanceof ChatScreen)) {
            return ;
        }

        int chatWidth = (int) (getChatWidth() + 12);
        int chatHeight = (int) ((getChatHeight() + mc.options.getChatLineSpacing().getValue()) * mc.options.getChatScale().getValue());
        int startY = (int) (mc.getWindow().getScaledHeight() - chatHeight - 40);
        NewRender2DUtil.drawBlur(
                0,
                startY,
                chatWidth,
                chatHeight
        );
    }

    @Override
    public void onSendMessageEvent(SendMessageEvent event, String message) {
        if (isNull() || event.isCancel()) {
            return;
        }
        if (message.startsWith("/") || message.startsWith(Vergence.PREFIX)) {
            return;
        }

        message = chatPrefix.getValue() + message + chatSuffix.getValue();

        event.message = message;
    }

    private int getChatWidth() {
        return getChatWidth((Double) mc.options.getChatWidth().getValue());
    }

    public int getChatHeight() {
        return getChatHeight((Double) mc.options.getChatHeightFocused().getValue());
    }

    public int getChatWidth(double widthOption) {
        return MathHelper.floor(widthOption * (double) 280.0F + 40);
    }

    public int getChatHeight(double heightOption) {
        return MathHelper.floor(heightOption * (double) 160.0F + 20);
    }

    public enum MessagePrefixTypes {
        Default,
        Line
    }
}
