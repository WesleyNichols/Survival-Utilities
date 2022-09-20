package survival.utilities.survivalutilities;

import org.bukkit.plugin.java.JavaPlugin;
import survival.utilities.survivalutilities.commands.AcceptCommand;
import survival.utilities.survivalutilities.commands.ApplyCommand;
import survival.utilities.survivalutilities.commands.HelpCommand;
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
        getCommand("accept").setExecutor(new AcceptCommand());
        getCommand("apply").setExecutor(new ApplyCommand());
        getCommand("help").setExecutor(new HelpCommand());
        getCommand("unaccept").setExecutor(new AcceptCommand());
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new DenyInteract(this), this);
    }
}

