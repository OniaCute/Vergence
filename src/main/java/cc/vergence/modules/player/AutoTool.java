package cc.vergence.modules.player;

import cc.vergence.features.event.events.ClickBlockEvent;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.modules.Module;
import cc.vergence.util.player.InventoryUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class AutoTool extends Module {
    public static AutoTool INSTANCE;
    private boolean switched = false;
    private int originalSlot = -1;

    public Option<Boolean> slotBack = addOption(new BooleanOption("SlotBack", true));

    public AutoTool() {
        super("AutoTool", Category.PLAYER);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return switched ? "Switched" : (slotBack.getValue() ? "SlotBacked" : "Released");
    }

    @Override
    public void onClickBlockEvent(ClickBlockEvent event, BlockPos pos) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        BlockState state = mc.world.getBlockState(pos);
        if (state == null || state.isAir()) {
            return;
        }
        Item current = mc.player.getMainHandStack().getItem();
        Item bestTool = getBestTool(state);

        if (current == bestTool) {
            if (switched && slotBack.getValue()) {
                switched = false;
                originalSlot = -1;
            }
            return;
        }

        int slot = InventoryUtil.findHotbarSlot(stack -> stack.getItem() == bestTool);
        if (slot != -1) {
            if (!switched && slotBack.getValue()) {
                originalSlot = mc.player.getInventory().selectedSlot;
            }
            InventoryUtil.setSlotBoth(slot);
            switched = true;
        }
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (!switched || !slotBack.getValue() || originalSlot == -1) {
            return;
        }
        if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) {
            InventoryUtil.setSlotBoth(originalSlot);
            originalSlot = -1;
            switched = false;
        }
    }

    @Override
    public void onDisable() {
        switched = false;
        originalSlot = -1;
    }

    private Item getBestTool(BlockState state) {
        float bestSpeed = 1.0F;
        Item bestItem = null;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = InventoryUtil.getStack(i);
            if (stack.isEmpty()) {
                continue;
            }
            float speed = stack.getMiningSpeedMultiplier(state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestItem = stack.getItem();
            }
        }
        return bestItem;
    }
}
