package cc.vergence.ui.clickgui.subcomponent.button;

import cc.vergence.ui.GuiComponent;
import cc.vergence.ui.clickgui.subcomponent.color.ColorPalette;
import net.minecraft.client.gui.DrawContext;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_0_ui_gird
 */
public class ColorButtonComponent extends GuiComponent {
    private ColorPalette colorPalette;

    public ColorButtonComponent(ColorPalette palette) {
        this.colorPalette = palette;
    }


    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {

    }
}
