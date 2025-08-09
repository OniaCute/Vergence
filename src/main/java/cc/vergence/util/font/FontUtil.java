package cc.vergence.util.font;

import cc.vergence.features.enums.other.Aligns;
import cc.vergence.features.enums.font.FontSize;
import cc.vergence.features.enums.font.Fonts;
import cc.vergence.modules.client.Client;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.render.utils.Render2DUtil;
import io.github.humbleui.types.Rect;
import oshi.util.tuples.Pair;

import java.awt.*;

public class FontUtil implements Wrapper {
    public static boolean LOADED;

    public static float asFontSizeValue(FontSize size) { // for "/ 4f", It is because I performed the font scaling under the 4x UI reduction, so I used /4f for fix.
        float scale = (float) getScale();
        switch (size) {
            case LARGEST -> {
                return 16 / 4f * scale;
            }
            case LARGE -> {
                return 12 / 4f * scale;
            }
            case SMALL -> {
                return 8 / 4f * scale;
            }
            case SMALLEST -> {
                return 6 / 4f * scale;
            }
            default -> {
                return 10 / 4f * scale;
            }
        }
    }

    public static io.github.humbleui.skija.Font getClientFont(float size) {
        if (Client.INSTANCE != null) {
            switch ((Fonts) Client.INSTANCE.font.getValue()) {
                case Sans -> {
                    return FontRenderers.getSans(size);
                }
                case RHR -> {
                    return FontRenderers.getRhr(size);
                }
                case Icon -> {
                    return FontRenderers.getIcon(size);
                }
                default -> {
                    return FontRenderers.getSmooth(size);
                }
            }
        }
        return FontRenderers.getSmooth(0);
    }

    public static double getHeight(FontSize size) {
        return getTextBounds("I", getClientFont(asFontSizeValue(size))).getHeight();
    }

    public static double getWidth(FontSize size, String text) { // old method template support
        return getWidth(text, size);
    }

    public static double getWidth(String text, FontSize size) {
        return getTextBounds(text, getClientFont(asFontSizeValue(size))).getWidth();
    }

    public static double[] getSides(String text, FontSize size) {
        Rect fontRect = getTextBounds(text, getClientFont(asFontSizeValue(size)));
        return new double[] {fontRect.getLeft(), fontRect.getTop(), fontRect.getRight(), fontRect.getBottom()};
    }

    public static Rect getTextBounds(String text, io.github.humbleui.skija.Font font) {
        return font.measureText(text);
    }

    public static void drawText(String text, double x, double y, int color, FontSize size) {
        drawText(text, x, y, new Color(color, true), size, false);
    }

    public static void drawText(String text, double x, double y, Color color, FontSize size) {
        drawText(text, x, y, color, size, false);
    }

    public static void drawText(String text, double x, double y, int color, FontSize size, boolean shadow) {
        drawText(text, x, y, new Color(color, true), size, shadow);
    }

    public static void drawText(String text, double x, double y, Color color, FontSize size, boolean shadow) {
        if (Client.INSTANCE == null || !LOADED) {
            return;
        }
        io.github.humbleui.skija.Font font = getClientFont(asFontSizeValue(size));
        Rect bounds = getTextBounds(text, font);
        double baselineOffset = -bounds.getTop();

        double baseX = x;
        double baseY = y + baselineOffset;

        float scale = (float) getScale();

        float drawX = (float) (baseX * scale);
        float drawY = (float) (baseY * scale);

        if (shadow) {
            float shadowOffset = 2f * scale;
            Render2DUtil.getCanvas().drawString(text, drawX + shadowOffset, drawY + shadowOffset, font, Render2DUtil.getPaint(new Color(7, 7, 7)));
        }
        Render2DUtil.getCanvas().drawString(text, drawX, drawY, font, Render2DUtil.getPaint(color));
    }

    public static void drawIcon(String text, double x, double y, int color, FontSize size) {
        drawIcon(text, x, y, new Color(color, true), size);
    }

    public static void drawIcon(String text, double x, double y, Color color, FontSize size) {
        if (Client.INSTANCE == null || !LOADED) {
            return;
        }
        float scale = (float) getScale();
        io.github.humbleui.skija.Font font = FontRenderers.getIcon(asFontSizeValue(size));
        Rect bounds = getTextBounds(text, font);
        float baselineOffset = (float)(-bounds.getTop());

        float drawX = (float)(x * scale);
        float drawY = (float)(y * scale + baselineOffset);

        Render2DUtil.getCanvas().drawString(text, drawX, drawY, font, Render2DUtil.getPaint(color));
    }

    public static void drawTextWithAlign(String text, double x, double y, double ex, double ey, int color, FontSize size, Aligns align) {
        drawTextWithAlign(text, x, y, ex, ey, new Color(color, true), size, align, false);
    }

    public static void drawTextWithAlign(String text, double x, double y, double ex, double ey, Color color, FontSize size, Aligns align) {
        drawTextWithAlign(text, x, y, ex, ey, color, size, align, false);
    }

    public static void drawTextWithAlign(String text, double x, double y, double ex, double ey, int color, FontSize size, Aligns align, boolean shadow) {
        drawTextWithAlign(text, x, y, ex, ey, new Color(color, true), size, align, shadow);
    }

    public static void drawTextWithAlign(String text, double x, double y, double ex, double ey, Color color, FontSize size, Aligns align, boolean shadow) {
        io.github.humbleui.skija.Font font = getClientFont(asFontSizeValue(size));
        Rect b = getTextBounds(text, font);
        double w = b.getWidth();
        double h = b.getHeight();
        Pair<Double, Double> pos = Render2DUtil.getAlignPositionAsPair(x, y, ex, ey, w, h, align);
        drawText(text, pos.getA(), pos.getB(), color, size);
    }


    public static void drawIconWithAlign(String text, double x, double y, double ex, double ey, Color color, FontSize size, Aligns align) {
        io.github.humbleui.skija.Font font = FontRenderers.getIcon(asFontSizeValue(size));
        Rect b = getTextBounds(text, font);
        double w = b.getWidth();
        double h = b.getHeight();
        Pair<Double, Double> pos = Render2DUtil.getAlignPositionAsPair(x, y, ex, ey, w, h, align);
        drawIcon(text, pos.getA(), pos.getB(), color, size);
    }

    public static double getScale() {
        return 1;
    }
}
