package cc.vergence.ui.gui.impl.impl.hovered;

import cc.vergence.Vergence;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.ui.gui.impl.impl.choice.BindChoicesComponent;
import cc.vergence.ui.gui.impl.impl.choice.MultipleChoicesComponent;
import cc.vergence.util.render.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class HoverBindChoicesComponent extends GuiComponent {
    public HoverBindChoicesComponent(BindChoicesComponent bindChoicesComponent) {
        this.setParentComponent(bindChoicesComponent);
    }

    @Override
    public double getHeight() {
        return super.getHeight() * ((BindChoicesComponent) getParentComponent()).animationProgress;
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (!isHovered(mouseX, mouseY)) {
            if (clickLeft || clickRight) {
                ((BindChoicesComponent) this.parentComponent).fold();
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

        if (((BindChoicesComponent) getParentComponent()).isSpread() || (((BindChoicesComponent) getParentComponent()).animationProgress > 0.001f && ((BindChoicesComponent) getParentComponent()).animationProgress != 1f)) {
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
            matrices.scale(1f, ((BindChoicesComponent) getParentComponent()).animationProgress, 1f);
            matrices.translate(-getX(), -(baseY), 0);

            for (GuiComponent component : getSubComponents()) {
                component.onDraw(context, mouseX, mouseY, clickLeft, clickRight);
            }

            matrices.pop();
            Render2DUtil.popDisplayArea();
        }
    }
}
