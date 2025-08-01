package cc.vergence.injections.mixins.screen;

import cc.vergence.modules.client.MainMenu;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DownloadingTerrainScreen.class)
public class MixinDownloadingTerrainScreen extends Screen implements Wrapper {
    protected MixinDownloadingTerrainScreen(Text title) {
        super(title);
    }

    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    private void renderBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (MainMenu.INSTANCE != null && MainMenu.INSTANCE.getStatus() && MainMenu.INSTANCE.background.getValue()) {
            Render2DUtil.drawRect(context, 0, 0, width, height, MainMenu.INSTANCE.backgroundColor.getValue());
            ci.cancel();
        }
    }
}
