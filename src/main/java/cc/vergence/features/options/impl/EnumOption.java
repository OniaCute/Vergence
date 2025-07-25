package cc.vergence.features.options.impl;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.OptionValueUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.util.other.EnumUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EnumOption extends Option<Enum<?>> {
    private ArrayList<String> hideItems = new ArrayList<>();

    public EnumOption(String name, Enum<?> value) {
        super(name, value, v -> true);
    }

    public EnumOption(String name, Enum<?> value, Predicate<?> invisibility) {
        super(name, value, invisibility);
    }

    public EnumOption addHideItem(String item) {
        hideItems.add(item);
        return this;
    }

    public boolean isHidden(String item) {
        return !hideItems.isEmpty() && hideItems.contains(item);
    }

    @Override
    public Enum<?> getValue() {
        return this.value;
    }

    @Override
    public void setValue(Enum<?> value) {
        Vergence.EVENTBUS.post(new OptionValueUpdateEvent());
        if (isHidden(value.name())) {
            return ;
        }
        this.value = value;
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
