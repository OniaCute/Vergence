package cc.vergence.modules.hud;

import cc.vergence.modules.Module;

public class KeyStrokes extends Module {
    public static KeyStrokes INSTANCE;

    public KeyStrokes() {
        super("KeyStrokes", Category.HUD);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
