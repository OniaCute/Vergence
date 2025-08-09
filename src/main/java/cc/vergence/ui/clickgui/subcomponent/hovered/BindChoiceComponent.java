package cc.vergence.ui.clickgui.subcomponent.hovered;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.options.impl.BindOption;
import cc.vergence.ui.GuiComponent;
import cc.vergence.ui.clickgui.subcomponent.choice.BindChoicesComponent;
import cc.vergence.util.animations.ColorAnimation;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_1_ui_gird
 */
public class BindChoiceComponent extends GuiComponent {
    private Enum<?> value;
    private boolean chosen;
    private ColorAnimation backgroundColorAnimation;
    private ColorAnimation textColorAnimation;
    private Color lastBgTarget = null;
    private Color lastTextTarget = null;

    public BindChoiceComponent(HoverBindChoicesComponent component, Enum<?> value) {
        this.setParentComponent(component);
        this.value = value;

        this.backgroundColorAnimation = new ColorAnimation(Vergence.THEME.getTheme().getChoicesDefaultBackgroundColor(), Vergence.THEME.getTheme().getChoicesDefaultBackgroundColor(), 130);
        this.textColorAnimation = new ColorAnimation(Vergence.THEME.getTheme().getChoicesDefaultTextColor(), Vergence.THEME.getTheme().getChoicesDefaultTextColor(), 130);
        this.lastBgTarget = Vergence.THEME.getTheme().getChoicesDefaultBackgroundColor();
        this.lastTextTarget = Vergence.THEME.getTheme().getChoicesDefaultTextColor();
    }

    @Override
    public void onDraw(double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        GuiManager.setCurrentComponent(this);
        for (GuiComponent component : this.getParentComponent().getSubComponents()) {
            ((BindChoiceComponent) component).chosen = false;
        }
        if (isHovered(mouseX, mouseY) && clickLeft) {
            GuiManager.CLICKED_LEFT = false;
            ((BindChoicesComponent) this.getParentComponent().getParentComponent()).getOption().setBindType((BindOption.BindType) value);
            this.chosen = true;
        }

        if (((BindChoicesComponent) this.getParentComponent().getParentComponent()).getOption().getBindType().name().equals(value.name())) {
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
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight(),
                2,
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
                value.name(),
                this.getX(),
                this.getY(),
                this.getX() + this.getWidth() - 3,
                this.getY() + this.getHeight(),
                textColorAnimation.getCurrent(),
                FontSize.SMALLEST,
                Aligns.RIGHT
        );

        if (chosen) {
            FontUtil.drawTextWithAlign(
                    " ✓ ",
                    this.getX() + 3,
                    this.getY() + 1,
                    this.getX() + this.getWidth(),
                    this.getY() + this.getHeight(),
                    textColorAnimation.getCurrent(),
                    FontSize.SMALLEST,
                    Aligns.LEFT
            );
        }
    }
}
