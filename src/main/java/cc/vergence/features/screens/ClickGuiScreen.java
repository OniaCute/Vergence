package cc.vergence.features.screens;

import cc.vergence.Vergence;
import cc.vergence.features.enums.MouseButtons;
import cc.vergence.features.managers.GuiManager;
import cc.vergence.modules.client.ClickGUI;
import cc.vergence.ui.gui.GuiComponent;
import cc.vergence.ui.gui.impl.CategoryComponent;
import cc.vergence.ui.gui.impl.impl.input.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClickGuiScreen extends Screen {
    public ClickGuiScreen() {
        super(Text.of("VergenceClientClickGuiScreen"));
    }

    public boolean LEFT_CLICKED = false; // Mouse 1 (0)
    public boolean RIGHT_CLICKED = false; // Mouse 2 (1)
    public boolean CENTER_CLICKED = false; // Mouse 3 (2)
    public boolean FLANK_FRONT_CLICKED = false; // Mouse 5 (4)
    public boolean FLANK_BACK_CLICKED = false; // mouse 4 (3)
    public boolean MOUSE_CLICKED = false; // However, this var always active in mouse click with any "Click type".

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        MOUSE_CLICKED = true;
        if (button == 0) {
            LEFT_CLICKED = true;
            Vergence.EVENTS.onMouseClick(mouseX, mouseY, this, MouseButtons.LEFT);
        }
        else if (button == 1) {
            RIGHT_CLICKED = true;
            Vergence.EVENTS.onMouseClick(mouseX, mouseY, this, MouseButtons.RIGHT);
        }
        else if (button == 2) {
            CENTER_CLICKED = true;
            Vergence.EVENTS.onMouseClick(mouseX, mouseY, this, MouseButtons.CENTER);
        }
        else if (button == 3) {
            FLANK_FRONT_CLICKED = true;
            Vergence.EVENTS.onMouseClick(mouseX, mouseY, this, MouseButtons.FLANK_FRONT);
        }
        else if (button == 4) {
            FLANK_BACK_CLICKED = true;
            Vergence.EVENTS.onMouseClick(mouseX, mouseY, this, MouseButtons.FLANK_BACK);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            LEFT_CLICKED = false;
            Vergence.EVENTS.onMouseRelease(mouseX, mouseY, this, MouseButtons.LEFT);
        }
        else if (button == 1) {
            RIGHT_CLICKED = false;
            Vergence.EVENTS.onMouseRelease(mouseX, mouseY, this, MouseButtons.RIGHT);
        }
        else if (button == 2) {
            CENTER_CLICKED = false;
            Vergence.EVENTS.onMouseRelease(mouseX, mouseY, this, MouseButtons.CENTER);
        }
        else if (button == 3) {
            FLANK_FRONT_CLICKED = false;
            Vergence.EVENTS.onMouseRelease(mouseX, mouseY, this, MouseButtons.FLANK_FRONT);
        }
        else if (button == 4) {
            FLANK_BACK_CLICKED = false;
            Vergence.EVENTS.onMouseRelease(mouseX, mouseY, this, MouseButtons.FLANK_BACK);
        }
        if (!LEFT_CLICKED && !RIGHT_CLICKED && !CENTER_CLICKED && !FLANK_FRONT_CLICKED && !FLANK_BACK_CLICKED) {
            MOUSE_CLICKED = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        LEFT_CLICKED = false;
        RIGHT_CLICKED = false;
        CENTER_CLICKED = false;
        FLANK_FRONT_CLICKED = false;
        FLANK_BACK_CLICKED = false;
        MOUSE_CLICKED = false;
        super.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        if (ClickGUI.INSTANCE != null && ClickGUI.INSTANCE.blurBackground.getValue()) {
            applyBlur();
        }
        Vergence.EVENTS.onRenderClickGui(context, mouseX, mouseY, partialTicks);
        Vergence.EVENTS.onMouseMoveInClickGuiScreen(context, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (GuiComponent component : GuiManager.inputComponents) {
            if (component instanceof TextFrameComponent) {
                if (((TextFrameComponent) component).isListening()) {
                    ((TextFrameComponent) component).keyType(keyCode);
                }
            }
            else if (component instanceof BindFrameComponent) {
                if (((BindFrameComponent) component).isListening()) {
                    ((BindFrameComponent) component).typeIn(keyCode);
                }
            }
            else if (component instanceof DoubleFrameComponent) {
                if (((DoubleFrameComponent) component).isListening()) {
                    ((DoubleFrameComponent) component).keyType(keyCode);
                }
            }
            else if (component instanceof ColorFrameComponent) {
                if (((ColorFrameComponent) component).isListening()) {
                    ((ColorFrameComponent) component).keyType(keyCode);
                }
            }
            else if (component instanceof SearchFrameComponent) {
                if (((SearchFrameComponent) component).isListening()) {
                    ((SearchFrameComponent) component).keyType(keyCode);
                }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (GuiComponent.isHovering(
                GuiManager.MAIN_PAGE_X,
                GuiManager.MAIN_PAGE_Y,
                GuiManager.MAIN_PAGE_WIDTH,
                GuiManager.MAIN_PAGE_HEIGHT,
                mouseX,
                mouseY
        )) {
            GuiManager.scrollAnimation.to(GuiManager.scrollAnimation.getTarget() + verticalAmount * (ClickGUI.INSTANCE != null ? ClickGUI.INSTANCE.scrollScale.getValue() : 3));
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
