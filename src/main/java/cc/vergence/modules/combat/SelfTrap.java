package cc.vergence.modules.combat;

import cc.vergence.modules.Module;

public class SelfTrap extends Module {
    public static SelfTrap INSTANCE;

    public SelfTrap() {
        super("SelfTrap", Category.COMBAT);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
