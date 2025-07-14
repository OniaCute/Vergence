package cc.vergence.injections.mixins;

import cc.vergence.Vergence;
import cc.vergence.features.managers.ModuleManager;
import cc.vergence.modules.Module;
import cc.vergence.modules.combat.NoCooldown;
import cc.vergence.util.font.FontRenderers;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
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

import java.util.Random;

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
        String[] text = {
                "Get unique sense of the Minecraft.",
                "No crack, No leak, No deserted."
        };

        Random random = new Random();
        int randomIndex = random.nextInt(text.length);
        String randomSentence = text[randomIndex];

        return "Vergence Client | " + randomSentence + "  -  emotionclient.cc";
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

//    @Inject(method = "doAttack", at = @At("HEAD"))
//    private void doAttack(CallbackInfoReturnable<Boolean> info) {
//        if (NoCooldown.INSTANCE != null && NoCooldown.INSTANCE.forAttack.getValue()) {
//            attackCooldown = 0;
//        }
//    }
}
