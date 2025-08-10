package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;
import net.minecraft.entity.LivingEntity;

public class HurtEvent extends Event {
    private final LivingEntity entity;

    public HurtEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }
}
