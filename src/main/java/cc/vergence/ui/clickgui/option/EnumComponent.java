package cc.vergence.ui.clickgui.option;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.ui.GuiComponent;
import cc.vergence.ui.clickgui.subcomponent.choice.EnumChoicesComponent;
import cc.vergence.util.font.FontUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_0_ui_gird
 */
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
