package cc.vergence.features.managers.ui;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.enums.client.MouseButtons;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.modules.client.HudEditor;
import cc.vergence.util.font.FontUtil;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import oshi.util.tuples.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HudManager implements Wrapper {
    public static final HudEditorScreen HUD_EDITOR_SCREEN = new HudEditorScreen();
    public static ArrayList<Module> hudList = new ArrayList<Module>();
    public static Module currentHud = null;
    public static double MOUSE_X = 0;
    public static double MOUSE_Y = 0;
    public static boolean CLICKED_LEFT = false;
    public static boolean CLICKED_RIGHT = false;

    public void onRenderHudEditor(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        if (!HudEditor.INSTANCE.getStatus()) {
            return ;
        }
        if (mc.currentScreen instanceof HudEditorScreen) {
            drawHudEditor(context, partialTicks);
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
        if (mc.getWindow() == null) {
            return ;
        }

        FontUtil.drawTextWithAlign(
                context,
                "LEFT - Move Position",
                0,
                0,
                mc.getWindow().getScaledWidth(),
                mc.getWindow().getScaledHeight(),
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
                    currentHud.getY() + currentHud.getHeight() + 3,
                    (HudEditor.INSTANCE != null ? HudEditor.INSTANCE.textColor.getValue() : new Color(0, 0, 0)),
                    FontSize.SMALLEST
            );
        }
    }
}
