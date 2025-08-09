package cc.vergence.injections.mixins.client;

import cc.vergence.Vergence;
import cc.vergence.features.managers.feature.ModuleManager;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.Title;
import cc.vergence.modules.player.NoCooldown;
import cc.vergence.modules.player.AutoRespawn;
import cc.vergence.modules.player.MultipleTask;
import cc.vergence.util.font.FontRenderers;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.other.SkiaContext;
import cc.vergence.util.render.utils.Render2DUtil;
import cc.vergence.util.render.utils.blur.KawaseBlur;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient extends ReentrantThreadExecutor<Runnable> implements Wrapper {
    @Unique
    private boolean worldIsNull = true;

    @Shadow
    @Nullable
    public ClientWorld world;

    @Shadow
    @Final
    private Window window;

    @Shadow
    public int attackCooldown;

    public MixinMinecraftClient(String string) {
        super(string);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    void postWindowInit(RunArgs args, CallbackInfo ci) {
        try {
            SkiaContext.createSurface(window.getWidth(), window.getHeight());
            FontUtil.LOADED = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @author Onia/Voury & love
     * @reason No Reason, just change the Title.
     */
    @Overwrite
    private String getWindowTitle() {
        return Title.INSTANCE != null && Title.INSTANCE.getStatus() ? (Title.title.isEmpty() ? "Vergence Client | Get unique sense of the Minecraft" : Title.title) : "Vergence Client | Get unique sense of the Minecraft";
    }

    @Inject(at = @At("HEAD"), method = "tick()V")
    public void tick(CallbackInfo info) {
        if (this.world != null) {
            if (Vergence.EVENTS != null && Vergence.LOADED) {
                Vergence.EVENTS.onTick();
            }
        }
        if (!Vergence.LOADED) {
            return;
        }
        if (worldIsNull && mc.world != null) {
            worldIsNull = false;
            for (Module module : ModuleManager.modules) {
                if (module.getStatus()) {
                    module.onLogin();
                }
            }
        } else if (!worldIsNull && mc.world == null) {
            worldIsNull = true;
            for (Module module : ModuleManager.modules) {
                if (module.getStatus()) {
                    module.onLogout();
                }
            }
        }
    }

    @Inject(method = "doAttack", at = @At("HEAD"))
    private void doAttack(CallbackInfoReturnable<Boolean> info) {
        if (NoCooldown.INSTANCE != null && NoCooldown.INSTANCE.forAttack.getValue()) {
            attackCooldown = 0;
        }
    }


    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreen(Screen screen, CallbackInfo info) {
        if (screen instanceof DeathScreen && mc.player != null && AutoRespawn.INSTANCE.getStatus()) {
            mc.player.requestRespawn();
            info.cancel();
        }
    }

    @ModifyExpressionValue(method = "handleBlockBreaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean handleBlockBreaking(boolean original) {
        if (MultipleTask.INSTANCE != null && MultipleTask.INSTANCE.getStatus()) {
            return false;
        }
        return original;
    }

    @ModifyExpressionValue(method = "doItemUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isBreakingBlock()Z"))
    private boolean handleInputEvents(boolean original) {
        if (MultipleTask.INSTANCE != null && MultipleTask.INSTANCE.getStatus()) {
            return false;
        }
        return original;
    }

    @Inject(method = "onResolutionChanged", at = @At("TAIL"))
    public void onResolutionChanged(CallbackInfo info) {
        KawaseBlur.GUI_BLUR.resize();
        KawaseBlur.INGAME_BLUR.resize();
    }
}
