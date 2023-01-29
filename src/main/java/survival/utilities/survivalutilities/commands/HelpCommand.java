package survival.utilities.survivalutilities.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import survival.utilities.survivalutilities.managers.PageManager;


public class HelpCommand implements CommandExecutor {

    public static String chatPrefix = ChatColor.GOLD + "[" + ChatColor.YELLOW + "BeeBox" + ChatColor.GOLD + "]" + ChatColor.YELLOW + " > " + ChatColor.WHITE;
    public static String getCommand = "help";

    /**
     Displays helpful commands
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            int pageNum = 1;
            try {
                pageNum = args.length > 0 ? Integer.parseInt(args[0]) : 1;
            } catch (NumberFormatException e) {
                sender.sendMessage(chatPrefix + "'" + args[0] + "' is not a number!");
            }
            for (Component line : PageManager.getPage(pageNum, getCommand)) {
                sender.sendMessage(line);
            }
        }
        return true;
    }
}
