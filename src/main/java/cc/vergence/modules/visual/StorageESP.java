package cc.vergence.modules.visual;

import cc.vergence.modules.Module;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;

public class StorageESP extends Module {
    public static StorageESP INSTANCE;
    private int amount = 0;

    public StorageESP() {
        super("StorageESP", Category.VISUAL);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return amount == 0 ? "NoStorage" : String.valueOf(amount);
    }
}
