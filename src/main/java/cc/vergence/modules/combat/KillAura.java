package cc.vergence.modules.combat;

import cc.vergence.Vergence;
import cc.vergence.features.enums.player.RotateModes;
import cc.vergence.features.enums.player.SwingModes;
import cc.vergence.features.enums.player.TargetTypes;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.MultipleOption;
import cc.vergence.modules.Module;
import cc.vergence.modules.client.AntiCheat;
import cc.vergence.util.interfaces.Wrapper;

import java.util.EnumSet;

public class KillAura extends Module implements Wrapper {
    public static KillAura INSTANCE;

    public KillAura() {
        super("KillAura", Category.COMBAT);
        INSTANCE = this;
    }

    public Option<EnumSet<TargetTypes>> targets = addOption(new MultipleOption<>("Targets", EnumSet.of(TargetTypes.EnemyPlayers, TargetTypes.Mobs)));
    public Option<Enum<?>> clickType = addOption(new EnumOption("ClickType", ClickTypes.New));
    public Option<Double> range = addOption(new DoubleOption("Range", 1, 7, 3));
    public Option<Double> fov = addOption(new DoubleOption("FOV", 1, 180, 180, v -> !AntiCheat.INSTANCE.isGrim()));
    public Option<Double> minCPS = addOption(new DoubleOption("MinCPS", 1, 18, 3, v -> clickType.getValue().equals(ClickTypes.Old)));
    public Option<Double> maxCPS = addOption(new DoubleOption("MaxCPS", 1, 18, 7, v -> clickType.getValue().equals(ClickTypes.Old)));
    public Option<Double> delay = addOption(new DoubleOption("Delay", 1, 18, 7, v -> clickType.getValue().equals(ClickTypes.New)).addSpecialValue(1, "INSTANT"));
    public Option<Boolean> crosshairLock = addOption(new BooleanOption("CrosshairLock", true, v -> AntiCheat.INSTANCE.isLegit()));
    public Option<Enum<?>> rotateType = addOption(new EnumOption("RotateType", RotateModes.Server));
    public Option<Double> rotateSpeed = addOption(new DoubleOption("RotateSpeed", 1, 180, 180).addSpecialValue(1, "INSTANT"));
    public Option<Boolean> rotateLock = addOption(new BooleanOption("RotateLock", true, v -> !AntiCheat.INSTANCE.isGrim()));
    public Option<Double> rotateLockTime = addOption(new DoubleOption("RotateLockTime", 1, 300, 40, v -> rotateLock.getValue() && !AntiCheat.INSTANCE.isGrim()).setUnit("ms"));
    public Option<Enum<?>> swingMode = addOption(new EnumOption("SwingMode", SwingModes.Client));

    @Override
    public String getDetails() {
        return AntiCheat.INSTANCE.antiCheat.getValue().name() + " | " + clickType.getValue().name();
    }

    public enum ClickTypes {
        Old, New
    }
}
