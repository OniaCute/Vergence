package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public class ServerConnectEvent extends Event {
    private final ServerAddress address;
    private final ServerInfo info;

    public ServerConnectEvent(ServerAddress address, ServerInfo info) {
        this.address = address;
        this.info = info;
    }

    public ServerAddress getAddress() {
        return address;
    }

    public ServerInfo getInfo() {
        return info;
    }
}
