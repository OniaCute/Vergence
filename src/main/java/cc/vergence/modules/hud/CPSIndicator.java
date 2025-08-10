package cc.vergence.modules.hud;

import cc.vergence.features.enums.client.MouseButtons;
import cc.vergence.features.enums.font.FontSize;
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

import java.awt.*;

public class CPSIndicator extends Module {
    public static CPSIndicator INSTANCE;
    private boolean lastMouseStatus = false;
    private double lastMouseX = -1;
    private double lastMouseY = -1;

    public CPSIndicator() {
        super("CPSIndicator", Category.HUD);
        INSTANCE = this;
        setX(0);
        setY(0);
        setWidth(0);
        setHeight(0);
    }

    public Option<Enum<?>> fontSize = addOption(new EnumOption("FontSize", FontSize.SMALL));
    public Option<String> text = addOption(new TextOption("Text", "CPS {cps} | {right_cps}"));
    public Option<Color> textColor = addOption(new ColorOption("TextColor", new Color(0, 0, 0)));
    public Option<Boolean> blur = addOption(new BooleanOption("Blur", false));
    public Option<Boolean> icon = addOption(new BooleanOption("Icon", true));
    public Option<Color> iconColor = addOption(new ColorOption("IconColor", new Color(0, 0, 0), v -> icon.getValue()));
    public Option<Boolean> background = addOption(new BooleanOption("Background", false));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(255, 255, 255, 236), v -> background.getValue()));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", false, v -> background.getValue()));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0, 6, 3, v -> background.getValue() && rounded.getValue()));
    public Option<Boolean> outline = addOption(new BooleanOption("Outline", false));
    public Option<Double> outlineWidth = addOption(new DoubleOption("OutlineWidth", 0, 3, 1, v -> background.getValue() && outline.getValue()));
    public Option<Color> outlineColor = addOption(new ColorOption("OutlineColor", new Color(255, 255, 255, 26), v -> outline.getValue()));
    public Option<Double> offset = addOption(new DoubleOption("Offset", -4, 5, 0));
    public Option<Double> iconOffset = addOption(new DoubleOption("IconOffset", -4, 5, 0));

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
        if (!blur.getValue()) {
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
        FontSize size = (FontSize) fontSize.getValue();
        String iconText = icon.getValue() ? "\uF8CC " : "";

        setHeight(FontUtil.getHeight(size) + 2 * 2); // 2 padding
        setWidth(FontUtil.getWidth(size, iconText) + FontUtil.getWidth(size, text.getValue()) + 3 * 2); // 3 padding

        if (background.getValue()) {
            if (rounded.getValue()) {
                if (outline.getValue()) {
                    Render2DUtil.drawRoundedRectWithOutline(
                            context.getMatrices(),
                            getX(),
                            getY(),
                            getWidth(),
                            getHeight(),
                            radius.getValue(),
                            outlineWidth.getValue(),
                            backgroundColor.getValue(),
                            outlineColor.getValue()
                    );
                } else {
                    Render2DUtil.drawRoundedRect(
                            context.getMatrices(),
                            getX(),
                            getY(),
                            getWidth(),
                            getHeight(),
                            radius.getValue(),
                            backgroundColor.getValue()
                    );
                }
            } else {
                if (outline.getValue()) {
                    Render2DUtil.drawRectWithOutline(
                            context,
                            getX(),
                            getY(),
                            getWidth(),
                            getHeight(),
                            outlineWidth.getValue(),
                            backgroundColor.getValue(),
                            outlineColor.getValue()
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
        }

        FontUtil.drawIcon(
                context,
                iconText,
                getX() + 3 + 1,
                getY() + 7 + iconOffset.getValue(),
                iconColor.getValue(),
                size
        );

        FontUtil.drawText(
                context,
                text.getValue(),
                getX() + 3 + FontUtil.getWidth(size, iconText),
                getY() + 4 + offset.getValue(),
                textColor.getValue(),
                size
        );

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
