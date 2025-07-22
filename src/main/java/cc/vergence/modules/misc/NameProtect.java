package cc.vergence.modules.misc;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.modules.Module;

public class NameProtect extends Module {
    public static NameProtect INSTANCE;

    public NameProtect() {
        super("NameProtect", Category.MISC);
        INSTANCE = this;
    }

    public Option<String> nickname = addOption(new TextOption("NickName", "Vergence User"));

    @Override
    public String getDetails() {
        return "";
    }
}
