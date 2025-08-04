package cc.vergence.modules.combat;

import cc.vergence.features.enums.player.TargetTypes;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;

import java.util.EnumSet;

public class AutoAim extends Module {
    public static AutoAim INSTANCE;

    public AutoAim() {
        super("AutoAim", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Double> fov = addOption(new DoubleOption("fov", 1, 180, 30));
    public Option<EnumSet<TargetTypes>> targets = addOption(new MultipleOption<TargetTypes>("Targets", EnumSet.of(TargetTypes.EnemyPlayers)));

    @Override
    public String getDetails() {
        return "";
    }
}
