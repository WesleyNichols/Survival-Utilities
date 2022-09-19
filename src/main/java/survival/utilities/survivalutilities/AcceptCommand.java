package survival.utilities.survivalutilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class AcceptCommand implements CommandExecutor{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("SurvivalUtilities");
        Player player = (Player) sender;
        if (player.hasPermission("survivalutil.accept") && command.getName().equalsIgnoreCase("accept") && args.length == 1) {
            Player p = Bukkit.getPlayer(args[0]);

            if (p != null) {
                if (p.hasPermission("survivalutil.apply")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent remove default");
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent add player");
                    sender.sendMessage(ChatColor.GREEN + args[0] + " was accepted into the server.");
                }
            } else {
                if (plugin.getConfig().contains(args[0])) {
                    sender.sendMessage(ChatColor.RED + args[0] + " is already accepted!");
                    return true;
                }
                plugin.getConfig().set(args[0], 0);
                plugin.saveConfig();
                sender.sendMessage(ChatColor.RED + args[0] + " will be accepted into the server the next time they join.");
            }
            return true;
        }

        if(player.hasPermission("survivalutil.unaccept") && command.getName().equalsIgnoreCase("unaccept") && args.length == 1) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);

            if(p.hasPlayedBefore()) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent remove player");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent add default");
                sender.sendMessage(ChatColor.GREEN + args[0] + " was unaccepted from the server.");
            } else {
                if (!plugin.getConfig().contains(args[0])) {
                    sender.sendMessage(ChatColor.RED + args[0] + " is not currently accepted!");
                    return true;
                }
                plugin.getConfig().set(args[0], null);
                plugin.saveConfig();
                sender.sendMessage(ChatColor.RED + args[0] + " will no longer be accepted the next time they join.");
            }
            return true;
        }
        sender.sendMessage(ChatColor.RED + "You don't have permission to run this command or an error occurred!");
        return false;
    }
}
