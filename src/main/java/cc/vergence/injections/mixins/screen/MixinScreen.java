package cc.vergence.injections.mixins.screen;

import cc.vergence.modules.client.MainMenu;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class MixinScreen implements Wrapper {
    @Shadow public int width;
    @Shadow public int height;

    @Inject(method = "renderBackground", at = @At("HEAD"))
    private void renderBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (MainMenu.INSTANCE != null && MainMenu.INSTANCE.getStatus() && MainMenu.INSTANCE.background.getValue()) {
            Render2DUtil.drawRect(0, 0, width, height, MainMenu.INSTANCE.backgroundColor.getValue());
        }
    }
}
