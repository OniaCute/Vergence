package cc.vergence.modules.visual;

import cc.vergence.features.event.events.PlayerUpdateEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FullBright extends Module {
    public static FullBright INSTANCE;

    public FullBright() {
        super("FullBright", Category.VISUAL);
    }

    public Option<Enum<?>> mode = addOption(new EnumOption("Mode", Modes.Gamma));

    @Override
    public String getDetails() {
        return mode.getValue().name();
    }

    @Override
    public void onPlayerUpdateEvent(PlayerUpdateEvent event) {
        if (mode.getValue().equals(Modes.Potion) && !mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, StatusEffectInstance.INFINITE));
        }
    }

    @Override
    public void onEnable() {
        if (mc.player == null) {
            return ;
        }
        if (mode.getValue().equals(Modes.Potion) && !mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, StatusEffectInstance.INFINITE));
        }
    }

    @Override
    public void onDisable() {
        if (mc.player == null) {
            return ;
        }
        if (mode.getValue().equals(Modes.Potion) && mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }

    public enum Modes {
        Gamma,
        Potion
    }
}
