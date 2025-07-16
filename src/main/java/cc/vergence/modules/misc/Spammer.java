package cc.vergence.modules.misc;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.modules.Module;

public class Spammer extends Module {
    public static Spammer INSTANCE;
    public Spammer() {
        super("Spammer", Category.MISC);
        INSTANCE = this;
    }

    public Option<Double> cooldown = addOption(new DoubleOption("Cooldown", 0, 1400, 700));
    public Option<Enum<?>> listOrder = addOption(new EnumOption("ListOrder", ListOrder.Random));
    public Option<String> fileName = addOption(new TextOption("FileName", "default.txt"));

    @Override
    public String getDetails() {
        return String.valueOf(cooldown.getValue().intValue());
    }

    public enum ListOrder {
        Order,
        Random
    }
}
