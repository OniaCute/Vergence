package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;
import net.minecraft.util.math.BlockPos;

public class ClickBlockEvent extends Event {
    private final BlockPos pos;

    public ClickBlockEvent(BlockPos pos) {
        super(Stage.Pre);
        this.pos = pos;
    }

    public BlockPos getBlockPos() {
        return pos;
    }
}

