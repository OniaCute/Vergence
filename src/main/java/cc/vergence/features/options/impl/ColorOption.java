package cc.vergence.features.options.impl;

import cc.vergence.features.options.Option;
import cc.vergence.modules.client.ClickGUI;
import cc.vergence.modules.client.Client;
import cc.vergence.util.color.ColorUtil;
import cc.vergence.util.other.RandomUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.awt.*;
import java.util.function.Predicate;

public class ColorOption extends Option<Color> {
    private boolean isRainbow;

    public ColorOption(String name) {
        super(name, new Color(0), v -> true);
    }

    public ColorOption(String name, int defaultValue) {
        super(name, new Color(defaultValue), v -> true);
    }

    public ColorOption(String name, Color defaultValue) {
        super(name, defaultValue, v -> true);
    }

    public ColorOption(String name, Color defaultValue, Predicate<?> invisibility) {
        super(name, defaultValue, invisibility);
    }

    @Override
    public Color getValue() {
        if (isRainbow()) {
            float[] hsb = Color.RGBtoHSB(value.getRed(), value.getGreen(), value.getBlue(), null);

            long base = System.currentTimeMillis();
            if (ClickGUI.INSTANCE != null && !ClickGUI.INSTANCE.rainbowSync.getValue()) {
                base += (long) (ClickGUI.INSTANCE.rainbowSpeed.getValue() % ClickGUI.INSTANCE.rainbowOffset.getValue().longValue());
            }

            double rainbowState = (base / (ClickGUI.INSTANCE != null ? ClickGUI.INSTANCE.rainbowSpeed.getValue() : 300)) % 360;

            int rgb = Color.getHSBColor((float) (rainbowState / 360.0f), hsb[1], hsb[2]).getRGB();
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = (rgb) & 0xFF;
            return new Color(red, green, blue, value.getAlpha());
        }
        return this.value;
    }


    @Override
    public void setValue(Color value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = new Color(value);
    }

    public void setRainbow(boolean rainbow) {
        isRainbow = rainbow;
    }

    public boolean isRainbow() {
        return isRainbow;
    }

    public JsonElement getJsonValue() {
        return new JsonPrimitive(ColorUtil.asRGBA(getValue().getRed(), getValue().getGreen(), getValue().getBlue(), getValue().getAlpha()));
    }
}
