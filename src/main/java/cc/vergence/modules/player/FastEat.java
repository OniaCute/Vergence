package cc.vergence.modules.player;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.event.events.SetCurrentHandEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import cc.vergence.util.player.InventoryUtil;
import cc.vergence.util.player.MovementUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

public class FastEat extends Module {
    public static FastEat INSTANCE;
    private int packets = 0;

    public FastEat() {
        super("FastEat", Category.PLAYER);
        INSTANCE = this;
    }

    public Option<Enum<?>> mode = addOption(new EnumOption("Mode", Modes.None));
    public Option<Double> ticks = addOption(new DoubleOption("Ticks", 0, 30, 10));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (MovementUtil.isMoving() || !mc.player.isOnGround()) {
            packets--;
            if (packets <= 0) {
                packets = 0;
            }
        } else {
            packets++;
            if (packets > 100) {
                packets = 100;
            }
        }

        if (!mc.player.isUsingItem()) {
            return;
        }
        final ItemStack stack = mc.player.getStackInHand(mc.player.getActiveHand());
        if (stack.isEmpty() || !InventoryUtil.isFood(stack)) {
            return;
        }

        final int timeUsed = mc.player.getItemUseTime();
        if (timeUsed >= ticks.getValue()) {
            final int packets = 32 - timeUsed;
            for (int i = 0; i < packets; ++i) {
                switch (((Modes) mode.getValue())) {

                }
            }
        }
    }

    @Override
    public void onSetCurrentHand(SetCurrentHandEvent event, Hand hand, ItemStack stack) {
        if (isNull()) {
            return ;
        }

        if (mode.getValue().equals(Modes.Shift)) {
            if (!InventoryUtil.isFood(stack) && !(stack.getItem() instanceof PotionItem)) {
                return;
            }
            int maxUseTime = stack.getMaxUseTime(mc.player);
            if (packets < maxUseTime) {
                return;
            }
            for (int i = 0; i < maxUseTime; i++) {
                Vergence.NETWORK.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.isOnGround(), false));
                packets -= maxUseTime;
            }
            event.cancel();
            stack.getItem().finishUsing(stack, mc.world, mc.player);
        }
    }


    public enum Modes {
        None,
        Shift,
        Grim
    }
}
