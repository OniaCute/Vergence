package cc.vergence.ui.clickgui.subcomponent.input;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.ui.GuiComponent;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.SelectionManager;
import org.lwjgl.glfw.GLFW;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_0_ui_gird
 */
public class TextFrameComponent extends GuiComponent implements Wrapper {
    private TextOption option;
    private FastTimerUtil animationTimer;
    private boolean showSuffixChar;
    private boolean isListening;

    public TextFrameComponent(TextOption option) {
        this.option = option;
        animationTimer = new FastTimerUtil();
        GuiManager.inputComponents.add(this);
    }

    public boolean isListening() {
        return this.option.isEditable() && isListening;
    }

    public void setListening(boolean listening) {
        isListening = listening;
    }

    public void keyType(int keyCode) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_V -> {
                if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL)) {
                    if (option.getValue().length() + SelectionManager.getClipboard(mc).length() >= option.sizeLimit && option.sizeLimit != -1) {
                        return ;
                    }
                    option.setValue(option.getRawValue() + SelectionManager.getClipboard(mc));
                }
            }
            case GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                isListening = false;
            }
            case GLFW.GLFW_KEY_BACKSPACE -> {
                option.setValue(removeLastChar(option.getRawValue()));
            }
        }
    }

    public void charType(char word) {
        if (option.getRawValue().length() >= option.sizeLimit && option.sizeLimit != -1) {
            return ;
        }
        option.setValue(option.getRawValue() + word);
    }

    public static String removeLastChar(String string) {
        String output = "";
        if (string != null && !string.isEmpty()) {
            output = string.substring(0, string.length() - 1);
        }
        return output;
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        Render2DUtil.pushDisplayArea(
                context.getMatrices(),
                (float) getX(),
                (float) getY(),
                (float) (getX() + getWidth()),
                (float) (getY() + getHeight() + 3),
                1d
        );

        if (animationTimer.passedMs(520)) { // suffix animation
            animationTimer.reset();
            showSuffixChar = !showSuffixChar;
        }

        if (isHovered(mouseX, mouseY)){
            if (clickLeft) {
                this.isListening = !this.isListening;
                GuiManager.CLICKED_LEFT = false;

                if (this.isListening) {
                    animationTimer.reset();
                    showSuffixChar = true;
                }
            }
        } else {
            if (clickLeft && isListening) {
                this.isListening = false;
            }
        }
        if (clickRight && isListening) {
            this.isListening = false;
        }

        Render2DUtil.drawRoundedRectWithAlign(
                context.getMatrices(),
                this.getX(),
                this.getY(),
                this.getX() + this.getParentComponent().getWidth(),
                this.getY() + this.getParentComponent().getHeight(),
                this.getWidth(),
                this.getHeight(),
                4 * Render2DUtil.getScaleFactor(),
                isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getInputFrameHoveredBackgroundColor() : Vergence.THEME.getTheme().getInputFrameBackgroundColor(),
                Aligns.LEFT
        );

        FontUtil.drawTextWithAlign(
                context,
                option.getRawValue() + (this.showSuffixChar && this.isListening() ? "_" : ""),
                this.getX() + 2,
                this.getY() + 4,
                this.getX() + this.getWidth() - 4,
                this.getY() + this.getHeight(),
                Aligns.LEFT,
                isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getInputFrameHoveredTextColor() : Vergence.THEME.getTheme().getInputFrameTextColor(),
                FontSize.SMALLEST
        );

        Render2DUtil.popDisplayArea();
    }
}
