package cc.vergence.util.player;

import cc.vergence.features.enums.player.SwapModes;
import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.blocks.BlockUtil;
import cc.vergence.util.interfaces.Wrapper;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PickItemFromEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;

import java.util.*;

public class InventoryUtil implements Wrapper {
    static int lastSlot = -1;
    static int lastSelect = -1;
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

    public static void doSwap(int slot) {
        mc.player.getInventory().selectedSlot = slot;
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
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

    public static int getItemCount(Item item) {
        int count = 0;
        for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue().getItem() != item) continue;
            count = count + entry.getValue().getCount();
        }
        return count;
    }
    public static boolean isHoldingItem(Class clazz) {
        boolean result;
        ItemStack stack = mc.player.getMainHandStack();
        result = isInstanceOf(stack, clazz);
        if (!result) {
            result = isInstanceOf(stack, clazz);
        }
        return result;
    }

    public static boolean isSword(Item item) {
        return item == Items.WOODEN_SWORD ||
                item == Items.STONE_SWORD   ||
                item == Items.IRON_SWORD    ||
                item == Items.GOLDEN_SWORD  ||
                item == Items.DIAMOND_SWORD ||
                item == Items.NETHERITE_SWORD;
    }

    public static boolean isInstanceOf(ItemStack stack, Class clazz) {
        if (stack == null) {
            return false;
        }
        Item item = stack.getItem();
        if (clazz.isInstance(item)) {
            return true;
        }
        if (item instanceof BlockItem) {
            Block block = Block.getBlockFromItem(item);
            return clazz.isInstance(block);
        }
        return false;
    }
    public static ItemStack getStackInSlot(int i) {
        return mc.player.getInventory().getStack(i);
    }
    public static int findItem(Item input) {
        for (int i = 0; i < 9; ++i) {
            Item item = getStackInSlot(i).getItem();
            if (Item.getRawId(item) != Item.getRawId(input)) continue;
            return i;
        }
        return -1;
    }

    public static int findClass(Class clazz) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = getStackInSlot(i);
            if (stack == ItemStack.EMPTY) continue;
            if (clazz.isInstance(stack.getItem())) {
                return i;
            }
            if (!(stack.getItem() instanceof BlockItem) || !clazz.isInstance(((BlockItem) stack.getItem()).getBlock()))
                continue;
            return i;
        }
        return -1;
    }

    public static int findClassInventorySlot(Class clazz) {
        for (int i = 0; i < 45; ++i) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack == ItemStack.EMPTY) continue;
            if (clazz.isInstance(stack.getItem())) {
                return i < 9 ? i + 36 : i;
            }
            if (!(stack.getItem() instanceof BlockItem) || !clazz.isInstance(((BlockItem) stack.getItem()).getBlock()))
                continue;
            return i < 9 ? i + 36 : i;
        }
        return -1;
    }

    public static int findBlock(Block blockIn) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof BlockItem) || ((BlockItem) stack.getItem()).getBlock() != blockIn)
                continue;
            return i;
        }
        return -1;
    }

    public static int findUnBlock() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = getStackInSlot(i);
            if (stack.getItem() instanceof BlockItem)
                continue;
            return i;
        }
        return -1;
    }

    public static int findBlock() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = getStackInSlot(i);
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

    public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<>();

        for (int current = 0; current <= 44; ++current) {
            fullInventorySlots.put(current, mc.player.getInventory().getStack(current));
        }

        return fullInventorySlots;
    }

    public static void switchToSlot(int slot) {
        mc.player.getInventory().selectedSlot = slot;
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
    }

    public static void sendServerSlot(int slot) {
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
    }

    public static void setClientSlot(final int barSlot) {
        if (mc.player.getInventory().selectedSlot != barSlot && PlayerInventory.isValidHotbarIndex(barSlot)) {
            mc.player.getInventory().selectedSlot = barSlot;
            sendServerSlot(barSlot);
        }
    }

    public static int findHotbarSlot(java.util.function.Predicate<ItemStack> filter) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!stack.isEmpty() && filter.test(stack)) return i;
        }
        return -1;
    }

    public static void switchToSlot(java.util.function.Predicate<ItemStack> filter) {
        int slot = findHotbarSlot(filter);
        if (slot != -1) {
            switchToSlot(slot);
        }
    }

    public static void pickupSlot(final int slot) {
        click(slot, 0, SlotActionType.PICKUP);
    }

    public static void click(int slot, int button, SlotActionType type) {
        ScreenHandler screenHandler = mc.player.currentScreenHandler;
        DefaultedList<Slot> defaultedList = screenHandler.slots;
        int i = defaultedList.size();
        ArrayList<ItemStack> list = Lists.newArrayListWithCapacity(i);
        for (Slot slot1 : defaultedList) {
            list.add(slot1.getStack().copy());
        }
        screenHandler.onSlotClick(slot, button, type, mc.player);
        Int2ObjectOpenHashMap<ItemStack> int2ObjectMap = new Int2ObjectOpenHashMap<>();
        for (int j = 0; j < i; ++j) {
            ItemStack itemStack2;
            ItemStack itemStack = list.get(j);
            if (ItemStack.areEqual(itemStack, itemStack2 = defaultedList.get(j).getStack())) continue;
            int2ObjectMap.put(j, itemStack2.copy());
        }
        mc.player.networkHandler.sendPacket(new ClickSlotC2SPacket(screenHandler.syncId, screenHandler.getRevision(), slot, button, type, screenHandler.getCursorStack().copy(), int2ObjectMap));
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
        if (AntiCheat.INSTANCE.inventorySync.getValue()) {
            mc.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
        }
    }

    public static void inventorySwap(int slot, int selectedSlot) {
        if (slot == lastSlot) {
            switchToSlot(lastSelect);
            lastSlot = -1;
            lastSelect = -1;
            return;
        }
        if (slot - 36 == selectedSlot) return;
        if (AntiCheat.INSTANCE.inventoryBypass.getValue()) {
            if (slot - 36 >= 0) {
                lastSlot = slot;
                lastSelect = selectedSlot;
                switchToSlot(slot - 36);
                return;
            }
            doSwap(SwapModes.Pickup, mc.player.getInventory().selectedSlot, selectedSlot);
            return;
        }
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slot, selectedSlot, SlotActionType.SWAP, mc.player);
    }
}

