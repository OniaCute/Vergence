package cc.vergence.features.event.events;

import cc.vergence.features.event.Event;
import net.minecraft.util.math.Vec3d;

public class UpdateVelocityEvent extends Event {
    private final Vec3d movementInput;
    private final float speed;

    public UpdateVelocityEvent(Vec3d movementInput, float speed) {
        this.movementInput = movementInput;
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public Vec3d getMovementInput() {
        return movementInput;
    }
}
