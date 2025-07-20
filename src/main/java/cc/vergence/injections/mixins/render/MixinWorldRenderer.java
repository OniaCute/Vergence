package cc.vergence.injections.mixins.render;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.RenderEntityEvent;
import cc.vergence.modules.player.FreeCamera;
import cc.vergence.modules.visual.NoBacktrack;
import cc.vergence.modules.visual.NoRender;
import cc.vergence.util.interfaces.Wrapper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer implements Wrapper {
    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;

    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    private void renderEntityHead(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo info) {
        if (entity != MinecraftClient.getInstance().player && entity instanceof PlayerEntity && NoBacktrack.INSTANCE != null && NoBacktrack.INSTANCE.getStatus()) {
            RenderEntityEvent event = new RenderEntityEvent(entity, vertexConsumers);
            Vergence.EVENTBUS.post(event);

            entityRenderDispatcher.render(entity, entity.getX() - cameraX, entity.getY() - cameraY, entity.getZ() - cameraZ, tickDelta, matrices, event.getVertexConsumers(), this.entityRenderDispatcher.getLight(entity, tickDelta));
            info.cancel();
        }
    }

    @WrapOperation(method = "renderEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private <E extends Entity> void renderEntityRender(EntityRenderDispatcher instance, E entity, double x, double y, double z, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Operation<Void> original) {
        RenderEntityEvent event = new RenderEntityEvent(entity, vertexConsumers);
        Vergence.EVENTBUS.post(event);

        original.call(instance, entity, x, y, z, tickDelta, matrices, event.getVertexConsumers(), light);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;setupTerrain(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/Frustum;ZZ)V"), index = 3)
    private boolean render$setupTerrain(boolean spectator) {
        return (FreeCamera.INSTANCE != null && FreeCamera.INSTANCE.getStatus()) || spectator;
    }

    @Inject(method = "hasBlindnessOrDarkness(Lnet/minecraft/client/render/Camera;)Z", at = @At("HEAD"), cancellable = true)
    private void hasBlindnessOrDarkness(Camera camera, CallbackInfoReturnable<Boolean> info) {
        if (NoRender.INSTANCE != null && NoRender.INSTANCE.getStatus() && NoRender.INSTANCE.noBlindness.getValue()) {
            info.setReturnValue(false);
        }
    }

//    @Inject(method = "render", at = @At(value = "HEAD"))
//    private void render(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo info, @Local(argsOnly = true) LocalBooleanRef blockOutline) {
//        if () {
//            blockOutline.set(true); // waiting for Highlight Block Outline module
//        }
//    }
}