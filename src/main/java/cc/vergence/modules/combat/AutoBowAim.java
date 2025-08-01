package cc.vergence.modules.combat;

import cc.vergence.Vergence;
import cc.vergence.features.enums.player.RotateModes;
import cc.vergence.features.enums.player.TargetTypes;
import cc.vergence.features.event.events.LookDirectionEvent;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.combat.CombatUtil;
import cc.vergence.util.rotation.Rotation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;

import java.util.EnumSet;

public class AutoBowAim extends Module {
    public static AutoBowAim INSTANCE;
    private Entity aimTarget;

    public AutoBowAim() {
        super("AutoBowAim", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<EnumSet<TargetTypes>> targets = addOption(new MultipleOption<TargetTypes>("Targets", EnumSet.of(TargetTypes.EnemyPlayers)));
    public Option<Double> range = addOption(new DoubleOption("Range", 1, 70, 30, v -> !targets.getValue().isEmpty()));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onLookDirection(LookDirectionEvent event, Entity entity, double cursorDeltaX, double cursorDeltaY) {
        if (aimTarget != null) {
            event.cancel();
        }
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (isNull()) {
            return ;
        }

        aimTarget = null;
        if (mc.player.getMainHandStack().getItem() instanceof BowItem
                && mc.player.getItemUseTime() >= 3) {
            double minDist = Double.MAX_VALUE;
            for (Entity entity : mc.world.getEntities()) {
                if (entity == null || entity == mc.player || !entity.isAlive()
                        || !CombatUtil.isValidTarget(entity, targets.getValue(), range.getValue())
                        || entity.getDisplayName() != null && Vergence.FRIEND.isFriend(entity.getDisplayName().getString())) {
                    continue;
                }
                double dist = mc.player.distanceTo(entity);
                if (dist < minDist) {
                    minDist = dist;
                    aimTarget = entity;
                }
            }
            if (aimTarget instanceof LivingEntity target) {
                float[] rotations = getBowRotationsTo(target);
                Vergence.ROTATE.rotate(new Rotation(rotations[1], rotations[0], RotateModes.Client));
            }
        }
    }

    private float[] getBowRotationsTo(Entity entity) {
        float duration = (float) (mc.player.getActiveItem().getMaxUseTime(mc.player) - mc.player.getItemUseTime()) / 20.0f;
        duration = (duration * duration + duration * 2.0f) / 3.0f;
        if (duration >= 1.0f) {
            duration = 1.0f;
        }
        double duration1 = duration * 3.0f;
        double coeff = 0.05000000074505806;
        float pitch = (float)(-Math.toDegrees(calculateArc(entity, duration1, coeff)));
        double ix = entity.getX() - entity.prevX;
        double iz = entity.getZ() - entity.prevZ;
        double d = mc.player.distanceTo(entity);
        d -= d % 2.0;
        ix = d / 2.0 * ix * (mc.player.isSprinting() ? 1.3 : 1.1);
        iz = d / 2.0 * iz * (mc.player.isSprinting() ? 1.3 : 1.1);
        float yaw = (float)Math.toDegrees(Math.atan2(entity.getZ() + iz - mc.player.getZ(), entity.getX() + ix - mc.player.getX())) - 90.0f;
        return new float[] { yaw, pitch };
    }

    private float calculateArc(Entity target, double duration, double coeff) {
        double yArc = target.getY() + (double)(target.getStandingEyeHeight() / 2.0f) - (mc.player.getY() + (double)mc.player.getStandingEyeHeight());
        double dX = target.getX() - mc.player.getX();
        double dZ = target.getZ() - mc.player.getZ();
        double dirRoot = Math.sqrt(dX * dX + dZ * dZ);
        return calculateArc(duration, coeff, dirRoot, yArc);
    }

    private float calculateArc(double duration, double coeff, double root, double yArc) {
        double dirCoeff = coeff * (root * root);
        yArc = 2.0 * yArc * (duration * duration);
        yArc = coeff * (dirCoeff + yArc);
        yArc = Math.sqrt(duration * duration * duration * duration - yArc);
        duration = duration * duration - yArc;
        yArc = Math.atan2(duration * duration + yArc, coeff * root);
        duration = Math.atan2(duration, coeff * root);
        return (float)Math.min(yArc, duration);
    }
}
