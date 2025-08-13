package cc.vergence.modules.hud;

import cc.vergence.features.enums.client.MouseButtons;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.managers.ui.HudManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.*;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.modules.Module;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.NewRender2DUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.awt.*;

public class PotionHud extends Module {
    public static PotionHud INSTANCE;
    private boolean lastMouseStatus = false;
    private double lastMouseX = -1;
    private double lastMouseY = -1;

    public PotionHud() {
        super("PotionHud", Category.HUD);
        INSTANCE = this;
        setX(0);
        setY(0);
        setWidth(0);
        setHeight(0);
    }

    public Option<String> title = addOption(new TextOption("title", "Potions"));
    public Option<Enum<?>> titleAlign = addOption(new EnumOption("TitleAlign", Aligns.Center));
    public Option<String> infinityTime = addOption(new TextOption("InfinityTime", "**:**"));
    public Option<Color> titleColor = addOption(new ColorOption("TitleColor", new Color(241, 137, 255)));
    public Option<Boolean> customPotionColor = addOption(new BooleanOption("CustomPotionColor", false));
    public Option<Color> potionColor = addOption(new ColorOption("PotionColor", new Color(0, 0, 0), v -> customPotionColor.getValue()));
    public Option<Color> potionTimeColor = addOption(new ColorOption("PotionTimeColor", new Color(38, 38, 38)));
    public Option<Double> warnTime = addOption(new DoubleOption("WarnTime", 0, 20, 5).setUnit("s").addSpecialValue(0, "DISABLE"));
    public Option<Color> warnTimeColor = addOption(new ColorOption("WarnTimeColor", new Color(238, 30, 30)));
    public Option<Color> splitLineColor = addOption(new ColorOption("SplitLineColor", new Color(215, 215, 215)));
    public Option<Boolean> icon = addOption(new BooleanOption("Icon", true));
    public Option<Boolean> background = addOption(new BooleanOption("Background", true));
    public Option<Boolean> blur = addOption(new BooleanOption("Blur", true, v -> background.getValue()));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(241, 241, 241), v -> background.getValue()));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", true, v -> background.getValue()));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0, 6, 4, v -> background.getValue()));
    public Option<Double> titleOffset = addOption(new DoubleOption("titleOffset", -5, 5, 0));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onMouseRelease(double mouseX, double mouseY, Screen screen, MouseButtons button) {
        if (screen instanceof HudEditorScreen && button.equals(MouseButtons.LEFT)) {
            this.lastMouseStatus = false;
            HudManager.currentHud = null;
        }
    }

    @Override
    public void onDrawSkia(DrawContext context, float tickDelta) {
        if (isNull() || !blur.getValue() || !background.getValue()) {
            return ;
        }

        if (rounded.getValue()) {
            NewRender2DUtil.drawRoundedBlur(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight(),
                    radius.getValue()
            );
        } else {
            NewRender2DUtil.drawBlur(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight()
            );
        }
    }

    @Override
    public void onDraw2D(DrawContext context, float tickDelta) {
        if (isNull()) {
            return ;
        }

        double maxWidth = FontUtil.getWidth(FontSize.SMALL, title.getValue()) + 10;
        for (StatusEffectInstance potionEffect : mc.player.getStatusEffects()) {
            StatusEffect potion = potionEffect.getEffectType().value();
            maxWidth = Math.max(FontUtil.getWidth(FontSize.SMALLEST, potion.getName().getString() + getDuration(potionEffect) + 35) + FontUtil.getWidth(FontSize.SMALL, title.getValue()), maxWidth);
        }
        setWidth(maxWidth);
        setHeight(
                (!mc.player.getStatusEffects().isEmpty() ? 2 * 2 : 0) + FontUtil.getHeight(FontSize.SMALL) + // title
                mc.player.getStatusEffects().size() * (FontUtil.getHeight(FontSize.SMALLEST)) + 2
        );

        if (background.getValue()) {
            if (rounded.getValue()) {
                Render2DUtil.drawRoundedRect(
                        context.getMatrices(),
                        getX(),
                        getY(),
                        getWidth(),
                        getHeight(),
                        radius.getValue(),
                        backgroundColor.getValue()
                );
            } else {
                Render2DUtil.drawRect(
                        context,
                        getX(),
                        getY(),
                        getWidth(),
                        getHeight(),
                        backgroundColor.getValue()
                );
            }
        }

        FontUtil.drawTextWithAlign(
                context,
                title.getValue(),
                getX() + (titleAlign.getValue().equals(Aligns.Left) ? 2 : 0),
                getY(),
                getX() + getWidth() + (titleAlign.getValue().equals(Aligns.Right) ? -2 : 0),
                getY() + 2 * 2 + FontUtil.getHeight(FontSize.SMALL) + 2 + titleOffset.getValue(),
                translateAlign((Aligns) titleAlign.getValue()),
                titleColor.getValue(),
                FontSize.SMALL
        );

        if (!mc.player.getStatusEffects().isEmpty()) {
            Render2DUtil.drawRect(
                    context,
                    getX(),
                    getY() + 2 + FontUtil.getHeight(FontSize.SMALL),
                    getWidth(),
                    0.6,
                    splitLineColor.getValue()
            );
        }

        int counter = 0;
        for (StatusEffectInstance potionEffect : mc.player.getStatusEffects()) {
            StatusEffect potion = potionEffect.getEffectType().value();
            FontUtil.drawTextWithAlign(
                    context,
                    potion.getName().getString(),
                    getX() + 2,
                    getY() + 2 * 2 + FontUtil.getHeight(FontSize.SMALL) + (counter * FontUtil.getHeight(FontSize.SMALLEST) + 2),
                    getX() + getWidth(),
                    getY() + getHeight(),
                    cc.vergence.features.enums.other.Aligns.LEFT_TOP,
                    customPotionColor.getValue() ? potionColor.getValue() : new Color(potion.getColor(), true),
                    FontSize.SMALLEST
            );
            FontUtil.drawTextWithAlign(
                    context,
                    getDuration(potionEffect),
                    getX() + 2,
                    getY() + 2 * 2 + FontUtil.getHeight(FontSize.SMALL) + (counter * FontUtil.getHeight(FontSize.SMALLEST) + 2),
                    getX() + getWidth() - 2,
                    getY() + getHeight(),
                    cc.vergence.features.enums.other.Aligns.RIGHT_TOP,
                    warnTime.getValue().intValue() != 0 ? (potionEffect.getDuration() < warnTime.getValue() * 20 ? warnTimeColor.getValue() : potionTimeColor.getValue()) : potionTimeColor.getValue(),
                    FontSize.SMALLEST
            );
            counter ++;
        }

        if (HudManager.CLICKED_LEFT) {
            if (HudManager.MOUSE_X > getX() && HudManager.MOUSE_X < getX() + getWidth() &&
                    HudManager.MOUSE_Y > getY() && HudManager.MOUSE_Y < getY() + getHeight() && HudManager.currentHud == null || HudManager.currentHud == this) {
                HudManager.currentHud = this;
                if (!lastMouseStatus) {
                    lastMouseStatus = true;
                } else {
                    setX(getX() + HudManager.MOUSE_X - lastMouseX);
                    setY(getY() + HudManager.MOUSE_Y - lastMouseY);
                }
                lastMouseX = HudManager.MOUSE_X;
                lastMouseY = HudManager.MOUSE_Y;
            }
        }
    }

    public String getDuration(StatusEffectInstance potionEffect) {
        if (potionEffect.isInfinite()) {
            return infinityTime.getValue();
        } else {
            int mins = potionEffect.getDuration() / 1200;
            String sec = String.format("%02d", (potionEffect.getDuration() % 1200) / 20);
            return mins + ":" + sec;
        }
    }

    private cc.vergence.features.enums.other.Aligns translateAlign(Aligns aligns) {
        switch (aligns) {
            case Left -> {
                return cc.vergence.features.enums.other.Aligns.LEFT;
            }
            case Center -> {
                return cc.vergence.features.enums.other.Aligns.CENTER;
            }
            default -> {
                return cc.vergence.features.enums.other.Aligns.RIGHT;
            }
        }

    }

    private enum Aligns {
        Left,
        Center,
        Right
    }
}
