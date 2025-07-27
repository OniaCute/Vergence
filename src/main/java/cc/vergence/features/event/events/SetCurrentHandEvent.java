package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class SetCurrentHandEvent extends Event implements Wrapper {
    private final Hand hand;

    public SetCurrentHandEvent(Hand hand) {
        this.hand = hand;
    }

    public Hand getHand() {
        return hand;
    }

    public ItemStack getStackInHand() {
        return mc.player.getStackInHand(hand);
    }
}
