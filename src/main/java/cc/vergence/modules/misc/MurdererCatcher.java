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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

// For mini-games ~
public class MurdererCatcher extends Module implements Wrapper {
    public static MurdererCatcher INSTANCE;
    private final HashSet<String> notifiedMurderers = new HashSet<>();
    private final HashSet<String> notifiedBowmen = new HashSet<>();
    private final ArrayList<String> murderers = new ArrayList<>();
    private final ArrayList<String> bowmen = new ArrayList<>();


    public MurdererCatcher() {
        super("MurdererCatcher", Category.MISC);
        INSTANCE = this;
    }

    public Option<Boolean> markEnemy = addOption(new BooleanOption("MarkEnemy", true));
    public Option<Boolean> withSound = addOption(new BooleanOption("Sound", true));


    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDeathEvent(DeathEvent event, PlayerEntity deadPlayer) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        if (mc.player == deadPlayer) {
            reset();
            return ;
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
            String text = Vergence.TEXT.get("Module.Modules.MurdererCatcher.Messages.PlayerDiedSuppose")
                    .replace("{player}", deadPlayer.getName().getString()
                    .replace("{Murderer}", nearestPlayer.getName().getString()
                    .replace("{distance}", String.format("%.2f", minDistance))));
            NotifyManager.newNotification(this, text);
            MessageManager.newMessage(this, text);
            if (withSound.getValue()) {
                try {
                    mc.world.playSound(mc.player, mc.player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.BLOCKS, 1f, 1f);
                } catch (Exception ignored) {
                }
            }

            if (markEnemy.getValue()) {
                Vergence.ENEMY.addEnemy(nearestPlayer.getName().getString());
                murderers.add(nearestPlayer.getName().getString());
            }
        }
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;

            String playerName = player.getName().getString();

            boolean hasIronSword = !player.getMainHandStack().isEmpty() && InventoryUtil.isSword(player.getMainHandStack().getItem());
            if (!hasIronSword) {
                for (int i = 0; i < player.getInventory().size(); i++) {
                    if (!player.getInventory().getStack(i).isEmpty() && InventoryUtil.isSword(player.getInventory().getStack(i).getItem())) {
                        hasIronSword = true;
                        break;
                    }
                }
            }

            if (hasIronSword && !notifiedMurderers.contains(playerName)) {
                String text = Vergence.TEXT.get("Module.Modules.MurdererCatcher.Messages.ItemCheckSuppose")
                        .replace("{murderer}", playerName)
                        .replace("{distance}", String.format("%.2f", EntityUtil.getDistance(player)));

                NotifyManager.newNotification(this, text);
                MessageManager.newMessage(this, text);
                if (withSound.getValue()) {
                    try {
                        mc.world.playSound(mc.player, mc.player.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
                    } catch (Exception ignored) {}
                }

                if (markEnemy.getValue()) {
                    Vergence.ENEMY.addEnemy(playerName);
                    murderers.add(playerName);
                }

                notifiedMurderers.add(playerName);
            }

            boolean hasBowOrArrow = !player.getMainHandStack().isEmpty() && player.getMainHandStack().getItem().getTranslationKey().contains("bow") || player.getMainHandStack().getItem().getTranslationKey().contains("arrow");
            for (int i = 0; i < player.getInventory().size(); i++) {
                var stack = player.getInventory().getStack(i);
                if (stack.isEmpty()) continue;
                String name = stack.getItem().getTranslationKey();
                if (name.contains("bow") || name.contains("arrow")) {
                    hasBowOrArrow = true;
                    break;
                }
            }

            if (hasBowOrArrow && !notifiedBowmen.contains(playerName)) {
                String text = Vergence.TEXT.get("Module.Modules.MurdererCatcher.Messages.BowCheck")
                        .replace("{bowmen}", playerName)
                        .replace("{distance}", String.format("%.2f", EntityUtil.getDistance(player)));

                NotifyManager.newNotification(this, text);
                MessageManager.newMessage(this, text);
                if (withSound.getValue()) {
                    try {
                        mc.world.playSound(mc.player, mc.player.getBlockPos(), SoundEvents.ENTITY_VILLAGER_TRADE, SoundCategory.BLOCKS, 1f, 1f);
                    } catch (Exception ignored) {}
                }

                Vergence.FRIEND.addFriend(playerName);
                bowmen.add(playerName);
                notifiedBowmen.add(playerName);
            }
        }
    }


    private void reset() {
        for (String name : murderers) {
            Vergence.ENEMY.removeEnemy(name);
        }
        murderers.clear();
        notifiedMurderers.clear();

        for (String name : bowmen) {
            Vergence.FRIEND.removeFriend(name);
        }
        bowmen.clear();
        notifiedBowmen.clear();
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

    @Override
    public void onShutDown() {
        reset();
    }
}
