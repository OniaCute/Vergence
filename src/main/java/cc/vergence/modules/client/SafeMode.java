package cc.vergence.modules.client;

import cc.vergence.Vergence;
import cc.vergence.features.enums.client.AntiCheats;
import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.combat.*;
import cc.vergence.modules.exploit.MovementLagSpoof;
import cc.vergence.modules.misc.Spammer;
import cc.vergence.modules.movement.*;
import cc.vergence.modules.player.CarryPro;
import cc.vergence.modules.player.ChestStealer;
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
        return antiCheatMode.getValue().equals(AntiCheats.Legit) ? "Legit" : "Unsafe";
    }

    @Override
    public void onRegister() {
        this.enable(); // default enable
    }

    @Override
    public void onEnable() {
        defaultValue.clear();
        if (isNull() || !Vergence.LOADED) {
            return ;
        }
        doBlock();
    }

    private void addBlocked(Module module) {
        defaultValue.put(module, module.getStatus());
        module.block(this);
    }

    @Override
    public void onDisable() {
        release();
    }

    @Override
    public void onConfigChange() {
        release();
        doBlock();
    }

    public void release() {
        if (defaultValue.isEmpty()) {
            return;
        }
        for (Module module : defaultValue.keySet()) {
            module.unblock(defaultValue.get(module));
        }
    }

    public void doBlock() {
        switch ((AntiCheats) antiCheatMode.getValue()) {
            case Legit -> {
                addBlocked(KillAura.INSTANCE);
                addBlocked(Scaffold.INSTANCE);
                addBlocked(InventoryMove.INSTANCE);
                addBlocked(NoFall.INSTANCE);
                addBlocked(Reach.INSTANCE);
                addBlocked(MovementLagSpoof.INSTANCE);
                addBlocked(AntiLevitation.INSTANCE);
                addBlocked(TickShift.INSTANCE);
                addBlocked(CrystalAura.INSTANCE);
                addBlocked(Surround.INSTANCE);
                addBlocked(Defender.INSTANCE);
                addBlocked(AutoReplenish.INSTANCE);
                addBlocked(CarryPro.INSTANCE);
                addBlocked(QuickStop.INSTANCE);
                if (!ChestStealer.INSTANCE.random.getValue() || ChestStealer.INSTANCE.minDelay.getValue() < 60) {
                    addBlocked(ChestStealer.INSTANCE);
                }
                if (FastUse.INSTANCE.delay.getValue() < 1) {
                    addBlocked(FastUse.INSTANCE);
                }
                if (Spammer.INSTANCE.cooldown.getValue() < 300) {
                    addBlocked(Spammer.INSTANCE);
                }
                if (SafeWalk.INSTANCE.doInject.getValue()) {
                    addBlocked(SafeWalk.INSTANCE);
                }
                if (AutoSprint.INSTANCE.forAttack.getValue() || AutoSprint.INSTANCE.useItem.getValue()) {
                    addBlocked(AutoSprint.INSTANCE);
                }
            }
            case NCP -> {
                addBlocked(KillAura.INSTANCE);
                addBlocked(Scaffold.INSTANCE);
                addBlocked(NoFall.INSTANCE);
                if (Spammer.INSTANCE.cooldown.getValue() < 300) {
                    addBlocked(Spammer.INSTANCE);
                }
                if (SafeWalk.INSTANCE.doInject.getValue()) {
                    addBlocked(SafeWalk.INSTANCE);
                }
                if (AutoSprint.INSTANCE.forAttack.getValue() || AutoSprint.INSTANCE.useItem.getValue()) {
                    addBlocked(AutoSprint.INSTANCE);
                }
            }
        }
    }
}
