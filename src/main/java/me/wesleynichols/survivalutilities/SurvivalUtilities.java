package me.wesleynichols.survivalutilities;

import me.wesleynichols.survivalutilities.chat.ChatManager;
import me.wesleynichols.survivalutilities.commands.*;
import me.wesleynichols.survivalutilities.configs.ConfigUtil;
import me.wesleynichols.survivalutilities.configs.PlayerConfig;
import me.wesleynichols.survivalutilities.listeners.AFKListener;
import me.wesleynichols.survivalutilities.listeners.ArmorStandListener;
import me.wesleynichols.survivalutilities.listeners.DenyInteractListener;
import me.wesleynichols.survivalutilities.managers.AFKManager;
import me.wesleynichols.survivalutilities.managers.PlayerManager;
import me.wesleynichols.survivalutilities.pose.ArmorPoseLoader;
import me.wesleynichols.survivalutilities.pose.ArmorPoseManager;
import me.wesleynichols.survivalutilities.model.ArmorStandPose;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Objects;

public final class SurvivalUtilities extends JavaPlugin {

    private static SurvivalUtilities plugin;

    private PlayerConfig playerConfig;

    private PlayerManager playerManager;
    private ChatManager chatManager;
    private AFKManager afkManager;

    public static SurvivalUtilities getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        loadArmorStandPoses();

        playerConfig = new PlayerConfig(this);
        playerManager = new PlayerManager(this, playerConfig);

        chatManager = new ChatManager(this);
        afkManager = new AFKManager(this);
        afkManager.runTaskTimer(this, 0L, 40L);

        // Register events
        registerEvent(playerManager);
        registerEvent(new DenyInteractListener());
        registerEvent(new AFKListener(afkManager));
        registerEvent(chatManager);
        registerEvent(new ArmorStandListener());

        // Initialize chat tracking
        Bukkit.getOnlinePlayers().forEach(player -> chatManager.getOrCreatePlayer(player.getUniqueId()));

        // Register commands
        registerCommand("accept", new AcceptCommand());
        registerCommand("apply", new ApplyCommand());
        registerCommand("broadcast", new BroadcastCommand());
        registerCommand("heal", new HealCommand());
        registerCommand("help", new HelpCommand());
        registerCommand("info", new InfoCommand());
        registerCommand("map", new MapCommand());
        registerCommand("portal", new PortalCommand());
        registerCommand("reload", new ReloadCommand());
        registerCommand("rules", new RulesCommand());
        registerCommand("slime", new SlimeCommand());
        registerCommand("smite", new SmiteCommand());
        registerCommand("togglecommand", new ToggleCommand());

        getLogger().info("SurvivalUtilities enabled.");
    }

    public void reloadConfigs() {
        playerConfig.reload();
        reloadConfig(); // default config
        loadArmorStandPoses();

        if (playerManager != null) playerManager.reload();
        if (chatManager != null) chatManager.reload();
        if (afkManager != null) afkManager.reload();

        getLogger().info("SurvivalUtilities configuration reloaded.");
    }

    public void registerEvent(Listener event) {
        getServer().getPluginManager().registerEvents(event, this);
    }

    public void registerCommand(String command, CommandExecutor executor) {
        Objects.requireNonNull(getCommand(command)).setExecutor(executor);
    }

    public boolean isCommandEnabled(String command) {
        return getConfig().getBoolean("enabled-commands." + command, true);
    }

    private void loadArmorStandPoses() {
        Map<Integer, ArmorStandPose> loadedPoses = ArmorPoseLoader.loadPoses(getConfig());
        ArmorPoseManager.setPoses(loadedPoses);
    }

    public TextComponent getPrefix() {
        String rawPrefix = getConfig().getString("chat-prefix", "&7[SurvivalUtilities] ");
        return (TextComponent) ConfigUtil.formatMessage(rawPrefix);
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public PlayerConfig getPlayerConfig() {
        return playerConfig;
    }
}
