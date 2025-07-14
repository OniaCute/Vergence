package cc.vergence.ui.gui.impl;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.util.font.FontUtil;
import net.minecraft.client.gui.DrawContext;

public class TextComponent extends GuiComponent {
    private TextOption option;

    public TextComponent(TextOption option) {
        this.option = option;
    }

    public void setOption(TextOption option) {
        this.option = option;
    }

    public TextOption getOption() {
        return option;
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        FontUtil.drawTextWithAlign(
                context,
                option.getDisplayName(),
                this.getX() + 2,
                this.getY() + 2,
                getX() + this.getWidth() - 4,
                getY() + this.getHeight() + 1,
                Aligns.LEFT,
                Vergence.THEME.getTheme().getOptionsTextColor(),
                FontSize.SMALL
        );

        for (GuiComponent component : getSubComponents()) {
            component.onDraw(context, mouseX, mouseY, clickLeft, clickRight);
        }
    }
}
