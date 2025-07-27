package cc.vergence.modules.player;

import cc.vergence.Vergence;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class FastDrop extends Module {
    public static FastDrop INSTANCE;
    private int dropTicks;

    public FastDrop() {
        super("FastDrop", Category.PLAYER);
        INSTANCE = this;
    }

    public Option<Double> delay = addOption(new DoubleOption("Delay", 0, 4, 0));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onTick() {
        if (isNull()) {
            return ;
        }

        if (mc.options.dropKey.isPressed() && dropTicks > delay.getValue()) {
            Vergence.NETWORK.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.DROP_ITEM, BlockPos.ORIGIN, Direction.DOWN));
            dropTicks = 0;
        }
        ++dropTicks;
    }
}
