package cc.vergence.modules.combat;

import cc.vergence.modules.Module;

public class Trap extends Module {
    public static Trap INSTANCE;

    public Trap() {
        super("Trap", Category.COMBAT);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
