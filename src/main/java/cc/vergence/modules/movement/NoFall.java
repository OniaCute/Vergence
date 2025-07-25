package cc.vergence.modules.movement;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.player.EntityUtil;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {
    public static NoFall INSTANCE;
    public NoFall() {
        super("NoFall", Category.MOVEMENT);
        INSTANCE = this;
    }

    
    public Option<Boolean> horizontalCollision = addOption(new BooleanOption("HorizontalCollision", false, v -> AntiCheat.INSTANCE.isGrim()));
    public Option<Boolean> alwaysActive = addOption(new BooleanOption("AlwaysActive", false));

    @Override
    public String getDetails() {
        return AntiCheat.INSTANCE.getAntiCheat();
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (mc.player == null) {
            return ;
        }
        if (AntiCheat.INSTANCE.isGrim() && (EntityUtil.isFalling() || alwaysActive.getValue())) {
            Vergence.NETWORK.sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY() + 1.0e-9, mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), true, horizontalCollision.getValue()));
            mc.player.onLanding();
        }
    }
}
