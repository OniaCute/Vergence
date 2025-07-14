package cc.vergence.modules.visual;

import cc.vergence.features.enums.AntiCheats;
import cc.vergence.modules.Module;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;

public class ESP extends Module {
    public ESP() {
        super("ESP", Category.VISUAL);
    }

    ArrayList<PlayerEntity> players = new ArrayList<>();

    @Override
    public String getDetails() {
        return String.valueOf(players.size());
    }
}
