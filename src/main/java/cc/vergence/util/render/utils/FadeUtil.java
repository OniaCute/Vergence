package cc.vergence.util.render.utils;

public class FadeUtil {
    protected long start;
    public long length;

    public FadeUtil(final long ms) {
        this.length = ms;
        this.reset();
    }

    public FadeUtil reset() {
        this.start = System.currentTimeMillis();
        return this;
    }

    public boolean isEnd() {
        return this.getTime() >= this.length;
    }

    protected long getTime() {
        return System.currentTimeMillis() - this.start;
    }
    public long GetTime(){
        return getTime();
    }

    public void setLength(final long length) {
        this.length = length;
    }

    public double getFadeOne() {
        return this.isEnd() ? 1.0 : (this.getTime() / (double) this.length);
    }

    public double getFadeInDefault() {
        return Math.tanh(this.getTime() / (double) this.length * 3.0);
    }

    public double getFadeOutDefault() {
        return 1.0 - Math.tanh(this.getTime() / (double) this.length * 3.0);
    }

    public double getFadeIn() {
        return 1.0 - Math.sin(1.5707963267948966 * this.getFadeOne()) * Math.sin(2.5132741228718345 * this.getFadeOne());
    }

    public double getFadeOut() {
        return Math.sin(1.5707963267948966 * this.getFadeOne()) * Math.sin(2.5132741228718345 * this.getFadeOne());
    }

    public double easeOutQuad() {
        return 1.0 - (1.0 - this.getFadeOne()) * (1.0 - this.getFadeOne());
    }

    public double easeInQuad() {
        return this.getFadeOne() * this.getFadeOne();
    }

    public double def() {
        return this.isEnd() ? 1.0 : this.getFadeOne();
    }


    public double getQuad(Quad quad) {
        switch (quad) {
            case QuadIn -> {
                return easeInQuad();
            }
            case FadeIn -> {
                return getFadeInDefault();
            }
            case QuadOut -> {
                return easeOutQuad();
            }
        }
        return easeOutQuad();
    }

    public enum Quad {
        QuadIn,
        FadeIn,
        QuadOut,
    }
}
