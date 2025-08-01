package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;
import net.minecraft.entity.Entity;

public class LookDirectionEvent extends Event {
    private final Entity entity;
    private final double cursorDeltaX, cursorDeltaY;

    public LookDirectionEvent(final Entity entity, double cursorDeltaX, double cursorDeltaY) {
        this.entity = entity;
        this.cursorDeltaX = cursorDeltaX;
        this.cursorDeltaY = cursorDeltaY;
    }

    public Entity getEntity() {
        return entity;
    }

    public double getCursorDeltaX() {
        return cursorDeltaX;
    }

    public double getCursorDeltaY() {
        return cursorDeltaY;
    }
}
