package cc.vergence.features.notifications;

import cc.vergence.modules.client.Notify;
import cc.vergence.util.animations.NotificationAnimation;
import net.minecraft.client.gui.DrawContext;

public abstract class Notification {
    protected double x, y, width, height;
    protected String text;
    protected double aliveTime, fullAliveTime;
    protected boolean exiting = false;
    protected double targetX, targetY;
    protected final NotificationAnimation animation;

    public Notification() {
        this.animation = new NotificationAnimation(Notify.INSTANCE.animationTime.getValue().longValue());
    }

    public void initAnimation() {
        animation.startIn();
    }

    public void initPosition(double baseX, boolean alignRight, boolean popUp, double screenHeight, double padding) {
        double startX = alignRight ? baseX + getWidth() : baseX - getWidth();
        double startY = popUp ? padding - getHeight() : screenHeight + getHeight() + padding;

        this.x = startX;
        this.y = startY;
        this.targetX = alignRight ? baseX - getWidth() : baseX;
        this.targetY = popUp ? padding : screenHeight - padding;
    }


    public void updateAnimation() {
        animation.update();
    }

    public boolean isDead() {
        return exiting && animation.isOutComplete();
    }

    public boolean shouldStartOut() {
        return !exiting && aliveTime <= 0;
    }

    public void startExit() {
        this.exiting = true;
        animation.startOut();
    }

    public boolean isExiting() {
        return exiting;
    }

    public double getAliveTime() {
        return aliveTime;
    }

    public void reduceAliveTime(double tick) {
        this.aliveTime -= tick;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public NotificationAnimation getAnimation() {
        return animation;
    }

    public void setTargetX(double targetX) {
        this.targetX = targetX;
    }
    public void setTargetY(double targetY) {
        this.targetY = targetY;
    }

    public double getTargetX() {
        return targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public abstract void onDraw2D(DrawContext context, float tickDelta);
}
