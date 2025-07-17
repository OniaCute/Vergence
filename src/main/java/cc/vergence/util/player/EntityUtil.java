package cc.vergence.util.player;

import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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
}
