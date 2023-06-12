package me.wesleynichols.survivalutilities.managers;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.config.CustomConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.logging.Level;

public class PlayerManager implements Listener {

    private static final LuckPerms luckPerms = LuckPermsProvider.get();
    public static FileConfiguration config;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        //  Display welcome message
        Bukkit.getScheduler().runTaskLater(SurvivalUtilities.getInstance(), () -> {
            if (player.hasPlayedBefore()) {
                player.sendActionBar(Component.text("Welcome back ", NamedTextColor.GOLD).append(Component.text(player.getName(), NamedTextColor.YELLOW).append(Component.text("!", NamedTextColor.GOLD))));
                player.playSound(player.getLocation(), Sound.BLOCK_BEEHIVE_ENTER, 1.5F, 1.0F);
            } else {
                player.sendActionBar(Component.text("Welcome, ", NamedTextColor.GOLD).append(Component.text(player.getName(), NamedTextColor.YELLOW).append(Component.text("!", NamedTextColor.GOLD))));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.5F, 0.69F);
            }
        }, 120);

        //  Accept player if necessary
        if(playerStatus(player) && config.getInt(player.getUniqueId().toString()) == 0){
            playerAccept(player);
        }
    }

    public static boolean playerStatus(OfflinePlayer player) {
        return config.contains(player.getUniqueId().toString());
    }

    public static boolean playerAccept(OfflinePlayer player) {
        try {
            CustomConfig.load("player.yml");
            if (player.isOnline()) {
                luckPerms.getUserManager().modifyUser(player.getUniqueId(), u -> {
                    u.data().add(Node.builder("group.player").build());
                    u.data().remove(Node.builder("group.default").build());
                });
                config.set(player.getUniqueId().toString(), 1);
                Bukkit.getScheduler().runTaskLater(SurvivalUtilities.getInstance(),
                        () -> Bukkit.broadcast(Component.text(player.getName() + " was accepted as a member!", NamedTextColor.GREEN)), 40);
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
            CustomConfig.load("player.yml");
            luckPerms.getUserManager().modifyUser(player.getUniqueId(), u -> u.data().clear());
            config.set(player.getUniqueId().toString(), null);
            CustomConfig.save();
            return true;
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to un-accept user " + player.getName() + "!");
            return false;
        }
    }
}
