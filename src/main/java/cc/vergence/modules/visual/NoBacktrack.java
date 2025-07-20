package cc.vergence.modules.visual;

import cc.vergence.modules.Module;

public class NoBacktrack extends Module {
    public static NoBacktrack INSTANCE;

    public NoBacktrack() {
        super("NoBacktrack", Category.VISUAL);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
