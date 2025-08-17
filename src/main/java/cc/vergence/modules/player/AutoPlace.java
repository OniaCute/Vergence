package cc.vergence.modules.player;

import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.combat.AutoClicker;
import cc.vergence.util.blocks.BlockUtil;
import cc.vergence.util.interfaces.IRightClick;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.other.RandomUtil;
import cc.vergence.util.player.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import java.util.Objects;

public class AutoPlace extends Module {
    public static AutoPlace INSTANCE;
    private FastTimerUtil timer = new FastTimerUtil();

    public AutoPlace() {
        super("AutoPlace", Category.PLAYER);
        INSTANCE = this;
    }

    public Option<Enum<?>> clickMode = addOption(new EnumOption("ClickMode", AutoClicker.ClickModes.Press));
    public Option<Double> minDelay = addOption(new DoubleOption("MinDelay", 1, 950, 30).setUnit("ms"));
    public Option<Double> maxDelay = addOption(new DoubleOption("MaxDelay", 1, 950, 60).setUnit("ms"));
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
        mc.options.attackKey.setPressed(false);
        mc.options.useKey.setPressed(false);
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (!inScreen.getValue() && mc.currentScreen != null || isNull()) {
            return;
        }
        if (!(mc.player.getMainHandStack().getItem() instanceof BlockItem)) {
            return ;
        }
        switch (((AutoClicker.ClickModes) clickMode.getValue())) {
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

    public void rightClick() {
        ((IRightClick) mc).vergence$rightClick();
    }

    public enum ClickModes {
        Hold,
        Press
    }
}
