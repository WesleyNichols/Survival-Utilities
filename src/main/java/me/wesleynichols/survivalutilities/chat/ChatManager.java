package me.wesleynichols.survivalutilities.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.wesleynichols.survivalutilities.SurvivalUtilities;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatManager implements Listener {

    private final Map<UUID, ChatPlayer> chatPlayers = new HashMap<>();
    private final Map<UUID, Double> muteUntil = new HashMap<>();

    private double threshold;
    private double resetThreshold;
    private int muteTime;
    private int maxMessages;

    private final SurvivalUtilities plugin;

    public ChatManager(SurvivalUtilities plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        this.threshold = plugin.getConfig().getDouble("chat_spam_threshold");
        this.resetThreshold = plugin.getConfig().getDouble("chat_time_expiry");
        this.muteTime = plugin.getConfig().getInt("mute_time");
        this.maxMessages = plugin.getConfig().getInt("max_messages");
    }

    public boolean isMuted(UUID uuid) {
        return muteUntil.containsKey(uuid);
    }

    public void mutePlayer(UUID uuid, int strikes) {
        muteUntil.put(uuid, currentTime() + muteTime + strikes);
    }

    public void unmutePlayer(UUID uuid) {
        muteUntil.remove(uuid);
    }

    public double getTimeLeft(UUID uuid) {
        return muteUntil.getOrDefault(uuid, 0.0) - currentTime();
    }

    private double currentTime() {
        return System.currentTimeMillis() / 1000D;
    }

    public ChatPlayer getOrCreatePlayer(UUID uuid) {
        return chatPlayers.computeIfAbsent(uuid, id -> new ChatPlayer(id, this));
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ChatPlayer chatPlayer = getOrCreatePlayer(uuid);

        if (isMuted(uuid)) {
            double timeLeft = getTimeLeft(uuid);
            if (chatPlayer.handleMuteExpiration(timeLeft)) {
                player.sendMessage(plugin.getPrefix().append(Component.text("Chat disabled for " + ((int) timeLeft + 1) + "s!")));
                event.setCancelled(true);
                return;
            }
        }

        chatPlayer.recordMessage();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        getOrCreatePlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        chatPlayers.remove(uuid);
        muteUntil.remove(uuid);
    }

    @EventHandler
    public void onDisconnectSpam(PlayerKickEvent event) {
        if (event.getCause() == PlayerKickEvent.Cause.SPAM) {
            event.setCancelled(true);
        }
    }

    // Getters
    public double getThreshold() { return threshold; }
    public double getResetThreshold() { return resetThreshold; }
    public int getMaxMessages() { return maxMessages; }
}
