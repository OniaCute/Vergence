package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.entity.Entity;

public class RenderEntityEvent extends Event {
    private final Entity entity;
    private VertexConsumerProvider vertexConsumers;

    public RenderEntityEvent(Entity entity, VertexConsumerProvider vertexConsumers) {
        this.entity = entity;
        this.vertexConsumers = vertexConsumers;
    }

    public Entity getEntity() {
        return entity;
    }

    public VertexConsumerProvider getVertexConsumers() {
        return vertexConsumers;
    }
}
