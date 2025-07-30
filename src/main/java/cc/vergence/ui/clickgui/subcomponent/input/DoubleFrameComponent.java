package cc.vergence.ui.clickgui.subcomponent.input;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.options.impl.DoubleOption;
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
public class DoubleFrameComponent extends GuiComponent implements Wrapper {
    private final DoubleOption option;
    private final FastTimerUtil cursorTimer = new FastTimerUtil();
    private boolean cursorVisible = true;
    private boolean isListening = false;
    private String inputCache = "";

    public DoubleFrameComponent(DoubleOption option) {
        this.option = option;
        this.inputCache = String.valueOf(option.getValue());
        GuiManager.inputComponents.add(this);
    }

    public boolean isListening() {
        return isListening;
    }

    public void setListening(boolean listening) {
        if (!this.isListening && listening) {
            inputCache = String.valueOf(option.getValue());
            cursorTimer.reset();
            cursorVisible = true;
        }
        this.isListening = listening;
    }

    private void applyInput() {
        try {
            double parsed = Double.parseDouble(inputCache);
            if (Math.abs(parsed - option.getValue()) > 1e-6) {
                if (parsed >= option.getMaxValue()) {
                    option.setValue(option.getMaxValue());
                } else {
                    option.setValue(Math.max(parsed, option.getMinValue()));
                }
            }
        } catch (NumberFormatException e) {
            Vergence.CONSOLE.logWarn("A incorrect value: " + inputCache);
        }
        inputCache = String.valueOf(option.getValue());
    }

    public void keyType(int keyCode) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_V -> {
                if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL)) {
                    String clip = SelectionManager.getClipboard(mc);
                    if (clip != null) {
                        for (char ch : clip.toCharArray()) {
                            charType(ch);
                        }
                    }
                }
            }
            case GLFW.GLFW_KEY_ESCAPE -> setListening(false);
            case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                applyInput();
                setListening(false);
            }
            case GLFW.GLFW_KEY_BACKSPACE -> {
                if (!inputCache.isEmpty()) {
                    inputCache = inputCache.substring(0, inputCache.length() - 1);
                }
            }
        }
    }

    public void charType(char c) {
        if (Character.isDigit(c) || c == '.' || c == '-') {
            if (c == '.' && inputCache.contains(".")) return;
            if (c == '-' && !inputCache.isEmpty()) return;
            inputCache += c;
        }
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        Render2DUtil.pushDisplayArea(
                context.getMatrices(),
                (float) getX(),
                (float) getY(),
                (float) (getX() + getWidth()),
                (float) (getY() + getHeight()),
                1d
        );

        if (cursorTimer.passedMs(520)) {
            cursorVisible = !cursorVisible;
            cursorTimer.reset();
        }

        if (clickLeft) {
            if (isHovered(mouseX, mouseY)) {
                if (!isListening) {
                    setListening(true);
                }
                GuiManager.CLICKED_LEFT = false;
            } else {
                if (isListening) {
                    setListening(false);
                }
            }
        }

        if (clickRight && isListening) {
            setListening(false);
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
                isHovered(mouseX, mouseY)
                        ? Vergence.THEME.getTheme().getInputFrameHoveredBackgroundColor()
                        : Vergence.THEME.getTheme().getInputFrameBackgroundColor(),
                Aligns.LEFT
        );

        String displayText = isListening
                ? inputCache + (cursorVisible ? "_" : "")
                : String.valueOf(option.getValueAsText());

        FontUtil.drawTextWithAlign(
                context,
                displayText,
                this.getX() + 2,
                this.getY() + 4,
                this.getX() + this.getWidth() - 4,
                this.getY() + this.getHeight(),
                Aligns.CENTER,
                isHovered(mouseX, mouseY)
                        ? Vergence.THEME.getTheme().getInputFrameHoveredTextColor()
                        : Vergence.THEME.getTheme().getInputFrameTextColor(),
                FontSize.SMALLEST
        );

        for (GuiComponent component : getSubComponents()) {
            component.onDraw(context, mouseX, mouseY, clickLeft, clickRight);
        }

        Render2DUtil.popDisplayArea();
    }
}
