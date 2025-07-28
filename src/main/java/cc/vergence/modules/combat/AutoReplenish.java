package cc.vergence.modules.combat;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.player.InventoryUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class AutoReplenish extends Module {
    public static AutoReplenish INSTANCE;

    public AutoReplenish() {
        super("AutoReplenish", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Double> limit = addOption(new DoubleOption("Limit", 1, 80, 20));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onTick() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isEmpty() || !stack.isStackable()) {
                continue;
            }
            double stackPercent = ((float) stack.getCount() / stack.getMaxCount()) * 100.0f;
            if (stack.getCount() == 1 || stackPercent <= Math.max(limit.getValue(), 5.0f)) {
                replenishStack(stack, i);
            }
        }
    }

    private void replenishStack(ItemStack item, int hotbarSlot) {
        int total = item.getCount();
        for (int i = 9; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!stack.getName().equals(item.getName())) {
                continue;
            }
            if (stack.getItem() instanceof BlockItem blockItem && (!(item.getItem() instanceof BlockItem blockItem1) || blockItem.getBlock() != blockItem1.getBlock())) {
                continue;
            }
            if (stack.getItem() != item.getItem()) {
                continue;
            }
            if (total < stack.getMaxCount()) {
                InventoryUtil.pickupSlot(i);
                InventoryUtil.pickupSlot(hotbarSlot + 36);
                if (!mc.player.currentScreenHandler.getCursorStack().isEmpty()) {
                    InventoryUtil.pickupSlot(i);
                }
                total += stack.getCount();
            }
        }
    }
}
