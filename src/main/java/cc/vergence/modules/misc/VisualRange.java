package cc.vergence.modules.misc;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.EntityRemoveEvent;
import cc.vergence.features.event.events.EntitySpawnEvent;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;

import java.util.ArrayList;

public class VisualRange extends Module {
    public static VisualRange INSTANCE;
    private static final ArrayList<String> players = new ArrayList<>();

    public VisualRange() {
        super("VisualRange", Category.MISC);
        INSTANCE = this;
    }

    public Option<Boolean> playerJoin = addOption(new BooleanOption("PlayerJoin", true));
    public Option<Boolean> playerLeave = addOption(new BooleanOption("PlayerLeave", true));
    public Option<Boolean> forFriend = addOption(new BooleanOption("Friend", true));
    public Option<Boolean> withSound = addOption(new BooleanOption("Sound", true));

    @Override
    public String getDetails() {
        return String.valueOf(players.size());
    }

    @Override
    public void onEntitySpawn(EntitySpawnEvent event, Entity entity) {
        if (!isValid(entity)) {
            return;
        }
        if (!players.contains(event.getEntity().getName().getString())) {
            players.add(event.getEntity().getName().getString());
        } else {
            return;
        }
        if (playerJoin.getValue()) {
            notify(entity, true);
        }
    }

    @Override
    public void onEntityRemove(EntityRemoveEvent event, Entity entity) {
        if (!isValid(entity)) {
            return;
        }
        if (players.contains(event.getEntity().getName().getString())) {
            players.remove(event.getEntity().getName().getString());
        } else {
            return;
        }
        if (playerLeave.getValue()) {
            notify(entity, false);
        }
    }

    public void notify(Entity entity, boolean enter) {
        String message = "";

        if (enter) {
            message = Vergence.TEXT.get("Module.Modules.VisualRange.Messages.Join").replace("{player}", entity.getName().getString());
        } else {
            message = Vergence.TEXT.get("Module.Modules.VisualRange.Messages.Leave").replace("{player}", entity.getName().getString());
        }

        NotifyManager.newNotification(this, message);

        if (withSound.getValue()) {
            try {
                mc.world.playSound(mc.player, mc.player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
            } catch (Exception ignored) {
            }
        }
    }

    public boolean isValid(Entity entity) {
        if (!(entity instanceof PlayerEntity)) {
            return false;
        }
        return entity != mc.player && (!Vergence.FRIEND.isFriend(entity.getName().getString()) || forFriend.getValue());
    }
}
