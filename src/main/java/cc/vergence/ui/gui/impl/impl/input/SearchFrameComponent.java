package cc.vergence.ui.gui.impl.impl.input;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.SelectionManager;
import org.lwjgl.glfw.GLFW;

public class SearchFrameComponent extends GuiComponent implements Wrapper {
    public String searchText = "";
    private final FastTimerUtil animationTimer;
    private boolean showSuffixChar;
    private boolean isListening;

    public SearchFrameComponent() {
        animationTimer = new FastTimerUtil();
        GuiManager.inputComponents.add(this);
    }

    public boolean isListening() {
        return isListening;
    }

    public void setListening(boolean listening) {
        this.isListening = listening;
    }

    public void keyType(int keyCode) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_V -> {
                if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL)) {
                    String clipboard = SelectionManager.getClipboard(mc);
                    searchText += clipboard;
                }
            }
            case GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                isListening = false;
            }
            case GLFW.GLFW_KEY_BACKSPACE -> {
                searchText = removeLastChar(searchText);
            }
        }
    }

    public void charType(char word) {
        searchText += word;
    }

    public static String removeLastChar(String string) {
        return (string != null && !string.isEmpty()) ? string.substring(0, string.length() - 1) : "";
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (isListening()) {
            GuiManager.currentCategory = null;
        }

        if (animationTimer.passedMs(520)) {
            animationTimer.reset();
            showSuffixChar = !showSuffixChar;
        }

        if (isHovered(mouseX, mouseY)) {
            if (clickLeft) {
                isListening = !isListening;
                GuiManager.CLICKED_LEFT = false;

                if (isListening) {
                    animationTimer.reset();
                    showSuffixChar = true;
                }
            }
        } else {
            if (clickLeft && isListening) {
                isListening = false;
            }
        }
        if (clickRight && isListening) {
            isListening = false;
        }

        Render2DUtil.drawRoundedRectWithAlign(
                context.getMatrices(),
                getX(),
                getY(),
                getX() + getWidth(),
                getY() + getHeight(),
                getWidth(),
                getHeight(),
                4 * Render2DUtil.getScaleFactor(),
                isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getInputFrameHoveredBackgroundColor() : Vergence.THEME.getTheme().getInputFrameBackgroundColor(),
                Aligns.LEFT
        );

        FontUtil.drawTextWithAlign(
                context,
                searchText + (showSuffixChar && isListening ? "_" : ""),
                getX() + 2,
                getY() + 4,
                getX() + getWidth() - 4,
                getY() + getHeight(),
                Aligns.LEFT,
                isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getInputFrameHoveredTextColor() : Vergence.THEME.getTheme().getInputFrameTextColor(),
                FontSize.SMALLEST
        );
    }
}
