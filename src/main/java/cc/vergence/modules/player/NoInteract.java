package cc.vergence.modules.player;

import cc.vergence.modules.Module;

public class NoInteract extends Module {
    public static NoInteract INSTANCE;

    public NoInteract() {
        super("NoInteract", Category.PLAYER);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
