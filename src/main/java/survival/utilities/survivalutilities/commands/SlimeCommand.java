package survival.utilities.survivalutilities.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import survival.utilities.survivalutilities.SurvivalUtilities;

import java.util.List;

public class SlimeCommand implements CommandExecutor {

    public static String chatPrefix = ChatColor.GOLD + "[" + ChatColor.YELLOW + "BeeBox" + ChatColor.GOLD + "]" + ChatColor.YELLOW + " > " + ChatColor.WHITE;
    public static String getCommand = "slime";

    /**
     Returns basic info such as map dimensions, spawn and shopping district locations, etc
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player && sender.hasPermission("survivalutil.slime")) {
            Player player = (Player) sender;
            Chunk chunk = player.getChunk();
            if (chunk.isSlimeChunk()) {
                sender.sendMessage(Component.text(chatPrefix + "This chunk is a slime chunk!"));
            } else {
                sender.sendMessage(Component.text(chatPrefix + "This chunk is not a slime chunk!"));
            }
            return true;
        }
        return false;
    }
}
