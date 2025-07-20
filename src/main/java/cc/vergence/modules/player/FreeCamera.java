package cc.vergence.modules.player;

import cc.vergence.modules.Module;

public class FreeCamera extends Module {
    public static FreeCamera INSTANCE;

    public FreeCamera() {
        super("FreeCamera", Category.PLAYER);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
