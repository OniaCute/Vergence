package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;

public class SyncEvent extends Event {
    public SyncEvent(float yaw, float pitch) {
        super();
        this.yaw = yaw;
        this.pitch = pitch;
    }

    float yaw;
    float pitch;

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}