package cc.vergence.modules.hud;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.modules.Module;

import java.awt.*;

public class InventoryHud extends Module {
    public static InventoryHud INSTANCE;

    public InventoryHud() {
        super("InventoryHud",Category.HUD);
        INSTANCE = this;
    }

    public Option<Boolean> count = addOption(new BooleanOption("Count", true));
    public Option<Color> countColor = addOption(new ColorOption("CountColor", new Color(0, 0, 0), v -> count.getValue()));
    public Option<Boolean> background = addOption(new BooleanOption("Background", true));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(246, 246, 246, 242)));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", true));

    @Override
    public String getDetails() {
        return "";
    }
}
