package cc.vergence.injections.mixins.render;

import cc.vergence.Vergence;
import cc.vergence.features.screens.ClickGuiScreen;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinMinecraftInGameHud implements Wrapper {
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        float tickDelta = tickCounter.getTickDelta(false);
        Vergence.EVENTS.onDraw2D(context, tickDelta);
    }


    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void onRenderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (mc.currentScreen instanceof ClickGuiScreen || mc.currentScreen instanceof HudEditorScreen) {
            ci.cancel();
        }
    }
}


