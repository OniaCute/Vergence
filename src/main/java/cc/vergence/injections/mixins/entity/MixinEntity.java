package cc.vergence.injections.mixins.entity;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.LookDirectionEvent;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity implements Wrapper {
    @Inject(method = "changeLookDirection", at = @At(value = "HEAD"), cancellable = true)
    private void hookChangeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if ((Object) this == mc.player) {
            LookDirectionEvent lookDirectionEvent = new LookDirectionEvent((Entity) (Object) this, cursorDeltaX, cursorDeltaY);
            Vergence.EVENTBUS.post(lookDirectionEvent);
            if (lookDirectionEvent.isCancelled()) {
                ci.cancel();
            }
        }
    }
}
