package cc.vergence.features.notifications;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Aligns;
import cc.vergence.features.enums.FontSize;
import cc.vergence.features.managers.NotifyManager;
import cc.vergence.features.managers.ThemeManager;
import cc.vergence.modules.client.Notify;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

public class SimpleNotification extends Notification{
    public SimpleNotification(String text, double aliveTime) {
        this.text = text;
        this.aliveTime = aliveTime;
    }

    @Override
    public void onDraw2D(DrawContext context, float tickDelta) {
        if (Notify.INSTANCE.rounded.getValue()) {
            Render2DUtil.drawRoundedRect(
                    context.getMatrices(),
                    NotifyManager.nextNotificationPos.getA(),
                    NotifyManager.nextNotificationPos.getB(),
                    FontUtil.getWidth(FontSize.MEDIUM, this.text),
                    FontUtil.getHeight(FontSize.MEDIUM),
                    Notify.INSTANCE.radius.getValue(),
                    Vergence.THEME.getTheme().getNotificationBackgroundColor()
            );
        } else {
            Render2DUtil.drawRect(
                    context,
                    NotifyManager.nextNotificationPos.getA() - 2,
                    NotifyManager.nextNotificationPos.getB() - 2,
                    FontUtil.getWidth(FontSize.MEDIUM, this.text) + 4,
                    FontUtil.getHeight(FontSize.MEDIUM) + 4,
                    Vergence.THEME.getTheme().getNotificationBackgroundColor()
            );
        }

        FontUtil.drawTextWithAlign(
                context,
                text,
                NotifyManager.nextNotificationPos.getA(),
                NotifyManager.nextNotificationPos.getB(),
                FontUtil.getWidth(FontSize.MEDIUM, this.text),
                FontUtil.getHeight(FontSize.MEDIUM),
                Aligns.LEFT,
                Vergence.THEME.getTheme().getNotificationTextColor(),
                FontSize.MEDIUM);
    }
}
