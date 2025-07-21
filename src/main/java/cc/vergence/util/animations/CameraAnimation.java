package cc.vergence.util.animations;

import cc.vergence.util.maths.FrameRateCounter;
import cc.vergence.util.maths.MathUtil;

public class CameraAnimation {
    public static float fast(float end, float start, float multiple) {
        float clampedDelta = MathUtil.clamp(deltaTime() * multiple, 0f, 1f);
        return (1f - clampedDelta) * end + clampedDelta * start;
    }

    public static float deltaTime() {
        return FrameRateCounter.INSTANCE.getFps() > 5 ? (1f / FrameRateCounter.INSTANCE.getFps()) : 0.016f;
    }
}
