package cc.vergence.ui.clickgui.subcomponent.hovered;

import cc.vergence.Vergence;
import cc.vergence.ui.GuiComponent;
import cc.vergence.ui.clickgui.subcomponent.choice.MultipleChoicesComponent;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_1_ui_gird
 */
public class HoverMultipleChoicesComponent extends GuiComponent {
    public HoverMultipleChoicesComponent(MultipleChoicesComponent multipleChoicesComponent) {
        this.setParentComponent(multipleChoicesComponent);
    }

    @Override
    public double getHeight() {
        return super.getHeight() * ((MultipleChoicesComponent) getParentComponent()).animationProgress;
    }

    @Override
    public void onDraw(double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (!isHovered(mouseX, mouseY)) {
            if (clickLeft || clickRight) {
                ((MultipleChoicesComponent) this.parentComponent).fold();
            }
        }

        Render2DUtil.drawRoundedRect(
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight(),
                3,
                Vergence.THEME.getTheme().getChoicesAreaBackgroundColor()
        );

        if (((MultipleChoicesComponent) getParentComponent()).isSpread() || (((MultipleChoicesComponent) getParentComponent()).animationProgress > 0.001f && ((MultipleChoicesComponent) getParentComponent()).animationProgress != 1f)) {
            Render2DUtil.pushDisplayArea(
                    (float) getX(),
                    (float) getY(),
                    (float) (getX() + getWidth()),
                    (float) (getY() + getHeight())
            );

            for (GuiComponent component : getSubComponents()) {
                component.onDraw(mouseX, mouseY, clickLeft, clickRight);
            }
            Render2DUtil.popDisplayArea();
        }
    }
}
