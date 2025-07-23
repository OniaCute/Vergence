package cc.vergence.modules.misc;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.DeathEvent;
import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.player.EntityUtil;
import cc.vergence.util.player.InventoryUtil;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Objects;

// For mini-games ~
public class MurderCatcher extends Module implements Wrapper {
    public static MurderCatcher INSTANCE;
    private boolean notified;
    private String murderName;
    private ArrayList<String> murders = new ArrayList<>();

    public MurderCatcher() {
        super("MurderCatcher", Category.MISC);
        INSTANCE = this;
    }

    public Option<Boolean> markEnemy = addOption(new BooleanOption("MarkEnemy", true));


    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDeathEvent(DeathEvent event, PlayerEntity deadPlayer) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        PlayerEntity nearestPlayer = null;
        double minDistance = Double.MAX_VALUE;

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == deadPlayer) {
                continue;
            }

            double dist = player.getPos().distanceTo(deadPlayer.getPos());
            if (dist < minDistance) {
                minDistance = dist;
                nearestPlayer = player;
            }
        }

        if (nearestPlayer != null) {
            String text = Vergence.TEXT.get("Module.Modules.MurderCatcher.Messages.PlayerDiedSuppose")
                    .replace("{player}", deadPlayer.getName().getString()
                    .replace("{murder}", nearestPlayer.getName().getString()
                    .replace("{distance}", String.format("%.2f", minDistance))));
            NotifyManager.newNotification(this, text);
            MessageManager.newMessage(this, text);

            if (markEnemy.getValue()) {
                Vergence.ENEMY.addEnemy(nearestPlayer.getName().getString());
                murders.add(nearestPlayer.getName().getString());
            }
        }
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) {
            return ;
        }

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) {
                continue;
            }
            boolean hasIronSword = !player.getMainHandStack().isEmpty() && InventoryUtil.isSword(player.getMainHandStack().getItem());
            if (!hasIronSword) {
                for (int i = 0; i < player.getInventory().size(); i++) {
                    if (!player.getInventory().getStack(i).isEmpty() && InventoryUtil.isSword(player.getInventory().getStack(i).getItem())) {
                        hasIronSword = true;
                        break;
                    }
                }
            }

            if (hasIronSword) {
                String playerName = player.getName().getString();
                if (!notified || !Objects.equals(murderName, playerName)) {
                    String text = Vergence.TEXT.get("Module.Modules.MurderCatcher.Messages.ItemCheckSuppose")
                            .replace("{murder}", playerName)
                            .replace("{distance}", String.format("%.2f", EntityUtil.getDistance(player)));
                    NotifyManager.newNotification(this, text);
                    MessageManager.newMessage(this, text);
                    notified = true;
                    murderName = playerName;

                    if (markEnemy.getValue()) {
                        Vergence.ENEMY.addEnemy(playerName);
                        murders.add(playerName);
                    }
                }
            }
        }
    }

    private void reset() {
        notified = false;
        for (String name : murders) {
            Vergence.ENEMY.removeEnemy(name);
        }
    }

    @Override
    public void onEnable() {
        reset();
    }

    @Override
    public void onDisable() {
        reset();
    }

    @Override
    public void onLogout() {
        reset();
    }
}
