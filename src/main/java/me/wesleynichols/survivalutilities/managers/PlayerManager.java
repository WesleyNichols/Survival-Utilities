package me.wesleynichols.survivalutilities.managers;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.config.CustomConfig;
import me.wesleynichols.survivalutilities.util.ConfigUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class PlayerManager implements Listener {

    private final SurvivalUtilities plugin;
    private final LuckPerms luckPerms;
    private final CustomConfig customConfig;
    private FileConfiguration config;

    private static final int PENDING = 0;
    private static final int ACCEPTED = 1;

    public PlayerManager(SurvivalUtilities plugin, CustomConfig customConfig) {
        this.plugin = plugin;
        this.customConfig = customConfig;
        this.config = customConfig.getConfig();
        this.luckPerms = LuckPermsProvider.get();
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void reload() {
        customConfig.reloadConfig();
        this.config = customConfig.getConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.hasPlayedBefore()) {
                player.sendActionBar(ConfigUtil.formatMessage(plugin.getConfig().getString("greet-returning"), player));

                var returningSound = plugin.getConfig().getConfigurationSection("returning-sound");
                if (returningSound != null) {
                    ConfigUtil.playConfigSound(returningSound, player);
                }
            } else {
                ConfigUtil.sendFormattedMessage(plugin.getConfig().getStringList("welcome-message"), player);
                Bukkit.broadcast(ConfigUtil.formatMessage(plugin.getConfig().getString("welcome-global"), player));

                player.sendActionBar(ConfigUtil.formatMessage(plugin.getConfig().getString("greet-new"), player));

                var newSound = plugin.getConfig().getConfigurationSection("new-sound");
                if (newSound != null) {
                    ConfigUtil.playConfigSound(newSound, player);
                }
            }
        }, 120L);

        // If accepted but not applied (they were offline)
        if (isPlayerPending(player)) {
            playerAccept(player);
        }
    }

    public boolean isPlayerPending(OfflinePlayer player) {
        if (!config.contains(player.getUniqueId().toString())) return false;

        List<?> data = config.getList(player.getUniqueId().toString());
        if (data == null || data.size() < 2) return false;

        Object statusObj = data.get(1);
        if (!(statusObj instanceof Integer)) return false;

        int status = (Integer) statusObj;
        return status == PENDING;
    }

    public boolean hasBeenAccepted(OfflinePlayer player) {
        if (!config.contains(player.getUniqueId().toString())) return false;

        List<?> data = config.getList(player.getUniqueId().toString());
        if (data == null || data.size() < 2) return false;

        Object statusObj = data.get(1);
        if (!(statusObj instanceof Integer)) return false;

        int status = (Integer) statusObj;
        return status == PENDING || status == ACCEPTED;
    }

    public boolean playerAccept(OfflinePlayer player) {
        try {
            int status = player.isOnline() ? ACCEPTED : PENDING;
            List<Object> entry = List.of(Objects.requireNonNull(player.getName()), status);

            if (player.isOnline()) {
                luckPerms.getUserManager().modifyUser(player.getUniqueId(), user -> {
                    user.data().add(Node.builder("group.player").build());
                    user.data().remove(Node.builder("group.default").build());
                });

                Bukkit.broadcast(SurvivalUtilities.getInstance().getPrefix()
                        .append(ConfigUtil.formatMessage(plugin.getConfig().getString("accepted-global"), player.getPlayer())));
                Objects.requireNonNull(player.getPlayer()).sendMessage(SurvivalUtilities.getInstance().getPrefix()
                        .append(ConfigUtil.formatMessage(plugin.getConfig().getString("accepted-player"), player.getPlayer())));
            }

            config.set(player.getUniqueId().toString(), entry);
            customConfig.saveConfig();
            plugin.getLogger().info("Accepted player " + player.getName());
            return true;
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to accept user " + player.getName(), e);
            return false;
        }
    }

    public boolean playerUnaccept(OfflinePlayer player) {
        try {
            luckPerms.getUserManager().modifyUser(player.getUniqueId(), user -> user.data().clear());
            config.set(player.getUniqueId().toString(), null);
            customConfig.saveConfig();
            plugin.getLogger().info("Unaccepted player " + player.getName());
            return true;
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to unaccept user " + player.getName(), e);
            return false;
        }
    }
}
