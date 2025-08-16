package cc.vergence.modules.movement;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.ChangeHandEvent;
import cc.vergence.features.event.events.MoveEvent;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.AntiCheat;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.util.Hand;

import java.util.EnumSet;

public class NoSlowdown extends Module {
    public static NoSlowdown INSTANCE;
    private boolean sneaking = false;

    public NoSlowdown() {
        super("NoSlowdown", Category.MOVEMENT);
        INSTANCE = this;
    }

    public Option<EnumSet<Items>> items = addOption(new MultipleOption<Items>("Items", EnumSet.of(Items.Items, Items.SoulSand, Items.SlimeBlock, Items.HoneyBlock)));
    public Option<Boolean> airBypass = addOption(new BooleanOption("AirBypass", false, v -> AntiCheat.INSTANCE.isNCP()));
    public Option<Boolean> groundBypass = addOption(new BooleanOption("GroundBypass", false, v -> AntiCheat.INSTANCE.isNCP() || AntiCheat.INSTANCE.isGrim()));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onTick() {
        if (isNull()) {
            return ;
        }
        if (airBypass.getValue() && sneaking && !mc.player.isUsingItem()) {
            mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
            sneaking = false;
        }
    }

    @Override
    public void onMoveEvent(MoveEvent event, double x, double y, double z) {
        if (isNull()) {
            return ;
        }
        if (!groundBypass.getValue() && AntiCheat.INSTANCE.isGrim()) {
            return ;
        }
        if (!mc.player.isUsingItem() || mc.player.isRiding() || mc.player.isGliding()) {
            return;
        }
        if (mc.player.getActiveHand() == Hand.OFF_HAND) {
            mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot % 8 + 1));
            mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot % 7 + 2));
            mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
        } else {
            Vergence.NETWORK.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.OFF_HAND, id, mc.player.getYaw(), mc.player.getPitch()));
        }
    }

    @Override
    public void onSendPacket(PacketEvent.Send event, Packet<?> packet) {
        if (isNull()) {
            return ;
        }
        if (groundBypass.getValue() && AntiCheat.INSTANCE.isNCP()) {
            if (event.getPacket() instanceof PlayerMoveC2SPacket.Full || event.getPacket() instanceof PlayerMoveC2SPacket.PositionAndOnGround || event.getPacket() instanceof PlayerMoveC2SPacket.LookAndOnGround || event.getPacket() instanceof PlayerMoveC2SPacket.OnGroundOnly) {
                Vergence.NETWORK.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
            }

            if (event.getPacket() instanceof ClickSlotC2SPacket) {
                if (mc.player.isUsingItem()) {
                    mc.player.stopUsingItem();
                }
                if (mc.player.isSprinting()) {
                    Vergence.NETWORK.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                }
                if (mc.player.isSneaking()) {
                    Vergence.NETWORK.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
                }
            }
        }
    }

    @Override
    public void onChangeHandEvent(ChangeHandEvent event, Hand hand) {
        if (airBypass.getValue() && !sneaking && (!mc.player.isRiding() && !mc.player.isSneaking() && (mc.player.isUsingItem() && items.getValue().contains(Items.Items) && (!groundBypass.getValue() && AntiCheat.INSTANCE.isGrim())))) {
            Vergence.NETWORK.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
            sneaking = true;
        }
    }

    @Override
    public void onDisable() {
        Vergence.TIMER.set(1f);
        if (isNull()) {
            return ;
        }
        if (airBypass.getValue() && sneaking) {
            mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
        }
        sneaking = false;
    }

    private enum Items {
        Items,
        SoulSand,
        SlimeBlock,
        HoneyBlock
    }
}
