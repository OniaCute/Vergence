package cc.vergence.modules.movement;

import cc.vergence.features.event.events.MoveEvent;
import cc.vergence.features.event.events.TickMovementEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import cc.vergence.util.other.FastTimerUtil;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class FastFall extends Module {
    public static FastFall INSTANCE;
    private boolean prevOnGround;
    private boolean cancelFallMovement;
    private int fallTicks;
    private final FastTimerUtil fallTimer = new FastTimerUtil();

    public FastFall() {
        super("FastFall", Category.MOVEMENT);
        INSTANCE = this;
    }

    public Option<Enum<?>> fallMode = addOption(new EnumOption("FallMode", Modes.Shift));
    public Option<Double> shiftTicks = addOption(new DoubleOption("ShiftTicks", 1, 5, 3, v -> fallMode.getValue().equals(Modes.Shift)));
    public Option<Double> distance = addOption(new DoubleOption("Distance", 1, 10, 3));

    @Override
    public String getDetails() {
            return "";
    }

    @Override
    public void onTick() {
        prevOnGround = mc.player.isOnGround();
        if (fallMode.getValue().equals(Modes.ReverseStep)) {
            if (mc.player.isRiding()
                    || mc.player.isHoldingOntoLadder()
                    || mc.player.isInLava()
                    || mc.player.isTouchingWater()
                    || mc.player.input.playerInput.jump()
                    || mc.player.input.playerInput.sneak()) {
                return;
            }
            if (mc.player.isOnGround() && isNearestBlockWithinHeight(distance.getValue())) {
                Vec3d motion = mc.player.getVelocity();
                mc.player.setVelocity(motion.getX(), -3.0, motion.getZ());
            }
        }
    }

    @Override
    public void onTickMovement(TickMovementEvent event) {
        if (fallMode.getValue().equals(Modes.Shift)) {
            if (mc.player.isRiding()
                    || mc.player.isHoldingOntoLadder()
                    || mc.player.isInLava()
                    || mc.player.isTouchingWater()
                    || mc.player.input.playerInput.jump()
                    || mc.player.input.playerInput.sneak()) {
                return;
            }
            if (!fallTimer.passedMs(1000)) {
                return;
            }
            if (mc.player.getVelocity().y < 0 && prevOnGround && !mc.player.isOnGround() && isNearestBlockWithinHeight(distance.getValue() + 0.01)) {
                fallTimer.reset();
                event.cancel();
                event.setIterations(shiftTicks.getValue().intValue());
                cancelFallMovement = true;
                fallTicks = 0;
            }
        }
    }

    @Override
    public void onMoveEvent(MoveEvent event, double x, double y, double z) {
        if (isNull()) {
            return;
        }
        if (cancelFallMovement && fallMode.getValue().equals(Modes.Shift)) {
            event.setX(0.0);
            event.setZ(0.0);
            Vec3d motion = mc.player.getVelocity();
            mc.player.setVelocity(0, motion.y, 0);
            ++fallTicks;
            if (fallTicks > shiftTicks.getValue()) {
                cancelFallMovement = false;
                fallTicks = 0;
            }
        }
    }

    private boolean isNearestBlockWithinHeight(double height) {
        Box bb = mc.player.getBoundingBox();
        for (double i = 0; i < height + 0.5; i += 0.01) {
            if (!mc.world.isSpaceEmpty(mc.player, bb.offset(0, -i, 0))) {
                return true;
            }
        }
        return false;
    }

    public enum Modes {
        Shift,
        ReverseStep
    }
}
