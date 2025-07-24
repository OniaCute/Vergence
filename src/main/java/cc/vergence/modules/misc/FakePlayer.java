package cc.vergence.modules.misc;

import cc.vergence.Vergence;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.modules.Module;
import cc.vergence.util.other.FastTimerUtil;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

import java.util.Date;
import java.util.UUID;

public class FakePlayer extends Module {
	public static FakePlayer INSTANCE;

	public FakePlayer() {
		super("FakePlayer", Category.MISC);
		INSTANCE = this;
	}

	public Option<String> playerName = addOption(new TextOption("PlayerName", "\\/ar6auca"));
	public Option<Boolean> goldenApple = addOption(new BooleanOption("GoldenApple"));
	public Option<Boolean> autoTotem = addOption(new BooleanOption("AutoTotem"));

	public static OtherClientPlayerEntity fakePlayer;

	@Override
	public String getDetails() {
		return playerName.getValue();
	}

	private final FastTimerUtil timer = new FastTimerUtil();
	@Override
	public void onEnable() {
		pops = 0;
		if (isNull()) {
			disable();
			return;
		}
		if (mc.player != null) {
			fakePlayer = new OtherClientPlayerEntity(mc.world, new GameProfile(UUID.randomUUID(), playerName.getValue()));
			fakePlayer.copyPositionAndRotation(mc.player);
            fakePlayer.getInventory().clone(mc.player.getInventory());
        }
        mc.world.addEntity(fakePlayer);
		fakePlayer.setYaw(mc.player.getYaw());
		fakePlayer.setPitch(mc.player.getPitch());
		fakePlayer.setBodyYaw(mc.player.getBodyYaw());
		fakePlayer.setHeadYaw(mc.player.getHeadYaw());
		fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 9999, 2));
		fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 9999, 4));
		fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 9999, 1));
	}

	int pops = 0;

	@Override
	public void onTick() {
		if (fakePlayer != null) {
			fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 9999, 2));
			fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 9999, 4));
			if (goldenApple.getValue()) {
				if (timer.passedMs(4000)) {
					fakePlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 9999, 1));
					timer.reset();
					fakePlayer.setAbsorptionAmount(16);
				}
			}
			if (autoTotem.getValue() && fakePlayer.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
				Vergence.TOTEM.onTotemPop(fakePlayer);
				fakePlayer.setStackInHand(Hand.OFF_HAND, new ItemStack(Items.TOTEM_OF_UNDYING));
			}
			if (fakePlayer.isDead()) {
				if (fakePlayer.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
					fakeTotemPop(fakePlayer);
				}
			}
		} else {
			disable();
		}
	}

	@Override
	public void onDisable() {
		if (fakePlayer == null) {
			return;
		}
        fakePlayer.setRemoved(Entity.RemovalReason.DISCARDED);
		fakePlayer.onRemoved();
		fakePlayer = null;
	}

	private void fakeTotemPop(OtherClientPlayerEntity fake) {
		fake.getWorld().sendEntityStatus(fake, EntityStatuses.USE_TOTEM_OF_UNDYING);
		fake.setHealth(20.0F);
		fake.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
		fake.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 800, 1));
		fake.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
		fake.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
	}
}