package cc.vergence.modules.hud;

import cc.vergence.features.enums.units.SpeedUnit;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.modules.Module;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class Speed extends Module {
    public static Speed INSTANCE;

    public Speed() {
        super("Speed", Category.HUD);
        INSTANCE = this;
    }

    public Option<Enum<?>> unit = addOption(new EnumOption("Unit", SpeedUnit.Kilometers));
    public Option<String> text = addOption(new TextOption("Text", "{speed}"));
    public Option<Color> textColor = addOption(new ColorOption("TextColor", new Color(0, 0, 0)));
    public Option<Boolean> background = addOption(new BooleanOption("Background", false));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(255, 255, 255, 236), v -> background.getValue()));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", false, v -> background.getValue()));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0, 4, 3, v -> background.getValue() && rounded.getValue()));
    public Option<Boolean> outline = addOption(new BooleanOption("Outline", false));
    public Option<Color> outlineColor = addOption(new ColorOption("OutlineColor", new Color(255, 255, 255, 26), v -> outline.getValue()));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDraw2D(DrawContext context, float tickDelta) {

    }
}
