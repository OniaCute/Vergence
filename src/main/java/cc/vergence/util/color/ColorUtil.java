package cc.vergence.util.color;

import java.awt.*;

public class ColorUtil {
    public static Color setRed(Color color, int red) {
        return new Color(red, color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static Color setGreen(Color color, int green) {
        return new Color(color.getRed(), green, color.getBlue(), color.getAlpha());
    }

    public static Color setBlue(Color color, int blue) {
        return new Color(color.getRed(), color.getGreen(), blue, color.getAlpha());
    }

    public static Color setAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static Color setAlpha(Color color, float alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static int setAlpha(int color, int alpha) {
        return asRGBA(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), alpha);

    }

    public static int asRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static Color HSLToRGB(float[] hsl) {
        float red, green, blue;

        if (hsl[1] == 0) {
            red = green = blue = 1;
        } else {
            float q = hsl[2] < .5 ? hsl[2] * (1 + hsl[1]) : hsl[2] + hsl[1] - hsl[2] * hsl[1];
            float p = 2 * hsl[2] - q;

            red = HUEToRGB(p, q, hsl[0] + 1 / 3f);
            green = HUEToRGB(p, q, hsl[0]);
            blue = HUEToRGB(p, q, hsl[0] - 1 / 3f);
        }

        red *= 255;
        green *= 255;
        blue *= 255;

        return new Color((int) red, (int) green, (int) blue);
    }

    public static float HUEToRGB(float p, float q, float t) {
        float newT = t;
        if (newT < 0) newT += 1;
        if (newT > 1) newT -= 1;
        if (newT < 1 / 6f) return p + (q - p) * 6 * newT;
        if (newT < .5f) return q;
        if (newT < 2 / 3f) return p + (q - p) * (2 / 3f - newT) * 6;
        return p;
    }

    public HexColor asHexColor(Color color) {
        return new HexColor(color);
    }

    public HexColor asHexColor(int color) {
        return new HexColor(color);
    }

    public int asIntColor(Color color) {
        return color.getRGB();
    }

    public int asIntColor(HexColor color) {
        return color.getValueAsInt();
    }

    public Color asColor(int color) {
        return new Color(color);
    }

    public Color asColor(HexColor color) {
        return color.getValueAsColor();
    }
}
