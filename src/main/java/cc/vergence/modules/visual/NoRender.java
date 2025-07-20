package cc.vergence.modules.visual;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.modules.Module;

public class NoRender extends Module {
    public static NoRender INSTANCE;

    public NoRender() {
        super("NoRender", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Boolean> noBlindness = addOption(new BooleanOption("NoBlindness", true));
    public Option<Boolean> noCorpses = addOption(new BooleanOption("NoCorpses", true));
    public Option<Boolean> noLiquidOverlay = addOption(new BooleanOption("NoLiquidOverlay", true));

    @Override
    public String getDetails() {
        return "";
    }
}
