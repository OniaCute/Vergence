package cc.vergence.modules.hud;

import cc.vergence.features.enums.client.MouseButtons;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.managers.ui.HudManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.modules.Module;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.NewRender2DUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;

public class KeyStrokes extends Module {
    public static KeyStrokes INSTANCE;
    private boolean lastMouseStatus = false;
    private double lastMouseX = -1;
    private double lastMouseY = -1;
    private boolean key_w, key_a, key_s, key_d, key_space;

    public KeyStrokes() {
        super("KeyStrokes", Category.HUD);
        INSTANCE = this;
        setX(0);
        setY(0);
        setWidth(0);
        setHeight(0);
    }

    public Option<Enum<?>> fontSize = addOption(new EnumOption("FontSize", FontSize.SMALL));
    public Option<Color> textColor = addOption(new ColorOption("TextColor", new Color(0, 0, 0)));
    public Option<Color> textEnabledColor = addOption(new ColorOption("TextEnabledColor", new Color(0, 0, 0)));
    public Option<Double> offset = addOption(new DoubleOption("Offset", -8, 8, 0));
    public Option<Double> margin = addOption(new DoubleOption("Margin", 0, 15, 2));
    public Option<Double> padding = addOption(new DoubleOption("Padding", 0, 6, 2));
    public Option<Boolean> withSpace = addOption(new BooleanOption("Space", true));
    public Option<Double> spaceMargin = addOption(new DoubleOption("SpaceMargin", 0, 6, 2, v -> withSpace.getValue()));
    public Option<Boolean> background = addOption(new BooleanOption("Background", true));
    public Option<Boolean> blur = addOption(new BooleanOption("Blur", true, v -> background.getValue()));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(255, 255, 255, 240), v -> background.getValue()));
    public Option<Color> backgroundEnabledColor = addOption(new ColorOption("BackgroundEnabledColor", new Color(255, 255, 255, 240), v -> background.getValue()));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", true, v -> background.getValue()));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0, 7, 3, v -> background.getValue() && rounded.getValue()));
    public Option<Double> spaceRadius = addOption(new DoubleOption("SpaceRadius", 0, 3, 1, v -> background.getValue() && rounded.getValue() && withSpace.getValue()));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onKeyboardActive(int key, int action) {
        if (key == GLFW.GLFW_KEY_W) {
            key_w = action != 0;
        }
        else if (key == GLFW.GLFW_KEY_A) {
            key_a = action != 0;
        }
        else if (key == GLFW.GLFW_KEY_S) {
            key_s = action != 0;
        }
        else if (key == GLFW.GLFW_KEY_D) {
            key_d = action != 0;
        }
        else if (key == GLFW.GLFW_KEY_SPACE) {
            key_space = action != 0;
        }
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
        if (!blur.getValue() || !background.getValue()) {
            return;
        }

        double blockSize = 10 + margin.getValue() * 2;

        if (rounded.getValue()) {
            NewRender2DUtil.drawRoundedBlur( // W
                    getX() + blockSize + padding.getValue(),
                    getY(),
                    blockSize,
                    blockSize,
                    radius.getValue()
            );
            NewRender2DUtil.drawRoundedBlur( // A
                    getX(),
                    getY() + blockSize + padding.getValue(),
                    blockSize,
                    blockSize,
                    radius.getValue()
            );
            NewRender2DUtil.drawRoundedBlur( // S
                    getX() + blockSize + padding.getValue(),
                    getY() + blockSize + padding.getValue(),
                    blockSize,
                    blockSize,
                    radius.getValue()
            );
            NewRender2DUtil.drawRoundedBlur( // D
                    getX() + (blockSize + padding.getValue()) * 2,
                    getY() + blockSize + padding.getValue(),
                    blockSize,
                    blockSize,
                    radius.getValue()
            );
            if (withSpace.getValue()) {
                NewRender2DUtil.drawRoundedBlur( // space
                        getX(),
                        getY() + (blockSize + padding.getValue()) * 2,
                        getWidth(),
                        spaceMargin.getValue() * 2 + 2,
                        spaceRadius.getValue()
                );
            }
        } else {
            NewRender2DUtil.drawBlur( // W
                    getX() + blockSize + padding.getValue(),
                    getY(),
                    blockSize,
                    blockSize
            );
            NewRender2DUtil.drawBlur( // A
                    getX(),
                    getY() + blockSize + padding.getValue(),
                    blockSize,
                    blockSize
            );
            NewRender2DUtil.drawBlur( // S
                    getX() + blockSize + padding.getValue(),
                    getY() + blockSize + padding.getValue(),
                    blockSize,
                    blockSize
            );
            NewRender2DUtil.drawBlur( // D
                    getX() + (blockSize + padding.getValue()) * 2,
                    getY() + blockSize + padding.getValue(),
                    blockSize,
                    blockSize
            );
            if (withSpace.getValue()) {
                NewRender2DUtil.drawBlur( // space
                        getX(),
                        getY() + (blockSize + padding.getValue()) * 2,
                        getWidth(),
                        spaceMargin.getValue() * 2 + 2
                );
            }
        }
    }

    @Override
    public void onDraw2D(DrawContext context, float tickDelta) {
        FontSize size = (FontSize) fontSize.getValue();
        double blockSize = 10 + margin.getValue() * 2;

        setWidth(blockSize * 3 + padding.getValue() * 2);
        setHeight(blockSize * 2 + padding.getValue() + (withSpace.getValue() ? spaceMargin.getValue() * 2 + 2 + padding.getValue() : 0));

        if (rounded.getValue()) {
            Render2DUtil.drawRoundedRect( // W
                    context.getMatrices(),
                    getX() + blockSize + padding.getValue(),
                    getY(),
                    blockSize,
                    blockSize,
                    radius.getValue(),
                    key_w ? backgroundEnabledColor.getValue() : backgroundColor.getValue()
            );
            Render2DUtil.drawRoundedRect( // A
                    context.getMatrices(),
                    getX(),
                    getY() + blockSize + padding.getValue(),
                    blockSize,
                    blockSize,
                    radius.getValue(),
                    key_a ? backgroundEnabledColor.getValue() : backgroundColor.getValue()
            );
            Render2DUtil.drawRoundedRect( // S
                    context.getMatrices(),
                    getX() + blockSize + padding.getValue(),
                    getY() + blockSize + padding.getValue(),
                    blockSize,
                    blockSize,
                    radius.getValue(),
                    key_s ? backgroundEnabledColor.getValue() : backgroundColor.getValue()
            );
            Render2DUtil.drawRoundedRect( // D
                    context.getMatrices(),
                    getX() + (blockSize + padding.getValue()) * 2,
                    getY() + blockSize + padding.getValue(),
                    blockSize,
                    blockSize,
                    radius.getValue(),
                    key_d ? backgroundEnabledColor.getValue() : backgroundColor.getValue()
            );
            if (withSpace.getValue()) {
                Render2DUtil.drawRoundedRect( // space
                        context.getMatrices(),
                        getX(),
                        getY() + (blockSize + padding.getValue()) * 2,
                        getWidth(),
                        spaceMargin.getValue() * 2 + 2,
                        spaceRadius.getValue(),
                        key_space ? backgroundEnabledColor.getValue() : backgroundColor.getValue()
                );
            }
        } else {
            Render2DUtil.drawRect( // W
                    context,
                    getX() + blockSize + padding.getValue(),
                    getY(),
                    blockSize,
                    blockSize,
                    key_w ? backgroundEnabledColor.getValue() : backgroundColor.getValue()
            );
            Render2DUtil.drawRect( // A
                    context,
                    getX(),
                    getY() + blockSize + padding.getValue(),
                    blockSize,
                    blockSize,
                    key_a ? backgroundEnabledColor.getValue() : backgroundColor.getValue()
            );
            Render2DUtil.drawRect( // S
                    context,
                    getX() + blockSize + padding.getValue(),
                    getY() + blockSize + padding.getValue(),
                    blockSize,
                    blockSize,
                    key_s ? backgroundEnabledColor.getValue() : backgroundColor.getValue()
            );
            Render2DUtil.drawRect( // D
                    context,
                    getX() + (blockSize + padding.getValue()) * 2,
                    getY() + blockSize + padding.getValue(),
                    blockSize,
                    blockSize,
                    key_d ? backgroundEnabledColor.getValue() : backgroundColor.getValue()
            );
            if (withSpace.getValue()) {
                Render2DUtil.drawRect( // space
                        context,
                        getX(),
                        getY() + (blockSize + padding.getValue()) * 2,
                        getWidth(),
                        spaceMargin.getValue() * 2 + 2,
                        key_space ? backgroundEnabledColor.getValue() : backgroundColor.getValue()
                );
            }
        }

        if (withSpace.getValue()) {
            Render2DUtil.drawRoundedRectWithAlign( // space line
                    context.getMatrices(),
                    getX(),
                    getY() + (blockSize + padding.getValue()) * 2,
                    getX() + getWidth(),
                    getY() + getHeight(),
                    (getWidth() / 3) * 2,
                    2,
                    0,
                    key_space ? textEnabledColor.getValue() : textColor.getValue(),
                    Aligns.CENTER
            );
        }

        FontUtil.drawTextWithAlign( // W
                context,
                "W",
                getX() + blockSize + padding.getValue(),
                getY() + offset.getValue(),
                getX() + blockSize + padding.getValue() + blockSize,
                getY() + offset.getValue() + blockSize,
                Aligns.CENTER,
                key_w ? textEnabledColor.getValue() : textColor.getValue(),
                size
        );

        FontUtil.drawTextWithAlign( // A
                context,
                "A",
                getX(),
                getY() + blockSize + padding.getValue() + offset.getValue(),
                getX() + blockSize,
                getY() + blockSize + padding.getValue() + offset.getValue() + blockSize,
                Aligns.CENTER,
                key_a ? textEnabledColor.getValue() : textColor.getValue(),
                size
        );

        FontUtil.drawTextWithAlign( // S
                context,
                "S",
                getX() + blockSize + padding.getValue(),
                getY() + blockSize + padding.getValue() + offset.getValue(),
                getX() + blockSize + padding.getValue() + blockSize,
                getY() + blockSize + padding.getValue() + offset.getValue() + blockSize,
                Aligns.CENTER,
                key_s ? textEnabledColor.getValue() : textColor.getValue(),
                size
        );

        FontUtil.drawTextWithAlign( // D
                context,
                "D",
                getX() + (blockSize + padding.getValue()) * 2,
                getY() + blockSize + padding.getValue() + offset.getValue(),
                getX() + (blockSize + padding.getValue()) * 2 + blockSize,
                getY() + blockSize + padding.getValue() + offset.getValue() + blockSize,
                Aligns.CENTER,
                key_d ? textEnabledColor.getValue() : textColor.getValue(),
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
