package cc.vergence.ui.clickgui.subcomponent.color;

import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.ui.GuiComponent;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_1_ui_gird
 */
public class ColorPreviewer extends GuiComponent {
    private ColorPalette colorPalette;
    public boolean isSpread;

    public ColorPreviewer(ColorPalette colorPalette) {
        this.colorPalette = colorPalette;
    }

    public void setColorPalette(ColorPalette colorPalette) {
        this.colorPalette = colorPalette;
    }

    public ColorPalette getColorPalette() {
        return colorPalette;
    }

    @Override
    public void onDraw(double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (isHovered(mouseX, mouseY)) {
            if (clickLeft) {
                this.colorPalette.getOption().setRainbow(!this.colorPalette.getOption().isRainbow());
                this.colorPalette.getOption().setTime(System.currentTimeMillis());
                GuiManager.CLICKED_LEFT = false;
            }
            if (clickRight) {
                isSpread = !isSpread;
                GuiManager.CLICKED_RIGHT = false;
            }
        } else {
            if (clickRight) {
                isSpread = false;
            }
        }

        Render2DUtil.drawRoundedRectWithAlign(
                this.x,
                this.y,
                this.x + getWidth(),
                this.y + getHeight(),
                this.getWidth(),
                this.getHeight(),
                2,
                colorPalette.getOption().getValue(),
                Aligns.CENTER
        );
    }
}
