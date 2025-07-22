package cc.vergence.features.options.impl;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.OptionValueUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.util.other.EnumUtil;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.function.Predicate;

public class MultipleOption<E extends Enum<E>> extends Option<EnumSet<E>> {
    public Class<E> enumClass;

    public  MultipleOption(String name, EnumSet<E> value) {
        super(name, value, v -> true);
        if (!value.isEmpty()) {
            E first = value.iterator().next();
            this.enumClass = (Class<E>) first.getDeclaringClass();
        }
    }

    public  MultipleOption(String name, EnumSet<E> value, Predicate<?> invisibility) {
        super(name, value, invisibility);
        if (!value.isEmpty()) {
            E first = value.iterator().next();
            this.enumClass = (Class<E>) first.getDeclaringClass();
        }
    }

    @Override
    public EnumSet<E> getValue() {
        return this.value;
    }

    @Override
    public void setValue(EnumSet<E> value) {
        this.value = value;
        if (enumClass == null && !value.isEmpty()) {
            E first = value.iterator().next();
            this.enumClass = (Class<E>) first.getDeclaringClass();
        }
        Vergence.EVENTBUS.post(new OptionValueUpdateEvent());
    }

    public Class<E> getEnumClass() {
        return enumClass;
    }
}
