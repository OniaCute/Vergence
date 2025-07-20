package cc.vergence.features.managers.other;

import cc.vergence.Vergence;
import cc.vergence.modules.Module;
import cc.vergence.util.interfaces.IChatHud;
import cc.vergence.util.interfaces.Wrapper;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

import java.util.HashMap;

public class MessageManager implements Wrapper {
    public static final String SYNC = "§(";
    public static HashMap<OrderedText, StringVisitable> messages = new HashMap<>();

    public static void blockedMessage(Module moduleA, Module moduleB) {
        newMessage(moduleA,
                Vergence.TEXT.get("Module.Special.Messages.Blocked")
                .replace("{a}", moduleA.getDisplayName())
                .replace("{b}", moduleB.getDisplayName()), -1);
    }

    public static void newMessage(Module module, String text, int id) {
        newMessage(module.getDisplayName(), text, id);
    }

    public static void newMessage(String source, String text, int id) {
        if (mc.player == null || mc.world == null) {
            return ;
        }
        ((IChatHud) mc.inGameHud.getChatHud()).vergence$add(Text.of(SYNC + "§r[" + source + "§r]§f " + text), id);
    }

    public static void newMessage(Module module, String text) {
        newMessage(module.getDisplayName(), text);
    }

    public static void newMessage(String source, String text) {
        if (mc.player == null || mc.world == null) {
            return ;
        }
        mc.inGameHud.getChatHud().addMessage(Text.of(SYNC + "§r[" + source + "§r] §r" + text));
    }
}
