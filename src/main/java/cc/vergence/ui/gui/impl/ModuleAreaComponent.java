package cc.vergence.ui.gui.impl;

import cc.vergence.Vergence;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.managers.GuiManager;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

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
