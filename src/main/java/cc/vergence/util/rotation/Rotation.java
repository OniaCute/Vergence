package cc.vergence.util.rotation;

import cc.vergence.features.enums.player.RotateModes;

public class Rotation {
    private float pitch;
    private double yaw;
    private int priority;
    private double ticks;
    private double rotateSpeed;
    private RotateModes rotateModes;

    public Rotation(float pitch, double yaw, double rotateSpeed, RotateModes rotateModes, int priority) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.rotateSpeed = rotateSpeed;
        this.rotateModes = rotateModes;
        this.priority = priority;
        this.ticks = 0;
    }

    public Rotation(float pitch, double yaw, double rotateSpeed, RotateModes rotateModes) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.rotateSpeed = rotateSpeed;
        this.rotateModes = rotateModes;
        this.priority = 0;
        this.ticks = 0;
    }

    public Rotation(float pitch, double yaw, RotateModes rotateModes) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.rotateSpeed = 180;
        this.rotateModes = rotateModes;
        this.priority = 0;
        this.ticks = 0;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public float getPitch() {
        return pitch;
    }

    public double getYaw() {
        return yaw;
    }

    public int getPriority() {
        return priority;
    }

    public void setTicks(double ticks) {
        this.ticks = ticks;
    }

    public double getTicks() {
        return ticks;
    }

    public void setRotateModes(RotateModes rotateModes) {
        this.rotateModes = rotateModes;
    }

    public RotateModes getRotateModes() {
        return rotateModes;
    }

    public void setRotateSpeed(double rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
    }

    public double getRotateSpeed() {
        return rotateSpeed;
    }
}
