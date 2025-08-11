package cc.vergence.modules.hud;

import cc.vergence.features.enums.client.MouseButtons;
import cc.vergence.features.managers.ui.HudManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.screens.HudEditorScreen;
import cc.vergence.modules.Module;
import cc.vergence.util.render.utils.NewRender2DUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.awt.*;
import java.util.List;

public class ArmorHud extends Module {
    public static ArmorHud INSTANCE;
    private boolean lastMouseStatus = false;
    private double lastMouseX = -1;
    private double lastMouseY = -1;

    public ArmorHud() {
        super("ArmorHud", Category.HUD);
        INSTANCE = this;
        setX(0);
        setY(0);
        setWidth(0);
        setHeight(0);
    }

    public Option<Enum<?>> mode = addOption(new EnumOption("Mode", Modes.Horizontal));
    public Option<Double> padding = addOption(new DoubleOption("Padding", -4, 8, 0));
    public Option<Double> offsetX = addOption(new DoubleOption("OffsetX", -8, 8, 0));
    public Option<Double> offsetY = addOption(new DoubleOption("OffsetY", -8, 8, 0));
    public Option<Boolean> blur = addOption(new BooleanOption("Blur", true));
    public Option<Boolean> durability = addOption(new BooleanOption("Durability", false));
    public Option<Color> durabilityColor = addOption(new ColorOption("DurabilityColor", new Color(251, 142, 255, 223), v -> durability.getValue()));
    public Option<Boolean> background = addOption(new BooleanOption("Background", true));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(255, 255, 255, 244), v -> background.getValue()));
    public Option<Boolean> rounded = addOption(new BooleanOption("Rounded", false, v -> background.getValue()));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0, 6, 4, v -> background.getValue() && rounded.getValue()));

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
        if (!blur.getValue() || !background.getValue()) {
            return;
        }
        Modes mode = (Modes) this.mode.getValue();
        DefaultedList<ItemStack> armorItems = mc.player.getInventory().armor;
        int iconSize = 16;
        double totalWidth = 0;
        double totalHeight = 0;

        for (int i = 0; i < armorItems.size(); i++) {
            if (mode == Modes.Horizontal) {
                totalWidth += iconSize + padding.getValue();
                totalHeight = iconSize;
            } else {
                totalWidth = iconSize;
                totalHeight += iconSize + padding.getValue();
            }
        }

        if (rounded.getValue()) {
            NewRender2DUtil.drawRoundedBlur(
                    getX(),
                    getY(),
                    totalWidth,
                    totalHeight,
                    radius.getValue()
            );
        } else {
            NewRender2DUtil.drawBlur(
                    getX(),
                    getY(),
                    totalWidth,
                    totalHeight
            );
        }
    }

    @Override
    public void onDraw2D(DrawContext context, float tickDelta) {
        if (isNull()) {
            return;
        }
        double padding = this.padding.getValue();
        double offsetX = this.offsetX.getValue();
        double offsetY = this.offsetY.getValue();
        boolean durability = this.durability.getValue();
        boolean background = this.background.getValue();
        Color backgroundColor = this.backgroundColor.getValue();
        boolean rounded = this.rounded.getValue();
        double radius = this.radius.getValue();
        Modes mode = (Modes) this.mode.getValue();
        List<ItemStack> armorItems = mc.player.getInventory().armor;
        int iconSize = 16;

        double totalWidth = 0;
        double totalHeight = 0;

        for (int i = 0; i < armorItems.size(); i++) {
            if (mode == Modes.Horizontal) {
                totalWidth += iconSize + padding;
                totalHeight = iconSize;
            } else {
                totalWidth = iconSize;
                totalHeight += iconSize + padding;
            }
        }

        if (background) {
            if (rounded) {
                Render2DUtil.drawRoundedRect(
                        context.getMatrices(),
                        (float) getX(),
                        (float) getY(),
                        (float) totalWidth,
                        (float) totalHeight,
                        (float) radius,
                        backgroundColor
                );
            } else {
                Render2DUtil.drawRect(
                        context,
                        (float) getX(),
                        (float) getY(),
                        (float) totalWidth,
                        (float) totalHeight,
                        backgroundColor.getRGB()
                );
            }
        }

        for (int i = 0; i < armorItems.size(); i++) {
            ItemStack itemStack = armorItems.get(armorItems.size() - 1 - i);
            if (itemStack.isEmpty()) {
                continue;
            }

            double itemX, itemY;
            if (mode == Modes.Horizontal) {
                itemX = getX() + i * (iconSize + padding) + offsetX;
                itemY = getY() + offsetY;
            } else {
                itemX = getX() + offsetX;
                itemY = getY() + i * (iconSize + padding) + offsetY;
            }

            context.drawItem(itemStack, (int) itemX, (int) itemY);

            if (durability) {
                int maxDurability = itemStack.getMaxDamage();
                int currentDurability = maxDurability - itemStack.getDamage();
                double durabilityWidth = ((currentDurability / (double) maxDurability) * iconSize) - 7;

                Render2DUtil.drawRect(
                        context,
                        (float) itemX + 3.5,
                        (float) (itemY + iconSize - 1),
                        (float) durabilityWidth,
                        1,
                        durabilityColor.getValue().getRGB()
                );
            }
        }

        setWidth((int) totalWidth - padding);
        setHeight((int) totalHeight);

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

    private enum Modes {
        Vertical,
        Horizontal
    }
}
