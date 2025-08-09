package cc.vergence.ui.configs.toolbar.subcomponent;

import cc.vergence.ui.GuiComponent;
import net.minecraft.client.gui.DrawContext;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_1_ui_gird
 */
public class ConfigToolbarButton extends GuiComponent {
    private String icon;
    private Runnable runnable;

    public ConfigToolbarButton(String icon, Runnable runnable) {
        this.icon = icon;
        this.runnable = runnable;
    }

    @Override
    public void onDraw(double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        

        if (isHovered(mouseX, mouseY) && clickLeft) {
            runnable.run();
        }
    }
}
