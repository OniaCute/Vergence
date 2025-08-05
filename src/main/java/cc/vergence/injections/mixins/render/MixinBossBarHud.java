package cc.vergence.injections.mixins.render;

import cc.vergence.Vergence;
import cc.vergence.modules.visual.NoRender;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class MixinBossBarHud {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void render(CallbackInfo ci) {
        if (NoRender.INSTANCE.getStatus() && NoRender.INSTANCE.noBossBar.getValue()) {
            ci.cancel();
        }
    }
}
