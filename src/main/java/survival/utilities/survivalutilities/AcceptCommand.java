package survival.utilities.survivalutilities;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.shanerx.mojang.Mojang;

import java.util.Objects;
import java.util.UUID;

public class AcceptCommand implements CommandExecutor{

    private final Mojang api = new Mojang().connect();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("SurvivalUtilities");
        assert plugin != null;

        if (!(args.length == 1)) { return false; }

        OfflinePlayer user = Bukkit.getOfflinePlayer(args[0]);
        UUID uuid = user.getUniqueId();

        if (player.hasPermission("survivalutil.accept")) {
            try {
                api.getUUIDOfUsername(user.getName());
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + user.getName() + " is not a valid user!");
                return true;
            }

            if (command.getName().equalsIgnoreCase("accept")) {
                if (plugin.getConfig().contains(uuid.toString())) {
                    sender.sendMessage(ChatColor.RED + user.getName() + " is already accepted!");
                    return true;
                }

                if (user.hasPlayedBefore()) {
                    if (user.isOnline()) {
                        Objects.requireNonNull(user.getPlayer()).getInventory().clear();
                        Bukkit.broadcast(Component.text(ChatColor.GREEN + user.getName() + " was accepted as a member!"));
                    }

                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + user.getName() + " parent add player");
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + user.getName() + " parent remove default");
                    sender.sendMessage(ChatColor.GOLD + "You accepted " + ChatColor.YELLOW + user.getName() + ChatColor.GOLD + " to the server!");
                    plugin.getConfig().set(uuid.toString(), 1);
                } else {
                    sender.sendMessage(ChatColor.RED + user.getName() + " will be accepted the next time they join.");
                    plugin.getConfig().set(uuid.toString(), 0);
                }
                plugin.saveConfig();
                return true;

            } else if (command.getName().equalsIgnoreCase("unaccept")) {
                if (!plugin.getConfig().contains(uuid.toString())) {
                    sender.sendMessage(ChatColor.RED + user.getName() + " is not currently accepted!");
                    return true;
                }

                if(user.hasPlayedBefore()) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + user.getName() + " parent add default");
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + user.getName() + " parent remove player");
                    sender.sendMessage(ChatColor.GOLD + "You unaccepted " + ChatColor.YELLOW + user.getName() + ChatColor.GOLD + " from the server!");
                } else {
                    sender.sendMessage(ChatColor.RED + user.getName() + " will no longer be accepted the next time they join.");
                }

                plugin.getConfig().set(uuid.toString(), null);
                plugin.saveConfig();
                return true;
            }
        }

        sender.sendMessage(ChatColor.RED + "You don't have permission to run this command or an error occurred!");
        return false;
    }
}
