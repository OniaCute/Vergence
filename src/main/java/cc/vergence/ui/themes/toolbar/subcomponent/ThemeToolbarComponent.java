package cc.vergence.ui.themes.toolbar.subcomponent;

import cc.vergence.ui.GuiComponent;
import net.minecraft.client.gui.DrawContext;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_1_ui_gird
 */
public class ThemeToolbarComponent extends GuiComponent {
    private String icon;
    private Runnable runnable;

    public ThemeToolbarComponent(String icon, Runnable runnable) {
        this.icon = icon;
        this.runnable = runnable;
    }

    @Override
    public void onDraw(double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {

    }
}
