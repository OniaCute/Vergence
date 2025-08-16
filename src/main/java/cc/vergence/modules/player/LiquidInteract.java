package cc.vergence.modules.player;

import cc.vergence.modules.Module;

public class LiquidInteract extends Module {
    public static LiquidInteract INSTANCE;

    public LiquidInteract() {
        super("LiquidInteract", Category.PLAYER);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
