package cc.vergence.modules.visual;

import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

import java.awt.*;
import java.util.EnumSet;

public class WorldTweaks extends Module {
    public static WorldTweaks INSTANCE;
    private long oldTime;

    public WorldTweaks() {
        super("WorldTweaks", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<EnumSet<Items>> items = addOption(new MultipleOption<Items>("Items", EnumSet.of(Items.Time, Items.Fog)));
    public Option<Double> time = addOption(new DoubleOption("Time", 0, 23, 21, v -> items.getValue().contains(Items.Time)));
    public Option<Double> fogAppear = addOption(new DoubleOption("FogAppear", 0, 256, 0, v -> items.getValue().contains(Items.Fog)));
    public Option<Double> fogDisappear = addOption(new DoubleOption("FogDisappear", 6, 256, 64, v -> items.getValue().contains(Items.Fog)));
    public Option<Color> fogColor = addOption(new ColorOption("FogColor", new Color(225, 109, 213), v -> items.getValue().contains(Items.Fog)));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onEnable() {
        if (isNull()) {
            return ;
        }
        oldTime = mc.world.getTime();
    }

    @Override
    public void onDisable() {
        if (isNull()) {
            return ;
        }
        mc.world.setTime(oldTime, oldTime, true);
    }

    @Override
    public void onReceivePacket(PacketEvent.Receive event, Packet<?> packet) {
        if (packet instanceof WorldTimeUpdateS2CPacket && items.getValue().contains(Items.Time)) {
            oldTime = ((WorldTimeUpdateS2CPacket) packet).time();
            event.cancel();
        }
    }

    @Override
    public void onTick() {
        if (items.getValue().contains(Items.Time)) {
            mc.world.setTime((long) (time.getValue() * 1000), (long) (time.getValue() * 1000), false);
        }
    }

    public enum Items {
        Time,
        Fog
    }
}
