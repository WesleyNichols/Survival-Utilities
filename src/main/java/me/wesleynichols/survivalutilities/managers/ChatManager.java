package me.wesleynichols.survivalutilities.managers;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.util.ChatPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class ChatManager implements Listener {

    public static final HashMap<UUID, ChatPlayer> chatPlayers = new HashMap<>();
    public static final HashMap<UUID, Double> preventChat = new HashMap<>();
    public static double threshold;
    public static double resetArrThreshold;
    public static int muteTime;
    public static int maxMessages;

    public static void initChatManager(UUID player) {
        chatPlayers.put(player, new ChatPlayer(player));
    }

    public static void initConfigVars() {
        threshold = SurvivalUtilities.getInstance().getConfig().getDouble("chat_spam_threshold");
        resetArrThreshold = SurvivalUtilities.getInstance().getConfig().getDouble("chat_time_expiry");
        muteTime = SurvivalUtilities.getInstance().getConfig().getInt("mute_time");
        maxMessages = SurvivalUtilities.getInstance().getConfig().getInt("max_messages");
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        ChatPlayer chatPlayer = chatPlayers.get(playerUUID);

        if (preventChat.containsKey(playerUUID)) {
            double timeLeft = preventChat.get(playerUUID) - System.currentTimeMillis()/1000D;
            if (chatPlayer.checkMuteTime(timeLeft)) {
                player.sendMessage(SurvivalUtilities.getInstance().getPrefix().append(Component.text("Chat disabled for " + ((int)timeLeft + 1) + "s!")));
                event.setCancelled(true);
                return;
            }
        }

        chatPlayer.checkMessage();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID player = event.getPlayer().getUniqueId();
        chatPlayers.put(player, new ChatPlayer(player));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID player = event.getPlayer().getUniqueId();
        chatPlayers.remove(player);
    }

    @EventHandler
    public void onDisconnectSpam(PlayerKickEvent event) {
        if(event.getCause().equals(PlayerKickEvent.Cause.SPAM)) {
            event.setCancelled(true);
        }
    }
}
