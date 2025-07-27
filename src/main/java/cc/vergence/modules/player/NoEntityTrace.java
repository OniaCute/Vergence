package cc.vergence.modules.player;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.modules.Module;

public class NoEntityTrace extends Module {
    public static NoEntityTrace INSTANCE;

    public NoEntityTrace() {
        super("NoEntityTrace", Category.PLAYER);
        INSTANCE = this;
    }

    public Option<Boolean> onlyPickaxe = addOption(new BooleanOption("OnlyPickaxe", false));

    @Override
    public String getDetails() {
        return "";
    }
}
