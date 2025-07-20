package cc.vergence.injections.mixins.render;

import cc.vergence.Vergence;
import cc.vergence.modules.misc.BetterTab;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerListHud.class)
public class MixinPlayerListHud {
    @Shadow
    @Final
    private MinecraftClient client;

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 0)
    private List<PlayerListEntry> modifyPlayerList(List<PlayerListEntry> original) {
        if (BetterTab.INSTANCE == null || !BetterTab.INSTANCE.getStatus()) {
            return original;
        }

        int limit = BetterTab.INSTANCE.playerLimit.getValue().intValue();
        if (limit < 10000 && original.size() > limit) {
            return original.subList(0, limit);
        }
        return original;
    }

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void onGetPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        if (BetterTab.INSTANCE == null || !BetterTab.INSTANCE.getStatus()) {
            return;
        }

        String name = entry.getProfile().getName();

        if (BetterTab.INSTANCE.forMyself.getValue() && name.equals(client.player.getGameProfile().getName())) {
            cir.setReturnValue(Text.literal(name).styled(style -> style.withColor(BetterTab.INSTANCE.myColor.getValue().getRGB())));
            return;
        }
        if (BetterTab.INSTANCE.forFriends.getValue() && Vergence.FRIEND.isFriend(name)) {
            cir.setReturnValue(Text.literal(name).styled(style -> style.withColor(BetterTab.INSTANCE.friendColor.getValue().getRGB())));
            return;
        }
        if (BetterTab.INSTANCE.forEnemy.getValue() && Vergence.ENEMY.isEnemy(name)) {
            cir.setReturnValue(Text.literal(name).styled(style -> style.withColor(BetterTab.INSTANCE.enemyColor.getValue().getRGB())));
        }
    }
}
