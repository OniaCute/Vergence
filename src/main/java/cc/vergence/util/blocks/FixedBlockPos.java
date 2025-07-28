package cc.vergence.util.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class FixedBlockPos extends BlockPos {
    public FixedBlockPos(double x, double y, double z) {
        super(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
    }

    public FixedBlockPos(double x, double y, double z, boolean fix) {
        this(x, y + (fix ? 0.5 : 0), z);
    }

    public FixedBlockPos(Vec3d vec3d) {
        this(vec3d.x, vec3d.y, vec3d.z);
    }

    public FixedBlockPos(Vec3d vec3d, boolean fix) {
        this(vec3d.x, vec3d.y, vec3d.z, fix);
    }
}