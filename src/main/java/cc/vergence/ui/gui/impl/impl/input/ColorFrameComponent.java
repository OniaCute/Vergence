package cc.vergence.ui.gui.impl.impl.input;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.util.color.HexColor;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.SelectionManager;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ColorFrameComponent extends GuiComponent implements Wrapper {
    private ColorOption option;
    private FastTimerUtil animationTimer;
    private boolean isListening;
    private boolean showSuffixChar;
    private String inputCache = "";
    private Color oldValue;

    public ColorFrameComponent(ColorOption option) {
        this.option = option;
        this.animationTimer = new FastTimerUtil();
        this.inputCache = new HexColor(option.getValue()).getValue();
        this.oldValue = option.getValue();
        GuiManager.inputComponents.add(this);
    }

    public ColorOption getOption() {
        return option;
    }

    public void setOption(ColorOption option) {
        this.option = option;
    }

    public boolean isListening() {
        return isListening;
    }

    public void setListening(boolean listening) {
        if (this.isListening == listening) return;

        this.isListening = listening;

        if (listening) {
            oldValue = option.getValue();
            inputCache = new HexColor(oldValue).getValue();
        } else {
            applyInput();
        }
    }

    private void applyInput() {
        try {
            HexColor hex = new HexColor(inputCache);
            Color newColor = hex.getValueAsColor();
            if (newColor != oldValue) {
                option.setValue(newColor);
            }
        } catch (Exception e) {
            Vergence.CONSOLE.logWarn("A incorrect value: " + inputCache);
        }
        inputCache = new HexColor(option.getValue()).getValue();
    }

    public void keyType(int keyCode) {
        switch (keyCode) {
            case GLFW.GLFW_KEY_V -> {
                if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL)) {
                    String clip = SelectionManager.getClipboard(mc);
                    if (clip != null) {
                        for (char c : clip.toCharArray()) charType(c);
                    }
                }
            }
            case GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> setListening(false);
            case GLFW.GLFW_KEY_BACKSPACE -> {
                if (!inputCache.isEmpty()) inputCache = inputCache.substring(0, inputCache.length() - 1);
            }
        }
    }

    public void charType(char c) {
        if (Character.isLetterOrDigit(c) || c == '#') {
            if (inputCache.length() < 9) {
                inputCache += c;
            }
        }
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        if (option.isRainbow()) {
            this.setListening(false);
        }

        if (animationTimer.passedMs(520)) {
            animationTimer.reset();
            showSuffixChar = !showSuffixChar;
        }

        if (clickLeft) {
            if (isHovered(mouseX, mouseY)) {
                if (!isListening) {
                    setListening(true);
                    animationTimer.reset();
                    showSuffixChar = true;
                    GuiManager.CLICKED_LEFT = false;
                }
            } else {
                if (isListening) {
                    setListening(false);
                    GuiManager.CLICKED_LEFT = false;
                }
            }
        }

        if (clickRight && isListening) {
            setListening(false);
            GuiManager.CLICKED_LEFT = false;
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

        String display = isListening ? inputCache + (showSuffixChar ? "_" : "") : new HexColor(option.getValue()).getValue();
        FontUtil.drawTextWithAlign(
                context,
                display,
                this.getX() + 2,
                this.getY() + 4,
                this.getX() + this.getWidth() - 4,
                this.getY() + this.getHeight(),
                Aligns.LEFT,
                isHovered(mouseX, mouseY) ? Vergence.THEME.getTheme().getInputFrameHoveredTextColor() : Vergence.THEME.getTheme().getInputFrameTextColor(),
                FontSize.SMALLEST
        );
    }
}
