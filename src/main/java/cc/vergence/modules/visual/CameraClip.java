package cc.vergence.modules.visual;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.animations.CameraAnimation;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.math.MatrixStack;

public class CameraClip extends Module {
    public static CameraClip INSTANCE;
    private float animation;

    public CameraClip() {
        super("CameraClip", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Double> distance = addOption(new DoubleOption("Distance", 1, 30, 4).addSpecialValue(4, "DEFAULT"));

    @Override
    public String getDetails() {
        return String.valueOf(getDistance());
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if (mc.options.getPerspective() == Perspective.FIRST_PERSON) {
            animation = CameraAnimation.fast(animation, 0f, 10);
        } else {
            animation = CameraAnimation.fast(animation, 1f, 10);
        }

        // Front disabled
//        if (mc.options.getPerspective() == Perspective.THIRD_PERSON_FRONT) {
//            mc.options.setPerspective(Perspective.FIRST_PERSON);
//        }
    }

    public double getDistance() {
        return 1f + ((distance.getValue() - 1f) * animation);
    }
}
