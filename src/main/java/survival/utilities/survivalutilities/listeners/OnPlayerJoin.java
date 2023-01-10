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

import static survival.utilities.survivalutilities.managers.PlayerManager.*;

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

        //  Accept player if needed
        if(playerStatus(player) && config.getInt(uuid.toString()) == 0){
            playerAccept(player);
        }
    }
}
