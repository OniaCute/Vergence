package cc.vergence.injections.mixins.render;

import cc.vergence.Vergence;
import cc.vergence.modules.visual.FOVModifier;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.utils.Render3DUtil;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer implements Wrapper {
    @Inject(method = "getFov", at = @At("TAIL"), cancellable = true)
    private void getFOV(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Float> info) {
        if(FOVModifier.INSTANCE != null && FOVModifier.INSTANCE.getStatus()) {
            if(info.getReturnValue() == 70 && !FOVModifier.INSTANCE.forItem.getValue()) return;
            info.setReturnValue(FOVModifier.INSTANCE.fov.getValue().floatValue());
        }
    }

    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void renderWorld$HEAD(RenderTickCounter tickCounter, CallbackInfo info) {
        Render3DUtil.reload();
    }

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/ObjectAllocator;Lnet/minecraft/client/render/RenderTickCounter;ZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", shift = At.Shift.AFTER))
    private void hookRenderWorld(RenderTickCounter tickCounter, CallbackInfo info, @Local(ordinal = 2) Matrix4f matrix4f3, @Local(ordinal = 1) float tickDelta, @Local MatrixStack matrixStack) {
        RenderSystem.getModelViewStack().pushMatrix();
        RenderSystem.getModelViewStack().mul(matrix4f3);
        RenderSystem.getModelViewStack().mul(matrixStack.peek().getPositionMatrix().invert());
        Vergence.EVENTS.onDraw3D(matrixStack, tickDelta);
        Render3DUtil.draw(Render3DUtil.QUADS, Render3DUtil.DEBUG_LINES, false);
        Render3DUtil.draw(Render3DUtil.SHINE_QUADS, Render3DUtil.SHINE_DEBUG_LINES, true);
        Vergence.EVENTS.onDraw3D(matrixStack, tickDelta);
        RenderSystem.getModelViewStack().popMatrix();
    }
}
