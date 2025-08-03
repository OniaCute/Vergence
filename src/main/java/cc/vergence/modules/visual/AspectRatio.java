package cc.vergence.modules.visual;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;

public class AspectRatio extends Module {
    public static AspectRatio INSTANCE;

    public AspectRatio() {
        super("AspectRatio", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Double> ratio = addOption(new DoubleOption("Ratio", 0.1, 5, 1.7172));

    @Override
    public String getDetails() {
        return "";
    }
}
