package cc.vergence.modules.client;

import cc.vergence.features.enums.*;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;

import java.util.EnumSet;

// If the module is in an enabled state, it indicates that **Safe Mode Module** is being used.
public class AntiCheat extends Module {
    public static AntiCheat INSTANCE;
    public AntiCheat() {
        super("AntiCheat", Category.CLIENT);
        INSTANCE = this;
    }

    public Option<Enum<?>> antiCheat = addOption(new EnumOption("AntiCheat", AntiCheats.Legit));
    public Option<EnumSet<TargetTypes>> attackTargets = addOption(new MultipleOption<>("AttackTargets", EnumSet.of(TargetTypes.EnemyPlayers, TargetTypes.Mobs)));
    public Option<Boolean> rotateSync = addOption(new BooleanOption("RotateSync", false));

    // Custom Anti-Cheat Mode
    public Option<Enum<?>> rotateMode = addOption(new EnumOption("RotateMode", RotateModes.Legit, v -> antiCheat.getValue().equals(AntiCheats.Custom)));
    public Option<Double> rotateTime = addOption(new DoubleOption("RotateTime", 0, 1200, 400, v -> antiCheat.getValue().equals(AntiCheats.Custom)).addSpecialValue(0, "INSTANT"));
    public Option<Enum<?>> swingMode = addOption(new EnumOption("SwingMode", SwingModes.Legit, v -> antiCheat.getValue().equals(AntiCheats.Custom)));
    public Option<Enum<?>> PlaceMode = addOption(new EnumOption("PlaceMode", PlaceModes.Legit, v -> antiCheat.getValue().equals(AntiCheats.Custom)));

    @Override
    public String getDetails() {
        return antiCheat.getValue().name();
    }
}
