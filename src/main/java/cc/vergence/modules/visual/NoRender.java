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

    public Option<Boolean> noHurtCamera = addOption(new BooleanOption("NoHurtCamera", true));
    public Option<Boolean> noExplosions = addOption(new BooleanOption("NoExplosions", true));
    public Option<Boolean> noParticles = addOption(new BooleanOption("NoParticle", false));
    public Option<Boolean> noBlindness = addOption(new BooleanOption("NoBlindness", true));
    public Option<Boolean> noCorpses = addOption(new BooleanOption("NoCorpses", true));
    public Option<Boolean> noLiquidOverlay = addOption(new BooleanOption("NoLiquidOverlay", true));
    public Option<Boolean> noBlockOverlay = addOption(new BooleanOption("NoBlockOverlay", true));
    public Option<Boolean> noFireOverlay = addOption(new BooleanOption("NoFireOverlay", true));
    public Option<Boolean> noSnowOverlay = addOption(new BooleanOption("NoSnowOverlay", true));
    public Option<Boolean> noPumpkinOverlay = addOption(new BooleanOption("NoPumpkinOverlay", true));
    public Option<Boolean> noPortalOverlay = addOption(new BooleanOption("NoPortalOverlay", true));
    public Option<Boolean> noVignette = addOption(new BooleanOption("NoVignette", false));
    public Option<Boolean> noFog = addOption(new BooleanOption("NoFog", true));
    public Option<Boolean> noArmor = addOption(new BooleanOption("NoArmor", false));
    public Option<Boolean> noSignText = addOption(new BooleanOption("NoSignText", false));

    @Override
    public String getDetails() {
        return "";
    }
}
