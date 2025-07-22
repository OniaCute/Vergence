package cc.vergence.modules.player;

import cc.vergence.modules.Module;

public class AutoRespawn extends Module {
    public static AutoRespawn INSTANCE;

    public AutoRespawn() {
        super("AutoRespawn", Category.PLAYER);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
