package cc.vergence.ui.gui.impl.impl.color;

import cc.vergence.features.enums.Aligns;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

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
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (isHovered(mouseX, mouseY)) {
            if (clickLeft) {
                this.colorPalette.getOption().setRainbow(!this.colorPalette.getOption().isRainbow());
                GuiManager.CLICKED_LEFT = false;
            }
            if (clickRight) {
                isSpread = !isSpread;
                GuiManager.CLICKED_RIGHT = false;
            }
        }

        Render2DUtil.drawRoundedRectWithAlign(
                context.getMatrices(),
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
