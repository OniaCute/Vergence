package cc.vergence.injections.mixins.player;

import cc.vergence.modules.combat.Reach;
import cc.vergence.modules.movement.AutoSprint;
import cc.vergence.modules.movement.SafeWalk;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements Wrapper {
    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "clipAtLedge", at = @At("HEAD"), cancellable = true)
    private void clipAtLedge(CallbackInfoReturnable<Boolean> info) {
        if (SafeWalk.INSTANCE != null && SafeWalk.INSTANCE.getStatus() && SafeWalk.INSTANCE.doInject.getValue()) {
            info.setReturnValue(true);
        }
    }

    @Inject(method = "getBlockInteractionRange", at = @At("HEAD"), cancellable = true)
    private void getBlockInteractionRange(CallbackInfoReturnable<Double> info) {
        if (Reach.INSTANCE != null && Reach.INSTANCE.getStatus()) {
            info.setReturnValue(Reach.INSTANCE.range.getValue());
        }
    }

    @Inject(method = "getEntityInteractionRange", at = @At("HEAD"), cancellable = true)
    private void getEntityInteractionRange(CallbackInfoReturnable<Double> info) {
        if (Reach.INSTANCE != null && Reach.INSTANCE.getStatus()) {
            info.setReturnValue(Reach.INSTANCE.range.getValue());
        }
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V", shift = At.Shift.AFTER))
    private void attack(CallbackInfo callbackInfo) {
        if (AutoSprint.INSTANCE != null && AutoSprint.INSTANCE.getStatus() && AutoSprint.INSTANCE.forAttack.getValue()) {
            float multiplier = (float) (0.6f + 0.4f * AutoSprint.INSTANCE.attackCounteract.getValue());
            mc.player.setVelocity(mc.player.getVelocity().x / 0.6 * multiplier, mc.player.getVelocity().y, mc.player.getVelocity().z / 0.6 * multiplier);
            mc.player.setSprinting(true);
        }
    }
}
