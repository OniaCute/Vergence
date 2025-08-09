package cc.vergence.ui.clickgui.subcomponent.button;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.ui.GuiComponent;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import oshi.util.tuples.Pair;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_1_ui_gird
 */
public class BooleanButtonComponent extends GuiComponent {
    private BooleanOption option;

    public BooleanButtonComponent(BooleanOption option) {
        this.option = option;
    }

    @Override
    public void onDraw(double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
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

        Pair<Double, Double> rectPosition = Render2DUtil.getAlignPositionAsPair(
                this.getX(),
                this.getY(),
                this.getX() + this.getWidth(),
                this.getY() + this.getHeight(),
                this.getWidth() - 6,
                this.getHeight() / 2,
                Aligns.CENTER
        );

        Render2DUtil.drawRoundedRect(
            rectPosition.getA(),
            rectPosition.getB(),
            this.getWidth() - 6,
            this.getHeight() / 2,
            4,
            isHovered(mouseX, mouseY) ? (this.option.getValue() ? Vergence.THEME.getTheme().getButtonEnabledBackgroundColor() : Vergence.THEME.getTheme().getButtonHoveredBackgroundColor()) : (this.option.getValue() ? Vergence.THEME.getTheme().getButtonEnabledBackgroundColor() : Vergence.THEME.getTheme().getButtonBackgroundColor())
        );

        if (option.getValue()) {
            Render2DUtil.drawCircleWithInlineWithAlign(
                    rectPosition.getA(),
                    rectPosition.getB(),
                    rectPosition.getA() + this.getWidth(),
                    rectPosition.getB() + this.getHeight(),
                    4,
                    1,
                    1,
                    Vergence.THEME.getTheme().getButtonEnabledCircleColor(),
                    Vergence.THEME.getTheme().getButtonEnabledInlineColor(),
                    Aligns.RIGHT
            );
        } else {
            Render2DUtil.drawCircleWithInlineWithAlign(
                rectPosition.getA(),
                rectPosition.getB(),
                rectPosition.getA() + this.getWidth(),
                rectPosition.getB() + this.getHeight(),
                4,
                1,
                1,
                isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getButtonHoveredCircleColor(): Vergence.THEME.getTheme().getButtonCircleColor(),
                isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getButtonHoveredInlineColor(): Vergence.THEME.getTheme().getButtonInlineColor(),
                Aligns.RIGHT
            );
        }
    }
}
