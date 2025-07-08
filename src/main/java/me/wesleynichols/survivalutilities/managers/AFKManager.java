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

import java.util.*;

public class AFKManager extends BukkitRunnable {

    private final SurvivalUtilities plugin;
    private final TabAPI tabAPI;
    private final TabListFormatManager formatManager;

    private long movementThreshold;
    private static final Component AFK_ENTER_MSG = Component.text("You're now AFK", NamedTextColor.GRAY);
    private static final Component AFK_EXIT_MSG = Component.text("You're no longer AFK", NamedTextColor.GRAY);
    private static final Component AFK_STATUS_MSG = Component.text("You're currently AFK", NamedTextColor.GRAY);
    private static final String AFK_PREFIX = "§7";

    private final Map<UUID, Long> lastActivity = new HashMap<>();
    private final Set<UUID> afkPlayers = new HashSet<>();

    public AFKManager(SurvivalUtilities plugin) {
        this.plugin = plugin;
        this.tabAPI = TabAPI.getInstance();
        this.formatManager = tabAPI.getTabListFormatManager();

        reload();
    }

    public void reload() {
        // Config is in seconds, convert to milliseconds (× 1000)
        long thresholdInSeconds = plugin.getConfig().getLong("afk", 600); // 600s default = 10 mins
        this.movementThreshold = thresholdInSeconds * 1000L;
    }

    @Override
    public void run() {
        if (!plugin.isEnabled()) {
            cancel();
            return;
        }

        long now = System.currentTimeMillis();
        Set<UUID> toRemove = new HashSet<>();

        for (UUID uuid : lastActivity.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline() || player.hasPermission("group.default")) {
                toRemove.add(uuid);
                continue;
            }

            long last = lastActivity.get(uuid);
            boolean isAFK = now - last >= movementThreshold;
            boolean wasAFK = afkPlayers.contains(uuid);

            if (isAFK && !wasAFK) {
                afkPlayers.add(uuid);
                player.sendActionBar(AFK_ENTER_MSG);
                setTabPrefix(player, AFK_PREFIX);

            } else if (!isAFK && wasAFK) {
                afkPlayers.remove(uuid);
                player.sendActionBar(AFK_EXIT_MSG);
                setTabPrefix(player, null);

            } else if (isAFK) {
                player.sendActionBar(AFK_STATUS_MSG);
            }
        }

        for (UUID uuid : toRemove) {
            lastActivity.remove(uuid);
            afkPlayers.remove(uuid);
        }
    }

    public void recordActivity(Player player) {
        UUID uuid = player.getUniqueId();
        lastActivity.put(uuid, System.currentTimeMillis());

        if (afkPlayers.remove(uuid)) {
            player.sendActionBar(AFK_EXIT_MSG);
            setTabPrefix(player, null);
        }
    }

    public void removePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        lastActivity.remove(uuid);
        afkPlayers.remove(uuid);
        setTabPrefix(player, null);
    }

    private void setTabPrefix(Player player, String prefix) {
        TabPlayer tabPlayer = tabAPI.getPlayer(player.getUniqueId());
        if (tabPlayer != null && formatManager != null) {
            formatManager.setPrefix(tabPlayer, prefix);
        }
    }
}
