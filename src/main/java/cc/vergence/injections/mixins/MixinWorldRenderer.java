package cc.vergence.injections.mixins;

import cc.vergence.Vergence;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
    @Inject(at = { @At("RETURN") }, method = { "render" })
    private void onRenderWorld(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        MatrixStack matrices = new MatrixStack();
        matrices.peek().getPositionMatrix().set(positionMatrix);
        Vergence.EVENTS.onDraw3D(matrices, tickCounter.getTickDelta(false));
    }
}