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
            Location from = event.getFrom();
            Location to = event.getTo();

            boolean positionChanged = from.getBlockX() != to.getBlockX()
                    || from.getBlockY() != to.getBlockY()
                    || from.getBlockZ() != to.getBlockZ();

            boolean directionChanged = from.getYaw() != to.getYaw()
                    || from.getPitch() != to.getPitch();

            if (positionChanged || directionChanged) {
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
