package cc.vergence.modules.misc;

import cc.vergence.Vergence;
import cc.vergence.features.enums.AntiCheats;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.player.InventoryUtil;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

public class AirPearl extends Module {
    public static AirPearl INSTANCE;
    private boolean switched = false;

    public AirPearl() {
        super("AirPearl", Module.Category.MISC);
    }

    public Option<Boolean> fastCheck = addOption(new BooleanOption("FastCheck", false));
    public Option<Double> distance = addOption(new DoubleOption("Distance", 2, 7, 5));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (mc.player == null) {
            return ;
        }
        if (EntityUtil.isFalling(distance.getValue()) || (fastCheck.getValue() && !mc.player.isOnGround()) && !switched) {
            if (mc.player.getStackInHand(Hand.MAIN_HAND).getItem().equals(Items.ENDER_PEARL)) {
                return ;
            }

            if (InventoryUtil.findItem(Items.ENDER_PEARL) != -1) {
                InventoryUtil.switchToSlot(InventoryUtil.findItem(Items.ENDER_PEARL));
                switched = true;
            }
        } else {
            switched = false;
        }
    }
}
