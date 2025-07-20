package cc.vergence.modules.client;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.modules.Module;

import java.awt.*;

public class MainMenu extends Module {
    public static MainMenu INSTANCE;
    public MainMenu() {
        super("MainMenu", Category.CLIENT);
        INSTANCE = this;
    }

    public Option<String> title = addOption(new TextOption("Title", "Vergence Client"));
    public Option<Color> titleColor = addOption(new ColorOption("TitleColor", new Color(255, 142, 247)));
    public Option<Color> textColor = addOption(new ColorOption("TextColor", new Color(35, 35, 35)));
    public Option<Boolean> background = addOption(new BooleanOption("Background", true));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(241, 241, 241, 178), v -> background.getValue()));
    public Option<Color> buttonTextColor = addOption(new ColorOption("ButtonTextColor", new Color(31, 31, 31)));
    public Option<Color> buttonHoveredTextColor = addOption(new ColorOption("ButtonHoveredTextColor", new Color(204, 204, 204)));
    public Option<Color> buttonBackgroundColor = addOption(new ColorOption("ButtonBackgroundColor", new Color(225, 225, 225)));
    public Option<Color> buttonHoveredBackgroundColor = addOption(new ColorOption("ButtonHoveredBackgroundColor", new Color(255, 215, 253)));
    @Override
    public String getDetails() {
        return "";
    }
}
