package cc.vergence.injections.mixins.player;

import cc.vergence.Vergence;
import cc.vergence.features.event.Event;
import cc.vergence.features.event.events.MoveEvent;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.event.events.UpdateWalkingEvent;
import cc.vergence.modules.player.PortalGod;
import cc.vergence.modules.visual.SwingModifier;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {
	@Shadow
	private void sendSprintingPacket() {
	}

	@Final
	@Shadow
	protected MinecraftClient client;

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
	}

	@Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"), cancellable = true)
	public void onMoveHook(MovementType movementType, Vec3d movement, CallbackInfo ci) {
		MoveEvent event = new MoveEvent(movement.x, movement.y, movement.z);
		Vergence.EVENTBUS.post(event);
		ci.cancel();
		if (!event.isCancel()) {
			super.move(movementType, new Vec3d(event.getX(), event.getY(), event.getZ()));
		}
	}

	@Inject(method = {"sendMovementPackets"}, at = {@At(value = "HEAD")})
	private void preMotion(CallbackInfo info) {
		UpdateWalkingEvent event = new UpdateWalkingEvent(Event.Stage.Pre);
		Vergence.EVENTBUS.post(event);
	}

	@Inject(method = {"sendMovementPackets"}, at = {@At(value = "RETURN")})
	private void postMotion(CallbackInfo info) {
		UpdateWalkingEvent event = new UpdateWalkingEvent(Event.Stage.Post);
		Vergence.EVENTBUS.post(event);
	}

	@Inject(method = "tickNausea", at = @At("HEAD"), cancellable = true)
	private void updateNauseaHook(CallbackInfo ci) {
		if (PortalGod.INSTANCE != null && PortalGod.INSTANCE.getStatus())
			ci.cancel();
	}
}
