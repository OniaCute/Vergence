package cc.vergence.modules.movement;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.combat.KillAura;

public class AutoSprint extends Module {
    public static AutoSprint INSTANCE;
    public AutoSprint() {
        super("AutoSprint", Category.MOVEMENT);
        INSTANCE = this;
    }

    public Option<Boolean> forAttack = addOption(new BooleanOption("Attack", false));
    public Option<Double> attackCounteract = addOption(new DoubleOption("AttackCounteract", 0, 1, 0));
    public Option<Boolean> useItem = addOption(new BooleanOption("UseItem", false));

    @Override
    public String getDetails() {
        if (mc.player == null) {
            return "Unknown";
        }
        return mc.player.isSprinting() ? "Sprinting" : "NoSprint";
    }

    @Override
    public void onTick() {
        if (mc.player == null) {
            return;
        }

        mc.player.setSprinting(
                mc.player.getHungerManager().getFoodLevel() > 6
                        && !mc.player.horizontalCollision
                        && mc.player.input.movementForward > 0
                        && (!mc.player.isSneaking())
                        && (!mc.player.isUsingItem() || useItem.getValue())
                        && (!KillAura.INSTANCE.getStatus())
        );
    }
}
