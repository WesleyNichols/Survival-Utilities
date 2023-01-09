package survival.utilities.survivalutilities.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import survival.utilities.survivalutilities.SurvivalUtilities;
import survival.utilities.survivalutilities.config.CustomConfig;

import java.util.UUID;

public class OnPlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        FileConfiguration config = CustomConfig.get();

        //  Welcome message
        Bukkit.getScheduler().runTaskLater(SurvivalUtilities.getInstance(), () -> {
            if (player.hasPlayedBefore()) {
                player.sendActionBar(Component.text(ChatColor.GOLD + "Welcome back " + ChatColor.YELLOW + player.getName() + ChatColor.GOLD + "!"));
                player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.5F, 1.5F);
                return;
            }
            player.sendActionBar(Component.text(ChatColor.GOLD + "Welcome " + ChatColor.YELLOW + player.getName() + ChatColor.GOLD + "!"));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.5F, 0.69F);
        }, 120);

        //  Accept if default player who is accepted in config
        if(config.getString(uuid.toString()) != null && config.getInt(uuid.toString()) == 0 && player.hasPermission("group.default")){
            player.getInventory().clear();
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent add player");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent remove default");
            Bukkit.getScheduler().runTaskLater(SurvivalUtilities.getInstance(), () -> Bukkit.broadcast(Component.text(ChatColor.GREEN + player.getName() + " was accepted as a member!")), 60);
            config.set(player.getUniqueId().toString(), 1);
            CustomConfig.save();
        }
    }
}
