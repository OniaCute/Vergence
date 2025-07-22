package cc.vergence.modules.client;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.DisconnectEvent;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.other.FastTimerUtil;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class PacketsLimit extends Module {
    public static PacketsLimit INSTANCE;
    private final FastTimerUtil timer = new FastTimerUtil();
    private int fullPacket;
    private int positionPacket;
    public boolean full;

    public PacketsLimit() {
        super("PacketsLimit", Category.CLIENT);
        INSTANCE = this;
    }

    public Option<Boolean> doStop = addOption(new BooleanOption("Stop", true));
    public Option<Double> resetTime = addOption(new DoubleOption("ResetTime", 10, 1000, 40).setUnit("ms"));
    public Option<Double> limit = addOption(new DoubleOption("Limit", 1, 200, 15));
    public Option<Boolean> notify = addOption(new BooleanOption("Notify", false));

    @Override
    public String getDetails() {
        return positionPacket + "P | " + fullPacket + "F" + " | " + (full ? "Limiting" : "Normal");
    }

    @Override
    public void onTick() {
        if (timer.passedMs(resetTime.getValue())) {
            reset();
        }
    }

    @Override
    public void onEnable() {
        reset();
    }

    @Override
    public void onDisable() {
        reset();
    }

    @Override
    public void onLogout() {
        reset();
    }

    @Override
    public void onDisconnect(DisconnectEvent event, String reason) {
        reset();
    }

    @Override
    public void onSendPacket(PacketEvent.Send event, Packet<?> packet) {
        if (packet instanceof PlayerMoveC2SPacket.PositionAndOnGround && !full) {
            positionPacket++;
            if (positionPacket >= limit.getValue()) {
                positionPacket = 0;
                full = true;
                timer.reset();
                if (notify.getValue()) {
                    NotifyManager.newNotification(this, Vergence.TEXT.get("Module.Modules.PacketsLimit.Messages.Full"));
                }
                if (doStop.getValue()) {
                    event.cancel();
                }
            }
        } else if (packet instanceof PlayerMoveC2SPacket.Full) {
            fullPacket++;
        }
        if (full && timer.passedMs(resetTime.getValue())) {
            reset();
        }
    }

    private void reset() {
        fullPacket = 0;
        positionPacket = 0;
        full = false;
        timer.reset();
    }
}
