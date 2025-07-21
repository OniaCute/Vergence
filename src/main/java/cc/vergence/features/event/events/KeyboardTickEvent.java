package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;

public class KeyboardTickEvent extends Event {
    public KeyboardTickEvent() {
        super(Stage.Post);
    }
}
