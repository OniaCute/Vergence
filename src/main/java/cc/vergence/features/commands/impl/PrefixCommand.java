package cc.vergence.features.commands.impl;

import cc.vergence.Vergence;
import cc.vergence.features.commands.Command;
import cc.vergence.features.managers.other.MessageManager;

import java.util.List;

public class PrefixCommand extends Command {

	public PrefixCommand() {
		super("prefix", Vergence.TEXT.get("COMMANDS.Prefix.desc"), "[prefix]");
	}

	@Override
	public void runCommand(String[] parameters) {
		if (parameters.length == 0) {
			sendUsage();
			return;
		}
		if (parameters[0].startsWith("/")) {
			MessageManager.newMessage("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.PREFIX.NO_PREFIX"));
			return;
		}
		Vergence.PREFIX = parameters[0];
		MessageManager.newMessage("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.PREFIX.OK") + " \"" + parameters[0] + "\"");
	}

	@Override
	public String[] getAutocorrect(int count, List<String> seperated) {
		return null;
	}
}
