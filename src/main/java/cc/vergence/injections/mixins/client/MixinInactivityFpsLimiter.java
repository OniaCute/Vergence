package cc.vergence.injections.mixins.client;

import cc.vergence.modules.misc.UnfocusedFPS;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.InactivityFpsLimiter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InactivityFpsLimiter.class)
public class MixinInactivityFpsLimiter {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void getFramerateLimit(CallbackInfoReturnable<Integer> info) {
        if (UnfocusedFPS.INSTANCE.getStatus() && !client.isWindowFocused()) {
            info.setReturnValue(UnfocusedFPS.INSTANCE.fpsLimit.getValue().intValue());
        }
    }
}