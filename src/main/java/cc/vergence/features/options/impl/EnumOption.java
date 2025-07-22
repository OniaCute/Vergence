package cc.vergence.features.options.impl;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.OptionValueUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.util.other.EnumUtil;

import java.util.List;
import java.util.function.Predicate;

public class EnumOption extends Option<Enum<?>> {

    public EnumOption(String name, Enum<?> value) {
        super(name, value, v -> true);
    }

    public EnumOption(String name, Enum<?> value, Predicate<?> invisibility) {
        super(name, value, invisibility);
    }

    @Override
    public Enum<?> getValue() {
        return this.value;
    }

    @Override
    public void setValue(Enum<?> value) {
        this.value = value;
        Vergence.EVENTBUS.post(new OptionValueUpdateEvent());
    }

    public <T extends Enum<T>> T getNext(T value) {
        return EnumUtil.getNextEnumValue(EnumUtil.getEnumClassFromValue(value), value);
    }

    public <T extends Enum<T>> T getPrevious(T value) {
        return EnumUtil.getPreviousEnumValue(EnumUtil.getEnumClassFromValue(value), value);
    }

    public <T extends Enum<T>> List<T> getList(T value) {
        return EnumUtil.getAllEnumValues(value.getDeclaringClass());
    }
}
