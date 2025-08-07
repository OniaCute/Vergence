package cc.vergence.injections.mixins.render;

import cc.vergence.Vergence;
import cc.vergence.features.screens.ClickGuiScreen;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.modules.hud.Scoreboard;
import cc.vergence.modules.visual.NoRender;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.other.SkiaContext;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinMinecraftInGameHud implements Wrapper {
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        float tickDelta = tickCounter.getTickDelta(true);
        Vergence.EVENTS.onDraw2D(context, tickDelta);

        SkiaContext.draw((contexts) -> {
            Render2DUtil.save();
            Render2DUtil.scale((float) mc.getWindow().getScaleFactor());
            Render2DUtil.restore();
        });
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void onRenderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (mc.currentScreen instanceof ClickGuiScreen || mc.currentScreen instanceof HudEditorScreen) {
            ci.cancel();
        }
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", at = @At("HEAD"), cancellable = true)
    private void cancelSidebar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (Scoreboard.INSTANCE != null && Scoreboard.INSTANCE.getStatus()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("HEAD"), cancellable = true)
    private void cancelSidebar(DrawContext drawContext, ScoreboardObjective objective, CallbackInfo ci) {
        if (Scoreboard.INSTANCE != null && Scoreboard.INSTANCE.getStatus()) {
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "renderHeldItemTooltip", cancellable = true)
    public void renderHeldItemTooltipHook(DrawContext context, CallbackInfo ci) {
        if (NoRender.INSTANCE.getStatus() && NoRender.INSTANCE.noItemName.getValue()) {
            ci.cancel();
        }
    }
}


