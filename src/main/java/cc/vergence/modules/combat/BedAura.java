package cc.vergence.modules.combat;

import cc.vergence.modules.Module;

public class BedAura extends Module{
    public static BedAura INSTANCE;

    public BedAura() {
        super("BedAura", Category.COMBAT);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
