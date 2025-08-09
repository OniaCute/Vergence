package cc.vergence.injections.mixins.screen;

import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.other.SkiaContext;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class MixinWindow implements Wrapper {
    @Inject(method = "onFramebufferSizeChanged", at = @At("RETURN"))
    private void onFramebufferSizeChanged(long window, int width, int height, CallbackInfo ci) {
        SkiaContext.createSurface(width, height);
    }
}
