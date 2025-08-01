package cc.vergence.modules.combat;

import cc.vergence.features.enums.player.RotateModes;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import cc.vergence.util.other.FastTimerUtil;

public class Defender extends Module {
    public static Defender INSTANCE;
    private final FastTimerUtil timer = new FastTimerUtil();

    public Defender() {
        super("Defender", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Double> placeDelay = addOption(new DoubleOption("PlaceDelay", 0, 500, 50));
    public Option<Double> multiPlace = addOption(new DoubleOption("MultiPlace", 1, 8, 1));
    public Option<Boolean> doRotate = addOption(new BooleanOption("Rotate", true));
    public Option<Enum<?>> rotateMode = addOption(new EnumOption("RotateMode", RotateModes.Server, v -> doRotate.getValue()));

    @Override
    public String getDetails() {
        return "";
    }
}
