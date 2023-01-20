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

public class ReloadCommand implements CommandExecutor {

    public static String getCommand = "reload";

    /**
     Reloads the config of the plugin
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("survivalutil.reload")) {
            if (sender instanceof Player player) {
                player.sendMessage(Component.text(runReload()));
            } else {
                Bukkit.getServer().getConsoleSender().sendMessage(runReload());
            }
            return true;
        }
        return false;
    }

    public String runReload() {
        try {
            SurvivalUtilities.getInstance().reloadConfigs();
        } catch (Exception e) {
            e.printStackTrace();
            return ChatColor.RED + "[SurvivalUtilities] An error occurred while trying to reload configs.";
        }
        return ChatColor.GREEN + "[SurvivalUtilities] Configs reloaded!";
    }

}
