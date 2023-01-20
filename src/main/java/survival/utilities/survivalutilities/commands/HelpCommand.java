package survival.utilities.survivalutilities.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import survival.utilities.survivalutilities.SurvivalUtilities;

import java.util.List;

public class HelpCommand implements CommandExecutor {

    public static String getCommand = "help";

    /**
     Displays helpful commands
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            List<String> helpText = SurvivalUtilities.getInstance().getConfig().getStringList("help");
            for (String line : helpText) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
            }
        }
        return true;
    }
}
