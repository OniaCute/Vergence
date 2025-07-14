package cc.vergence.features.commands.impl;


import cc.vergence.Vergence;
import cc.vergence.features.commands.Command;
import cc.vergence.features.managers.MessageManager;

import java.util.ArrayList;
import java.util.List;

public class FriendCommand extends Command {

	public FriendCommand() {
		super("friend", Vergence.TEXT.get("COMMANDS.Friend.desc"), "[reset/list] | [add/remove] [name]");
	}

	@Override
	public void runCommand(String[] parameters) {
		if (parameters.length == 0) {
			sendUsage();
			return;
		}
		if (parameters[0].equals("reset")) {
			Vergence.FRIEND.resetFriend();
			MessageManager.newMessage("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.FRIEND.RESET"));
			return;
		}
		if (parameters[0].equals("list")) {
			if (Vergence.FRIEND.friendList.isEmpty()) {
				MessageManager.newMessage("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.FRIEND.EMPTY"));
				return ;
			}
			MessageManager.newMessage("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.FRIEND.LIST_TITLE"));
			for (String name : Vergence.FRIEND.friendList) {
				MessageManager.newMessage("Vergence", "§e - " + name);
			}
			return;
		}

		if (parameters[0].equals("add")) {
			if (parameters.length == 2) {
				if (Vergence.ENEMY.isEnemy(parameters[1])) {
					MessageManager.newMessage("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.FRIEND.IS_ENEMY"));
					return;
				}
				Vergence.FRIEND.addFriend(parameters[1]);
				MessageManager.newMessage("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.FRIEND.ADD"));
				return;
			}
			sendUsage();
			return;
		} else if (parameters[0].equals("remove")) {
			if (parameters.length == 2) {
				Vergence.FRIEND.removeFriend(parameters[1]);
				MessageManager.newMessage("Vergence", Vergence.TEXT.get("COMMANDS.MESSAGE.FRIEND.REMOVE"));
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
				if (input.equalsIgnoreCase(Vergence.PREFIX + "friend") || x.toLowerCase().startsWith(input)) {
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
