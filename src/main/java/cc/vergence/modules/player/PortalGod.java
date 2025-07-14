package cc.vergence.modules.player;

import cc.vergence.modules.Module;

public class PortalGod extends Module {
    public static PortalGod INSTANCE;
    public PortalGod() {
        super("PortalGod", Category.PLAYER);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
