package survival.utilities.survivalutilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AcceptCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if(player.hasPermission("survivalutil.accept") && command.getName().equalsIgnoreCase("accept") && args.length == 1) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
            if(p.hasPlayedBefore()){
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + args[0] + " parent remove default");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + args[0] + " parent add player");
                sender.sendMessage(ChatColor.GREEN + args[0] + " was successfully accepted into the server.");
            } else {
                sender.sendMessage(ChatColor.RED + args[0] + " has never joined the server before! The username is case sensitive, make sure you spelled it correctly.");
            }
        }
        return false;
    }
}
