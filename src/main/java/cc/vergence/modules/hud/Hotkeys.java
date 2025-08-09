package cc.vergence.modules.hud;

import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.enums.client.MouseButtons;
import cc.vergence.features.managers.ui.HudManager;
import cc.vergence.features.managers.feature.ModuleManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.modules.Module;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import oshi.util.tuples.Pair;

import java.awt.*;
import java.util.ArrayList;

public class Hotkeys extends Module {
    private ArrayList<Module> hotkeyModeles = new ArrayList<>();
    private boolean lastMouseStatus = false;
    private double lastMouseX = -1;
    private double lastMouseY = -1;

    public Hotkeys() {
        super("Hotkeys", Category.HUD);
        setX(0);
        setY(0);
        setWidth(0);
        setHeight(0);
    }

    public Option<Boolean> onlyEnabled = addOption(new BooleanOption("OnlyEnabled", true));
    public Option<Enum<?>> fontSize = addOption(new EnumOption("FontSize", FontSize.SMALL));
    public Option<Double> padding = addOption(new DoubleOption("Padding", -2, 6, 2).setUnit("px"));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", false));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0, 6, 3, v -> rounded.getValue()).setUnit("px"));
    public Option<Color> textTopColor = addOption(new ColorOption("TextTopColor", new Color(249, 146, 255, 245)));
    public Option<Color> textColor = addOption(new ColorOption("TextColor", new Color(24, 24, 24, 245)));
    public Option<Color> backgroundTopColor = addOption(new ColorOption("BackgroundTopColor", new Color(241, 241, 241, 245)));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(241, 241, 241, 245)));
    public Option<Color> textEnabledColor = addOption(new ColorOption("TextEnabledColor", new Color(24, 24, 24, 245)));
    public Option<Color> backgroundEnabledColor = addOption(new ColorOption("BackgroundEnabledColor", new Color(241, 241, 241, 245), v -> rounded.getValue()));

    @Override
    public String getDetails() {
        return String.valueOf(hotkeyModeles.size());
    }

    @Override
    public void onMouseRelease(double mouseX, double mouseY, Screen screen, MouseButtons button) {
        if (screen instanceof HudEditorScreen && button.equals(MouseButtons.LEFT)) {
            this.lastMouseStatus = false;
            HudManager.currentHud = null;
        }
    }

    @Override
    public void onDraw2D() {
        hotkeyModeles.clear();
        FontSize size = ((FontSize) fontSize.getValue());

        for (Module module : ModuleManager.modules) {
            if (module.getBind().getValue() == -1) {
                continue;
            }
            if (onlyEnabled.getValue() && !module.getStatus()) {
                continue;
            }
            hotkeyModeles.add(module);
        }

        double maxWidth = 12 + FontUtil.getWidth(size, getDisplayName());
        double totalHeight = FontUtil.getHeight(size);

        for (Module module : hotkeyModeles) {
            totalHeight += padding.getValue();
            maxWidth = Math.max(maxWidth, (padding.getValue() + FontUtil.getWidth(size, module.getDisplayName()) + FontUtil.getWidth(size, module.getBind().getBindChar()) + 8));
            if (rounded.getValue()) {
                Pair<Double, Double> pos = Render2DUtil.getAlignPositionAsPair(
                        getX(),
                        getY() + totalHeight,
                        getX() + maxWidth,
                        getY() + totalHeight + FontUtil.getHeight(size),
                        FontUtil.getWidth(size, module.getDisplayName()) + 4,
                        FontUtil.getHeight(size),
                        Aligns.LEFT
                );

                Render2DUtil.drawRoundedRect(
                        pos.getA(),
                        pos.getB(),
                        FontUtil.getWidth(size, module.getDisplayName()) + 4,
                        FontUtil.getHeight(size),
                        radius.getValue(),
                        module.getStatus() ? backgroundEnabledColor.getValue() : backgroundColor.getValue()
                );

                FontUtil.drawTextWithAlign(
                        module.getDisplayName(),
                        pos.getA() + 2,
                        pos.getB() + 1,
                        pos.getA() + FontUtil.getWidth(size, module.getDisplayName()),
                        pos.getB() + FontUtil.getHeight(size),
                        module.getStatus() ? textEnabledColor.getValue() : textColor.getValue(),
                        size,
                        Aligns.CENTER
                );

                Pair<Double, Double> pos1 = Render2DUtil.getAlignPositionAsPair(
                        getX(),
                        getY() + totalHeight,
                        getX() + maxWidth,
                        getY() + totalHeight + FontUtil.getHeight(size),
                        FontUtil.getWidth(size, module.getBind().getBindChar()) + 4,
                        FontUtil.getHeight(size),
                        Aligns.RIGHT
                );

                Render2DUtil.drawRoundedRect(
                        pos1.getA(),
                        pos1.getB(),
                        FontUtil.getWidth(size, module.getBind().getBindChar()) + 4,
                        FontUtil.getHeight(size),
                        radius.getValue(),
                        module.getStatus() ? backgroundEnabledColor.getValue() : backgroundColor.getValue()
                );

                FontUtil.drawTextWithAlign(
                        module.getBind().getBindChar(),
                        pos1.getA() + 2,
                        pos1.getB() + 1,
                        pos1.getA() + FontUtil.getWidth(size, module.getBind().getBindChar()) + 2,
                        pos1.getB() + FontUtil.getHeight(size),
                        module.getStatus() ? textEnabledColor.getValue() : textColor.getValue(),
                        size,
                        Aligns.CENTER
                );
            } else {
                Pair<Double, Double> pos = Render2DUtil.getAlignPositionAsPair(
                        getX(),
                        getY() + totalHeight,
                        getX() + maxWidth,
                        getY() + totalHeight + FontUtil.getHeight(size),
                        FontUtil.getWidth(size, module.getDisplayName()) + 4,
                        FontUtil.getHeight(size),
                        Aligns.LEFT
                );

                Render2DUtil.drawRect(
                        pos.getA(),
                        pos.getB(),
                        FontUtil.getWidth(size, module.getDisplayName()) + 4,
                        FontUtil.getHeight(size),
                        module.getStatus() ? backgroundEnabledColor.getValue() : backgroundColor.getValue()
                );

                FontUtil.drawTextWithAlign(
                        module.getDisplayName(),
                        pos.getA() + 2,
                        pos.getB() + 1,
                        pos.getA() + FontUtil.getWidth(size, module.getDisplayName()),
                        pos.getB() + FontUtil.getHeight(size),
                        module.getStatus() ? textEnabledColor.getValue() : textColor.getValue(),
                        size,
                        Aligns.CENTER
                );

                Pair<Double, Double> pos1 = Render2DUtil.getAlignPositionAsPair(
                        getX(),
                        getY() + totalHeight,
                        getX() + maxWidth,
                        getY() + totalHeight + FontUtil.getHeight(size),
                        FontUtil.getWidth(size, module.getBind().getBindChar()) + 4,
                        FontUtil.getHeight(size),
                        Aligns.RIGHT
                );

                Render2DUtil.drawRect(
                        pos1.getA(),
                        pos1.getB(),
                        FontUtil.getWidth(size, module.getBind().getBindChar()) + 4,
                        FontUtil.getHeight(size),
                        module.getStatus() ? backgroundEnabledColor.getValue() : backgroundColor.getValue()
                );

                FontUtil.drawTextWithAlign(
                        module.getBind().getBindChar(),
                        pos1.getA() + 2,
                        pos1.getB() + 1,
                        pos1.getA() + FontUtil.getWidth(size, module.getBind().getBindChar()) + 2,
                        pos1.getB() + FontUtil.getHeight(size),
                        module.getStatus() ? textEnabledColor.getValue() : textColor.getValue(),
                        size,
                        Aligns.CENTER
                );
            }

            totalHeight += FontUtil.getHeight(size);
        }

        if (rounded.getValue()) {
            Render2DUtil.drawRoundedRect(
                    getX(),
                    getY(),
                    maxWidth,
                    FontUtil.getHeight(size),
                    radius.getValue(),
                    backgroundTopColor.getValue()
            );
            FontUtil.drawTextWithAlign(
                    getDisplayName(),
                    getX(),
                    getY() + 1,
                    getX() + maxWidth,
                    getY() + FontUtil.getHeight(size),
                    textTopColor.getValue(),
                    size,
                    Aligns.CENTER
            );
        } else {
            Render2DUtil.drawRect(
                    getX(),
                    getY(),
                    maxWidth,
                    FontUtil.getHeight(size),
                    backgroundTopColor.getValue()
            );
            FontUtil.drawTextWithAlign(
                    getDisplayName(),
                    getX(),
                    getY() + 1,
                    getX() + maxWidth,
                    getY() + FontUtil.getHeight(size),
                    textTopColor.getValue(),
                    size,
                    Aligns.CENTER
            );
        }

        setWidth(maxWidth);
        setHeight(totalHeight);

        if (HudManager.CLICKED_LEFT) {
            if (HudManager.MOUSE_X > getX() && HudManager.MOUSE_X < getX() + getWidth() &&
                    HudManager.MOUSE_Y > getY() && HudManager.MOUSE_Y < getY() + getHeight() && HudManager.currentHud == null || HudManager.currentHud == this) {
                HudManager.currentHud = this;
                if (!lastMouseStatus) {
                    lastMouseStatus = true;
                } else {
                    setX(getX() + HudManager.MOUSE_X - lastMouseX);
                    setY(getY() + HudManager.MOUSE_Y - lastMouseY);
                }
                lastMouseX = HudManager.MOUSE_X;
                lastMouseY = HudManager.MOUSE_Y;
            }
        }
    }
}
