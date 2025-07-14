package cc.vergence.util.animations;

import cc.vergence.features.managers.AnimationManager;

public class GuiAnimation {
    private long startTime;
    private long duration;
    private boolean running;
    private boolean reversed;

    public GuiAnimation(long durationMillis) {
        this.duration = durationMillis;
        this.startTime = System.currentTimeMillis();
        this.running = false;
        this.reversed = false;
        AnimationManager.guiAnimations.add(this);
    }

    public void restart() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
        this.reversed = false;
    }

    public void reset() {
        this.startTime = System.currentTimeMillis() + this.duration;
        this.running = false;
        this.reversed = false;
    }

    public void setToEnd() {
        this.startTime = System.currentTimeMillis() - this.duration;
        this.running = false;
        this.reversed = false;
    }

    public void reverse() {
        if (!running) {
            float currentProgress = getProgressInternal();
            this.startTime = System.currentTimeMillis() + (long) (currentProgress * duration);
            this.running = true;
            this.reversed = true;
        } else {
            this.reversed = !this.reversed;
            float currentProgress = getProgressInternal();
            this.startTime = System.currentTimeMillis() + (long) ((1.0f - currentProgress) * duration);
        }
    }

    public float getProgress() {
        float progress = getProgressInternal();
        if (reversed) {
            return easeOutCubic(1.0f - progress);
        } else {
            return easeOutCubic(progress);
        }
    }

    private float getProgressInternal() {
        long elapsed = System.currentTimeMillis() - startTime;
        float rawProgress = elapsed / (float) duration;
        return Math.max(0.0f, Math.min(1.0f, rawProgress));
    }

    public boolean isRunning() {
        long elapsed = System.currentTimeMillis() - startTime;
        return running && elapsed < duration && elapsed >= 0;
    }

    private float easeOutCubic(float x) {
        return (float) (1 - Math.pow(1 - x, 3));
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}