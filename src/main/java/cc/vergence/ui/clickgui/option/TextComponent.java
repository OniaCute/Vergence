package cc.vergence.ui.clickgui.option;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.ui.GuiComponent;
import cc.vergence.util.font.FontUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_1_ui_gird
 */
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
    public void onDraw(double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        FontUtil.drawTextWithAlign(
                option.getDisplayName(),
                this.getX() + 2,
                this.getY() + 2,
                getX() + this.getWidth() - 4,
                getY() + this.getHeight() + 1,
                Vergence.THEME.getTheme().getOptionsTextColor(),
                FontSize.SMALL,
                Aligns.LEFT
        );

        for (GuiComponent component : getSubComponents()) {
            component.onDraw(mouseX, mouseY, clickLeft, clickRight);
        }
    }
}
