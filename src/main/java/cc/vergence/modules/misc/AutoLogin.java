package cc.vergence.modules.misc;

import cc.vergence.Vergence;
import cc.vergence.features.event.events.PacketEvent;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.modules.Module;
import cc.vergence.util.other.FastTimerUtil;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class AutoLogin extends Module {
    public static AutoLogin INSTANCE;
    private final FastTimerUtil timer = new FastTimerUtil();

    public AutoLogin() {
        super("AutoLogin", Category.MISC);
    }

    public Option<String> password = addOption(new TextOption("Password", "password"));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onReceivePacket(PacketEvent.Receive event, Packet<?> packet) {
        if(isNull() || !timer.passedMs(10000)) {
            return;
        }

        if(event.getPacket() instanceof GameMessageS2CPacket packet1) {
            String s = packet1.content().getString().toLowerCase();

            if(s.contains("/register") || s.contains("/reg")) {
                Vergence.NETWORK.sendCommand("register " + password.getValue() + " " + password.getValue());
                timer.reset();
            } else if (s.contains("/login") || s.contains("/l")) {
                Vergence.NETWORK.sendCommand("login " + password.getValue());
                timer.reset();
            }
        }
    }
}
