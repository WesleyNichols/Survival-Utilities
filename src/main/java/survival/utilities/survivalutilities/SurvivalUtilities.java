package survival.utilities.survivalutilities;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import survival.utilities.survivalutilities.commands.*;
import survival.utilities.survivalutilities.config.CustomConfig;
import survival.utilities.survivalutilities.listeners.AFKListener;
import survival.utilities.survivalutilities.listeners.ArmorPoseListener;
import survival.utilities.survivalutilities.listeners.DenyInteract;
import survival.utilities.survivalutilities.managers.AFKManager;
import survival.utilities.survivalutilities.managers.ChatManager;
import survival.utilities.survivalutilities.managers.PlayerManager;
import survival.utilities.survivalutilities.util.ArmorPoseUtil;

import java.util.Objects;

public final class SurvivalUtilities extends JavaPlugin {

    private static SurvivalUtilities instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        reloadConfigs();
        initChatSpam();

        this.registerEvent(new PlayerManager());
        this.registerEvent(new DenyInteract());
        this.registerEvent(new AFKListener());
        this.registerEvent(new ChatManager());
        this.registerEvent(new ArmorPoseListener());

        this.registerCommand(AcceptCommand.getCommand, new AcceptCommand());
        this.registerCommand(ApplyCommand.getCommand, new ApplyCommand());
        this.registerCommand(BroadcastCommand.getCommand, new BroadcastCommand());
        this.registerCommand(HealCommand.getCommand, new HealCommand());
        this.registerCommand(HelpCommand.getCommand, new HelpCommand());
        this.registerCommand(InfoCommand.getCommand, new InfoCommand());
        this.registerCommand(MapCommand.getCommand, new MapCommand());
        this.registerCommand(PortalCommand.getCommand, new PortalCommand());
        this.registerCommand(ReloadCommand.getCommand, new ReloadCommand());
        this.registerCommand(RulesCommand.getCommand, new RulesCommand());
        this.registerCommand(SlimeCommand.getCommand, new SlimeCommand());

        new AFKManager().runTaskTimer(this, 20L, 40L);
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

    public void reloadConfigs() {
        CustomConfig.load("player.yml");
        CustomConfig.save();
        PlayerManager.config = CustomConfig.get();
        CustomConfig.load("armor_stand.yml");
        CustomConfig.save();
        ArmorPoseUtil.config = CustomConfig.get();
        reloadConfig();
    }

    public void initChatSpam() {
        if(Bukkit.getOnlinePlayers().size() > 0) {
            for(Player player: Bukkit.getOnlinePlayers()) {
                ChatManager.initChatManager(player.getUniqueId());
            }
        }
        ChatManager.initConfigVars();
    }
}