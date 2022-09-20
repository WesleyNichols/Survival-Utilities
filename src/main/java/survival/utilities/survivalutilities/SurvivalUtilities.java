package survival.utilities.survivalutilities;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import survival.utilities.survivalutilities.commands.AcceptCommand;
import survival.utilities.survivalutilities.commands.ApplyCommand;
import survival.utilities.survivalutilities.commands.HelpCommand;
import survival.utilities.survivalutilities.listeners.DenyInteract;
import survival.utilities.survivalutilities.listeners.OnPlayerJoin;

import java.io.File;
import java.io.IOException;

public final class SurvivalUtilities extends JavaPlugin{

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        File configFile = new File(getDataFolder(), "player.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        try {
            config.save(configFile);
        }catch (Exception e) {
            e.printStackTrace();
        }
        registerCommands();
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new DenyInteract(this), this);
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
}

