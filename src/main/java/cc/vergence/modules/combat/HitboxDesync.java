package cc.vergence.modules.combat;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.modules.Module;
import cc.vergence.util.other.FastTimerUtil;
import lombok.Getter;
import net.minecraft.util.math.Vec3d;

public class HitboxDesync extends Module {
    public static HitboxDesync INSTANCE;
    private final FastTimerUtil timer = new FastTimerUtil();
    private double prevY;

    public HitboxDesync() {
        super("HitBoxDesync", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Boolean> alternating = addOption(new BooleanOption("Alternating", false));
    public Option<Boolean> minimal = addOption(new BooleanOption("specific", false, v -> alternating.getValue()));
    public Option<Boolean> specific = addOption(new BooleanOption("Specific", false, v -> alternating.getValue()));
    public Option<Boolean> autoDisable = addOption(new BooleanOption("AutoDisable", false, v -> alternating.getValue()));
    public Option<Boolean> jumpDisable = addOption(new BooleanOption("JumpDisable", false, v -> alternating.getValue()));


    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onTick() {
        if (isNull() || (jumpDisable.getValue() && mc.player.getY() != prevY)) {
            disable();
            return;
        }

        Vec3d vec3d = mc.player.getBlockPos().toCenterPos();
        double offset = minimal.getValue() ? 0.001 : 0.002;
        double timeout = specific.getValue() ? 500 : 1500;

        boolean flag = timer.passedMs(timeout) && alternating.getValue() && !mc.player.isSneaking();
        boolean flagX = (vec3d.x - mc.player.getX()) > 0;
        boolean flagZ = (vec3d.z - mc.player.getZ()) > 0;

        double x = vec3d.x + ((flag ? offset : 0) * (flagX ? 1 : -1)) + 0.20000000009497754 * (flagX ? -1 : 1);
        double z = vec3d.z + ((flag ? offset : 0) * (flagZ ? 1 : -1)) + 0.2000000000949811 * (flagZ ? -1 : 1);

        mc.player.setPosition(x, mc.player.getY(), z);

        if (timer.passedMs(timeout)) {
            timer.reset();
        }

        if (autoDisable.getValue() && !alternating.getValue()) {
            disable();
        }
    }

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) {
            enable();
            return;
        }

        prevY = mc.player.getY();
    }
}
