package cc.vergence.features.options.impl;

import cc.vergence.features.options.Option;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.FastTimerUtil;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class BindOption extends Option<Integer> implements Wrapper {
    private BindType bindType;
    private boolean needShift;
    private boolean status;

    public BindOption(String name, Integer key, BindType bindType) {
        super(name, key, v -> true);
        this.bindType = bindType;
    }

    public BindOption(String name, Integer key, BindType bindType, Predicate<?> invisibility) {
        super(name, key, invisibility);
        this.bindType = bindType;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
        this.needShift = false;
    }

    public void setValue(char value) {
        this.value = (int) value;
        this.needShift = false;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public char getValueAsChar() {
        return (char) (getValue().intValue());
    }

    public void setBindType(BindType bindType) {
        this.bindType = bindType;
    }

    public BindType getBindType() {
        return bindType;
    }

    public void setNeedShift(boolean needShift) {
        this.needShift = needShift;
    }

    public boolean isNeedShift() {
        return needShift;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public String getBindChar() {
        int code = getValue();
        return switch (code) {
            case -1 -> "No Hotkey";
            case -100 -> "Mouse_1";
            case -101 -> "Mouse_2";
            case -102 -> "Mouse_3";
            case -103 -> "Mouse_4";
            case -104 -> "Mouse_5";
            default -> {
                String name = null;
                try {
                    name = GLFW.glfwGetKeyName(code, GLFW.glfwGetKeyScancode(code));
                } catch (Exception ignored) {
                }
                if (name == null) {
                    try {
                        for (Field clazz : GLFW.class.getDeclaredFields()) {
                            if (clazz.getName().startsWith("GLFW_KEY_") && clazz.getType() == int.class) {
                                int value = clazz.getInt(null);
                                if (value == code) {
                                    name = clazz.getName().substring("GLFW_KEY_".length());
                                    break;
                                }
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }
                yield name != null
                        ? name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase()
                        : "No Hotkey";
            }
        };
    }


    public enum BindType {
        Press,
        Click
    }
}
