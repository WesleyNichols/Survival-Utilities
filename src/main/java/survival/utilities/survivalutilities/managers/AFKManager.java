package survival.utilities.survivalutilities.managers;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TablistFormatManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import survival.utilities.survivalutilities.SurvivalUtilities;

import java.util.HashMap;
import java.util.UUID;

public class AFKManager extends BukkitRunnable {

//    private final TabAPI tabAPI = TabAPI.getInstance();
//    private static final TablistFormatManager formatManager = TabAPI.getInstance().getTablistFormatManager();

    private static final long MovementThreshold = 3000L;    // TODO -> 600000L
    private static final HashMap<UUID, Long> players = new HashMap<>();

    public void run() {
        if (!SurvivalUtilities.getInstance().isEnabled()) { this.cancel(); }

        for (UUID uuid : players.keySet()) {
            Bukkit.broadcast(Component.text("cringe"));
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                players.remove(uuid);
                continue;
            }

            if (isAFK(player)) {
                Bukkit.broadcast(Component.text("im cringing"));
                player.sendActionBar(Component.text(ChatColor.RED + "You're marked CRINGE"));
                enableAFK(player);
            } else {
                Bukkit.broadcast(Component.text("giga cringe"));
                disableAFK(player);
            }
        }
    }

    public static void playerRemove(Player player) {
        players.remove(player.getUniqueId());
    }

    public static void playerActive(Player player) {
        players.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public static boolean isAFK(Player player) {
        return System.currentTimeMillis() - players.get(player.getUniqueId()) >= MovementThreshold;
    }

    private void enableAFK(Player player) {
//        formatManager.setPrefix(tabAPI.getPlayer(player.getUniqueId()), ChatColor.GRAY + "");
    }

    private void disableAFK(Player player) {
//        formatManager.resetPrefix(tabAPI.getPlayer(player.getUniqueId()));
    }

}
