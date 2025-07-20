package cc.vergence.ui.gui.impl.impl.hovered;

import cc.vergence.Vergence;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.ui.gui.impl.impl.choice.MultipleChoicesComponent;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class HoverMultipleChoicesComponent extends GuiComponent {
    public HoverMultipleChoicesComponent(MultipleChoicesComponent multipleChoicesComponent) {
        this.setParentComponent(multipleChoicesComponent);
    }

    @Override
    public double getHeight() {
        return super.getHeight() * ((MultipleChoicesComponent) getParentComponent()).animationProgress;
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (!isHovered(mouseX, mouseY)) {
            if (clickLeft || clickRight) {
                ((MultipleChoicesComponent) this.parentComponent).fold();
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

        if (((MultipleChoicesComponent) getParentComponent()).isSpread() || (((MultipleChoicesComponent) getParentComponent()).animationProgress > 0.001f && ((MultipleChoicesComponent) getParentComponent()).animationProgress != 1f)) {
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
            matrices.scale(1f, ((MultipleChoicesComponent) getParentComponent()).animationProgress, 1f);
            matrices.translate(-getX(), -(baseY), 0);

            for (GuiComponent component : getSubComponents()) {
                component.onDraw(context, mouseX, mouseY, clickLeft, clickRight);
            }

            matrices.pop();
            Render2DUtil.popDisplayArea();
        }
    }
}
