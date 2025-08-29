package cc.vergence.modules.misc;

import cc.vergence.Vergence;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.player.EntityUtil;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;

public class FastParty extends Module {
    public static FastParty INSTANCE;
    private final ArrayList<String> players = new ArrayList<>();

    public FastParty() {
        super("FastParty", Category.MISC);
        INSTANCE = this;
    }

    public Option<Double> range = addOption(new DoubleOption("Range", 1, 12, 3));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onEnable() {
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player != null && player != mc.player && !Vergence.FRIEND.isFriend(player.getName().getString()) && !Vergence.ENEMY.isEnemy(player.getName().getString()) && EntityUtil.getDistance(player) <= range.getValue()) {
                players.add(player.getName().getString());
                Vergence.FRIEND.addFriend(player.getName().getString());
            }
        }
    }

    @Override
    public void onDisable() {
        for (String s : players) {
            if (Vergence.FRIEND.isFriend(s)) {
                Vergence.FRIEND.removeFriend(s);
            }
        }
    }

    @Override
    public void onShutDown() {
        disable();
    }

    @Override
    public void onLogout() {
        disable();
    }
}
