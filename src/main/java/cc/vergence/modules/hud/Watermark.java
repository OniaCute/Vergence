package cc.vergence.modules.hud;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.enums.client.MouseButtons;
import cc.vergence.features.managers.ui.HudManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.modules.Module;
import cc.vergence.modules.misc.NameProtect;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Watermark extends Module {
    private boolean lastMouseStatus = false;
    private double lastMouseX = -1;
    private double lastMouseY = -1;

    public Watermark() {
        super("Watermark", Category.HUD);
        setX(0);
        setY(0);
        setWidth(0);
        setHeight(10);
    }

    public Option<Boolean> customTitle = addOption(new BooleanOption("CustomTitle"));
    public Option<String> title = addOption(new TextOption("Title", "{full_name} {version}", v -> customTitle.getValue()));
    public Option<Boolean> outline = addOption(new BooleanOption("Outline"));
    public Option<Double> outlineWidth = addOption(new DoubleOption("OutlineWidth", 1, 3, 1, v -> outline.getValue()));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded"));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 1, 6, 3, v -> rounded.getValue()));
    public Option<Boolean> split = addOption(new BooleanOption("Split"));
    public Option<Boolean> includedTime = addOption(new BooleanOption("IncludedTime"));
    public Option<Boolean> includedFps = addOption(new BooleanOption("IncludedFPS"));
    public Option<Boolean> includedUser = addOption(new BooleanOption("IncludedUser"));
    public Option<Boolean> includedConfig = addOption(new BooleanOption("IncludedConfig"));
    public Option<Color> textColor = addOption(new ColorOption("TextColor", new Color(17, 17, 17)));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(246, 246, 246)));
    public Option<Color> outlineColor = addOption(new ColorOption("OutlineColor", new Color(239, 239, 239), v -> outline.getValue()));

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
    public void onDraw2D() {
        String separator = split.getValue() ? "  " : " | ";
        ArrayList<String> parts = new ArrayList<>();
        parts.add(customTitle.getValue() ? title.getValue() : "Vergence");
        if (includedTime.getValue()) {
            String timeString = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            parts.add(timeString);
        }
        if (includedFps.getValue()) {
            parts.add(mc.getCurrentFps() + " FPS");
        }
        if (includedUser.getValue()) {
            parts.add(mc.player != null && NameProtect.INSTANCE != null ? (NameProtect.INSTANCE.getStatus() ? NameProtect.INSTANCE.nickname.getValue() : mc.player.getName().getString()) : "Unknown");
        }
        if (includedConfig.getValue()) {
            String configName = Vergence.CONFIG.currentConfigName;
            parts.add(configName);
        }

        String displayString = String.join(separator, parts);

        setWidth(FontUtil.getWidth(FontSize.SMALL, displayString) + 4);
        setHeight(FontUtil.getHeight(FontSize.SMALL));

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

        if (split.getValue()) {
            //
        } else {
            if (rounded.getValue()) {
                Render2DUtil.drawRoundedRectWithOutline(
                        this.getX(),
                        this.getY(),
                        this.getWidth(),
                        this.getHeight(),
                        radius.getValue(),
                        outline.getValue() ? outlineWidth.getValue() : 0,
                        backgroundColor.getValue(),
                        outlineColor.getValue()
                );
            } else {
                Render2DUtil.drawRectWithOutline(
                        this.getX(),
                        this.getY(),
                        this.getWidth(),
                        this.getHeight(),
                        outline.getValue() ? outlineWidth.getValue() : 0,
                        backgroundColor.getValue(),
                        outlineColor.getValue()
                );
            }

            FontUtil.drawTextWithAlign(
                    displayString,
                    this.getX(),
                    this.getY() + 2,
                    this.getX() + this.getWidth(),
                    this.getY() + this.getHeight(),
                    textColor.getValue(),
                    FontSize.SMALL,
                    Aligns.CENTER
            );
        }
    }
}
