package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;

import java.util.UUID;

public class PlayerDisconnectEvent extends Event {
    private final UUID id;

    public PlayerDisconnectEvent(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
