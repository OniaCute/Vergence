package cc.vergence.features.commands.impl;


import cc.vergence.Vergence;
import cc.vergence.features.commands.Command;
import cc.vergence.features.managers.feature.ModuleManager;
import cc.vergence.modules.Module;
import cc.vergence.features.managers.other.MessageManager;

import java.util.ArrayList;
import java.util.List;

public class BindCommand extends Command {

	public BindCommand() {
		super("bind", Vergence.TEXT.get("COMMANDS.Bind.desc"), "[module] [key]");
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
			MessageManager.newMessage("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.BIND.UNKNOWN_MODULE"));
			return;
		}
		if (parameters.length == 1) {
			MessageManager.newMessage("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.BIND.NO_KEY"));
			return;
		}
		String rkey = parameters[1];
		if (rkey == null) {
			MessageManager.newMessage("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.BIND.UNKNOWN_ERROR"));
			return;
		}
		if (module.setBindValue(rkey.toUpperCase())) {
			MessageManager.newMessage("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.BIND.BIND_OK"));
		}
	}

	@Override
	public String[] getAutocorrect(int count, List<String> seperated) {
		if (count == 1) {
			String input = seperated.get(seperated.size() - 1).toLowerCase();
			List<String> correct = new ArrayList<>();
			for (Module x : ModuleManager.modules) {
				if (input.equalsIgnoreCase(Vergence.PREFIX + "bind") || x.getName().toLowerCase().startsWith(input)) {
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
