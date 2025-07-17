package cc.vergence.util.blocks;

import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.List;

public class BlockUtil implements Wrapper {
    public static final List<Block> shiftBlocks = Arrays.asList(
            Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE,
            Blocks.BIRCH_TRAPDOOR, Blocks.BAMBOO_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR, Blocks.CHERRY_TRAPDOOR,
            Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER,
            Blocks.ACACIA_TRAPDOOR, Blocks.ENCHANTING_TABLE, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX,
            Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX
    );

    public static BlockHitResult findPlaceableFace(BlockPos target) {
        return findPlaceableFace(target, 1);
    }

    public static BlockHitResult findPlaceableFace(BlockPos center, int range) {
        BlockPos.Mutable searchPos = new BlockPos.Mutable();
        Direction[] directions = Direction.values();

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    searchPos.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                    for (Direction direction : directions) {
                        BlockPos neighbor = searchPos.offset(direction);
                        BlockState neighborState = mc.world.getBlockState(neighbor);
                        if (!neighborState.isAir() && !neighborState.getCollisionShape(mc.world, neighbor).isEmpty()) {
                            Vec3d hitPos = Vec3d.ofCenter(neighbor).add(Vec3d.of(direction.getVector()).multiply(0.5));
                            return new BlockHitResult(hitPos, direction.getOpposite(), neighbor, false);
                        }
                    }
                }
            }
        }
        return null;
    }
}
