package cc.vergence.modules.movement;

import cc.vergence.features.event.events.MoveEvent;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import cc.vergence.util.player.MovementUtil;
import com.google.common.collect.Lists;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.shape.VoxelShape;

import java.util.List;

public class LongJump extends Module {
    public static LongJump INSTANCE;
    private int stage;
    private double distance;
    private double speed;
    private int airTicks;
    private int groundTicks;

    public LongJump() {
        super("LongJump", Category.MOVEMENT);
        INSTANCE = this;
    }

    public Option<Enum<?>> mode = addOption(new EnumOption("Mode", Modes.Normal));
    public Option<Double> boost = addOption(new DoubleOption("Boost", 0.1, 12, 4.5));
    public Option<Boolean> autoDisable = addOption(new BooleanOption("AutoDisable", true));
    public Option<Boolean> debug = addOption(new BooleanOption("Debug", false));

    @Override
    public String getDetails() {
        return debug.getValue() ? String.format("%.2f", speed) + " | " + stage : "";
    }

    @Override
    public void onMoveEvent(MoveEvent event, double x, double y, double z) {
        if (mc.player == null || mc.world == null || !MovementUtil.isInputtingMovement()) {
            return;
        }

        double speedEffect = 1.0;
        double slowEffect = 1.0;
        if (mc.player.hasStatusEffect(StatusEffects.SPEED)) {
            double amplifier = mc.player.getStatusEffect(StatusEffects.SPEED).getAmplifier();
            speedEffect = 1 + (0.2 * (amplifier + 1));
        }
        if (mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) {
            double amplifier = mc.player.getStatusEffect(StatusEffects.SLOWNESS).getAmplifier();
            slowEffect = 1 + (0.2 * (amplifier + 1));
        }
        final double base = 0.2873f * speedEffect / slowEffect;
        if (stage == 0) {
            stage = 1;
            speed = boost.getValue() * base - 0.01;
        } else if (stage == 1) {
            stage = 2;
            MovementUtil.setMotionY(0.42);
            event.setY(0.42);
            speed *= 2.149;
        } else if (stage == 2) {
            stage = 3;
            double moveSpeed = 0.66 * (distance - base);
            speed = distance - moveSpeed;
        } else {
            if (!mc.world.isSpaceEmpty(mc.player, mc.player.getBoundingBox().offset(0,
                    mc.player.getVelocity().getY(), 0)) || mc.player.verticalCollision) {
                stage = 0;
            }
            speed = distance - distance / 159.0;
        }
        speed = Math.max(speed, base);
        event.cancel();
        Vec2f motion = MovementUtil.handleStrafeMotion((float) speed);
        event.setX(motion.x);
        event.setZ(motion.y);
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (mode.getValue().equals(Modes.Glide)) {
            if (mc.player.isGliding() || mc.player.isHoldingOntoLadder() || mc.player.isTouchingWater()) {
                return;
            }
            if (mc.player.isOnGround()) {
                distance = 0.0;
            }
            final float direction = mc.player.getYaw() + ((mc.player.forwardSpeed < 0.0f) ? 180 : 0) + ((mc.player.sidewaysSpeed > 0.0f) ? (-90.0f * ((mc.player.forwardSpeed < 0.0f) ? -0.5f : ((mc.player.forwardSpeed > 0f) ? 0.5f : 1.0f))) : 0.0f) - ((mc.player.sidewaysSpeed < 0.0f) ? (-90.0f * ((mc.player.forwardSpeed < 0.0f) ? -0.5f : ((mc.player.forwardSpeed > 0.0f) ? 0.5f : 1.0f))) : 0.0f);
            final float dx = (float) Math.cos((direction + 90.0f) * Math.PI / 180.0);
            final float dz = (float) Math.sin((direction + 90.0f) * Math.PI / 180.0);
            if (!mc.player.verticalCollision) {
                airTicks++;
                if (mc.player.isSneaking()) {
                    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(0.0, 2.147483647e9, 0.0, false, mc.player.horizontalCollision));
                }
                groundTicks = 0;
                if (!mc.player.verticalCollision) {
                    if (mc.player.getVelocity().y == -0.07190068807140403) {
                        MovementUtil.setMotionY(mc.player.getVelocity().y * 0.35f);
                    }
                    if (mc.player.getVelocity().y == -0.10306193759436909) {
                        MovementUtil.setMotionY(mc.player.getVelocity().y * 0.55f);
                    }
                    if (mc.player.getVelocity().y == -0.13395038817442878) {
                        MovementUtil.setMotionY(mc.player.getVelocity().y * 0.67f);
                    }
                    if (mc.player.getVelocity().y == -0.16635183030382) {
                        MovementUtil.setMotionY(mc.player.getVelocity().y * 0.69f);
                    }
                    if (mc.player.getVelocity().y == -0.19088711097794803) {
                        MovementUtil.setMotionY(mc.player.getVelocity().y * 0.71f);
                    }
                    if (mc.player.getVelocity().y == -0.21121925191528862) {
                        MovementUtil.setMotionY(mc.player.getVelocity().y * 0.2f);
                    }
                    if (mc.player.getVelocity().y == -0.11979897632390576) {
                        MovementUtil.setMotionY(mc.player.getVelocity().y * 0.93f);
                    }
                    if (mc.player.getVelocity().y == -0.18758479151225355) {
                        MovementUtil.setMotionY(mc.player.getVelocity().y * 0.72f);
                    }
                    if (mc.player.getVelocity().y == -0.21075983825251726) {
                        MovementUtil.setMotionY(mc.player.getVelocity().y * 0.76f);
                    }
                    if (getJumpCollisions(mc.player, 70.0) < 0.5) {
                        if (mc.player.getVelocity().y == -0.23537393014173347) {
                            MovementUtil.setMotionY(mc.player.getVelocity().y * 0.03f);
                        }
                        if (mc.player.getVelocity().y == -0.08531999505205401) {
                            MovementUtil.setMotionY(mc.player.getVelocity().y * -0.5);
                        }
                        if (mc.player.getVelocity().y == -0.03659320313669756) {
                            MovementUtil.setMotionY(mc.player.getVelocity().y * -0.1f);
                        }
                        if (mc.player.getVelocity().y == -0.07481386749524899) {
                            MovementUtil.setMotionY(mc.player.getVelocity().y * -0.07f);
                        }
                        if (mc.player.getVelocity().y == -0.0732677700939672) {
                            MovementUtil.setMotionY(mc.player.getVelocity().y * -0.05f);
                        }
                        if (mc.player.getVelocity().y == -0.07480988066790395) {
                            MovementUtil.setMotionY(mc.player.getVelocity().y * -0.04f);
                        }
                        if (mc.player.getVelocity().y == -0.0784000015258789) {
                            MovementUtil.setMotionY(mc.player.getVelocity().y * 0.1f);
                        }
                        if (mc.player.getVelocity().y == -0.08608320193943977) {
                            MovementUtil.setMotionY(mc.player.getVelocity().y * 0.1f);
                        }
                        if (mc.player.getVelocity().y == -0.08683615560584318) {
                            MovementUtil.setMotionY(mc.player.getVelocity().y * 0.05f);
                        }
                        if (mc.player.getVelocity().y == -0.08265497329678266) {
                            MovementUtil.setMotionY(mc.player.getVelocity().y * 0.05f);
                        }
                        if (mc.player.getVelocity().y == -0.08245009535659828) {
                            MovementUtil.setMotionY(mc.player.getVelocity().y * 0.05f);
                        }
                        if (mc.player.getVelocity().y == -0.08244005633718426) {
                            MovementUtil.setMotionY(-0.08243956442521608);
                        }
                        if (mc.player.getVelocity().y == -0.08243956442521608) {
                            MovementUtil.setMotionY(-0.08244005590677261);
                        }
                        if (mc.player.getVelocity().y > -0.1
                                && mc.player.getVelocity().y < -0.08
                                && !mc.player.isOnGround()
                                && mc.options.forwardKey.isPressed()) {
                            MovementUtil.setMotionY(-1.0e-4f);
                        }
                    } else {
                        if (mc.player.getVelocity().y < -0.2
                                && mc.player.getVelocity().y > -0.24) {
                            MovementUtil.setMotionY(mc.player.getVelocity().y * 0.7);
                        }
                        if (mc.player.getVelocity().y < -0.25
                                && mc.player.getVelocity().y > -0.32) {
                            MovementUtil.setMotionY(mc.player.getVelocity().y * 0.8);
                        }
                        if (mc.player.getVelocity().y < -0.35
                                && mc.player.getVelocity().y > -0.8) {
                            MovementUtil.setMotionY(mc.player.getVelocity().y * 0.98);
                        }
                        if (mc.player.getVelocity().y < -0.8
                                && mc.player.getVelocity().y > -1.6) {
                            MovementUtil.setMotionY(mc.player.getVelocity().y * 0.99);
                        }
                    }
                }
//                Managers.TICK.setClientTick(0.85f);
                double[] jumpFactor = new double[]
                        {
                                0.420606, 0.417924, 0.415258, 0.412609,
                                0.409977, 0.407361, 0.404761, 0.402178,
                                0.399611, 0.39706, 0.394525, 0.392, 0.3894,
                                0.38644, 0.383655, 0.381105, 0.37867, 0.37625,
                                0.37384, 0.37145, 0.369, 0.3666, 0.3642, 0.3618,
                                0.35945, 0.357, 0.354, 0.351, 0.348, 0.345,
                                0.342, 0.339, 0.336, 0.333, 0.33, 0.327, 0.324,
                                0.321, 0.318, 0.315, 0.312, 0.309, 0.307,
                                0.305, 0.303, 0.3, 0.297, 0.295, 0.293, 0.291,
                                0.289, 0.287, 0.285, 0.283, 0.281, 0.279, 0.277,
                                0.275, 0.273, 0.271, 0.269, 0.267, 0.265, 0.263,
                                0.261, 0.259, 0.257, 0.255, 0.253, 0.251, 0.249,
                                0.247, 0.245, 0.243, 0.241, 0.239, 0.237
                        };
                if (mc.options.forwardKey.isPressed()) {
                    try {
                        MovementUtil.setMotionXZ((double) dx * jumpFactor[airTicks - 1] * 3.0,
                                (double) dz * jumpFactor[airTicks - 1] * 3.0);
                    } catch (ArrayIndexOutOfBoundsException ignored) {

                    }
                    return;
                }
                MovementUtil.setMotionXZ(0.0, 0.0);
                return;
            }
//            Managers.TICK.setClientTick(1.0f);
            airTicks = 0;
            groundTicks++;
            MovementUtil.setMotionXZ(mc.player.getVelocity().x / 13.0,
                    mc.player.getVelocity().z / 13.0);
            if (groundTicks == 1) {
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        mc.player.getX(), mc.player.getY(),
                        mc.player.getZ(), mc.player.isOnGround(), mc.player.horizontalCollision));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        mc.player.getX() + 0.0624, mc.player.getY(),
                        mc.player.getZ(), mc.player.isOnGround(), mc.player.horizontalCollision));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        mc.player.getX(), mc.player.getY() + 0.419,
                        mc.player.getZ(), mc.player.isOnGround(), mc.player.horizontalCollision));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        mc.player.getX() + 0.0624, mc.player.getY(),
                        mc.player.getZ(), mc.player.isOnGround(), mc.player.horizontalCollision));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        mc.player.getX(), mc.player.getY() + 0.419,
                        mc.player.getZ(), mc.player.isOnGround(), mc.player.horizontalCollision));
            }
            if (groundTicks > 2) {
                groundTicks = 0;
                MovementUtil.setMotionXZ(dx * 0.3, dz * 0.3);
                MovementUtil.setMotionY(0.42399999499320984);
            }
        }
    }

    private double getJumpCollisions(PlayerEntity player, double d) {
//        List<VoxelShape> collisions = Lists.newArrayList(mc.world.getCollisions(player, player.getBoundingBox().expand(0.0, -d, 0.0)));
//        if (collisions.isEmpty()) {
//            return 0.0;
//        }
//        d = 0.0;
//        for (VoxelShape coll : collisions) {
//            Box bb = coll.getBoundingBox();
//            if (bb.maxY <= d) {
//                continue;
//            }
//            d = bb.maxY;
//        }
//        return player.getY() - d;
        return 1.0;
    }

    public enum Modes {
        Normal,
        Glide
    }
}
