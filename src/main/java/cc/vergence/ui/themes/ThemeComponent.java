package cc.vergence.ui.themes;

import cc.vergence.Vergence;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.features.themes.Theme;
import cc.vergence.ui.GuiComponent;
import cc.vergence.util.animations.ColorAnimation;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_0_ui_gird
 */
public class ThemeComponent extends GuiComponent {
    private Theme theme;
    private ColorAnimation backgroundColorAnimation;
    private ColorAnimation textColorAnimation;
    private ColorAnimation authorsColorAnimation;

    public ThemeComponent(Theme theme) {
        this.theme = theme;
        this.backgroundColorAnimation = new ColorAnimation(
                Vergence.THEME.getTheme().getThemePageComponentBackgroundColor(),
                Vergence.THEME.getTheme().getThemePageComponentBackgroundColor(),
                130
        );
        this.textColorAnimation = new ColorAnimation(
                Vergence.THEME.getTheme().getThemePageComponentTextColor(),
                Vergence.THEME.getTheme().getThemePageComponentTextColor(),
                130
        );
        this.authorsColorAnimation = new ColorAnimation(
                Vergence.THEME.getTheme().getThemePageComponentAuthorsColor(),
                Vergence.THEME.getTheme().getThemePageComponentAuthorsColor(),
                130
        );
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (isHovered(mouseX, mouseY)) {
            if (clickLeft) {
                Vergence.THEME.loadTheme(theme);
                NotifyManager.newNotification("Theme", Vergence.TEXT.get("Theme.Messages.Loaded").replace("{theme}", theme.getDisplayName()).replace("{raw_theme}", theme.getName()));
                GuiManager.CLICKED_LEFT = false;
            }
        }

        Color targetColor;
        if (isHovered(mouseX, mouseY)) {
            if (Vergence.THEME.getTheme().getName().equals(theme.getName())) {
                targetColor = Vergence.THEME.getTheme().getThemePageComponentChosenBackgroundColor();
            } else {
                targetColor = Vergence.THEME.getTheme().getThemePageComponentHoveredBackgroundColor();
            }
        }
        else if (Vergence.THEME.getTheme().getName().equals(theme.getName())) {
            targetColor = Vergence.THEME.getTheme().getThemePageComponentChosenBackgroundColor();
        } else {
            targetColor = Vergence.THEME.getTheme().getThemePageComponentHoveredBackgroundColor();
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
            if (Vergence.THEME.getTheme().getName().equals(theme.getName())) {
                targetColor1 = Vergence.THEME.getTheme().getThemePageComponentChosenTextColor();
            } else {
                targetColor1 = Vergence.THEME.getTheme().getThemePageComponentHoveredTextColor();
            }
        }
        else if (Vergence.THEME.getTheme().getName().equals(theme.getName())) {
            targetColor1 = Vergence.THEME.getTheme().getThemePageComponentChosenTextColor();
        } else {
            targetColor1 = Vergence.THEME.getTheme().getThemePageComponentHoveredTextColor();
        }

        textColorAnimation.reset(textColorAnimation.getCurrent(), targetColor1);
        FontUtil.drawText(
                context,
                theme.getDisplayName() + " (" + theme.getName() + ")",
                getX() + 3,
                getY() + 2,
                targetColor1,
                FontSize.MEDIUM
        );
        FontUtil.drawText(
                context,
                theme.getDescription(),
                getX() + 3,
                getY() + FontUtil.getHeight(FontSize.MEDIUM) + 3,
                targetColor1,
                FontSize.SMALLEST
        );

        Color targetColor2;
        if (isHovered(mouseX, mouseY)) {
            if (Vergence.THEME.getTheme().getName().equals(theme.getName())) {
                targetColor2 = Vergence.THEME.getTheme().getThemePageComponentChosenAuthorsColor();
            } else {
                targetColor2 = Vergence.THEME.getTheme().getThemePageComponentHoveredAuthorsColor();
            }
        }
        else if (Vergence.THEME.getTheme().getName().equals(theme.getName())) {
            targetColor2 = Vergence.THEME.getTheme().getThemePageComponentChosenAuthorsColor();
        } else {
            targetColor2 = Vergence.THEME.getTheme().getThemePageComponentHoveredAuthorsColor();
        }

        authorsColorAnimation.reset(authorsColorAnimation.getCurrent(), targetColor2);
        FontUtil.drawTextWithAlign(
                context,
                String.join(", ", theme.getAuthors()),
                getX(),
                getY() + 3,
                getX() + getWidth() - 3,
                getY() + getHeight(),
                Aligns.RIGHT_TOP,
                targetColor2,
                FontSize.SMALL
        );
    }
}
