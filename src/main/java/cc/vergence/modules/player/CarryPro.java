package cc.vergence.modules.player;

import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.modules.Module;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

public class CarryPro extends Module {
    public static CarryPro INSTANCE;

    public CarryPro() {
        super("CarryPro", Category.PLAYER);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onSendPacket(PacketEvent.Send event, Packet<?> packet) {
        if (packet instanceof CloseHandledScreenC2SPacket) {
            event.cancel();
        }
    }
}
