package cc.vergence.util.font;

import cc.vergence.util.interfaces.Wrapper;
import io.github.humbleui.skija.Data;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.Typeface;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FontHelper implements Wrapper {
    private static final Map<String, Typeface> typefaceCache = new HashMap<>();

    private static Typeface getTypeface(String font, String type) {
        return typefaceCache.computeIfAbsent(font, k -> loadTypeface(k, type));
    }

    private static Typeface loadTypeface(String font, String type) {
        Optional<Data> fontDataOptional = convertToData(font);
        return fontDataOptional.map(Typeface::makeFromData).orElseThrow(() -> new IllegalArgumentException("Font not found: " + font));
    }

    public static Font load(String font, float size, String fontType) {
        Typeface typeface = getTypeface(font, fontType);
        return new Font(typeface, (float) (size * (mc.getWindow() != null ? mc.getWindow().getScaleFactor() : 1)));
    }

    public static Font load(String font, float size) {
        return load(font, size, getFontType(font));
    }

    private static String getFontType(String font) {
        return font.substring(font.lastIndexOf('.') + 1).toLowerCase();
    }

    public static void clearCache() {
        typefaceCache.clear();
    }

    public static void preloadFonts(String... fonts) {
        for (String font : fonts) {
            getTypeface(font, getFontType(font));
        }
    }

    public static Optional<byte[]> convertToBytes(String path) {
        try (InputStream inputStream = getResourceAsStream(path)) {
            return Optional.of(inputStream.readAllBytes());
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public static Optional<Data> convertToData(String path) {
        return convertToBytes(path).map(Data::makeFromBytes);
    }

    public static InputStream getResourceAsStream(String path) {
        InputStream inputStream = FontHelper.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }
        return inputStream;
    }
}
