package cc.vergence.ui.clickgui.option;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.ui.GuiComponent;
import cc.vergence.util.font.FontUtil;
import net.minecraft.client.gui.DrawContext;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_1_ui_gird
 */
public class BooleanComponent extends GuiComponent {
    private BooleanOption option;

    public BooleanComponent(BooleanOption option) {
        this.option = option;
    }

    public void setOption(BooleanOption option) {
        this.option = option;
    }

    public BooleanOption getOption() {
        return option;
    }

    @Override
    public void onDraw(double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (!GuiManager.hoverComponentDrawing && isHovered(mouseX, mouseY)) {
            GuiManager.setCurrentComponent(this);
            if (Vergence.GUI.isAvailable(this)) {
                if (clickLeft) {
                    GuiManager.CLICKED_LEFT = false;
                }
                else if (clickRight) {
                    GuiManager.CLICKED_RIGHT = false;
                }
            }
        } else {
            GuiManager.setCurrentComponent(null);
        }

//        Render2DUtil.drawRoundedRect(
//                context.getMatrices(),
//                this.x,
//                this.y,
//                this.width,
//                this.height,
//                1,
//                new Color(255, 183, 0)
//        );

        FontUtil.drawTextWithAlign (
                this.option.getDisplayName(),
                this.x + 2,
                this.y + 2,
                this.x + this.width - 6,
                this.y + this.getHeight(),
                Vergence.THEME.getTheme().getOptionsTextColor(),
                FontSize.SMALL,
                Aligns.LEFT
        );

        for (GuiComponent component : getSubComponents()) {
            component.onDraw(mouseX, mouseY, clickLeft, clickRight);
        }
    }
}
