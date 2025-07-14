package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class PlaceBlockEvent extends Event {
    private static final PlaceBlockEvent INSTANCE = new PlaceBlockEvent();

    public BlockPos blockPos;
    public Block block;

    public PlaceBlockEvent() {
        super(Stage.Pre);
    }

    public static PlaceBlockEvent get(BlockPos blockPos, Block block) {
        INSTANCE.blockPos = blockPos;
        INSTANCE.block = block;
        return INSTANCE;
    }
}