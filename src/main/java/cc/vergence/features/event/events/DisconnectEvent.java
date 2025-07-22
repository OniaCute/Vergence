package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;

public class DisconnectEvent extends Event {
    private String reason;
    public DisconnectEvent(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}