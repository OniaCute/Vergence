package cc.vergence.modules.combat;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;

public class Reach extends Module {
    public static Reach INSTANCE;
    public Reach() {
        super("Reach", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Double> range = addOption(new DoubleOption("Range", 0, 9, 5));

    @Override
    public String getDetails() {
        return String.valueOf(range.getValue().intValue());
    }
}
