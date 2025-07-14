package cc.vergence.util.other;

public class FastTimerUtil {
    private long time;

    public FastTimerUtil() {
        reset();
    }

    public void reset() {
        time = System.currentTimeMillis();
    }

    public double getGapMs() {
        return (System.currentTimeMillis() - time);
    }

    public double getGapS() {
        return (System.currentTimeMillis() - time) / 1000.0;
    }

    public boolean passedMs(double ms) {
        return System.currentTimeMillis() - time >= ms;
    }

    public boolean passedS(double s) {
        return System.currentTimeMillis() - time >= s * 1000.0;
    }
}