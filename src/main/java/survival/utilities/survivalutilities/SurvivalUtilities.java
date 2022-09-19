package survival.utilities.survivalutilities;

import org.bukkit.plugin.java.JavaPlugin;

public final class SurvivalUtilities extends JavaPlugin{

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        registerCommands();
        getServer().getPluginManager().registerEvents(new JoinCheck(this), this);
        getServer().getPluginManager().registerEvents(new DenyInteract(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerCommands() {
        getCommand("apply").setExecutor(new ApplicationBook());
        getCommand("accept").setExecutor(new AcceptCommand());
        getCommand("unaccept").setExecutor(new AcceptCommand());
    }
}
