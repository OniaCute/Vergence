package cc.vergence.modules.hud;

import cc.vergence.modules.Module;

public class InventoryHud extends Module {
    public static InventoryHud INSTANCE;

    public InventoryHud() {
        super("InventoryHud",Category.HUD);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
