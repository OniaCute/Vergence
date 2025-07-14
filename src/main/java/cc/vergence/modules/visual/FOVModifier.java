package cc.vergence.modules.visual;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;

public class FOVModifier extends Module {
    public static FOVModifier INSTANCE;
    public FOVModifier() {
        super("FOVModifier", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Double> fov = addOption(new DoubleOption("FOV", 30, 180, 110));
    public Option<Boolean> forItem = addOption(new BooleanOption("Items", true));

    @Override
    public String getDetails() {
        return String.valueOf(fov.getValue().intValue());
    }
}
