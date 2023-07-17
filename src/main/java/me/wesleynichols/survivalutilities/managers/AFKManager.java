package me.wesleynichols.survivalutilities.managers;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.tablist.TabListFormatManager;
import me.quantiom.advancedvanish.util.AdvancedVanishAPI;
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
    private static final long MovementThreshold = 600000L;  //  10 minutes
    private static final HashMap<UUID, Long> players = new HashMap<>();

    public void run() {
        if (!SurvivalUtilities.getInstance().isEnabled()) { this.cancel(); }

        for (UUID uuid : players.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || player.hasPermission("group.default")) {
                players.remove(uuid);
                continue;
            }

            TabPlayer tabPlayer = tabAPI.getPlayer(player.getUniqueId());
            assert formatManager != null;
            assert tabPlayer != null;
            String prefix = formatManager.getCustomPrefix(tabPlayer);

            if (isAFK(player) && !AdvancedVanishAPI.INSTANCE.isPlayerVanished(player)) {
                player.sendActionBar(Component.text("You're currently AFK", NamedTextColor.GRAY));
//                if (prefix == null) {   //  Is AFK, but hasn't been marked in tablist
//                    formatManager.setPrefix(tabPlayer, "&7");
//                }
            } else {
                if (prefix != null) {   //  No longer AFK, but hasn't been updated in tablist
                    player.sendActionBar(Component.text(""));
//                    formatManager.resetPrefix(tabPlayer);
                }
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

}
