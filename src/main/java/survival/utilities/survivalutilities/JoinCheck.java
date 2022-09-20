package survival.utilities.survivalutilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class JoinCheck implements Listener {
    public JoinCheck(SurvivalUtilities e) {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("SurvivalUtilities");

        if (player.hasPlayedBefore()) {
            Bukkit.getScheduler().runTaskLater(p, () -> {
                player.sendActionBar(Component.text(ChatColor.GOLD + "Welcome back " + ChatColor.YELLOW + player.getName() + ChatColor.GOLD + "!"));
                player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.5F, 1.5F);
            }, 100);
        } else {
            Bukkit.getScheduler().runTaskLater(p, () -> {
                player.sendActionBar(Component.text(ChatColor.GOLD + "Welcome " + ChatColor.YELLOW + player.getName() + ChatColor.GOLD + "!"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.5F, 0.69F);
            }, 100);
        }

        if(p.getConfig().getString(uuid.toString()) != null && p.getConfig().getInt(uuid.toString()) == 0 && player.hasPermission("group.default")){
            player.getInventory().clear();

            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent add player");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent remove default");

            Bukkit.getScheduler().runTaskLater(p, () -> { Bukkit.broadcast(Component.text(ChatColor.GREEN + player.getName() + " was accepted as a member!")); }, 60);

            p.getConfig().set(player.getUniqueId().toString(), 1);
            p.saveConfig();
        }
    }
}
