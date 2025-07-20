package cc.vergence.modules.visual;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;

public class HandModifier extends Module {
    public static HandModifier INSTANCE;

    public HandModifier() {
        super("HandModifier", Category.VISUAL);
    }

    public Option<Boolean> noEatingAnimation = addOption(new BooleanOption("NoEatingAnimation", false));
    public Option<Boolean> mainHandModify = addOption(new BooleanOption("MainHandModify", false));
    public Option<Double> mainHandProgress = addOption(new DoubleOption("MainHandProgress", -1, 1, 1,  v -> mainHandModify.getValue()));
    public Option<Boolean> offHandModify = addOption(new BooleanOption("OffHandModify", false));
    public Option<Double> offHandProgress = addOption(new DoubleOption("OffHandProgress", -1, 1, 1, v -> offHandModify.getValue()));
    public Option<Double> translateX = addOption(new DoubleOption("TranslateX", -2, 2, 0));
    public Option<Double> translateY = addOption(new DoubleOption("TranslateY", -2, 2, 0));
    public Option<Double> translateZ = addOption(new DoubleOption("TranslateZ", -2, 2, 0));
    public Option<Double> rotationX = addOption(new DoubleOption("RotationX", -180, 180, 0));
    public Option<Double> rotationY = addOption(new DoubleOption("RotationY", -180, 180, 0));
    public Option<Double> rotationZ = addOption(new DoubleOption("RotationZ", -180, 180, 0));
    public Option<Double> scaleX = addOption(new DoubleOption("ScaleX", 0.1, 3, 1));
    public Option<Double> scaleY = addOption(new DoubleOption("ScaleY", 0.1, 3, 1));
    public Option<Double> scaleZ = addOption(new DoubleOption("ScaleZ", 0.1, 3, 1));

    @Override
    public String getDetails() {
        return "";
    }
}
