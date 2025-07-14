package cc.vergence.modules.client;

import cc.vergence.features.enums.AntiCheats;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.combat.KillAura;
import cc.vergence.modules.combat.Reach;
import cc.vergence.modules.exploit.AntiHungry;
import cc.vergence.modules.misc.Spammer;
import cc.vergence.modules.movement.Scaffold;
import cc.vergence.modules.player.FastUse;

import java.util.HashMap;

public class SafeMode extends Module {
    public static SafeMode INSTANCE;
    public HashMap<Module, Boolean> defaultValue = new HashMap<>();

    public SafeMode() {
        super("SafeMode", Category.CLIENT, -1); // Highest priority
        INSTANCE = this;
    }

    // any function that may affect the detection should be checked first for these items.
    public Option<Enum<?>> antiCheatMode = addOption(new EnumOption("AntiCheatMode", AntiCheats.Legit));
    public Option<Boolean> allowTimer = addOption(new BooleanOption("AllowTimer", false));

    @Override
    public String getDetails() {
        return antiCheatMode.getValue().equals(AntiCheats.Grim) ? "Legit" : "Unsafe";
    }

    @Override
    public void onRegister() {
        this.enable(); // default enable
    }

    @Override
    public void onEnable() {
        switch ((AntiCheats) antiCheatMode.getValue()) {
            case None -> {
                if (KillAura.INSTANCE.getStatus() && !KillAura.INSTANCE.antiCheat.getValue().equals(antiCheatMode.getValue())){
                    defaultValue.put(KillAura.INSTANCE, KillAura.INSTANCE.getStatus());
                    KillAura.INSTANCE.block(this);
                }
                if (Scaffold.INSTANCE.getStatus() && !Scaffold.INSTANCE.antiCheat.getValue().equals(antiCheatMode.getValue())){
                    defaultValue.put(Scaffold.INSTANCE, Scaffold.INSTANCE.getStatus());
                    Scaffold.INSTANCE.block(this);
                }
                if (FastUse.INSTANCE.getStatus() && FastUse.INSTANCE.delay.getValue() < 2){
                    defaultValue.put(FastUse.INSTANCE, FastUse.INSTANCE.getStatus());
                    FastUse.INSTANCE.block(this);
                }
                if (Reach.INSTANCE.getStatus()){
                    defaultValue.put(Reach.INSTANCE, Reach.INSTANCE.getStatus());
                    Reach.INSTANCE.block(this);
                }
                if (AntiCheat.INSTANCE.getStatus() && !AntiCheat.INSTANCE.antiCheat.getValue().equals(antiCheatMode.getValue())){
                    defaultValue.put(AntiCheat.INSTANCE, AntiCheat.INSTANCE.getStatus());
                    AntiCheat.INSTANCE.block(this);
                }
                if (Spammer.INSTANCE.getStatus() && Spammer.INSTANCE.cooldown.getValue() < 300){
                    defaultValue.put(Spammer.INSTANCE, Spammer.INSTANCE.getStatus());
                    Spammer.INSTANCE.block(this);
                }
                if (AntiHungry.INSTANCE.getStatus() && !AntiHungry.INSTANCE.antiCheat.getValue().equals(antiCheatMode.getValue())){
                    defaultValue.put(AntiHungry.INSTANCE, AntiHungry.INSTANCE.getStatus());
                    AntiHungry.INSTANCE.block(this);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        if (defaultValue.isEmpty()) {return;}

        for (Module module : defaultValue.keySet()) {
            module.unblock(defaultValue.get(module));
        }
    }
}
