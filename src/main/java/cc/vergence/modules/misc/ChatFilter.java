package cc.vergence.modules.misc;

import cc.vergence.features.event.events.ReceiveMessageEvent;
import cc.vergence.features.options.Option;
import cc.vergence.features.options.impl.TextOption;
import cc.vergence.modules.Module;

import java.util.Arrays;

public class ChatFilter extends Module {
    public static ChatFilter INSTANCE;

    public ChatFilter() {
        super("ChatFilter", Category.MISC);
    }

    public Option<String> text = addOption(new TextOption("Text", "join|buy"));
    public Option<String> unless = addOption(new TextOption("Unless", "{player}|alert|warn|ban|check|mail"));

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public void onReceivedMessage(ReceiveMessageEvent event, String message) {
        String[] filterArr = Arrays.stream(text.getValue().toLowerCase().split("\\|"))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
        String[] unlessArr = Arrays.stream(unless.getValue().toLowerCase().split("\\|"))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        String content = message.toLowerCase();
        boolean hitFilter = Arrays.stream(filterArr).anyMatch(content::contains);
        boolean hitUnless = Arrays.stream(unlessArr).anyMatch(content::contains);

        if (hitFilter && !hitUnless) {
            event.cancel();
        }
    }
}
