package cc.vergence.modules.misc;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.modules.Module;
import cc.vergence.util.maths.MathUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class LagNotify extends Module {
    public static LagNotify INSTANCE;
    Vec3d lagPos = null;
    double lagDistance;
    long laggingTime = System.currentTimeMillis();

    public LagNotify() {
        super("LagNotify", Category.MISC);
        INSTANCE = this;
    }

    public Option<Boolean> serverLagging = addOption(new BooleanOption("ServerLagging", true));
    public Option<Boolean> clientLayback = addOption(new BooleanOption("ClientLayback", true));
    public Option<Boolean> withSound = addOption(new BooleanOption("Sounds", true));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onReceivePacket(PacketEvent.Receive event, Packet<?> packet) {
        if(isNull()) {
            return;
        }

        if(event.getPacket() instanceof PlayerPositionLookS2CPacket packet1) {
            lagPos = new Vec3d(packet1.change().position().getX(), packet1.change().position().getY(), packet1.change().position().getZ());
            lagDistance = mc.player.getPos().distanceTo(lagPos);
            laggingTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if(isNull()) {
            return;
        }

        if (serverLagging.getValue() && Vergence.SERVER.getResponseTimer().passedMs(1000)) {
            NotifyManager.newNotification(this, Vergence.TEXT.get("Module.Modules.LagNotify.Messages.ServerLag").replace("{timer}", String.valueOf(MathUtil.round(Vergence.SERVER.getResponseTimer().getGapMs() / 1000f, 1))));
        }

        if (clientLayback.getValue() && System.currentTimeMillis() - laggingTime < 3000) {
            NotifyManager.newNotification(this, Vergence.TEXT.get("Module.Modules.LagNotify.Messages.ClientLag").replace("{timer}", String.valueOf(MathUtil.round((System.currentTimeMillis() - laggingTime) / 1000f, 1))));
        }

        if (withSound.getValue()) {
            mc.world.playSound(mc.player, mc.player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
        }
    }
}
