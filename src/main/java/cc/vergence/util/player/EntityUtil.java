package cc.vergence.util.player;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Hands;
import cc.vergence.features.enums.SpeedUnit;
import cc.vergence.features.enums.SwingModes;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.RaycastContext;

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

    public static String getGameModeText(GameMode gameMode) {
        return switch (gameMode) {
            case CREATIVE -> "C";
            case ADVENTURE -> "A";
            case SPECTATOR -> "SP";
            default -> "S";
        };
    }
}
