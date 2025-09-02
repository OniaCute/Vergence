package cc.vergence.modules.player;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.modules.Module;
import cc.vergence.util.combat.CombatUtil;
import cc.vergence.util.other.RandomUtil;
import cc.vergence.util.player.InventoryUtil;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.*;
import net.minecraft.screen.slot.SlotActionType;

import java.util.EnumSet;
import java.util.Objects;

public class InventoryCleaner extends Module {
    public static InventoryCleaner INSTANCE;

    public InventoryCleaner() {
        super("InventoryCleaner", Category.PLAYER);
        INSTANCE = this;
    }

    public Option<EnumSet<Items>> items = addOption(new MultipleOption<Items>("Items", EnumSet.of(Items.Armor, Items.Other)));
    public Option<Double> minDelay = addOption(new DoubleOption("MinDelay", 0, 3000, 400).setUnit("ms"));
    public Option<Double> maxDelay = addOption(new DoubleOption("MaxDelay", 0, 3000, 900).setUnit("ms"));
    public Option<Boolean> includeHotbar = addOption(new BooleanOption("Hotbar", true));
    public Option<Boolean> inventoryOnly = addOption(new BooleanOption("InventoryOnly", false));
    public Option<Boolean> keepBest = addOption(new BooleanOption("KeepBest", true));
    public Option<Boolean> goldenApple = addOption(new BooleanOption("GoldenApple", true, v -> items.getValue().contains(Items.Foods)));

    private long lastCleanTime = 0;

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onTick() {
        if (isNull()) {
            return;
        }

        if (inventoryOnly.getValue() && !(mc.currentScreen instanceof InventoryScreen)) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCleanTime < minDelay.getValue()) {
            return;
        }

        lastCleanTime = currentTime;
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
        if (currentTime - lastCleanTime < delay) {
            return;
        }

        int start = includeHotbar.getValue() ? 0 : 9;
        int end = 36;

        for (int i = start; i < end; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isEmpty()) {
                continue;
            }

            Items itemType = getItemType(stack);
            if (items.getValue().contains(itemType)) {
                if (keepBest.getValue() && isBestItem(stack, itemType)) {
                    continue;
                }
                mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, i, 0, SlotActionType.THROW, mc.player);
            }
        }
    }

    private Items getItemType(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ArmorItem) {
            return Items.Armor;
        } else if (item instanceof SwordItem) {
            return Items.Sword;
        } else if (InventoryUtil.isFood(stack)) {
            return Items.Foods;
        } else {
            return Items.Other;
        }
    }

    private boolean isBestItem(ItemStack stack, Items itemType) {
        int bestSlot = -1;
        int bestProtect = -1;
        float bestAttackDamage = 0;

        for (int i = 0; i < 36; i++) {
            ItemStack otherStack = mc.player.getInventory().getStack(i);
            if (otherStack.isEmpty() || getItemType(otherStack) != itemType) {
                continue;
            }

            switch (itemType) {
                case Armor:
                    if (CombatUtil.getProtectionAmount(stack) >= bestProtect) {
                        bestProtect = CombatUtil.getProtectionAmount(stack);
                        bestSlot = i;
                    }
                    break;
                case Sword:
                    if (otherStack.getItem() instanceof SwordItem sword) {
                        float attackDamage = otherStack.getDamage();
                        if (attackDamage > bestAttackDamage) {
                            bestSlot = i;
                            bestAttackDamage = attackDamage;
                        }
                    }
                    break;
                case Foods:
                    if (goldenApple.getValue() && (stack.isOf(net.minecraft.item.Items.GOLDEN_APPLE) || stack.isOf(net.minecraft.item.Items.ENCHANTED_GOLDEN_APPLE))) {
                        return true;
                    }
                    break;
            }
        }

        return bestSlot == -1;
    }

    public enum Items {
        Armor,
        Sword,
        Foods,
        Other
    }
}