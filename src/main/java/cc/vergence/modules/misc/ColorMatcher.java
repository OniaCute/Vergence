package cc.vergence.modules.misc;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.render.utils.Render3DUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

// For mini-games ~
public class ColorMatcher extends Module {
    public static ColorMatcher INSTANCE;
    private final Map<DyeColor, Option<Color>> colorOptionMap = new HashMap<>();

    public Option<EnumSet<Modes>> mode = addOption(new MultipleOption<>("Mode", EnumSet.of(Modes.Outline, Modes.Fill)));
    public Option<Double> maxRange = addOption(new DoubleOption("Range", 1, 50, 20));
    public Option<Color> nearestColor = addOption(new ColorOption("NearestColor", new Color(166, 124, 255)));
    public Option<Color> whiteColor = addOption(new ColorOption("WhiteColor", new Color(0xF9F9F9)));
    public Option<Color> orangeColor = addOption(new ColorOption("OrangeColor", new Color(0xF9801D)));
    public Option<Color> magentaColor = addOption(new ColorOption("MagentaColor", new Color(0xC74EBD)));
    public Option<Color> lightBlueColor = addOption(new ColorOption("LightBlueColor", new Color(0x3AB3DA)));
    public Option<Color> yellowColor = addOption(new ColorOption("YellowColor", new Color(0xFED83D)));
    public Option<Color> limeColor = addOption(new ColorOption("LimeColor", new Color(0x80C71F)));
    public Option<Color> pinkColor = addOption(new ColorOption("PinkColor", new Color(0xF38BAA)));
    public Option<Color> grayColor = addOption(new ColorOption("GrayColor", new Color(0x474F52)));
    public Option<Color> lightGrayColor = addOption(new ColorOption("LightGrayColor", new Color(0x9D9D97)));
    public Option<Color> cyanColor = addOption(new ColorOption("CyanColor", new Color(0x169C9C)));
    public Option<Color> purpleColor = addOption(new ColorOption("PurpleColor", new Color(0x8932B8)));
    public Option<Color> blueColor = addOption(new ColorOption("BlueColor", new Color(0x3C44AA)));
    public Option<Color> brownColor = addOption(new ColorOption("BrownColor", new Color(0x835432)));
    public Option<Color> greenColor = addOption(new ColorOption("GreenColor", new Color(0x5E7C16)));
    public Option<Color> redColor = addOption(new ColorOption("RedColor", new Color(0xB02E26)));
    public Option<Color> blackColor = addOption(new ColorOption("BlackColor", new Color(0x1D1D21)));

    @Override
    public String getDetails() {
        return "";
    }

    public ColorMatcher() {
        super("ColorMatcher", Category.MISC);
        INSTANCE = this;

        colorOptionMap.put(DyeColor.WHITE, whiteColor);
        colorOptionMap.put(DyeColor.ORANGE, orangeColor);
        colorOptionMap.put(DyeColor.MAGENTA, magentaColor);
        colorOptionMap.put(DyeColor.LIGHT_BLUE, lightBlueColor);
        colorOptionMap.put(DyeColor.YELLOW, yellowColor);
        colorOptionMap.put(DyeColor.LIME, limeColor);
        colorOptionMap.put(DyeColor.PINK, pinkColor);
        colorOptionMap.put(DyeColor.GRAY, grayColor);
        colorOptionMap.put(DyeColor.LIGHT_GRAY, lightGrayColor);
        colorOptionMap.put(DyeColor.CYAN, cyanColor);
        colorOptionMap.put(DyeColor.PURPLE, purpleColor);
        colorOptionMap.put(DyeColor.BLUE, blueColor);
        colorOptionMap.put(DyeColor.BROWN, brownColor);
        colorOptionMap.put(DyeColor.GREEN, greenColor);
        colorOptionMap.put(DyeColor.RED, redColor);
        colorOptionMap.put(DyeColor.BLACK, blackColor);
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if (mc.player == null || mc.world == null) return;

        HashSet<DyeColor> hotbarColors = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            Block block = Block.getBlockFromItem(stack.getItem());
            DyeColor color = getBlockDyeColor(block);
            if (color != null) {
                hotbarColors.add(color);
            }
        }

        BlockPos playerPos = mc.player.getBlockPos();
        BlockPos closestBlockPos = null;
        double closestDistance = Double.MAX_VALUE;

        int range = maxRange.getValue().intValue();
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    Block block = mc.world.getBlockState(pos).getBlock();
                    DyeColor color = getBlockDyeColor(block);

                    if (color != null) {
                        if (!hotbarColors.contains(color)) continue;

                        double dist = pos.getSquaredDistance(playerPos);
                        if (dist < closestDistance) {
                            closestDistance = dist;
                            closestBlockPos = pos;
                        }

                        Color renderColor = colorOptionMap.get(color).getValue();
                        Render3DUtil.draw3DBox(matrixStack, new Box(pos),
                                renderColor, mode.getValue().contains(Modes.Fill),
                                renderColor, mode.getValue().contains(Modes.Outline));
                    }
                }
            }
        }

        if (closestBlockPos != null) {
            Color specialColor = nearestColor.getValue();
            Render3DUtil.draw3DBox(matrixStack, new Box(closestBlockPos),
                    specialColor, mode.getValue().contains(Modes.Fill),
                    specialColor, mode.getValue().contains(Modes.Outline));
        }
    }


    private DyeColor getBlockDyeColor(Block block) {
        if (block == Blocks.WHITE_WOOL || block == Blocks.WHITE_TERRACOTTA || block == Blocks.WHITE_CONCRETE ||
                block == Blocks.WHITE_CONCRETE_POWDER || block == Blocks.WHITE_STAINED_GLASS || block == Blocks.WHITE_STAINED_GLASS_PANE ||
                block == Blocks.WHITE_GLAZED_TERRACOTTA) return DyeColor.WHITE;

        if (block == Blocks.ORANGE_WOOL || block == Blocks.ORANGE_TERRACOTTA || block == Blocks.ORANGE_CONCRETE ||
                block == Blocks.ORANGE_CONCRETE_POWDER || block == Blocks.ORANGE_STAINED_GLASS || block == Blocks.ORANGE_STAINED_GLASS_PANE ||
                block == Blocks.ORANGE_GLAZED_TERRACOTTA) return DyeColor.ORANGE;

        if (block == Blocks.MAGENTA_WOOL || block == Blocks.MAGENTA_TERRACOTTA || block == Blocks.MAGENTA_CONCRETE ||
                block == Blocks.MAGENTA_CONCRETE_POWDER || block == Blocks.MAGENTA_STAINED_GLASS || block == Blocks.MAGENTA_STAINED_GLASS_PANE ||
                block == Blocks.MAGENTA_GLAZED_TERRACOTTA) return DyeColor.MAGENTA;

        if (block == Blocks.LIGHT_BLUE_WOOL || block == Blocks.LIGHT_BLUE_TERRACOTTA || block == Blocks.LIGHT_BLUE_CONCRETE ||
                block == Blocks.LIGHT_BLUE_CONCRETE_POWDER || block == Blocks.LIGHT_BLUE_STAINED_GLASS || block == Blocks.LIGHT_BLUE_STAINED_GLASS_PANE ||
                block == Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA) return DyeColor.LIGHT_BLUE;

        if (block == Blocks.YELLOW_WOOL || block == Blocks.YELLOW_TERRACOTTA || block == Blocks.YELLOW_CONCRETE ||
                block == Blocks.YELLOW_CONCRETE_POWDER || block == Blocks.YELLOW_STAINED_GLASS || block == Blocks.YELLOW_STAINED_GLASS_PANE ||
                block == Blocks.YELLOW_GLAZED_TERRACOTTA) return DyeColor.YELLOW;

        if (block == Blocks.LIME_WOOL || block == Blocks.LIME_TERRACOTTA || block == Blocks.LIME_CONCRETE ||
                block == Blocks.LIME_CONCRETE_POWDER || block == Blocks.LIME_STAINED_GLASS || block == Blocks.LIME_STAINED_GLASS_PANE ||
                block == Blocks.LIME_GLAZED_TERRACOTTA) return DyeColor.LIME;

        if (block == Blocks.PINK_WOOL || block == Blocks.PINK_TERRACOTTA || block == Blocks.PINK_CONCRETE ||
                block == Blocks.PINK_CONCRETE_POWDER || block == Blocks.PINK_STAINED_GLASS || block == Blocks.PINK_STAINED_GLASS_PANE ||
                block == Blocks.PINK_GLAZED_TERRACOTTA) return DyeColor.PINK;

        if (block == Blocks.GRAY_WOOL || block == Blocks.GRAY_TERRACOTTA || block == Blocks.GRAY_CONCRETE ||
                block == Blocks.GRAY_CONCRETE_POWDER || block == Blocks.GRAY_STAINED_GLASS || block == Blocks.GRAY_STAINED_GLASS_PANE ||
                block == Blocks.GRAY_GLAZED_TERRACOTTA) return DyeColor.GRAY;

        if (block == Blocks.LIGHT_GRAY_WOOL || block == Blocks.LIGHT_GRAY_TERRACOTTA || block == Blocks.LIGHT_GRAY_CONCRETE ||
                block == Blocks.LIGHT_GRAY_CONCRETE_POWDER || block == Blocks.LIGHT_GRAY_STAINED_GLASS || block == Blocks.LIGHT_GRAY_STAINED_GLASS_PANE ||
                block == Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA) return DyeColor.LIGHT_GRAY;

        if (block == Blocks.CYAN_WOOL || block == Blocks.CYAN_TERRACOTTA || block == Blocks.CYAN_CONCRETE ||
                block == Blocks.CYAN_CONCRETE_POWDER || block == Blocks.CYAN_STAINED_GLASS || block == Blocks.CYAN_STAINED_GLASS_PANE ||
                block == Blocks.CYAN_GLAZED_TERRACOTTA) return DyeColor.CYAN;

        if (block == Blocks.PURPLE_WOOL || block == Blocks.PURPLE_TERRACOTTA || block == Blocks.PURPLE_CONCRETE ||
                block == Blocks.PURPLE_CONCRETE_POWDER || block == Blocks.PURPLE_STAINED_GLASS || block == Blocks.PURPLE_STAINED_GLASS_PANE ||
                block == Blocks.PURPLE_GLAZED_TERRACOTTA) return DyeColor.PURPLE;

        if (block == Blocks.BLUE_WOOL || block == Blocks.BLUE_TERRACOTTA || block == Blocks.BLUE_CONCRETE ||
                block == Blocks.BLUE_CONCRETE_POWDER || block == Blocks.BLUE_STAINED_GLASS || block == Blocks.BLUE_STAINED_GLASS_PANE ||
                block == Blocks.BLUE_GLAZED_TERRACOTTA) return DyeColor.BLUE;

        if (block == Blocks.BROWN_WOOL || block == Blocks.BROWN_TERRACOTTA || block == Blocks.BROWN_CONCRETE ||
                block == Blocks.BROWN_CONCRETE_POWDER || block == Blocks.BROWN_STAINED_GLASS || block == Blocks.BROWN_STAINED_GLASS_PANE ||
                block == Blocks.BROWN_GLAZED_TERRACOTTA) return DyeColor.BROWN;

        if (block == Blocks.GREEN_WOOL || block == Blocks.GREEN_TERRACOTTA || block == Blocks.GREEN_CONCRETE ||
                block == Blocks.GREEN_CONCRETE_POWDER || block == Blocks.GREEN_STAINED_GLASS || block == Blocks.GREEN_STAINED_GLASS_PANE ||
                block == Blocks.GREEN_GLAZED_TERRACOTTA) return DyeColor.GREEN;

        if (block == Blocks.RED_WOOL || block == Blocks.RED_TERRACOTTA || block == Blocks.RED_CONCRETE ||
                block == Blocks.RED_CONCRETE_POWDER || block == Blocks.RED_STAINED_GLASS || block == Blocks.RED_STAINED_GLASS_PANE ||
                block == Blocks.RED_GLAZED_TERRACOTTA) return DyeColor.RED;

        if (block == Blocks.BLACK_WOOL || block == Blocks.BLACK_TERRACOTTA || block == Blocks.BLACK_CONCRETE ||
                block == Blocks.BLACK_CONCRETE_POWDER || block == Blocks.BLACK_STAINED_GLASS || block == Blocks.BLACK_STAINED_GLASS_PANE ||
                block == Blocks.BLACK_GLAZED_TERRACOTTA) return DyeColor.BLACK;

        return null;
    }


    public enum Modes {
        Fill,
        Outline
    }
}
