package cc.vergence.injections.mixins.entity;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.LookDirectionEvent;
import cc.vergence.features.event.events.UpdateVelocityEvent;
import cc.vergence.modules.movement.Velocity;
import cc.vergence.util.interfaces.Wrapper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity implements Wrapper {
    @Shadow public abstract void setVelocity(Vec3d velocity);

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

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    private void pushAwayFrom(Entity entity, CallbackInfo info) {
        if ((Object) this == mc.player && Velocity.INSTANCE.getStatus() && Velocity.INSTANCE.antiPush.getValue()) {
            info.cancel();
        }
    }

    @ModifyExpressionValue(method = "updateMovementInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getVelocity()Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d updateMovementInFluid(Vec3d vec3d) {
        if ((Object) this == mc.player && Velocity.INSTANCE.getStatus() && Velocity.INSTANCE.antiLiquidPush.getValue()) {
            return new Vec3d(0, 0, 0);
        }
        return vec3d;
    }

    @Inject(method = "updateVelocity", at = @At("HEAD"), cancellable = true)
    private void updateVelocity(float speed, Vec3d movementInput, CallbackInfo info) {
        if ((Object) this != mc.player) {
            return;
        }
        UpdateVelocityEvent event = new UpdateVelocityEvent(movementInput, speed);
        Vergence.EVENTBUS.post(event);
        if (event.isCancelled()) {
            info.cancel();
            if (!event.isCancelled()) {
                setVelocity(new Vec3d((double) mc.player.getVelocity().x / 8000.0, (double) mc.player.getVelocity().y / 8000.0, (double) mc.player.getVelocity().z / 8000.0));
            }
        }
    }
}
