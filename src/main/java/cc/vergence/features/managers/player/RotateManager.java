package cc.vergence.features.managers.player;

import cc.vergence.Vergence;
import cc.vergence.features.event.eventbus.EventHandler;
import cc.vergence.features.event.events.MoveEvent;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.event.events.SyncEvent;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.rotation.Rotation;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;

import java.util.PriorityQueue;

public class RotateManager implements Wrapper {
    private final PriorityQueue<RotationTask> rotationQueue = new PriorityQueue<>();
    private RotationTask currentTask = null;

    private float lastYaw, lastPitch, serverYaw, serverPitch;
    private long lastRenderTime;

    public RotateManager() {
        Vergence.EVENTBUS.subscribe(this);
    }

    @EventHandler
    public void onSync(SyncEvent event) {
        serverPitch = event.getPitch();
        serverYaw = event.getYaw();

    }

    @EventHandler
    public void onReceivePackets(PacketEvent.Receive event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet && packet.changesLook()) {
            float packetYaw = packet.getYaw(0.0f);
            float packetPitch = packet.getPitch(0.0f);
            serverYaw = packetYaw;
            serverPitch = packetPitch;
        }
    }

    public float getServerPitch() {
        return serverPitch;
    }

    public float getServerYaw() {
        return serverYaw;
    }

    public float getWrappedYaw() {
        return MathHelper.wrapDegrees(serverYaw);
    }

    public void snapBack() {
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.Full(mc.player.getX(), mc.player.getY(), mc.player.getZ(), serverYaw, serverPitch, mc.player.isOnGround(), false));
    }

    public boolean inRenderTime() {
        return System.currentTimeMillis() - lastRenderTime < 1000;
    }

    public void rotate(Rotation rotation) {
        rotate(rotation, () -> {});
    }

    public void rotate(Rotation rotation, Runnable onFinish) {
        if (mc.player == null) {
            return;
        }

        if (currentTask == null || rotation.getPriority() > currentTask.rotation().getPriority()) {
            lastYaw = mc.player.getYaw();
            lastPitch = mc.player.getPitch();

            currentTask = new RotationTask(rotation, onFinish);
            applyCurrent();
        } else {
            rotationQueue.offer(new RotationTask(rotation, onFinish));
        }
    }

    public void onTick() {
        if (mc.player == null || currentTask == null) {
            return;
        }

        applyCurrent();

        if (currentTask.onFinish() != null) currentTask.onFinish().run();
        currentTask = rotationQueue.poll();

        if (currentTask != null) {
            lastYaw = mc.player.getYaw();
            lastPitch = mc.player.getPitch();
            applyCurrent();
        }

        lastRenderTime = System.currentTimeMillis();
    }

    private void applyCurrent() {
        if (currentTask == null || mc.player == null) return;

        Rotation rot = currentTask.rotation();
        float yaw = (float) rot.getYaw();
        float pitch = rot.getPitch();

        switch (rot.getRotateModes()) {
            case Client -> applyClientRotation(yaw, pitch);
            case Server -> sendServerRotation(yaw, pitch);
            case Both -> {
                applyClientRotation(yaw, pitch);
                sendServerRotation(yaw, pitch);
            }
        }

        lastYaw = yaw;
        lastPitch = pitch;
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
        float yaw = currentTask != null ? (float) currentTask.rotation().getYaw() : mc.player.getYaw();
        float pitch = currentTask != null ? currentTask.rotation().getPitch() : mc.player.getPitch();
        return new float[] { yaw, pitch };
    }

    private record RotationTask(Rotation rotation, Runnable onFinish) implements Comparable<RotationTask> {
        @Override
        public int compareTo(RotationTask other) {
            return -Integer.compare(rotation.getPriority(), other.rotation.getPriority());
        }
    }
}