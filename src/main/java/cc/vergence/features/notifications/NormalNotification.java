package cc.vergence.features.notifications;

import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.modules.client.Notify;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

public class NormalNotification extends Notification {
    private String description;

    public NormalNotification(String title, String description, double aliveTime) {
        this.text = title;
        this.description = description;
        this.fullAliveTime = aliveTime;
        this.aliveTime = aliveTime;
        double titleHeight = FontUtil.getHeight(FontSize.MEDIUM);
        double descHeight = FontUtil.getHeight(FontSize.SMALL);
        double totalHeight = titleHeight + descHeight;
        double maxWidth = Math.max(FontUtil.getWidth(FontSize.MEDIUM, title), FontUtil.getWidth(FontSize.SMALL, description)) + 4;
        setHeight(totalHeight);
        setWidth(maxWidth);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void onDraw2D() {
        double x = getX();
        double y = getY();
        double width = getWidth();
        double height = getHeight();
        double barHeight = Notify.INSTANCE.aliveTimeWidth.getValue();
        if (Notify.INSTANCE.rounded.getValue()) {
            Render2DUtil.drawRoundedRect(
                    x,
                    y,
                    width,
                    height,
                    Notify.INSTANCE.radius.getValue(),
                    Notify.INSTANCE.backgroundColor.getValue()
            );
        } else {
            Render2DUtil.drawRect(
                    x,
                    y,
                    width,
                    height,
                    Notify.INSTANCE.backgroundColor.getValue()
            );
        }

        FontUtil.drawTextWithAlign(
                text,
                x + 2,
                y - 4,
                x + width,
                y + height,
                Notify.INSTANCE.titleColor.getValue(),
                FontSize.MEDIUM,
                Aligns.LEFT
        );

        double titleHeight = FontUtil.getHeight(FontSize.MEDIUM);
        FontUtil.drawTextWithAlign(
                description,
                x + 2,
                y + titleHeight + 1,
                x + width,
                y + height,
                Notify.INSTANCE.textColor.getValue(),
                FontSize.SMALL,
                Aligns.LEFT
        );

        double progress = aliveTime / fullAliveTime;
        double barWidth = width * progress;
        barWidth = Math.max(0, Math.min(barWidth, width));
        boolean alignRight = Notify.INSTANCE.align.getValue().equals(Notify.Aligns.Right);
        double barX = alignRight ? x + width - barWidth : x;
        Render2DUtil.drawRoundedRect(
                barX,
                y + height - barHeight,
                barWidth,
                barHeight,
                Notify.INSTANCE.aliveTimeRadius.getValue(),
                Notify.INSTANCE.aliveTimeColor.getValue()
        );
    }
}
