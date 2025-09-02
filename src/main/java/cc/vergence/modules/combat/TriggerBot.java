package cc.vergence.modules.combat;

import cc.vergence.features.enums.player.TargetTypes;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.combat.CombatUtil;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.other.RandomUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.EnumSet;
import java.util.Objects;

public class TriggerBot extends Module {
    public static TriggerBot INSTANCE;
    private final FastTimerUtil timer = new FastTimerUtil();

    public TriggerBot() {
        super("TriggerBot", Category.COMBAT);
    }

    public Option<EnumSet<TargetTypes>> targets = addOption(new MultipleOption<TargetTypes>("Targets", EnumSet.of(TargetTypes.EnemyPlayers)));
    public Option<Double> minDelay = addOption(new DoubleOption("MinDelay", 1, 950, 30).setUnit("ms"));
    public Option<Double> maxDelay = addOption(new DoubleOption("MaxDelay", 1, 950, 60).setUnit("ms"));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onTick() {
        if (isNull()) {
            return ;
        }
        if (mc.crosshairTarget == null || !mc.crosshairTarget.getType().equals(HitResult.Type.ENTITY)) {
            return;
        }
        if (!CombatUtil.isValidTarget(((EntityHitResult) mc.crosshairTarget).getEntity(), targets.getValue(), 100)) {
            return ;
        }

        double delay = maxDelay.getValue();
        if (!Objects.equals(maxDelay.getValue(), minDelay.getValue())) {
            double min = minDelay.getValue();
            double max = maxDelay.getValue();
            if (min > max) {
                double temp = min;
                min = max;
                max = temp;
            }
            delay = RandomUtil.getDouble(min, max);
        }
        if (timer.passedMs(delay)) {
            AutoClicker.INSTANCE.click(false);
            timer.reset();
        }
    }
}
