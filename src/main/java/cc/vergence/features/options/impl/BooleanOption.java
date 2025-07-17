package cc.vergence.features.options.impl;

import cc.vergence.features.options.Option;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.function.Predicate;

public class BooleanOption extends Option<Boolean> {
    public BooleanOption(String name) {
        super(name, false, v -> true);
    }
    public BooleanOption(String name, boolean defaultValue) {
        super(name, defaultValue, v -> true);
    }
    public BooleanOption(String name, boolean defaultValue, Predicate<?> invisibility) {
        super(name, defaultValue, invisibility);
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return this.value;
    }

    public JsonElement getJsonValue() {
        return new JsonPrimitive(getValue());
    }
}
