package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;
import net.minecraft.entity.Entity;

public class EntityRemoveEvent extends Event {
    private final Entity entity;

    public EntityRemoveEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}
