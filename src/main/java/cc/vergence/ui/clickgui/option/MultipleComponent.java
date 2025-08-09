package cc.vergence.ui.clickgui.option;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.ui.GuiComponent;
import cc.vergence.ui.clickgui.subcomponent.choice.MultipleChoicesComponent;
import cc.vergence.util.font.FontUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_1_ui_gird
 */
public class MultipleComponent extends GuiComponent {
    private MultipleOption<?> option;
    private MultipleChoicesComponent multipleChoicesComponent;

    public MultipleComponent(MultipleOption<?> option, MultipleChoicesComponent multipleChoicesComponent) {
        this.option = option;
        this.setParentComponent(multipleChoicesComponent);
    }

    public void setOption(MultipleOption<?> option) {
        this.option = option;
    }

    public MultipleOption<?> getOption() {
        return option;
    }

    @Override
    public void onDraw(double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        FontUtil.drawTextWithAlign(
                option.getDisplayName(),
                this.getX() + 2,
                this.getY() + 2,
                this.getX() - 4,
                this.getY() + this.getHeight(),
                Vergence.THEME.getTheme().getOptionsTextColor(),
                FontSize.SMALL,
                Aligns.LEFT
        );

        for (GuiComponent component : getSubComponents()) {
            component.onDraw(mouseX, mouseY, clickLeft, clickRight);
        }
    }
}
