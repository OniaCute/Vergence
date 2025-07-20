package cc.vergence.util.other;

import cc.vergence.features.enums.Dimensions;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldUtil implements Wrapper {
    public static Dimensions getDimension() {
        if (mc.world == null) {
            return Dimensions.Overworld;
        }

        if (mc.world.getRegistryKey().equals(World.NETHER)) {
            return Dimensions.Nether;
        }
        else if (mc.world.getRegistryKey().equals(World.END)) {
            return Dimensions.TheEnd;
        }
        else {
            return Dimensions.Overworld;
        }
    }

    public static boolean equals(BlockPos x, BlockPos y) {
        if(x == null && y == null) {
            return true;
        } else if(x == null || y == null) {
            return false;
        } else {
            return x.equals(y);
        }
    }
}
