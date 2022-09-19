package survival.utilities.survivalutilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class DenyInteract implements Listener {
    public DenyInteract(SurvivalUtilities e) {
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("group.default")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You are not allowed to do this action! Please apply using the /apply command.");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("group.default")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You are not allowed to do this action! Please apply using the /apply command.");
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("group.default")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You are not allowed to do this action! Please apply using the /apply command.");
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if(event.getTarget() instanceof Player){
            Player player = ((Player) event.getTarget()).getPlayer();
            if(player.hasPermission("group.default")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("group.default")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You are not allowed to do this action! Please apply using the /apply command.");
        }
    }

    @EventHandler
    public void onPickup(PlayerAttemptPickupItemEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("group.default")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntityType() == EntityType.PLAYER){
            Player player = ((Player) event.getEntity()).getPlayer();
            if(player.hasPermission("group.default")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void preventDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager().getType() == EntityType.PLAYER){
            Player player = ((Player) event.getDamager()).getPlayer();
            if(player.hasPermission("group.default")) {
                event.setCancelled(true);
            }
        }
    }
}