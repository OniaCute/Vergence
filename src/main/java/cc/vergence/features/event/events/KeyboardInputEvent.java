package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;

public class KeyboardInputEvent extends Event {
    public KeyboardInputEvent() {
        super(Stage.Post);
    }
}
