package cc.vergence.ui.clickgui.option;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.options.impl.BindOption;
import cc.vergence.ui.GuiComponent;
import cc.vergence.util.font.FontUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_1_ui_gird
 */
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
    public void onDraw(double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        FontUtil.drawTextWithAlign(
                option.getDisplayName(),
                this.getX() + 2,
                this.getY(),
                getX() + this.getWidth() - 4,
                getY() + this.getHeight(),
                Vergence.THEME.getTheme().getOptionsTextColor(),
                FontSize.SMALL,
                Aligns.LEFT
        );

        for (GuiComponent component : getSubComponents()) {
            component.onDraw(mouseX, mouseY, clickLeft, clickRight);
        }
    }
}
