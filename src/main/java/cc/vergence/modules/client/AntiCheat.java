package cc.vergence.modules.client;

import cc.vergence.features.enums.client.AntiCheats;
import cc.vergence.features.enums.player.PlaceModes;
import cc.vergence.features.enums.player.RotateModes;
import cc.vergence.features.enums.player.SwingModes;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;

public class AntiCheat extends Module {
    public static AntiCheat INSTANCE;

    public AntiCheat() {
        super("AntiCheat", Category.CLIENT);
        INSTANCE = this;
        setAlwaysEnable(true);
    }

    public Option<Double> fov = addOption(new DoubleOption("Fov", 0, 180, 90));
    public Option<Enum<?>> antiCheat = addOption(new EnumOption("AntiCheat", AntiCheats.Legit));
    public Option<Enum<?>> rotateMode = addOption(new EnumOption("RotateMode", RotateModes.Server));
    public Option<Enum<?>> swingMode = addOption(new EnumOption("SwingMode", SwingModes.Both));
    public Option<Enum<?>> placeMode = addOption(new EnumOption("PlaceMode", PlaceModes.Legit));
    public Option<Boolean> attackRotate = addOption(new BooleanOption("AttackRotate", false));
    public Option<Double> attackDelay = addOption(new DoubleOption("AttackDelay", 0, 5, 1).setUnit("s"));
    public Option<Boolean> forceSync = addOption(new BooleanOption("ForceSync", false));
    public Option<Boolean> spamCheck = addOption(new BooleanOption("SpamCheck", true));
    public Option<Boolean> multiPlace = addOption(new BooleanOption("MultiPlace", false));
    public Option<Boolean> strictBlock = addOption(new BooleanOption("StrictBlock", false));
    public Option<Boolean> inventorySync = addOption(new BooleanOption("InventorySync", true));
    public Option<Boolean> inventoryBypass = addOption(new BooleanOption("InventoryBypass", true));
    public Option<Double> hitboxSize = addOption(new DoubleOption("HitboxSize", 0, 1, 0.6));
    public Option<Boolean> packetPlace = addOption(new BooleanOption("PacketPlace", true));

    @Override
    public String getDetails() {
        return antiCheat.getValue().name();
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

    public static double getHitboxOffset() {
        if (INSTANCE != null) {
            return INSTANCE.hitboxSize.getValue() / 2;
        }
        return 0.3;
    }
}
