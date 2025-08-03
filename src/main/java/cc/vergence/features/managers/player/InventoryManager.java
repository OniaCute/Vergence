package cc.vergence.features.managers.player;

import cc.vergence.Vergence;
import cc.vergence.features.event.eventbus.EventHandler;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.util.interfaces.Wrapper;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;

public class InventoryManager implements Wrapper {
    public static int serverSlot = -1;

    public InventoryManager() {
        Vergence.EVENTBUS.subscribe(this);
    }

    @EventHandler
    public void onSendPackets(PacketEvent.Send event) {
        if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket packet) {
            serverSlot = packet.getSelectedSlot();
        }
    }

    @EventHandler
    public void onReceivedPackets(PacketEvent.Receive event) {
        if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket packet) {
            serverSlot = packet.getSelectedSlot();
        }
    }

    public void setSlot(int slot) {
        if (serverSlot != slot && PlayerInventory.isValidHotbarIndex(slot)) {
            Vergence.NETWORK.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
        }
    }

    public void setClientSlot(int slot) {
        if (mc.player.getInventory().selectedSlot != slot && PlayerInventory.isValidHotbarIndex(slot)) {
            mc.player.getInventory().selectedSlot = slot;
            setSlot(slot);
        }
    }

    public boolean isSync() {
        return mc.player.getInventory().selectedSlot == serverSlot;
    }

    public void close() {
        Vergence.NETWORK.sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
    }

    private void click(int slot, int button, SlotActionType type) {
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

    public int count(Item item) {
        ItemStack offhandStack = mc.player.getOffHandStack();
        int itemCount = offhandStack.getItem() == item ? offhandStack.getCount() : 0;
        for (int i = 0; i < 36; i++) {
            ItemStack slot = mc.player.getInventory().getStack(i);
            if (slot.getItem() == item) {
                itemCount += slot.getCount();
            }
        }
        return itemCount;
    }

    public int getServerSlot() {
        return serverSlot;
    }

    public ItemStack getServerItem() {
        if (mc.player != null && getServerSlot() != -1) {
            return mc.player.getInventory().getStack(getServerSlot());
        }
        return null;
    }

    public void pickup(int slot) {
        click(slot, 0, SlotActionType.PICKUP);
    }
}
