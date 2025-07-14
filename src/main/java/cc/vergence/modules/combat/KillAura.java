package cc.vergence.modules.combat;

import cc.vergence.Vergence;
import cc.vergence.features.enums.AntiCheats;
import cc.vergence.features.enums.RotateModes;
import cc.vergence.features.managers.MessageManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;

public class KillAura extends Module {
    public static KillAura INSTANCE;

    public KillAura() {
        super("KillAura", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<Enum<?>> antiCheat = addOption(new EnumOption("AntiCheat", AntiCheats.Legit));
    public Option<Enum<?>> clickType = addOption(new EnumOption("ClickType", ClickTypes.New));
    public Option<Double> range = addOption(new DoubleOption("Range", 1, 7, 3));
    public Option<Double> fov = addOption(new DoubleOption("FOV", 1, 180, 180));
    public Option<Double> minCPS = addOption(new DoubleOption("MinCPS", 1, 18, 3, v -> clickType.getValue().equals(ClickTypes.Old)));
    public Option<Double> maxCPS = addOption(new DoubleOption("MaxCPS", 1, 18, 7, v -> clickType.getValue().equals(ClickTypes.Old)));
    public Option<Double> delay = addOption(new DoubleOption("Delay", 1, 18, 7, v -> clickType.getValue().equals(ClickTypes.New)));
    public Option<Enum<?>> rotateType = addOption(new EnumOption("RotateType", RotateModes.Legit));

    @Override
    public String getDetails() {
        return antiCheat.getValue().name() + " | " + clickType.getValue().name();
    }

    @Override
    public void onBlock(Module module) {
        MessageManager.blockedMessage(this, module);
    }

    public enum ClickTypes {
        Old,
        New
    }
}
