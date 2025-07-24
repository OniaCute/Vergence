package cc.vergence.modules.combat;

import cc.vergence.Vergence;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;
import cc.vergence.util.other.FastTimerUtil;
import cc.vergence.util.player.EntityUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.text.Text;

public class AutoLogout extends Module {
    public static AutoLogout INSTANCE;
    private FastTimerUtil timer = new FastTimerUtil();

    public AutoLogout() {
        super("AutoLogout", Category.COMBAT);
        INSTANCE = this;
        timer.reset();
    }

    public Option<Boolean> autoDisable = addOption(new BooleanOption("AutoDisable", true));
    public Option<Boolean> antiMistake = addOption(new BooleanOption("AntiMistake", true));
    public Option<Double> regretTime = addOption(new DoubleOption("RegretTime", 8, 20, 10).setUnit("s"));
    public Option<Boolean> illegal = addOption(new BooleanOption("IllegalDisconnect", false));
    public Option<Boolean> onHealth = addOption(new BooleanOption("Health", true));
    public Option<Double> lowestHealth = addOption(new DoubleOption("LowestHealth", 1, 35, 6, v -> onHealth.getValue()));
    public Option<Boolean> onPlayers = addOption(new BooleanOption("Players", false));
    public Option<Double> maxPlayers = addOption(new DoubleOption("MaxPlayers", 1, 30, 12, v -> onPlayers.getValue()).addSpecialValue(1, "ANYBODY"));
    public Option<Boolean> onDistance = addOption(new BooleanOption("Distance", false));
    public Option<Double> minDistance = addOption(new DoubleOption("MinDistance", 1, 40, 8, v -> onDistance.getValue()));
    public Option<Boolean> ignoreFriend = addOption(new BooleanOption("IgnoreFriend", true, v -> onPlayers.getValue() || onDistance.getValue()));
    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onLogin() {
        timer.reset();
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    @Override
    public void onDisable() {
        timer.reset();
    }

    @Override
    public void onTick() {
        if ((!timer.passedS(regretTime.getValue()) && antiMistake.getValue()) || isNull()) {
            return ;
        }

        if (onHealth.getValue()) {
            if (mc.player.getHealth() + mc.player.getAbsorptionAmount() <= lowestHealth.getValue()) {
                doDisconnect(Vergence.TEXT.get("Module.Modules.AutoLogout.Messages.LowHealth"));
            }
        }
        else if (onPlayers.getValue()) {
            if (mc.world.getPlayers().size() >= maxPlayers.getValue()) {
                doDisconnect(Vergence.TEXT.get("Module.Modules.AutoLogout.Messages.TooManyPlayers").replace("{amount}", String.valueOf(mc.world.getPlayers().size())));
            }
        }
        else if (onDistance.getValue()) {
            AbstractClientPlayerEntity player = mc.world.getPlayers().stream().filter(p -> !Vergence.FRIEND.isFriend(p)).findFirst().orElse(null);
            if (EntityUtil.getDistance(player) <= minDistance.getValue()) {
                doDisconnect(Vergence.TEXT.get("Module.Modules.AutoLogout.Messages.TooCloseAround").replace("{distance}", String.valueOf(EntityUtil.getDistance(player))));
            }
        }
    }

    private void doDisconnect(String reason) {
        if (illegal.getValue()) {
            Vergence.NETWORK.sendPacket(PlayerInteractEntityC2SPacket.attack(mc.player, false)); // send illegal packet to disconnect
            if (autoDisable.getValue()) {
                disable();
            }
            return;
        }
        if (mc.getNetworkHandler() == null) {
            mc.world.disconnect();
            if (autoDisable.getValue()) {
                disable();
            }
            return;
        }
        mc.getNetworkHandler().getConnection().disconnect(Text.of(reason));
        if (autoDisable.getValue()) {
            disable();
        }
    }

    @Override
    public void onLogout() {
        timer.reset();
    }
}
