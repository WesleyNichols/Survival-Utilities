package me.wesleynichols.survivalutilities.managers;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
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
        //  Display welcome message
        Bukkit.getScheduler().runTaskLater(SurvivalUtilities.getInstance(), () -> {
            if (player.hasPlayedBefore()) {
                player.sendActionBar(Component.text("Welcome back ", NamedTextColor.GOLD).append(Component.text(player.getName(), NamedTextColor.YELLOW).append(Component.text("!", NamedTextColor.GOLD))));
                player.playSound(player.getLocation(), Sound.BLOCK_BEEHIVE_ENTER, 1.5F, 1.0F);
            } else {
                player.sendActionBar(Component.text("Welcome, ", NamedTextColor.GOLD).append(Component.text(player.getName(), NamedTextColor.YELLOW).append(Component.text("!", NamedTextColor.GOLD))));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.5F, 0.69F);
                player.sendMessage(Component.text("Welcome to Bee Box MC!").color(NamedTextColor.GOLD).append(Component.text(" Our server runs on a greylist system, you won't able to interact with the world until you become a member. Use /apply to get started!").color(NamedTextColor.YELLOW)));
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
                            Bukkit.broadcast(Component.text(player.getName() + " was accepted as a member!", NamedTextColor.GREEN));
                            Objects.requireNonNull(player.getPlayer()).sendMessage(Component.text("Congratulations!").color(NamedTextColor.GOLD).append(Component.text(" You've been accepted as a member - be sure to read /rules, /information, and visit /map to learn the ins and outs of Bee Box MC!").color(NamedTextColor.YELLOW)));
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
