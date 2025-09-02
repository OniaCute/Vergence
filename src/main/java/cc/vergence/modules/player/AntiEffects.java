package cc.vergence.modules.player;

import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import net.minecraft.entity.effect.StatusEffects;

import java.util.EnumSet;

public class AntiEffects extends Module {
    public static AntiEffects INSTANCE;

    public AntiEffects() {
        super("AntiEffects", Category.PLAYER);
        INSTANCE = this;
    }

    public Option<EnumSet<Effects>> items = addOption(new MultipleOption<Effects>("Effects", EnumSet.of(Effects.Blindness, Effects.Nausea, Effects.Levitation)));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (isNull()) {
            return;
        }

        if (items.getValue().contains(Effects.Blindness) && mc.player.hasStatusEffect(StatusEffects.BLINDNESS)) {
            mc.player.removeStatusEffect(StatusEffects.BLINDNESS);
        }
        if (items.getValue().contains(Effects.Nausea) && mc.player.hasStatusEffect(StatusEffects.NAUSEA)) {
            mc.player.removeStatusEffect(StatusEffects.NAUSEA);
        }
        if (items.getValue().contains(Effects.MiningFatigue) && mc.player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            mc.player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
        }
        if (items.getValue().contains(Effects.Levitation) && mc.player.hasStatusEffect(StatusEffects.LEVITATION)) {
            mc.player.removeStatusEffect(StatusEffects.LEVITATION);
        }
        if (items.getValue().contains(Effects.Slowness) && mc.player.hasStatusEffect(StatusEffects.SLOWNESS)) {
            mc.player.removeStatusEffect(StatusEffects.SLOWNESS);
        }
        if (items.getValue().contains(Effects.Weakness) && mc.player.hasStatusEffect(StatusEffects.WEAKNESS)) {
            mc.player.removeStatusEffect(StatusEffects.WEAKNESS);
        }
    }

    public enum Effects {
        Blindness,
        Nausea,
        MiningFatigue,
        Levitation,
        Slowness,
        Weakness
    }
}
