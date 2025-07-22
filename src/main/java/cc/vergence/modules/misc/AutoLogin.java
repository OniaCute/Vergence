package cc.vergence.modules.misc;

import cc.vergence.Vergence;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.modules.Module;

public class AutoLogin extends Module {
    public static AutoLogin INSTANCE;

    public AutoLogin() {
        super("AutoLogin", Category.MISC);
    }

    public Option<String> username = addOption(new TextOption("Username", "{player}"));
    public Option<String> command = addOption(new TextOption("Command", "/login"));
    public Option<String> password = addOption(new TextOption("Password", "password"));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onLogin() {
        if (isNull()) {
            return ;
        }

        if (username.getValue().equals(mc.player.getName().getString())) {
            Vergence.NETWORK.sendCommand(command.getValue() + " " + password.getValue());
        }
    }
}
