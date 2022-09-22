package survival.utilities.survivalutilities;

import org.bukkit.plugin.java.JavaPlugin;
import survival.utilities.survivalutilities.commands.Accept;
import survival.utilities.survivalutilities.commands.Apply;
import survival.utilities.survivalutilities.commands.Help;
import survival.utilities.survivalutilities.config.CustomConfig;
import survival.utilities.survivalutilities.listeners.DenyInteract;
import survival.utilities.survivalutilities.listeners.OnPlayerJoin;


public final class SurvivalUtilities extends JavaPlugin{

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        CustomConfig.load("player.yml");
        CustomConfig.save();

        registerCommands();
        registerEvents();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerCommands() {
        getCommand("accept").setExecutor(new Accept());
        getCommand("apply").setExecutor(new Apply());
        getCommand("help").setExecutor(new Help());
        getCommand("unaccept").setExecutor(new Accept());
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new DenyInteract(this), this);
    }
}

