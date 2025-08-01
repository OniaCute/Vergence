package cc.vergence.injections.accessors.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {

    @Mutable
    @Accessor("pos")
    void setPos(Vec3d pos);

    @Mutable
    @Accessor("velocity")
    void setVelocity(Vec3d pos);
    @Accessor("dimensions")
    EntityDimensions getDimensions();
}
