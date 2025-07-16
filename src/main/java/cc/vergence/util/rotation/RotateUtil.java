package cc.vergence.util.rotation;

import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.entity.player.PlayerEntity;

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
}
