package cc.vergence.ui.clickgui;

import cc.vergence.Vergence;
import cc.vergence.features.enums.client.Pages;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.modules.Module;
import cc.vergence.ui.GuiComponent;
import cc.vergence.util.animations.ColorAnimation;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import oshi.util.tuples.Pair;

import java.awt.*;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_0_ui_gird
 */
public class CategoryComponent extends GuiComponent {
    public Module.Category category;
    private ColorAnimation backgroundColorAnimation;
    private ColorAnimation textColorAnimation;


    public CategoryComponent(Module.Category category) {
        this.category = category;
        this.setDisplayName(Vergence.TEXT.get("Module.Category." + category.name() + ".name"));
        this.backgroundColorAnimation = new ColorAnimation(
                Vergence.THEME.getTheme().getCategoryBackgroundColor(),
                Vergence.THEME.getTheme().getCategoryBackgroundColor(),
                130
        );
        this.textColorAnimation = new ColorAnimation(
                Vergence.THEME.getTheme().getCategoryTextColor(),
                Vergence.THEME.getTheme().getCategoryTextColor(),
                130
        );
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (isHovered(mouseX, mouseY)) {
            GuiManager.setCurrentComponent(this);
            if (clickLeft) {
                GuiManager.currentCategory = this.category;
                GuiManager.PAGE = Pages.Modules;
                GuiManager.SEARCH.searchText = "";
                GuiManager.SEARCH.setListening(false);
                GuiManager.resetScroll();
                GuiManager.latestModuleComponentPosition = new Pair<>(GuiManager.MAIN_PAGE_X, GuiManager.MAIN_PAGE_Y + 34 * Render2DUtil.getScaleFactor() + (GuiManager.mouseScrolledOffset * 8));
                GuiManager.CLICKED_LEFT = false;
            }
        } else {
            GuiManager.setCurrentComponent(null);
        }

        Color targetColor;
        if (isHovered(mouseX, mouseY)) {
            if (!(GuiManager.currentCategory == null) && GuiManager.currentCategory.equals(this.category)) {
                targetColor = Vergence.THEME.getTheme().getCategoryCurrentBackgroundColor();
            } else {
                targetColor = Vergence.THEME.getTheme().getCategoryHoveredBackgroundColor();
            }
        }
        else if (!(GuiManager.currentCategory == null) && GuiManager.currentCategory.equals(this.category)) {
            targetColor = Vergence.THEME.getTheme().getCategoryCurrentBackgroundColor();
        } else {
            targetColor = Vergence.THEME.getTheme().getCategoryBackgroundColor();
        }

        backgroundColorAnimation.reset(backgroundColorAnimation.getCurrent(), targetColor);

        Render2DUtil.drawRoundedRect(
                context.getMatrices(),
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight(),
                4 * Render2DUtil.getScaleFactor(),
                backgroundColorAnimation.getCurrent()
        );

        Color targetTextColor;
        if (isHovered(mouseX, mouseY)) {
            if (!(GuiManager.currentCategory == null) && GuiManager.currentCategory.equals(this.category)) {
                targetTextColor = Vergence.THEME.getTheme().getCategoryCurrentTextColor();
            } else {
                targetTextColor = Vergence.THEME.getTheme().getCategoryHoveredTextColor();
            }
        }
        else if (!(GuiManager.currentCategory == null) && GuiManager.currentCategory.equals(this.category)) {
            targetTextColor = Vergence.THEME.getTheme().getCategoryCurrentTextColor();
        } else {
            targetTextColor = Vergence.THEME.getTheme().getCategoryTextColor();
        }

        textColorAnimation.resetTo(targetTextColor);

        FontUtil.drawTextWithAlign(
                context,
                this.getDisplayName(),
                this.getX(),
                this.getY(),
                this.getX() + this.getWidth(),
                this.getY() + this.getHeight(),
                Aligns.CENTER,
                textColorAnimation.getCurrent(),
                FontSize.LARGE
        );
        if (!(GuiManager.currentCategory == null) && GuiManager.currentCategory.equals(this.category) && GuiManager.PAGE.equals(Pages.Modules)) {
            for (GuiComponent component : this.subComponents) {
                Render2DUtil.pushDisplayArea(
                        context.getMatrices(),
                        (float) component.getX(),
                        (float) (GuiManager.MAIN_PAGE_Y + 33),
                        (float) (component.getX() + component.getWidth()),
                        (float) (GuiManager.MAIN_PAGE_Y + GuiManager.MAIN_PAGE_HEIGHT),
                        1d

                );

                component.onDraw(context, mouseX, mouseY, clickLeft, clickRight);

                Render2DUtil.popDisplayArea();
            }
        }
    }

    public void setCategory(Module.Category category) {
        this.category = category;
    }

    public Module.Category getCategory() {
        return category;
    }
}
