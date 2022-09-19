package survival.utilities.survivalutilities;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class SurvivalUtilities extends JavaPlugin{

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        System.out.println("Enabling Survival Utilities");
        this.getCommand("apply").setExecutor(new ApplicationBook());
        this.getCommand("accept").setExecutor(new AcceptCommand());
        getServer().getPluginManager().registerEvents(new JoinCheck(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Disabling Survival Utilities");
    }
}
