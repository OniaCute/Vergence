package cc.vergence.ui.clickgui.module;

import cc.vergence.Vergence;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.ui.GuiComponent;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_0_ui_gird
 */
public class ModuleAreaComponent extends GuiComponent {
    public ModuleAreaComponent(ModuleComponent moduleComponent) {
        this.setParentComponent(moduleComponent);
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (isHovered(mouseX, mouseY)) {
            GuiManager.setCurrentComponent(this);
            if (clickLeft) {
                if (Vergence.GUI.isAvailable(this)) {
                    GuiManager.CLICKED_LEFT = false;
                }
            }
        } else {
            GuiManager.setCurrentComponent(null);
        }

        Render2DUtil.drawRoundedRect(
                context.getMatrices(),
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight(),
                4,
                Vergence.THEME.getTheme().getOptionAreaBackgroundColor()
        );

        for (GuiComponent component : this.getSubComponents()) {
            if (GuiManager.shouldDisplayOptionComponent(component)) {
                component.onDraw(context, mouseX, mouseY, clickLeft, clickRight);
            }
        }
    }
}
