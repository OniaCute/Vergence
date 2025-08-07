package cc.vergence.modules.client;

import cc.vergence.Vergence;
import cc.vergence.features.managers.client.AnimationManager;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.features.screens.ClickGuiScreen;
import cc.vergence.modules.Module;
import cc.vergence.util.animations.ColorAnimation;
import cc.vergence.util.animations.GuiAnimation;
import net.minecraft.client.gui.DrawContext;

public class ClickGUI extends Module {
    public static ClickGUI INSTANCE;

    public ClickGUI() {
        super("ClickGUI", Category.CLIENT);
        INSTANCE = this;
    }

    public Option<String> title = addOption(new TextOption("Title", "Vergence", 10));
    public Option<Boolean> blurBackground = addOption(new BooleanOption("BlurBackground", false));
    public Option<Boolean> showOptionDescription = addOption(new BooleanOption("ShowOptionDescription", false));
    public Option<Boolean> searchIgnoreCase = addOption(new BooleanOption("SearchIgnoreCase", true));
    public Option<Boolean> searchForDescription = addOption(new BooleanOption("SearchForDescription", false));
    public Option<Boolean> rainbowSync = addOption(new BooleanOption("RainbowSync", false));
    public Option<Double> rainbowSpeed = addOption(new DoubleOption("RainbowSpeed", 1, 100, 40));
    public Option<Double> mainPageAnimationTime = addOption(new DoubleOption("MainPageAnimationTime", 500, 1100, 870));
    public Option<Double> moduleSpreadAnimationTime = addOption(new DoubleOption("ModuleSpreadAnimationTime", 200, 1200, 700));
    public Option<Double> optionsSpreadAnimationTime = addOption(new DoubleOption("OptionsSpreadAnimationTime", 200, 1300, 400));
    public Option<Double> descriptionSpreadAnimationTime = addOption(new DoubleOption("DescriptionSpreadAnimationTime", 0, 400, 200));
    public Option<Double> scrollAnimationTime = addOption(new DoubleOption("ScrollAnimationTime", 100, 250, 120));
    public Option<Double> scrollScale = addOption(new DoubleOption("ScrollScale", 1, 10, 3));
    public Option<Double> colorAnimationTime = addOption(new DoubleOption("ColorAnimationTime", 40, 250, 130));
    public Option<Boolean> advancedRenderer = addOption(new BooleanOption("AdvancedRenderer", true));

    @Override
    public String getDetails() {
        return title.getValue();
    }

    @Override
    public void onRegister() {
        this.getBind().setValue(344);
    }

    @Override
    public void onMouseMoveInClickGuiScreen(DrawContext context, double mouseX, double mouseY) {
        Vergence.GUI.onMouseMoveInClickGuiScreen(context, mouseX, mouseY);
    }

    @Override
    public void onTickAlways() {
        if (mc.currentScreen instanceof ClickGuiScreen) {
            ClickGUI.INSTANCE.enable();
        }  else {
            ClickGUI.INSTANCE.disable();
        }

        // Animation duration value sync
        for (ColorAnimation animation : AnimationManager.colorAnimations) {
            animation.setDuration(colorAnimationTime.getValue().intValue());
        }

        for (GuiAnimation animation : AnimationManager.descriptionAnimations) {
            animation.setDuration(descriptionSpreadAnimationTime.getValue().intValue());
        }
    }

    @Override
    public void onEnable() {
        if (HudEditor.INSTANCE.getStatus()) {
            HudEditor.INSTANCE.disable();
        }
        mc.setScreen(GuiManager.CLICK_GUI_SCREEN);
        GuiManager.ENTRY_ANIMATION.restart();
    }

    @Override
    public void onDisable() {
        mc.setScreen(null);
        GuiManager.mouseScrolledOffset = 0;
    }
}
