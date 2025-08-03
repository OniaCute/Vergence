package cc.vergence.modules.player;

import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.FastTimerUtil;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.*;

public class ChestStealer extends Module implements Wrapper {
    public static ChestStealer INSTANCE;
    private final FastTimerUtil timer = new FastTimerUtil();

    public ChestStealer() {
        super("ChestStealer", Category.PLAYER);
        INSTANCE = this;
    }

    public Option<Double> maxDelay = addOption(new DoubleOption("MaxDelay", 0, 1400, 500).setUnit("ms"));
    public Option<Double> minDelay = addOption(new DoubleOption("MinDelay", 0, 1400, 200).setUnit("ms"));
    public Option<Boolean> random = addOption(new BooleanOption("Random", true));
    public Option<Boolean> close = addOption(new BooleanOption("Close", true));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    @Override
    public void onDisable() {
        timer.reset();
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (!(mc.currentScreen instanceof GenericContainerScreen)) {
            return;
        }
        if (!(mc.player.currentScreenHandler instanceof net.minecraft.screen.GenericContainerScreenHandler container)) {
            return;
        }
        List<Integer> filledSlots = new ArrayList<>();
        for (int i = 0; i < container.getInventory().size(); i++) {
            Slot slot = container.getSlot(i);
            if (slot.hasStack()) {
                filledSlots.add(i);
            }
        }
        if (filledSlots.isEmpty()) {
            if (close.getValue()) {
                mc.player.closeHandledScreen();
            }
            return;
        }
        double delay = random.getValue() ? minDelay.getValue() + RANDOM.nextDouble(maxDelay.getValue() - minDelay.getValue() + 1) : maxDelay.getValue();
        if (!timer.passedMs(delay)) {
            return;
        }
        int slotIndex = random.getValue() ? filledSlots.get(RANDOM.nextInt(filledSlots.size())) : filledSlots.get(0);

        mc.interactionManager.clickSlot(container.syncId, slotIndex, 0, SlotActionType.QUICK_MOVE, mc.player);
        timer.reset();
    }
}
