package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;
import net.minecraft.entity.LivingEntity;

public class AttackEvent extends Event {
    private LivingEntity entity;

    public AttackEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }
}
