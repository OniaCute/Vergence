package cc.vergence.modules.combat;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;

public class NoCooldown extends Module {
    public static NoCooldown INSTANCE;
    public NoCooldown() {
        super("NoCooldown", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Boolean> forAttack = addOption(new BooleanOption("Attack", false));
    public Option<Boolean> forJump = addOption(new BooleanOption("Jump", false));
    public Option<Double> jumpTicks = addOption(new DoubleOption("JumpTicks", 0, 20, 1));

    @Override
    public String getDetails() {
        return forAttack.getValue() ? "Unsafe" : forJump.getValue() ? "Unsafe" : "None";
    }
}
