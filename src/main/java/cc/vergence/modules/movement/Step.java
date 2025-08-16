package cc.vergence.modules.movement;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.MoveEvent;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.AntiCheat;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Step extends Module {
    public static Step INSTANCE;
    private boolean resetTimer = false;

    public Step() {
        super("Step", Category.MOVEMENT);
        INSTANCE = this;
    }

    public Option<Double> height = addOption(new DoubleOption("Height", 0, 12, 2));
    public Option<Boolean> useTimer = addOption(new BooleanOption("UseTimer", true, v -> AntiCheat.INSTANCE.isNCP()));
    public Option<Boolean> debug = addOption(new BooleanOption("Debug", true));

    @Override
    public String getDetails() {
        return debug.getValue() ? AntiCheat.INSTANCE.getAntiCheat() : "";
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (!resetTimer) {
            return ;
        }
        Vergence.TIMER.set(1f);
    }

    @Override
    public void onMoveEvent(MoveEvent event, double x, double y, double z) {
        if (isNull() || !AntiCheat.INSTANCE.isNCP()) {
            return ;
        }

        if (mc.player.getY() - mc.player.prevY <= 0.75 || mc.player.getY() - mc.player.prevY > height.getValue()) {
            return;
        }
        double[] offsets = getOffset(mc.player.getY() - mc.player.prevY);
        if (offsets != null && offsets.length > 1) {
            if (useTimer.getValue()) {
                Vergence.TIMER.set(1.0f / offsets.length);
                resetTimer = true;
            }

            for (double offset : offsets) {
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.prevX, mc.player.prevY + offset, mc.player.prevZ, false, mc.player.horizontalCollision));
            }
        }
    }

    private double[] getOffset(double height) {
        return switch ((int) (height * 10000)) {
            case 7500, 10000 -> new double[]{0.42, 0.753};
            case 8125, 8750 -> new double[]{0.39, 0.7};
            case 15000 -> new double[]{0.42, 0.75, 1.0, 1.16, 1.23, 1.2};
            case 20000 -> new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43};
            case 250000 -> new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
            default -> null;
        };
    }
}
