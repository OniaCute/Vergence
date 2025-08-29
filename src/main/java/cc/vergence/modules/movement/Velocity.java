package cc.vergence.modules.movement;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.event.events.UpdateVelocityEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.injections.accessors.player.EntityVelocityUpdateS2CPacketAccessor;
import cc.vergence.injections.accessors.player.Vec3dAccessor;
import cc.vergence.modules.Module;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.player.EntityUtil;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Velocity extends Module {
    public static Velocity INSTANCE;
    private boolean cancel;
    private final FastTimerUtil lagBackTimer = new FastTimerUtil();
    private boolean flag;

    public Velocity() {
        super("Velocity", Category.MOVEMENT);
        INSTANCE = this;
    }

    public Option<Enum<?>> mode = addOption(new EnumOption("Mode", Modes.Normal));
    public Option<Double> horizontal = addOption(new DoubleOption("Horizontal", 0, 100, 100, v -> mode.getValue().equals(Modes.Normal)));
    public Option<Double> vertical = addOption(new DoubleOption("Vertical", 0, 100, 100, v -> mode.getValue().equals(Modes.Normal)));
    public Option<Boolean> flagInWall = addOption(new BooleanOption("FlagInWall", false, v -> mode.getValue().equals(Modes.Grim) || mode.getValue().equals(Modes.Wall)));
    public Option<Boolean> explosions = addOption(new BooleanOption("Explosions", true));
    public Option<Boolean> pause = addOption(new BooleanOption("Pause", true, v -> !mode.getValue().equals(Modes.Normal)));
    public Option<Boolean> pauseInLiquid = addOption(new BooleanOption("PauseInLiquid", false));
    public Option<Boolean> antiPush = addOption(new BooleanOption("AntiPush", true));
    public Option<Boolean> antiLiquidPush = addOption(new BooleanOption("AntiLiquidPush", false));
    public Option<Boolean> antiBlockPush = addOption(new BooleanOption("AntiBlockPush", true));
    public Option<Boolean> antiFishingRod = addOption(new BooleanOption("AntiFishingRod", false));

    @Override
    public String getDetails() {
        switch (((Modes) mode.getValue())) {
            case OldGrim -> {
                return "OldGrim";
            }
            case Grim -> {
                return "Grim";
            }
            case Cancel -> {
                return "0%";
            }
            default -> {
                return horizontal.getValue() + "%" + " | " + vertical.getValue() + "%";
            }
        }
    }

    @Override
    public void onTick() {
        if (mc.player == null) {
            return;
        }
        if (!mode.getValue().equals(Modes.Grim) && !mode.getValue().equals(Modes.Wall)) {
            if (!cancel) {
                return;
            }

            if (!pause.getValue() || Vergence.SERVER.getSetbackTimer().passedMs(100L)) {
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), Vergence.ROTATE.getServerYaw(), Vergence.ROTATE.getServerPitch(), mc.player.isOnGround(), mc.player.horizontalCollision));
                mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, mc.player.isCrawling() ? mc.player.getBlockPos() : mc.player.getBlockPos().up(), Direction.DOWN));
            }

            cancel = false;
        } else {
            if (mc.player != null && (mc.player.isTouchingWater() || mc.player.isSubmergedInWater() || mc.player.isInLava()) && pauseInLiquid.getValue()) {
                return;
            }

            if (flag) {
                if (lagBackTimer.passedMs(100) && (flagInWall.getValue() || !EntityUtil.isInsideBlock())) {
                    mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, mc.player.isCrawling() ? mc.player.getBlockPos() : mc.player.getBlockPos().up(), Direction.DOWN));
                }
                flag = false;
            }
        }
    }

    @Override
    public void onUpdateVelocity(UpdateVelocityEvent event, Vec3d movement, float speed) {
        if (isNull()) {
            return ;
        }

        if ((mc.player.isTouchingWater() || mc.player.isSubmergedInWater() || mc.player.isInLava()) && pauseInLiquid.getValue()) {
            return;
        }

        if (mode.getValue().equals(Modes.Grim) || mode.getValue().equals(Modes.Wall)) {
            if (!lagBackTimer.passedMs(100)) {
                return;
            }
            boolean insideBlock = EntityUtil.isInsideBlock();
            if (mode.getValue().equals(Modes.Wall) && !insideBlock) {
                return;
            }
            event.cancel();
            flag = true;
        }
    }

    @Override
    public void onReceivePacket(PacketEvent.Receive event, Packet<?> packet) {
        if (isNull()) {
            return ;
        }

        if (!mode.getValue().equals(Modes.Grim) && !mode.getValue().equals(Modes.Wall)) {
            if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket packet1) {
                if (packet1.getEntityId() != mc.player.getId()) {
                    return;
                }

                switch (((Modes) mode.getValue())) {
                    case Normal -> {
                        ((EntityVelocityUpdateS2CPacketAccessor) packet1).setVelocityX((int) (((packet1.getVelocityX() / 8000.0 - mc.player.getVelocity().x) * (horizontal.getValue().doubleValue() / 100.0)) * 8000 + mc.player.getVelocity().x * 8000));
                        ((EntityVelocityUpdateS2CPacketAccessor) packet1).setVelocityY((int) (((packet1.getVelocityY() / 8000.0 - mc.player.getVelocity().y) * (vertical.getValue().doubleValue() / 100.0)) * 8000 + mc.player.getVelocity().y * 8000));
                        ((EntityVelocityUpdateS2CPacketAccessor) packet1).setVelocityZ((int) (((packet1.getVelocityZ() / 8000.0 - mc.player.getVelocity().z) * (horizontal.getValue().doubleValue() / 100.0)) * 8000 + mc.player.getVelocity().z * 8000));
                    }
                    case Cancel -> {
                        if (pause.getValue() && !Vergence.SERVER.getSetbackTimer().passedMs(100L)) {
                            return;
                        }
                        event.cancel();
                    }
                    case OldGrim -> {
                        if (pause.getValue() && !Vergence.SERVER.getSetbackTimer().passedMs(100L)) {
                            return;
                        }
                        event.cancel();
                        cancel = true;
                    }
                }
            }
            if (event.getPacket() instanceof ExplosionS2CPacket packet1 && explosions.getValue()) {
                switch (((Modes) mode.getValue())) {
                    case Normal -> {
                        if (packet1.playerKnockback().isPresent())
                            ((Vec3dAccessor) packet1.playerKnockback().get()).setX((float) (packet1.playerKnockback().get().getX() * (horizontal.getValue().doubleValue() / 100.0)));
                        if (packet1.playerKnockback().isPresent())
                            ((Vec3dAccessor) packet1.playerKnockback().get()).setY((float) (packet1.playerKnockback().get().getY() * (vertical.getValue().doubleValue() / 100.0)));
                        if (packet1.playerKnockback().isPresent())
                            ((Vec3dAccessor) packet1.playerKnockback().get()).setZ((float) (packet1.playerKnockback().get().getZ() * (horizontal.getValue().doubleValue() / 100.0)));
                    }
                    case Cancel -> {
                        if (pause.getValue() && !Vergence.SERVER.getSetbackTimer().passedMs(100L)) {
                            return;
                        }
                        event.cancel();
                    }
                    case OldGrim -> {
                        if (pause.getValue() && !Vergence.SERVER.getSetbackTimer().passedMs(100L)) {
                            return;
                        }
                        event.cancel();
                        cancel = true;
                    }
                }

                if (event.isCancelled()) {
                    mc.executeSync(() -> {
                        Vec3d vec3d = packet1.center();
                        mc.world.playSound(vec3d.getX(), vec3d.getY(), vec3d.getZ(), packet1.explosionSound().value(), SoundCategory.BLOCKS, 4.0F, (1.0F + (mc.world.random.nextFloat() - mc.world.random.nextFloat()) * 0.2F) * 0.7F, false);
                        mc.world.addParticle(packet1.explosionParticle(), vec3d.getX(), vec3d.getY(), vec3d.getZ(), 1.0, 0.0, 0.0);
                    });
                }
            }
        } else {
            if ((mc.player.isTouchingWater() || mc.player.isSubmergedInWater() || mc.player.isInLava()) && pauseInLiquid.getValue())
                return;

            if (antiFishingRod.getValue()) {
                if (event.getPacket() instanceof EntityStatusS2CPacket packet1 && packet1.getStatus() == 31 && packet1.getEntity(mc.world) instanceof FishingBobberEntity fishHook) {
                    if (fishHook.getHookedEntity() == mc.player) {
                        event.cancel();
                    }
                }
            }
            if (!lagBackTimer.passedMs(100)) {
                return;
            }
            boolean insideBlock = EntityUtil.isInsideBlock();
            if (mode.equals(Modes.Wall) && !insideBlock) {
                return;
            }

            if (event.getPacket() instanceof ExplosionS2CPacket explosion) {
                ((Vec3dAccessor) explosion.playerKnockback().get()).setX(0);
                ((Vec3dAccessor) explosion.playerKnockback().get()).setY(0);
                ((Vec3dAccessor) explosion.playerKnockback().get()).setZ(0);
                flag = true;
            }
        }
    }

    @Override
    public void onDisable() {
        lagBackTimer.reset();
        flag = false;
        cancel = false;
    }

    public enum Modes {
        Normal,
        Cancel,
        Grim,
        OldGrim,
        Wall,
        JumpReset
    }
}
