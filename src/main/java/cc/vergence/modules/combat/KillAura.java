package cc.vergence.modules.combat;

import cc.vergence.Vergence;
import cc.vergence.features.enums.player.RotateModes;
import cc.vergence.features.enums.player.SwingModes;
import cc.vergence.features.enums.player.TargetTypes;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.combat.CombatUtil;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.rotation.Rotation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;
import java.util.Random;

public class KillAura extends Module implements Wrapper {
    public static KillAura INSTANCE;

    public KillAura() {
        super("KillAura", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<EnumSet<TargetTypes>> targets = addOption(new MultipleOption<>("Targets", EnumSet.of(TargetTypes.EnemyPlayers, TargetTypes.Mobs)));
    public Option<Enum<?>> clickType = addOption(new EnumOption("ClickType", ClickTypes.New));
    public Option<Double> range = addOption(new DoubleOption("Range", 1, 7, 3));
    public Option<Double> fov = addOption(new DoubleOption("FOV", 1, 180, 180, v -> !AntiCheat.INSTANCE.isGrim()));
    public Option<Double> minCPS = addOption(new DoubleOption("MinCPS", 1, 18, 3, v -> clickType.getValue().equals(ClickTypes.Old)));
    public Option<Double> maxCPS = addOption(new DoubleOption("MaxCPS", 1, 18, 7, v -> clickType.getValue().equals(ClickTypes.Old)));
    public Option<Double> delay = addOption(new DoubleOption("Delay", 1, 18, 7, v -> clickType.getValue().equals(ClickTypes.New)).addSpecialValue(1, "INSTANT"));
    public Option<Boolean> crosshairLock = addOption(new BooleanOption("CrosshairLock", true, v -> AntiCheat.INSTANCE.isLegit()));
    public Option<Enum<?>> rotateType = addOption(new EnumOption("RotateType", RotateModes.Server));
    public Option<Double> rotateSpeed = addOption(new DoubleOption("RotateSpeed", 1, 180, 180).addSpecialValue(1, "INSTANT"));
    public Option<Boolean> rotateLock = addOption(new BooleanOption("RotateLock", true, v -> !AntiCheat.INSTANCE.isGrim()));
    public Option<Double> rotateLockTime = addOption(new DoubleOption("RotateLockTime", 1, 300, 40, v -> rotateLock.getValue() && !AntiCheat.INSTANCE.isGrim()).setUnit("ms"));
    public Option<Enum<?>> swingMode = addOption(new EnumOption("SwingMode", SwingModes.Client));

    private long lastAttackTime = 0;
    private long lastRotateTime = 0;
    private long cooldownReadyTime = 0;

    private final Random random = new Random();

    @Override
    public String getDetails() {
        return AntiCheat.INSTANCE.antiCheat.getValue().name() + " | " + clickType.getValue().name();
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) {
            return;
        }

        if (clickType.getValue().equals(ClickTypes.Old)) {
            if (minCPS.getValue() > maxCPS.getValue()) {
                maxCPS.setValue(minCPS.getValue());
            }
        }

        if (clickType.getValue().equals(ClickTypes.New)) {
            if (mc.player.getAttackCooldownProgress(0) >= 1.0F && cooldownReadyTime == 0) {
                cooldownReadyTime = System.currentTimeMillis();
            } else if (mc.player.getAttackCooldownProgress(0) < 1.0F) {
                cooldownReadyTime = 0;
            }
        }

        if (AntiCheat.INSTANCE.isLegit()) {
            legitAction();
        }
    }

    private void legitAction() {
        if (rotateLock.getValue() && System.currentTimeMillis() - lastRotateTime < rotateLockTime.getValue()) {
            return;
        }

        LivingEntity target = CombatUtil.getClosestAnyTarget(range.getValue(), targets.getValue());
        if (target == null) {
            return;
        }
        if (target.hurtTime > 0) {
            return;
        }

        float playerYaw = mc.player.getYaw();
        if (Vergence.ROTATE.inRenderTime()) {
            playerYaw = Vergence.ROTATE.getRenderRotations()[0];
        }

        float angle = Math.abs(MathHelper.wrapDegrees(CombatUtil.getYawTo(target) - playerYaw));
        if (angle > fov.getValue()) return;

        Runnable attack = () -> {
            if (clickType.getValue().equals(ClickTypes.Old)) {
                int cps = random.nextInt((int)(maxCPS.getValue() - minCPS.getValue() + 1)) + minCPS.getValue().intValue();
                long interval = 1000L / cps;
                if (System.currentTimeMillis() - lastAttackTime >= interval) {
                    CombatUtil.attack(target, ((SwingModes) swingMode.getValue()), true);
                    lastAttackTime = System.currentTimeMillis();
                    lastRotateTime = System.currentTimeMillis();
                }
            } else {
                long delayMs = delay.getValue() == 1 ? 0 : (long)(1000 / delay.getValue());
                if (cooldownReadyTime > 0 && System.currentTimeMillis() - cooldownReadyTime >= delayMs) {
                    CombatUtil.attack(target, ((SwingModes) swingMode.getValue()), true);
                    lastAttackTime = System.currentTimeMillis();
                    lastRotateTime = System.currentTimeMillis();
                    cooldownReadyTime = 0;
                }
            }
        };

        if (crosshairLock.getValue() && CombatUtil.isCrosshairOnEntity(target, 15)) {
            attack.run();
            return;
        }

        float yaw = CombatUtil.getYawTo(target);
        float pitch = CombatUtil.getPitchTo(target);
        RotateModes mode = (RotateModes) rotateType.getValue();

        Rotation rotation = new Rotation(pitch, yaw, rotateSpeed.getValue(), mode, this.getPriority());
        Vergence.ROTATE.rotate(rotation, attack);
    }

    public enum ClickTypes {
        Old, New
    }
}
