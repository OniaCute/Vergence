package cc.vergence.modules.visual;

import cc.vergence.features.enums.player.TargetTypes;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;

import java.awt.*;
import java.util.EnumSet;

public class HitboxESP extends Module {
    public static HitboxESP INSTANCE;

    public HitboxESP() {
        super("HitboxESP", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<EnumSet<TargetTypes>> targets = addOption(new MultipleOption<TargetTypes>("Targets", EnumSet.of(TargetTypes.EnemyPlayers, TargetTypes.Invisible)));
    public Option<Color> fillColor = addOption(new ColorOption("FillColor", new Color(255, 255, 255)));

    @Override
    public String getDetails() {
        return "";
    }
}
