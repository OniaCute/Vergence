package cc.vergence.util.combat;

import cc.vergence.util.interfaces.Wrapper;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class CrystalUtil implements Wrapper {
    private static final Map<String, Integer> PROTECTION_MAP = new HashMap<>() {{
        put("protection", 1);
        put("blast_protection", 2);
        put("projectile_protection", 1);
        put("feather_falling", 1);
        put("fire_protection", 1);
    }};

    public static double getDamageTo(final Entity entity,
                                     final Vec3d crystal) {
        return getDamageTo(entity, crystal, false);
    }

    public static double getDamageTo(final Entity entity,
                                     final Vec3d crystal,
                                     final boolean ignoreTerrain) {
        double ab = getExposure(crystal, entity, ignoreTerrain);
        double w = Math.sqrt(entity.squaredDistanceTo(crystal)) / 12.0;
        double ac = (1.0 - w) * ab;
        double dmg = (float) ((int) ((ac * ac + ac) / 2.0 * 7.0 * 12.0 + 1.0));
        dmg = getReduction(entity, mc.world.getDamageSources().explosion(null), dmg);
        return Math.max(0.0, dmg);
    }

    public static double getDamageToPos(final Vec3d pos,
                                        final Entity entity,
                                        final Vec3d crystal) {
        return getDamageToPos(pos, entity, crystal, false);
    }

    public static double getDamageToPos(final Vec3d pos,
                                        final Entity entity,
                                        final Vec3d crystal,
                                        final boolean ignoreTerrain) {
        final Box bb = entity.getBoundingBox();
        double dx = pos.getX() - bb.minX;
        double dy = pos.getY() - bb.minY;
        double dz = pos.getZ() - bb.minZ;
        final Box box = bb.offset(dx, dy, dz);
        //
        double ab = getExposure(crystal, box, ignoreTerrain);
        double w = Math.sqrt(pos.squaredDistanceTo(crystal)) / 12.0;
        double ac = (1.0 - w) * ab;
        double dmg = (float) ((int) ((ac * ac + ac) / 2.0 * 7.0 * 12.0 + 1.0));
        dmg = getReduction(entity, mc.world.getDamageSources().explosion(null), dmg);
        return Math.max(0.0, dmg);
    }

    private static double getReduction(Entity entity, DamageSource damageSource, double damage) {
        damage = mc.world.getDifficulty() == Difficulty.EASY ? Math.min(damage / 2 + 1, damage) : mc.world.getDifficulty() == Difficulty.HARD ? damage * 3 / 2 : damage;
        if (entity instanceof LivingEntity livingEntity) {
            damage = DamageUtil.getDamageLeft(livingEntity, (float) damage, mc.world.getDamageSources().explosion(null), (float) livingEntity.getArmor(), (float) livingEntity.getAttributeInstance(EntityAttributes.ARMOR_TOUGHNESS).getValue());
            damage *= livingEntity.hasStatusEffect(StatusEffects.RESISTANCE) ? 1 - ((livingEntity.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1) * 0.2) : 1;
            damage = DamageUtil.getInflictedDamage((float) damage, getProtectionAmount(livingEntity.getArmorItems()));
        }
        return Math.max(damage, 0);
    }

    public static int getProtectionAmount(Iterable<ItemStack> armor) {
        int x = 0;
        for (ItemStack stack : armor) {
            x += getProtectionAmount(stack);
        }

        return x;
    }

    public static int getProtectionAmount(ItemStack armor) {
        int x = 0;
        ItemEnchantmentsComponent enchantments = EnchantmentHelper.getEnchantments(armor);
        for (RegistryEntry<Enchantment> enchantment : enchantments.getEnchantments()) {
            String id = enchantment.getIdAsString().replace("minecraft:", "");
            if (PROTECTION_MAP.containsKey(id)) {
                x += enchantments.getLevel(enchantment) * PROTECTION_MAP.get(id);
                break;
            }
        }

        return x;
    }

    private static <T extends LivingEntity> DefaultAttributeContainer getDefaultForEntity(T entity) {
        return DefaultAttributeRegistry.get((EntityType<? extends LivingEntity>) entity.getType());
    }

    private static float getExposure(final Vec3d source,
                                     final Entity entity,
                                     final boolean ignoreTerrain) {
        final Box box = entity.getBoundingBox();
        return getExposure(source, box, ignoreTerrain);
    }

    private static float getExposure(final Vec3d source,
                                     final Box box,
                                     final boolean ignoreTerrain) {
        RaycastFactory raycastFactory = getRaycastFactory(ignoreTerrain);

        double xDiff = box.maxX - box.minX;
        double yDiff = box.maxY - box.minY;
        double zDiff = box.maxZ - box.minZ;

        double xStep = 1 / (xDiff * 2 + 1);
        double yStep = 1 / (yDiff * 2 + 1);
        double zStep = 1 / (zDiff * 2 + 1);

        if (xStep > 0 && yStep > 0 && zStep > 0) {
            int misses = 0;
            int hits = 0;

            double xOffset = (1 - Math.floor(1 / xStep) * xStep) * 0.5;
            double zOffset = (1 - Math.floor(1 / zStep) * zStep) * 0.5;

            xStep = xStep * xDiff;
            yStep = yStep * yDiff;
            zStep = zStep * zDiff;

            double startX = box.minX + xOffset;
            double startY = box.minY;
            double startZ = box.minZ + zOffset;
            double endX = box.maxX + xOffset;
            double endY = box.maxY;
            double endZ = box.maxZ + zOffset;

            for (double x = startX; x <= endX; x += xStep) {
                for (double y = startY; y <= endY; y += yStep) {
                    for (double z = startZ; z <= endZ; z += zStep) {
                        Vec3d position = new Vec3d(x, y, z);

                        if (raycast(new ExposureRaycastContext(position, source), raycastFactory) == null) misses++;

                        hits++;
                    }
                }
            }

            return (float) misses / hits;
        }

        return 0f;
    }

    private static RaycastFactory getRaycastFactory(boolean ignoreTerrain) {
        if (ignoreTerrain) {
            return (context, blockPos) -> {
                BlockState blockState = mc.world.getBlockState(blockPos);
                if (blockState.getBlock().getBlastResistance() < 600) return null;

                return blockState.getCollisionShape(mc.world, blockPos).raycast(context.start(), context.end(), blockPos);
            };
        } else {
            return (context, blockPos) -> {
                BlockState blockState = mc.world.getBlockState(blockPos);
                return blockState.getCollisionShape(mc.world, blockPos).raycast(context.start(), context.end(), blockPos);
            };
        }
    }

    private static BlockHitResult raycast(ExposureRaycastContext context, RaycastFactory raycastFactory) {
        return BlockView.raycast(context.start, context.end, context, raycastFactory, ctx -> null);
    }

    public record ExposureRaycastContext(Vec3d start, Vec3d end) {}

    @FunctionalInterface
    public interface RaycastFactory extends BiFunction<ExposureRaycastContext, BlockPos, BlockHitResult> {}
}
