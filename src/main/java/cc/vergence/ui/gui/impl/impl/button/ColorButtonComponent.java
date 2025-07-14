package cc.vergence.ui.gui.impl.impl.button;

import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.ui.gui.impl.impl.color.ColorPalette;
import net.minecraft.client.gui.DrawContext;

public class ColorButtonComponent extends GuiComponent {
    private ColorPalette colorPalette;

    public ColorButtonComponent(ColorPalette palette) {
        this.colorPalette = palette;
    }


    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {

    }
}
