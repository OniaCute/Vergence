package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;

public class ReceiveMessageEvent extends Event {
    public String message;

    public ReceiveMessageEvent(String message) {
        super(Stage.Pre);
        this.message = message;
    }

    public String getString() {
        return this.message;
    }
}
