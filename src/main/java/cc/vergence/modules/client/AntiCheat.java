package cc.vergence.modules.client;

import cc.vergence.features.enums.client.AntiCheats;
import cc.vergence.features.enums.player.RotateModes;
import cc.vergence.features.enums.player.SwingModes;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;

public class AntiCheat extends Module {
    public static AntiCheat INSTANCE;

    public AntiCheat() {
        super("AntiCheat", Category.CLIENT);
        INSTANCE = this;
        setAlwaysEnable(true);
    }

    public Option<Enum<?>> antiCheat = addOption(new EnumOption("AntiCheat", AntiCheats.Legit));
    public Option<Enum<?>> rotateMode = addOption(new EnumOption("RotateMode", RotateModes.Server));
    public Option<Enum<?>> swingMode = addOption(new EnumOption("SwingMode", SwingModes.Both));

    @Override
    public String getDetails() {
        return "";
    }

    public boolean isLegit() {
        return antiCheat.getValue().equals(AntiCheats.Legit);
    }

    public boolean isGrim() {
        return antiCheat.getValue().equals(AntiCheats.Grim);
    }

    public boolean isNone() {
        return antiCheat.getValue().equals(AntiCheats.None);
    }

    public boolean isMatrix() {
        return antiCheat.getValue().equals(AntiCheats.Matrix);
    }

    public boolean isNCP() {
        return antiCheat.getValue().equals(AntiCheats.NCP);
    }

    public String getAntiCheat() {
        return antiCheat.getValue().name();
    }
}
