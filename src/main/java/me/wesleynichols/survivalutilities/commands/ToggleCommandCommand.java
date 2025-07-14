package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.structures.BaseCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ToggleCommandCommand extends BaseCommand {

    @Override
    protected boolean executeCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        SurvivalUtilities plugin = SurvivalUtilities.getInstance();

        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getPrefix()
                    .append(Component.text("Only players can use this command.", NamedTextColor.RED)));
            return true;
        }

        if (args.length != 2) return false;

        String commandName = args[0].trim().toLowerCase();
        String boolArg = args[1].trim().toLowerCase();

        if (!boolArg.equals("true") && !boolArg.equals("false")) {
            sender.sendMessage(plugin.getPrefix()
                    .append(Component.text("Second argument must be 'true' or 'false'.", NamedTextColor.RED)));
            return true;
        }

        boolean enable = Boolean.parseBoolean(boolArg);

        FileConfiguration config = plugin.getConfig();
        config.set("enabled-commands." + commandName, enable);
        plugin.saveConfig();

        sender.sendMessage(plugin.getPrefix()
                .append(Component.text("Command '" + commandName + "' has been set to: " + enable, NamedTextColor.GREEN)));
        return true;
    }
}
