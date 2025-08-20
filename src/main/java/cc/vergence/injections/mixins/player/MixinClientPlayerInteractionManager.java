package cc.vergence.injections.mixins.player;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.ClickBlockEvent;
import cc.vergence.features.event.events.ClickSlotEvent;
import cc.vergence.modules.Module;
import cc.vergence.modules.player.NoCooldown;
import cc.vergence.modules.player.SaveBreak;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {
	@ModifyConstant(method = "updateBlockBreakingProgress", constant = @Constant(intValue = 5))
	private int MiningCooldownFix(int value) {
		return NoCooldown.INSTANCE.getStatus() && NoCooldown.INSTANCE.forBreak.getValue() ? 0 : value;
	}

	@Inject(method = "cancelBlockBreaking", at = @At("HEAD"), cancellable = true)
	private void hookCancelBlockBreaking(CallbackInfo ci) {
		if (SaveBreak.INSTANCE.getStatus()) {
			ci.cancel();
		}
	}

	@Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
	private void onAttackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
		ClickBlockEvent event = new ClickBlockEvent(pos);
		Vergence.EVENTBUS.post(event);
		if (event.isCancelled()) {
			cir.setReturnValue(false);
		} else {
			Vergence.MINE.setMining(true);
		}
	}

	@Inject(method = "cancelBlockBreaking", at = @At("HEAD"))
	private void onBlockBreakingCancelled(CallbackInfo ci) {
		Vergence.MINE.setMining(false);
	}

	@Inject(method = "isBreakingBlock", at = @At("HEAD"))
	private void miningHook(CallbackInfoReturnable<Boolean> cir) {
		if (cir.isCancelled() || cir.getReturnValue() == null) {
			return ;
		}
		Vergence.MINE.setMining(cir.getReturnValue());
	}

	@Inject(method = "clickSlot", at = @At("HEAD"), cancellable = true)
	public void clickSlotHook(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
		if (Module.isNull()) {
			return ;
		}

		ClickSlotEvent event = new ClickSlotEvent(actionType, slotId, button, syncId);
		Vergence.EVENTBUS.post(event);
		if (event.isCancelled()) {
			ci.cancel();
		}
	}
}
