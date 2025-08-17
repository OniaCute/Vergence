package cc.vergence.modules.visual;

import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.util.ArrayList;

public class Xray extends Module {
    public static Xray INSTANCE;
    public ArrayList<String> blocks = new ArrayList<>();

    public Xray() {
        super("Xray", Category.VISUAL);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onEnable() {
        mc.chunkCullingEnabled = false;
        mc.worldRenderer.reload();
    }

    @Override
    public void onDisable() {
        mc.worldRenderer.reload();
        mc.chunkCullingEnabled = true;
    }

    @Override
    public void onTick() {
    }

    @Override
    public void onRegister() {
        blocks.add(Blocks.DIAMOND_ORE.getTranslationKey());
        blocks.add(Blocks.DEEPSLATE_DIAMOND_ORE.getTranslationKey());
        blocks.add(Blocks.GOLD_ORE.getTranslationKey());
        blocks.add(Blocks.NETHER_GOLD_ORE.getTranslationKey());
        blocks.add(Blocks.IRON_ORE.getTranslationKey());
        blocks.add(Blocks.DEEPSLATE_IRON_ORE.getTranslationKey());
        blocks.add(Blocks.REDSTONE_ORE.getTranslationKey());
        blocks.add(Blocks.EMERALD_ORE.getTranslationKey());
        blocks.add(Blocks.DEEPSLATE_EMERALD_ORE.getTranslationKey());
        blocks.add(Blocks.DEEPSLATE_REDSTONE_ORE.getTranslationKey());
        blocks.add(Blocks.COAL_ORE.getTranslationKey());
        blocks.add(Blocks.DEEPSLATE_COAL_ORE.getTranslationKey());
        blocks.add(Blocks.ANCIENT_DEBRIS.getTranslationKey());
        blocks.add(Blocks.NETHER_QUARTZ_ORE.getTranslationKey());
        blocks.add(Blocks.LAPIS_ORE.getTranslationKey());
        blocks.add(Blocks.DEEPSLATE_LAPIS_ORE.getTranslationKey());
    }

    public boolean check(Block block) {
        return blocks.contains(block.getTranslationKey());
    }
}
