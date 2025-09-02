package cc.vergence.modules.combat;

import cc.vergence.features.event.events.DisconnectEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.modules.Module;
import cc.vergence.util.combat.CombatUtil;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.other.RandomUtil;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.player.InventoryUtil;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

import java.util.Objects;

public class AutoArmor extends Module {
    public static AutoArmor INSTANCE;
    private int ticks = 0;
    private final FastTimerUtil timer = new FastTimerUtil();

    public AutoArmor() {
        super("AutoArmor", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Enum<?>> health = addOption(new EnumOption("Health", Modes.Highest));
    public Option<Double> minDelay = addOption(new DoubleOption("MinDelay", 0, 3000, 400).setUnit("ms"));
    public Option<Double> maxDelay = addOption(new DoubleOption("MaxDelay", 0, 3000, 900).setUnit("ms"));
    public Option<Boolean> inventoryOnly = addOption(new BooleanOption("InventoryOnly", false));
    public Option<Boolean> elytraPriority = addOption(new BooleanOption("ElytraPriority", true));
    public Option<Boolean> preserve = addOption(new BooleanOption("Preserve", false));
    public Option<Double>  preserveHealth = addOption(new DoubleOption("PreserveHealth", 10, 50, 20, v -> preserve.getValue()));
    public Option<Boolean> elytra = addOption(new BooleanOption("Elytra", false));
    public Option<Boolean> smartElytra = addOption(new BooleanOption("SmartElytra", false, v -> elytra.getValue()));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDisable() {
        timer.reset();
        ticks = 0;
    }

    @Override
    public void onDisconnect(DisconnectEvent event, String reason) {
        timer.reset();
        ticks = 0;
    }

    @Override
    public void onLogout() {
        timer.reset();
        ticks = 0;
    }

    @Override
    public void onConfigChange() {
        timer.reset();
        ticks = 0;
    }

    @Override
    public void onTick() {
        if (isNull()) {
            return ;
        }

        if (smartElytra.getValue() && elytra.getValue()) {
            if ((mc.player.isOnGround() && !(mc.player.getMainHandStack().getItem() instanceof ExperienceBottleItem) || EntityUtil.isInWeb(mc.player))) {
                elytra.setValue(false);
            }
        }

        if (ticks <= 0) {
            if (InventoryUtil.inOtherScreen()) {
                return;
            }
            if (inventoryOnly.getValue()) {
                if (!(mc.currentScreen instanceof InventoryScreen)) {
                    return;
                }
            } else {
                if (mc.currentScreen != null && !(mc.currentScreen instanceof InventoryScreen)) {
                    return;
                }
            }
            update(EquipmentSlot.HEAD, 5);
            if (!(elytraPriority.getValue() && mc.player.getInventory().getStack(38).getItem() == Items.ELYTRA) || elytra.getValue()) {
                update(EquipmentSlot.CHEST, 6);
            }
            update(EquipmentSlot.LEGS, 7);
            update(EquipmentSlot.FEET, 8);
        }
    }

    private void update(EquipmentSlot type, int x) {
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

        if (!timer.passedMs(delay)) {
            return ;
        }

        int elytraSlot = findElytra();
        boolean flag = elytra.getValue() && type == EquipmentSlot.CHEST;

        int slot = type == EquipmentSlot.HEAD ? 39 : type == EquipmentSlot.CHEST ? 38 : type == EquipmentSlot.LEGS ? 37 : 36;
        int armor = flag ? elytraSlot : findArmor(type);
        int best = flag ? compareElytra(38, armor) : compare(slot, armor, true);

        if (armor != -1 && best != slot) {
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, x, 0, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, InventoryUtil.indexToSlot(armor), 0, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, x, 0, SlotActionType.PICKUP, mc.player);

            ticks = 2 + (int) (EntityUtil.getLatency(mc.player) / 25.0f);
        }

        timer.reset();
    }

    private int compare(int x, int y, boolean swap) {
        if (y == -1) {
            return x;
        }
        if (!(mc.player.getInventory().getStack(x).getItem() instanceof ArmorItem)) {
            return y;
        }

        ItemStack stackX = mc.player.getInventory().getStack(x);
        ItemStack stackY = mc.player.getInventory().getStack(y);

        int protX = CombatUtil.getProtectionAmount(stackX);
        int protY = CombatUtil.getProtectionAmount(stackY);
        if (protX < protY) {
            return y;
        }
        if (preserve.getValue() && getDurability(x) < preserveHealth.getValue().floatValue()) {
            if (getDurability(x) < getDurability(y)) {
                return y;
            }
        }
        if (!swap) {
            if (health.getValue().equals(Modes.Lowest) && getDurability(x) > getDurability(y)) {
                return y;
            }
        }

        return x;
    }

    private int compareElytra(int x, int y) {
        if (y == -1) {
            return x;
        }
        if (mc.player.getInventory().getStack(x).getItem() != Items.ELYTRA) {
            return y;
        }
        if (health.getValue().equals(Modes.Highest) && getDurability(x) < getDurability(y)) {
            return y;
        }
        if (health.getValue().equals(Modes.Lowest) && getDurability(x) > getDurability(y)) {
            return y;
        }

        return x;
    }

    private int findArmor(EquipmentSlot type) {
        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!(stack.getItem() instanceof ArmorItem)) {
                continue;
            }
            if (getSlotType(stack).equals(type)) {
                slot = compare(i, slot, false);
            }
        }
        return slot;
    }

    private int findElytra() {
        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if(stack.getItem() != Items.ELYTRA) {
                continue;
            }
            slot = compareElytra(i, slot);
        }
        return slot;
    }

    private float getDurability(int slot) {
        ItemStack stack = mc.player.getInventory().getStack(slot);
        return ((stack.getMaxDamage() - stack.getDamage()) * 100.0f) / stack.getMaxDamage();
    }

    private EquipmentSlot getSlotType(ItemStack itemStack) {
        if (itemStack.contains(DataComponentTypes.GLIDER)) return EquipmentSlot.CHEST;
        return itemStack.get(DataComponentTypes.EQUIPPABLE).slot();
    }

    private enum Modes {
        Highest,
        Lowest,
        Any
    }
}