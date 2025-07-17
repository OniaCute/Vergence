package cc.vergence.util.animations;

public class SimpleAnimation {
    private double start;
    private double end;
    private long startTime;
    private long duration;

    public SimpleAnimation(double initialValue, long durationMillis) {
        this.start = initialValue;
        this.end = initialValue;
        this.duration = durationMillis;
        this.startTime = System.currentTimeMillis();
    }

    public void to(double newEnd) {
        this.start = get();
        this.end = newEnd;
        this.startTime = System.currentTimeMillis();
    }

    public void set(double value) {
        this.start = value;
        this.end = value;
        this.startTime = System.currentTimeMillis() - duration;
    }

    public double get() {
        double elapsed = System.currentTimeMillis() - startTime;
        if (elapsed >= duration) return end;

        double progress = Math.min(1.0, (double) elapsed / duration);
        double eased = 1 - Math.pow(1 - progress, 3);
        return start + (end - start) * eased;
    }

    public boolean isActive() {
        return get() != end;
    }

    public double getTarget() {
        return end;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
