package cc.vergence.modules.movement;

import cc.vergence.modules.Module;

public class NoSlowdown extends Module {
    public static NoSlowdown INSTANCE;

    public NoSlowdown() {
        super("NoSlowdown", Category.MOVEMENT);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
