package cc.vergence.modules.hud;

import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.Notify;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.utils.NewRender2DUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class HotbarHud extends Module implements Wrapper {
    public static HotbarHud INSTANCE;

    public HotbarHud() {
        super("HotbarHud", Category.HUD);
        INSTANCE = this;
    }

    public Option<Enum<?>> xpFontSize = addOption(new EnumOption("XPFontSize", FontSize.SMALL));
    public Option<Enum<?>> countFontSize = addOption(new EnumOption("CountFontSize", FontSize.SMALL));
    public Option<Enum<?>> countAligns = addOption(new EnumOption("CountAligns", Aligns.RIGHT_BOTTOM));
    public Option<Boolean> blur = addOption(new BooleanOption("Blur", true));
    public Option<Boolean> itemFloat = addOption(new BooleanOption("ItemFloat", true));
    public Option<Double> radius = addOption(new DoubleOption("Radius", 0, 7, 4));
    public Option<Color> xpColor = addOption(new ColorOption("XPColor", new Color(211, 68, 255)));
    public Option<Color> countColor = addOption(new ColorOption("CountColor", new Color(49, 49, 49)));
    public Option<Color> selectColor = addOption(new ColorOption("SelectColor", new Color(246, 167, 255)));
    public Option<Color> backgroundColor = addOption(new ColorOption("BackgroundColor", new Color(255, 255, 255, 205)));
    public Option<Double> offsetX = addOption(new DoubleOption("OffsetX", -10, 10, 0));
    public Option<Double> offsetY = addOption(new DoubleOption("OffsetY", -10, 10, 0));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDrawSkia(DrawContext context, float tickDelta) {
        if (isNull()) {
            return ;
        }

        int i = mc.getWindow().getScaledWidth() / 2;
        if (blur.getValue()) {
            if (mc.player.getOffHandStack().isEmpty()) {
                NewRender2DUtil.drawRoundedBlur(i - 90, mc.getWindow().getScaledHeight() - 25, 180, 20, radius.getValue());
            } else {
                NewRender2DUtil.drawRoundedBlur(i - 90, mc.getWindow().getScaledHeight() - 25, 180, 20, radius.getValue());
                NewRender2DUtil.drawRoundedBlur(i - 112.5f, mc.getWindow().getScaledHeight() - 25, 20, 20, radius.getValue());
            }
        }

        PlayerEntity playerEntity = mc.player;
        if (playerEntity != null) {
            MatrixStack matrices = context.getMatrices();
            if (mc.player.getOffHandStack().isEmpty()) {
                NewRender2DUtil.drawRoundedRect(i - 90, mc.getWindow().getScaledHeight() - 25, 180, 20, radius.getValue(), backgroundColor.getValue());
            } else {
                NewRender2DUtil.drawRoundedRect(i - 90, mc.getWindow().getScaledHeight() - 25, 180, 20, radius.getValue(), backgroundColor.getValue());
                NewRender2DUtil.drawRoundedRect(i - 112.5f, mc.getWindow().getScaledHeight() - 25, 20, 20, radius.getValue(), backgroundColor.getValue());
            }
            NewRender2DUtil.drawRoundedRect(i - 88 + playerEntity.getInventory().selectedSlot * 19.8f + 0.5, mc.getWindow().getScaledHeight() - 22 - (itemFloat.getValue() ? 2 : 0), 17, 17, radius.getValue(), selectColor.getValue());
        }
    }

    @Override
    public void onDraw2D(DrawContext context, float tickDelta) {
        // skia renderer
    }

    public void renderHotBarItems(DrawContext context) {
        if (isNull()) {
            return ;
        }

        PlayerEntity playerEntity = mc.player;
        if (playerEntity != null) {
            int i = mc.getWindow().getScaledWidth() / 2;
            int o = mc.getWindow().getScaledHeight() - 16 - 3;

            if (!mc.player.getOffHandStack().isEmpty()) {
                renderHotbarItem(context, i - 111, o - 5, playerEntity.getOffHandStack());
            }

            for (int m = 0; m < 9; ++m) {
                int n = i - 90 + m * 20 + 2;
                if (m == mc.player.getInventory().selectedSlot) {
                    renderHotbarItem(context, n, o - (itemFloat.getValue() ? 7 : 5), playerEntity.getInventory().main.get(m));
                } else {
                    renderHotbarItem(context, n, o - 5, playerEntity.getInventory().main.get(m));
                }
            }
        }
    }

    private void renderHotbarItem(DrawContext context, int i, int j, ItemStack itemStack) {
        if (isNull()) {
            return ;
        }

        if (!itemStack.isEmpty()) {
            context.getMatrices().push();
            context.getMatrices().translate((float) (i + 8), (float) (j + 12), 0.0F);
            context.getMatrices().scale(0.9f, 0.9f, 1.0F);
            context.getMatrices().translate((float) (-(i + 8)), (float) (-(j + 12)), 0.0F);
            context.drawItem(itemStack, i, j);
            if (!mc.player.isCreative() && itemStack.getMaxCount() != 1) {
                FontUtil.drawTextWithAlign(context, String.valueOf(itemStack.getCount()), i, j, i + (20) - 2 + offsetX.getValue(), j + (20) + 3 + offsetY.getValue(), (Aligns) countAligns.getValue(), countColor.getValue(), (FontSize) countFontSize.getValue());
            }
            context.getMatrices().pop();
        }
    }

    public void renderXpBar(MatrixStack matrices) {
        if (isNull()) {
            return ;
        }
        if (mc.player.experienceLevel > 0) {
            String string = String.valueOf(mc.player.experienceLevel);
            FontUtil.drawTextWithAlign(
                    matrices,
                    string,
                    0,
                    mc.getWindow().getScaledHeight() - 31 - 4,
                    mc.getWindow().getScaledWidth(),
                    mc.getWindow().getScaledHeight(),
                    Aligns.TOP,
                    xpColor.getValue(),
                    (FontSize) xpFontSize.getValue()
            );
        }
    }
}
