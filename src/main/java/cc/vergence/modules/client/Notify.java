package cc.vergence.modules.client;

import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class Notify extends Module {
    public static Notify INSTANCE;

    public Notify() {
        super("Notify", Category.CLIENT);
        INSTANCE = this;
    }

    public Option<Enum<?>> align = addOption(new EnumOption("Align", Aligns.Right));
    public Option<Enum<?>> popType = addOption(new EnumOption("PopType", PopTypes.DownToUp));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", true));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 1, 6, 3, v -> rounded.getValue()));
    public Option<Double> padding = addOption(new DoubleOption("Padding", 0, 10, 4));
    public Option<Double> aliveTimeWidth = addOption(new DoubleOption("AliveTimeWidth", 1, 3, 1));
    public Option<Double> notificationAliveTime = addOption(new DoubleOption("NotificationAliveTime", 40, 1200, 300));
    public Option<Double> animationTime = addOption(new DoubleOption("AnimationTime", 400, 1000, 600));
    public Option<Double> aliveTimeRadius = addOption(new DoubleOption("AliveTimeRadius", 0, 3, 1));
    public Option<Boolean> blur = addOption(new BooleanOption("Blur", false));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(241, 241, 241, 240)));
    public Option<Color> titleColor = addOption(new ColorOption("TitleColor", new Color(209, 46, 255)));
    public Option<Color> textColor = addOption(new ColorOption("TextColor", new Color(26, 26, 26)));
    public Option<Color> aliveTimeColor = addOption(new ColorOption("AliveTimeColor", new Color(255, 148, 253)));

    @Override
    public String getDetails() {
        return "";
    }

    public enum PopTypes {
        UpToDown,
        DownToUp
    }

    public enum Aligns {
        Left,
        Right
    }
}
