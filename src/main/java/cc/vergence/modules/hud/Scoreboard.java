package cc.vergence.modules.hud;

import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.enums.MouseButtons;
import cc.vergence.features.managers.ui.HudManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.modules.Module;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import java.awt.*;

public class Scoreboard extends Module {
    public static Scoreboard INSTANCE;
    private boolean lastMouseStatus = false;
    private double lastMouseX = -1;
    private double lastMouseY = -1;

    public Scoreboard() {
        super("Scoreboard", Category.HUD);
        INSTANCE = this;
        setX(0);
        setY(0);
        setWidth(0);
        setHeight(0);
    }

    public Option<Boolean> hide = addOption(new BooleanOption("Hide", true));
    public Option<Double> padding = addOption(new DoubleOption("Padding", 0, 6, 2));
    public Option<Color> titleColor = addOption(new ColorOption("TitleColor", new Color(255, 152, 219, 245)));
    public Option<Color> textColor = addOption(new ColorOption("TextColor", new Color(21, 21, 21, 245)));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", true));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0, 6, 3, v -> rounded.getValue()));
    public Option<Boolean> titleBackground = addOption(new BooleanOption("TitleBackground", true));
    public Option<Color> titleBackgroundColor = addOption(new ColorOption("TitleBackgroundColor", new Color(243, 243, 243, 245), v -> titleBackground.getValue()));
    public Option<Boolean> background = addOption(new BooleanOption("Background", true));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(255, 255, 255, 245), v -> background.getValue()));

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
    public void onDraw2D(DrawContext context, float tickDelta) {
        if (mc.world == null || mc.player == null || mc.getNetworkHandler() == null || hide.getValue()) {
            return;
        }
        net.minecraft.scoreboard.Scoreboard scoreboard = mc.getNetworkHandler().getScoreboard();

        double x = padding.getValue();
        double y = padding.getValue();
        double width = FontUtil.getWidth(FontSize.SMALL, this.getDisplayName()) + padding.getValue() * 2;
        double height = FontUtil.getHeight(FontSize.SMALL) + padding.getValue() * 2;

        // background
        if (rounded.getValue()) {
            Render2DUtil.drawRoundedRect(
                    context.getMatrices(),
                    getX(),
                    getY(),
                    width,
                    height,
                    radius.getValue(),
                    backgroundColor.getValue()
            );
        } else {
            Render2DUtil.drawRect(
                    context,
                    getX(),
                    getY(),
                    width,
                    height,
                    backgroundColor.getValue()
            );
        }

        // title
        if (rounded.getValue()) {
            Render2DUtil.drawRoundedRect(
                    context.getMatrices(),
                    x,
                    y,
                    FontUtil.getWidth(FontSize.SMALL, this.getDisplayName()),
                    FontUtil.getHeight(FontSize.SMALL),
                    radius.getValue(),
                    titleBackgroundColor.getValue()
            );
        } else {
            Render2DUtil.drawRect(
                    context,
                    x,
                    y,
                    FontUtil.getWidth(FontSize.SMALL, this.getDisplayName()),
                    FontUtil.getHeight(FontSize.SMALL),
                    titleBackgroundColor.getValue()
            );
        }
        FontUtil.drawTextWithAlign(
                context,
                getDisplayName(),
                x,
                y,
                x + FontUtil.getWidth(FontSize.SMALL, this.getDisplayName()),
                y + FontUtil.getHeight(FontSize.SMALL),
                Aligns.CENTER,
                titleColor.getValue(),
                FontSize.SMALL
        );

        setWidth(width);
        setHeight(height);

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
