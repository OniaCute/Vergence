package cc.vergence.util.other;

import cc.vergence.Vergence;

import java.util.*;
import java.util.stream.Collectors;

public class EnumUtil {
    public static <T extends Enum<T>> T getNextEnumValue(Class<T> enumClass, T currentValue) {
        T[] values = enumClass.getEnumConstants();
        int currentIndex = currentValue.ordinal();
        if (currentIndex == values.length - 1) {
            return values[0];
        } else {
            return values[currentIndex + 1];
        }
    }

    public static <T extends Enum<T>> T getPreviousEnumValue(Class<T> enumClass, T currentValue) {
        T[] values = enumClass.getEnumConstants();
        int currentIndex = currentValue.ordinal();
        if (currentIndex == 0) {
            return values[values.length - 1];
        } else {
            return values[currentIndex - 1];
        }
    }

    public static <T extends Enum<T>> List<T> getAllEnumValues(Class<T> enumClass) {
        return Arrays.asList(enumClass.getEnumConstants());
    }

    public static <T extends Enum<T>> T getEnumValueByName(Class<T> enumClass, String string) {
        T[] values = enumClass.getEnumConstants();
        for (T value : values) {
            if (value.name().equals(string)) {
                return value;
            }
        }
        return null;
    }

    public static <T extends Enum<T>> Class<T> getEnumClassFromValue(T enumValue) {
        return enumValue.getDeclaringClass();
    }


    public static <T extends Enum<T>> T getEnumByName(Enum<?> sampleEnum, String name) {
        if (sampleEnum == null || name == null) return null;
        Class<T> enumClass = (Class<T>) sampleEnum.getDeclaringClass();
        for (T constant : enumClass.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(name)) {
                return constant;
            }
        }
        return null;
    }

    public static <T extends Enum<T>> T getEnumByValue(Enum<?> value) {
        if (value == null) return null;
        Class<T> enumClass = (Class<T>) value.getDeclaringClass();
        return enumClass.getEnumConstants()[value.ordinal()];
    }


    public static List<Enum<?>> getAllEnumValuesReflect(Class<?> enumClass) {
        if (enumClass != null && enumClass.isEnum()) {
            return Arrays.asList((Enum<?>[]) enumClass.getEnumConstants());
        }
        return Collections.emptyList();
    }

    public static Enum<?> getNextEnumValueReflect(Enum<?> current) {
        if (current == null) return null;
        Class<? extends Enum<?>> enumClass = current.getDeclaringClass();
        Enum<?>[] values = enumClass.getEnumConstants();
        int index = current.ordinal();
        return (index == values.length - 1) ? values[0] : values[index + 1];
    }
}
