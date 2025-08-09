package cc.vergence.ui.clickgui.subcomponent.slider;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.client.MouseButtons;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.screens.ClickGuiScreen;
import cc.vergence.ui.GuiComponent;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import oshi.util.tuples.Pair;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_1_ui_gird
 */
public class DoubleSlider extends GuiComponent {
    private DoubleOption option;
    private boolean dragging = false;


    public DoubleSlider(DoubleOption option) {
        this.option = option;
        GuiManager.sliderComponents.add(this);
    }

    @Override
    public void onMouseRelease(double mouseX, double mouseY, Screen screen, MouseButtons button) {
        if (screen instanceof ClickGuiScreen && button.equals(MouseButtons.LEFT)) {
            dragging = false;
        }
    }

    @Override
    public void onDraw(double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        double maxValue = option.getMaxValue();
        double minValue = option.getMinValue();
        if (clickLeft && this.isHovered(mouseX, mouseY)) {
            dragging = true;
        }
        if (dragging) {
            double mouseRelativeX = Math.min(Math.max(mouseX - this.getX(), 0), this.getWidth());
            double newPercent = mouseRelativeX / this.getWidth();
            double newValue = minValue + (maxValue - minValue) * newPercent;
            option.setValue(Math.round(newValue * 100.0) / 100.0);
        }

        double currentValue = option.getValue();
        double percent = (currentValue - minValue) / (maxValue - minValue);

        Pair<Double, Double> rectPos = Render2DUtil.getAlignPositionAsPair(
                this.getX(),
                this.getY(),
                this.getX() + this.getWidth(),
                this.getY() + this.getHeight(),
                this.getWidth(),
                2,
                Aligns.CENTER
        );

        Render2DUtil.drawRoundedRect(
                rectPos.getA(),
                rectPos.getB(),
                this.getWidth(),
                2,
                1,
                isHovered(mouseX, mouseY)
                        ? Vergence.THEME.getTheme().getSliderHoveredBackgroundColor()
                        : Vergence.THEME.getTheme().getSliderBackgroundColor()
        );

        double sliderStartX = rectPos.getA();
        double sliderY = rectPos.getB() + 1;
        double fillWidth = percent * this.getWidth();
        double circleX = sliderStartX + fillWidth;
        double circleRadius = 4;

        Render2DUtil.drawRoundedRect(
                (float) sliderStartX,
                (float) (sliderY - 1),
                (float) fillWidth,
                2,
                1,
                Vergence.THEME.getTheme().getSliderValuedBackgroundColor()
        );

        Render2DUtil.drawCircleWithInline(
                circleX, sliderY, circleRadius,
                1.2f, 0.8f,
                isHovered(mouseX, mouseY) ? (dragging ? Vergence.THEME.getTheme().getSliderClickedCircleColor() : Vergence.THEME.getTheme().getSliderHoveredCircleColor()) : (dragging ? Vergence.THEME.getTheme().getSliderClickedCircleColor() : Vergence.THEME.getTheme().getSliderCircleColor()),
                isHovered(mouseX, mouseY) ? (dragging ? Vergence.THEME.getTheme().getSliderClickedInlineColor() : Vergence.THEME.getTheme().getSliderHoveredInlineColor()) : (dragging ? Vergence.THEME.getTheme().getSliderClickedInlineColor() : Vergence.THEME.getTheme().getSliderInlineColor())
        );
    }
}
