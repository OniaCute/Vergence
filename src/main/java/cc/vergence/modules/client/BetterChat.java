package cc.vergence.modules.client;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.SendMessageEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.modules.Module;
import cc.vergence.util.render.utils.FadeUtil;

import java.awt.*;

public class BetterChat extends Module {
    public static BetterChat INSTANCE;

    public BetterChat() {
        super("BetterChat", Category.CLIENT);
        INSTANCE = this;
    }

    public Option<Enum<?>> prefix = addOption(new EnumOption("Prefix", MessagePrefixTypes.Default));
    public Option<Color> prefixColor = addOption(new ColorOption("PrefixColor", Vergence.THEME.getTheme().getChatPrefixColor()));
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

    public enum MessagePrefixTypes {
        Default,
        Line
    }
}
