package cc.vergence.modules.combat;

import cc.vergence.modules.Module;

public class AnchorAura extends Module{
    public static AnchorAura INSTANCE;

    public AnchorAura() {
        super("AnchorAura", Module.Category.COMBAT);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
