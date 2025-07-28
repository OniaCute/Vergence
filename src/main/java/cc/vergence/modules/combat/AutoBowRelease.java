package cc.vergence.modules.combat;

import cc.vergence.Vergence;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoBowRelease extends Module {
    public static AutoBowRelease INSTANCE;

    public AutoBowRelease() {
        super("AutoBowRelease", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Boolean> autoRelease = addOption(new BooleanOption("AutoRelease", false));
    public Option<Double> ticks = addOption(new DoubleOption("Ticks", 3, 20, 5));
    public Option<Boolean> packet = addOption(new BooleanOption("Packets", false));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onTick() {
        if (isNull()) {
            return ;
        }

        if (mc.player.getMainHandStack().getItem() == Items.BOW) {
            if (mc.player.getItemUseTime() >= ticks.getValue()) {
                Vergence.NETWORK.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
                if (packet.getValue()) {
                    Vergence.NETWORK.sendSequencedPacket(id -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, id, mc.player.getYaw(), mc.player.getPitch()));
                }
                mc.player.stopUsingItem();
            }
        } else if (autoRelease.getValue() && mc.player.getMainHandStack().getItem() == Items.CROSSBOW && mc.player.getItemUseTime() / (float) CrossbowItem.getPullTime(mc.player.getMainHandStack(), mc.player) > 1.0f) {
            Vergence.NETWORK.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
            mc.player.stopUsingItem();
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        }
    }
}
