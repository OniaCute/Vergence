package cc.vergence.injections.mixins.hardware;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.KeyboardTickEvent;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin implements Wrapper {
    @Inject(method = "tick", at = @At(value = "TAIL", shift = At.Shift.BEFORE), cancellable = true)
    private void onSneak(CallbackInfo ci) {
        KeyboardTickEvent event = new KeyboardTickEvent();
        Vergence.EVENTBUS.post(event);
        if (event.isCancel()) {
            ci.cancel();
        }
    }
}
