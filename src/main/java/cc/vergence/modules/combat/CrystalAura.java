package cc.vergence.modules.combat;

import cc.vergence.features.enums.player.TargetTypes;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;

import java.util.*;

public class CrystalAura extends Module {
    public static CrystalAura INSTANCE;

    public CrystalAura() {
        super("CrystalAura", Category.COMBAT);
        INSTANCE = this;
    }

    // Page Option
    public Option<Enum<?>> page = addOption(new EnumOption("Page", Pages.General));

    // Page - General
    // Page - General | Normally
    public Option<EnumSet<TargetTypes>> targets = addOption(new MultipleOption<TargetTypes>("Targets", EnumSet.of(TargetTypes.EnemyPlayers, TargetTypes.Invisible), v -> page.getValue().equals(Pages.General)));
    public Option<Double> range = addOption(new DoubleOption("Range", 1, 14, 10, v -> page.getValue().equals(Pages.General)));

    @Override
    public String getDetails() {
        return "";
    }

    public enum Pages {
        General,
        Rotate,
        Place,
        Attack,
        Render,
        Other
    }
}
