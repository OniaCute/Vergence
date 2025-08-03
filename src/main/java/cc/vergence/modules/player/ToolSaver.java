package cc.vergence.modules.player;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;

public class ToolSaver extends Module {
    public static ToolSaver INSTANCE;

    public ToolSaver() {
        super("ToolSaver", Category.PLAYER);
        INSTANCE = this;
    }

    public Option<Double> limit = addOption(new DoubleOption("Limit", 1, 99, 20).setUnit("%"));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onTick() {
        if (isNull()) {
            return ;
        }

        ItemStack tool = mc.player.getMainHandStack();
        if(!(tool.getItem() instanceof MiningToolItem))
            return;

        double durability = tool.getMaxDamage() - tool.getDamage();
        double percent = ((durability / tool.getMaxDamage()) * 100.0);

        if (percent <= limit.getValue()) {
            mc.player.getInventory().selectedSlot = findNearestCurrentItem();
        }
    }

    public static int findNearestCurrentItem() {
        int i = mc.player.getInventory().selectedSlot;
        if (i == 8) return 7;
        if (i == 0) return 1;
        return i - 1;
    }
}
