package cc.vergence.modules.misc;

import cc.vergence.features.managers.client.ConfigManager;
import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.DoubleOption;
import cc.vergence.features.options.impl.EnumOption;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.modules.Module;
import cc.vergence.util.other.FastTimerUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Spammer extends Module {
    public static Spammer INSTANCE;
    private int index = 0;
    private final Random random = new Random();
    private ArrayList<String> messages = new ArrayList<>();
    private FastTimerUtil timer = new FastTimerUtil();

    public Spammer() {
        super("Spammer", Category.MISC);
        INSTANCE = this;
        timer.reset();
    }

    public Option<Double> cooldown = addOption(new DoubleOption("Cooldown", 0, 120, 30).setUnit("s"));
    public Option<Enum<?>> listOrder = addOption(new EnumOption("ListOrder", ListOrder.Random));
    public Option<String> fileName = addOption(new TextOption("FileName", "default.txt"));

    @Override
    public String getDetails() {
        return String.valueOf(cooldown.getValue().intValue());
    }

    @Override
    public void onTick() {
        if (messages.isEmpty()) {
            NotifyManager.newNotification(this, "File is empty!");
            return ;
        }

        if (timer.passedS(cooldown.getValue())) {
            String msg;
            if (listOrder.getValue() == ListOrder.Random) {
                msg = messages.get(random.nextInt(messages.size()));
            } else {
                msg = messages.get(index);
                index = (index + 1) % messages.size();
            }
            if (!msg.trim().isEmpty()) {
                Objects.requireNonNull(mc.getNetworkHandler()).sendChatMessage(msg);
            }
            timer.reset();
        }
    }

    @Override
    public void onEnable() {
        loadMessages();
        timer.reset();
    }

    private void loadMessages() {
        messages.clear();
        Path path = Paths.get(ConfigManager.MISC_SPAMMER_FOLDER + "/" + fileName.getValue());
        try {
            if (Files.exists(path)) {
                messages.addAll(Files.readAllLines(path));
            } else {
                NotifyManager.newNotification(this, "File not found : " + fileName.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum ListOrder {
        Order,
        Random
    }
}
