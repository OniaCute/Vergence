package cc.vergence.modules.hud;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class PotionHud extends Module {
    public static PotionHud INSTANCE;

    public PotionHud() {
        super("PotionHud", Category.HUD);
        INSTANCE = this;
        setX(0);
        setY(0);
        setWidth(0);
        setHeight(0);
    }

    public Option<Color> titleColor = addOption(new ColorOption("TitleColor", new Color(241, 137, 255)));
    public Option<Color> potionColor = addOption(new ColorOption("PotionColor", new Color(0, 0, 0)));
    public Option<Color> potionTimeColor = addOption(new ColorOption("PotionTimeColor", new Color(38, 38, 38)));
    public Option<Boolean> icon = addOption(new BooleanOption("Icon", true));
    public Option<Boolean> background = addOption(new BooleanOption("Background", true));
    public Option<Boolean> blur = addOption(new BooleanOption("Blur", true, v -> background.getValue()));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(0, 0, 0), v -> background.getValue()));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", true, v -> background.getValue()));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0, 6, 4, v -> background.getValue()));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDrawSkia(DrawContext context, float tickDelta) {

    }

    @Override
    public void onDraw2D(DrawContext context, float tickDelta) {

    }
}
