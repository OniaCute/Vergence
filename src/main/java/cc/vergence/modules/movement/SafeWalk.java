package cc.vergence.modules.movement;

import cc.vergence.Vergence;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.RandomUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class SafeWalk extends Module implements Wrapper {
    public static SafeWalk INSTANCE;
    private boolean sneaking = false;
    private int sneakResendTicks = 0;

    public SafeWalk() {
        super("SafeWalk", Category.MOVEMENT);
        INSTANCE = this;
    }

    public Option<Boolean> doInject = addOption(new BooleanOption("DoInject", true));
    public Option<Boolean> doShift = addOption(new BooleanOption("DoShift", true));
    public Option<Boolean> onlyBack = addOption(new BooleanOption("OnlyBack", false, v -> doShift.getValue()));
    public Option<EnumSet<SneakModes>> sneakMode = addOption(new MultipleOption<SneakModes>("SneakMode", EnumSet.of(SneakModes.Client), v -> doShift.getValue()));
    public Option<Double> sneakDelay = addOption(new DoubleOption("SneakDelay", 1, 20, 7, v -> doShift.getValue()));
    public Option<Boolean> randomThreshold = addOption(new BooleanOption("RandomThreshold", true, v -> doShift.getValue()));
    public Option<Double> threshold = addOption(new DoubleOption("Threshold", 0.02, 0.45, 0.15, v -> doShift.getValue() && !randomThreshold.getValue()));
    public Option<Double> maxThreshold = addOption(new DoubleOption("MaxThreshold", 0.02, 0.45, 0.15, v -> doShift.getValue() && randomThreshold.getValue()));
    public Option<Double> minThreshold = addOption(new DoubleOption("MinThreshold", 0.02, 0.45, 0.08, v -> doShift.getValue() && randomThreshold.getValue()));

    @Override
    public String getDetails() {
        return doInject.getValue() ? "Unsafe" : doShift.getValue() ? "Legit" : "None" + " | " + (sneaking ? "Sneaking" : "Released");
    }

    @Override
    public void onTick() {
        safeWalkAction(
                doShift.getValue(),
                randomThreshold.getValue(),
                minThreshold.getValue(),
                maxThreshold.getValue(),
                threshold.getValue(),
                sneakMode.getValue(),
                sneakDelay.getValue(),
                onlyBack.getValue()
        );
    }

    public void safeWalkAction(boolean doShift, boolean randomThreshold, double minThreshold, double maxThreshold, double threshold, EnumSet<SneakModes> mode, double delay, boolean onlyBack) {
        if (mc.player == null || mc.world == null || !doShift) {
            return;
        }

        double edgeThreshold;

        if (randomThreshold) {
            if (minThreshold == maxThreshold) {
                edgeThreshold = minThreshold;
            } else {
                double min = minThreshold;
                double max = maxThreshold;
                if (min > max) {
                    double temp = min;
                    min = max;
                    max = temp;
                }
                edgeThreshold = RandomUtil.getDouble(min, max);
            }
        } else {
            edgeThreshold = threshold;
        }

        boolean nearEdge = isNearEdge(mc.player, edgeThreshold, onlyBack);
        if (nearEdge) {
            if (!sneaking) {
                startSneaking(mode);
            } else {
                if (++sneakResendTicks >= delay) {
                    resendSneaking(mode);
                    sneakResendTicks = 0;
                }
            }
        } else if (sneaking) {
            stopSneaking(mode);
        }
    }

    private boolean isNearEdge(ClientPlayerEntity player, double threshold, boolean onlyBack) {
        if (!player.isOnGround()) {
            return false;
        }

        Vec3d pos = player.getPos();
        Box box = player.getBoundingBox().offset(0, -0.05, 0);

        double[][] offsets;

        if (onlyBack) {
            float yaw = player.getYaw();
            double rad = Math.toRadians(yaw + 180); // back
            double dx = -Math.sin(rad) * threshold;
            double dz = Math.cos(rad) * threshold;

            offsets = new double[][] {
                    {dx, dz}
            };
        } else {
            offsets = new double[][] {
                    {-threshold, 0},
                    {threshold, 0},
                    {0, -threshold},
                    {0, threshold},
                    {-threshold, -threshold},
                    {-threshold, threshold},
                    {threshold, -threshold},
                    {threshold, threshold},
            };
        }

        for (double[] offset : offsets) {
            double checkX = pos.x + offset[0];
            double checkY = box.minY - 0.01;
            double checkZ = pos.z + offset[1];

            BlockPos checkPos = BlockPos.ofFloored(checkX, checkY, checkZ);
            BlockState state = mc.world.getBlockState(checkPos);

            if (!isSupportedBlock(state)) {
                return true;
            }
        }

        return false;
    }


    private boolean isSupportedBlock(BlockState state) {
        if (state.getCollisionShape(mc.world, BlockPos.ORIGIN).isEmpty()) {
            return false;
        }
        return state.isFullCube(mc.world, BlockPos.ORIGIN)
                || state.getBlock().getTranslationKey().contains("slab")
                || state.getBlock().getTranslationKey().contains("stairs");
    }

    private void startSneaking(EnumSet<SneakModes> mode) {
        if (!sneaking) {
            sneaking = true;
            sneakResendTicks = 0;
            if (mode.contains(SneakModes.Client)) {
                mc.player.setSneaking(true);
                mc.options.sneakKey.setPressed(true);
            }
            sendSneakPacket(true, mode);
        }
    }

    private void stopSneaking(EnumSet<SneakModes> mode) {
        if (sneaking) {
            sneaking = false;
            sneakResendTicks = 0;
            if (mode.contains(SneakModes.Client)) {
                mc.player.setSneaking(false);
                mc.options.sneakKey.setPressed(false);
            }
            sendSneakPacket(false, mode);
        }
    }

    private void resendSneaking(EnumSet<SneakModes> mode) {
        if (mc.player != null && sneaking) {
            sendSneakPacket(true, mode);
        }
    }

    @Override
    public void onDisable() {
        if (mc.player == null) {
            return ;
        }
        stopSneaking(sneakMode.getValue());
    }

    private void sendSneakPacket(boolean sneak, EnumSet<SneakModes> mode) {
        if (mc.getNetworkHandler() != null && mode.contains(SneakModes.Server)) {
            Vergence.NETWORK.sendPacket(new net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket(mc.player, sneak ? ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY : ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
        }
    }

    public enum SneakModes {
        Server,
        Client
    }
}