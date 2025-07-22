package cc.vergence.modules.player;

import cc.vergence.Vergence;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;

public class Timer extends Module {
    public static Timer INSTANCE;

    public Timer() {
        super("Timer", Category.PLAYER);
        INSTANCE = this;
    }

    public Option<Double> timerScale = addOption(new DoubleOption("TimerScale", 0.05, 7, 1));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onOptionValueUpdate() {
        Vergence.TIMER.set(timerScale.getValue());
    }

    @Override
    public void onDisable() {
        Vergence.TIMER.set(1);
    }

    @Override
    public void onEnable() {
        Vergence.TIMER.set(timerScale.getValue());
    }
}
