package cc.vergence.features.managers.player;

import cc.vergence.Vergence;
import cc.vergence.features.enums.player.RotateModes;
import cc.vergence.features.event.eventbus.EventHandler;
import cc.vergence.features.event.events.MoveEvent;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.event.events.RotateEvent;
import cc.vergence.features.event.events.SyncEvent;
import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.combat.CombatUtil;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.rotation.Rotation;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.PriorityQueue;

import static java.lang.Integer.MAX_VALUE;

public class RotateManager implements Wrapper {
    private final PriorityQueue<RotationTask> rotationQueue = new PriorityQueue<>();
    private RotationTask currentTask = null;

    private float lastYaw, lastPitch, serverYaw, serverPitch;
    private long lastRenderTime;
    private long rotationExpireTime = -1;
    public boolean overrideServerRotation = false;


    public RotateManager() {
        Vergence.EVENTBUS.subscribe(this);
    }

    @EventHandler
    public void onSync(SyncEvent event) {
        if (overrideServerRotation) {
            if (System.currentTimeMillis() >= rotationExpireTime) {
                overrideServerRotation = false;
            } else {
                return;
            }
        }

        serverPitch = event.getPitch();
        serverYaw = event.getYaw();
        Vergence.EVENTBUS.post(new RotateEvent(serverYaw, serverPitch));
    }

    @EventHandler
    public void onReceivePackets(PacketEvent.Receive event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet && packet.changesLook()) {
            float packetYaw = packet.getYaw(0.0f);
            float packetPitch = packet.getPitch(0.0f);
            serverYaw = packetYaw;
            serverPitch = packetPitch;
            Vergence.EVENTBUS.post(new RotateEvent(packetYaw, packetPitch));
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

        if (currentTask == null || rotation.getPriority() >= currentTask.rotation().getPriority()) {
            rotationExpireTime = -1;
            overrideServerRotation = false;
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
            case Server -> sendServerRotation(yaw, pitch, rot.isSnap());
            case Both -> {
                applyClientRotation(yaw, pitch);
                sendServerRotation(yaw, pitch, rot.isSnap());
            }
        }

        lastYaw = yaw;
        lastPitch = pitch;
    }

    private void sendServerRotation(float yaw, float pitch, boolean isSnap) {
        Vergence.NETWORK.sendPacket(new PlayerMoveC2SPacket.Full(
                mc.player.getX(), mc.player.getY(), mc.player.getZ(), yaw, pitch,
                mc.player.isOnGround(), false
        ));
        if (!isSnap) {
            this.rotationExpireTime = System.currentTimeMillis() + AntiCheat.INSTANCE.rotateTime.getValue().longValue();
            this.overrideServerRotation = true;
        } else {
            this.rotationExpireTime = -1;
            this.overrideServerRotation = false;
        }
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

    public float[] getRotation(Vec3d vec) {
        Vec3d eyesPos = mc.player.getEyePos();
        return getRotation(eyesPos, vec);
    }

    public float[] getRotation(Vec3d eyesPos, Vec3d vec) {
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch)};
    }

    public boolean inFov(Vec3d directionVec, float fov) {
        float[] angle = getRotation(new Vec3d(mc.player.getX(), mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()), mc.player.getZ()), directionVec);
        return inFov(angle[0], angle[1], fov);
    }

    public boolean inFov(float yaw, float pitch, float fov) {
        return MathHelper.angleBetween(yaw, serverYaw) + Math.abs(pitch - serverPitch) <= fov;
    }

    private record RotationTask(Rotation rotation, Runnable onFinish) implements Comparable<RotationTask> {
        @Override
        public int compareTo(RotationTask other) {
            return -Integer.compare(rotation.getPriority(), other.rotation.getPriority());
        }
    }
}