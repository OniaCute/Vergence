package cc.vergence.util.player;

import cc.vergence.features.event.events.MoveEvent;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.maths.MathUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
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

    public static void setMotionY(double y) {
        Vec3d motion = mc.player.getVelocity();
        mc.player.setVelocity(motion.getX(), y, motion.getZ());
    }

    public static void setMotionXZ(double x, double z) {
        Vec3d motion = mc.player.getVelocity();
        mc.player.setVelocity(x, motion.y, z);
    }

    public static Vec2f handleStrafeMotion(final float speed) {
        float forward = mc.player.input.movementForward;
        float strafe = mc.player.input.movementSideways;
        float yaw = mc.player.prevYaw + (mc.player.getYaw() - mc.player.prevYaw) * mc.getRenderTickCounter().getTickDelta(true);
        if (forward == 0.0f && strafe == 0.0f) {
            return Vec2f.ZERO;
        } else if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += forward > 0.0f ? -45 : 45;
                strafe = 0.0f;
            } else if (strafe <= -1.0f) {
                yaw += forward > 0.0f ? 45 : -45;
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        float rx = (float) Math.cos(Math.toRadians(yaw));
        float rz = (float) -Math.sin(Math.toRadians(yaw));
        return new Vec2f((forward * speed * rz) + (strafe * speed * rx), (forward * speed * rx) - (strafe * speed * rz));
    }

    public static void modifyEventSpeed(MoveEvent event, double d) {
        double d2 = mc.player.input.movementForward;
        double d3 = mc.player.input.movementSideways;
        float f = mc.player.getYaw();
        if (d2 == 0.0 && d3 == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (d2 != 0.0) {
                if (d3 > 0.0) {
                    f += (float) (d2 > 0.0 ? -45 : 45);
                } else if (d3 < 0.0) {
                    f += (float) (d2 > 0.0 ? 45 : -45);
                }

                d3 = 0.0;
                if (d2 > 0.0) {
                    d2 = 1.0;
                } else if (d2 < 0.0) {
                    d2 = -1.0;
                }
            }
            double sin = Math.sin(Math.toRadians(f + 90.0F));
            double cos = Math.cos(Math.toRadians(f + 90.0F));

            event.setX(d2 * d * cos + d3 * d * sin);
            event.setZ(d2 * d * sin - d3 * d * cos);
        }
    }

    public static double getBaseMoveSpeed() {
        int n;
        double d = 0.2873;

        if (Module.isNull()) {
            return d;
        }

        if (mc.player.hasStatusEffect(StatusEffects.SPEED)) {
            n = mc.player.getStatusEffect(StatusEffects.SPEED).getAmplifier();
            d *= 1.0 + 0.2 * (n + 1);
        }
        if (mc.player.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
            n = mc.player.getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier();
            d /= 1.0 + 0.2 * (n + 1);
        }
        if (mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) {
            n = mc.player.getStatusEffect(StatusEffects.SLOWNESS).getAmplifier();
            d /= 1.0 + (0.2 * (n + 1));
        }
        return d;
    }
}
