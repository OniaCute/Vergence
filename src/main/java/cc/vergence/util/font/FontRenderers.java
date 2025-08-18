package cc.vergence.util.font;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FontRenderers {
    public static FontAdapter SMOOTH_3F;
    public static FontAdapter SMOOTH_4F;
    public static FontAdapter SMOOTH_5F;
    public static FontAdapter SMOOTH_6F;
    public static FontAdapter SMOOTH_7F;
    public static FontAdapter SMOOTH_8F;
    public static FontAdapter SMOOTH_9F;
    public static FontAdapter SMOOTH_10F;
    public static FontAdapter SMOOTH_12F;
    public static FontAdapter SMOOTH_14F;
    public static FontAdapter SMOOTH_15F;
    public static FontAdapter SMOOTH_16F;
    public static FontAdapter SMOOTH_18F;
    public static FontAdapter SMOOTH_20F;
    public static FontAdapter SMOOTH_21F;
    public static FontAdapter SMOOTH_24F;
    public static FontAdapter SMOOTH_28F;
    public static FontAdapter SMOOTH_32F;

    public static FontAdapter SANS_3F;
    public static FontAdapter SANS_4F;
    public static FontAdapter SANS_5F;
    public static FontAdapter SANS_6F;
    public static FontAdapter SANS_7F;
    public static FontAdapter SANS_8F;
    public static FontAdapter SANS_9F;
    public static FontAdapter SANS_10F;
    public static FontAdapter SANS_12F;
    public static FontAdapter SANS_14F;
    public static FontAdapter SANS_15F;
    public static FontAdapter SANS_16F;
    public static FontAdapter SANS_18F;
    public static FontAdapter SANS_20F;
    public static FontAdapter SANS_21F;
    public static FontAdapter SANS_24F;
    public static FontAdapter SANS_28F;
    public static FontAdapter SANS_32F;

    public static FontAdapter RHR_3F;
    public static FontAdapter RHR_4F;
    public static FontAdapter RHR_5F;
    public static FontAdapter RHR_6F;
    public static FontAdapter RHR_7F;
    public static FontAdapter RHR_8F;
    public static FontAdapter RHR_9F;
    public static FontAdapter RHR_10F;
    public static FontAdapter RHR_12F;
    public static FontAdapter RHR_14F;
    public static FontAdapter RHR_15F;
    public static FontAdapter RHR_16F;
    public static FontAdapter RHR_18F;
    public static FontAdapter RHR_20F;
    public static FontAdapter RHR_21F;
    public static FontAdapter RHR_24F;
    public static FontAdapter RHR_28F;
    public static FontAdapter RHR_32F;

    public static FontAdapter ICON_3F;
    public static FontAdapter ICON_4F;
    public static FontAdapter ICON_5F;
    public static FontAdapter ICON_6F;
    public static FontAdapter ICON_7F;
    public static FontAdapter ICON_8F;
    public static FontAdapter ICON_9F;
    public static FontAdapter ICON_10F;
    public static FontAdapter ICON_12F;
    public static FontAdapter ICON_14F;
    public static FontAdapter ICON_15F;
    public static FontAdapter ICON_16F;
    public static FontAdapter ICON_18F;
    public static FontAdapter ICON_20F;
    public static FontAdapter ICON_21F;
    public static FontAdapter ICON_24F;
    public static FontAdapter ICON_28F;
    public static FontAdapter ICON_32F;

    public static RendererFontAdapter create(String name, int style, float size) {
        return new RendererFontAdapter(new Font(name, style, (int) size), size);
    }

    public static RendererFontAdapter SmoothFont(float size) throws IOException, FontFormatException {
        InputStream fontStream = FontRenderers.class.getClassLoader().getResourceAsStream("assets/vergence/font/smooth.otf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, size);
        return new RendererFontAdapter(font, size);
    }

    public static RendererFontAdapter SansFont(float size) throws IOException, FontFormatException {
        InputStream fontStream = FontRenderers.class.getClassLoader().getResourceAsStream("assets/vergence/font/sans.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, size);
        return new RendererFontAdapter(font, size);
    }

    public static RendererFontAdapter RhrFont(float size) throws IOException, FontFormatException {
        InputStream fontStream = FontRenderers.class.getClassLoader().getResourceAsStream("assets/vergence/font/rhr.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, size);
        return new RendererFontAdapter(font, size);
    }

    public static RendererFontAdapter IconFont(float size) throws IOException, FontFormatException {
        InputStream fontStream = FontRenderers.class.getClassLoader().getResourceAsStream("assets/vergence/font/icons.otf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, size);
        return new RendererFontAdapter(font, size);
    }

    public static final String SMOOTH = "assets/vergence/font/smooth.otf";
    public static final String SANS = "assets/vergence/font/sans.ttf";
    public static final String RHR = "assets/vergence/font/rhr.ttf";
    public static final String ICON = "assets/vergence/font/icons.otf";

    public static io.github.humbleui.skija.Font getSans(float size) {
        return FontHelper.load(SANS, size);
    }

    public static io.github.humbleui.skija.Font getRhr(float size) {
        return FontHelper.load(RHR, size);
    }

    public static io.github.humbleui.skija.Font getSmooth(float size) {
        return FontHelper.load(SMOOTH, size);
    }

    public static io.github.humbleui.skija.Font getIcon(float size) {
        return FontHelper.load(ICON, size);
    }
}