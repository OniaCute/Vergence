package cc.vergence.injections.mixins.entity;

import cc.vergence.Vergence;
import cc.vergence.modules.visual.NameTags;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer<T extends Entity, S extends EntityRenderState> {
    @Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
    private void getDisplayName(T entity, CallbackInfoReturnable<Text> info) {
        if (entity instanceof PlayerEntity && NameTags.INSTANCE.getStatus()) {
            info.setReturnValue(null);
        }
    }
}