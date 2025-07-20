package cc.vergence.injections.mixins.player;

import cc.vergence.modules.player.FreeCamera;
import cc.vergence.modules.visual.CameraClip;
import cc.vergence.modules.visual.NoRender;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public class MixinCamera {
    @Shadow private boolean thirdPerson;

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;clipToSpace(F)F"))
    private void update(Args args) {
        if (CameraClip.INSTANCE != null && CameraClip.INSTANCE.getStatus() && CameraClip.INSTANCE.distance.getValue().intValue() == 0) {
            args.set(0, CameraClip.INSTANCE.distance.getValue());
        }
    }

    @Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
    private void clipToSpace(float f, CallbackInfoReturnable<Float> info) {
        if (CameraClip.INSTANCE != null && CameraClip.INSTANCE.getStatus()) {
            info.setReturnValue(f);
        }
    }

    @Inject(method = "getSubmersionType", at = @At("HEAD"), cancellable = true)
    private void getSubmersionType(CallbackInfoReturnable<CameraSubmersionType> info) {
        if (NoRender.INSTANCE != null && NoRender.INSTANCE.getStatus() && NoRender.INSTANCE.noBlindness.getValue()) {
            info.setReturnValue(CameraSubmersionType.NONE);
        }
    }

    @Inject(method = "update", at = @At("TAIL"))
    private void update$TAIL(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo info) {
        if (FreeCamera.INSTANCE != null && FreeCamera.INSTANCE.getStatus()) {
            this.thirdPerson = true;
        }
    }

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    private void update$setRotation(Args args) {
        if (FreeCamera.INSTANCE != null && FreeCamera.INSTANCE.getStatus()) {
            args.setAll(FreeCamera.INSTANCE.getFreeYaw(), FreeCamera.INSTANCE.getFreePitch());
        }
    }

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    private void update$setPos(Args args) {
        if (FreeCamera.INSTANCE != null && FreeCamera.INSTANCE.getStatus()) {
            args.setAll(FreeCamera.INSTANCE.getFreeX(), FreeCamera.INSTANCE.getFreeY(), FreeCamera.INSTANCE.getFreeZ());
        }
    }
}
