package cc.vergence.features.options.impl;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.OptionValueUpdateEvent;
import cc.vergence.features.options.Option;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

public class DoubleOption extends Option<Double> {
    private double maxValue;
    private double minValue;
    private String unit = "";
    private HashMap<Integer, String> specialValueMap = new HashMap<>();

    public DoubleOption(String name, String description, double minValue, double maxValue, double value) {
        super(name, description, value, v -> true);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public DoubleOption(String name, String description, double minValue, double maxValue, double value, Predicate<?> invisibility) {
        super(name, description, value, invisibility);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public DoubleOption(String name, String description, double minValue, double maxValue) {
        super(name, description, 0.00, v -> true);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public DoubleOption(String name, double minValue, double maxValue, double value) {
        super(name, value, v -> true);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public DoubleOption(String name, double minValue, double maxValue, double value, Predicate<?> invisibility) {
        super(name, value, invisibility);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public DoubleOption(String name, double minValue, double maxValue) {
        super(name, 0.00, v -> true);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public DoubleOption(String name, Double defaultValue) {
        super(name, defaultValue, v -> true);
    }

    @Override
    public Double getValue() {
        return this.value;
    }

    public Double getValueSquared() {
        return getValue() * getValue();
    }

    @Override
    public void setValue(Double value) {
        if (value > maxValue) {
            this.value = maxValue;
        }
        else if (value < minValue) {
            this.value = minValue;
        } else {
            this.value = value;
        }
        Vergence.EVENTBUS.post(new OptionValueUpdateEvent());
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public DoubleOption setUnit(String unit) {
        Vergence.EVENTBUS.post(new OptionValueUpdateEvent());
        this.unit = unit;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public String getValueAsText() {
        if (specialValueMap.get(this.getValue().intValue()) == null) {
            return String.valueOf(getValue()) + getUnit();
        }
        return specialValueMap.get(this.getValue().intValue());
    }

    public DoubleOption addSpecialValue(Integer number, String displayString) {
        specialValueMap.put(number, displayString);
        return this;
    }

    public JsonElement getJsonValue() {
        return new JsonPrimitive(getValue());
    }
}
