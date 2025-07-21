package cc.vergence.modules.movement;

import cc.vergence.Vergence;
import cc.vergence.features.enums.*;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
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

    public Option<Enum<?>> antiCheat = addOption(new EnumOption("AntiCheat", AntiCheats.Legit));
    public Option<Boolean> allowSprint = addOption(new BooleanOption("Sprint", false));
    public Option<Boolean> doRotate = addOption(new BooleanOption("Rotate", true));
    public Option<Enum<?>> rotateMode = addOption(new EnumOption("RotateMode", RotateModes.Both, v -> doRotate.getValue()));
    public Option<Double> rotateSpeed = addOption(new DoubleOption("RotateSpeed", 1, 180, 30).addSpecialValue(180, "INSTANT").setUnit("°"));
    public Option<Boolean> doSwing = addOption(new BooleanOption("Swing", true));
    public Option<Enum<?>> swingMode = addOption(new EnumOption("SwingMode", SwingModes.Both, v -> doSwing.getValue()));
    public Option<Enum<?>> swingHand = addOption(new EnumOption("SwingHand", Hands.MainHand, v -> doSwing.getValue()));
    public Option<Enum<?>> placeMode = addOption(new EnumOption("PlaceMode", PlaceModes.Legit));
    public Option<Double> placeDelay = addOption(new DoubleOption("PlaceDelay", 0, 600, 300).setUnit("ms"));
    public Option<Double> placeableRange = addOption(new DoubleOption("PlaceableRange", 1, 4, 2, v -> !placeMode.getValue().equals(PlaceModes.AirPlace)));
    public Option<Double> rotateYawOffset = addOption(new DoubleOption("RotateYawOffset", -180, 180, 180, v -> doRotate.getValue()));
    public Option<Double> rotatePitchOffset = addOption(new DoubleOption("RotatePitchOffset", -180, 180, 0, v -> doRotate.getValue()));
    public Option<Boolean> doShift = addOption(new BooleanOption("DoShift", true));
    public Option<Boolean> onlyBack = addOption(new BooleanOption("OnlyBack", false, v -> doShift.getValue()));
    public Option<EnumSet<SafeWalk.SneakModes>> sneakMode = addOption(new MultipleOption<SafeWalk.SneakModes>("SneakMode", EnumSet.of(SafeWalk.SneakModes.Client), v -> doShift.getValue()));
    public Option<Double> sneakDelay = addOption(new DoubleOption("SneakDelay", 1, 20, 7, v -> doShift.getValue()));
    public Option<Boolean> randomThreshold = addOption(new BooleanOption("RandomThreshold", true, v -> doShift.getValue()));
    public Option<Double> threshold = addOption(new DoubleOption("Threshold", 0.05, 0.3, 0.15, v -> doShift.getValue() && !randomThreshold.getValue()));
    public Option<Double> maxThreshold = addOption(new DoubleOption("MaxThreshold", 0.05, 0.3, 0.15, v -> doShift.getValue() && randomThreshold.getValue()));
    public Option<Double> minThreshold = addOption(new DoubleOption("MinThreshold", 0.05, 0.3, 0.08, v -> doShift.getValue() && randomThreshold.getValue()));

    @Override
    public String getDetails() {
        return antiCheat.getValue().name();
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    @Override
    public void onDisable() {
        timer.reset();
    }

    @Override
    public void onTick() {
        if (!antiCheat.getValue().equals(AntiCheats.Legit)) {
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
            if (!timer.passedMs(placeDelay.getValue())) {
                return;
            }
            placeBlockUnderPlayer();
        }
    }

    private void placeBlockUnderPlayer() {
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
        mc.player.getInventory().selectedSlot = slot;
        Vec3d eyePos = player.getEyePos();
        Vec3d blockCenter = Vec3d.ofCenter(placePos);
        Vec3d targetPos = blockCenter.add(0, 0.5, 0);
        double diffX = targetPos.x - eyePos.x;
        double diffY = targetPos.y - eyePos.y;
        double diffZ = targetPos.z - eyePos.z;

        double distXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        if (placeMode.getValue().equals(PlaceModes.AirPlace)) {
            float yaw = (float) ((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F + rotateYawOffset.getValue());
            float pitch = (float) ((float) -Math.toDegrees(Math.atan2(diffY, distXZ)) + rotatePitchOffset.getValue());

            if (doRotate.getValue()) {
                Vergence.ROTATE.rotate(new Rotation(pitch, yaw, 180, ((RotateModes) rotateMode.getValue())));
                lastRotation = new Rotation(pitch, yaw, 180, ((RotateModes) rotateMode.getValue()));
            }
            if (doSwing.getValue()) {
                EntityUtil.swingHand((Hands) swingHand.getValue(), (SwingModes) swingMode.getValue());
            }
            BlockHitResult blockHitResult = new BlockHitResult(targetPos, Direction.UP, placePos, false);
            Vergence.NETWORK.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, blockHitResult, InteractionUtil.getNextSequence(mc.interactionManager)));
            mc.interactionManager.interactBlock(player, Hand.MAIN_HAND, new BlockHitResult(targetPos, Direction.UP, placePos, false));
        }
        else if (placeMode.getValue().equals(PlaceModes.Legit) || placeMode.getValue().equals(PlaceModes.Strict)) {
            if (allowSprint.getValue()) {
                if (AutoSprint.INSTANCE != null) {
                    AutoSprint.INSTANCE.disable();
                }
                mc.player.setSprinting(false); // always no sprint
            }

            BlockHitResult result = BlockUtil.findPlaceableFace(placePos, placeableRange.getValue().intValue());
            if (result != null) {
                if (placeMode.getValue().equals(PlaceModes.Strict) && result.getSide().equals(Direction.DOWN)) {
                    return;
                }
                if (doRotate.getValue()) {
                    Rotation rotation = calculateSmartRotation(result.getPos(), mc.player.getEyePos(), (lastRotation == null ? new Rotation(mc.player.getPitch(), mc.player.getYaw(), 0, RotateModes.None) : lastRotation));
                    Vergence.ROTATE.rotate(rotation);
                    lastRotation = rotation;
                }
                if (doSwing.getValue()) {
                    EntityUtil.swingHand((Hands) swingHand.getValue(), (SwingModes) swingMode.getValue());
                }
                Vergence.NETWORK.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, result, InteractionUtil.getNextSequence(mc.interactionManager)));
                mc.interactionManager.interactBlock(player, Hand.MAIN_HAND, result);
            }
        }
        timer.reset();
    }

    private Rotation calculateSmartRotation(Vec3d target, Vec3d eyePos, Rotation lastRotation) {
        double targetYaw = (float) (Math.toDegrees(Math.atan2(target.subtract(eyePos).z, target.subtract(eyePos).x)) - 90f);
        double targetPitch = (float) -Math.toDegrees(Math.atan2(target.subtract(eyePos).y, Math.sqrt(target.subtract(eyePos).x * target.subtract(eyePos).x + target.subtract(eyePos).z * target.subtract(eyePos).z)));
        targetYaw += rotateYawOffset.getValue();
        targetPitch += rotatePitchOffset.getValue();
        double yawGap = MathHelper.wrapDegrees(targetYaw - lastRotation.getYaw());
        double pitchGap = targetPitch - lastRotation.getPitch();
        yawGap = MathHelper.clamp(yawGap, -rotateSpeed.getValue(), rotateSpeed.getValue());
        pitchGap = MathHelper.clamp(pitchGap, -rotateSpeed.getValue(), rotateSpeed.getValue());
        return new Rotation((float) (lastRotation.getPitch() + pitchGap), lastRotation.getYaw() + yawGap, rotateSpeed.getValue(), (RotateModes) rotateMode.getValue());
    }

}
