package cc.vergence.modules.player;

import cc.vergence.Vergence;
import cc.vergence.features.enums.player.SwapModes;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BindOption;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.player.InventoryUtil;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.*;

public class HotbarHelper extends Module {
    public static HotbarHelper INSTANCE;
    public boolean sortFinished = false;
    public static FastTimerUtil timer = new FastTimerUtil();

    public HotbarHelper() {
        super("HotbarHelper", Category.PLAYER);
        INSTANCE = this;
    }

    public Option<Boolean> autoSort = addOption(new BooleanOption("AutoSort", false));
    public Option<Enum<?>> slotMode = addOption(new EnumOption("SlotMode", SwapModes.Pickup, v -> autoSort.getValue()));
    public Option<Double> maxDelay = addOption(new DoubleOption("MaxDelay", 0, 2000, 670, v -> autoSort.getValue()).setUnit("ms"));
    public Option<Double> minDelay = addOption(new DoubleOption("MinDelay", 0, 2000, 430, v -> autoSort.getValue()).setUnit("ms"));
    public Option<Boolean> fastSwitch = addOption(new BooleanOption("FastSwitch", true));
    public Option<Boolean> switchCheck = addOption(new BooleanOption("SwitchCheck", false, v -> fastSwitch.getValue()));
    public Option<Integer> slot_0_bind = addOption(new BindOption("Slot_0_Bind", 1, BindOption.BindType.Click, v -> fastSwitch.getValue()));
    public Option<Enum<?>> slot_0 = addOption(new EnumOption("Slot_0", ItemTypes.Sword, v -> fastSwitch.getValue() || autoSort.getValue()));
    public Option<Integer> slot_1_bind = addOption(new BindOption("Slot_1_Bind", 1, BindOption.BindType.Click, v -> fastSwitch.getValue()));
    public Option<Enum<?>> slot_1 = addOption(new EnumOption("Slot_1", ItemTypes.Block, v -> fastSwitch.getValue() || autoSort.getValue()));
    public Option<Integer> slot_2_bind = addOption(new BindOption("Slot_2_Bind", 1, BindOption.BindType.Click, v -> fastSwitch.getValue()));
    public Option<Enum<?>> slot_2 = addOption(new EnumOption("Slot_2", ItemTypes.Food, v -> fastSwitch.getValue() || autoSort.getValue()));
    public Option<Integer> slot_3_bind = addOption(new BindOption("Slot_3_Bind", 1, BindOption.BindType.Click, v -> fastSwitch.getValue()));
    public Option<Enum<?>> slot_3 = addOption(new EnumOption("Slot_3", ItemTypes.WoodStick, v -> fastSwitch.getValue() || autoSort.getValue()));
    public Option<Integer> slot_4_bind = addOption(new BindOption("Slot_4_Bind", 1, BindOption.BindType.Click, v -> fastSwitch.getValue()));
    public Option<Enum<?>> slot_4 = addOption(new EnumOption("Slot_4", ItemTypes.Fireball, v -> fastSwitch.getValue() || autoSort.getValue()));
    public Option<Integer> slot_5_bind = addOption(new BindOption("Slot_5_Bind", 1, BindOption.BindType.Click, v -> fastSwitch.getValue()));
    public Option<Enum<?>> slot_5 = addOption(new EnumOption("Slot_5", ItemTypes.Other, v -> fastSwitch.getValue() || autoSort.getValue()));
    public Option<Integer> slot_6_bind = addOption(new BindOption("Slot_6_Bind", 1, BindOption.BindType.Click, v -> fastSwitch.getValue()));
    public Option<Enum<?>> slot_6 = addOption(new EnumOption("Slot_6", ItemTypes.EnderPearl, v -> fastSwitch.getValue() || autoSort.getValue()));
    public Option<Integer> slot_7_bind = addOption(new BindOption("Slot_7_Bind", 1, BindOption.BindType.Click, v -> fastSwitch.getValue()));
    public Option<Enum<?>> slot_7 = addOption(new EnumOption("Slot_7", ItemTypes.SunFlower, v -> fastSwitch.getValue() || autoSort.getValue()));
    public Option<Integer> slot_8_bind = addOption(new BindOption("Slot_8_Bind", 1, BindOption.BindType.Click, v -> fastSwitch.getValue()));
    public Option<Enum<?>> slot_8 = addOption(new EnumOption("Slot_8", ItemTypes.Feather, v -> fastSwitch.getValue() || autoSort.getValue()));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onEnable() {
        timer.reset();
        sortFinished = false;
    }

    @Override
    public void onDisable() {
        timer.reset();
        sortFinished = false;
    }

    @Override
    public void onKeyboardActive(int key, int action) { 
        if (!fastSwitch.getValue() || isNull()) {
            return ;
        }
        if (action == 1) {
            for (int i = 0; i < 9; i++) {
                if (getOptionHashMap().get("Slot_" + i + "_Bind") instanceof BindOption bindOption && bindOption.getValue().equals(key)) {
                    Option<Enum<?>> slotOption = ((EnumOption) getOptionHashMap().get("Slot_" + i));
                    if (isCorrect(mc.player.getInventory().getStack(i).getItem(), asItemType(slotOption)) || !switchCheck.getValue()) {
                        Vergence.INVENTORY.setClientSlot(i);
                    }
                }
            }
        }
    }

    @Override
    public void onMouseActive(int button, int action) {
        if (!fastSwitch.getValue() || isNull()) {
            return ;
        }
        if (action == 1) {
            for (int i = 0; i < 9; i++) {
                if (getOptionHashMap().get("Slot_" + i + "_Bind") instanceof BindOption bindOption && bindOption.getValue().equals(button)) {
                    Option<Enum<?>> slotOption = ((EnumOption) getOptionHashMap().get("Slot_" + i));
                    if (isCorrect(mc.player.getInventory().getStack(i).getItem(), asItemType(slotOption)) || !switchCheck.getValue()) {
                        Vergence.INVENTORY.setClientSlot(i);
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (isNull() || !autoSort.getValue() || !(mc.currentScreen instanceof InventoryScreen)) {
            return ;
        }

        double mind = minDelay.getValue();
        double maxd = maxDelay.getValue();
        if (mind > maxd) {
            double temp = mind;
            mind = maxd;
            maxd = temp;
        }
        double delay = mind + RANDOM.nextDouble() * (maxd - mind);

        if (timer.passedMs(delay)) {
            autoSlotItems();
            timer.reset();
        }
    }

    private void autoSlotItems() {
        for (int i = 0; i < 9; i++) {
            Option<Enum<?>> slotOption = ((EnumOption) getOptionHashMap().get("Slot_" + i));
            ItemTypes itemType = asItemType(slotOption);
            ItemStack currentStack = mc.player.getInventory().getStack(i);

            if (!isCorrect(currentStack.getItem(), itemType)) {
                if (!currentStack.isEmpty()) {
                    int emptySlot = InventoryUtil.findEmptySlot();
                    if (emptySlot != -1) {
                        InventoryUtil.doSwap(((SwapModes) slotMode.getValue()), i, emptySlot);
                    }
                }
                int targetSlot = findItemInInventory(itemType);
                if (targetSlot != -1) {
                    InventoryUtil.doSwap(((SwapModes) slotMode.getValue()), targetSlot, i);
                }
            }
        }
    }

    private int findItemInInventory(ItemTypes itemType) {
        for (int i = 9; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!stack.isEmpty() && isCorrect(stack.getItem(), itemType)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isCorrect(Item item, ItemTypes types) {
        switch (types) {
            case Sword -> {
                return InventoryUtil.isSword(item);
            }
            case Axe -> {
                return item instanceof AxeItem;
            }
            case Pickaxe -> {
                return item instanceof PickaxeItem;
            }
            case WoodStick -> {
                return item.equals(Items.STICK);
            }
            case Scissors -> {
                return item.equals(Items.SHEARS);
            }
            case Food -> {
                return InventoryUtil.isFood(item);
            }
            case Block -> {
                return (item instanceof BlockItem);
            }
            case Potion -> {
                return (item instanceof PotionItem);
            }
            case EnderPearl -> {
                return item.equals(Items.ENDER_PEARL);
            }
            case SnowBall -> {
                return item.equals(Items.SNOWBALL);
            }
            case Fireball -> {
                return item.equals(Items.FIRE_CHARGE);
            }
            case Feather -> {
                return item.equals(Items.FEATHER);
            }
            case SunFlower -> {
                return item.equals(Items.SUNFLOWER);
            }
            case Empty -> {
                return item == null;
            }
            default -> {
                return true;
            }
        }
    }

    public ItemTypes asItemType(Option<Enum<?>> option) {
        return ((ItemTypes) option.getValue());
    }

    public enum ItemTypes {
        Sword,
        Pickaxe,
        Axe,
        WoodStick,
        Scissors,
        Food,
        Block,
        Potion,
        EnderPearl,
        Fireball,
        SnowBall,
        Feather,
        SunFlower,
        Other,
        Empty
    }
}
