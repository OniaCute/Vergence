package cc.vergence.util.font;

import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class RendererFontAdapter implements FontAdapter {

    private final FontRenderer fontRenderer;
    private final float size;

    public RendererFontAdapter(Font font, float size) {
        this.fontRenderer = new FontRenderer(new Font[]{font}, size);
        this.size = size;
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public float getSize() {
        return size;
    }

    private void ensureAlpha(int color, float[] rgbaOut) {
        if ((color & 0xFC000000) == 0) color |= 0xFF000000;
        rgbaOut[3] = ((color >> 24) & 0xFF) / 255.0f;
        rgbaOut[0] = ((color >> 16) & 0xFF) / 255.0f;
        rgbaOut[1] = ((color >> 8) & 0xFF) / 255.0f;
        rgbaOut[2] = (color & 0xFF) / 255.0f;
    }

    @Override
    public void drawString(MatrixStack matrices, String text, float x, float y, int color) {
        float[] rgba = new float[4];
        ensureAlpha(color, rgba);
        drawString(matrices, text, x, y, rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    @Override
    public void drawString(MatrixStack matrices, String text, double x, double y, int color) {
        drawString(matrices, text, (float) x, (float) y, color);
    }

    @Override
    public void drawString(MatrixStack matrices, String text, float x, float y, float r, float g, float b, float a) {
        fontRenderer.drawString(matrices, text, x, y - 3, r, g, b, a, false);
    }

    @Override
    public void drawString(MatrixStack matrices, String text, float x, float y, int color, boolean dropShadow) {
        float[] rgba = new float[4];
        ensureAlpha(color, rgba);
        drawString(matrices, text, x, y, rgba[0], rgba[1], rgba[2], rgba[3], dropShadow);
    }

    @Override
    public void drawString(MatrixStack matrices, String text, double x, double y, int color, boolean dropShadow) {
        drawString(matrices, text, (float) x, (float) y, color, dropShadow);
    }

    @Override
    public void drawString(MatrixStack matrices, String text, float x, float y, float r, float g, float b, float a, boolean dropShadow) {
        if (dropShadow) {
            fontRenderer.drawStringWithShadow(matrices, text, x, y - 3, r, g, b, a);
        } else {
            fontRenderer.drawString(matrices, text, x, y - 3, r, g, b, a, false);
        }
    }


    @Override
    public void drawGradientString(MatrixStack matrices, String s, float x, float y, int offset, boolean hud) {
        fontRenderer.drawGradientString(matrices, s, x, y - 3);
    }

    @Override
    public void drawCenteredString(MatrixStack matrices, String text, double x, double y, int color) {
        float[] rgba = new float[4];
        ensureAlpha(color, rgba);
        drawCenteredString(matrices, text, x, y, rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    @Override
    public void drawCenteredString(MatrixStack matrices, String text, double x, double y, float r, float g, float b, float a) {
        fontRenderer.drawCenteredString(matrices, text, (float) x, (float) y - 3, r, g, b, a, false);
    }

    @Override
    public float getWidth(String text) {
        return fontRenderer.getStringWidth(text);
    }

    @Override
    public float getFontHeight() {
        return getFontHeight("emotion000");
    }

    @Override
    public float getFontHeight(String text) {
        return fontRenderer.getStringHeight(text);
    }

    @Override
    public float getMarginHeight() {
        return getFontHeight();
    }

    @Override
    public String trimStringToWidth(String in, double width) {
        StringBuilder sb = new StringBuilder();
        for (char c : in.toCharArray()) {
            if (getWidth(sb.toString() + c) >= width) return sb.toString();
            sb.append(c);
        }
        return sb.toString();
    }

    @Override
    public String trimStringToWidth(String in, double width, boolean reverse) {
        return trimStringToWidth(in, width);
    }
}
