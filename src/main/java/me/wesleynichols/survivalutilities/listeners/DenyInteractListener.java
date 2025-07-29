package me.wesleynichols.survivalutilities.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;

public class DenyInteractListener implements Listener {

    private static final String PERMISSION_NODE = "group.default";

    private static boolean shouldDeny(Player player, boolean notify) {
        if (player.hasPermission(PERMISSION_NODE)) {
            if (notify) {
                player.sendActionBar(Component.text("Use /apply to get started!", NamedTextColor.RED));
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (shouldDeny(event.getPlayer(), true)) event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player && shouldDeny(player, false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player player && shouldDeny(player, false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player && shouldDeny(player, false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (shouldDeny(event.getPlayer(), true)) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (shouldDeny(event.getPlayer(), true)) event.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        if (shouldDeny(event.getPlayer(), false)) event.setCancelled(true);
    }

    @EventHandler
    public void onArrowPickup(PlayerPickupArrowEvent event) {
        if (shouldDeny(event.getPlayer(), false)) event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (shouldDeny(event.getPlayer(), true)) event.setCancelled(true);
    }

    @EventHandler
    public void preventDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player && shouldDeny(player, true)) {
            event.setCancelled(true);
        }
    }
}
