package cc.vergence.ui.gui.impl;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.ui.gui.impl.impl.choice.EnumChoicesComponent;
import cc.vergence.util.font.FontUtil;
import net.minecraft.client.gui.DrawContext;

public class EnumComponent extends GuiComponent {
    private EnumOption option;

    public EnumComponent(EnumOption option, EnumChoicesComponent enumChoicesComponent) {
        this.option = option;
        this.setParentComponent(enumChoicesComponent);
    }

    public void setOption(EnumOption option) {
        this.option = option;
    }

    public EnumOption getOption() {
        return option;
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        FontUtil.drawTextWithAlign(
                context,
                option.getDisplayName(),
                this.getX() + 2,
                this.getY() + 2,
                this.getX() - 4,
                this.getY() + this.getHeight(),
                Aligns.LEFT,
                Vergence.THEME.getTheme().getOptionsTextColor(),
                FontSize.SMALL
        );

        for (GuiComponent component : getSubComponents()) {
            component.onDraw(context, mouseX, mouseY, clickLeft, clickRight);
        }
    }
}
