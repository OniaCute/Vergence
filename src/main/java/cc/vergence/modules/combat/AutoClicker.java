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
import cc.vergence.injections.accessors.client.MinecraftClientAccessor;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.IRightClick;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.other.RandomUtil;
import cc.vergence.util.player.EntityUtil;

import java.util.EnumSet;
import java.util.Objects;

public class AutoClicker extends Module {
    public static AutoClicker INSTANCE;
    private FastTimerUtil timer = new FastTimerUtil();
    private FastTimerUtil timerRight = new FastTimerUtil();

    public AutoClicker() {
        super("AutoClicker", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<EnumSet<Modes>> mode = addOption(new MultipleOption<Modes>("Mode", EnumSet.of(Modes.Left)));
    public Option<Enum<?>> clickMode = addOption(new EnumOption("ClickMode", ClickModes.Press));
    public Option<Enum<?>> swingMode = addOption(new EnumOption("Swing", SwingModes.Client));
    public Option<Double> minDelay = addOption(new DoubleOption("MinDelay", 1, 550, 30).setUnit("ms"));
    public Option<Double> maxDelay = addOption(new DoubleOption("MaxDelay", 1, 550, 60).setUnit("ms"));
    public Option<Boolean> inScreen = addOption(new BooleanOption("InScreen", false));
    public Option<Boolean> debug = addOption(new BooleanOption("Debug", false));

    @Override
    public String getDetails() {
        return debug.getValue() ? clickMode.getValue().name() : "";
    }

    @Override
    public void onEnable() {
        reset();
    }

    @Override
    public void onDisable() {
        reset();
    }

    @Override
    public void onConfigChange() {
        reset();
    }

    private void reset() {
        timer.reset();
        timerRight.reset();
        mc.options.attackKey.setPressed(false);
        mc.options.useKey.setPressed(false);
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (!inScreen.getValue() && mc.currentScreen != null || isNull()) {
            return;
        }

        if (mode.getValue().contains(Modes.Left)) {
            switch (((ClickModes) clickMode.getValue())) {
                case Hold -> mc.options.attackKey.setPressed(true);
                case Press -> {
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
                        click();
                        timer.reset();
                    }
                }
            }
        }

        if (mode.getValue().contains(Modes.Right)) {
            switch (((ClickModes) clickMode.getValue())) {
                case Hold -> mc.options.useKey.setPressed(true);
                case Press -> {
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
                        rightClick();
                        timer.reset();
                    }
                }
            }
        }
    }

    private void click() {
        int attackCooldown = ((MinecraftClientAccessor) mc).getAttackCooldown();
        if (attackCooldown == 10000) {
            ((MinecraftClientAccessor) mc).setAttackCooldown(0);
        }
        mc.options.attackKey.setPressed(true);
        ((MinecraftClientAccessor) mc).leftClick();
        mc.options.attackKey.setPressed(false);

        EntityUtil.swingHand(Hands.MainHand, ((SwingModes) swingMode.getValue()));
    }

    public void rightClick() {
        ((IRightClick) mc).vergence$rightClick();
        EntityUtil.swingHand(Hands.MainHand, ((SwingModes) swingMode.getValue()));
    }

    public enum ClickModes {
        Hold,
        Press
    }

    public enum Modes {
        Left,
        Right
    }
}
