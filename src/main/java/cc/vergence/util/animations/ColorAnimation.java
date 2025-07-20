package cc.vergence.util.animations;

import cc.vergence.features.managers.client.AnimationManager;

import java.awt.Color;

public class ColorAnimation {
    private Color from;
    private Color to;
    private long startTime;
    private long duration;

    public ColorAnimation(Color from, Color to, long duration) {
        this.from = from;
        this.to = to;
        this.duration = duration;
        this.startTime = System.currentTimeMillis();
        AnimationManager.colorAnimations.add(this);
    }

    public void reset(Color from, Color to) {
        this.from = from;
        this.to = to;
        this.startTime = System.currentTimeMillis();
    }

    public void resetTo(Color to) {
        this.from = getCurrent();
        this.to = to;
        this.startTime = System.currentTimeMillis();
    }

    public Color getCurrent() {
        float progress = (float) (System.currentTimeMillis() - startTime) / duration;
        progress = Math.min(Math.max(progress, 0f), 1f);

        int r = (int) (from.getRed() + (to.getRed() - from.getRed()) * progress);
        int g = (int) (from.getGreen() + (to.getGreen() - from.getGreen()) * progress);
        int b = (int) (from.getBlue() + (to.getBlue() - from.getBlue()) * progress);
        int a = (int) (from.getAlpha() + (to.getAlpha() - from.getAlpha()) * progress);

        return new Color(r, g, b, a);
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setFrom(Color from) {
        this.from = from;
    }

    public void setTo(Color to) {
        this.to = to;
    }
}
