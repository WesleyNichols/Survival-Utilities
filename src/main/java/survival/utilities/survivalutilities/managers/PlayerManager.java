package survival.utilities.survivalutilities.managers;

import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import survival.utilities.survivalutilities.SurvivalUtilities;
import survival.utilities.survivalutilities.config.CustomConfig;

import java.util.UUID;
import java.util.logging.Level;

public class PlayerManager {

    private static final LuckPerms luckPerms = LuckPermsProvider.get();
    private static final FileConfiguration config = CustomConfig.get();

    public static boolean playerStatus(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        return config.contains(uuid.toString());
    }

    public static boolean playerAccept(OfflinePlayer player) {
        try {
            if (player.isOnline()) {
                luckPerms.getUserManager().modifyUser(player.getUniqueId(), u -> {
                    u.data().add(Node.builder("group.player").build());
                    u.data().remove(Node.builder("group.default").build());
                });
                config.set(player.getUniqueId().toString(), 1);
                Bukkit.getScheduler().runTaskLater(SurvivalUtilities.getInstance(),
                        () -> Bukkit.broadcast(Component.text(ChatColor.GREEN + player.getName() + " was accepted as a member!")), 60);
            } else {
                config.set(player.getUniqueId().toString(), 0);
            }
            CustomConfig.save();
            return true;
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to accept user " + player.getName() + "!");
            return false;
        }
    }

    public static boolean playerUnaccept(OfflinePlayer player) {
        try {
            luckPerms.getUserManager().modifyUser(player.getUniqueId(), u -> u.data().clear());
            config.set(player.getUniqueId().toString(), null);
            CustomConfig.save();
            return true;
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to unaccept user " + player.getName() + "!");
            return false;
        }
    }
}
