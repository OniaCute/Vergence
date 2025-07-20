package cc.vergence.ui.gui.impl.impl.hovered;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.ui.gui.impl.impl.choice.EnumChoicesComponent;
import cc.vergence.util.animations.ColorAnimation;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class EnumChoiceComponent extends GuiComponent {
    private Enum<?> value;
    private boolean chosen;
    private ColorAnimation backgroundColorAnimation;
    private ColorAnimation textColorAnimation;
    private Color lastBgTarget = null;
    private Color lastTextTarget = null;

    public EnumChoiceComponent(HoverEnumChoicesComponent component, Enum<?> value) {
        this.setParentComponent(component);
        this.value = value;

        this.backgroundColorAnimation = new ColorAnimation(Vergence.THEME.getTheme().getChoicesDefaultBackgroundColor(), Vergence.THEME.getTheme().getChoicesDefaultBackgroundColor(), 130);
        this.textColorAnimation = new ColorAnimation(Vergence.THEME.getTheme().getChoicesDefaultTextColor(), Vergence.THEME.getTheme().getChoicesDefaultTextColor(), 130);
        this.lastBgTarget = Vergence.THEME.getTheme().getChoicesDefaultBackgroundColor();
        this.lastTextTarget = Vergence.THEME.getTheme().getChoicesDefaultTextColor();
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        GuiManager.setCurrentComponent(this);
        for (GuiComponent component : this.getParentComponent().getSubComponents()) {
            ((EnumChoiceComponent) component).chosen = false;
        }
        if (isHovered(mouseX, mouseY) && clickLeft) {
            GuiManager.CLICKED_LEFT = false;
            ((EnumChoicesComponent) this.getParentComponent().getParentComponent()).getOption().setValue(value);
            this.chosen = true;
        }

        if (((EnumChoicesComponent) this.getParentComponent().getParentComponent()).getOption().getValue().name().equals(value.name())) {
            this.chosen = true;
        }

        Color bgTarget;
        if (isHovered(mouseX, mouseY)) {
            bgTarget = chosen ? Vergence.THEME.getTheme().getChoicesEnabledBackgroundColor()
                    : Vergence.THEME.getTheme().getChoicesHoveredBackgroundColor();
        } else {
            bgTarget = chosen ? Vergence.THEME.getTheme().getChoicesEnabledBackgroundColor()
                    : Vergence.THEME.getTheme().getChoicesDefaultBackgroundColor();
        }

        if (!bgTarget.equals(lastBgTarget)) {
            backgroundColorAnimation.resetTo(bgTarget);
            lastBgTarget = bgTarget;
        }

        Render2DUtil.drawRoundedRect(
                context.getMatrices(),
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight(),
                2 * Render2DUtil.getScaleFactor(),
                backgroundColorAnimation.getCurrent()
        );

        Color textTarget;
        if (isHovered(mouseX, mouseY)) {
            textTarget = chosen ? Vergence.THEME.getTheme().getChoicesEnabledTextColor()
                    : Vergence.THEME.getTheme().getChoicesHoveredTextColor();
        } else {
            textTarget = chosen ? Vergence.THEME.getTheme().getChoicesEnabledTextColor()
                    : Vergence.THEME.getTheme().getChoicesDefaultTextColor();
        }

        if (!textTarget.equals(lastTextTarget)) {
            textColorAnimation.resetTo(textTarget);
            lastTextTarget = textTarget;
        }

        FontUtil.drawTextWithAlign(
                context,
                value.name(),
                this.getX(),
                this.getY(),
                this.getX() + this.getWidth() - 3 * Render2DUtil.getScaleFactor(),
                this.getY() + this.getHeight(),
                Aligns.RIGHT,
                textColorAnimation.getCurrent(),
                FontSize.SMALLEST
        );

        if (chosen) {
            FontUtil.drawTextWithAlign(
                    context,
                    " ✓ ",
                    this.getX() + 3 * Render2DUtil.getScaleFactor(),
                    this.getY() + 1 * Render2DUtil.getScaleFactor(),
                    this.getX() + this.getWidth(),
                    this.getY() + this.getHeight(),
                    Aligns.LEFT,
                    textColorAnimation.getCurrent(),
                    FontSize.SMALLEST
            );
        }
    }
}
