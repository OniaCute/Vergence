package cc.vergence.modules.combat;

import cc.vergence.features.event.events.MoveEvent;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.player.EntityUtil;
import net.minecraft.util.hit.HitResult;

public class AutoWtap extends Module {
    public static AutoWtap INSTANCE;
    private final FastTimerUtil timer = new FastTimerUtil();
    public boolean wasAttacked;

    public AutoWtap() {
        super("AutoWtap", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Double> minTime = addOption(new DoubleOption("MinTime", 0, 1000, 120).setUnit("ms"));
    public Option<Double> maxTime = addOption(new DoubleOption("MaxTime", 0, 1000, 200).setUnit("ms"));
    public Option<Boolean> debug = addOption(new BooleanOption("Debug", false));

    @Override
    public String getDetails() {
        return debug.getValue() ? wasAttacked ? "Stopping" : "Waiting" : "";
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    @Override
    public void onDisable() {
        wasAttacked = false;
        timer.reset();
    }

    @Override
    public void onPlayerUpdateEventAlways(PlayerUpdateEvent event) {
        if (isNull()) {
            return ;
        }
        if (!this.getStatus() && wasAttacked) {
            mc.options.backKey.setPressed(false);
        }
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (isNull()) {
            return;
        }

        if (mc.options.attackKey.isPressed() && mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            if (!wasAttacked) {
                wasAttacked = true;
                timer.reset();
            }
        }
        double minDelay = minTime.getValue();
        double maxDelay = maxTime.getValue();
        if (minDelay > maxDelay) {
            double temp = minDelay;
            minDelay = maxDelay;
            maxDelay = temp;
        }
        double delay = minDelay + RANDOM.nextDouble() * (maxDelay - minDelay);
        if (wasAttacked && timer.passedMs(delay)) {
            wasAttacked = false;
        }
    }

    @Override
    public void onMoveEvent(MoveEvent event, double x, double y, double z) {
        if (isNull()) {
            return ;
        }

        if (wasAttacked && !EntityUtil.isFalling() && mc.player.isOnGround()) {
            event.set(0, 0, 0);
            event.cancel();
        }
    }
}
