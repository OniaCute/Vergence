package cc.vergence.modules.misc;

import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.ColorOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.modules.Module;

import java.awt.*;

public class BetterTab extends Module {
    public static BetterTab INSTANCE;
    public BetterTab() {
        super("BetterTab", Category.MISC);
        INSTANCE = this;
    }

    public Option<Boolean> clean = addOption(new BooleanOption("Clean", true));
    public Option<Double> playerLimit = addOption(new DoubleOption("PlayerLimit", 1, 3000, 500).addSpecialValue(3000, "INFINITE"));
    public Option<Boolean> forMyself = addOption(new BooleanOption("Myself", true));
    public Option<String> myPrefix = addOption(new TextOption("MyPrefix", "", v -> forMyself.getValue()));
    public Option<String> mySuffix = addOption(new TextOption("MySuffix", "", v -> forMyself.getValue()));
    public Option<Color> myColor = addOption(new ColorOption("MyColor", new Color(249, 158, 255), v -> forMyself.getValue()));
    public Option<Boolean> forFriends = addOption(new BooleanOption("Friends", true));
    public Option<String> friendPrefix = addOption(new TextOption("FriendPrefix", "", v -> forFriends.getValue()));
    public Option<String> friendSuffix = addOption(new TextOption("FriendSuffix", "", v -> forFriends.getValue()));
    public Option<Color> friendColor = addOption(new ColorOption("FriendColor", new Color(0, 140, 255), v -> forFriends.getValue()));
    public Option<Boolean> forEnemy = addOption(new BooleanOption("Enemies", true));
    public Option<String> enemyPrefix = addOption(new TextOption("EnemyPrefix", "", v -> forEnemy.getValue()));
    public Option<String> enemySuffix = addOption(new TextOption("EnemySuffix", "", v -> forEnemy.getValue()));
    public Option<Color> enemyColor = addOption(new ColorOption("EnemyColor", new Color(227, 15, 15), v -> forEnemy.getValue()));

    @Override
    public String getDetails() {
        return "";
    }
}
