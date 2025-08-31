package cc.vergence.modules.visual;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.EntityRemoveEvent;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.combat.CombatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.EnumSet;

public class NoInvisible extends Module {
    public static NoInvisible INSTANCE;
    private final ArrayList<String> notifiedPlayer = new ArrayList<>();

    public NoInvisible() {
        super("NoInvisible", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<EnumSet<Targets>> targets = addOption(new MultipleOption<Targets>("Targets", EnumSet.of(Targets.Players)));
    public Option<Boolean> notify = addOption(new BooleanOption("Notify", false));
    public Option<Double> range = addOption(new DoubleOption("Range", 1, 32, 14, v -> notify.getValue()));
    public Option<Boolean> includeFriend = addOption(new BooleanOption("Friends", false, v -> notify.getValue()));
    public Option<Boolean> sounds = addOption(new BooleanOption("Sounds", false, v -> notify.getValue()));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onDisable() {
        notifiedPlayer.clear();
    }

    @Override
    public void onConfigChange() {
        notifiedPlayer.clear();
    }

    @Override
    public void onLogout() {
        notifiedPlayer.clear();
    }

    public boolean isValid(Object obj) {
        if (isNull()) {
            return false;
        }

        if (obj instanceof Entity) {
            if (obj instanceof PlayerEntity player && targets.getValue().contains(Targets.Players)) {
                if (player.distanceTo(mc.player) <= range.getValue() && notify.getValue()) {
                    if ((Vergence.FRIEND.isFriend(player) && !includeFriend.getValue()) || notifiedPlayer.contains(player.getName().getString())) {
                        return true;
                    }
                    NotifyManager.newNotification(this, Vergence.TEXT.get("Module.Modules.NoInvisible.Messages.Invisible").replace("{player}", player.getName().getString()));
                    notifiedPlayer.add(player.getName().getString());
                    if (sounds.getValue()) {
                        try {
                            mc.world.playSound(mc.player, mc.player.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
                        } catch (Exception ignored) {}
                    }
                }

                return true;
            }
            else if (obj instanceof MobEntity && targets.getValue().contains(Targets.Mobs)) {
                return true;
            }
            else if (obj instanceof AnimalEntity && targets.getValue().contains(Targets.Animals)) {
                return true;
            } else {
                return obj instanceof ArmorStandEntity && targets.getValue().contains(Targets.ArmorStands);
            }
        }
        return false;
    }

    @Override
    public void onEntityRemove(EntityRemoveEvent event, Entity entity) {
        if (entity instanceof PlayerEntity && notifiedPlayer.contains(entity.getName().getString())) {
            notifiedPlayer.remove(entity.getName().getString());
        }
    }

    public enum Targets {
        Players,
        Mobs,
        Animals,
        ArmorStands
    }
}
