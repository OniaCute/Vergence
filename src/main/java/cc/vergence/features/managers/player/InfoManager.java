package cc.vergence.features.managers.player;

import cc.vergence.Vergence;
import cc.vergence.modules.exploit.FastLatencyCalc;
import cc.vergence.modules.hud.TargetHud;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.player.EntityUtil;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;


public class InfoManager implements Wrapper {
    private float speed, speedPerS;
    private int currentFPS = 0;
    private int spentMemory = 0, maxMemory = 0;
    private Vec3d lastPosition;
    private int combo = 0;
    private long lastAttackTime = 0;
    private UUID lastTargetId = null;
    private int leftClicks = 0;
    private int rightClicks = 0;
    private long lastSecondTime = 0;
    private long gameTime = 0;
    private long startTime = 0;
    private float combat_distance = 0;

    public InfoManager() {
        Vergence.EVENTBUS.subscribe(this);
    }

    public void onTick() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        updateSpeed();
        updateMemory();
        updateCombo();
        updateGameTime();
    }

    public void onDraw2D() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        updateFps();
        updateCPS();
    }

    private void updateSpeed() {
        Vec3d currentPosition = mc.player.getPos();
        if (lastPosition == null) {
            lastPosition = currentPosition;
        }

        double deltaX = currentPosition.x - lastPosition.x;
        double deltaY = currentPosition.y - lastPosition.y;
        double deltaZ = currentPosition.z - lastPosition.z;

        double totalDistance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        double speedMetersPerSecond = totalDistance / 0.05;
        speedPerS = (float) speedMetersPerSecond;
        speed = (float) (speedMetersPerSecond * 3.6);

        lastPosition = currentPosition;
    }

    private void updateFps() {
        float targetFPS = mc.getCurrentFps();
        if (currentFPS < targetFPS) {
            currentFPS += (int) ((targetFPS - currentFPS) / 3);
        } else if (currentFPS > targetFPS) {
            currentFPS -= (int) ((currentFPS - targetFPS) / 3);
        }
    }

    private void updateMemory() {
        int targetSpentMemoryBytes = (int) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        int targetMaxMemoryBytes = (int) Runtime.getRuntime().totalMemory();
        int targetSpentMemoryMB = targetSpentMemoryBytes / (1024 * 1024);
        int targetMaxMemoryMB = targetMaxMemoryBytes / (1024 * 1024);
        if (spentMemory < targetSpentMemoryMB) {
            spentMemory += (targetSpentMemoryMB - spentMemory) / 3;
        } else if (spentMemory > targetSpentMemoryMB) {
            spentMemory -= (spentMemory - targetSpentMemoryMB) / 3;
        }
        if (maxMemory < targetMaxMemoryMB) {
            maxMemory += (targetMaxMemoryMB - maxMemory) / 3;
        } else if (maxMemory > targetMaxMemoryMB) {
            maxMemory -= (maxMemory - targetMaxMemoryMB) / 3;
        }
    }

    public int getPing() {
        if (mc.player == null || mc.world == null || mc.player.getUuid() == null || mc.player.networkHandler == null || mc.player.networkHandler.getPlayerListEntry(mc.player.getUuid()) == null) {
            return 0;
        }

        ClientPlayNetworkHandler networkHandler = mc.player.networkHandler;
        return FastLatencyCalc.INSTANCE != null && FastLatencyCalc.INSTANCE.getStatus() ?
                FastLatencyCalc.INSTANCE.getLatency() :
                networkHandler.getPlayerListEntry(mc.player.getUuid()).getLatency();
    }

    private void updateCombo() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime > 7000) { // 7s
            combo = 0;
            lastTargetId = null;
        }
        if (currentTime - lastAttackTime > 12000) { // 12s
            combat_distance = 0;
        }
    }

    private void updateCPS() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSecondTime >= 1000) {
            leftClicks = 0;
            rightClicks = 0;
            lastSecondTime = currentTime;
        }
    }

    public void startGameTime() {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
    }

    private void updateGameTime() {
        gameTime = System.currentTimeMillis() - startTime;
    }

    public void resetGameTime() {
        startTime = 0;
        gameTime = 0;
    }

    public void onLeftClick() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        leftClicks++;
    }

    public void onRightClick() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        rightClicks++;
    }

    public void onAttack(LivingEntity target, boolean cancelled) {
        if (mc.player == null || mc.world == null || target == null) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        UUID targetId = target.getUuid();
        if (lastTargetId == null || !lastTargetId.equals(targetId)) {
            combo = 1;
        } else {
            if (currentTime - lastAttackTime > 40) { // 2 ticks check
                combo++;
            }
        }

        if (!target.canHit() || !target.canTakeDamage() || cancelled) {
            combo = 0;
            combat_distance = 0;
        }

        lastAttackTime = currentTime;
        lastTargetId = targetId;
        combat_distance = EntityUtil.getDistance(target);
    }

    public void onHurt() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        combo = 0;
        lastTargetId = null;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public int getSpentMemory() {
        return spentMemory;
    }

    public int getCurrentFPS() {
        return currentFPS;
    }

    public float getSpeed() {
        return speed;
    }

    public float getSpeedPerS() {
        return speedPerS;
    }

    public int getCombo() {
        return combo;
    }

    public int getLeftClicks() {
        return leftClicks;
    }

    public int getRightClicks() {
        return rightClicks;
    }

    public long getGameTime() {
        return gameTime;
    }

    public String getGameTimeFormatted() {
        long hours = (gameTime / 1000) / 3600;
        long minutes = ((gameTime / 1000) % 3600) / 60;
        long seconds = (gameTime / 1000) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public float getCombatDistance() {
        return combat_distance;
    }
}