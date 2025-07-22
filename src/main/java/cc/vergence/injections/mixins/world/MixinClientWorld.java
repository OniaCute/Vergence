package cc.vergence.injections.mixins.world;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.EntityRemoveEvent;
import cc.vergence.features.event.events.EntitySpawnEvent;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class MixinClientWorld implements Wrapper {
    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    public void addEntityHook(Entity entity, CallbackInfo ci) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        EntitySpawnEvent event = new EntitySpawnEvent(entity);
        Vergence.EVENTBUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "addEntity", at = @At("RETURN"), cancellable = true)
    public void addEntityHookPost(Entity entity, CallbackInfo ci) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        EntitySpawnEvent event = new EntitySpawnEvent(entity);
        Vergence.EVENTBUS.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "removeEntity", at = @At("HEAD"))
    public void removeEntityHook(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci) {
        if(mc.player == null || mc.world == null) {
            return ;
        }
        EntityRemoveEvent event = new EntityRemoveEvent(mc.world.getEntityById(entityId));
        Vergence.EVENTBUS.post(event);
    }
}