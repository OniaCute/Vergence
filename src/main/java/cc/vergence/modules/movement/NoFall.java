package cc.vergence.modules.movement;

import cc.vergence.Vergence;
import cc.vergence.features.enums.AntiCheats;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.managers.MessageManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.AntiCheat;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", Category.MOVEMENT);
    }

    public Option<Boolean> horizontalCollision = addOption(new BooleanOption("HorizontalCollision", false, v -> AntiCheat.INSTANCE.antiCheat.getValue().equals(AntiCheats.Grim)));
    public Option<Boolean> alwaysActive = addOption(new BooleanOption("AlwaysActive", false));

    @Override
    public String getDetails() {
        return AntiCheat.INSTANCE != null ? AntiCheat.INSTANCE.antiCheat.getValue().name() : "Unknown";
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (mc.player == null || AntiCheat.INSTANCE == null || !AntiCheat.INSTANCE.getStatus()) {
            return ;
        }


        if (AntiCheat.INSTANCE.antiCheat.getValue().equals(AntiCheats.Grim) && (isFalling() || alwaysActive.getValue())) {
            Vergence.NETWORK.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY() + 1.0e-9, mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), true, horizontalCollision.getValue()));
            mc.player.onLanding();
        }
    }

    private boolean isFalling() {
        return mc.player.fallDistance > mc.player.getSafeFallDistance() || !mc.player.isOnGround() || mc.world.getBlockState(new BlockPos((int) mc.player.getX(), (int) (mc.player.getY()) - 1, (int) mc.player.getZ())).getBlock().equals(Blocks.AIR);
    }
}
