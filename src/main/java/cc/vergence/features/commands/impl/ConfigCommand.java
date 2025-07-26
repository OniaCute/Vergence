package cc.vergence.features.commands.impl;

import cc.vergence.Vergence;
import cc.vergence.features.commands.Command;
import cc.vergence.features.managers.client.ConfigManager;
import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.managers.ui.NotifyManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigCommand extends Command {

	public ConfigCommand() {
		super("config", Vergence.TEXT.get("COMMANDS.CONFIG.desc"), "[save|load|list] [name]");
	}

	@Override
	public void runCommand(String[] parameters) {
		if (parameters.length == 0) {
			sendUsage();
			return;
		}

		ConfigManager cm = Vergence.CONFIG;
		String cmd = parameters[0].toLowerCase();

		switch (cmd) {
			case "save":
				String saveName = parameters.length < 2 ? cm.currentConfigName : parameters[1];
				cm.save(new File(ConfigManager.CONFIG_FOLDER, saveName + ".vgc"));
				NotifyManager.newNotification(
						"§a" + Vergence.TEXT.get("COMMANDS.CONFIG.MESSAGE.SAVED").replace("{config}", saveName)
				);
				break;

			case "load":
				if (parameters.length < 2) {
					NotifyManager.newNotification(Vergence.TEXT.get("COMMANDS.CONFIG.MESSAGE.MISSING_NAME"));
					return;
				}
				File target = new File(ConfigManager.CONFIG_FOLDER, parameters[1] + ".vgc");
				if (!target.exists()) {
					NotifyManager.newNotification(Vergence.TEXT.get("COMMANDS.CONFIG.MESSAGE.NOT_FOUND").replace("{config}", parameters[1]));
					return;
				}
				cm.load(parameters[1]);
				NotifyManager.newNotification(Vergence.TEXT.get("COMMANDS.CONFIG.MESSAGE.LOADED").replace("{config}", parameters[1])
				);
				break;

			case "list":
				File[] files = ConfigManager.CONFIG_FOLDER.listFiles((d, n) -> n.endsWith(".vgc"));
				if (files == null || files.length == 0) {
					NotifyManager.newNotification(Vergence.TEXT.get("COMMANDS.CONFIG.MESSAGE.NO_CONFIGS"));
					return;
				}
				ArrayList<String> arr = new ArrayList<>();
				for (File f : files) {
					arr.add(f.getName().replace(".vgc", ""));
				}
				MessageManager.newMessage("Vergence", Vergence.TEXT.get("COMMANDS.CONFIG.MESSAGE.LIST_TITLE"));
				for (String name : arr) {
					MessageManager.newMessage("Vergence", "§e - " + name);
				}
				break;

			default:
				sendUsage();
		}
	}

	@Override
	public String[] getAutocorrect(int count, List<String> separated) {
		if (count == 1) return new String[]{"save", "load", "list"};
		if (count == 2 && "load".equalsIgnoreCase(separated.get(separated.size() - 2))) {
			File[] fs = ConfigManager.CONFIG_FOLDER.listFiles((d, n) -> n.endsWith(".vgc"));
			List<String> names = new ArrayList<>();
			if (fs != null) for (File f : fs) names.add(f.getName().replace(".vgc", ""));
			return names.toArray(new String[0]);
		}
		return null;
	}
}