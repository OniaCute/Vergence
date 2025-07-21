package cc.vergence.modules.visual;

import cc.vergence.features.enums.Hands;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;

import java.util.EnumSet;

public class SwingModifier extends Module {
    public static SwingModifier INSTANCE;

    public SwingModifier() {
        super("SwingModifier", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Double> speed = addOption(new DoubleOption("Speed", 1, 20, 15));
    public Option<Boolean> translateX = addOption(new BooleanOption("TranslateX", true));
    public Option<Boolean> translateY = addOption(new BooleanOption("TranslateY", true));
    public Option<Boolean> translateZ = addOption(new BooleanOption("TranslateZ", true));
    public Option<Boolean> rotationX = addOption(new BooleanOption("RotationX", true));
    public Option<Boolean> rotationY = addOption(new BooleanOption("RotationY", true));
    public Option<Boolean> rotationZ = addOption(new BooleanOption("RotationZ", true));

    @Override
    public String getDetails() {
        return "";
    }
}
