package cc.vergence.features.managers.ui;

import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.notifications.*;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.Notify;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.Iterator;

public class NotifyManager implements Wrapper {
    private static final Object lock = new Object();
    public static final ArrayList<Notification> notifications = new ArrayList<>();

    public static void newNotification(String text) {
        if (!FontUtil.LOADED) {
            return;
        }

        double alive = Notify.INSTANCE.notificationAliveTime.getValue();
        SimpleNotification simple = new SimpleNotification(text, alive);
        simple.initAnimation();

        double padding = Notify.INSTANCE.padding.getValue();
        boolean alignRight = Notify.INSTANCE.align.getValue() == Notify.Aligns.Right;
        boolean popUp = Notify.INSTANCE.popType.getValue() == Notify.PopTypes.UpToDown;
        double screenWidth = mc.getWindow().getScaledWidth();
        double screenHeight = mc.getWindow().getScaledHeight();
        double baseX = alignRight ? screenWidth - padding : padding;

        simple.initPosition(baseX, alignRight, popUp, screenHeight, padding);
        notifications.add(simple);
    }

    public static void newNotification(Module module, String description) {
        newNotification(module.getDisplayName(), description);
    }

    public static void newNotification(String title, String description) {
        if (!FontUtil.LOADED) {
            return;
        }

        double alive = Notify.INSTANCE.notificationAliveTime.getValue();
        NormalNotification normal = new NormalNotification(title, description, alive);
        normal.initAnimation();

        double padding = Notify.INSTANCE.padding.getValue();
        boolean alignRight = Notify.INSTANCE.align.getValue() == Notify.Aligns.Right;
        boolean popUp = Notify.INSTANCE.popType.getValue() == Notify.PopTypes.UpToDown;
        double screenWidth = mc.getWindow().getScaledWidth();
        double screenHeight = mc.getWindow().getScaledHeight();
        double baseX = alignRight ? screenWidth - padding : padding;

        normal.initPosition(baseX, alignRight, popUp, screenHeight, padding);
        notifications.add(normal);
    }

    public static void onTick() {
        if (Notify.INSTANCE == null) {
            return;
        }
        if (mc.player == null || mc.world == null) {
            synchronized (lock) {
                notifications.clear();
            }
            return;
        }
        synchronized (lock) {
            Iterator<Notification> iterator = notifications.iterator();
            while (iterator.hasNext()) {
                Notification n = iterator.next();
                if (n.isDead()) {
                    iterator.remove();
                } else {
                    if (n.shouldStartOut()) {
                        n.startExit();
                    }
                    else {
                        n.reduceAliveTime(1);
                    }
                }
            }
        }
    }

    public static void onDraw2D() {
        if (Notify.INSTANCE == null || !Notify.INSTANCE.getStatus()) {
            return;
        }

        double padding = Notify.INSTANCE.padding.getValue();
        boolean alignRight = Notify.INSTANCE.align.getValue() == Notify.Aligns.Right;
        boolean popUp = Notify.INSTANCE.popType.getValue() == Notify.PopTypes.UpToDown;
        double screenWidth = mc.getWindow().getScaledWidth();
        double screenHeight = mc.getWindow().getScaledHeight();
        double baseX = alignRight ? screenWidth - padding : padding;
        double accumulatedY = popUp ? padding : screenHeight - padding;
        double lerpSpeed = 0.2;
        synchronized (lock) {
            for (Notification notification : notifications) {
                notification.updateAnimation();
                double pX = notification.getAnimation().getProgressX();
                double pY = notification.getAnimation().getProgressY();
                double horizontalOffset = (1 - pX) * notification.getWidth() * (alignRight ? 1 : -1);
                double targetPosY = popUp ? accumulatedY : accumulatedY - notification.getHeight();
                double verticalOffset = (1 - pY) * (notification.getHeight() + padding) * (popUp ? -1 : 1);
                double animPosY = targetPosY + verticalOffset;
                double targetPosX = baseX + (alignRight ? -notification.getWidth() : 0) + horizontalOffset;
                notification.setTargetX(targetPosX);
                notification.setTargetY(animPosY);
                notification.setX(lerp(notification.getX(), notification.getTargetX(), lerpSpeed));
                notification.setY(lerp(notification.getY(), notification.getTargetY(), lerpSpeed));
                accumulatedY = popUp
                        ? accumulatedY + notification.getHeight() + padding
                        : accumulatedY - notification.getHeight() - padding;
                notification.onDraw2D();
            }
        }
    }

    private static double lerp(double start, double end, double amount) {
        return start + (end - start) * amount;
    }
}
