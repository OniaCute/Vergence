package cc.vergence.util.player;

import cc.vergence.Vergence;
import cc.vergence.features.enums.player.Hands;
import cc.vergence.features.enums.player.RotateModes;
import cc.vergence.features.enums.units.SpeedUnit;
import cc.vergence.features.enums.player.SwingModes;
import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.blocks.BlockUtil;
import cc.vergence.util.blocks.FixedBlockPos;
import cc.vergence.util.interfaces.IRotation;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.rotation.RotateUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.GameMode;
import net.minecraft.world.RaycastContext;

import java.util.ArrayList;
import java.util.List;

public class EntityUtil implements Wrapper {
    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = mc.player.getEyePos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{mc.player.getYaw() + MathHelper.wrapDegrees(yaw - mc.player.getYaw()), mc.player.getPitch() + MathHelper.wrapDegrees(pitch - mc.player.getPitch())};
    }

    public static boolean isFalling() {
        return mc.player.fallDistance > mc.player.getSafeFallDistance() && !mc.player.isOnGround() && !mc.player.isGliding();
    }

    public static boolean isFalling(double distance) {
        return mc.player.fallDistance > distance && !mc.player.isOnGround() && !mc.player.isGliding();
    }

    public static boolean isInsideBlock() {
        if (BlockUtil.getBlock(EntityUtil.getPlayerPos(true)) == Blocks.ENDER_CHEST) {
            return true;
        }
        return mc.world.canCollide(mc.player, mc.player.getBoundingBox());
    }

    public static BlockPos getPlayerPos(boolean fix) {
        return new FixedBlockPos(mc.player.getPos(), fix);
    }

    public static float getHealth(Entity entity) {
        if (entity.isLiving()) {
            LivingEntity livingBase = (LivingEntity) entity;
            return livingBase.getHealth() + livingBase.getAbsorptionAmount();
        }
        return 0.0f;
    }

    public static Vec3d getMotionVec(Entity entity, int ticks, boolean collision) {
        double dX = entity.getX() - entity.prevX;
        double dY = entity.getY() - entity.prevY;
        double dZ = entity.getZ() - entity.prevZ;
        double entityMotionPosX = 0;
        double entityMotionPosY = 0;
        double entityMotionPosZ = 0;
        if (collision) {
            for (double i = 1; i <= ticks; i = i + 0.5) {
                if (!mc.world.canCollide(entity, entity.getBoundingBox().offset(new Vec3d(dX * i, dY * i, dZ * i)))) {
                    entityMotionPosX = dX * i;
                    entityMotionPosY = dY * i;
                    entityMotionPosZ = dZ * i;
                } else {
                    break;
                }
            }
        } else {
            entityMotionPosX = dX * ticks;
            entityMotionPosY = dY * ticks;
            entityMotionPosZ = dZ * ticks;
        }

        return new Vec3d(entityMotionPosX, entityMotionPosY, entityMotionPosZ);
    }

    public static HitResult getRaytraceTarget(float yaw, float pitch, double x, double y, double z) {
        return getRaytraceTarget(yaw, pitch, x, y, z, 32);
    }

    public static HitResult getRaytraceTarget(float yaw, float pitch, double x, double y, double z, double distance) {
        Vec3d rotationVector = new Vec3d(MathHelper.sin(-yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F), -MathHelper.sin(pitch * 0.017453292F), MathHelper.cos(-yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F));
        HitResult result = mc.world.raycast(new RaycastContext(new Vec3d(x, y, z), new Vec3d(x + rotationVector.x * 5, y + rotationVector.y * 5, z + rotationVector.z * 5), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player));

        Vec3d vec3d = new Vec3d(x, y + mc.player.getEyeHeight(mc.player.getPose()), z);
        if (result != null) distance = result.getPos().squaredDistanceTo(vec3d);

        Vec3d multipliedVector = vec3d.add(rotationVector.x * 5, rotationVector.y * 5, rotationVector.z * 5);
        Box box = new Box(x - .3, y, z - .3, x + .3, y + 1.8, z + .3).stretch(rotationVector.multiply(5)).expand(1.0, 1.0, 1.0);

        EntityHitResult entityHitResult = ProjectileUtil.raycast(mc.player, vec3d, multipliedVector, box, (entity) -> !entity.isSpectator() && entity.canHit(), distance);
        if (entityHitResult != null) {
            if (vec3d.squaredDistanceTo(entityHitResult.getPos()) < distance || result == null) {
                if (entityHitResult.getEntity() instanceof LivingEntity) {
                    return entityHitResult;
                }
            }
        }

        return result;
    }

    public static void swingHand(Hands hand, SwingModes mode) {
        switch (hand) {
            case MainHand -> swingHand(Hand.MAIN_HAND, mode);
            case OffHand -> swingHand(Hand.OFF_HAND, mode);
        }
    }

    public static void swingHand(Hand hand, SwingModes mode) {
        switch (mode) {
            case Both -> mc.player.swingHand(hand);
            case Client -> mc.player.swingHand(hand, false);
            case Server -> mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
            default -> {return;} // no swing
        }
    }

    public static double getSpeed(Entity entity, SpeedUnit unit) {
        double speed = Math.sqrt(MathHelper.square(Math.abs(entity.getX() - entity.lastRenderX)) + MathHelper.square(Math.abs(entity.getZ() - entity.lastRenderZ)));

        if (unit == SpeedUnit.KILOMETERS) {
            return (speed * 3.6 * Vergence.TIMER.get()) * 20;
        } else {
            return speed / 0.05 * Vergence.TIMER.get();
        }
    }

    public static int getLatency(PlayerEntity player) {
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        return playerListEntry == null ? 0 : playerListEntry.getLatency();
    }

    public static GameMode getGameMode(PlayerEntity player) {
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        return playerListEntry == null ? GameMode.CREATIVE : playerListEntry.getGameMode();
    }

    public static float getDistance(Entity entity) {
        return mc.player.distanceTo(entity);
    }

    public static String getMovementDirection(Direction direction) {
        if (direction.getName().equalsIgnoreCase("North")) return "-Z";
        if (direction.getName().equalsIgnoreCase("East")) return "+X";
        if (direction.getName().equalsIgnoreCase("South")) return "+Z";
        if (direction.getName().equalsIgnoreCase("West")) return "-X";
        return "N/A";
    }

    public static String getGameModeText(GameMode gameMode) {
        return switch (gameMode) {
            case CREATIVE -> "C";
            case ADVENTURE -> "A";
            case SPECTATOR -> "SP";
            default -> "S";
        };
    }

    public static boolean isBot(PlayerEntity player) {
        PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        return entry == null || entry.getProfile() == null || player.getUuid().toString().startsWith(player.getName().getString()) || !player.getGameProfile().getName().equals(player.getName().getString());
    }

    public static boolean isHotbarKeysPressed() {
        for (KeyBinding binding : mc.options.hotbarKeys) {
            if (binding.isPressed()) {
                return true;
            }
        }
        return false;
    }

    public static boolean canSee(BlockPos pos, Direction side) {
        Vec3d testVec = pos.toCenterPos().add(side.getVector().getX() * 0.5, side.getVector().getY() * 0.5, side.getVector().getZ() * 0.5);
        HitResult result = mc.world.raycast(new RaycastContext(mc.player.getEyePos(), testVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player));
        return result == null || result.getType() == HitResult.Type.MISS;
    }

    public static void clickBlock(BlockPos pos, Direction side, boolean rotate, int priority, RotateModes mode, Hand hand, boolean packet) {
        Vec3d directionVec = new Vec3d(pos.getX() + 0.5 + side.getVector().getX() * 0.5, pos.getY() + 0.5 + side.getVector().getY() * 0.5, pos.getZ() + 0.5 + side.getVector().getZ() * 0.5);
        if (rotate) {
            Vergence.ROTATE.lookAt(directionVec, priority, mode);
        }
        EntityUtil.swingHand(hand, (SwingModes) AntiCheat.INSTANCE.swingMode.getValue());
        BlockHitResult result = new BlockHitResult(directionVec, side, pos, false);
        if (packet) {
            Vergence.NETWORK.sendSequencedPacket(id -> new PlayerInteractBlockC2SPacket(hand, result, id));
        } else {
            mc.interactionManager.interactBlock(mc.player, hand, result);
        }
    }

    public static void placeBlock(BlockPos pos, boolean rotate, int priority, RotateModes mode) {
        placeBlock(pos, rotate, priority, mode, AntiCheat.INSTANCE.packetPlace.getValue());
    }

    public static void placeBlock(BlockPos pos, boolean rotate, int priority, RotateModes mode, boolean packet) {
        Direction side = BlockUtil.getPlaceSide(pos);
        if (side == null) return;
        BlockUtil.placedPos.add(pos);
        clickBlock(pos.offset(side), side.getOpposite(), rotate, priority, mode, Hand.MAIN_HAND, packet);
    }

    public static boolean placeBlock(BlockPos pos, int slot, boolean strictDirection, boolean clientSwing, IRotation irotation) {
        Direction direction = RotateUtil.getInteractDirection(pos, strictDirection);
        if (direction == null) {
            return false;
        }
        BlockPos neighbor = pos.offset(direction.getOpposite());
        return placeBlock(neighbor, direction, slot, clientSwing, irotation);
    }

    public static boolean placeBlock(BlockPos pos, Direction direction, int slot, boolean clientSwing, IRotation irotation) {
        Vec3d hitVec = pos.toCenterPos().add(new Vec3d(direction.getUnitVector()).multiply(0.5));
        return placeBlock(new BlockHitResult(hitVec, direction, pos, false), slot, clientSwing, irotation);
    }

    public static boolean placeBlock(BlockHitResult hitResult, int slot, boolean clientSwing, IRotation irotation) {
//        boolean isSpoofing = slot != InventoryUtil.getServerSlot();
//        if (isSpoofing) {
            InventoryUtil.sendServerSlot(slot);
            // mc.player.getInventory().selectedSlot = slot;
//        }

        boolean isRotating = irotation != null;
        if (isRotating) {
            float[] angles = RotateUtil.getRotationsTo(mc.player.getEyePos(), hitResult.getPos());
            irotation.handleRotation(true, angles);
        }

        boolean result = placeBlockImmediately(hitResult, clientSwing);
        if (isRotating) {
            float[] angles = RotateUtil.getRotationsTo(mc.player.getEyePos(), hitResult.getPos());
            irotation.handleRotation(false, angles);
        }

//        if (isSpoofing)
//        {
            InventoryUtil.syncInventory();
//        }

        return result;
    }

    public static boolean placeBlockImmediately(BlockHitResult result, boolean clientSwing) {
        BlockState state = mc.world.getBlockState(result.getBlockPos());
        ActionResult actionResult = placeBlockInternally(result);
        if (actionResult.isAccepted()) {
            if (clientSwing) {
                mc.player.swingHand(Hand.MAIN_HAND);
            } else {
                Vergence.NETWORK.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
            }
        }
        return actionResult.isAccepted();
    }

    private static ActionResult placeBlockInternally(BlockHitResult hitResult) {
        return mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hitResult);
    }

    public static BlockPos getRoundedBlockPos(double x, double y, double z) {
        int flooredX = MathHelper.floor(x);
        int flooredY = (int) Math.round(y);
        int flooredZ = MathHelper.floor(z);
        return new BlockPos(flooredX, flooredY, flooredZ);
    }

    public static List<BlockPos> getAllInBox(Box box, BlockPos pos) {
        List<BlockPos> intersections = new ArrayList<>();
        for (int x = (int) Math.floor(box.minX); x < Math.ceil(box.maxX); x++) {
            for (int z = (int) Math.floor(box.minZ); z < Math.ceil(box.maxZ); z++) {
                intersections.add(new BlockPos(x, pos.getY(), z));
            }
        }
        return intersections;
    }

    public static List<BlockPos> getAllInBox(Box box) {
        List<BlockPos> intersections = new ArrayList<>();
        for (int x = (int) Math.floor(box.minX); x < Math.ceil(box.maxX); x++) {
            for (int y = (int) Math.floor(box.minY); y < Math.ceil(box.maxY); y++) {
                for (int z = (int) Math.floor(box.minZ); z < Math.ceil(box.maxZ); z++) {
                    intersections.add(new BlockPos(x, y, z));
                }
            }
        }
        return intersections;
    }
}
