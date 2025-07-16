package cc.vergence.modules.client;

import cc.vergence.Vergence;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.BooleanOption;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.Wrapper;
import cc.vergence.util.other.FastTimerUtil;

public class Title extends Module implements Wrapper {
    public static Title INSTANCE;
    public static String title = "Vergence Client | Get unique sense of the Minecraft";
    private int length = 0;
    private int breakTimer = 0;
    private boolean isReversed = false;
    private final FastTimerUtil timer = new FastTimerUtil();

    public Title() {
        super("Title", Category.CLIENT);
    }

    public Option<String> text = addOption(new TextOption("Title", "{full_name} {version}"));
    public Option<Boolean> applyAnimation = addOption(new BooleanOption("Animation", false));
    public Option<Boolean> reverseAnimation = addOption(new BooleanOption("ReverseAnimation", true, v -> applyAnimation.getValue()));
    public Option<Double> animationDelay = addOption(new DoubleOption("AnimationDelay", 20, 1000, 200, v -> applyAnimation.getValue()).setUnit("ms"));

    @Override
    public String getDetails() {
        return text.getValue();
    }

    @Override
    public void onEnable() {
        timer.reset();
        title = "";
    }

    @Override
    public void onDisable() {
        timer.reset();
        title = "Vergence Client | Get unique sense of the Minecraft";
        mc.getWindow().setTitle(title);
    }

    @Override
    public void onTick() {
        if (!text.getValue().equals(title)) {
            title = text.getValue();
            reset();
        }

        if (!applyAnimation.getValue()) {
            mc.getWindow().setTitle(text.getValue());
            return;
        }

        if (timer.passedMs(animationDelay.getValue())) {
            timer.reset();
            if ((length == title.length() && breakTimer != 2)
                    || (length == 0 && breakTimer != 4)) {
                breakTimer++;
                return;
            }
            breakTimer = 0;

            if (length == title.length()) {
                isReversed = true;
            }
            if (length == 0) {
                isReversed = false;
            }
            if (isReversed) {
                --length;
            } else {
                ++length;
            }

            String visible = title.substring(0, length);
            mc.getWindow().setTitle(visible);
        }
    }


    private void reset() {
        length = 0;
        breakTimer = 0;
        isReversed = false;
        timer.reset();
    }
}
