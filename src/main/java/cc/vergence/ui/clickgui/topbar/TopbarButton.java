package cc.vergence.ui.clickgui.topbar;

import cc.vergence.Vergence;
import cc.vergence.features.enums.client.Pages;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.ui.GuiComponent;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

public class TopbarButton extends GuiComponent {
    private Pages targetPage;
    private String iconString;

    public TopbarButton(Pages page, String icon) {
        this.targetPage = page;
        this.iconString = icon;
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (clickLeft && isHovered(mouseX, mouseY)) {
            GuiManager.SEARCH.searchText = "";
            GuiManager.SEARCH.setListening(false);
            GuiManager.currentCategory = null;
            GuiManager.PAGE = targetPage;
            GuiManager.CLICKED_LEFT = false;
            GuiManager.resetScroll();
        }

        Render2DUtil.drawRoundedRect(
                context.getMatrices(),
                getX(),
                getY(),
                getWidth(),
                getHeight(),
                4,
                isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getTopbarHoveredButtonBackgroundColor(): Vergence.THEME.getTheme().getTopbarButtonBackgroundColor()
        );

        FontUtil.drawIconWithAlign(
                context,
                iconString,
                getX(),
                getY() + 4.5,
                getX() + getWidth(),
                getY() + getHeight(),
                Aligns.CENTER,
                isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getTopbarHoveredButtonIconColor(): Vergence.THEME.getTheme().getTopbarButtonIconColor(),
                FontSize.MEDIUM
        );
    }
}
