package cc.vergence.modules.movement;

import cc.vergence.Vergence;
import cc.vergence.features.enums.*;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.blocks.BlockUtil;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.player.InteractionUtil;
import cc.vergence.util.player.InventoryUtil;
import cc.vergence.util.rotation.Rotation;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class Scaffold extends Module {
    public static Scaffold INSTANCE;
    private final FastTimerUtil timer = new FastTimerUtil();
    private Rotation lastRotation;

    public Scaffold() {
        super("Scaffold", Category.MOVEMENT);
        INSTANCE = this;
        timer.reset();
    }

    public Option<Boolean> allowSprint = addOption(new BooleanOption("Sprint", false));
    public Option<Boolean> doRotate = addOption(new BooleanOption("Rotate", true));
    public Option<Enum<?>> rotateMode = addOption(new EnumOption("RotateMode", RotateModes.Both, v -> doRotate.getValue()));
    public Option<Double> rotateSpeed = addOption(new DoubleOption("RotateSpeed", 1, 180, 30).addSpecialValue(180, "INSTANT").setUnit("°"));
    public Option<Boolean> enableRotateRandom = addOption(new BooleanOption("EnableRotateRandom", true, v -> doRotate.getValue()));
    public Option<Double> rotateRandomYaw = addOption(new DoubleOption("RotateRandomYaw", 0, 3, 0.7, v -> enableRotateRandom.getValue()));
    public Option<Double> rotateRandomPitch = addOption(new DoubleOption("RotateRandomPitch", 0, 3, 0.7, v -> enableRotateRandom.getValue()));
    public Option<Boolean> doSwing = addOption(new BooleanOption("Swing", true));
    public Option<Boolean> enableSwing = addOption(new BooleanOption("EnableSwing", true, v -> doSwing.getValue()));
    public Option<Enum<?>> swingMode = addOption(new EnumOption("SwingMode", SwingModes.Both, v -> enableSwing.getValue()));
    public Option<Enum<?>> swingHand = addOption(new EnumOption("SwingHand", Hands.MainHand, v -> enableSwing.getValue()));
    public Option<Enum<?>> placeMode = addOption(new EnumOption("PlaceMode", PlaceModes.Legit));
    public Option<Boolean> randomPlaceDelay = addOption(new BooleanOption("RandomPlaceDelay", true));
    public Option<Double> placeDelayMin = addOption(new DoubleOption("PlaceDelayMin", 50, 600, 200).setUnit("ms"));
    public Option<Double> placeDelayMax = addOption(new DoubleOption("PlaceDelayMax", 100, 800, 350).setUnit("ms"));
    public Option<Double> placeableRange = addOption(new DoubleOption("PlaceableRange", 1, 4, 2, v -> !placeMode.getValue().equals(PlaceModes.AirPlace)));
    public Option<Double> rotateYawOffset = addOption(new DoubleOption("RotateYawOffset", -180, 180, 180, v -> doRotate.getValue()));
    public Option<Double> rotatePitchOffset = addOption(new DoubleOption("RotatePitchOffset", -180, 180, 0, v -> doRotate.getValue()));
    public Option<Boolean> doShift = addOption(new BooleanOption("DoShift", true));
    public Option<Boolean> onlyBack = addOption(new BooleanOption("OnlyBack", false, v -> doShift.getValue()));
    public Option<EnumSet<SafeWalk.SneakModes>> sneakMode = addOption(new MultipleOption<>("SneakMode", EnumSet.of(SafeWalk.SneakModes.Client), v -> doShift.getValue()));
    public Option<Double> sneakDelay = addOption(new DoubleOption("SneakDelay", 1, 20, 7, v -> doShift.getValue()));
    public Option<Boolean> randomThreshold = addOption(new BooleanOption("RandomThreshold", true, v -> doShift.getValue()));
    public Option<Double> threshold = addOption(new DoubleOption("Threshold", 0.05, 0.3, 0.15, v -> doShift.getValue() && !randomThreshold.getValue()));
    public Option<Double> maxThreshold = addOption(new DoubleOption("MaxThreshold", 0.05, 0.3, 0.15, v -> doShift.getValue() && randomThreshold.getValue()));
    public Option<Double> minThreshold = addOption(new DoubleOption("MinThreshold", 0.05, 0.3, 0.08, v -> doShift.getValue() && randomThreshold.getValue()));

    @Override
    public String getDetails() {
        return AntiCheat.INSTANCE.getAntiCheat();
    }

    @Override
    public void onEnable() {
        timer.reset();
        if (mc.player != null) {
            lastRotation = new Rotation(mc.player.getPitch(), mc.player.getYaw(), rotateSpeed.getValue(), (RotateModes) rotateMode.getValue());
        }
    }

    @Override
    public void onDisable() {
        timer.reset();
        lastRotation = null;
    }

    @Override
    public void onTick() {
        if (!AntiCheat.INSTANCE.isLegit()) {
            return;
        }
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (!mc.player.isOnGround()) {
            return;
        }
        if (SafeWalk.INSTANCE != null) {
            SafeWalk.INSTANCE.safeWalkAction(
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

        if (!canPlace()) {
            return;
        }

        if (placeMode.getValue().equals(PlaceModes.AirPlace)) {
            placeBlockAirPlace();
        } else {
            placeBlockLegit();
        }
    }

    private boolean canPlace() {
        long minDelay = placeDelayMin.getValue().longValue();
        long maxDelay = placeDelayMax.getValue().longValue();
        long delay = randomPlaceDelay.getValue() ?
                minDelay + (long) (Math.random() * (maxDelay - minDelay)) : minDelay;

        return timer.passedMs(delay);
    }

    private float addRandomOffset(float base, double offsetRange) {
        if (!enableRotateRandom.getValue()) return base;
        return (float) (base + (Math.random() - 0.5) * offsetRange * 2);
    }

    private void placeBlockAirPlace() {
        ClientPlayerEntity player = mc.player;
        BlockPos playerPos = player.getBlockPos();
        BlockPos placePos = playerPos.down();

        BlockState blockBelow = mc.world.getBlockState(placePos);
        if (!blockBelow.isAir()) {
            return;
        }

        int slot = InventoryUtil.findBlock();
        if (slot == -1) {
            return;
        }
        InventoryUtil.switchToSlot(slot);

        Vec3d eyePos = player.getEyePos();
        Vec3d blockCenter = Vec3d.ofCenter(placePos);
        Vec3d targetPos = blockCenter.add(0, 0.5, 0);
        double diffX = targetPos.x - eyePos.x;
        double diffY = targetPos.y - eyePos.y;
        double diffZ = targetPos.z - eyePos.z;

        double distXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) (Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F + rotateYawOffset.getValue());
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, distXZ)) + rotatePitchOffset.getValue());

        if (doRotate.getValue()) {
            Rotation rotation = new Rotation(
                    addRandomOffset(pitch, rotateRandomPitch.getValue()),
                    addRandomOffset(yaw, rotateRandomYaw.getValue()),
                    rotateSpeed.getValue(),
                    (RotateModes) rotateMode.getValue()
            );
            Vergence.ROTATE.rotate(rotation);
            lastRotation = rotation;
        }

        if (enableSwing.getValue()) {
            EntityUtil.swingHand((Hands) swingHand.getValue(), (SwingModes) swingMode.getValue());
        }

        BlockHitResult blockHitResult = new BlockHitResult(targetPos, Direction.UP, placePos, false);
        Vergence.NETWORK.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, blockHitResult, InteractionUtil.getNextSequence(mc.interactionManager)));
        mc.interactionManager.interactBlock(player, Hand.MAIN_HAND, blockHitResult);

        timer.reset();
    }

    private void placeBlockLegit() {
        ClientPlayerEntity player = mc.player;
        BlockPos playerPos = player.getBlockPos();
        BlockPos placePos = playerPos.down();

        BlockState blockBelow = mc.world.getBlockState(placePos);
        if (!blockBelow.isAir()) {
            return;
        }

        int slot = InventoryUtil.findBlock();
        if (slot == -1) {
            return;
        }
        InventoryUtil.switchToSlot(slot);

        if (allowSprint.getValue()) {
            if (AutoSprint.INSTANCE != null) {
                AutoSprint.INSTANCE.disable();
            }
            mc.player.setSprinting(false);
        }

        BlockHitResult result = BlockUtil.findPlaceableFace(placePos, placeableRange.getValue().intValue());
        if (result == null) {
            return;
        }
        if (placeMode.getValue().equals(PlaceModes.Strict) && result.getSide().equals(Direction.DOWN)) {
            return ;
        }

        if (doRotate.getValue()) {
            Vec3d targetPos = result.getPos();

            Rotation rotation = calculateLegitRotation(
                    targetPos,
                    mc.player.getEyePos(),
                    lastRotation == null
                            ? new Rotation(mc.player.getPitch(), mc.player.getYaw(), rotateSpeed.getValue(), (RotateModes) rotateMode.getValue())
                            : lastRotation
            );

            Vergence.ROTATE.rotate(rotation);
            lastRotation = rotation;
        }


        if (enableSwing.getValue()) {
            EntityUtil.swingHand((Hands) swingHand.getValue(), (SwingModes) swingMode.getValue());
        }

        Vergence.NETWORK.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result, InteractionUtil.getNextSequence(mc.interactionManager)));
        mc.interactionManager.interactBlock(player, Hand.MAIN_HAND, result);

        timer.reset();
    }

    private Rotation calculateLegitRotation(Vec3d target, Vec3d eyePos, Rotation lastRotation) {
        double targetYaw = Math.toDegrees(Math.atan2(target.z - eyePos.z, target.x - eyePos.x)) - 90f + rotateYawOffset.getValue();
        double targetPitch = -Math.toDegrees(Math.atan2(target.y - eyePos.y, Math.sqrt(Math.pow(target.x - eyePos.x, 2) + Math.pow(target.z - eyePos.z, 2)))) + rotatePitchOffset.getValue();
        double yawDiff = MathHelper.wrapDegrees(targetYaw - lastRotation.getYaw());
        double pitchDiff = targetPitch - lastRotation.getPitch();
        double clampedYaw = MathHelper.clamp(yawDiff, -rotateSpeed.getValue(), rotateSpeed.getValue());
        double clampedPitch = MathHelper.clamp(pitchDiff, -rotateSpeed.getValue(), rotateSpeed.getValue());
        float newPitch = (float) (lastRotation.getPitch() + clampedPitch);
        float newYaw = (float) (lastRotation.getYaw() + clampedYaw);
        if (enableRotateRandom.getValue()) {
            newYaw += (Math.random() - 0.5) * rotateRandomYaw.getValue() * 2;
            newPitch += (Math.random() - 0.5) * rotateRandomPitch.getValue() * 2;
        }
        newPitch = MathHelper.clamp(newPitch, -90, 90);

        return new Rotation(newPitch, newYaw, rotateSpeed.getValue(), (RotateModes) rotateMode.getValue());
    }

}
