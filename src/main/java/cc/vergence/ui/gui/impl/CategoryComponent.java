package cc.vergence.ui.gui.impl;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.enums.MouseButtons;
import cc.vergence.features.managers.GuiManager;
import cc.vergence.features.managers.MessageManager;
import cc.vergence.modules.Module;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.util.animations.ColorAnimation;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import oshi.util.tuples.Pair;

import java.awt.*;

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
                GuiManager.scrollAnimation.reset();
                GuiManager.scrollAnimation.to(0.00);
                GuiManager.mouseScrolledOffset = 0;
                GuiManager.latestModuleComponentPosition = new Pair<>(GuiManager.MAIN_PAGE_X, GuiManager.MAIN_PAGE_Y + 34 * Render2DUtil.getScaleFactor() + (GuiManager.mouseScrolledOffset * 8));
                GuiManager.CLICKED_LEFT = false;
            }
        } else {
            GuiManager.setCurrentComponent(null);
        }

        Color targetColor;
        if (isHovered(mouseX, mouseY)) {
            if (GuiManager.currentCategory.equals(this.category)) {
                targetColor = Vergence.THEME.getTheme().getCategoryCurrentBackgroundColor();
            } else {
                targetColor = Vergence.THEME.getTheme().getCategoryHoveredBackgroundColor();
            }
        }
        else if (GuiManager.currentCategory.equals(this.category)) {
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
            if (GuiManager.currentCategory.equals(this.category)) {
                targetTextColor = Vergence.THEME.getTheme().getCategoryCurrentTextColor();
            } else {
                targetTextColor = Vergence.THEME.getTheme().getCategoryHoveredTextColor();
            }
        }
        else if (GuiManager.currentCategory.equals(this.category)) {
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

        if (GuiManager.currentCategory.equals(this.category)) {
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
