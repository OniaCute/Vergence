package cc.vergence.modules.player;

import cc.vergence.modules.Module;

public class MultipleTask extends Module {
    public static MultipleTask INSTANCE;

    public MultipleTask() {
        super("MultipleTask", Category.PLAYER);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
