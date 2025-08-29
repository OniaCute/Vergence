package cc.vergence.modules.combat;

import cc.vergence.modules.Module;

public class AutoBlock extends Module {
    public static AutoBlock INSTANCE;

    public AutoBlock() {
        super("AutoBlock", Category.COMBAT);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
