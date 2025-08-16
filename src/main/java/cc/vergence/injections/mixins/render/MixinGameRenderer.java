package cc.vergence.injections.mixins.render;

import cc.vergence.Vergence;
import cc.vergence.modules.client.Client;
import cc.vergence.modules.player.FreeCamera;
import cc.vergence.modules.player.LiquidInteract;
import cc.vergence.modules.player.NoEntityTrace;
import cc.vergence.modules.visual.AspectRatio;
import cc.vergence.modules.visual.FOVModifier;
import cc.vergence.modules.visual.NoRender;
import cc.vergence.modules.visual.TotemAnimation;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.render.utils.Render3DUtil;
import cc.vergence.util.render.utils.blur.KawaseBlur;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.profiler.Profilers;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer implements Wrapper {
    @Shadow
    private float zoom;

    @Shadow
    private float zoomX;

    @Shadow
    private float zoomY;

    @Shadow
    private float viewDistance;

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

    @Inject(method = "updateCrosshairTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;findCrosshairTarget(Lnet/minecraft/entity/Entity;DDF)Lnet/minecraft/util/hit/HitResult;"), cancellable = true)
    private void updateCrosshairTarget(float tickDelta, CallbackInfo info) {
        if (FreeCamera.INSTANCE != null && FreeCamera.INSTANCE.getStatus()) {
            Profilers.get().pop();
            mc.crosshairTarget = EntityUtil.getRaytraceTarget(FreeCamera.INSTANCE.getFreeYaw(), FreeCamera.INSTANCE.getFreePitch(), FreeCamera.INSTANCE.getFreeX(), FreeCamera.INSTANCE.getFreeY(), FreeCamera.INSTANCE.getFreeZ());
            info.cancel();
        }
    }

    @ModifyExpressionValue(method = "findCrosshairTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"))
    private @Nullable EntityHitResult findCrosshairTarget(@Nullable EntityHitResult original) {
        if (NoEntityTrace.INSTANCE != null && NoEntityTrace.INSTANCE.getStatus()) {
            return (NoEntityTrace.INSTANCE.onlyPickaxe.getValue() && mc.player.getMainHandStack().getItem() instanceof PickaxeItem) ? null : original;
        }
        return original;
    }

    @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true)
    private void tiltViewWhenHurt(CallbackInfo info) {
        if (NoRender.INSTANCE.getStatus() && NoRender.INSTANCE.noHurtCamera.getValue()) {
            info.cancel();
        }
    }

    @Inject(method = "getBasicProjectionMatrix", at = @At("TAIL"), cancellable = true)
    public void getBasicProjectionMatrixHook(float fovDegrees, CallbackInfoReturnable<Matrix4f> cir) {
        if (AspectRatio.INSTANCE.getStatus()) {
            MatrixStack matrixStack = new MatrixStack();
            matrixStack.peek().getPositionMatrix().identity();
            if (zoom != 1.0f) {
                matrixStack.translate(zoomX, -zoomY, 0.0f);
                matrixStack.scale(zoom, zoom, 1.0f);
            }
            matrixStack.peek().getPositionMatrix().mul(new Matrix4f().setPerspective((float) (fovDegrees * 0.01745329238474369), AspectRatio.INSTANCE.ratio.getValue().floatValue(), 0.05f, viewDistance * 4.0f));
            cir.setReturnValue(matrixStack.peek().getPositionMatrix());
        }
    }

    @Inject(method = "showFloatingItem", at = @At("HEAD"), cancellable = true)
    private void showFloatingItemHook(ItemStack floatingItem, CallbackInfo info) {
        if (TotemAnimation.INSTANCE.getStatus()) {
            TotemAnimation.INSTANCE.showFloatingItem(floatingItem);
            info.cancel();
        }
    }

    @Inject(method = "renderFloatingItem", at = @At("HEAD"), cancellable = true)
    private void renderFloatingItemHook(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (TotemAnimation.INSTANCE.getStatus()) {
            TotemAnimation.INSTANCE.renderFloatingItem(tickDelta);
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", shift = At.Shift.AFTER))
    public void renderGuiBlur(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        KawaseBlur.GUI_BLUR.draw(Client.INSTANCE.blurIntensity.getValue().intValue());
    }

    @Redirect(method = "findCrosshairTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;raycast(DFZ)Lnet/minecraft/util/hit/HitResult;"))
    private HitResult updateTargetedEntityEntityRayTraceProxy(Entity instance, double maxDistance, float tickDelta, boolean includeFluids) {
        if (LiquidInteract.INSTANCE != null && LiquidInteract.INSTANCE.getStatus()) {
            HitResult result = instance.raycast(maxDistance, tickDelta, includeFluids);
            if (result.getType() != HitResult.Type.MISS) {
                return result;
            }
            return instance.raycast(maxDistance, tickDelta, true);
        }
        return instance.raycast(maxDistance, tickDelta, includeFluids);
    }
}
