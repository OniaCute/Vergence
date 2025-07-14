package cc.vergence.modules.client;

import cc.vergence.Vergence;
import cc.vergence.features.enums.AntiCheats;
import cc.vergence.features.managers.GuiManager;
import cc.vergence.features.managers.HudManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.screens.ClickGuiScreen;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.modules.Module;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class HudEditor extends Module {
    public static HudEditor INSTANCE;
    public HudEditor() {
        super("HudEditor", Category.CLIENT);
        INSTANCE = this;
    }

    public Option<Color> textColor = addOption(new ColorOption("TextColor", new Color(26, 26, 26)));
    public Option<Boolean> outline = addOption(new BooleanOption("Outline", true));
    public Option<Color> outlineColor = addOption(new ColorOption("OutlineColor", new Color(59, 59, 59), v -> outline.getValue()));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onMouseMoveInHudEditorScreen(DrawContext context, double mouseX, double mouseY) {
        Vergence.HUD.onMouseMoveInHudEditorScreen(context, mouseX, mouseY);
    }

    @Override
    public void onTickAlways() {
        if (mc.currentScreen instanceof HudEditorScreen) {
            HudEditor.INSTANCE.enable();
        }  else {
            HudEditor.INSTANCE.disable();
        }
    }

    @Override
    public void onEnable() {
        if (ClickGUI.INSTANCE.getStatus()) {
            ClickGUI.INSTANCE.disable();
        }
        mc.setScreen(HudManager.HUD_EDITOR_SCREEN);
    }

    @Override
    public void onDisable() {
        mc.setScreen(null);
    }
}
