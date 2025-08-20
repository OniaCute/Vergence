package cc.vergence.modules.player;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.ClickBlockEvent;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.modules.Module;
import cc.vergence.util.player.InventoryUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.util.Pair;
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
        if (isNull()) {
            return;
        }

        BlockState state = mc.world.getBlockState(pos);
        if (state == null || state.isAir() || getBestToolStack(state) == null) {
            return;
        }

        Item bestTool = getBestToolStack(state).getItem();

        if (slotBack.getValue()) {
            originalSlot = mc.player.getInventory().selectedSlot;
        }

        if (mc.player.getInventory().getStack(originalSlot) == getBestToolStack(state)) {
            originalSlot = -1;
        }

        int slot = InventoryUtil.findHotbarSlot(stack -> stack.getItem() == bestTool);
        if (slot != -1 && mc.player.getMainHandStack() != getBestToolStack(state)) {
            Vergence.INVENTORY.setClientSlot(slot);
            switched = true;
        }
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (isNull()) {
            return ;
        }

        if (switched && originalSlot != -1 && Vergence.MINE.isMining()) {
            HitResult crosshairTarget = mc.crosshairTarget;
            if (crosshairTarget != null && crosshairTarget.getType().equals(HitResult.Type.BLOCK)) {
                BlockPos pos = new BlockPos((int) crosshairTarget.getPos().x, (int) crosshairTarget.getPos().y, (int) crosshairTarget.getPos().z);
                BlockState state = mc.world.getBlockState(pos);
                if (state == null || state.isAir() || getBestToolStack(state) == null) {
                    return;
                }

                Item bestTool = getBestToolStack(state).getItem();

                int slot = InventoryUtil.findHotbarSlot(stack -> stack.getItem() == bestTool);
                if (slot != -1 && mc.player.getMainHandStack() != getBestToolStack(state)) {
                    Vergence.INVENTORY.setClientSlot(slot);
                    switched = true;
                }
            }
        }

//        MessageManager.newMessage(this, "Mining: " + (Vergence.MINE.isMining() ? "YES" : "NO"), -4);
//        MessageManager.newMessage(this, "Type: " + (mc.crosshairTarget == null ? "Null" : mc.crosshairTarget.getType().name()), -3);
//        MessageManager.newMessage(this, "Switched: " + (switched ? "YES" : "NO"), -2);
//        MessageManager.newMessage(this, "OriginalSlot: " + originalSlot, -1);

        if (slotBack.getValue() && (!Vergence.MINE.isMining() || mc.crosshairTarget == null || !mc.crosshairTarget.getType().equals(HitResult.Type.BLOCK)) && originalSlot != -1) {
            Vergence.INVENTORY.setClientSlot(originalSlot);
            switched = false;
            originalSlot = -1;
        }
    }

    @Override
    public void onDisable() {
        switched = false;
        originalSlot = -1;
    }

    @Override
    public void onEnable() {
        switched = false;
        originalSlot = -1;
    }

    private ItemStack getBestToolStack(BlockState state) {
        float bestSpeed = 1.0F;
        ItemStack bestItem = null;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = InventoryUtil.getStack(i);
            if (stack.isEmpty()) {
                continue;
            }
            float speed = stack.getMiningSpeedMultiplier(state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestItem = stack;
            }
        }
        return bestItem;
    }
}
