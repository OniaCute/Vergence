package cc.vergence.features.managers;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.enums.MouseButtons;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.modules.client.ClickGUI;
import cc.vergence.modules.client.HudEditor;
import cc.vergence.util.font.FontUtil;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import java.awt.*;
import java.util.ArrayList;

public class HudManager implements Wrapper {
    public static final HudEditorScreen HUD_EDITOR_SCREEN = new HudEditorScreen();
    public static ArrayList<Module> hudList = new ArrayList<Module>();
    public static Module currentHud = null;
    public static double MOUSE_X = 0;
    public static double MOUSE_Y = 0;
    public static boolean CLICKED_LEFT = false;
    public static boolean CLICKED_RIGHT = false;

    public void onDraw2D(DrawContext context, float tickDelta) {
        if (mc.currentScreen instanceof HudEditorScreen) {
            drawHudEditor(context, tickDelta);
        }
    }

    public void onMouseClick(double mouseX, double mouseY, Screen screen, MouseButtons button) {
        if (button.equals(MouseButtons.LEFT)) {
            CLICKED_LEFT = true;
        }
        else if (button.equals(MouseButtons.RIGHT)) {
            CLICKED_RIGHT = true;
        }
    }

    public void onMouseRelease(double mouseX, double mouseY, Screen screen, MouseButtons button) {
        if (button.equals(MouseButtons.LEFT)) {
            CLICKED_LEFT = false;
        }
        else if (button.equals(MouseButtons.RIGHT)) {
            CLICKED_RIGHT = false;
        }
        for (Module module : hudList) {
            module.onMouseRelease(mouseX, mouseY, screen, button);
        }
    }

    public void onMouseMoveInHudEditorScreen(DrawContext context, double mouseX, double mouseY) {
        MOUSE_X = mouseX;
        MOUSE_Y = mouseY;
    }

    private void drawHudEditor(DrawContext context, double tickDelta) {
        FontUtil.drawTextWithAlign(
                context,
                "LEFT - Move Position",
                0,
                0,
                mc.currentScreen.width,
                mc.currentScreen.height,
                Aligns.LEFT_BOTTOM,
                Vergence.THEME.getTheme().getHudEditorTipsTextColor(),
                FontSize.SMALL
        );

        if (currentHud != null && (HudEditor.INSTANCE != null ? HudEditor.INSTANCE.outline.getValue() : false)) {
            Render2DUtil.drawRectOutline(
                    context,
                    currentHud.getX(),
                    currentHud.getY(),
                    currentHud.getWidth(),
                    currentHud.getHeight(),
                    1,
                    (HudEditor.INSTANCE != null ? HudEditor.INSTANCE.outlineColor.getValue() : new Color(0, 0, 0))
            );
            FontUtil.drawText(
                    context,
                    currentHud.getDisplayName(),
                    currentHud.getX(),
                    currentHud.getY() + FontUtil.getHeight(FontSize.SMALLEST) + FontUtil.getHeight(FontSize.SMALLEST),
                    (HudEditor.INSTANCE != null ? HudEditor.INSTANCE.textColor.getValue() : new Color(0, 0, 0)),
                    FontSize.SMALLEST
            );
        }
    }
}
