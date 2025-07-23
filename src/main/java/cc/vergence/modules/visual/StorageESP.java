package cc.vergence.modules.visual;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.utils.Render3DUtil;
import net.minecraft.block.*;
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

public class StorageESP extends Module implements Wrapper {
    public static StorageESP INSTANCE;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private List<SearchBlock> blocks = new ArrayList<>();
    private boolean lock = false;
    private long lastSearchTime = 0;
    private int amount = 0;
    private volatile boolean active = true;

    public StorageESP() {
        super("StorageESP", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<Double> range = addOption(new DoubleOption("Range", 0, 100, 100));
    public Option<Color> chestColor = addOption(new ColorOption("ChestColor", new Color(210, 178, 99, 140)));
    public Option<Color> trappedChestColor = addOption(new ColorOption("TrappedChestColor", new Color(255, 21, 104, 140)));
    public Option<Color> enderChestColor = addOption(new ColorOption("EnderChestColor", new Color(119, 29, 119, 140)));
    public Option<Color> barrelColor = addOption(new ColorOption("BarrelColor", new Color(160, 110, 60, 140)));
    public Option<Color> shulkerBoxColor = addOption(new ColorOption("ShulkerBoxColor", new Color(200, 100, 200, 140)));
    public Option<Color> furnaceColor = addOption(new ColorOption("FurnaceColor", new Color(85, 85, 85, 140)));
    public Option<Color> blastFurnaceColor = addOption(new ColorOption("BlastFurnaceColor", new Color(105, 75, 55, 140)));
    public Option<Color> smokerColor = addOption(new ColorOption("SmokerColor", new Color(105, 105, 105, 140)));
    public Option<Color> dispenserColor = addOption(new ColorOption("DispenserColor", new Color(70, 70, 70, 140)));
    public Option<Color> dropperColor = addOption(new ColorOption("DropperColor", new Color(90, 90, 90, 140)));
    public Option<Color> hopperColor = addOption(new ColorOption("HopperColor", new Color(75, 75, 75, 140)));
    public Option<Color> craftingTableColor = addOption(new ColorOption("CraftingTableColor", new Color(150, 120, 80, 140)));
    public Option<EnumSet<Modes>> mode = addOption(new MultipleOption<>("Mode", EnumSet.of(Modes.Outline)));

    @Override
    public String getDetails() {
        return amount == 0 ? "NoStorage" : String.valueOf(amount);
    }

    @Override
    public void onEnable() {
        blocks.clear();
        lock = false;
        lastSearchTime = 0;
        active = true;
        amount = 0;
    }

    @Override
    public void onLogout() {
        disable();
    }

    @Override
    public void onDisable() {
        active = false;
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        long now = System.currentTimeMillis();
        if (now - lastSearchTime > 1000 && !lock) {
            lock = true;
            CompletableFuture.supplyAsync(this::search, executor).thenAcceptAsync(this::sync, mc);
            lastSearchTime = now;
        }
    }

    @Override
    public void onDraw3D(MatrixStack matrixStack, float tickDelta) {
        if (blocks.isEmpty()) return;
        for (SearchBlock block : blocks) {
            Color fill = block.color;
            Render3DUtil.draw3DBox(matrixStack, block.box, fill, mode.getValue().contains(Modes.Fill), fill, mode.getValue().contains(Modes.Outline));
        }
    }

    private List<SearchBlock> search() {
        List<SearchBlock> result = new ArrayList<>();
        amount = 0;
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
                    if (isContainerBlock(state.getBlock())) {
                        Box box = state.getOutlineShape(mc.world, pos).getBoundingBox().offset(pos);
                        Color c = getColorForBlock(state.getBlock());
                        result.add(new SearchBlock(box, c));
                        amount++;
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

    private boolean isContainerBlock(Block block) {
        return block == Blocks.CHEST
                || block == Blocks.TRAPPED_CHEST
                || block == Blocks.ENDER_CHEST
                || block == Blocks.BARREL
                || block == Blocks.SHULKER_BOX
                || block == Blocks.WHITE_SHULKER_BOX
                || block == Blocks.ORANGE_SHULKER_BOX
                || block == Blocks.MAGENTA_SHULKER_BOX
                || block == Blocks.LIGHT_BLUE_SHULKER_BOX
                || block == Blocks.YELLOW_SHULKER_BOX
                || block == Blocks.LIME_SHULKER_BOX
                || block == Blocks.PINK_SHULKER_BOX
                || block == Blocks.GRAY_SHULKER_BOX
                || block == Blocks.CYAN_SHULKER_BOX
                || block == Blocks.PURPLE_SHULKER_BOX
                || block == Blocks.BLUE_SHULKER_BOX
                || block == Blocks.BROWN_SHULKER_BOX
                || block == Blocks.GREEN_SHULKER_BOX
                || block == Blocks.RED_SHULKER_BOX
                || block == Blocks.BLACK_SHULKER_BOX
                || block == Blocks.FURNACE
                || block == Blocks.BLAST_FURNACE
                || block == Blocks.SMOKER
                || block == Blocks.DISPENSER
                || block == Blocks.DROPPER
                || block == Blocks.HOPPER
                || block == Blocks.CRAFTING_TABLE;
    }

    private Color getColorForBlock(Block block) {
        if (block == Blocks.CHEST) return chestColor.getValue();
        if (block == Blocks.TRAPPED_CHEST) return trappedChestColor.getValue();
        if (block == Blocks.ENDER_CHEST) return enderChestColor.getValue();
        if (block == Blocks.BARREL) return barrelColor.getValue();
        if (block == Blocks.SHULKER_BOX
                || block == Blocks.WHITE_SHULKER_BOX
                || block == Blocks.ORANGE_SHULKER_BOX
                || block == Blocks.MAGENTA_SHULKER_BOX
                || block == Blocks.LIGHT_BLUE_SHULKER_BOX
                || block == Blocks.YELLOW_SHULKER_BOX
                || block == Blocks.LIME_SHULKER_BOX
                || block == Blocks.PINK_SHULKER_BOX
                || block == Blocks.GRAY_SHULKER_BOX
                || block == Blocks.CYAN_SHULKER_BOX
                || block == Blocks.PURPLE_SHULKER_BOX
                || block == Blocks.BLUE_SHULKER_BOX
                || block == Blocks.BROWN_SHULKER_BOX
                || block == Blocks.GREEN_SHULKER_BOX
                || block == Blocks.RED_SHULKER_BOX
                || block == Blocks.BLACK_SHULKER_BOX) return shulkerBoxColor.getValue();
        if (block == Blocks.FURNACE) return furnaceColor.getValue();
        if (block == Blocks.BLAST_FURNACE) return blastFurnaceColor.getValue();
        if (block == Blocks.SMOKER) return smokerColor.getValue();
        if (block == Blocks.DISPENSER) return dispenserColor.getValue();
        if (block == Blocks.DROPPER) return dropperColor.getValue();
        if (block == Blocks.HOPPER) return hopperColor.getValue();
        if (block == Blocks.CRAFTING_TABLE) return craftingTableColor.getValue();
        return new Color(255, 255, 255, 130);
    }

    private static class SearchBlock {
        private final Box box;
        private final Color color;

        public SearchBlock(Box box, Color color) {
            this.box = box;
            this.color = color;
        }
    }

    public enum Modes {
        Fill,
        Outline
    }
}
