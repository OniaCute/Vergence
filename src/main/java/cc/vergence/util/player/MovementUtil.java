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

    public static void setMotion(double speed) {
        double forward = mc.player.input.movementForward;
        double strafe = mc.player.input.movementSideways;
        float yaw = mc.player.getYaw();
        if (forward == 0 && strafe == 0) {
            mc.player.setVelocity(0, mc.player.getVelocity().y, 0);
        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += (float) (forward > 0 ? -45 : 45);
                } else if (strafe < 0) {
                    yaw += (float) (forward > 0 ? 45 : -45);
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else if (forward < 0) {
                    forward = -1;
                }
            }
            double sin = MathHelper.sin((float) Math.toRadians(yaw + 90));
            double cos = MathHelper.cos((float) Math.toRadians(yaw + 90));
            mc.player.setVelocity(forward * speed * cos + strafe * speed * sin, mc.player.getVelocity().y, forward * speed * sin - strafe * speed * cos);
        }
    }

    public static float getMoveDirection() {
        double forward = mc.player.input.movementForward;
        double strafe = mc.player.input.movementSideways;

        if (strafe > 0) {
            strafe = 1;
        } else if (strafe < 0) {
            strafe = -1;
        }

        float yaw = mc.player.getYaw();
        if (forward == 0 && strafe == 0) {
            return yaw;
        } else {
            if (forward != 0) {
                if (strafe > 0)
                    yaw += forward > 0 ? -45f : -135f;
                else if (strafe < 0)
                    yaw += forward > 0 ? 45f : 135f;
                else if (forward < 0) {
                    yaw += 180f;
                }
            }
            if (forward == 0) {
                if (strafe > 0)
                    yaw -= 90f;
                else if (strafe < 0)
                    yaw += 90f;
            }
        }

        return yaw;
    }

    public static Vec2f applySafeWalk(final double motionX, final double motionZ) {
        final double offset = 0.05;

        double moveX = motionX;
        double moveZ = motionZ;

        float fallDist = -mc.player.getStepHeight();
        if (!mc.player.isOnGround())
        {
            fallDist = -1.5f;
        }

        while(moveX != 0.0 && mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().offset(moveX, fallDist, 0.0))) {
            if (moveX < offset && moveX >= -offset) {
                moveX = 0.0;
            } else if (moveX > 0.0) {
                moveX -= offset;
            } else {
                moveX += offset;
            }
        }

        while(moveZ != 0.0 && mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().offset(0.0, fallDist, moveZ))) {
            if (moveZ < offset && moveZ >= -offset) {
                moveZ = 0.0;
            } else if (moveZ > 0.0) {
                moveZ -= offset;
            } else {
                moveZ += offset;
            }
        }

        while(moveX != 0.0 && moveZ != 0.0 && mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().offset(moveX, fallDist, moveZ))) {
            if (moveX < offset && moveX >= -offset) {
                moveX = 0.0;
            } else if (moveX > 0.0) {
                moveX -= offset;
            } else {
                moveX += offset;
            }

            if (moveZ < offset && moveZ >= -offset) {
                moveZ = 0.0;
            } else if (moveZ > 0.0) {
                moveZ -= offset;
            } else {
                moveZ += offset;
            }
        }
        return new Vec2f((float) moveX, (float) moveZ);
    }

    public static double[] forwardWithoutStrafe(final double d) {
        float f3 = mc.player.getYaw();
        final double d4 = d * Math.cos(Math.toRadians(f3 + 90.0f));
        final double d5 = d * Math.sin(Math.toRadians(f3 + 90.0f));
        return new double[]{d4, d5};
    }

    public static double getJumpSpeed() {
        double jumpSpeed = 0.3999999463558197;
        if (mc.player.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
            double amplifier = mc.player.getStatusEffect(StatusEffects.JUMP_BOOST).getAmplifier();
            jumpSpeed += (amplifier + 1) * 0.1;
        }
        return jumpSpeed;
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

        if (Module.isNull()) return d;

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

    public static boolean sprintIsLegit(float yaw) {
        return (Math.abs(Math.abs(MathHelper.wrapDegrees(yaw)) - Math.abs(MathHelper.wrapDegrees(mc.player.getYaw()))) < 40);
    }
}
