package me.wesleynichols.survivalutilities.managers;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.util.ConfigUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

public class PlayerManager implements Listener {

    private static final LuckPerms luckPerms = LuckPermsProvider.get();
    public static FileConfiguration config;
    private static File file;
    private static Plugin plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        loadConfig();
        Player player = event.getPlayer();

        //  Greet new or returning players
        Bukkit.getScheduler().runTaskLater(SurvivalUtilities.getInstance(), () -> {
            if (player.hasPlayedBefore()) {
                player.sendActionBar(ConfigUtil.formatMessage(plugin.getConfig().getString("greet-returning"), player));
                ConfigUtil.playConfigSound(Objects.requireNonNull(plugin.getConfig().getConfigurationSection("returning-sound")), player);
            } else {
                ConfigUtil.sendFormattedMessage(plugin.getConfig().getStringList("welcome-message"), player);
                Bukkit.broadcast(ConfigUtil.formatMessage(plugin.getConfig().getString("welcome-global"), player));

                player.sendActionBar(ConfigUtil.formatMessage(plugin.getConfig().getString("greet-new"), player));
                ConfigUtil.playConfigSound(Objects.requireNonNull(plugin.getConfig().getConfigurationSection("new-sound")), player);
            }
        }, 120);

        //  Accept player if necessary
        if(playerStatus(player) && config.getInt(player.getUniqueId().toString()) == 0){
            playerAccept(player);
        }
    }

    public static boolean playerStatus(OfflinePlayer player) {
        loadConfig();
        return config.contains(player.getUniqueId().toString());
    }

    public static boolean playerAccept(OfflinePlayer player) {
        loadConfig();
        try {
            if (player.isOnline()) {
                luckPerms.getUserManager().modifyUser(player.getUniqueId(), u -> {
                    u.data().add(Node.builder("group.player").build());
                    u.data().remove(Node.builder("group.default").build());
                });
                config.set(player.getUniqueId().toString(), 1);
                Bukkit.getScheduler().runTaskLater(SurvivalUtilities.getInstance(),
                        () -> {
                            Bukkit.broadcast(ConfigUtil.formatMessage(plugin.getConfig().getString("accepted-global"), player.getPlayer()));
                            Objects.requireNonNull(player.getPlayer()).sendMessage(ConfigUtil.formatMessage(plugin.getConfig().getString("accepted-player"), player.getPlayer()));
                        }
                        , 40);
            } else {
                config.set(player.getUniqueId().toString(), 0);
            }
            config.save(file);
            return true;
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to accept user " + player.getName() + "!");
            return false;
        }
    }

    public static boolean playerUnaccept(OfflinePlayer player) {
        loadConfig();
        try {
            luckPerms.getUserManager().modifyUser(player.getUniqueId(), u -> u.data().clear());
            config.set(player.getUniqueId().toString(), null);
            config.save(file);
            return true;
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to un-accept user " + player.getName() + "!");
            return false;
        }
    }

    private static void loadConfig() {
        plugin = Bukkit.getServer().getPluginManager().getPlugin("SurvivalUtilities");
        file = new File(plugin.getDataFolder(), "player.yml");
        config = YamlConfiguration.loadConfiguration(file);
    }
}
