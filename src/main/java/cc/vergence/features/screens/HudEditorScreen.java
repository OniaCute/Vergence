package cc.vergence.features.screens;

import cc.vergence.Vergence;
import cc.vergence.features.enums.MouseButtons;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class HudEditorScreen extends Screen {
    public HudEditorScreen()  {
        super(Text.of("VergenceClientHUDEditorScreen"));
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
        Vergence.EVENTS.onRenderHudEditor(context, mouseX, mouseY, partialTicks);
        Vergence.EVENTS.onMouseMoveInHudEditorScreen(context, mouseX, mouseY);
    }
}