package cc.vergence.util.color;

import cc.vergence.modules.client.ClickGUI;
import cc.vergence.modules.client.Client;
import cc.vergence.util.maths.MathUtil;
import net.minecraft.util.Formatting;

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

    public static Color getRainbow() {
        return getRainbow(255);
    }

    public static Color getRainbow(int alpha) {
        return getRainbow(ClickGUI.INSTANCE.rainbowSpeed.getValue().longValue(), 1f, 1f, alpha, 0);
    }

    public static Color getRainbow(long speed, float saturation, float brightness, int alpha, long index) {
        speed = (long) MathUtil.clamp(speed, 1, 20);

        float hue = ((System.currentTimeMillis() + index) % (10500 - (500 * speed))) / (10500.0f - (500.0f * (float) speed));
        Color color = new Color(Color.HSBtoRGB(MathUtil.clamp(hue, 0.0f, 1.0f), MathUtil.clamp(saturation, 0.0f, 1.0f), MathUtil.clamp(brightness, 0.0f, 1.0f)));

        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static Color applyOpacity(Color color, float opacity) {
        opacity = Math.min(1, Math.max(0, opacity));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (color.getAlpha() * opacity));
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

    public static Formatting getHealthColor(double health) {
        if (health > 18.0) return Formatting.GREEN;
        else if (health > 16.0) return Formatting.DARK_GREEN;
        else if (health > 12.0) return Formatting.YELLOW;
        else if (health > 8.0) return Formatting.GOLD;
        else if (health > 5.0) return Formatting.RED;

        return Formatting.DARK_RED;
    }

    public static Formatting getPingColor(double lag) {
        if (lag < 25.0) return Formatting.GREEN;
        else if (lag < 40.0) return Formatting.DARK_GREEN;
        else if (lag < 60.0) return Formatting.YELLOW;
        else if (lag < 70.0) return Formatting.GOLD;
        else if (lag < 100.0) return Formatting.RED;
        return Formatting.DARK_RED;
    }
}
