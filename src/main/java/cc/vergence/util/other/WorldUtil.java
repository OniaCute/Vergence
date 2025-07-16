package cc.vergence.util.other;

import cc.vergence.features.enums.Dimensions;
import cc.vergence.util.interfaces.Wrapper;
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
}
