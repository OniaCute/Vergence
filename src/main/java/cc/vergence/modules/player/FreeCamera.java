package cc.vergence.modules.player;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.maths.MathUtil;
import cc.vergence.util.player.MovementUtil;
import net.minecraft.util.PlayerInput;
import org.joml.Vector2d;

public class FreeCamera extends Module {
    public static FreeCamera INSTANCE;
    private float freeYaw, freePitch;
    private float prevFreeYaw, prevFreePitch;
    private double freeX, freeY, freeZ;
    private double prevFreeX, prevFreeY, prevFreeZ;

    public FreeCamera() {
        super("FreeCamera", Category.PLAYER);
        INSTANCE = this;
    }

    public Option<Double> horizontalSpeed = addOption(new DoubleOption("HorizontalSpeed", 0.1, 4.0, 1.0));
    public Option<Double> verticalSpeed = addOption(new DoubleOption("VerticalSpeed", 0.1, 4.0, 0.5));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onKeyboardActive(int key, int action) {
        if (mc.player == null) return;

        Vector2d motion = MovementUtil.forward(horizontalSpeed.getValue());

        prevFreeX = freeX;
        prevFreeY = freeY;
        prevFreeZ = freeZ;

        freeX += motion.x;
        freeZ += motion.y;

        if (mc.options.jumpKey.isPressed()) freeY += verticalSpeed.getValue();
        if (mc.options.sneakKey.isPressed()) freeY -= verticalSpeed.getValue();

        mc.player.input.playerInput = new PlayerInput(mc.player.input.playerInput.forward(), mc.player.input.playerInput.backward(), mc.player.input.playerInput.left(), mc.player.input.playerInput.right(), false, false, false);
        mc.player.input.movementForward = 0;
        mc.player.input.movementSideways = 0;
    }

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) {
            disable();
            return;
        }

        mc.chunkCullingEnabled = false;

        freeYaw = prevFreeYaw = mc.player.getYaw();
        freePitch = prevFreePitch = mc.player.getPitch();

        freeX = prevFreeX = mc.player.getX();
        freeY = prevFreeY = mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose());
        freeZ = prevFreeZ = mc.player.getZ();
    }

    @Override
    public void onDisable() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        mc.chunkCullingEnabled = true;
    }

    public float getFreeYaw() {
        return (float) MathUtil.interpolate(prevFreeYaw, freeYaw, mc.getRenderTickCounter().getTickDelta(true));
    }

    public float getFreePitch() {
        return (float) MathUtil.interpolate(prevFreePitch, freePitch, mc.getRenderTickCounter().getTickDelta(true));
    }

    public double getFreeX() {
        return MathUtil.interpolate(prevFreeX, freeX, mc.getRenderTickCounter().getTickDelta(true));
    }

    public double getFreeY() {
        return MathUtil.interpolate(prevFreeY, freeY, mc.getRenderTickCounter().getTickDelta(true));
    }

    public double getFreeZ() {
        return MathUtil.interpolate(prevFreeZ, freeZ, mc.getRenderTickCounter().getTickDelta(true));
    }
}
