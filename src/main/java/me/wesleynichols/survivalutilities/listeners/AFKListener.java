package me.wesleynichols.survivalutilities.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.wesleynichols.survivalutilities.managers.AFKManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AFKListener implements Listener {

    private final AFKManager afkManager;

    public AFKListener(AFKManager afkManager) {
        this.afkManager = afkManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("group.default")) {
            afkManager.recordActivity(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!event.getPlayer().hasPermission("group.default")) {
            afkManager.removePlayer(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.getPlayer().hasPermission("group.default")) {
            Location from = event.getFrom();
            Location to = event.getTo();

            boolean movedBlocks = from.getBlockX() != to.getBlockX()
                    || from.getBlockY() != to.getBlockY()
                    || from.getBlockZ() != to.getBlockZ();

            boolean rotated = from.getYaw() != to.getYaw()
                    || from.getPitch() != to.getPitch();

            if (movedBlocks || rotated) {
                afkManager.recordActivity(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        if (!event.getPlayer().hasPermission("group.default")) {
            afkManager.recordActivity(event.getPlayer());
        }
    }
}
