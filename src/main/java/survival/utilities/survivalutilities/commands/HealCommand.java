package survival.utilities.survivalutilities.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HealCommand implements CommandExecutor {

    public static String getCommand = "heal";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("heal") && sender instanceof Player player) {
            if (args.length > 0) {
                if (Bukkit.getOnlinePlayers().stream().anyMatch(e -> e.getName().equalsIgnoreCase(args[0]))) {
                    Player target = Bukkit.getPlayer(args[0]);
                    assert target != null;
                    target.setHealth(20);
                    target.setFoodLevel(20);
                    player.sendMessage(Component.text(ChatColor.GREEN + "Healed " + target.getName()));
                    if (target != player) { target.sendMessage(Component.text(ChatColor.GREEN + "You were healed by " + player.getName())); }
                } else {
                    player.sendMessage(Component.text(ChatColor.DARK_GREEN + args[0] + " is not a valid target!"));
                    return true;
                }
            } else {
                player.setHealth(20);
                player.setFoodLevel(20);
                player.sendMessage(Component.text(ChatColor.GREEN + "Healed " + player.getName()));
            }
            return true;
        }
        return false;
    }

}
