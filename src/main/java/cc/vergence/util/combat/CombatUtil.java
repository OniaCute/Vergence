package cc.vergence.util.combat;

import cc.vergence.Vergence;
import cc.vergence.features.enums.player.Hands;
import cc.vergence.features.enums.player.RotateModes;
import cc.vergence.features.enums.player.SwingModes;
import cc.vergence.features.enums.player.TargetTypes;
import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.blocks.BlockUtil;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.rotation.Rotation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CombatUtil implements Wrapper {
    public static final FastTimerUtil breakTimer = new FastTimerUtil();

    public static void aimAt(Vec3d pos, int priority, RotateModes rotateModes) {
        if (mc.player == null) {
            return;
        }

        double dx = pos.getX() - mc.player.getX();
        double dy = pos.getY() - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double dz = pos.getZ() - mc.player.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, distance));

        Vergence.ROTATE.rotate(new Rotation(pitch, yaw, priority, rotateModes));
    }

    public static void aimAt(Entity entity, int priority, RotateModes rotateModes, Runnable onFinish) {
        if (entity == null || mc.player == null) {
            return;
        }

        double dx = entity.getX() - mc.player.getX();
        double dy = (entity.getY() + entity.getHeight() / 2.0) - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double dz = entity.getZ() - mc.player.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, distance));

        Vergence.ROTATE.rotate(new Rotation(pitch, yaw, priority, rotateModes), onFinish);
    }

    public static PlayerEntity getClosestTarget(double range) {
        if (mc.player == null || mc.world == null) {
            return null;
        }

        List<PlayerEntity> players = mc.world.getPlayers().stream()
                .filter(p -> p != mc.player && !p.isDead() && mc.player.distanceTo(p) <= range)
                .filter(p -> !Vergence.FRIEND.isFriend(p.getGameProfile().getName()))
                .sorted(Comparator.comparingDouble(p -> mc.player.distanceTo(p)))
                .collect(Collectors.toList());

        List<PlayerEntity> enemies = players.stream()
                .filter(p -> Vergence.ENEMY.isEnemy(p.getGameProfile().getName()))
                .toList();

        if (!enemies.isEmpty()) {
            return enemies.get(0);
        }
        return players.isEmpty() ? null : players.get(0);
    }

    public static LivingEntity getClosestAnyTarget(double range, EnumSet<TargetTypes> types) {
        if (mc.world == null || mc.player == null) {
            return null;
        }

        LivingEntity entity = StreamSupport.stream(mc.world.getEntities().spliterator(), false)
                .filter(e -> e instanceof LivingEntity le && isValidTarget(le, types, range))
                .map(e -> (LivingEntity) e)
                .min(Comparator.comparingDouble(mc.player::distanceTo))
                .orElse(null);

        if (entity instanceof PlayerEntity player && Vergence.FRIEND.isFriend(player.getGameProfile().getName())) {
            return null;
        }
        return entity;
    }

    public static boolean isValidTarget(Entity entity, EnumSet<TargetTypes> types, double range) {
        if (entity == null || entity == mc.player || !entity.isAlive()) {
            return false;
        }
        if (mc.player.distanceTo(entity) > range) {
            return false;
        }
        if (entity.isInvisible() && !types.contains(TargetTypes.Invisible)) {
            return false;
        }
        if (entity instanceof PlayerEntity player) {
            if (player == mc.player) {
                return false;
            }
            if (Vergence.FRIEND.isFriend(player.getGameProfile().getName())) {
                return types.contains(TargetTypes.FriendPlayers);
            }
            return types.contains(TargetTypes.EnemyPlayers);
        }
        if (entity instanceof AnimalEntity) {
            return types.contains(TargetTypes.Animals);
        }
        if (entity instanceof MobEntity) {
            return types.contains(TargetTypes.Mobs);
        }
        if (entity instanceof EndCrystalEntity) {
            return types.contains(TargetTypes.Crystal);
        }
        return false;
    }

    public static boolean canSee(Entity entity) {
        return mc.player != null && mc.player.canSee(entity);
    }

    public static boolean isEnemy(PlayerEntity player) {
        return !Vergence.FRIEND.isFriend(player.getGameProfile().getName()) && Vergence.ENEMY.isEnemy(player.getGameProfile().getName());
    }

    public static boolean isFriend(PlayerEntity player) {
        return Vergence.FRIEND.isFriend(player.getGameProfile().getName());
    }
}
