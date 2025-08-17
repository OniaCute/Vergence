package cc.vergence.modules.player;

import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.other.FastTimerUtil;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.util.math.BlockPos;

public class PortalGod extends Module {
    public static PortalGod INSTANCE;
    private final FastTimerUtil timer = new FastTimerUtil();
    private boolean teleported = false;

    public PortalGod() {
        super("PortalGod", Category.PLAYER);
        INSTANCE = this;
    }

    public Option<Boolean> inventory = addOption(new BooleanOption("Inventory", true));
    public Option<Boolean> godMode = addOption(new BooleanOption("GodMode", false));
    public Option<Double> confirmTime = addOption(new DoubleOption("ConfirmTime", 0, 99999, 200).setUnit("ms").addSpecialValue(0, "NoDelay").addSpecialValue(99999, "INFINITY"));

    @Override
    public String getDetails() {
        return godMode.getValue() ? (teleported ? "GodMode" : timer.passedMs(confirmTime.getValue()) ? "Available" : "Waiting") : "";
    }

    @Override
    public void onEnable() {
        timer.reset();
        teleported = false;
    }

    @Override
    public void onDisable() {
        timer.reset();
        teleported = false;
    }

    @Override
    public void onSendPacket(PacketEvent.Send event, Packet<?> packet) {
        if (godMode.getValue()) {
            if (packet instanceof TeleportConfirmC2SPacket && timer.passedMs(confirmTime.getValue())) {
                teleported = true;
                event.cancel();
            }
        }
    }

    @Override
    public void onTick() {
        if (!godMode.getValue()) {
            return ;
        }

        for (int x = (int) (mc.player.getX() - 2); x < mc.player.getX() + 2; x++) {
            for (int z = (int) (mc.player.getZ() - 2); z < mc.player.getZ() + 2; z++) {
                for (int y = (int) (mc.player.getY() - 2); y < mc.player.getY() + 2; y++) {
                    if (mc.world.getBlockState(BlockPos.ofFloored(x, y, z)).getBlock() == Blocks.NETHER_PORTAL) {
                        timer.reset();
                    }
                }
            }
        }
    }
}
