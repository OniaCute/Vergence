package cc.vergence.modules.hud;

import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.managers.ModuleManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.modules.Module;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Hotkeys extends Module {
    private HashMap<BindOption, Module> hotkeys = new HashMap<>();

    public Hotkeys() {
        super("Hotkeys", Category.HUD);
    }

    public Option<Boolean> onlyEnabled = addOption(new BooleanOption("OnlyEnabled", true));
    public Option<Enum<?>> aligns = addOption(new EnumOption("Aligns", Aligns.CENTER));
    public Option<Enum<?>> fontSize = addOption(new EnumOption("FontSize", FontSize.SMALL));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", false));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0, 6, 3, v -> rounded.getValue()).setUnit("px"));
    public Option<Color> textColor = addOption(new ColorOption("TextColor", new Color(24, 24, 24, 245)));
    public Option<Color> backgroundTopColor = addOption(new ColorOption("BackgroundTopColor", new Color(241, 241, 241, 245)));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(241, 241, 241, 245)));
    public Option<Color> textEnabledColor = addOption(new ColorOption("TextEnabledColor", new Color(24, 24, 24, 245)));
    public Option<Color> backgroundEnabledColor = addOption(new ColorOption("BackgroundEnabledColor", new Color(241, 241, 241, 245), v -> rounded.getValue()));

    @Override
    public String getDetails() {
        return String.valueOf(hotkeys.size());
    }

    @Override
    public void onDraw2D(DrawContext context, float tickDelta) {
        FontSize size = ((FontSize) fontSize.getValue());
        Aligns align = ((Aligns) aligns.getValue());

        for (Module module : ModuleManager.modules) {
            hotkeys.put(module.getBind(), module);
        }

        if (rounded.getValue()) {
            Render2DUtil.drawRoundedRect(
                    context.getMatrices(),
                    getX(),
                    getY(),
                    12 + FontUtil.getWidth(size, getDisplayName()),
                    FontUtil.getHeight(size),
                    radius.getValue(),
                    backgroundTopColor.getValue()
            );
            FontUtil.drawTextWithAlign(
                    context,
                    getDisplayName(),
                    getX(),
                    getY(),
                    getX() + 12 + FontUtil.getWidth(size, getDisplayName()),
                    getY() + FontUtil.getHeight(size),
                    Aligns.CENTER,
                    textColor.getValue(),
                    size
            );
        } else {
            Render2DUtil.drawRect(
                    context,
                    getX(),
                    getY(),
                    12 + FontUtil.getWidth(size, getDisplayName()),
                    FontUtil.getHeight(size),
                    backgroundTopColor.getValue()
            );
            FontUtil.drawTextWithAlign(
                    context,
                    getDisplayName(),
                    getX(),
                    getY(),
                    getX() + 12 + FontUtil.getWidth(size, getDisplayName()),
                    getY() + FontUtil.getHeight(size),
                    Aligns.CENTER,
                    textColor.getValue(),
                    size
            );
        }

        for (BindOption bindOption : hotkeys.keySet()) {
            Module module = hotkeys.get(bindOption);
        }
    }
}
