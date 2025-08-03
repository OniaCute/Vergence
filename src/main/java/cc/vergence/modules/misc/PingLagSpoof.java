package cc.vergence.modules.misc;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.s2c.common.KeepAliveS2CPacket;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PingLagSpoof extends Module {
    public static PingLagSpoof INSTANCE;
    private final ConcurrentLinkedQueue<DelayedPacket> queue = new ConcurrentLinkedQueue<>();

    public PingLagSpoof() {
        super("PingLagSpoof", Category.MISC);
        INSTANCE = this;
    }

    public Option<Double> delay = addOption(new DoubleOption("Delay", 3, 2000, 120).setUnit("ms"));

    @Override
    public String getDetails() {
        return delay.getValue() + "ms";
    }

    @Override
    public void onReceivePacket(PacketEvent.Receive event, Packet<?> packet) {
        if (isNull()) {
            return ;
        }

        if (packet instanceof KeepAliveS2CPacket) {
            event.cancel();
            queue.add(new DelayedPacket((KeepAliveS2CPacket) packet, System.currentTimeMillis()));
        }
    }

    @Override
    public void onDisable() {
        queue.clear();
    }

    @Override
    public void onLogout() {
        queue.clear();
    }

    @Override
    public void onTick() {
        if (isNull()) {
            return;
        }

        DelayedPacket packet = queue.peek();
        if (packet == null) {
            return;
        }
        if (System.currentTimeMillis() - packet.time() >= delay.getValue().intValue()) {
            Vergence.NETWORK.sendPacket(new KeepAliveC2SPacket(Objects.requireNonNull(queue.poll()).packet().getId()));
        }
    }

    private record DelayedPacket(KeepAliveS2CPacket packet, long time) {}
}