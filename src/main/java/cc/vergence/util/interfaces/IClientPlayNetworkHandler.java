package cc.vergence.util.interfaces;

import net.minecraft.network.packet.Packet;

public interface IClientPlayNetworkHandler {
    void sendSilentPacket(final Packet<?> packet);
}
