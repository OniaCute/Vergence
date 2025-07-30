package cc.vergence.injections.mixins.client;

import cc.vergence.Vergence;
import cc.vergence.features.managers.feature.ModuleManager;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.Title;
import cc.vergence.modules.combat.NoCooldown;
import cc.vergence.modules.player.AutoRespawn;
import cc.vergence.modules.player.MultipleTask;
import cc.vergence.util.font.FontRenderers;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.interfaces.Wrapper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient extends ReentrantThreadExecutor<Runnable> implements Wrapper {
    private boolean worldIsNull = true;
    @Shadow @Nullable public ClientWorld world;
    @Shadow public int attackCooldown;

    public MixinMinecraftClient(String string) {
        super(string);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    void postWindowInit(RunArgs args, CallbackInfo ci) {
        try {
            FontRenderers.SMOOTH_3F = FontRenderers.SmoothFont(3f);
            FontRenderers.SMOOTH_4F = FontRenderers.SmoothFont(4f);
            FontRenderers.SMOOTH_5F = FontRenderers.SmoothFont(5f);
            FontRenderers.SMOOTH_6F = FontRenderers.SmoothFont(6f);
            FontRenderers.SMOOTH_7F = FontRenderers.SmoothFont(7f);
            FontRenderers.SMOOTH_8F = FontRenderers.SmoothFont(8f);
            FontRenderers.SMOOTH_9F = FontRenderers.SmoothFont(9f);
            FontRenderers.SMOOTH_10F = FontRenderers.SmoothFont(10f);
            FontRenderers.SMOOTH_12F = FontRenderers.SmoothFont(12f);
            FontRenderers.SMOOTH_14F = FontRenderers.SmoothFont(14f);
            FontRenderers.SMOOTH_15F = FontRenderers.SmoothFont(15f);
            FontRenderers.SMOOTH_16F = FontRenderers.SmoothFont(16f);
            FontRenderers.SMOOTH_18F = FontRenderers.SmoothFont(18f);
            FontRenderers.SMOOTH_20F = FontRenderers.SmoothFont(20f);
            FontRenderers.SMOOTH_21F = FontRenderers.SmoothFont(21f);
            FontRenderers.SMOOTH_24F = FontRenderers.SmoothFont(24f);
            FontRenderers.SMOOTH_28F = FontRenderers.SmoothFont(28f);
            FontRenderers.SMOOTH_32F = FontRenderers.SmoothFont(32f);

            FontRenderers.SANS_3F = FontRenderers.SansFont(3f);
            FontRenderers.SANS_4F = FontRenderers.SansFont(4f);
            FontRenderers.SANS_5F = FontRenderers.SansFont(5f);
            FontRenderers.SANS_6F = FontRenderers.SansFont(6f);
            FontRenderers.SANS_7F = FontRenderers.SansFont(7f);
            FontRenderers.SANS_8F = FontRenderers.SansFont(8f);
            FontRenderers.SANS_9F = FontRenderers.SansFont(9f);
            FontRenderers.SANS_10F = FontRenderers.SansFont(10f);
            FontRenderers.SANS_12F = FontRenderers.SansFont(12f);
            FontRenderers.SANS_14F = FontRenderers.SansFont(14f);
            FontRenderers.SANS_15F = FontRenderers.SansFont(15f);
            FontRenderers.SANS_16F = FontRenderers.SansFont(16f);
            FontRenderers.SANS_18F = FontRenderers.SansFont(18f);
            FontRenderers.SANS_20F = FontRenderers.SansFont(20f);
            FontRenderers.SANS_21F = FontRenderers.SansFont(21f);
            FontRenderers.SANS_24F = FontRenderers.SansFont(24f);
            FontRenderers.SANS_28F = FontRenderers.SansFont(28f);
            FontRenderers.SANS_32F = FontRenderers.SansFont(32f);

            FontRenderers.RHR_3F = FontRenderers.RhrFont(3f);
            FontRenderers.RHR_4F = FontRenderers.RhrFont(4f);
            FontRenderers.RHR_5F = FontRenderers.RhrFont(5f);
            FontRenderers.RHR_6F = FontRenderers.RhrFont(6f);
            FontRenderers.RHR_7F = FontRenderers.RhrFont(7f);
            FontRenderers.RHR_8F = FontRenderers.RhrFont(8f);
            FontRenderers.RHR_9F = FontRenderers.RhrFont(9f);
            FontRenderers.RHR_10F = FontRenderers.RhrFont(10f);
            FontRenderers.RHR_12F = FontRenderers.RhrFont(12f);
            FontRenderers.RHR_14F = FontRenderers.RhrFont(14f);
            FontRenderers.RHR_15F = FontRenderers.RhrFont(15f);
            FontRenderers.RHR_16F = FontRenderers.RhrFont(16f);
            FontRenderers.RHR_18F = FontRenderers.RhrFont(18f);
            FontRenderers.RHR_20F = FontRenderers.RhrFont(20f);
            FontRenderers.RHR_21F = FontRenderers.RhrFont(21f);
            FontRenderers.RHR_24F = FontRenderers.RhrFont(24f);
            FontRenderers.RHR_28F = FontRenderers.RhrFont(28f);
            FontRenderers.RHR_32F = FontRenderers.RhrFont(32f);

            FontRenderers.ICON_3F = FontRenderers.IconFont(3f);
            FontRenderers.ICON_4F = FontRenderers.IconFont(4f);
            FontRenderers.ICON_5F = FontRenderers.IconFont(5f);
            FontRenderers.ICON_6F = FontRenderers.IconFont(6f);
            FontRenderers.ICON_7F = FontRenderers.IconFont(7f);
            FontRenderers.ICON_8F = FontRenderers.IconFont(8f);
            FontRenderers.ICON_9F = FontRenderers.IconFont(9f);
            FontRenderers.ICON_10F = FontRenderers.IconFont(10f);
            FontRenderers.ICON_12F = FontRenderers.IconFont(12f);
            FontRenderers.ICON_14F = FontRenderers.IconFont(14f);
            FontRenderers.ICON_15F = FontRenderers.IconFont(15f);
            FontRenderers.ICON_16F = FontRenderers.IconFont(16f);
            FontRenderers.ICON_18F = FontRenderers.IconFont(18f);
            FontRenderers.ICON_20F = FontRenderers.IconFont(20f);
            FontRenderers.ICON_21F = FontRenderers.IconFont(21f);
            FontRenderers.ICON_24F = FontRenderers.IconFont(24f);
            FontRenderers.ICON_28F = FontRenderers.IconFont(28f);
            FontRenderers.ICON_32F = FontRenderers.IconFont(32f);

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
}
