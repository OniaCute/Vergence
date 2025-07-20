package cc.vergence.modules.visual;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;

public class CameraClip extends Module {
    public static CameraClip INSTANCE;

    public CameraClip() {
        super("CameraClip", Category.VISUAL);
    }

    public Option<Double> distance = addOption(new DoubleOption("Distance", 0, 5, 1).addSpecialValue(0, "DEFAULT"));

    @Override
    public String getDetails() {
        return distance.getValue().intValue() == 0 ? "" : distance.getValue().toString();
    }
}
