package cc.vergence.modules.movement;

import cc.vergence.Vergence;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.modules.Module;

public class AutoWalk extends Module {
    public AutoWalk() {
        super("AutoWalk", Category.MOVEMENT);
    }

    public Option<Enum<?>> mode = addOption(new EnumOption("Mode", modes.GoStraight));

    @Override
    public String getDetails() {
        return mode.getValue().name();
    }

    @Override
    public void onEnable() {
        if (mc.player == null) {
            return;
        }

        if (mode.getValue().equals(modes.Baritone)) {
            Vergence.NETWORK.sendChatMessage("#goto " + 1000000 * Math.cos(Math.toRadians(mc.player.getYaw() + 90f)) + " " + 1000000 * Math.sin(Math.toRadians(mc.player.getYaw() + 90f)));
        }
    }

    @Override
    public void onDisable() {
        if (mc.player == null) {
            return;
        }

        if (mode.getValue().equals(modes.Baritone)) {
            Vergence.NETWORK.sendChatMessage("#stop");
        }
    }

    @Override
    public void onTick() {
        if (mc.player == null) {
            return;
        }

        if (mode.getValue().equals(modes.GoStraight)) {
            mc.options.forwardKey.setPressed(true);
        }
    }

    public enum modes {
        GoStraight, Baritone
    }
}
