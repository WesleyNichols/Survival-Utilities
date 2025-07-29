package me.wesleynichols.survivalutilities.managers;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.configs.ConfigUtil;
import me.wesleynichols.survivalutilities.configs.PlayerConfig;
import me.wesleynichols.survivalutilities.model.PlayerStatus;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;
import java.util.logging.Level;

public class PlayerManager implements Listener {

    private final SurvivalUtilities plugin;
    private final PlayerConfig playerConfig;
    private final LuckPerms luckPerms;

    public PlayerManager(SurvivalUtilities plugin, PlayerConfig playerConfig) {
        this.plugin = plugin;
        this.playerConfig = playerConfig;
        this.luckPerms = LuckPermsProvider.get();
    }

    public void reload() {
        playerConfig.reload();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.hasPlayedBefore()) {
                player.sendActionBar(ConfigUtil.formatMessage(plugin.getConfig().getString("greet-returning"), player));
                ConfigurationSection returningSound = plugin.getConfig().getConfigurationSection("returning-sound");
                if (returningSound != null) ConfigUtil.playConfigSound(returningSound, player);
            } else {
                ConfigUtil.sendFormattedMessage(plugin.getConfig().getStringList("welcome-message"), player);
                Bukkit.broadcast(ConfigUtil.formatMessage(plugin.getConfig().getString("welcome-global"), player));
                player.sendActionBar(ConfigUtil.formatMessage(plugin.getConfig().getString("greet-new"), player));
                ConfigurationSection newSound = plugin.getConfig().getConfigurationSection("new-sound");
                if (newSound != null) ConfigUtil.playConfigSound(newSound, player);
            }
        }, 120L);

        if (isPlayerPending(player)) {
            playerAccept(player);
        }
    }

    public boolean isPlayerPending(OfflinePlayer player) {
        return playerConfig.isPending(player.getUniqueId());
    }

    public boolean playerAccept(OfflinePlayer player) {
        try {
            UUID uuid = player.getUniqueId();
            PlayerStatus status = player.isOnline() ? PlayerStatus.ACCEPTED : PlayerStatus.PENDING;
            playerConfig.setStatus(uuid, status);

            if (player.isOnline()) {
                luckPerms.getUserManager().modifyUser(uuid, user -> {
                    user.data().add(Node.builder("group.player").build());
                    user.data().remove(Node.builder("group.default").build());
                });

                Player onlinePlayer = player.getPlayer();
                if (onlinePlayer != null) {
                    Bukkit.broadcast(SurvivalUtilities.getInstance().getPrefix().append(
                            ConfigUtil.formatMessage(plugin.getConfig().getString("accepted-global"), onlinePlayer)));
                    onlinePlayer.sendMessage(SurvivalUtilities.getInstance().getPrefix().append(
                            ConfigUtil.formatMessage(plugin.getConfig().getString("accepted-player"), onlinePlayer)));
                }
            }

            plugin.getLogger().info("Accepted player " + player.getName());
            return true;
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to accept user " + player.getName(), e);
            return false;
        }
    }

    public boolean playerUnaccept(OfflinePlayer player) {
        try {
            UUID uuid = player.getUniqueId();

            luckPerms.getUserManager().modifyUser(uuid, user -> {
                user.data().remove(Node.builder("group.player").build());
                user.data().add(Node.builder("group.default").build());
            });

            playerConfig.removePlayer(uuid);
            plugin.getLogger().info("Unaccepted player " + player.getName());
            return true;
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to unaccept user " + player.getName(), e);
            return false;
        }
    }
}
