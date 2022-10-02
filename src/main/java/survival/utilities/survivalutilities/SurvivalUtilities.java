package survival.utilities.survivalutilities;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import survival.utilities.survivalutilities.commands.AcceptCommand;
import survival.utilities.survivalutilities.commands.ApplyCommand;
import survival.utilities.survivalutilities.commands.HealCommand;
import survival.utilities.survivalutilities.commands.HelpCommand;
import survival.utilities.survivalutilities.config.CustomConfig;
import survival.utilities.survivalutilities.listeners.AFKListener;
import survival.utilities.survivalutilities.listeners.DenyInteract;
import survival.utilities.survivalutilities.listeners.OnPlayerJoin;
import survival.utilities.survivalutilities.managers.AFKManager;


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
        this.registerEvent(new AFKListener());

        this.registerCommand(ApplyCommand.getCommand, new ApplyCommand());
        this.registerCommand(AcceptCommand.getCommand, new AcceptCommand());
        this.registerCommand(HealCommand.getCommand, new HealCommand());
        this.registerCommand(HelpCommand.getCommand, new HelpCommand());

        new AFKManager().runTaskTimer(this, 30L, 30L);
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

