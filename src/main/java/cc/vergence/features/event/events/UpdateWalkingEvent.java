package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;

public class UpdateWalkingEvent extends Event {
    private boolean cancelRotate = false;
    public UpdateWalkingEvent(Stage stage) {
        super(stage);
    }
    public void cancelRotate() {
        this.cancelRotate = true;
    }
    public void setCancelRotate(boolean cancelRotate) {
        this.cancelRotate = cancelRotate;
    }

    public boolean isCancelRotate() {
        return cancelRotate;
    }
}
