package cc.vergence.ui.gui.impl;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.options.impl.BindOption;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.util.font.FontUtil;
import net.minecraft.client.gui.DrawContext;

public class BindComponent extends GuiComponent {
    private BindOption option;

    public BindComponent(BindOption option) {
        this.option = option;
    }

    public void setOption(BindOption option) {
        this.option = option;
    }

    public BindOption getOption() {
        return option;
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        FontUtil.drawTextWithAlign(
                context,
                option.getDisplayName(),
                this.getX() + 2,
                this.getY(),
                getX() + this.getWidth() - 4,
                getY() + this.getHeight(),
                Aligns.LEFT,
                Vergence.THEME.getTheme().getOptionsTextColor(),
                FontSize.SMALL
        );

        for (GuiComponent component : getSubComponents()) {
            component.onDraw(context, mouseX, mouseY, clickLeft, clickRight);
        }
    }
}
