package cc.vergence.injections.mixins.player;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.BlockCollisionEvent;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.BlockView;
import net.minecraft.world.CollisionView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockCollisionSpliterator.class)
public class MixinBlockCollisionSpliterator implements Wrapper {
    @Redirect(method = "computeNext", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/ShapeContext;getCollisionShape(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/CollisionView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/shape/VoxelShape;"))
    private VoxelShape hookGetCollisionShape(ShapeContext instance, BlockState blockState, CollisionView collisionView, BlockPos pos) {
        VoxelShape voxelShape = instance.getCollisionShape(blockState, collisionView, pos);
        if (collisionView != mc.world) {
            return voxelShape;
        }
        BlockCollisionEvent blockCollisionEvent = new BlockCollisionEvent(voxelShape, pos, blockState);
        Vergence.EVENTBUS.post(blockCollisionEvent);
        if (blockCollisionEvent.isCancelled()) {
            return blockCollisionEvent.getVoxelShape();
        }
        return voxelShape;
    }
}