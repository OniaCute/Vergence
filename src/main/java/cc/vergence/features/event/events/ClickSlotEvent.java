package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;
import net.minecraft.screen.slot.SlotActionType;

public class ClickSlotEvent extends Event {
    private final SlotActionType slotActionType;
    private final int slot, button, id;

    public ClickSlotEvent(SlotActionType slotActionType, int slot, int button, int id) {
        this.slot = slot;
        this.button = button;
        this.id = id;
        this.slotActionType = slotActionType;
    }

    public SlotActionType getSlotActionType() {
        return slotActionType;
    }

    public int getSlot() {
        return slot;
    }

    public int getButton() {
        return button;
    }

    public int getId() {
        return id;
    }
}