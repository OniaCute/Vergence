package cc.vergence.modules.movement;

import cc.vergence.Vergence;
import cc.vergence.features.enums.SpeedUnit;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.player.EntityUtil;
import net.minecraft.util.Formatting;

public class TickShift extends Module {
    public static TickShift INSTANCE;
    private int ticks = 0;
    private int waiting = 0;

    public TickShift() {
        super("TickShift", Category.MOVEMENT);
        INSTANCE = this;
    }

    public Option<Double> safeDistance = addOption(new DoubleOption("SafeDistance", 0, 7, 5).addSpecialValue(0, "INFINITY"));
    public Option<Double> maxTicks = addOption(new DoubleOption("MaxTicks", 1, 60, 1));
    public Option<Double> delay = addOption(new DoubleOption("Delay", 1, 10, 4));
    public Option<Double> timerSpeed = addOption(new DoubleOption("TimerSpeed", 1, 10, 1.2));

    @Override
    public String getDetails() {
        return (ticks >= maxTicks.getValue().intValue() ? "Available" : "Counting") + " | " + ticks;
    }

    @Override
    public void onDisable() {
        if (isNull()) {
            return ;
        }
        reset();
    }

    @Override
    public void onEnable() {
        if (isNull()) {
            return ;
        }
        reset();
    }

    public void reset() {
        Vergence.TIMER.set(1);
        ticks = 0;
        waiting = 0;
    }

    @Override
    public void onTick() {
        if (isNull()) {
            return ;
        }

        if (mc.player.fallDistance >= safeDistance.getValue() && safeDistance.getValue().intValue() != 0) {
            return;
        }

        if ((mc.player.sidewaysSpeed == 0.0f && mc.player.forwardSpeed == 0.0f && mc.player.fallDistance == 0.0f) || EntityUtil.getSpeed(mc.player, SpeedUnit.KILOMETERS) <= 5) {
            Vergence.TIMER.set(1);
            if(waiting >= delay.getValue().intValue()) {
                if(ticks < maxTicks.getValue().intValue()) {
                    ticks++;
                }
                waiting = 0;
            }
            waiting++;
        } else {
            if(ticks > 0) {
                if (!mc.options.jumpKey.isPressed()) {
                    Vergence.TIMER.set(timerSpeed.getValue());
                }
                ticks--;
            } else {
                reset();
            }
        }
    }
}
