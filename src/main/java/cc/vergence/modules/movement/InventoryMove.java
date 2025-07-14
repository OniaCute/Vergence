package cc.vergence.modules.movement;

import cc.vergence.features.enums.AntiCheats;
import cc.vergence.features.event.events.ClickSlotEvent;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.injections.accessors.CreativeInventoryScreenAccessor;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.player.MovementUtil;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class InventoryMove extends Module {
    public InventoryMove() {
        super("InventoryMove", Category.MOVEMENT);
    }

    public Option<Boolean> horizontalCollision = addOption(new BooleanOption("HorizontalCollision", false, v -> AntiCheat.INSTANCE.antiCheat.getValue().equals(AntiCheats.NCP)));

    private final Queue<ClickSlotC2SPacket> clicks = new LinkedList<>();
    private AtomicBoolean pause = new AtomicBoolean();

    @Override
    public String getDetails() {
        return AntiCheat.INSTANCE != null ? AntiCheat.INSTANCE.antiCheat.getValue().name() : "Unknown";
    }

    public void onTick() {
        if (mc.player == null || AntiCheat.INSTANCE == null || !AntiCheat.INSTANCE.getStatus()) {
            return;
        }

        if (mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen || mc.currentScreen instanceof BookEditScreen || mc.currentScreen instanceof SignEditScreen || mc.currentScreen instanceof JigsawBlockScreen || mc.currentScreen instanceof StructureBlockScreen || mc.currentScreen instanceof AnvilScreen || (mc.currentScreen instanceof CreativeInventoryScreen && CreativeInventoryScreenAccessor.getSelectedTab().getType() == ItemGroup.Type.SEARCH))) {
            for (KeyBinding binding : new KeyBinding[]{mc.options.forwardKey, mc.options.backKey, mc.options.rightKey, mc.options.leftKey, mc.options.sprintKey, mc.options.sneakKey, mc.options.jumpKey}) {
                binding.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(binding.getBoundKeyTranslationKey()).getCode()));
            }
        }
    }

    public void onClickSlot(ClickSlotEvent event) {
        if (!AntiCheat.INSTANCE.getStatus()) {
            return;
        }

        if (AntiCheat.INSTANCE.antiCheat.getValue().equals(AntiCheats.Legit) && (MovementUtil.isMoving() || mc.options.jumpKey.isPressed())) {
            event.cancel();
        }
    }

    @Override
    public void onSendPacket(PacketEvent.Send event, Packet<?> packet) {
        if (mc.player == null || AntiCheat.INSTANCE == null || !MovementUtil.isMoving() || !mc.options.jumpKey.isPressed() || pause.get() || !AntiCheat.INSTANCE.getStatus()) {
            return;
        }

        if (packet instanceof ClickSlotC2SPacket click){
            if (AntiCheat.INSTANCE.antiCheat.getValue().equals(AntiCheats.Grim)) {
                if (click.getActionType() != SlotActionType.PICKUP && click.getActionType() != SlotActionType.PICKUP_ALL) {
                    mc.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(0));
                }
            }
            else if (AntiCheat.INSTANCE.antiCheat.getValue().equals(AntiCheats.NCP)) {
                if (mc.player.isOnGround() && !mc.world.getBlockCollisions(mc.player, mc.player.getBoundingBox().offset(0.0, 0.0656, 0.0)).iterator().hasNext()) {
                    if (mc.player.isSprinting())
                        mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                    mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.0656, mc.player.getZ(), false, horizontalCollision.getValue()));
                }
            }
            else if (AntiCheat.INSTANCE.antiCheat.getValue().equals(AntiCheats.Matrix)) {
                mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                mc.options.forwardKey.setPressed(false);
                mc.player.input.movementForward = 0;
            }
            else if (AntiCheat.INSTANCE.antiCheat.getValue().equals(AntiCheats.None)) {
                clicks.add(click);
                event.cancel();
            }
        }

        else if (packet instanceof CloseHandledScreenC2SPacket) {
            if (AntiCheat.INSTANCE.antiCheat.getValue().equals(AntiCheats.None)) {
                pause.set(true);
                while (!clicks.isEmpty()) {
                    mc.getNetworkHandler().sendPacket(clicks.poll());
                }
                pause.set(false);
            }
        }
    }
}
