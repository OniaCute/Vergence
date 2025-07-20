package cc.vergence.features.managers.player;

import cc.vergence.Vergence;
import cc.vergence.features.enums.RotateModes;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.maths.Easing;
import cc.vergence.util.rotation.Rotation;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.PriorityQueue;

public class RotateManager implements Wrapper {
    private final PriorityQueue<RotationTask> rotationQueue = new PriorityQueue<>();
    private RotationTask currentTask = null;

    private float lastYaw, lastPitch;
    private int tick, totalTicks;
    private long lastRenderTime;

    public boolean inRenderTime() {
        return System.currentTimeMillis() - lastRenderTime < 1000;
    }

    public void rotate(Rotation rotation) {
        rotate(rotation, () -> {});
    }

    public void rotate(Rotation rotation, Runnable onFinish) {
        if (mc.player == null) return;

        if (currentTask == null || rotation.getPriority() > currentTask.rotation().getPriority() || tick >= totalTicks) {
            lastYaw = mc.player.getYaw();
            lastPitch = mc.player.getPitch();

            currentTask = new RotationTask(rotation, onFinish);
            setupTicks();
        } else {
            rotationQueue.offer(new RotationTask(rotation, onFinish));
        }
    }

    public void onTick() {
        if (currentTask == null || mc.player == null) {
            return;
        }
        Rotation rot = currentTask.rotation();

        if (tick < totalTicks && rot.isSmooth()) {
            float t = (float) tick / totalTicks;
            float smoothed = smoothLerp(t);

            float targetYaw = (float) rot.getYaw();
            float targetPitch = rot.getPitch();

            float yaw = interpolateAngle(lastYaw, targetYaw, smoothed);
            float pitch = interpolateLinear(lastPitch, targetPitch, smoothed);

            applyRotation(yaw, pitch, rot.getRotateModes());
            tick++;
        } else {
            applyRotation((float) rot.getYaw(), rot.getPitch(), rot.getRotateModes());

            lastYaw = (float) rot.getYaw();
            lastPitch = rot.getPitch();

            if (currentTask.onFinish() != null) currentTask.onFinish().run();

            currentTask = rotationQueue.poll();
            setupTicks();
        }

        lastRenderTime = System.currentTimeMillis();
    }

    private void setupTicks() {
        if (currentTask == null) return;

        Rotation rot = currentTask.rotation();
        float yawDiff = Math.abs(wrapDegrees((float) rot.getYaw() - lastYaw));
        float pitchDiff = Math.abs(rot.getPitch() - lastPitch);
        float maxDiff = Math.max(yawDiff, pitchDiff);

        totalTicks = rot.isSmooth() ? Math.max(2, (int) (maxDiff / rot.getRotateSpeed() * 2.5)) : 1;
        tick = 0;
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

    private void sendServerRotation(float yaw, float pitch) {
        Vergence.NETWORK.sendPacket(new PlayerMoveC2SPacket.Full(
                mc.player.getX(), mc.player.getY(), mc.player.getZ(), yaw, pitch,
                mc.player.isOnGround(), false
        ));
    }

    private void applyClientRotation(float yaw, float pitch) {
        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
    }

    public float[] getRenderRotations() {
        Rotation rot = currentTask != null ? currentTask.rotation() : null;
        float from = wrapDegrees(lastYaw);
        float to = wrapDegrees(rot != null ? (float) rot.getYaw() : mc.player.getYaw());
        float delta = wrapDegrees(to - from);

        float yaw = interpolateAngle(lastYaw, from + delta, smoothLerp(Easing.toDelta(lastRenderTime, 1000)));
        float pitch = interpolateLinear(lastPitch, rot != null ? rot.getPitch() : mc.player.getPitch(), Easing.toDelta(lastRenderTime, 1000));
        return new float[]{yaw, pitch};
    }

    private float wrapDegrees(float degrees) {
        degrees %= 360;
        if (degrees >= 180) degrees -= 360;
        if (degrees < -180) degrees += 360;
        return degrees;
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

    private record RotationTask(Rotation rotation, Runnable onFinish) implements Comparable<RotationTask> {
        @Override
        public int compareTo(RotationTask other) {
            return -Integer.compare(rotation.getPriority(), other.rotation.getPriority());
        }
    }
}
