package cc.vergence.ui.gui.impl.impl.button;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Aligns;
import cc.vergence.features.managers.GuiManager;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.util.render.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import oshi.util.tuples.Pair;

public class BooleanButtonComponent extends GuiComponent {
    private BooleanOption option;

    public BooleanButtonComponent(BooleanOption option) {
        this.option = option;
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (!GuiManager.hoverComponentDrawing && isHovered(mouseX, mouseY)) {
            GuiManager.setCurrentComponent(this);
            if (clickLeft) {
                if (Vergence.GUI.isAvailable(this)) {
                    option.setValue(!option.getValue());
                    GuiManager.CLICKED_LEFT = false;
                }
            }
        } else {
            GuiManager.setCurrentComponent(null);
        }

        Pair<Double, Double> rectPosition = Render2DUtil.drawRoundedRectWithAlign(
                context.getMatrices(),
                this.getX(),
                this.getY(),
                this.getX() + this.getWidth(),
                this.getY() + this.getHeight(),
                this.getWidth() - 6 * Render2DUtil.getScaleFactor(),
                this.getHeight() / 2,
                4 * Render2DUtil.getScaleFactor(),
                isHovered(mouseX, mouseY) ? (this.option.getValue() ? Vergence.THEME.getTheme().getButtonEnabledBackgroundColor() : Vergence.THEME.getTheme().getButtonHoveredBackgroundColor()) : (this.option.getValue() ? Vergence.THEME.getTheme().getButtonEnabledBackgroundColor() : Vergence.THEME.getTheme().getButtonBackgroundColor()),
                Aligns.CENTER
        );

        if (option.getValue()) {
            Render2DUtil.drawCircleWithInlineWithAlign(
                    context.getMatrices(),
                    Vergence.THEME.getTheme().getButtonEnabledCircleColor(),
                    Vergence.THEME.getTheme().getButtonEnabledInlineColor(),
                    rectPosition.getA() + 3 * Render2DUtil.getScaleFactor(),
                    rectPosition.getB(),
                    rectPosition.getA() + this.getWidth() - 9 * Render2DUtil.getScaleFactor(),
                    rectPosition.getB() + this.getHeight() / 2,
                    4 * Render2DUtil.getScaleFactor(),
                    1 * Render2DUtil.getScaleFactor(),
                    1 * Render2DUtil.getScaleFactor(),
                    360,
                    Aligns.RIGHT
            );
        } else {
            Render2DUtil.drawCircleWithInlineWithAlign(
                    context.getMatrices(),
                    isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getButtonHoveredCircleColor(): Vergence.THEME.getTheme().getButtonCircleColor(),
                    isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getButtonHoveredInlineColor(): Vergence.THEME.getTheme().getButtonInlineColor(),
                    rectPosition.getA() + 3 * Render2DUtil.getScaleFactor(),
                    rectPosition.getB(),
                    rectPosition.getA() + this.getWidth() - 9 * Render2DUtil.getScaleFactor(),
                    rectPosition.getB() + this.getHeight() / 2,
                    4 * Render2DUtil.getScaleFactor(),
                    1 * Render2DUtil.getScaleFactor(),
                    1 * Render2DUtil.getScaleFactor(),
                    360,
                    Aligns.LEFT
            );
        }
    }
}
