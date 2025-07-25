package cc.vergence.ui.clickgui.subcomponent.color;

import cc.vergence.Vergence;
import cc.vergence.features.managers.ui.GuiManager;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.ui.GuiComponent;
import cc.vergence.util.render.utils.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import org.joml.Matrix4f;

import java.awt.*;

/**
 * &#064;author: Voury_, OniaCute
 * &#064;version: vergence_1_0_ui_gird
 */
public class ColorPalette extends GuiComponent {
    private ColorOption option;

    public void setOption(ColorOption option) {
        this.option = option;
    }

    public ColorOption getOption() {
        return option;
    }

    public ColorPalette(ColorOption option) {
        this.option = option;
    }

    private Color hsvToColor(float hue, float saturation, float brightness, int alpha) {
        Color color = Color.getHSBColor(hue, saturation, brightness);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    @Override
    public void onDraw(DrawContext context, double mouseX, double mouseY, boolean clickLeft, boolean clickRight) {
        double x = this.getX();
        double y = this.getY();
        double width = this.getWidth();
        double height = this.getHeight();

        Render2DUtil.drawRoundedRectWithOutline(
                context.getMatrices(),
                x - 0.5,
                y - 0.5,
                width + 1,
                height + 1,
                2,
                1,
                Vergence.THEME.getTheme().getColorPaletteBackgroundColor(),
                Vergence.THEME.getTheme().getColorPaletteOutlineColor()
        );

        double hueBarWidth = 10;
        double alphaBarWidth = 10;
        double spacing = 1;

        double svWidth = width - hueBarWidth - alphaBarWidth - spacing * 2;
        double svHeight = height;

        double svX = x;
        double hueX = svX + svWidth + spacing;
        double alphaX = hueX + hueBarWidth + spacing;


        Color currentColor = option.getValue();
        float[] hsb = Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null);
        float hue = hsb[0];
        float sat = hsb[1];
        float bright = hsb[2];
        int alpha = currentColor.getAlpha();
        Matrix4f svMatrix = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder svBufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Render2DUtil.enableRender();

        Color topLeftColor = hsvToColor(hue, 0.0f, 1.0f, 255);
        Color topRightColor = hsvToColor(hue, 1.0f, 1.0f, 255);
        Color bottomLeftColor = hsvToColor(hue, 0.0f, 0.0f, 255);
        Color bottomRightColor = hsvToColor(hue, 1.0f, 0.0f, 255);

        svBufferBuilder.vertex(svMatrix, (float)svX, (float)y, 0.0F).color(topLeftColor.getRGB());
        svBufferBuilder.vertex(svMatrix, (float)svX, (float)(y + svHeight), 0.0F).color(bottomLeftColor.getRGB());
        svBufferBuilder.vertex(svMatrix, (float)(svX + svWidth), (float)(y + svHeight), 0.0F).color(bottomRightColor.getRGB());
        svBufferBuilder.vertex(svMatrix, (float)(svX + svWidth), (float)y, 0.0F).color(topRightColor.getRGB());
        BufferRenderer.drawWithGlobalProgram(svBufferBuilder.end());
        Render2DUtil.disableRender();
        Matrix4f hueMatrix = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder hueBufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        Render2DUtil.enableRender();

        float segmentHeight = (float)svHeight / 6.0f;
        for (int i = 0; i < svHeight; i++) {
            float h = i / (float) svHeight;
            Color c = hsvToColor(h, 1.0f, 1.0f, 255);
            hueBufferBuilder.vertex(hueMatrix, (float)hueX, (float)(y + i), 0.0F).color(c.getRGB());
            hueBufferBuilder.vertex(hueMatrix, (float)hueX, (float)(y + i + 1), 0.0F).color(c.getRGB());
            hueBufferBuilder.vertex(hueMatrix, (float)(hueX + hueBarWidth), (float)(y + i + 1), 0.0F).color(c.getRGB());
            hueBufferBuilder.vertex(hueMatrix, (float)(hueX + hueBarWidth), (float)(y + i), 0.0F).color(c.getRGB());
        }
        BufferRenderer.drawWithGlobalProgram(hueBufferBuilder.end());
        Render2DUtil.disableRender();

        Render2DUtil.verticalGradient(
                context.getMatrices(),
                (float)alphaX,
                (float)y,
                (float)(alphaX + alphaBarWidth),
                (float)(y + svHeight),
                new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), 255),
                new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), 0)
        );

        if (clickLeft) {
            if (mouseX >= svX && mouseX <= svX + svWidth && mouseY >= y && mouseY <= y + svHeight) {
                float s = (float)((mouseX - svX) / svWidth);
                float b = 1.0f - (float)((mouseY - y) / svHeight);
                option.setValue(hsvToColor(hue, s, b, alpha));
            }
            else if (mouseX >= hueX && mouseX <= hueX + hueBarWidth && mouseY >= y && mouseY <= y + svHeight) {
                float newHue = (float)((mouseY - y) / svHeight);
                option.setValue(hsvToColor(newHue, sat, bright, alpha));
            }
            else if (mouseX >= alphaX && mouseX <= alphaX + alphaBarWidth && mouseY >= y && mouseY <= y + svHeight) {
                int newAlpha = 255 - (int)((mouseY - y) / svHeight * 255);
                option.setValue(new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), newAlpha));
            }
            else if (GuiManager.CLICKED_LEFT && !isHovered(mouseX, mouseY)) {
                ((ColorPreviewer) getParentComponent()).isSpread = false;
                GuiManager.CLICKED_LEFT = false;
            }
        }
    }
}
