package cc.vergence.modules.movement;

import cc.vergence.Vergence;
import cc.vergence.features.managers.MessageManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.RandomUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

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
    public Option<Boolean> randomThreshold = addOption(new BooleanOption("RandomThreshold", true, v -> doShift.getValue()));
    public Option<Double> threshold = addOption(new DoubleOption("Threshold", 0.05, 0.3, 0.15, v -> doShift.getValue() && !randomThreshold.getValue()));
    public Option<Double> maxThreshold = addOption(new DoubleOption("MaxThreshold", 0.05, 0.3, 0.15, v -> doShift.getValue() && randomThreshold.getValue()));
    public Option<Double> minThreshold = addOption(new DoubleOption("MinThreshold", 0.05, 0.3, 0.08, v -> doShift.getValue() && randomThreshold.getValue()));

    @Override
    public String getDetails() {
        return doInject.getValue() ? "Unsafe" : doShift.getValue() ? "Legit" : "None" + " | " + (sneaking ? "Sneaking" : "Released");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || !doShift.getValue()) return;

        double edgeThreshold;

        if (randomThreshold.getValue()) {
            if (minThreshold.getValue().equals(maxThreshold.getValue())) {
                edgeThreshold = minThreshold.getValue();
            } else {
                double min = minThreshold.getValue();
                double max = maxThreshold.getValue();
                if (min > max) {
                    double temp = min;
                    min = max;
                    max = temp;
                }
                edgeThreshold = RandomUtil.getDouble(min, max);
            }
        } else {
            edgeThreshold = threshold.getValue();
        }

        boolean nearEdge = isNearEdge(mc.player, edgeThreshold);
        if (nearEdge) {
            if (!sneaking) {
                startSneaking();
            } else {
                if (++sneakResendTicks >= 6) {
                    resendSneaking();
                    sneakResendTicks = 0;
                }
            }
        } else if (sneaking) {
            stopSneaking();
        }
    }

    private boolean isNearEdge(ClientPlayerEntity player, double threshold) {
        if (!player.isOnGround()) {
            return false;
        }

        Vec3d pos = player.getPos();
        Box box = player.getBoundingBox().offset(0, -0.05, 0);

        for (double dx = -threshold; dx <= threshold; dx += threshold) {
            for (double dz = -threshold; dz <= threshold; dz += threshold) {
                double checkX = pos.x + dx;
                double checkY = box.minY - 0.01;
                double checkZ = pos.z + dz;

                BlockPos checkPos = BlockPos.ofFloored(checkX, checkY, checkZ);

                BlockState state = mc.world.getBlockState(checkPos);
                if (isSupportedBlock(state)) {
                    continue;
                }
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

    private void startSneaking() {
        if (!sneaking) {
            sneaking = true;
            sneakResendTicks = 0;
            mc.player.setSneaking(true);
            mc.options.sneakKey.setPressed(true);
            sendSneakPacket(true);
        }
    }

    private void stopSneaking() {
        if (sneaking) {
            sneaking = false;
            sneakResendTicks = 0;

            mc.player.setSneaking(false);
            mc.options.sneakKey.setPressed(false);
            sendSneakPacket(false);
        }
    }

    private void resendSneaking() {
        if (mc.player != null && sneaking) {
            sendSneakPacket(true);
        }
    }

    private void sendSneakPacket(boolean sneak) {
        if (mc.getNetworkHandler() != null) {
            Vergence.NETWORK.sendPacket(
                    new net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket(
                            mc.player,
                            sneak ?
                                    ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY :
                                    ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY
                    )
            );
        }
    }
}