package cc.vergence.features.managers.client;

import cc.vergence.Vergence;
import cc.vergence.features.commands.Command;
import cc.vergence.features.commands.impl.*;
import cc.vergence.features.managers.other.MessageManager;
import cc.vergence.features.managers.ui.NotifyManager;

import java.util.HashMap;

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();

    public CommandManager() {
        registerCommands(
                new AimCommand(),
                new BindCommand(),
                new ToggleCommand(),
                new PrefixCommand(),
                new FriendCommand(),
                new EnemyCommand(),
                new ConfigCommand()
        );
    }

    private void registerCommands(Command ... cmds) {
        for (Command cmd : cmds) {
            commands.put(cmd.getName(), cmd);
        }
    }

    public Command getCommandBySyntax(String string) {
        return this.commands.get(string);
    }

    public HashMap<String, Command> getCommands() {
        return this.commands;
    }

    public int getNumOfCommands() {
        return this.commands.size();
    }

    public void command(String[] commandIn) {
        Command command = commands.get(commandIn[0].substring(Vergence.PREFIX.length()));
        if (command == null)
            NotifyManager.newNotification(Vergence.TEXT.get("COMMANDS.MESSAGE.INVALID_COMMAND"));
        else {
            String[] parameterList = new String[commandIn.length - 1];
            for (int i = 1; i < commandIn.length; i++) {
                parameterList[i - 1] = commandIn[i];
            }
            if (parameterList.length == 1 && parameterList[0].equals("help")) {
                command.sendUsage();
                return;
            }
            command.runCommand(parameterList);
        }
    }
}
