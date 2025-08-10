package cc.vergence.features.options.impl;

import cc.vergence.Vergence;
import cc.vergence.features.enums.world.Dimensions;
import cc.vergence.features.event.events.OptionValueUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.modules.client.Placeholder;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.WorldUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.function.Predicate;

public class TextOption extends Option<String> implements Wrapper {
    public int sizeLimit = -1; // default no limitation
    private boolean editable = true;
    public TextOption(String name, String defaultValue) {
        super(name, "", defaultValue, v -> true);
    }
    public TextOption(String name, String defaultValue, int sizeLimit) {
        super(name, "", defaultValue, v -> true);
        this.sizeLimit = sizeLimit;

    }
    public TextOption(String name, String defaultValue, Predicate<?> invisibility) {
        super(name, "", defaultValue, invisibility);
    }
    public TextOption(String name, String description, String defaultValue) {
        super(name, description, defaultValue, v -> true);
    }
    public TextOption(String name, String description, String defaultValue, Predicate<?> invisibility) {
        super(name, description, defaultValue, invisibility);
    }

    @Override
    public void setValue(String value) {
        this.value = value;
        Vergence.EVENTBUS.post(new OptionValueUpdateEvent());
    }

    @Override
    public String getValue() {
        String raw = this.getRawValue();
        if (raw == null) {
            return "";
        }
        if (Placeholder.INSTANCE == null || !Placeholder.INSTANCE.getStatus()) {
            return raw.replaceAll("&", "§");
        }
        raw = raw
                .replaceAll("\\{id}", Vergence.MOD_ID)
                .replaceAll("\\{name}", Vergence.NAME)
                .replaceAll("\\{full_name}", Vergence.NAME + " Client")
                .replaceAll("\\{version}", Vergence.VERSION);
        if (mc.player != null) {
            raw = raw
                    .replaceAll("\\{player}", mc.player.getName().getString())
                    .replaceAll("\\{hp}", String.valueOf((int) mc.player.getHealth()))
                    .replaceAll("\\{max_hp}", String.valueOf((int) mc.player.getMaxHealth()))
                    .replaceAll("\\{armor}", String.valueOf((int) mc.player.getArmor()))
                    .replaceAll("\\{speed}", String.format("%.2f", Vergence.INFO.getSpeedPerS()))
                    .replaceAll("\\{speed_km}", String.format("%.2f", Vergence.INFO.getSpeed()))
                    .replaceAll("\\{fps}", String.valueOf(Vergence.INFO.getCurrentFPS()))
                    .replaceAll("\\{memory}", String.valueOf(Vergence.INFO.getSpentMemory()))
                    .replaceAll("\\{memory_max}", String.valueOf(Vergence.INFO.getMaxMemory()))
                    .replaceAll("\\{ping}", String.valueOf(Vergence.INFO.getPing()))
                    .replaceAll("\\{combo}", String.valueOf(Vergence.INFO.getCombo()))
                    .replaceAll("\\{x}", String.format("%.2f", mc.player.getPos().x))
                    .replaceAll("\\{y}", String.format("%.2f", mc.player.getPos().y))
                    .replaceAll("\\{z}", String.format("%.2f", mc.player.getPos().z))
                    .replaceAll("\\{cps}", String.valueOf(Vergence.INFO.getLeftClicks()))
                    .replaceAll("\\{right_cps}", String.valueOf(Vergence.INFO.getRightClicks()));

        }
        if (mc.world != null) {
            Dimensions dim = WorldUtil.getDimension();
            String world;
            if (Placeholder.INSTANCE != null) {
                switch (dim) {
                    case Overworld -> world = ((TextOption) Placeholder.INSTANCE.placeholder_world_overworld).getRawValue();
                    case Nether -> world = ((TextOption) Placeholder.INSTANCE.placeholder_world_nether).getRawValue();
                    case TheEnd -> world = ((TextOption) Placeholder.INSTANCE.placeholder_world_the_end).getRawValue();
                    default -> world = dim.name();
                }
            } else {
                world = dim.name();
            }
            raw = raw.replaceAll("\\{world}", world);
        }
        return raw.replaceAll("&", "§");
    }

    public String getRawValue() {
        return this.value;
    }

    public char getFirstChar() {
        return getValue().charAt(0);
    }

    public char getLastChar() {
        return getValue().charAt(getValue().length() - 1);
    }

    public TextOption setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    public JsonElement getJsonValue() {
        return new JsonPrimitive(getRawValue());
    }

    public boolean isEditable() {
        return editable;
    }
}
