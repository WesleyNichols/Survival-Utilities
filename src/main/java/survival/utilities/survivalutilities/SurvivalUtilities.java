package survival.utilities.survivalutilities;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import survival.utilities.survivalutilities.commands.*;
import survival.utilities.survivalutilities.config.CustomConfig;
import survival.utilities.survivalutilities.listeners.AFKListener;
import survival.utilities.survivalutilities.listeners.DenyInteract;
import survival.utilities.survivalutilities.managers.AFKManager;
import survival.utilities.survivalutilities.managers.PlayerManager;

import java.util.Objects;


public final class SurvivalUtilities extends JavaPlugin {

    private static SurvivalUtilities instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        CustomConfig.load("player.yml");
        CustomConfig.save();

        this.registerEvent(new PlayerManager());
        this.registerEvent(new DenyInteract());
        this.registerEvent(new AFKListener());

        this.registerCommand(ApplyCommand.getCommand, new ApplyCommand());
        this.registerCommand(AcceptCommand.getCommand, new AcceptCommand());
        this.registerCommand(HealCommand.getCommand, new HealCommand());
        this.registerCommand(HelpCommand.getCommand, new HelpCommand());
        this.registerCommand(MapCommand.getCommand, new MapCommand());

        new AFKManager().runTaskTimer(this, 10L, 60L);
    }

    @Override
    public void onDisable() {

    }

    public static SurvivalUtilities getInstance() {
        return instance;
    }

    public void registerEvent(Listener event) {
        this.getServer().getPluginManager().registerEvents(event, this);
    }

    public void registerCommand(String command, CommandExecutor executor) {
        Objects.requireNonNull(instance.getCommand(command)).setExecutor(executor);
    }
}