package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class ToggleCommandCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!sender.hasPermission("survivalutilities.togglecommand")) {
            sender.sendMessage("You don't have permission.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("Usage: /togglecommand <command> <true|false>");
            return true;
        }

        String commandName = args[0].toLowerCase();
        boolean enable;

        try {
            enable = Boolean.parseBoolean(args[1]);
        } catch (Exception e) {
            sender.sendMessage("Second argument must be true or false.");
            return true;
        }

        FileConfiguration config = SurvivalUtilities.getInstance().getConfig();
        config.set("enabled-commands." + commandName, enable);
        SurvivalUtilities.getInstance().saveConfig();

        sender.sendMessage("Command '" + commandName + "' has been set to: " + enable);
        return true;
    }
}
