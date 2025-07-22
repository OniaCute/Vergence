package cc.vergence.modules.misc;

import cc.vergence.Vergence;
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

public class Advertiser extends Module {
    public static Advertiser INSTANCE;
    private int index = 0;
    private final Random random = new Random();
    private ArrayList<String> messages = new ArrayList<>();
    private FastTimerUtil timer = new FastTimerUtil();

    public Advertiser() {
        super("Advertiser", Category.MISC);
        INSTANCE = this;
        timer.reset();
    }

    public Option<Double> cooldown = addOption(new DoubleOption("Cooldown", 0, 600, 30).setUnit("s"));
    public Option<String> command = addOption(new TextOption("Command", "/tell"));
    public Option<Enum<?>> listOrder = addOption(new EnumOption("ListOrder", Spammer.ListOrder.Random));
    public Option<String> fileName = addOption(new TextOption("FileName", "default.txt"));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onTick() {
        if (messages.isEmpty()) {
            NotifyManager.newNotification(this, "File is empty!");
            return ;
        }

        if (timer.passedS(cooldown.getValue())) {
            String msg;
            if (listOrder.getValue() == Spammer.ListOrder.Random) {
                msg = messages.get(random.nextInt(messages.size()));
            } else {
                msg = messages.get(index);
                index = (index + 1) % messages.size();
            }
            if (!msg.trim().isEmpty()) {
                Vergence.NETWORK.sendCommand(command.getValue() + " " + msg);
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
        Path path = Paths.get(ConfigManager.MISC_ADVERTISER_FOLDER + "/" + fileName.getValue());
        try {
            if (Files.exists(path)) {
                messages.addAll(Files.readAllLines(path));
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
