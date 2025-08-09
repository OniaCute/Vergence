package cc.vergence.features.screens;

import cc.vergence.Vergence;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.modules.client.MainMenu;
import cc.vergence.util.color.ColorUtil;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.text.Text;
import oshi.util.tuples.Pair;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainMenuScreen extends Screen implements Wrapper {
    private boolean CLICKED;
    private double buttonOffset = 0;

    public MainMenuScreen() {
        super(Text.of("VergenceClientMainMenuScreen"));
    }

    public boolean shouldPause() {
        return false;
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            CLICKED = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            CLICKED = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // background
        Render2DUtil.drawRect(0, 0, width, height, ColorUtil.setAlpha(MainMenu.INSTANCE.backgroundColor.getValue(), 255));

        // Title
        FontUtil.drawTextWithAlign(
                MainMenu.INSTANCE.title.getValue(),
                0,
                0,
                width,
                height / 3.00,
                MainMenu.INSTANCE.titleColor.getValue(),
                FontSize.LARGEST,
                Aligns.CENTER,
                true
        );

        // buttons
        drawButton(context, mouseX, mouseY, "SinglePlayer");
        drawButton(context, mouseX, mouseY, "MultiPlayer");
        drawButton(context, mouseX, mouseY, "Options");
        drawButton(context, mouseX, mouseY, "Exit");

        // Client Info
        FontUtil.drawTextWithAlign(
                Vergence.NAME + " " + Vergence.VERSION + " / Minecraft 1.21.4",
                3, // padding
                0,
                width,
                height,
                MainMenu.INSTANCE.textColor.getValue(),
                FontSize.SMALLEST,
                Aligns.LEFT_BOTTOM
        );

        // Date
        String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + " " + new SimpleDateFormat("hh:mm:ss aa").format(new Date());
        FontUtil.drawTextWithAlign(
                date,
                0, // padding
                0,
                width - 3,
                height,
                MainMenu.INSTANCE.textColor.getValue(),
                FontSize.SMALLEST,
                Aligns.RIGHT_BOTTOM
        );

        // Out of date alert
        FontUtil.drawTextWithAlign(
                Vergence.OUT_OF_DATE ? "This version of client is out of the date\nGet the latest version in https://www.vergence.cc" : "",
                0,
                0,
                width,
                height,
                new Color(236, 8, 8),
                FontSize.SMALL,
                Aligns.BOTTOM
        );
        buttonOffset = 0;
    }

    private void drawButton(DrawContext context, int mouseX, int mouseY, String name) {
        Pair<Double, Double> pos = Render2DUtil.getAlignPositionAsPair(
                0,
                buttonOffset,
                width,
                height / 1.5,
                100,
                18,
                Aligns.CENTER
        );

        boolean hovered = isHovered(pos.getA(), pos.getB(), 100, 18, mouseX, mouseY);

        if (hovered && CLICKED) {
            Screen screen = getScreen(name);
            if (screen.equals(this)) {
                mc.scheduleStop();
                return ;
            }
            mc.setScreen(screen);
            CLICKED = false;
        }

        Render2DUtil.drawRoundedRect(
                pos.getA(),
                pos.getB(),
                100,
                18,
                4,
                hovered ? MainMenu.INSTANCE.buttonHoveredBackgroundColor.getValue() : MainMenu.INSTANCE.buttonBackgroundColor.getValue()
        );
        FontUtil.drawTextWithAlign(
                Vergence.TEXT.get("SCREEN.MainMenu.Buttons." + name),
                pos.getA(),
                pos.getB(),
                pos.getA() + 100,
                pos.getB() + 18,
                hovered ? MainMenu.INSTANCE.buttonHoveredTextColor.getValue() : MainMenu.INSTANCE.buttonTextColor.getValue(),
                FontSize.SMALL,
                Aligns.CENTER
        );

        buttonOffset += 50;
    }

    private boolean isHovered(double x, double y, double width, double height, double mouseX, double mouseY) {
        return x < mouseX && x + width > mouseX && y < mouseY && y + height > mouseY;
    }

    private Screen getScreen(String name) {
        switch (name) {
            case "SinglePlayer" -> {return new SelectWorldScreen(this);}
            case "MultiPlayer" -> {return new MultiplayerScreen(this);}
            case "Options" -> {return new OptionsScreen(this, mc.options);}
            default -> {return this;}
        }
    }
}
