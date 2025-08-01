package cc.vergence.features.commands.impl;

import cc.vergence.Vergence;
import cc.vergence.features.commands.Command;
import cc.vergence.features.managers.feature.ModuleManager;
import cc.vergence.features.managers.ui.NotifyManager;
import cc.vergence.modules.Module;
import cc.vergence.features.managers.other.MessageManager;

import java.util.ArrayList;
import java.util.List;

public class ToggleCommand extends Command {

	public ToggleCommand() {
		super("toggle", Vergence.TEXT.get("COMMANDS.Toggle.desc"), "[module]");
	}

	@Override
	public void runCommand(String[] parameters) {
		if (parameters.length == 0) {
			sendUsage();
			return;
		}
		String moduleName = parameters[0];
		Module module = Vergence.MODULE.getModuleByName(moduleName);
		if (module == null) {
			NotifyManager.newNotification("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.TOGGLE.UNKNOWN_MODULE"));
			return;
		}
		module.toggle();
	}

	@Override
	public String[] getAutocorrect(int count, List<String> seperated) {
		if (count == 1) {
			String input = seperated.get(seperated.size() - 1).toLowerCase();
			List<String> correct = new ArrayList<>();
			for (Module x : ModuleManager.modules) {
				if (input.equalsIgnoreCase(Vergence.PREFIX + "toggle") || x.getName().toLowerCase().startsWith(input)) {
					correct.add(x.getName());
				}
			}
			int numCmds = correct.size();
			String[] commands = new String[numCmds];

			int i = 0;
			for (String x : correct) {
				commands[i++] = x;
			}

			return commands;
		}
		return null;
	}
}
