package cc.vergence.injections.mixins;

import cc.vergence.Vergence;
import cc.vergence.features.managers.GuiManager;
import cc.vergence.features.managers.ModuleManager;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.features.screens.ClickGuiScreen;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.ui.gui.impl.ColorComponent;
import cc.vergence.ui.gui.impl.impl.input.BindFrameComponent;
import cc.vergence.ui.gui.impl.impl.input.ColorFrameComponent;
import cc.vergence.ui.gui.impl.impl.input.DoubleFrameComponent;
import cc.vergence.ui.gui.impl.impl.input.TextFrameComponent;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class MixinKeyboard implements Wrapper {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        Vergence.EVENTS.onKeyboardActive(key, action);
    }

    @Inject(method = "onChar", at = @At("HEAD"), cancellable = true)
    private void onChar(long window, int codePoint, int modifiers, CallbackInfo ci) {
        if (window != client.getWindow().getHandle()) {
            return;
        }
        if (client.currentScreen instanceof ClickGuiScreen && Vergence.GUI != null) {
            char[] chars = Character.toChars(codePoint);
            for (GuiComponent component : GuiManager.inputComponents) {
                if (component instanceof TextFrameComponent tf && tf.isListening()) {
                    for (char c : chars) {
                        tf.charType(c);
                    }
                } else if (component instanceof DoubleFrameComponent df && df.isListening()) {
                    for (char c : chars) {
                        df.charType(c);
                    }
                } else if (component instanceof ColorFrameComponent cf && cf.isListening()) {
                    for (char c : chars) {
                        cf.charType(c);
                    }
                }
            }

            ci.cancel();
        }
    }
}
