package cc.vergence.ui.clickgui.subcomponent.hovered;

import cc.vergence.Vergence;
import cc.vergence.ui.GuiComponent;
import cc.vergence.ui.clickgui.subcomponent.choice.EnumChoicesComponent;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_0_ui_gird
 */
public class HoverEnumChoicesComponent extends GuiComponent {
    public HoverEnumChoicesComponent(EnumChoicesComponent enumChoicesComponent) {
        this.setParentComponent(enumChoicesComponent);
    }

    @Override
    public double getHeight() {
        return super.getHeight() * ((EnumChoicesComponent) getParentComponent()).animationProgress;
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (!isHovered(mouseX, mouseY)) {
            if (clickLeft || clickRight) {
                ((EnumChoicesComponent) this.parentComponent).fold();
            }
        }

        Render2DUtil.drawRoundedRect(
                context.getMatrices(),
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight(),
                3 * Render2DUtil.getScaleFactor(),
                Vergence.THEME.getTheme().getChoicesAreaBackgroundColor()
        );

        if (((EnumChoicesComponent) getParentComponent()).isActuallySpread || (((EnumChoicesComponent) getParentComponent()).animationProgress > 0.001f && ((EnumChoicesComponent) getParentComponent()).animationProgress != 1f)) {
            MatrixStack matrices = context.getMatrices();
            matrices.push();

            Render2DUtil.pushDisplayArea(
                    context.getMatrices(),
                    (float) getX(),
                    (float) getY(),
                    (float) (getX() + getWidth()),
                    (float) (getY() + getHeight()),
                    1f
            );

            double baseY = getY();

            matrices.translate(getX(), baseY, 0);
            matrices.scale(1f, ((EnumChoicesComponent) getParentComponent()).animationProgress, 1f);
            matrices.translate(-getX(), -(baseY), 0);

            for (GuiComponent component : getSubComponents()) {
                component.onDraw(context, mouseX, mouseY, clickLeft, clickRight);
            }

            matrices.pop();
            Render2DUtil.popDisplayArea();
        }
    }
}
