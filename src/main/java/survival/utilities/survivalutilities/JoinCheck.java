package survival.utilities.survivalutilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class JoinCheck implements Listener {
    public JoinCheck(SurvivalUtilities e) {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
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

        if(p.getConfig().getString(name) != null && p.getConfig().getInt(name) == 1 && player.hasPermission("group.default")){
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + name + " parent add player");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + name + " parent remove default");
            Bukkit.broadcast(Component.text(p.getName() + " is now a member!").color(NamedTextColor.GRAY));
            player.getInventory().clear();
            p.getConfig().set(name, null);
            p.saveConfig();
        }
    }
}
