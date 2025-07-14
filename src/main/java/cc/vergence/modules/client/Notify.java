package cc.vergence.modules.client;

import cc.vergence.features.enums.AntiCheats;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;

public class Notify extends Module {
    public static Notify INSTANCE;

    public Notify() {
        super("Notify", Category.CLIENT);
        INSTANCE = this;
    }

    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", true));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 1, 6, 3, v -> rounded.getValue()));
    public Option<Double> notificationAliveTime = addOption(new DoubleOption("NotificationAliveTime", 100, 1000, 300));
    public Option<Double> animationSpeed = addOption(new DoubleOption("AnimationSpeed", 300, 800, 400));

    @Override
    public String getDetails() {
        return "";
    }
}
