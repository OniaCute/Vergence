package cc.vergence.modules.combat;

import cc.vergence.features.enums.player.RotateModes;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.other.FastTimerUtil;
import java.util.*;

public class Surround extends Module {
    public static Surround INSTANCE;
    private FastTimerUtil timer = new FastTimerUtil();

    public Surround() {
        super("Surround", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Double> placeRange = addOption(new DoubleOption("PlaceRange", 1, 10, 4));
    public Option<Double> placeDelay = addOption(new DoubleOption("PlaceDelay", 0, 1000, 50).setUnit("ms"));
    public Option<Double> multiPlace = addOption(new DoubleOption("MultiPlace", 1, 8, 1));
    public Option<Boolean> doRotate = addOption(new BooleanOption("Rotate", true));
    public Option<Enum<?>> rotateMode = addOption(new EnumOption("RotateMode", RotateModes.Client, v -> doRotate.getValue()));
    public Option<Boolean> whileEating = addOption(new BooleanOption("WhileEating", true));
    public Option<Boolean> antiCrystal = addOption(new BooleanOption("AntiCrystal", true));
    public Option<Boolean> center = addOption(new BooleanOption("ToCenter", true));
    public Option<Boolean> expansion = addOption(new BooleanOption("Expansion", true));
    public Option<Boolean> doSupport = addOption(new BooleanOption("Support", true));
    public Option<Boolean> autoDisable = addOption(new BooleanOption("AutoDisable", false));
    public Option<EnumSet<DisableItems>> disableItems = addOption(new MultipleOption<DisableItems>("DisableItems", EnumSet.of(DisableItems.Jump, DisableItems.Leave, DisableItems.Death), v -> autoDisable.getValue()));

    @Override
    public String getDetails() {
        return "";
    }

    public enum DisableItems {
        Jump,
        Leave,
        Death,
        Move
    }
}
