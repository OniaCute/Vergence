package cc.vergence.injections.mixins.player;

import cc.vergence.Vergence;
import cc.vergence.features.event.Event;
import cc.vergence.features.event.events.*;
import cc.vergence.modules.movement.Velocity;
import cc.vergence.modules.player.PortalGod;
import cc.vergence.modules.visual.SwingModifier;
import cc.vergence.util.interfaces.Wrapper;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity implements Wrapper {
	@Shadow
	private void sendSprintingPacket() {
	}

	@Unique
	private boolean ticking;

	@Final
	@Shadow
	protected MinecraftClient client;

	@Shadow
	protected abstract void sendMovementPackets();

	@Shadow
	public abstract float getPitch(float tickDelta);

	public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tickHook(CallbackInfo info) {
		if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().world != null) {
			Vergence.EVENTBUS.post(new PlayerUpdateEvent());
		}
		if (Vergence.ROTATE.overrideServerRotation) {
			info.cancel();
		}
	}

	@Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"), cancellable = true)
	public void onMoveHook(MovementType movementType, Vec3d movement, CallbackInfo ci) {
		MoveEvent event = new MoveEvent(movement.x, movement.y, movement.z);
		Vergence.EVENTBUS.post(event);
		if (event.isCancelled()) {
			super.move(movementType, new Vec3d(event.getX(), event.getY(), event.getZ()));
			ci.cancel();
		}
	}

	@Inject(method = {"sendMovementPackets"}, at = {@At(value = "HEAD")})
	private void preMotion(CallbackInfo info) {
		if (mc.player == null) {
			return ;
		}
		Vergence.EVENTBUS.post(new SyncEvent(getYaw(), getPitch()));
		Vergence.EVENTBUS.post(new UpdateWalkingEvent(Event.Stage.Pre));
	}

	@Inject(method = {"sendMovementPackets"}, at = {@At(value = "RETURN")})
	private void postMotion(CallbackInfo info) {
		UpdateWalkingEvent event = new UpdateWalkingEvent(Event.Stage.Post);
		Vergence.EVENTBUS.post(event);
	}

	@Inject(method = "tickNausea", at = @At("HEAD"), cancellable = true)
	private void updateNauseaHook(CallbackInfo ci) {
		if (PortalGod.INSTANCE != null && PortalGod.INSTANCE.getStatus() && PortalGod.INSTANCE.inventory.getValue()) {
			ci.cancel();
		}
	}

	@Inject(method = "setCurrentHand", at = @At(value = "HEAD"))
	private void hookSetCurrentHand(Hand hand, CallbackInfo ci) {
		SetCurrentHandEvent setCurrentHandEvent = new SetCurrentHandEvent(hand);
		Vergence.EVENTBUS.post(setCurrentHandEvent);
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/" +
			"minecraft/client/network/ClientPlayerEntity;sendMovementPackets" +
			"()V", ordinal = 0, shift = At.Shift.AFTER))
	private void hookTick(CallbackInfo ci) {
		if (ticking) {
			return;
		}
		TickMovementEvent event = new TickMovementEvent();
		Vergence.EVENTBUS.post(event);
		if (event.isCancelled()) {
			for (int i = 0; i < event.getIterations(); i++) {
				ticking = true;
				tick();
				ticking = false;
				sendMovementPackets();
			}
		}
	}

	@Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
	private void pushOutOfBlocks(double x, double z, CallbackInfo info) {
		if (Velocity.INSTANCE.getStatus() && Velocity.INSTANCE.antiBlockPush.getValue()) {
			info.cancel();
		}
	}

	@Inject(method = "setCurrentHand", at = @At(value = "HEAD"))
	private void setCurrentHand(Hand hand, CallbackInfo info) {
		Vergence.EVENTBUS.post(new ChangeHandEvent(hand));
	}
}
