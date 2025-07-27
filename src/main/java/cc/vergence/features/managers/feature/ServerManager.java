package cc.vergence.features.managers.feature;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.PlayerDisconnectEvent;
import cc.vergence.features.event.eventbus.EventHandler;
import cc.vergence.features.event.events.ClientConnectEvent;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.event.events.PlayerConnectEvent;
import cc.vergence.features.event.events.ServerConnectEvent;
import cc.vergence.modules.exploit.FastLatencyCalc;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.maths.MathUtil;
import cc.vergence.util.other.FastTimerUtil;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

import java.util.Arrays;
import java.util.UUID;

public class ServerManager implements Wrapper {
    private final FastTimerUtil setbackTimer = new FastTimerUtil();
    private final FastTimerUtil responseTimer = new FastTimerUtil();

    private final float[] tickRates = new float[20];
    private int nextIndex = 0;
    private long lastUpdate = -1;
    private long timeJoined;

    private Pair<ServerAddress, ServerInfo> lastConnection;

    public ServerManager() {
        Vergence.EVENTBUS.subscribe(this);
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event) {
        responseTimer.reset();

        if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
            setbackTimer.reset();
        }

        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            tickRates[nextIndex] = MathUtil.clamp(20.0f / ((System.currentTimeMillis() - lastUpdate) / 1000.0F), 0.0f, 20.0f);
            nextIndex = (nextIndex + 1) % tickRates.length;
            lastUpdate = System.currentTimeMillis();
        }
    }

    @EventHandler
    public void onClientConnect(ClientConnectEvent event) {
        Arrays.fill(tickRates, 0);
        nextIndex = 0;
        timeJoined = System.currentTimeMillis();
        lastUpdate = System.currentTimeMillis();
    }

    @EventHandler
    public void handleConnections(PacketEvent.Receive event) {
        if (mc.world == null) return;

        if (event.getPacket() instanceof PlayerListS2CPacket packet) {
            if (packet.getActions().contains(PlayerListS2CPacket.Action.ADD_PLAYER)) {
                for(PlayerListS2CPacket.Entry entry : packet.getPlayerAdditionEntries()) {
                    Vergence.EVENTBUS.post(new PlayerConnectEvent(entry.profile().getId()));
                }
            }
        } else if(event.getPacket() instanceof PlayerRemoveS2CPacket packet) {
            for(UUID id : packet.profileIds()) {
                Vergence.EVENTBUS.post(new PlayerDisconnectEvent(id));
            }
        }
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        lastConnection = new ObjectObjectImmutablePair<>(event.getAddress(), event.getInfo());
    }

    public float getTickRate() {
        if (mc.player == null) return 0;
        if (System.currentTimeMillis() - timeJoined < 4000) return 20;

        int ticks = 0;
        float tickRates = 0.0f;

        for (float tickRate : this.tickRates) {
            if (tickRate > 0) {
                tickRates += tickRate;
                ticks++;
            }
        }

        return tickRates / ticks;
    }

    public int getPingDelay() {
        return (int) (getPing() / 25.0f);
    }

    public int getPing() {
        if (FastLatencyCalc.INSTANCE != null && FastLatencyCalc.INSTANCE.getStatus()) {
            return FastLatencyCalc.INSTANCE.getLatency();
        }

        PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
        return entry == null ? 0 : entry.getLatency();
    }

    public String getServerBrand() {
        if (mc.getCurrentServerEntry() == null || mc.getNetworkHandler() == null || mc.getNetworkHandler().getBrand() == null) return "Vanilla";
        return mc.getNetworkHandler().getBrand();
    }

    public String getServer() {
        return mc.isInSingleplayer() ? "SinglePlayer" : ServerAddress.parse(mc.getCurrentServerEntry().address).getAddress();
    }

    public FastTimerUtil getResponseTimer() {
        return responseTimer;
    }

    public FastTimerUtil getSetbackTimer() {
        return setbackTimer;
    }
}
