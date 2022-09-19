package survival.utilities.survivalutilities;

import com.sun.tools.javac.Main;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class JoinCheck implements Listener {
    public JoinCheck(SurvivalUtilities e) {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("SurvivalUtilities");
        if(p.getConfig().getString(name) != null && p.getConfig().getInt(name) == 1 && player.hasPermission("group.default")){
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + name + " parent add player");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + name + " parent remove default");
            player.getInventory().clear();
            p.getConfig().set(name, null);
            p.saveConfig();
        }
    }
}
