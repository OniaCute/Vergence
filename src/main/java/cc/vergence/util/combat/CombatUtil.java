package cc.vergence.util.combat;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Hands;
import cc.vergence.features.enums.RotateModes;
import cc.vergence.features.enums.SwingModes;
import cc.vergence.features.enums.TargetTypes;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.rotation.Rotation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CombatUtil implements Wrapper {

    public static void aimAt(Entity entity) {
        aimAt(entity, 0);
    }

    public static void aimAt(Entity entity, RotateModes rotateModes) {
        aimAt(entity, 0, rotateModes);
    }

    public static void aimAt(Entity entity, int priority) {
        aimAt(entity, priority, () -> {});
    }

    public static void aimAt(Entity entity, int priority, RotateModes rotateModes) {
        if (entity == null || mc.player == null) {
            return;
        }

        double dx = entity.getX() - mc.player.getX();
        double dy = (entity.getY() + entity.getHeight() / 2.0) - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double dz = entity.getZ() - mc.player.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, distance));

        Vergence.ROTATE.rotate(new Rotation(pitch, yaw, priority, rotateModes));
    }

    public static void aimAt(Entity entity, int priority, Runnable onFinish) {
        if (entity == null || mc.player == null) {
            return;
        }

        double dx = entity.getX() - mc.player.getX();
        double dy = (entity.getY() + entity.getHeight() / 2.0) - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double dz = entity.getZ() - mc.player.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, distance));

        Vergence.ROTATE.rotate(new Rotation(pitch, yaw, priority, RotateModes.Both), onFinish);
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
        if (entity.isInvisible() && !types.contains(TargetTypes.invisible)) {
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
        return false;
    }

    public static void attack(Entity entity) {
        attack(entity, SwingModes.Client, true);
    }

    public static void attack(Entity entity, SwingModes swingModes, boolean swing) {
        if (mc.player == null || mc.interactionManager == null || entity == null) {
            return;
        }
        mc.interactionManager.attackEntity(mc.player, entity);
        if (swing) {
            EntityUtil.swingHand(Hands.MainHand, swingModes);
        }
    }

    public static void aimAndAttack(Entity entity) {
        aimAndAttack(entity, 0);
    }

    public static void aimAndAttack(Entity entity, int priority) {
        aimAt(entity, priority, () -> attack(entity));
    }

    public static void aimAndAttackClosest(double range, EnumSet<TargetTypes> types) {
        LivingEntity target = getClosestTarget(range);
        if (target == null) target = getClosestAnyTarget(range, types);
        if (target != null) aimAndAttack(target);
    }

    public static void aimAndAttackClosest(EnumSet<TargetTypes> types) {
        aimAndAttackClosest(100, types);
    }

    public static boolean isCrosshairOnEntity(Entity entity, double threshold) {
        if (mc.player == null || entity == null) {
            return false;
        }
        Vec3d eyes = mc.player.getCameraPosVec(1.0F);
        Vec3d look = mc.player.getRotationVec(1.0F);
        Vec3d toTarget = entity.getPos().add(0, entity.getHeight() / 2.0, 0).subtract(eyes).normalize();
        double dot = look.dotProduct(toTarget);
        double angle = Math.toDegrees(Math.acos(dot));
        return angle < threshold;
    }


    public static float getYawTo(Entity entity) {
        Vec3d target = entity.getPos().add(0, entity.getHeight() / 2.0, 0);
        Vec3d eyes = mc.player.getEyePos();
        double dx = target.x - eyes.x;
        double dz = target.z - eyes.z;
        return (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(dz, dx)) - 90);
    }

    public static float getPitchTo(Entity entity) {
        Vec3d target = entity.getPos().add(0, entity.getHeight() / 2.0, 0);
        Vec3d eyes = mc.player.getEyePos();
        double dx = target.x - eyes.x;
        double dy = target.y - eyes.y;
        double dz = target.z - eyes.z;
        return (float) -Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));
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
