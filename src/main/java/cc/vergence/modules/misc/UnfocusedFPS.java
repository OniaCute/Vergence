package cc.vergence.modules.misc;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;

public class UnfocusedFPS extends Module {
    public static UnfocusedFPS INSTANCE;

    public UnfocusedFPS() {
        super("UnfocusedFPS", Category.MISC);
        INSTANCE = this;
    }

    public Option<Double> fpsLimit = addOption(new DoubleOption("FPSLimit", 5, 200, 30));

    @Override
    public String getDetails() {
        return fpsLimit.getValue() + "FPS";
    }
}
