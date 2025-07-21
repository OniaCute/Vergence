package cc.vergence.util.animations;

public class NotificationAnimation {
    private final long duration;
    private long startTime;
    private State state;

    public enum State {
        IN, OUT, IDLE
    }

    public NotificationAnimation(long duration) {
        this.duration = duration;
        this.state = State.IDLE;
        this.startTime = System.currentTimeMillis();
    }

    public void startIn() {
        this.state = State.IN;
        this.startTime = System.currentTimeMillis();
    }

    public void startOut() {
        this.state = State.OUT;
        this.startTime = System.currentTimeMillis();
    }

    public void update() {
    }

    public boolean isOutComplete() {
        return state == State.OUT && getRawProgress() >= 1.0;
    }

    private double getRawProgress() {
        long elapsed = System.currentTimeMillis() - startTime;
        return Math.min(1.0, elapsed / (double) duration);
    }

    public double getProgressX() {
        double t = getRawProgress();
        return switch (state) {
            case IN -> easeOutCubic(t);
            case OUT -> 1.0 - easeInCubic(t);
            default -> 1.0;
        };
    }

    public double getProgressY() {
        double t = getRawProgress();
        return switch (state) {
            case IN -> easeOutQuad(t);
            case OUT -> 1.0;
            default -> 1.0;
        };
    }

    private double easeOutQuad(double t) {
        return 1 - (1 - t) * (1 - t);
    }

    private double easeOutCubic(double t) {
        return 1 - Math.pow(1 - t, 3);
    }

    private double easeInCubic(double t) {
        return t * t * t;
    }

    public State getState() {
        return state;
    }
}
