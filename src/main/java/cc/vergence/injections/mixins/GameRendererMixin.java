package cc.vergence.injections.mixins;

import cc.vergence.modules.visual.FOVModifier;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "getFov", at = @At("TAIL"), cancellable = true)
    private void getFOV(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> info) {
        if(FOVModifier.INSTANCE != null && FOVModifier.INSTANCE.getStatus()) {
            if(info.getReturnValue() == 70 && !FOVModifier.INSTANCE.forItem.getValue()) return;
            info.setReturnValue(FOVModifier.INSTANCE.fov.getValue().floatValue());
        }
    }
}
