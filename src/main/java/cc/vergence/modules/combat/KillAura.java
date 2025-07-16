package cc.vergence.modules.combat;

import cc.vergence.Vergence;
import cc.vergence.features.enums.AntiCheats;
import cc.vergence.features.enums.RotateModes;
import cc.vergence.features.enums.TargetTypes;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.combat.CombatUtil;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.rotation.Rotation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;
import java.util.Random;

public class KillAura extends Module implements Wrapper {
    public static KillAura INSTANCE;

    public KillAura() {
        super("KillAura", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Enum<?>> antiCheat = addOption(new EnumOption("AntiCheat", AntiCheats.Legit));
    public Option<EnumSet<TargetTypes>> targets = addOption(new MultipleOption<TargetTypes>("Targets", EnumSet.of(TargetTypes.EnemyPlayers, TargetTypes.Mobs)));
    public Option<Enum<?>> clickType = addOption(new EnumOption("ClickType", ClickTypes.New));
    public Option<Double> range = addOption(new DoubleOption("Range", 1, 7, 3));
    public Option<Double> fov = addOption(new DoubleOption("FOV", 1, 180, 180));
    public Option<Double> minCPS = addOption(new DoubleOption("MinCPS", 1, 18, 3, v -> clickType.getValue().equals(ClickTypes.Old)));
    public Option<Double> maxCPS = addOption(new DoubleOption("MaxCPS", 1, 18, 7, v -> clickType.getValue().equals(ClickTypes.Old)));
    public Option<Double> delay = addOption(new DoubleOption("Delay", 1, 18, 7, v -> clickType.getValue().equals(ClickTypes.New)).addSpecialValue(1, "INSTANT"));
    public Option<Boolean> crosshairLock = addOption(new BooleanOption("CrosshairLock", true));
    public Option<Enum<?>> rotateType = addOption(new EnumOption("RotateType", RotateModes.Server));
    public Option<Double> rotateSpeed = addOption(new DoubleOption("RotateSpeed", 1, 1200, 200).addSpecialValue(1, "INSTANT"));
    public Option<Boolean> smoothRotate = addOption(new BooleanOption("SmoothRotate", true));
    public Option<Double> smoothOffset = addOption(new DoubleOption("SmoothOffset", 1, 100, 36, v -> smoothRotate.getValue()));
    public Option<Boolean> rotateLock = addOption(new BooleanOption("RotateLock", true));
    public Option<Double> rotateLockTime = addOption(new DoubleOption("RotateLockTime", 10, 300, 40, v -> rotateLock.getValue()).setUnit("ms"));

    private long lastAttackTime = 0;
    private long lastRotateTime = 0;
    private final Random random = new Random();

    @Override
    public String getDetails() {
        return antiCheat.getValue().name() + " | " + clickType.getValue().name();
    }

    @Override
    public void onTick() {
        if (mc.player == null || !antiCheat.getValue().equals(AntiCheats.Legit)) {
            return;
        }

        if (rotateLock.getValue() && System.currentTimeMillis() - lastRotateTime < rotateLockTime.getValue()) {
            return;
        }

        LivingEntity target = CombatUtil.getClosestAnyTarget(range.getValue(), targets.getValue());
        if (target == null) return;

        float angle = CombatUtil.getYawTo(target) - mc.player.getYaw();
        angle = Math.abs(((angle + 180) % 360) - 180);
        if (angle > fov.getValue()) return;

        Runnable attack = () -> {
            if (clickType.getValue().equals(ClickTypes.Old)) {
                int cps = random.nextInt((int)(maxCPS.getValue() - minCPS.getValue() + 1)) + minCPS.getValue().intValue();
                long interval = 1000L / cps;
                if (System.currentTimeMillis() - lastAttackTime >= interval) {
                    CombatUtil.attack(target);
                    lastAttackTime = System.currentTimeMillis();
                    lastRotateTime = System.currentTimeMillis();
                }
            } else {
                long delayMs = delay.getValue() == 1 ? 0 : (long)(1000 / delay.getValue());
                if (System.currentTimeMillis() - lastAttackTime >= delayMs) {
                    CombatUtil.attack(target);
                    lastAttackTime = System.currentTimeMillis();
                    lastRotateTime = System.currentTimeMillis();
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

        Rotation rotation;
        if (smoothRotate.getValue()) {
            double offset = smoothOffset.getValue();
            double jitter = random.nextDouble() * offset;
            rotation = new Rotation(pitch, yaw, rotateSpeed.getValue(), jitter, mode, 1);
        } else {
            rotation = new Rotation(pitch, yaw, rotateSpeed.getValue(), mode, 1);
        }

        Vergence.ROTATE.rotate(rotation, attack);
    }

    public enum ClickTypes {
        Old,
        New
    }
}