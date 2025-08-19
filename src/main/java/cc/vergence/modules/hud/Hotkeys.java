package cc.vergence.modules.hud;

import cc.vergence.features.enums.client.MouseButtons;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.managers.feature.ModuleManager;
import cc.vergence.features.managers.ui.HudManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.modules.Module;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.NewRender2DUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import oshi.util.tuples.Pair;

import java.awt.*;
import java.util.ArrayList;

public class Hotkeys extends Module {
    public static Hotkeys INSTANCE;
    private boolean lastMouseStatus = false;
    private double lastMouseX = -1;
    private double lastMouseY = -1;

    public Hotkeys() {
        super("Hotkeys", Category.HUD);
        INSTANCE = this;
        setX(0);
        setY(0);
        setWidth(0);
        setHeight(0);
    }

    public Option<Enum<?>> mode = addOption(new EnumOption("Mode", Modes.Normal));
    public Option<String> title = addOption(new TextOption("Title", "Hotkeys"));
    public Option<Enum<?>> titleAlign = addOption(new EnumOption("TitleAlign", Aligns.Center));
    public Option<Color> titleColor = addOption(new ColorOption("TitleColor", new Color(241, 137, 255)));
    public Option<Boolean> icon = addOption(new BooleanOption("Icon", true));
    public Option<Color> moduleColor = addOption(new ColorOption("ModuleColor", new Color(0, 0, 0)));
    public Option<Color> keybindColor = addOption(new ColorOption("KeybindColor", new Color(0, 0, 0)));
    public Option<Color> moduleEnabledColor = addOption(new ColorOption("ModuleEnabledColor", new Color(0, 0, 0)));
    public Option<Color> keybindEnabledColor = addOption(new ColorOption("KeybindEnabledColor", new Color(0, 0, 0)));
    public Option<Color> splitLineColor = addOption(new ColorOption("SplitLineColor", new Color(246, 246, 246, 247)));
    public Option<Boolean> enabledOnly = addOption(new BooleanOption("EnabledOnly", true));
    public Option<Boolean> background = addOption(new BooleanOption("Background", true));
    public Option<Boolean> blur = addOption(new BooleanOption("Blur", true, v -> background.getValue()));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(241, 241, 241), v -> background.getValue()));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", true, v -> background.getValue()));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0, 6, 4, v -> background.getValue()));
    public Option<Double> titleOffset = addOption(new DoubleOption("TitleOffset", -5, 5, 0));
    public Option<Double> iconOffset = addOption(new DoubleOption("IconOffset", -5, 5, 0));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onMouseRelease(double mouseX, double mouseY, Screen screen, MouseButtons button) {
        if (screen instanceof HudEditorScreen && button.equals(MouseButtons.LEFT)) {
            this.lastMouseStatus = false;
            HudManager.currentHud = null;
        }
    }

    @Override
    public void onDrawSkia(DrawContext context, float tickDelta) {
        if (isNull() || !blur.getValue() || !background.getValue()) {
            return ;
        }

        if (rounded.getValue()) {
            NewRender2DUtil.drawRoundedBlur(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight(),
                    radius.getValue()
            );
        } else {
            NewRender2DUtil.drawBlur(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight()
            );
        }
    }

    @Override
    public void onDraw2D(DrawContext context, float tickDelta) {
        if (isNull()) {
            return ;
        }
        ArrayList<Module> bindModules = new ArrayList<>();
        String iconText = icon.getValue() ? "⌨ " : "";

        for (Module module : ModuleManager.modules) {
            if (module.getBind().getValue() != -1 && (!enabledOnly.getValue() || module.getStatus())) {
                bindModules.add(module);
            }
        }

        double maxWidth = FontUtil.getWidth(getTitleFontSize(), title.getValue() + iconText) + 10;
        for (Module module : bindModules) {
            maxWidth = Math.max(FontUtil.getWidth(getFontSize(), module.getDisplayName() + " " + module.getBind().getBindChar()) + 5 + FontUtil.getWidth(getFontSize(), title.getValue()), maxWidth);
        }
        setWidth(maxWidth);
        setHeight(
                (!bindModules.isEmpty() ? 2 * 2 : 0) + FontUtil.getHeight(getTitleFontSize()) + // title
                        bindModules.size() * (FontUtil.getHeight(getFontSize())) + 2
        );

        if (background.getValue()) {
            if (rounded.getValue()) {
                Render2DUtil.drawRoundedRect(
                        context.getMatrices(),
                        getX(),
                        getY(),
                        getWidth(),
                        getHeight(),
                        radius.getValue(),
                        backgroundColor.getValue()
                );
            } else {
                Render2DUtil.drawRect(
                        context,
                        getX(),
                        getY(),
                        getWidth(),
                        getHeight(),
                        backgroundColor.getValue()
                );
            }
        }

        double[] pos = Render2DUtil.getAlignPosition(
                getX() + (titleAlign.getValue().equals(Aligns.Left) ? 2 : 0),
                getY(),
                getX() + getWidth() + (titleAlign.getValue().equals(Aligns.Right) ? -2 : 0),
                getY() + 2 * 2 + FontUtil.getHeight(getTitleFontSize()) + 2 + titleOffset.getValue(),
                FontUtil.getWidth(getTitleFontSize(), iconText + title.getValue()),
                FontUtil.getHeight(getTitleFontSize()),
                translateAlign((Aligns) titleAlign.getValue())
        );

        FontUtil.drawIcon(
                context,
                iconText,
                pos[0],
                pos[1] + 2 + iconOffset.getValue(),
                titleColor.getValue(),
                getTitleFontSize()
        );

        FontUtil.drawText(
                context,
                title.getValue(),
                pos[0] + FontUtil.getWidth(getTitleFontSize(), iconText),
                pos[1] + titleOffset.getValue(),
                titleColor.getValue(),
                getTitleFontSize()
        );

        if (!bindModules.isEmpty()) {
            Render2DUtil.drawRect(
                    context,
                    getX(),
                    getY() + 2 + FontUtil.getHeight(getTitleFontSize()),
                    getWidth(),
                    0.6,
                    splitLineColor.getValue()
            );
        }

        int counter = 0;
        for (Module module : bindModules) {
            FontUtil.drawTextWithAlign(
                    context,
                    module.getDisplayName(),
                    getX() + 2,
                    getY() + 2 * 2 + FontUtil.getHeight(getTitleFontSize()) + (counter * FontUtil.getHeight(getFontSize()) + 2),
                    getX() + getWidth(),
                    getY() + getHeight(),
                    cc.vergence.features.enums.other.Aligns.LEFT_TOP,
                    module.getStatus() ? moduleEnabledColor.getValue() : moduleColor.getValue(),
                    getFontSize()
            );
            FontUtil.drawTextWithAlign(
                    context,
                    module.getBind().getBindChar(),
                    getX() + 2,
                    getY() + 2 * 2 + FontUtil.getHeight(getTitleFontSize()) + (counter * FontUtil.getHeight(getFontSize()) + 2),
                    getX() + getWidth() - 2,
                    getY() + getHeight(),
                    cc.vergence.features.enums.other.Aligns.RIGHT_TOP,
                    module.getStatus() ? keybindEnabledColor.getValue() : keybindColor.getValue(),
                    getFontSize()
            );
            counter ++;
        }

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

    private cc.vergence.features.enums.other.Aligns translateAlign(Aligns aligns) {
        switch (aligns) {
            case Left -> {
                return cc.vergence.features.enums.other.Aligns.LEFT;
            }
            case Center -> {
                return cc.vergence.features.enums.other.Aligns.CENTER;
            }
            default -> {
                return cc.vergence.features.enums.other.Aligns.RIGHT;
            }
        }
    }

    private FontSize getTitleFontSize() {
        return mode.getValue().equals(Modes.Normal) ? FontSize.MEDIUM : FontSize.SMALL;
    }

    private FontSize getFontSize() {
        return mode.getValue().equals(Modes.Normal) ? FontSize.SMALL : FontSize.SMALLEST;
    }

    private enum Aligns {
        Left,
        Center,
        Right
    }

    private enum Modes {
        Normal,
        Mini
    }
}
