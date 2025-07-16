package cc.vergence.features.managers;

import cc.vergence.Vergence;
import cc.vergence.features.event.eventbus.EventHandler;
import cc.vergence.features.event.events.DisconnectEvent;
import cc.vergence.injections.accessors.ClientWorldAccessor;
import cc.vergence.util.interfaces.IClientPlayNetworkHandler;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.client.network.*;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class NetworkManager implements Wrapper {
    private static final Set<Packet<?>> PACKETS = new HashSet<>();
    private ServerAddress address;
    private ServerInfo info;

    public NetworkManager() {
        Vergence.EVENTBUS.subscribe(this);
    }

    @EventHandler
    public void onDisconnect(DisconnectEvent event) {
        PACKETS.clear();
    }

    public void sendPacket(final Packet<?> p) {
        if (mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().sendPacket(p);
        }
    }

    public void sendPacketWithChach(final Packet<?> p) {
        if (mc.getNetworkHandler() != null) {
            PACKETS.add(p);
            mc.getNetworkHandler().sendPacket(p);
        }
    }

    public void sendSilentPacket(final Packet<?> p) {
        if (mc.getNetworkHandler() != null) {
            ((IClientPlayNetworkHandler) mc.getNetworkHandler()).sendSilentPacket(p);
        }
    }

    public void sendSilentPacketWithChach(final Packet<?> p) {
        if (mc.getNetworkHandler() != null) {
            PACKETS.add(p);
            ((IClientPlayNetworkHandler) mc.getNetworkHandler()).sendSilentPacket(p);
        }
    }

    public void sendIgnoredPacket(final Packet<?> packet) {
        Objects.requireNonNull(mc.getNetworkHandler()).getConnection().send(packet, null, true);
    }

    public void sendIgnoredPacketWithChach(final Packet<?> packet) {
        PACKETS.add(packet);
        Objects.requireNonNull(mc.getNetworkHandler()).getConnection().send(packet, null, true);
    }

    public void sendSequencedPacket(final SequencedPacketCreator p) {
        if (mc.world != null) {
            PendingUpdateManager updater = ((ClientWorldAccessor) mc.world).invokeGetPendingUpdateManager().incrementSequence();
            try {
                int i = updater.getSequence();
                Packet<ServerPlayPacketListener> packet = p.predict(i);
                sendPacket(packet);
            } catch (Throwable e) {
                e.printStackTrace();
                if (updater != null) {
                    try {
                        updater.close();
                    } catch (Throwable e1) {
                        e1.printStackTrace();
                        e.addSuppressed(e1);
                    }
                }
                throw e;
            }
            if (updater != null) {
                updater.close();
            }
        }
    }

    public int getClientLatency() {
        if (mc.getNetworkHandler() != null) {
            final PlayerListEntry playerEntry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getGameProfile().getId());
            if (playerEntry != null) {
                return playerEntry.getLatency();
            }
        }
        return 0;
    }

    public ServerAddress getAddress() {
        return address;
    }

    public void setAddress(ServerAddress address) {
        this.address = address;
    }

    public ServerInfo getInfo() {
        return info;
    }

    public void setInfo(ServerInfo info) {
        this.info = info;
    }

    public boolean isCached(Packet<?> p) {
        return PACKETS.contains(p);
    }
}
