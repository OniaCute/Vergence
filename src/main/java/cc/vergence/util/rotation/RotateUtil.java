package cc.vergence.util.rotation;

import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
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

    public static Vec2f getRotationVec2f(Vec3d posFrom, Vec3d posTo) {
        Vec3d vec3d = posTo.subtract(posFrom);
        return getRotationFromVec(vec3d);
    }

    private static Vec2f getRotationFromVec(Vec3d vec) {
        double d = vec.x;
        double d2 = vec.z;
        double xz = Math.hypot(d, d2);
        d2 = vec.z;
        double d3 = vec.x;
        double yaw = humanizeAngle(Math.toDegrees(Math.atan2(d2, d3)) - 90.0);
        double pitch = humanizeAngle(Math.toDegrees(-Math.atan2(vec.y, xz)));
        return new Vec2f((float) yaw, (float) pitch);
    }

    private static double humanizeAngle(double angleIn) {
        double angle = angleIn;
        if ((angle %= 360.0) >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }
}
