package cc.vergence.injections.mixins.render;

import cc.vergence.Vergence;
import cc.vergence.features.screens.ClickGuiScreen;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.modules.client.Client;
import cc.vergence.modules.hud.HotbarHud;
import cc.vergence.modules.hud.PotionHud;
import cc.vergence.modules.hud.Scoreboard;
import cc.vergence.modules.visual.NoRender;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.other.SkiaContext;
import cc.vergence.util.render.utils.NewRender2DUtil;
import cc.vergence.util.render.utils.blur.KawaseBlur;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud implements Wrapper {
    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    private void renderPortalOverlay(DrawContext context, float nauseaStrength, CallbackInfo info) {
        if (NoRender.INSTANCE.getStatus() && NoRender.INSTANCE.noPortalOverlay.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    private void renderVignetteOverlay(DrawContext context, Entity entity, CallbackInfo info) {
        if (NoRender.INSTANCE.getStatus() && NoRender.INSTANCE.noVignette.getValue()) {
            info.cancel();
        }
    }

    @WrapWithCondition(method = "renderMiscOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V", ordinal = 0))
    private boolean renderPumpkinOverlay(InGameHud instance, DrawContext context, Identifier texture, float opacity) {
        return !(NoRender.INSTANCE.getStatus() && NoRender.INSTANCE.noPumpkinOverlay.getValue());
    }

    @WrapWithCondition(method = "renderMiscOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;F)V", ordinal = 1))
    private boolean renderSnowOverlay(InGameHud instance, DrawContext context, Identifier texture, float opacity) {
        return !(NoRender.INSTANCE.getStatus() && NoRender.INSTANCE.noSnowOverlay.getValue());
    }

    @Inject(at = @At(value = "HEAD"), method = "renderHotbar", cancellable = true)
    public void renderHotbarCustom(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (HotbarHud.INSTANCE != null && HotbarHud.INSTANCE.getStatus()) {
            ci.cancel();
            HotbarHud.INSTANCE.renderHotBarItems(context);
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        float tickDelta = tickCounter.getTickDelta(true);
        KawaseBlur.INGAME_BLUR.draw(Client.INSTANCE.blurIntensity.getValue().intValue());
        SkiaContext.draw((contexts) -> {
            NewRender2DUtil.save();
            NewRender2DUtil.scale((float) mc.getWindow().getScaleFactor());
            Vergence.EVENTS.onDrawSkia(context, tickDelta);
            NewRender2DUtil.restore();
        });
        Vergence.EVENTS.onDraw2D(context, tickDelta);
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

    @Inject(at = @At(value = "HEAD"), method = "renderStatusEffectOverlay", cancellable = true)
    public void renderStatusEffectOverlayHook(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (NoRender.INSTANCE.getStatus() && NoRender.INSTANCE.noPotionHud.getValue() || PotionHud.INSTANCE.getStatus()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderExperienceBar", at = @At(value = "HEAD"), cancellable = true)
    public void renderXpBarCustom(DrawContext context, int x, CallbackInfo ci) {
        if (HotbarHud.INSTANCE != null && HotbarHud.INSTANCE.getStatus()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderExperienceLevel", at = @At(value = "HEAD"), cancellable = true)
    public void renderXpBarCustom(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (HotbarHud.INSTANCE != null && HotbarHud.INSTANCE.getStatus()) {
            ci.cancel();
            HotbarHud.INSTANCE.renderXpBar(context.getMatrices());
        }
    }
}