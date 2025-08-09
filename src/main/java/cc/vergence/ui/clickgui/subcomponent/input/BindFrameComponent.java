package cc.vergence.ui.clickgui.subcomponent.input;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.options.impl.BindOption;
import cc.vergence.ui.GuiComponent;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_1_ui_gird
 */
public class BindFrameComponent extends GuiComponent implements Wrapper {
    private BindOption option;
    private FastTimerUtil animationTimer;
    private boolean showSuffixChar;
    private boolean isListening;

    public void setOption(BindOption option) {
        this.option = option;
    }

    public BindOption getOption() {
        return option;
    }

    public BindFrameComponent(BindOption option) {
        this.option = option;
        animationTimer = new FastTimerUtil();
        GuiManager.inputComponents.add(this);
    }

    public boolean isListening() {
        return isListening;
    }

    public void setListening(boolean listening) {
        isListening = listening;
    }

    public void typeIn(int key) {
        switch (key) {
            case GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                isListening = false;
            }
            case GLFW.GLFW_KEY_BACKSPACE, GLFW.GLFW_KEY_DELETE -> {
                option.setValue(-1);
                isListening = false;
            }
            case GLFW.GLFW_KEY_LEFT_SHIFT -> {
                return ;
            }
            default -> {
                if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
                    option.setValue(key);
                    option.setNeedShift(true);
                    isListening = false;
                } else {
                    option.setValue(key);
                    isListening = false;
                }
            }
        }
    }

    @Override
    public void onDraw(double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (animationTimer.passedMs(520)) { // suffix animation
            animationTimer.reset();
            showSuffixChar = !showSuffixChar;
        }

        if (isHovered(mouseX, mouseY)){
            if (clickLeft) {
                this.isListening = !this.isListening;
                GuiManager.CLICKED_LEFT = false;
            }
        } else {
            if (clickLeft && isListening) {
                this.isListening = false;
                GuiManager.CLICKED_LEFT = false;
            }
        }
        if (clickRight && isListening) {
            this.isListening = false;
            GuiManager.CLICKED_RIGHT = false;
        }

        Render2DUtil.drawRoundedRect(
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight(),
                4,
                isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getInputFrameHoveredBackgroundColor() : Vergence.THEME.getTheme().getInputFrameBackgroundColor()
        );

        FontUtil.drawTextWithAlign(
                (option.isNeedShift() ? "Shift+" : "") + option.getBindChar() + (this.showSuffixChar && this.isListening() ? "_" : ""),
                this.getX() + 2,
                this.getY() + 4,
                this.getX() + this.getWidth() - 4,
                this.getY() + this.getHeight(),
                isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getInputFrameHoveredTextColor() : Vergence.THEME.getTheme().getInputFrameTextColor(),
                FontSize.SMALLEST,
                Aligns.CENTER
        );
    }
}
