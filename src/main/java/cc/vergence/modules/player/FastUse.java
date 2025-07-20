package cc.vergence.modules.player;

import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.injections.accessors.MinecraftClientAccessor;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class FastUse extends Module implements Wrapper {
    public static FastUse INSTANCE;
    public FastUse() {
        super("FastUse", Category.PLAYER);
        INSTANCE = this;
    }

    public Option<Double> delay = addOption(new DoubleOption("Delay", 0, 4, 0));
    public Option<Boolean> forBlock = addOption(new BooleanOption("Blocks", false));
    public Option<Boolean> forCrystal = addOption(new BooleanOption("Crystals", false));
    public Option<Boolean> forXP = addOption(new BooleanOption("XP", false));

    @Override
    public String getDetails() {
        return String.valueOf(delay.getValue().intValue());
    }

    @Override
    public void onBlock(Module module) {
        MessageManager.blockedMessage(this, module);
    }

    @Override
    public void onTick() {
        if (isNull()) {
            return ;
        }

        if (check(mc.player.getMainHandStack().getItem())) {
            ((MinecraftClientAccessor) mc).setUseCooldown(delay.getValue().intValue());
        }
    }

    public boolean check(Item item) {
        return (item instanceof BlockItem && forBlock.getValue())
                || (item == Items.END_CRYSTAL && forCrystal.getValue())
                || (item == Items.EXPERIENCE_BOTTLE && forXP.getValue());
    }
}
