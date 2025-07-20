package cc.vergence.features.managers.player;

import cc.vergence.Vergence;
import cc.vergence.features.event.eventbus.EventHandler;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

public class PositionManager implements Wrapper {
    public PositionManager() {
        Vergence.EVENTBUS.subscribe(this);
    }

    private double serverX;
    private double serverY;
    private double serverZ;

    private boolean serverOnGround;

    private boolean serverSprinting;
    private boolean serverSneaking;

    private int serverSlot;

    @EventHandler
    public void onPacketSend(PacketEvent.Send event) {
        if (mc.player == null) {
            return;
        }
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet) {
            if (packet.changesPosition()) {
                serverX = packet.getX(mc.player.getX());
                serverY = packet.getY(mc.player.getY());
                serverZ = packet.getZ(mc.player.getZ());
            }

            serverOnGround = packet.isOnGround();
        }
        if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket packet) {
            serverSlot = packet.getSelectedSlot();
        }
        if (event.getPacket() instanceof ClientCommandC2SPacket packet) {
            switch (packet.getMode()) {
                case START_SPRINTING -> serverSprinting = true;
                case STOP_SPRINTING -> serverSprinting = false;
                case PRESS_SHIFT_KEY -> serverSneaking = true;
                case RELEASE_SHIFT_KEY -> serverSneaking = false;
            }
        }
    }
}
