package cc.vergence.modules.player;

import cc.vergence.modules.Module;

public class SaveBreak extends Module {
    public static SaveBreak INSTANCE;

    public SaveBreak() {
        super("SaveBreak", Category.PLAYER);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
