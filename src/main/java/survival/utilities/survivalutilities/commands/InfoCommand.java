package survival.utilities.survivalutilities.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import survival.utilities.survivalutilities.SurvivalUtilities;

import java.util.List;

public class InfoCommand implements CommandExecutor {

    public static String getCommand = "info";

    /**
     Returns basic info such as map dimensions, spawn and shopping district locations, main nether portal coordinates, what season it is (the first map is season 1)
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            List<String> infoText = SurvivalUtilities.getInstance().getConfig().getStringList("info");
            for (String line : infoText) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
            }
        }
        return true;
    }
}
