package cc.vergence.injections.mixins;

import cc.vergence.features.screens.MainMenuScreen;
import cc.vergence.modules.client.MainMenu;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen implements Wrapper {
    protected MixinTitleScreen(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void hookInit(CallbackInfo ci) {
        if (MainMenu.INSTANCE != null && MainMenu.INSTANCE.getStatus() && !(mc.currentScreen instanceof MainMenuScreen)) {
            mc.setScreen(new MainMenuScreen());
        }
    }
}