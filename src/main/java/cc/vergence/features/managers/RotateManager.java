package cc.vergence.features.managers;

import cc.vergence.Vergence;
import cc.vergence.features.enums.RotateModes;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.rotation.Rotation;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.Comparator;
import java.util.PriorityQueue;

public class RotateManager implements Wrapper {
    private final PriorityQueue<RotationTask> rotationQueue = new PriorityQueue<>(Comparator.comparingInt(r -> -r.rotation().getPriority()));
    private RotationTask currentTask = null;

    private float lastYaw = 0;
    private float lastPitch = 0;

    private int tick = 0;
    private int totalTicks = 0;

    public void rotate(Rotation rotation) {
        rotate(rotation, null);
    }

    public void rotate(Rotation rotation, Runnable onFinish) {
        if (mc.player == null) return;

        if (lastYaw == 0 && lastPitch == 0) {
            lastYaw = mc.player.getYaw();
            lastPitch = mc.player.getPitch();
        }

        if (rotation.isSmooth() && rotation.getSmoothOffset() > 0) {
            double offset = rotation.getSmoothOffset();
            double randomYawOffset = (Math.random() * 2 - 1) * offset;
            double randomPitchOffset = (Math.random() * 2 - 1) * offset;

            rotation.setYaw(rotation.getYaw() + randomYawOffset);
            rotation.setPitch(rotation.getPitch() + (float) randomPitchOffset);
        }

        RotationTask newTask = new RotationTask(rotation, onFinish);

        if (currentTask == null || rotation.getPriority() > currentTask.rotation().getPriority() || tick >= totalTicks) {
            currentTask = newTask;
            setupTicks();
        } else {
            rotationQueue.offer(newTask);
        }
    }


    public void onTick() {
        if (currentTask == null || mc.player == null) return;

        Rotation rotation = currentTask.rotation();
        boolean smooth = rotation.isSmooth();

        if (tick < totalTicks && smooth) {
            float t = (float) tick / totalTicks;
            float smoothStep = smoothLerp(t);

            float targetYaw = (float) rotation.getYaw();
            float targetPitch = rotation.getPitch();

            float yaw = interpolateAngle(lastYaw, targetYaw, smoothStep);
            float pitch = interpolateLinear(lastPitch, targetPitch, smoothStep);

            applyRotation(yaw, pitch, rotation.getRotateModes());
            tick++;
        } else {
            applyRotation((float) rotation.getYaw(), rotation.getPitch(), rotation.getRotateModes());
            lastYaw = (float) rotation.getYaw();
            lastPitch = rotation.getPitch();

            if (currentTask.onFinish() != null)
                currentTask.onFinish().run();

            if (!rotationQueue.isEmpty()) {
                currentTask = rotationQueue.poll();
                setupTicks();
            } else {
                currentTask = null;
            }
        }
    }

    private void setupTicks() {
        if (currentTask == null) return;

        Rotation rotation = currentTask.rotation();
        float yawDiff = Math.abs(wrapDegrees((float) rotation.getYaw() - lastYaw));
        float pitchDiff = Math.abs(rotation.getPitch() - lastPitch);
        float maxDiff = Math.max(yawDiff, pitchDiff);
        double speed = rotation.getRotateSpeed();

        if (!rotation.isSmooth()) {
            totalTicks = 1;
        } else {
            totalTicks = (int) Math.max(2, maxDiff / speed * 2.5);
        }

        tick = 0;
    }

    private float interpolateLinear(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private float interpolateAngle(float a, float b, float t) {
        float diff = wrapDegrees(b - a);
        return a + diff * t;
    }

    private float smoothLerp(float t) {
        return t < 0.5f
                ? 4f * t * t * t
                : 1f - (float) Math.pow(-2f * t + 2f, 3f) / 2f;
    }

    private void applyRotation(float yaw, float pitch, RotateModes mode) {
        switch (mode) {
            case Server -> sendServerRotation(yaw, pitch);
            case Client -> applyClientRotation(yaw, pitch);
            case Both -> {
                applyClientRotation(yaw, pitch);
                sendServerRotation(yaw, pitch);
            }
        }
    }

    private float wrapDegrees(float degrees) {
        degrees %= 360;
        if (degrees >= 180) degrees -= 360;
        if (degrees < -180) degrees += 360;
        return degrees;
    }

    private void sendServerRotation(float yaw, float pitch) {
        if (mc.player == null) return;
        Vergence.NETWORK.sendPacket(new PlayerMoveC2SPacket.Full(
                mc.player.getX(),
                mc.player.getY(),
                mc.player.getZ(),
                yaw,
                pitch,
                mc.player.isOnGround(),
                false
        ));
    }

    private void applyClientRotation(float yaw, float pitch) {
        if (mc.player == null) return;
        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
    }

    public Rotation getCurrentRotation() {
        return currentTask != null ? currentTask.rotation() : null;
    }

    private record RotationTask(Rotation rotation, Runnable onFinish) {}
}
