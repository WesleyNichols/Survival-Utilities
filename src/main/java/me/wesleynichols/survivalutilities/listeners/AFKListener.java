package me.wesleynichols.survivalutilities.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.wesleynichols.survivalutilities.managers.AFKManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class AFKListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("group.default"))
            AFKManager.playerActive(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!event.getPlayer().hasPermission("group.default"))
            AFKManager.playerRemove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.getPlayer().hasPermission("group.default")) {
            Location moveFrom = event.getFrom();
            Location moveTo = event.getTo();
            if (moveFrom.getBlockX() != moveTo.getBlockX() || moveFrom.getBlockY() != moveTo.getBlockY() || moveFrom.getBlockZ() != moveTo.getBlockZ()) {
                AFKManager.playerActive(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        if (!event.getPlayer().hasPermission("group.default"))
            AFKManager.playerActive(event.getPlayer());
    }

}
