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
        if(player.hasPermission("survivalutil.accept") && command.getName().equalsIgnoreCase("accept") && args.length == 1) {
            Player p = Bukkit.getPlayer(args[0]);
            if(p != null){
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent remove default");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " parent add player");
                sender.sendMessage(ChatColor.GREEN + args[0] + " was successfully accepted into the server.");
            } else {
                plugin.getConfig().set(args[0], 1);
                plugin.saveConfig();
                sender.sendMessage(ChatColor.RED + args[0] + " will be accepted into the server the next time they join.");
            }
        }
        return false;
    }
}
