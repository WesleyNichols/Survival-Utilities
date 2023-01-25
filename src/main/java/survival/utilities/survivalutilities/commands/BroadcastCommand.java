package survival.utilities.survivalutilities.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import survival.utilities.survivalutilities.SurvivalUtilities;

import java.util.Arrays;
import java.util.List;

public class BroadcastCommand implements CommandExecutor {

    public static String chatPrefix = ChatColor.GOLD + "[" + ChatColor.YELLOW + "BeeBox" + ChatColor.GOLD + "]" + ChatColor.YELLOW + " > " + ChatColor.WHITE;
    public static String getCommand = "broadcast";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("survivalutil.broadcast")) {
            if (args.length == 0) return false;
            String message = String.join(" ", args);
            Bukkit.broadcast(Component.text(chatPrefix + ChatColor.translateAlternateColorCodes('&', message)));
        }
        return true;
    }
}
