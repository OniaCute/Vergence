package cc.vergence.modules.combat;

import cc.vergence.Vergence;
import cc.vergence.features.enums.player.RotateModes;
import cc.vergence.features.enums.player.SwapModes;
import cc.vergence.features.enums.player.SwingModes;
import cc.vergence.features.event.events.*;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.blocks.BlockUtil;
import cc.vergence.util.combat.CombatUtil;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.player.InventoryUtil;
import cc.vergence.util.player.MovementUtil;
import cc.vergence.util.rotation.RotateUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;

import java.util.EnumSet;

public class Surround extends Module {
    public static Surround INSTANCE;
    private final FastTimerUtil timer = new FastTimerUtil();
    double startX = 0;
    double startY = 0;
    double startZ = 0;
    int progress = 0;
    private boolean shouldCenter = true;
    public Vec3d directionVec = null;

    public Surround() {
        super("Surround", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Double> fov = addOption(new DoubleOption("FOV", 0, 30, 5));
    public Option<Boolean> tickLimit = addOption(new BooleanOption("TickLimit", false));
    public Option<Boolean> toCenter = addOption(new BooleanOption("ToCenter", true));
    public Option<Boolean> extension = addOption(new BooleanOption("Extension", true));
    public Option<Boolean> onlySelf = addOption(new BooleanOption("OnlySelf", true, v -> extension.getValue()));
    public Option<Boolean> whileEating = addOption(new BooleanOption("WhileEating", true));
    public Option<Boolean> whileMining = addOption(new BooleanOption("WhileMining", true));
    public Option<Boolean> inAir = addOption(new BooleanOption("InAir", false));
    public Option<Boolean> packetPlace = addOption(new BooleanOption("PacketPlace", false));
    public Option<Double> multiPlace = addOption(new DoubleOption("MultiPlace", 1, 8, 1));
    public Option<Double> placeDelay = addOption(new DoubleOption("PlaceDelay", 0, 500, 50));
    public Option<Boolean> inventorySwap = addOption(new BooleanOption("InventorySwap", true));
    public Option<Boolean> enderChest = addOption(new BooleanOption("EnderChest", true));
    public Option<Boolean> doRotate = addOption(new BooleanOption("Rotate", true));
    public Option<Boolean> yawStep = addOption(new BooleanOption("YawStep", true, v -> doRotate.getValue()));
    public Option<Enum<?>> rotateMode = addOption(new EnumOption("RotateMode", RotateModes.Server, v -> doRotate.getValue()));
    public Option<Boolean> doSwing = addOption(new BooleanOption("Swing", true));
    public Option<Boolean> autoDisable = addOption(new BooleanOption("AutoDisable", false));
    public Option<EnumSet<DisableItems>> disableItems = addOption(new MultipleOption<DisableItems>("DisableItems", EnumSet.of(DisableItems.Exit, DisableItems.Jump, DisableItems.Place, DisableItems.Death)));
    public Option<Boolean> antiCrystal = addOption(new BooleanOption("AntiCrystal", false, v -> !autoDisable.getValue() || disableItems.getValue().isEmpty()));
    public Option<EnumSet<BlockModules>> blocker = addOption(new MultipleOption<BlockModules>("BlockModules", EnumSet.of(BlockModules.Step)));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onPlayerJump(PlayerJumpEvent event) {
        if (autoDisable.getValue() && disableItems.getValue().contains(DisableItems.Jump)) {
            this.disable();
        }
    }

    @Override
    public void onRotateEvent(RotateEvent event, float yaw, float pitch) {
        if (isNull()) {
            return ;
        }

        if (directionVec != null && doRotate.getValue() && yawStep.getValue()) {
            CombatUtil.aimAt(directionVec, this.getPriority(), (RotateModes) rotateMode.getValue());
        }
    }

    @Override
    public void onEntityRemove(EntityRemoveEvent event, Entity entity) {
        if (entity == mc.player && autoDisable.getValue() && disableItems.getValue().contains(DisableItems.Death)) {
            disable();
        }
    }

    @Override
    public void onShutDown() {
        if (autoDisable.getValue() && disableItems.getValue().contains(DisableItems.Exit)) {
            this.disable();
        }
    }

    @Override
    public void onDisable() {
        shouldCenter = true;
        progress = 0;
        directionVec = null;
    }

    @Override
    public void onTickMovement(TickMovementEvent event) {
        if (isNull() || tickLimit.getValue()) {
            return;
        }
        onTick();
    }

    @Override
    public void onEnable() {
        if (isNull() && autoDisable.getValue() && disableItems.getValue().contains(DisableItems.Exit)) {
            this.disable();
            return ;
        }

        startX = mc.player.getX();
        startY = mc.player.getY();
        startZ = mc.player.getZ();
        shouldCenter = true;
    }

    @Override
    public void onMoveEvent(MoveEvent event, double x, double y, double z) {
        if (isNull() || !toCenter.getValue() || EntityUtil.isFalling(10)) {
            return;
        }
        BlockPos blockPos = EntityUtil.getPlayerPos(true);
        if (mc.player.getX() - blockPos.getX() - 0.5 <= 0.2 && mc.player.getX() - blockPos.getX() - 0.5 >= -0.2 && mc.player.getZ() - blockPos.getZ() - 0.5 <= 0.2 && mc.player.getZ() - 0.5 - blockPos.getZ() >= -0.2) {
            if (shouldCenter && (mc.player.isOnGround() || MovementUtil.isMoving())) {
                event.setX(0);
                event.setZ(0);
                shouldCenter = false;
            }
        } else {
            if (shouldCenter) {
                Vec3d centerPos = EntityUtil.getPlayerPos(true).toCenterPos();
                float rotation = RotateUtil.getRotationVec2f(mc.player.getPos(), centerPos).x;
                float yawRad = rotation / 180.0f * 3.1415927f;
                double dist = mc.player.getPos().distanceTo(new Vec3d(centerPos.x, mc.player.getY(), centerPos.z));
                double cappedSpeed = Math.min(0.2873, dist);
                double x1 = -(float) Math.sin(yawRad) * cappedSpeed;
                double z1 = (float) Math.cos(yawRad) * cappedSpeed;
                event.setX(x1);
                event.setZ(z1);
            }
        }
    }

    @Override
    public void onTick() {
        if (isNull()) {
            return ;
        }

        if (!timer.passedMs(placeDelay.getValue())) {
            return;
        }
        directionVec = null;
        progress = 0;
        if (!MovementUtil.isMoving() && !mc.options.jumpKey.isPressed()) {
            startX = mc.player.getX();
            startY = mc.player.getY();
            startZ = mc.player.getZ();
        }
        BlockPos pos = EntityUtil.getPlayerPos(true);

        double distanceToStart = MathHelper.sqrt((float) mc.player.squaredDistanceTo(startX, startY, startZ));

        if (getBlock() == -1) {
            NotifyManager.newNotification(this, Vergence.TEXT.get("Module.Modules.Surround.Messages.NoSuchBlocks"));
            disable();
            return;
        }
        if (!whileEating.getValue() && mc.player.isUsingItem()) {
            return;
        }

        if (!inAir.getValue() && !mc.player.isOnGround()) return;
        for (Direction i : Direction.values()) {
            if (i == Direction.UP) continue;
            BlockPos offsetPos = pos.offset(i);
            if (BlockUtil.getPlaceSide(offsetPos) != null) {
                tryPlaceBlock(offsetPos);
            } else if (BlockUtil.canReplace(offsetPos)) {
                tryPlaceBlock(getHelperPos(offsetPos));
            }
            if ((selfIntersectPos(offsetPos) || !onlySelf.getValue() && otherIntersectPos(offsetPos)) && extension.getValue()) {
                for (Direction i2 : Direction.values()) {
                    if (i2 == Direction.UP) continue;
                    BlockPos offsetPos2 = offsetPos.offset(i2);
                    if (selfIntersectPos(offsetPos2)|| !onlySelf.getValue() && otherIntersectPos(offsetPos2)) {
                        for (Direction i3 : Direction.values()) {
                            if (i3 == Direction.UP) continue;
                            tryPlaceBlock(offsetPos2);
                            BlockPos offsetPos3 = offsetPos2.offset(i3);
                            tryPlaceBlock(BlockUtil.getPlaceSide(offsetPos3) != null || !BlockUtil.canReplace(offsetPos3) ? offsetPos3 : getHelperPos(offsetPos3));
                        }
                    }
                    tryPlaceBlock(BlockUtil.getPlaceSide(offsetPos2) != null || !BlockUtil.canReplace(offsetPos2) ? offsetPos2 : getHelperPos(offsetPos2));
                }
            }
        }
    }

    private void tryPlaceBlock(BlockPos pos) {
        if (pos == null) return;
        if (!whileMining.getValue() && Vergence.MINE.isMining(pos)) return;
        if (!(progress < multiPlace.getValue().intValue())) return;
        int block = getBlock();
        if (block == -1) return;
        Direction side = BlockUtil.getPlaceSide(pos);
        if (side == null) return;
        Vec3d directionVec = new Vec3d(pos.getX() + 0.5 + side.getVector().getX() * 0.5, pos.getY() + 0.5 + side.getVector().getY() * 0.5, pos.getZ() + 0.5 + side.getVector().getZ() * 0.5);
        if (!BlockUtil.canPlace(pos, 6, true)) return;
        if (doRotate.getValue()) {
            if (!faceVector(directionVec)) return;
        }
        if (antiCrystal.getValue()) {
            CombatUtil.attackCrystal(pos, doRotate.getValue(), this.getPriority(), ((RotateModes) rotateMode.getValue()), !whileEating.getValue());
        } else if (BlockUtil.hasEntity(pos, false)) return;
        int old = mc.player.getInventory().selectedSlot;
        doSwap(block);
        BlockUtil.placedPos.add(pos);
        EntityUtil.clickBlock(pos.offset(side), side.getOpposite(), false, getPriority(), ((RotateModes) rotateMode.getValue()), Hand.MAIN_HAND, packetPlace.getValue());
        if (inventorySwap.getValue()) {
            doSwap(block);
            InventoryUtil.syncInventory();
        } else {
            doSwap(old);
        }
        if (doRotate.getValue() && !yawStep.getValue() && AntiCheat.INSTANCE.snapBack.getValue()) {
            Vergence.ROTATE.snapBack();
        }
        progress++;
        timer.reset();
    }

    private void doSwap(int slot) {
        if (inventorySwap.getValue()) {
            InventoryUtil.inventorySwap(slot, mc.player.getInventory().selectedSlot);
        } else {
            InventoryUtil.switchToSlot(slot);
        }
    }

    private int getBlock() {
        if (inventorySwap.getValue()) {
            if (InventoryUtil.findBlockInventorySlot(Blocks.OBSIDIAN) != -1 || !enderChest.getValue()) {
                return InventoryUtil.findBlockInventorySlot(Blocks.OBSIDIAN);
            }
            return InventoryUtil.findBlockInventorySlot(Blocks.ENDER_CHEST);
        } else {
            if (InventoryUtil.findBlock(Blocks.OBSIDIAN) != -1 || !enderChest.getValue()) {
                return InventoryUtil.findBlock(Blocks.OBSIDIAN);
            }
            return InventoryUtil.findBlock(Blocks.ENDER_CHEST);
        }
    }

    public BlockPos getHelperPos(BlockPos pos) {
        for (Direction i : Direction.values()) {
            if (!whileMining.getValue() && Vergence.MINE.isMining(pos.offset(i))) continue;
            if (!BlockUtil.isStrictDirection(pos.offset(i), i.getOpposite())) continue;
            if (BlockUtil.canPlace(pos.offset(i))) return pos.offset(i);
        }
        return null;
    }

    private boolean faceVector(Vec3d directionVec) {
        if (!yawStep.getValue()) {
            Vergence.ROTATE.lookAt(directionVec, this.getPriority(), ((RotateModes) rotateMode.getValue()));
            return true;
        } else {
            this.directionVec = directionVec;
            if (Vergence.ROTATE.inFov(directionVec, fov.getValue().floatValue())) {
                return true;
            }
        }
        return false;
    }

    public static boolean selfIntersectPos(BlockPos pos) {
        return mc.player.getBoundingBox().intersects(new Box(pos));
    }

    public static boolean otherIntersectPos(BlockPos pos) {
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player.getBoundingBox().intersects(new Box(pos))) {
                return true;
            }
        }
        return false;
    }

    public enum BlockModules {
        Step,
        Speed,
        Strafe,
        SelfTrap
    }

    public enum DisableItems {
        Exit,
        Jump,
        Move,
        Place,
        Death
    }
}
