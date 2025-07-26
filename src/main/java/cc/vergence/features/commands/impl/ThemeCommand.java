package cc.vergence.features.commands.impl;

import cc.vergence.Vergence;
import cc.vergence.features.commands.Command;
import cc.vergence.features.managers.client.ConfigManager;
import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.features.themes.Theme;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ThemeCommand extends Command {

    public ThemeCommand() {
        super("theme", Vergence.TEXT.get("COMMANDS.THEME.desc"), "[save|load|list|delete] [name]");
    }

    @Override
    public void runCommand(String[] parameters) {
        if (parameters.length == 0) {
            sendUsage();
            return;
        }

        File themesFolder = ConfigManager.THEMES_FOLDER;
        String cmd = parameters[0].toLowerCase();

        switch (cmd) {
            case "save":
                if (parameters.length < 2) {
                    NotifyManager.newNotification(Vergence.TEXT.get("COMMANDS.THEME.MESSAGE.MISSING_NAME"));
                    return;
                }
                String saveName = parameters[1];
                Vergence.THEME.getTheme().setName(saveName);
                JsonObject json = Vergence.THEME.dumpToJson();
                Vergence.THEME.saveTheme(saveName, json);
                NotifyManager.newNotification(Vergence.TEXT.get("COMMANDS.THEME.MESSAGE.SAVED").replace("{theme}", saveName));
                break;

            case "load":
                if (parameters.length < 2) {
                    NotifyManager.newNotification(
                            Vergence.TEXT.get("COMMANDS.THEME.MESSAGE.MISSING_NAME"));
                    return;
                }
                File target = new File(themesFolder, parameters[1] + ".json");
                if (!target.exists()) {
                    NotifyManager.newNotification(Vergence.TEXT.get("COMMANDS.THEME.MESSAGE.NOT_FOUND").replace("{theme}", parameters[1]));
                    return;
                }
                Theme theme = null;
                try {
                    theme = Vergence.THEME.loadFromJson(JsonParser.parseReader(new FileReader(target)).getAsJsonObject());
                } catch (Exception e) {
                    NotifyManager.newNotification(Vergence.TEXT.get("COMMANDS.THEME.MESSAGE.NOT_FOUND").replace("{theme}", parameters[1]));
                    e.printStackTrace();
                }
                if (theme != null) Vergence.THEME.loadTheme(theme);
                NotifyManager.newNotification(Vergence.TEXT.get("COMMANDS.THEME.MESSAGE.LOADED").replace("{theme}", parameters[1]));
                break;

            case "list":
                File[] files = themesFolder.listFiles((d, n) -> n.endsWith(".json"));
                if (files == null || files.length == 0) {
                    NotifyManager.newNotification(Vergence.TEXT.get("COMMANDS.THEME.MESSAGE.NO_THEMES"));
                    return;
                }
                ArrayList<String> sb = new ArrayList<>();
                for (File f : files) {
                    sb.add(f.getName().replace(".json", ""));
                };
                MessageManager.newMessage("Vergence", Vergence.TEXT.get("COMMANDS.THEME.MESSAGE.LIST_TITLE"));
                for (String name : sb) {
                    MessageManager.newMessage("Vergence", "§e - " + name);
                }
                break;

            case "delete":
                if (parameters.length < 2) {
                    NotifyManager.newNotification(Vergence.TEXT.get("COMMANDS.THEME.MESSAGE.MISSING_NAME"));
                    return;
                }
                File del = new File(themesFolder, parameters[1] + ".json");
                if (!del.exists()) {
                    NotifyManager.newNotification(Vergence.TEXT.get("COMMANDS.THEME.MESSAGE.NOT_FOUND").replace("{theme}", parameters[1]));
                    return;
                }
                if ("default".equalsIgnoreCase(parameters[1])) {
                    NotifyManager.newNotification(Vergence.TEXT.get("COMMANDS.THEME.MESSAGE.CANNOT_DELETE_DEFAULT"));
                    return;
                }
                if (del.delete()) {
                    NotifyManager.newNotification(Vergence.TEXT.get("COMMANDS.THEME.MESSAGE.DELETED").replace("{theme}", parameters[1]));
                }
                if (parameters[1].equals(Vergence.THEME.getTheme().getName())) {
                    Vergence.THEME.unloadTheme();
                }
                break;

            default:
                sendUsage();
        }
    }

    @Override
    public String[] getAutocorrect(int count, List<String> separated) {
        if (count == 1) return new String[]{"save", "load", "list", "delete"};
        if (count == 2) {
            String cmd = separated.get(separated.size() - 2).toLowerCase();
            if ("load".equals(cmd) || "delete".equals(cmd)) {
                File[] fs = ConfigManager.THEMES_FOLDER.listFiles((d, n) -> n.endsWith(".json"));
                List<String> names = new ArrayList<>();
                if (fs != null) for (File f : fs) names.add(f.getName().replace(".json", ""));
                return names.toArray(new String[0]);
            }
        }
        return null;
    }
}