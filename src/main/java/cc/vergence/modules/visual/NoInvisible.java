package cc.vergence.modules.visual;

import cc.vergence.modules.Module;

public class NoInvisible extends Module {
    public static NoInvisible INSTANCE;

    public NoInvisible() {
        super("NoInvisible", Category.VISUAL);
        INSTANCE = this;
    }

    @Override
    public String getDetails() {
        return "";
    }
}
