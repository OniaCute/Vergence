package cc.vergence.util.player;

import cc.vergence.features.enums.player.SwapModes;
import cc.vergence.features.managers.player.InventoryManager;
import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.blocks.BlockUtil;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

import java.util.*;

public class InventoryUtil implements Wrapper {
    private static final HashSet<Item> FOOD_ITEMS = new HashSet<>();
    static {
        FOOD_ITEMS.add(Items.APPLE);
        FOOD_ITEMS.add(Items.BAKED_POTATO);
        FOOD_ITEMS.add(Items.BREAD);
        FOOD_ITEMS.add(Items.CARROT);
        FOOD_ITEMS.add(Items.CHORUS_FRUIT);
        FOOD_ITEMS.add(Items.COOKIE);
        FOOD_ITEMS.add(Items.GOLDEN_APPLE);
        FOOD_ITEMS.add(Items.GOLDEN_CARROT);
        FOOD_ITEMS.add(Items.MELON_SLICE);
        FOOD_ITEMS.add(Items.POISONOUS_POTATO);
        FOOD_ITEMS.add(Items.POTATO);
        FOOD_ITEMS.add(Items.PUMPKIN_PIE);
        FOOD_ITEMS.add(Items.RABBIT_STEW);
        FOOD_ITEMS.add(Items.BEEF);
        FOOD_ITEMS.add(Items.CHICKEN);
        FOOD_ITEMS.add(Items.MUTTON);
        FOOD_ITEMS.add(Items.PORKCHOP);
        FOOD_ITEMS.add(Items.RABBIT);
        FOOD_ITEMS.add(Items.ROTTEN_FLESH);
        FOOD_ITEMS.add(Items.SPIDER_EYE);
        FOOD_ITEMS.add(Items.BROWN_MUSHROOM);
        FOOD_ITEMS.add(Items.RED_MUSHROOM);
        FOOD_ITEMS.add(Items.DRIED_KELP);
        FOOD_ITEMS.add(Items.SWEET_BERRIES);
        FOOD_ITEMS.add(Items.GLOW_BERRIES);
    }

    public static boolean isFood(ItemStack stack) {
        return FOOD_ITEMS.contains(stack.getItem());
    }

    public static boolean isFood(Item item) {
        return FOOD_ITEMS.contains(item);
    }

    public static boolean isThrowable(Item item) {
        return item instanceof EnderPearlItem || item instanceof TridentItem || item instanceof ExperienceBottleItem || item instanceof SnowballItem || item instanceof EggItem || item instanceof SplashPotionItem || item instanceof LingeringPotionItem;
    }

    public static void doSwap(SwapModes mode, int slot, int targetSlot) {
        switch (mode) {
            case Pickup -> {
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, indexToSlot(slot), 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, indexToSlot(targetSlot), 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, indexToSlot(slot), 0, SlotActionType.PICKUP, mc.player);
            }
            case Swap -> {
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, indexToSlot(slot), targetSlot, SlotActionType.SWAP, mc.player);
            }
        }
    }

    public static int indexToSlot(int index) {
        if (index >= 0 && index <= 8) return 36 + index;
        return index;
    }

    public static int countItems(Item item) {
        int count = 0;
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getAllSlots().entrySet()) {
            if (entry.getValue().getItem() != item) continue;
            count = count + entry.getValue().getCount();
        }
        return count;
    }

    public static boolean isSword(Item item) {
        return item == Items.WOODEN_SWORD ||
                item == Items.STONE_SWORD   ||
                item == Items.IRON_SWORD    ||
                item == Items.GOLDEN_SWORD  ||
                item == Items.DIAMOND_SWORD ||
                item == Items.NETHERITE_SWORD;
    }

    public static ItemStack getStack(int i) {
        return mc.player.getInventory().getStack(i);
    }

    public static int findItem(Item input) {
        for (int i = 0; i < 9; ++i) {
            Item item = getStack(i).getItem();
            if (Item.getRawId(item) != Item.getRawId(input)) continue;
            return i;
        }
        return -1;
    }

    public static int findBlock(Block blockIn) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = getStack(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof BlockItem) || ((BlockItem) stack.getItem()).getBlock() != blockIn)
                continue;
            return i;
        }
        return -1;
    }

    public static int findBlock() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = getStack(i);
            if (stack.getItem() instanceof BlockItem && !BlockUtil.shiftBlocks.contains(Block.getBlockFromItem(stack.getItem())) && ((BlockItem) stack.getItem()).getBlock() != Blocks.COBWEB)
                return i;
        }
        return -1;
    }

    public static int findBlockInventorySlot(Block block) {
        return findItemInventorySlot(Item.fromBlock(block));
    }

    public static int findItemInventorySlot(Item item) {
        for (int i = 0; i < 45; ++i) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == item) return i < 9 ? i + 36 : i;
        }
        return -1;
    }

    public static int findTotem() {
        int t =0;
        for (int i = 0; i < 45; ++i) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) t ++;
        }
        return t;
    }

    public static Map<Integer, ItemStack> getAllSlots() {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<>();

        for (int current = 0; current <= 44; ++current) {
            fullInventorySlots.put(current, mc.player.getInventory().getStack(current));
        }

        return fullInventorySlots;
    }

    public static void setSlotBoth(int slot) {
        mc.player.getInventory().selectedSlot = slot;
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
    }

    public static int findHotbarSlot(java.util.function.Predicate<ItemStack> filter) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!stack.isEmpty() && filter.test(stack)) return i;
        }
        return -1;
    }

    public static int findEmptySlot() {
        // hotbar
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).isEmpty()) {
                return i;
            }
        }
        // inv
        for (int i = 9; i < 36; i++) {
            if (mc.player.getInventory().getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    public static void syncInventory() {
        if (AntiCheat.INSTANCE.inventorySync.getValue() && InventoryManager.serverSlot != mc.player.getInventory().selectedSlot) {
            mc.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
        }
    }
}

