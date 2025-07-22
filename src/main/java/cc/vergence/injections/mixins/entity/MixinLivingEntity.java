package cc.vergence.injections.mixins.entity;

import cc.vergence.modules.combat.NoCooldown;
import cc.vergence.modules.movement.AntiLevitation;
import cc.vergence.modules.visual.SwingModifier;
import cc.vergence.util.interfaces.ILivingEntity;
import cc.vergence.util.interfaces.Wrapper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements ILivingEntity, Wrapper {

    @Shadow private int jumpingCooldown;

    @Unique private boolean staticPlayerEntity = false;

    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean vergence$getStaticPlayer() {
        return staticPlayerEntity;
    }

    @Override
    public void vergence$setStaticPlayer(boolean staticPlayerEntity) {
        this.staticPlayerEntity = staticPlayerEntity;
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", ordinal = 2, shift = At.Shift.BEFORE))
    private void doItemUse(CallbackInfo info) {
        if (NoCooldown.INSTANCE != null && NoCooldown.INSTANCE.getStatus() && NoCooldown.INSTANCE.forJump.getValue() && jumpingCooldown == 10) {
            jumpingCooldown = NoCooldown.INSTANCE.jumpTicks.getValue().intValue();
        }
    }

    @ModifyConstant(method = "getHandSwingDuration", constant = @Constant(intValue = 6))
    private int getHandSwingDuration(int constant) {
        if ((Object) this != mc.player) {
            return constant;
        }
        return (SwingModifier.INSTANCE != null && SwingModifier.INSTANCE.getStatus() && mc.options.getPerspective().isFirstPerson() ? (int) (21 - SwingModifier.INSTANCE.speed.getValue()) : constant);
    }

    @ModifyExpressionValue(method = "travelMidAir", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/entity/effect/StatusEffectInstance;"))
    private @Nullable StatusEffectInstance travelMidAir$getStatusEffect(@Nullable StatusEffectInstance original) {
        if (AntiLevitation.INSTANCE.getStatus()) {
            return null;
        }
        return original;
    }

    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z", ordinal = 1))
    private boolean tickMovement$hasStatusEffect(boolean original) {
        if (AntiLevitation.INSTANCE.getStatus()) {
            return false;
        }
        return original;
    }
}
