package cc.vergence.modules.misc;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.modules.Module;

import java.awt.*;

public class BetterTab extends Module {
    public static BetterTab INSTANCE;
    public BetterTab() {
        super("BetterTab", Category.MISC);
        INSTANCE = this;
    }

    public Option<Double> playerLimit = addOption(new DoubleOption("PlayerLimit", 1, 3000, 500).addSpecialValue(3000, "INFINITE"));
    public Option<Boolean> forMyself = addOption(new BooleanOption("Myself", true));
    public Option<Color> myColor = addOption(new ColorOption("MyColor", new Color(249, 158, 255), v -> forMyself.getValue()));
    public Option<Boolean> forFriends = addOption(new BooleanOption("Friends", true));
    public Option<Color> friendColor = addOption(new ColorOption("FriendColor", new Color(0, 140, 255), v -> forFriends.getValue()));
    public Option<Boolean> forEnemy = addOption(new BooleanOption("Enemies", true));
    public Option<Color> enemyColor = addOption(new ColorOption("EnemyColor", new Color(227, 15, 15), v -> forEnemy.getValue()));

    @Override
    public String getDetails() {
        return "";
    }
}
