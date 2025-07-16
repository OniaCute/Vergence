package cc.vergence.features.options.impl;

import cc.vergence.Vergence;
import cc.vergence.features.enums.Dimensions;
import cc.vergence.features.options.Option;
import cc.vergence.modules.client.Placeholder;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.WorldUtil;

import java.util.Objects;
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
    }

    @Override
    public String getValue() {
        String formatText = this.getRawValue().toLowerCase();
        String sourceText = this.getRawValue();
        while (formatText.contains("{id}")) {
            sourceText = sourceText.replace("{id}", Vergence.MOD_ID);
            formatText = formatText.replace("{id}", Vergence.MOD_ID);
        }
        while (formatText.contains("{name}")) {
            sourceText = sourceText.replace("{name}", Vergence.NAME);
            formatText = formatText.replace("{name}", Vergence.NAME);
        }
        while (formatText.contains("{full_name}")) {
            sourceText = sourceText.replace("{full_name}", Vergence.NAME + " Client");
            formatText = formatText.replace("{full_name}", Vergence.NAME + " Client");
        }
        while (formatText.contains("{version}")) {
            sourceText = sourceText.replace("{version}", Vergence.VERSION);
            formatText = formatText.replace("{version}", Vergence.VERSION);
        }
        if (mc.player != null) {
            while (formatText.contains("{player}")) {
                sourceText = sourceText.replace("{player}", mc.player.getName().getString());
                formatText = formatText.replace("{player}", mc.player.getName().getString());
            }
            while (formatText.contains("{hp}")) {
                sourceText = sourceText.replace("{hp}", String.valueOf((int) mc.player.getHealth()));
                formatText = formatText.replace("{hp}", String.valueOf((int) mc.player.getHealth()));
            }
            while (formatText.contains("{max_hp}")) {
                sourceText = sourceText.replace("{max_hp}", String.valueOf((int) mc.player.getMaxHealth()));
                formatText = formatText.replace("{max_hp}", String.valueOf((int) mc.player.getMaxHealth()));
            }
            while (formatText.contains("{armor}")) {
                sourceText = sourceText.replace("{armor}", String.valueOf((int) mc.player.getArmor()));
                formatText = formatText.replace("{armor}", String.valueOf((int) mc.player.getArmor()));
            }
        }
        if (mc.world != null) {
            while (formatText.contains("{world}")) {
                if (Placeholder.INSTANCE != null) {
                    Dimensions dimension = WorldUtil.getDimension();
                    switch (dimension) {
                        case Overworld -> {
                            sourceText = sourceText.replace("{world}", ((TextOption) Placeholder.INSTANCE.placeholder_world_overworld).getRawValue());
                            formatText = formatText.replace("{world}", ((TextOption) Placeholder.INSTANCE.placeholder_world_overworld).getRawValue());
                        }
                        case Nether -> {
                            sourceText = sourceText.replace("{world}", ((TextOption) Placeholder.INSTANCE.placeholder_world_nether).getRawValue());
                            formatText = formatText.replace("{world}", ((TextOption) Placeholder.INSTANCE.placeholder_world_nether).getRawValue());
                        }
                        case TheEnd -> {
                            sourceText = sourceText.replace("{world}", ((TextOption) Placeholder.INSTANCE.placeholder_world_the_end).getRawValue());
                            formatText = formatText.replace("{world}", ((TextOption) Placeholder.INSTANCE.placeholder_world_the_end).getRawValue());
                        }
                    }
                } else {
                    sourceText = sourceText.replace("{world}", WorldUtil.getDimension().name());
                    formatText = formatText.replace("{world}", WorldUtil.getDimension().name());
                }
            }
        }
        return sourceText;
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

    public boolean isEditable() {
        return editable;
    }
}
