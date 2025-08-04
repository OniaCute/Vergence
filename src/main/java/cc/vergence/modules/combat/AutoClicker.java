package cc.vergence.modules.combat;

import cc.vergence.features.enums.player.Hands;
import cc.vergence.features.enums.player.SwingModes;
import cc.vergence.features.enums.player.TargetTypes;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.combat.CombatUtil;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.player.EntityUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.EnumSet;

public class AutoClicker extends Module {
    public static AutoClicker INSTANCE;
    private boolean cooldownFinished = false;
    private boolean pressing = false;
    private int passedTicks = 0;
    private FastTimerUtil timer = new FastTimerUtil();

    public AutoClicker() {
        super("AutoClicker", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Enum<?>> mode = addOption(new EnumOption("Mode", Modes.New));
    public Option<EnumSet<TargetTypes>> targets = addOption(new MultipleOption<TargetTypes>("Targets", EnumSet.of(TargetTypes.EnemyPlayers, TargetTypes.Mobs)));
    public Option<Enum<?>> swingMode = addOption(new EnumOption("Swing", SwingModes.Client));
    public Option<Double> minExtraDelay = addOption(new DoubleOption("MinExtraDelay", 0, 200, 10, v -> mode.getValue().equals(Modes.New)).setUnit("ms"));
    public Option<Double> maxExtraDelay = addOption(new DoubleOption("MaxExtraDelay", 0, 200, 20, v -> mode.getValue().equals(Modes.New)).setUnit("ms"));
    public Option<Double> minCPS = addOption(new DoubleOption("MinCPS", 1, 22, 4, v -> mode.getValue().equals(Modes.Old)));
    public Option<Double> maxCPS = addOption(new DoubleOption("MaxCPS", 1, 22, 7, v -> mode.getValue().equals(Modes.Old)));
    public Option<Double> minTicks = addOption(new DoubleOption("MinTicks", 1, 20, 3));
    public Option<Double> maxTicks = addOption(new DoubleOption("MaxTicks", 1, 20, 4));
    public Option<Boolean> always = addOption(new BooleanOption("Always", false));
    public Option<Boolean> packetAttack = addOption(new BooleanOption("PacketAttack", false));

    @Override
    public String getDetails() {
        return mode.getValue().name();
    }

    @Override
    public void onEnable() {
        timer.reset();
        passedTicks = 0;
    }

    @Override
    public void onDisable() {
        timer.reset();
        passedTicks = 0;
        mc.options.attackKey.setPressed(false);
    }

    @Override
    public void onTick() {
        passedTicks ++;
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (isNull() || mc.interactionManager == null || mc.interactionManager.isBreakingBlock()) {
            return ;
        }
        int minDelay = minTicks.getValue().intValue();
        int maxDelay = maxTicks.getValue().intValue();
        if (minDelay > maxDelay) {
            int temp = minDelay;
            minDelay = maxDelay;
            maxDelay = temp;
        }
        int delay = minDelay + RANDOM.nextInt(maxDelay - minDelay + 1);
        if (pressing && mc.options.attackKey.isPressed() || mc.crosshairTarget == null) {
            mc.options.attackKey.setPressed(false);
            pressing = false;
            passedTicks = 0;
        }
        if (mc.crosshairTarget == null || (mc.crosshairTarget.getType() != HitResult.Type.ENTITY && !always.getValue())) {
            return;
        }
        cooldownFinished = mc.player.getAttackCooldownProgress(0) >= 1;
        if (always.getValue() || mc.crosshairTarget.getType() == HitResult.Type.ENTITY && CombatUtil.isValidTarget(((EntityHitResult) mc.crosshairTarget).getEntity(), targets.getValue(), Reach.INSTANCE.getStatus() ? Reach.INSTANCE.range.getValue() : 100)) {
            if (mode.getValue().equals(Modes.New)) {
                double minDelay1 = minExtraDelay.getValue();
                double maxDelay1 = maxExtraDelay.getValue();
                if (minDelay1 > maxDelay1) {
                    double temp1 = minDelay1;
                    minDelay1 = maxDelay1;
                    maxDelay1 = temp1;
                }
                double delay1 = minDelay1 + RANDOM.nextDouble(maxDelay1 - minDelay1 + 1);
                if (timer.passedMs(delay1) && cooldownFinished) {
                    click();
                    timer.reset();
                }
            }
            else if (mode.getValue().equals(Modes.Old)) {
                double minDelay1 = 1000.0 / maxCPS.getValue();
                double maxDelay1 = 1000.0 / minCPS.getValue();
                if (minDelay1 > maxDelay1) {
                    double temp1 = minDelay1;
                    minDelay1 = maxDelay1;
                    maxDelay1 = temp1;
                }
                double delay1 = minDelay1 + RANDOM.nextDouble(maxDelay1 - minDelay1 + 1);
                if (timer.passedMs(delay1)) {
                    click();
                    timer.reset();
                }
            }
        }
    }

    private void click() {
        if (mc.player != null) {
            if (packetAttack.getValue() && mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
                mc.interactionManager.attackEntity(mc.player, ((EntityHitResult) mc.crosshairTarget).getEntity());
            }
            mc.options.attackKey.setPressed(true);
            pressing = true;
            EntityUtil.swingHand(Hands.MainHand, ((SwingModes) swingMode.getValue()));
        }
    }

    public enum Modes {
        New,
        Old
    }
}
