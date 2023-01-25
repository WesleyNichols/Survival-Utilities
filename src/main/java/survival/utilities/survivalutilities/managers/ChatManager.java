package survival.utilities.survivalutilities.managers;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import survival.utilities.survivalutilities.SurvivalUtilities;

import java.util.*;

public class ChatManager implements Listener {

    public static String chatPrefix = ChatColor.GOLD + "[" + ChatColor.YELLOW + "BeeBox" + ChatColor.GOLD + "]" + ChatColor.YELLOW + " > " + ChatColor.WHITE;
    private static final HashMap<UUID, List<Long>> chatTimes = new HashMap<>();
    private static final HashMap<UUID, Boolean> preventChat = new HashMap<>();
    public static double threshold;
    public static double resetArrThreshold;
    public static int muteTime;
    public static int maxMessages;

    public static void initTimes(UUID player) {
        chatTimes.put(player, new ArrayList<>());
        preventChat.put(player, false);
    }

    public static void initConfigVars() {
        threshold = SurvivalUtilities.getInstance().getConfig().getDouble("chat_spam_threshold");
        resetArrThreshold = SurvivalUtilities.getInstance().getConfig().getDouble("chat_time_expiry");
        muteTime = SurvivalUtilities.getInstance().getConfig().getInt("mute_time");
        maxMessages = SurvivalUtilities.getInstance().getConfig().getInt("max_messages");
    }

    public void checkThreshhold(List<Long> timeArr, UUID playerUUID) {
        double sumDiff = 0;
        for (int i = 0; i < maxMessages - 1; i++) {
            sumDiff += (timeArr.get(i + 1) - timeArr.get(i))/1000D;
        }
        if (sumDiff/4D < threshold) {
            preventChat.put(playerUUID, true);
        } else {
            chatTimes.put(playerUUID, new ArrayList<>());
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        List<Long> timeArr = chatTimes.get(playerUUID);

        if (preventChat.get(playerUUID)) {
            double timeLeft = muteTime + (timeArr.get(maxMessages - 1) - System.currentTimeMillis())/1000D;
            if(timeLeft <= 0) {
                preventChat.put(playerUUID, false);
                timeArr = new ArrayList<>();
            } else {
                player.sendMessage(Component.text(chatPrefix + "You can chat in " + (int)timeLeft + "s!"));
                event.setCancelled(true);
                return;
            }
        }

        int size = timeArr.size();
        if (size < maxMessages) {
            if (size > 0 && (System.currentTimeMillis() - timeArr.get(size - 1))/1000D > resetArrThreshold) {
                timeArr = new ArrayList<>();
            }
            timeArr.add(System.currentTimeMillis());
            chatTimes.put(playerUUID, timeArr);
            if (size == maxMessages - 1) {
                checkThreshhold(timeArr, playerUUID);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID player = event.getPlayer().getUniqueId();
        chatTimes.put(player, new ArrayList<>());
        preventChat.put(player, false);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID player = event.getPlayer().getUniqueId();
        chatTimes.remove(player);
        preventChat.remove(player);
    }
}
