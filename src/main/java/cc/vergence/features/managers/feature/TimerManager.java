package cc.vergence.features.managers.feature;

public class TimerManager {
    public double timer = 1;
    public double lastTime;

    public void set(double factor) {
        if (factor < 0.05) {
            factor = 0.05;
        }
        this.timer = factor;
    }

    public void reset() {
        this.lastTime = this.timer = this.get();
    }

    public void tryReset() {
        if (this.lastTime != this.get()) {
            this.reset();
        }
    }

    public double get() {
        return this.timer;
    }
}
