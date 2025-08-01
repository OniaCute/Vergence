package cc.vergence.modules.movement;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.ClickSlotEvent;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.injections.accessors.client.CreativeInventoryScreenAccessor;
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
    public static InventoryMove INSTANCE;

    public InventoryMove() {
        super("InventoryMove", Category.MOVEMENT);
        INSTANCE = this;
    }

    
    public Option<Boolean> horizontalCollision = addOption(new BooleanOption("HorizontalCollision", false, v -> AntiCheat.INSTANCE.isNCP()));

    private final Queue<ClickSlotC2SPacket> clicks = new LinkedList<>();
    private AtomicBoolean pause = new AtomicBoolean();

    @Override
    public String getDetails() {
        return AntiCheat.INSTANCE.getAntiCheat();
    }

    public void onTick() {
        if (mc.player == null || !AntiCheat.INSTANCE.isLegit()) {
            return;
        }

        if (mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen || mc.currentScreen instanceof BookEditScreen || mc.currentScreen instanceof SignEditScreen || mc.currentScreen instanceof JigsawBlockScreen || mc.currentScreen instanceof StructureBlockScreen || mc.currentScreen instanceof AnvilScreen || (mc.currentScreen instanceof CreativeInventoryScreen && CreativeInventoryScreenAccessor.getSelectedTab().getType() == ItemGroup.Type.SEARCH))) {
            for (KeyBinding binding : new KeyBinding[]{mc.options.forwardKey, mc.options.backKey, mc.options.rightKey, mc.options.leftKey, mc.options.sprintKey, mc.options.sneakKey, mc.options.jumpKey}) {
                binding.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(binding.getBoundKeyTranslationKey()).getCode()));
            }
        }
    }

    public void onClickSlot(ClickSlotEvent event) {
        if (!AntiCheat.INSTANCE.isLegit()) {
            return;
        }

        if (AntiCheat.INSTANCE.isLegit() && (MovementUtil.isMoving() || mc.options.jumpKey.isPressed())) {
            event.cancel();
        }
    }

    @Override
    public void onSendPacket(PacketEvent.Send event, Packet<?> packet) {
        if (mc.player == null || !MovementUtil.isMoving() || !mc.options.jumpKey.isPressed() || pause.get()) {
            return;
        }

        if (packet instanceof ClickSlotC2SPacket click){
            if (AntiCheat.INSTANCE.isGrim()) {
                if (click.getActionType() != SlotActionType.PICKUP && click.getActionType() != SlotActionType.PICKUP_ALL) {
                    Vergence.NETWORK.sendPacket(new CloseHandledScreenC2SPacket(0));
                }
            }
            else if (AntiCheat.INSTANCE.isNCP()) {
                if (mc.player.isOnGround() && !mc.world.getBlockCollisions(mc.player, mc.player.getBoundingBox().offset(0.0, 0.0656, 0.0)).iterator().hasNext()) {
                    if (mc.player.isSprinting())
                        Vergence.NETWORK.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                    Vergence.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.0656, mc.player.getZ(), false, horizontalCollision.getValue()));
                }
            }
            else if (AntiCheat.INSTANCE.isMatrix()) {
                Vergence.NETWORK.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                mc.options.forwardKey.setPressed(false);
                mc.player.input.movementForward = 0;
            }
            else if (AntiCheat.INSTANCE.isNone()) {
                clicks.add(click);
                event.cancel();
            }
        }

        else if (packet instanceof CloseHandledScreenC2SPacket) {
            if (AntiCheat.INSTANCE.isNone()) {
                pause.set(true);
                while (!clicks.isEmpty()) {
                    Vergence.NETWORK.sendPacket(clicks.poll());
                }
                pause.set(false);
            }
        }
    }
}
