package cc.vergence.features.commands.impl;


import cc.vergence.Vergence;
import cc.vergence.features.commands.Command;
import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.managers.ui.NotifyManager;

import java.util.ArrayList;
import java.util.List;

public class EnemyCommand extends Command {

	public EnemyCommand() {
		super("enemy", Vergence.TEXT.get("COMMANDS.Enemy.desc"), "[reset/list] | [add/remove] [name]");
	}

	@Override
	public void runCommand(String[] parameters) {
		if (parameters.length == 0) {
			sendUsage();
			return;
		}
		if (parameters[0].equals("reset")) {
			Vergence.ENEMY.resetEnemy();
			NotifyManager.newNotification("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.ENEMY.RESET"));
			return;
		}
		if (parameters[0].equals("list")) {
			if (Vergence.ENEMY.enemyList.isEmpty()) {
				NotifyManager.newNotification("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.ENEMY.EMPTY"));
				return ;
			}
			MessageManager.newMessage("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.ENEMY.LIST_TITLE"));
			for (String name : Vergence.ENEMY.enemyList) {
				MessageManager.newMessage("Vergence", "§e - " + name);
			}
			return;
		}

		if (parameters[0].equals("add")) {
			if (parameters.length == 2) {
				if (Vergence.FRIEND.isFriend(parameters[1])) {
					NotifyManager.newNotification("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.ENEMY.IS_FRIEND"));
					return;
				}
				Vergence.ENEMY.addEnemy(parameters[1]);
				NotifyManager.newNotification("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.ENEMY.ADD"));
				return;
			}
			sendUsage();
			return;
		} else if (parameters[0].equals("remove")) {
			if (parameters.length == 2) {
				Vergence.ENEMY.removeEnemy(parameters[1]);
				NotifyManager.newNotification("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.ENEMY.REMOVE"));
				return;
			}
			sendUsage();
			return;
		}

		sendUsage();
	}

	@Override
	public String[] getAutocorrect(int count, List<String> seperated) {
		if (count == 1) {
			String input = seperated.get(seperated.size() - 1).toLowerCase();
			List<String> correct = new ArrayList<>();
			List<String> list = List.of("add", "remove", "list", "reset");
			for (String x : list) {
				if (input.equalsIgnoreCase(Vergence.PREFIX + "enemy") || x.toLowerCase().startsWith(input)) {
					correct.add(x);
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
