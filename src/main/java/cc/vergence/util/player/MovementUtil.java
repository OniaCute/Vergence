package cc.vergence.util.player;

import cc.vergence.features.event.events.MoveEvent;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.maths.MathUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import org.joml.Vector2d;

public class MovementUtil implements Wrapper {
    public static boolean isMoving() {
        return mc.player != null && mc.world != null && mc.player.input != null && (mc.player.input.movementForward != 0.0 || mc.player.input.movementSideways != 0.0);
    }

    public static boolean isInputtingMovement() {
        return mc.player.input.playerInput.forward()
                || mc.player.input.playerInput.backward()
                || mc.player.input.playerInput.left()
                || mc.player.input.playerInput.right();
    }

    public static double getSpeed() {
        return Math.hypot(mc.player.getVelocity().x, mc.player.getVelocity().z);
    }

    public static Vector2d forward(double speed) {
        float forward = mc.player.input.movementForward;
        float sideways = mc.player.input.movementSideways;
        float yaw = mc.player.getYaw();

        if (forward == 0.0f && sideways == 0.0f) return new Vector2d(0, 0);
        if (forward != 0.0f) {
            if (sideways >= 1.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
                sideways = 0.0f;
            } else if (sideways <= -1.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
                sideways = 0.0f;
            }

            if (forward > 0.0f) forward = 1.0f;
            else if (forward < 0.0f) forward = -1.0f;
        }

        double motionX = Math.cos(Math.toRadians(yaw + 90.0f));
        double motionZ = Math.sin(Math.toRadians(yaw + 90.0f));

        return new Vector2d(forward * speed * motionX + sideways * speed * motionZ, forward * speed * motionZ - sideways * speed * motionX);
    }
}
