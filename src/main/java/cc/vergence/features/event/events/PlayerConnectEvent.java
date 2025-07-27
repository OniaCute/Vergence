package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;

import java.util.UUID;

public class PlayerConnectEvent extends Event {
    private final UUID id;

    public PlayerConnectEvent(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
