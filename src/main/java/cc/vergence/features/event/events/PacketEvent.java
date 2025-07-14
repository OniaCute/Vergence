package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;
import cc.vergence.util.interfaces.Wrapper;
import com.llamalad7.mixinextras.sugar.Cancellable;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;

public class PacketEvent extends Event implements Wrapper {

    private final Packet<?> packet;
    public PacketEvent(Packet<?> packet) {
        super(Stage.Pre);
        this.packet = packet;
    }
    public <T extends Packet<?>> T getPacket() {
        return (T) packet;
    }
    public static class Send extends PacketEvent {
        public Send(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Receive extends PacketEvent {
        public Receive(Packet<?> packet) {
            super(packet);
        }
    }
}
