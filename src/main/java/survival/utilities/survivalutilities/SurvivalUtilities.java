package survival.utilities.survivalutilities;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import survival.utilities.survivalutilities.commands.*;
import survival.utilities.survivalutilities.config.CustomConfig;
import survival.utilities.survivalutilities.listeners.DenyInteract;
import survival.utilities.survivalutilities.listeners.OnPlayerJoin;


public final class SurvivalUtilities extends JavaPlugin {

    private static SurvivalUtilities instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        CustomConfig.load("player.yml");
        CustomConfig.save();

        this.registerEvent(new OnPlayerJoin());
        this.registerEvent(new DenyInteract());

        this.registerCommand(ApplyCommand.getCommand, new AcceptCommand());
        this.registerCommand(AcceptCommand.getCommand[0], new ApplyCommand());
        this.registerCommand(AcceptCommand.getCommand[1], new ApplyCommand());
        this.registerCommand(HealCommand.getCommand, new HealCommand());
        this.registerCommand(HelpCommand.getCommand, new HelpCommand());
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
        instance.getCommand(command).setExecutor(executor);
    }
}

