package cc.vergence.ui.gui.impl;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.ui.gui.impl.impl.choice.EnumChoicesComponent;
import cc.vergence.ui.gui.impl.impl.choice.MultipleChoicesComponent;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

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
