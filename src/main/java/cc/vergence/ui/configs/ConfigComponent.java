package cc.vergence.ui.configs;

import cc.vergence.Vergence;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.managers.client.ConfigManager;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.ui.GuiComponent;
import cc.vergence.util.animations.ColorAnimation;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class ConfigComponent extends GuiComponent {
    private String configName;
    private String version;
    private String date;
    private ColorAnimation backgroundColorAnimation;
    private ColorAnimation textColorAnimation;
    private ColorAnimation dateColorAnimation;

    public ConfigComponent(String name, String version, String date) {
        this.configName = name;
        this.version = version;
        this.date = date;
        this.backgroundColorAnimation = new ColorAnimation(
                Vergence.THEME.getTheme().getConfigPageComponentBackgroundColor(),
                Vergence.THEME.getTheme().getConfigPageComponentBackgroundColor(),
                130
        );
        this.textColorAnimation = new ColorAnimation(
                Vergence.THEME.getTheme().getConfigPageComponentTextColor(),
                Vergence.THEME.getTheme().getConfigPageComponentTextColor(),
                130
        );
        this.dateColorAnimation = new ColorAnimation(
                Vergence.THEME.getTheme().getConfigPageComponentDateColor(),
                Vergence.THEME.getTheme().getConfigPageComponentDateColor(),
                130
        );
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (isHovered(mouseX, mouseY)) {
            if (clickLeft) {
                Vergence.CONFIG.saveCurrentConfig();
                Vergence.CONFIG.load(configName);
                NotifyManager.newNotification("Config", Vergence.TEXT.get("Config.Messages.Loaded").replace("{config}", configName));
                if (!version.isEmpty() && version == Vergence.VERSION) {
                    NotifyManager.newNotification("Config", Vergence.TEXT.get("Config.Messages.OutOfDate").replace("{version}", version));
                }
                GuiManager.CLICKED_LEFT = false;
            }
        }

        Color targetColor;
        if (isHovered(mouseX, mouseY)) {
            if (Vergence.CONFIG.currentConfigName.equals(configName)) {
                targetColor = Vergence.THEME.getTheme().getConfigPageComponentChosenBackgroundColor();
            } else {
                targetColor = Vergence.THEME.getTheme().getConfigPageComponentHoveredBackgroundColor();
            }
        }
        else if (Vergence.CONFIG.currentConfigName.equals(configName)) {
            targetColor = Vergence.THEME.getTheme().getConfigPageComponentChosenBackgroundColor();
        } else {
            targetColor = Vergence.THEME.getTheme().getConfigPageComponentHoveredBackgroundColor();
        }

        backgroundColorAnimation.reset(backgroundColorAnimation.getCurrent(), targetColor);

        Render2DUtil.drawRoundedRect(
                context.getMatrices(),
                getX(),
                getY(),
                getWidth(),
                getHeight(),
                4,
                targetColor
        );

        Color targetColor1;
        if (isHovered(mouseX, mouseY)) {
            if (Vergence.CONFIG.currentConfigName.equals(configName)) {
                targetColor1 = Vergence.THEME.getTheme().getConfigPageComponentChosenTextColor();
            } else {
                targetColor1 = Vergence.THEME.getTheme().getConfigPageComponentHoveredTextColor();
            }
        }
        else if (Vergence.CONFIG.currentConfigName.equals(configName)) {
            targetColor1 = Vergence.THEME.getTheme().getConfigPageComponentChosenTextColor();
        } else {
            targetColor1 = Vergence.THEME.getTheme().getConfigPageComponentHoveredTextColor();
        }

        textColorAnimation.reset(textColorAnimation.getCurrent(), targetColor1);
        FontUtil.drawText(
                context,
                configName,
                getX() + 3,
                getY() + 2,
                targetColor1,
                FontSize.MEDIUM
        );
        FontUtil.drawText(
                context,
                version,
                getX() + 3,
                getY() + FontUtil.getHeight(FontSize.MEDIUM) + 3,
                targetColor1,
                FontSize.SMALLEST
        );

        Color targetColor2;
        if (isHovered(mouseX, mouseY)) {
            if (Vergence.CONFIG.currentConfigName.equals(configName)) {
                targetColor2 = Vergence.THEME.getTheme().getConfigPageComponentChosenDateColor();
            } else {
                targetColor2 = Vergence.THEME.getTheme().getConfigPageComponentHoveredDateColor();
            }
        }
        else if (Vergence.CONFIG.currentConfigName.equals(configName)) {
            targetColor2 = Vergence.THEME.getTheme().getConfigPageComponentChosenDateColor();
        } else {
            targetColor2 = Vergence.THEME.getTheme().getConfigPageComponentHoveredDateColor();
        }

        dateColorAnimation.reset(dateColorAnimation.getCurrent(), targetColor2);
        FontUtil.drawTextWithAlign(
                context,
                date,
                getX(),
                getY() + 3,
                getX() + getWidth() - 3,
                getY() + getHeight(),
                Aligns.RIGHT_TOP,
                targetColor2,
                FontSize.SMALLEST
        );
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigName() {
        return configName;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ColorAnimation getDateColorAnimation() {
        return dateColorAnimation;
    }
}
