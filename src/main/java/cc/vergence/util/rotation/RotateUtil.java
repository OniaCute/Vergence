package cc.vergence.util.rotation;

import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotateUtil implements Wrapper {
    public static float getPlayerPitch() {
        return  mc.player == null ? 0 : mc.player.getPitch();
    }

    public static double getPlayerYaw() {
        return mc.player == null ? 0 : mc.player.getY();
    }

    public static float getPlayerPitch(PlayerEntity player) {
        return  player == null ? 0 : player.getPitch();
    }

    public static double getPlayerYaw(PlayerEntity player) {
        return  player == null ? 0 : player.getYaw();
    }

    public static float[] getRotationsTo(Vec3d src, Vec3d dest) {
        float yaw = (float) (Math.toDegrees(Math.atan2(dest.subtract(src).z, dest.subtract(src).x)) - 90);
        float pitch = (float) Math.toDegrees(-Math.atan2(dest.subtract(src).y, Math.hypot(dest.subtract(src).x, dest.subtract(src).z)));
        return new float[] {MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch)};
    }
}
