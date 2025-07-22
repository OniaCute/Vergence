package cc.vergence.modules.movement;

import cc.vergence.modules.Module;

public class AntiLevitation extends Module {
    public static AntiLevitation INSTANCE;

    public AntiLevitation() {
        super("AntiLevitation", Category.MOVEMENT);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
