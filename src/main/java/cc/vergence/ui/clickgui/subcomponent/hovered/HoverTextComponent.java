package cc.vergence.ui.clickgui.subcomponent.hovered;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.managers.client.AnimationManager;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.modules.client.ClickGUI;
import cc.vergence.ui.GuiComponent;
import cc.vergence.ui.clickgui.option.*;
import cc.vergence.util.animations.GuiAnimation;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_0_ui_gird
 */
public class HoverTextComponent extends GuiComponent {
    private final GuiAnimation animation = new GuiAnimation(200);
    private boolean lastHovered = true;

    public HoverTextComponent(GuiComponent component) {
        this.setParentComponent(component);
        animation.setToEnd();
        AnimationManager.descriptionAnimations.add(animation);
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (ClickGUI.INSTANCE != null && !ClickGUI.INSTANCE.showOptionDescription.getValue()) {
            return ;
        }

        boolean hovered = isHovered(
                getParentComponent().getX(),
                getParentComponent().getY(),
                getParentComponent().getWidth(),
                getParentComponent().getHeight(),
                mouseX, mouseY
        );

        if (hovered && !lastHovered) {
            animation.restart();
        }
        else if (!hovered && lastHovered) {
            animation.reverse();
        }
        lastHovered = hovered;

        double progress = animation.getProgress();
        if (progress > 0.001) {
            Render2DUtil.insertDisplayArea(
                    context.getMatrices(),
                    (float) getX(),
                    (float) GuiManager.MAIN_PAGE_Y + 33,
                    (float) (getX() + getWidth() * progress),
                    (float) (GuiManager.MAIN_PAGE_Y + GuiManager.MAIN_PAGE_HEIGHT),
                    1.0,
                    () -> {
                        Render2DUtil.drawRoundedRectWithAlign(
                                context.getMatrices(),
                                getX(),
                                getY(),
                                getX() + getWidth(),
                                getY() + getHeight(),
                                getWidth(),
                                getHeight(),
                                3,
                                Vergence.THEME.getTheme().getDescriptionBackgroundColor(),
                                Aligns.CENTER
                        );

                        FontUtil.drawTextWithAlign(
                                context,
                                getDescription(getParentComponent()),
                                getX(),
                                getY() + 2,
                                getX() + getWidth(),
                                getY() + getHeight(),
                                Aligns.CENTER,
                                Vergence.THEME.getTheme().getDescriptionTextColor(),
                                FontSize.SMALLEST
                        );
                    }
            );
        }
    }

    public String getDescription(GuiComponent component) {
        if (component instanceof BooleanComponent) {
            return ((BooleanComponent) component).getOption().getDescription();
        }
        else if (component instanceof EnumComponent) {
            return ((EnumComponent) component).getOption().getDescription();
        }
        else if (component instanceof MultipleComponent) {
            return ((MultipleComponent) component).getOption().getDescription();
        }
        else if (component instanceof TextComponent) {
            return ((TextComponent) component).getOption().getDescription();
        }
        else if (component instanceof DoubleComponent) {
            return ((DoubleComponent) component).getOption().getDescription();
        }
        else if (component instanceof ColorComponent) {
            return ((ColorComponent) component).getOption().getDescription();
        }
        else if (component instanceof BindComponent) {
            return ((BindComponent) component).getOption().getDescription();
        }
        return "No Description";
    }
}
