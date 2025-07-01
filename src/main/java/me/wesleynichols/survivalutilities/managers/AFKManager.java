package me.wesleynichols.survivalutilities.managers;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.tablist.TabListFormatManager;
import me.wesleynichols.survivalutilities.SurvivalUtilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class AFKManager extends BukkitRunnable {

    private final TabAPI tabAPI = TabAPI.getInstance();
    private static final TabListFormatManager formatManager = TabAPI.getInstance().getTabListFormatManager();
    private static final long MovementThreshold = 600_000L;  // 10 minutes

    private static final HashMap<UUID, Long> players = new HashMap<>();
    private static final HashMap<UUID, Boolean> afkStatus = new HashMap<>();

    public void run() {
        if (!SurvivalUtilities.getInstance().isEnabled()) {
            this.cancel();
        }

        for (UUID uuid : new HashMap<>(players).keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || player.hasPermission("group.default")) {
                players.remove(uuid);
                afkStatus.remove(uuid);
                continue;
            }

            long lastActive = players.getOrDefault(uuid, System.currentTimeMillis());
            boolean isAFK = System.currentTimeMillis() - lastActive >= MovementThreshold;
            boolean wasAFK = afkStatus.getOrDefault(uuid, false);

            if (isAFK && !wasAFK) {
                afkStatus.put(uuid, true);
                player.sendActionBar(Component.text("You're now AFK", NamedTextColor.GRAY));

                //  Interact with TAB to set AFK prefix
                TabPlayer tabPlayer = tabAPI.getPlayer(uuid);
                if (tabPlayer != null && formatManager != null) {
                    formatManager.setPrefix(tabPlayer, "§7[AFK] ");
                }

            } else if (!isAFK && wasAFK) {
                afkStatus.put(uuid, false);
                player.sendActionBar(Component.text("You're no longer AFK", NamedTextColor.GRAY));

                //  Remove AFK prefix
                TabPlayer tabPlayer = tabAPI.getPlayer(uuid);
                if (tabPlayer != null && formatManager != null) {
                    formatManager.setPrefix(tabPlayer, null);
                }
            } else if (isAFK) {
                player.sendActionBar(Component.text("You're currently AFK", NamedTextColor.GRAY));
            }
        }
    }

    public static void playerRemove(Player player) {
        UUID uuid = player.getUniqueId();
        players.remove(uuid);
        afkStatus.remove(uuid);
    }

    public static void playerActive(Player player) {
        UUID uuid = player.getUniqueId();
        players.put(uuid, System.currentTimeMillis());

        if (afkStatus.getOrDefault(uuid, false)) {
            afkStatus.put(uuid, false);

            TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(uuid);
            if (tabPlayer != null && formatManager != null) {
                formatManager.setPrefix(tabPlayer, null);
            }

            player.sendActionBar(Component.text("You're no longer AFK", NamedTextColor.GRAY));
        }
    }
}
