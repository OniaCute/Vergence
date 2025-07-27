package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;

public class TickMovementEvent extends Event {
    private int iterations;

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
}
