package survival.utilities.survivalutilities.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.shanerx.mojang.Mojang;

import static survival.utilities.survivalutilities.managers.PlayerManager.*;

public class AcceptCommand implements CommandExecutor{

    public static String getCommand = "accept";
    private final Mojang mojang = new Mojang().connect();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(args.length == 1)) { return false; }

        if (sender.hasPermission("survivalutil.accept")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            try {
                mojang.getUUIDOfUsername(target.getName());
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + target.getName() + " is not a valid user!");
                return true;
            }

            //  region Accept
            if (label.equalsIgnoreCase("accept")) {
                if (playerStatus(target)) {
                    sender.sendMessage(ChatColor.RED + target.getName() + " is already accepted!");
                    return true;
                }

                if (playerAccept(target)) {
                    sender.sendMessage(ChatColor.GOLD + "You accepted " + ChatColor.YELLOW + target.getName() + ChatColor.GOLD + " to the server!");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.DARK_RED + "Failed to accept user " + target.getName() + "! If you believe this is an error please contact a developer.");
                    return false;
                }
            }
            // endregion

            //  region Unaccept
            if (label.equalsIgnoreCase("unaccept")) {
                if (!playerStatus(target)) {
                    sender.sendMessage(ChatColor.RED + target.getName() + " is already not accepted!");
                    return true;
                }

                if (playerUnaccept(target)) {
                    sender.sendMessage(ChatColor.GOLD + "You unaccepted " + ChatColor.YELLOW + target.getName() + ChatColor.GOLD + " from the server!");
                    return true;
                } else {
                    sender.sendMessage(ChatColor.DARK_RED + "Failed to unaccept user " + target.getName() + "! If you believe this is an error please contact a developer.");
                    return false;
                }
            }
            //  endregion
        }
        return false;
    }
}
