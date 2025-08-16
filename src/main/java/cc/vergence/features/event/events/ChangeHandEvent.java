package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;
import net.minecraft.util.Hand;

public class ChangeHandEvent extends Event {
    private Hand hand;

    public ChangeHandEvent(Hand hand) {
        this.hand = hand;
    }

    public Hand getHand() {
        return hand;
    }
}
