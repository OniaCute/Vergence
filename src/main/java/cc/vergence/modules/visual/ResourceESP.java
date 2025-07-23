package cc.vergence.modules.visual;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.render.utils.Render3DUtil;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.Heightmap;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResourceESP extends Module implements Wrapper {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private List<SearchBlock> blocks = new ArrayList<>();
    private boolean lock = false;
    private long lastSearchTime = 0;
    private volatile boolean active = true;

    public ResourceESP() {
        super("ResourceESP", Category.VISUAL);
    }

    public Option<Double> range = addOption(new DoubleOption("Range", 0, 200, 30));
    public Option<Color> netherPortalColor = addOption(new ColorOption("NetherPortalColor", new Color(100, 50, 255)));
    public Option<Color> diamondColor = addOption(new ColorOption("DiamondColor", new Color(70, 150, 255)));
    public Option<Color> goldColor = addOption(new ColorOption("GoldColor", new Color(255, 200, 70)));
    public Option<Color> emeraldColor = addOption(new ColorOption("EmeraldColor", new Color(70, 255, 90)));
    public Option<Color> redstoneColor = addOption(new ColorOption("RedstoneColor", new Color(250, 30, 30)));
    public Option<Color> lapisColor = addOption(new ColorOption("LapisColor", new Color(30, 50, 250)));
    public Option<Color> ironColor = addOption(new ColorOption("IronColor", new Color(170, 150, 130)));
    public Option<Color> coalColor = addOption(new ColorOption("CoalColor", new Color(35, 35, 35)));
    public Option<Color> netheriteColor = addOption(new ColorOption("NetheriteColor", new Color(140, 30, 15)));
    public Option<EnumSet<Modes>> mode = addOption(new MultipleOption<>("Mode", EnumSet.of(Modes.Outline)));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onEnable() {
        blocks.clear();
        lock = false;
        lastSearchTime = 0;
        active = true;
    }

    @Override
    public void onLogout() {
        disable();
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - lastSearchTime > 1000 && !lock) {
            lock = true;
            CompletableFuture.supplyAsync(this::search, executor).thenAcceptAsync(this::sync, mc);
            lastSearchTime = now;
        }
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if (blocks.isEmpty()) {
            return;
        }
        for (SearchBlock block : blocks) {
            Color fill = block.color;
            Render3DUtil.draw3DBox(matrixStack, block.box, fill, mode.getValue().contains(Modes.Fill), fill, mode.getValue().contains(Modes.Outline));
        }
    }

    private List<SearchBlock> search() {
        List<SearchBlock> result = new ArrayList<>();
        double r = range.getValue();
        int startX = (int) (mc.player.getX() - r);
        int endX = (int) (mc.player.getX() + r);
        int startZ = (int) (mc.player.getZ() - r);
        int endZ = (int) (mc.player.getZ() + r);

        for (int x = startX; x <= endX; x++) {
            for (int z = startZ; z <= endZ; z++) {
                int maxY = mc.world.getTopY(Heightmap.Type.WORLD_SURFACE, x, z);
                for (int y = mc.world.getBottomY(); y <= maxY; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = mc.world.getBlockState(pos);
                    if (isTargetBlock(state.getBlock())) {
                        Box box = state.getOutlineShape(mc.world, pos).getBoundingBox().offset(pos);
                        Color c = getColor(state.getBlock());
                        result.add(new SearchBlock(box, c));
                    }
                }
            }
        }
        return result;
    }

    private void sync(List<SearchBlock> blocks) {
        if (active) {
            this.blocks = blocks;
        }
        lock = false;
    }

    private boolean isTargetBlock(Block block) {
        return switch (block.asItem().toString()) {
            case "minecraft:nether_portal" -> true;
            case "minecraft:diamond_ore", "minecraft:diamond_block" -> true;
            case "minecraft:gold_ore", "minecraft:gold_block" -> true;
            case "minecraft:emerald_ore", "minecraft:emerald_block" -> true;
            case "minecraft:redstone_ore", "minecraft:redstone_block" -> true;
            case "minecraft:lapis_ore", "minecraft:lapis_block" -> true;
            case "minecraft:iron_ore", "minecraft:iron_block" -> true;
            case "minecraft:coal_ore", "minecraft:coal_block" -> true;
            case "minecraft:netherite_block" -> true;
            default -> false;
        };
    }

    private Color getColor(Block block) {
        if (block == Blocks.NETHER_PORTAL) return netherPortalColor.getValue();
        if (block == Blocks.DIAMOND_ORE || block == Blocks.DIAMOND_BLOCK) return diamondColor.getValue();
        if (block == Blocks.GOLD_ORE || block == Blocks.GOLD_BLOCK) return goldColor.getValue();
        if (block == Blocks.EMERALD_ORE || block == Blocks.EMERALD_BLOCK) return emeraldColor.getValue();
        if (block == Blocks.REDSTONE_ORE || block == Blocks.REDSTONE_BLOCK) return redstoneColor.getValue();
        if (block == Blocks.LAPIS_ORE || block == Blocks.LAPIS_BLOCK) return lapisColor.getValue();
        if (block == Blocks.IRON_ORE || block == Blocks.IRON_BLOCK) return ironColor.getValue();
        if (block == Blocks.COAL_ORE || block == Blocks.COAL_BLOCK) return coalColor.getValue();
        if (block == Blocks.NETHERITE_BLOCK) return netheriteColor.getValue();
        return new Color(255, 255, 255);
    }

    private static class SearchBlock {
        private final Box box;
        private final Color color;

        public SearchBlock(Box box, Color color) {
            this.box = box;
            this.color = color;
        }
    }

    @Override
    public void onDisable() {
        active = false;
    }

    public enum Modes {
        Fill,
        Outline
    }
}
