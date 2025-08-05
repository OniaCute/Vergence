package cc.vergence.injections.mixins.render;

import cc.vergence.modules.visual.NoRender;
import cc.vergence.modules.visual.WorldTweaks;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.Entity;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.awt.*;

@Mixin(BackgroundRenderer.class)
public class MixinBackgroundRenderer {
    @ModifyArgs(method = "applyFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Fog;<init>(FFLnet/minecraft/client/render/FogShape;FFFF)V"))
    private static void applyFog(Args args, Camera camera, BackgroundRenderer.FogType fogType, Vector4f originalColor, float viewDistance, boolean thickenFog, float tickDelta) {
        if (fogType == BackgroundRenderer.FogType.FOG_TERRAIN && NoRender.INSTANCE.getStatus() && NoRender.INSTANCE.noFog.getValue()) {
            args.set(0, viewDistance * 4);
            args.set(1, viewDistance * 4.25f);
        }
    }

    @Inject(method = "getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;", at = @At("HEAD"), cancellable = true)
    private static void getFogModifier(Entity entity, float tickDelta, CallbackInfoReturnable<BackgroundRenderer.StatusEffectFogModifier> info) {
        if (NoRender.INSTANCE.getStatus() && NoRender.INSTANCE.noBlindness.isVisible()) {
            info.setReturnValue(null);
        }
    }

    @Inject(method = "applyFog", at = @At("TAIL"))
    private static void onApplyFog(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfoReturnable<Fog> cir) {
        if(WorldTweaks.INSTANCE.getStatus() && WorldTweaks.INSTANCE.items.getValue().contains(WorldTweaks.Items.Fog)) {
            Fog fog = new Fog(
                    WorldTweaks.INSTANCE.fogAppear.getValue().floatValue(),
                    WorldTweaks.INSTANCE.fogDisappear.getValue().floatValue(),
                    FogShape.CYLINDER,
                    WorldTweaks.INSTANCE.fogColor.getValue().getRed() / 255f,
                    WorldTweaks.INSTANCE.fogColor.getValue().getGreen() / 255f,
                    WorldTweaks.INSTANCE.fogColor.getValue().getBlue() / 255f,
                    WorldTweaks.INSTANCE.fogColor.getValue().getAlpha() / 255f
            );
            RenderSystem.setShaderFog(fog);
        }
    }
}
