package cc.vergence.modules.visual;

import cc.vergence.features.enums.player.TargetTypes;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class NoInvisible extends Module {
    public static NoInvisible INSTANCE;

    public NoInvisible() {
        super("NoInvisible", Category.VISUAL);
        INSTANCE = this;
    }

    public Option<EnumSet<Targets>> targets = addOption(new MultipleOption<Targets>("Targets", EnumSet.of(Targets.Players)));

    @Override
    public String getDetails() {
        return "";
    }

    public boolean isValid(Object obj) {
        if (obj instanceof Entity) {
            if (obj instanceof PlayerEntity && targets.getValue().contains(Targets.Players)) {
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

    public enum Targets {
        Players,
        Mobs,
        Animals,
        ArmorStands
    }
}
