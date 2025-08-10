package cc.vergence.features.notifications;

import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.modules.client.ClickGUI;
import cc.vergence.modules.client.Notify;
import cc.vergence.util.font.FontUtil;
import cc.vergence.util.render.utils.NewRender2DUtil;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;

public class SimpleNotification extends Notification {
    public SimpleNotification(String text, double aliveTime) {
        this.text = text;
        this.aliveTime = aliveTime;
        this.fullAliveTime = aliveTime;
        setHeight(FontUtil.getHeight(FontSize.MEDIUM) + 2);
        setWidth(FontUtil.getWidth(FontSize.MEDIUM, text) + 4);
    }

    @Override
    public void onDrawSkia(DrawContext context, float tickDelta) {
        if (Notify.INSTANCE.blur.getValue()) {
            if (Notify.INSTANCE.rounded.getValue()) {
                NewRender2DUtil.drawRoundedBlur(
                        x,
                        y,
                        width,
                        height,
                        Notify.INSTANCE.radius.getValue()
                );
            } else {
                NewRender2DUtil.drawBlur(
                        x,
                        y,
                        width,
                        height
                );
            }
        }
    }

    @Override
    public void onDraw2D(DrawContext context, float tickDelta) {
        double x = getX();
        double y = getY();
        double width = getWidth();
        double height = getHeight();
        double barHeight = Notify.INSTANCE.aliveTimeWidth.getValue();
        if (Notify.INSTANCE.rounded.getValue()) {
            Render2DUtil.drawRoundedRect(
                    context.getMatrices(),
                    x,
                    y,
                    width,
                    height,
                    Notify.INSTANCE.radius.getValue(),
                    Notify.INSTANCE.backgroundColor.getValue()
            );
        } else {
            Render2DUtil.drawRect(
                    context,
                    x,
                    y,
                    width,
                    height,
                    Notify.INSTANCE.backgroundColor.getValue()
            );
        }
        double progress = aliveTime / fullAliveTime;
        double barWidth = width * progress;
        barWidth = Math.max(0, Math.min(barWidth, width));
        boolean alignRight = Notify.INSTANCE.align.getValue().equals(Notify.Aligns.Right);
        double barX = alignRight
                ? x + width - barWidth
                : x;
        Render2DUtil.drawRoundedRect(
                context.getMatrices(),
                barX,
                y + height - barHeight,
                barWidth,
                barHeight,
                Notify.INSTANCE.aliveTimeRadius.getValue(),
                Notify.INSTANCE.aliveTimeColor.getValue()
        );
        FontUtil.drawTextWithAlign(
                context,
                text,
                x + 1,
                y + 2,
                x + width,
                y + height,
                Aligns.LEFT,
                Notify.INSTANCE.textColor.getValue(),
                FontSize.MEDIUM
        );
    }
}
