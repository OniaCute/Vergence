package cc.vergence.util.font;

import io.github.humbleui.skija.Font;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FontRenderers {
    public static final String SMOOTH = "assets/vergence/font/smooth.otf";
    public static final String SANS = "assets/vergence/font/sans.ttf";
    public static final String RHR = "assets/vergence/font/rhr.ttf";
    public static final String ICON = "assets/vergence/font/icons.otf";

    public static Font getSans(float size) {
        return FontHelper.load(SANS, size);
    }

    public static Font getRhr(float size) {
        return FontHelper.load(RHR, size);
    }

    public static Font getSmooth(float size) {
        return FontHelper.load(SMOOTH, size);
    }

    public static Font getIcon(float size) {
        return FontHelper.load(ICON, size);
    }
}