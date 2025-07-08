package me.wesleynichols.survivalutilities;

import me.wesleynichols.survivalutilities.commands.*;
import me.wesleynichols.survivalutilities.config.CustomConfig;
import me.wesleynichols.survivalutilities.listeners.AFKListener;
import me.wesleynichols.survivalutilities.listeners.ArmorPoseListener;
import me.wesleynichols.survivalutilities.listeners.DenyInteract;
import me.wesleynichols.survivalutilities.managers.AFKManager;
import me.wesleynichols.survivalutilities.managers.ChatManager;
import me.wesleynichols.survivalutilities.managers.PlayerManager;
import me.wesleynichols.survivalutilities.util.ArmorPoseUtil;
import me.wesleynichols.survivalutilities.util.ConfigUtil;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SurvivalUtilities extends JavaPlugin {

    private static SurvivalUtilities plugin;

    private CustomConfig playerConfig;
    private CustomConfig armorStandConfig;

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

        // Initialize custom configs
        playerConfig = new CustomConfig("player.yml");
        armorStandConfig = new CustomConfig("armor_stand.yml");

        playerManager = new PlayerManager(this, playerConfig);
        ArmorPoseUtil.setConfig(armorStandConfig.getConfig());

        chatManager = new ChatManager(this);
        afkManager = new AFKManager(this);

        afkManager.runTaskTimer(this, 0L, 40L);

        // Register events
        registerEvent(playerManager);
        registerEvent(new DenyInteract());
        registerEvent(new AFKListener(afkManager));
        registerEvent(chatManager);
        registerEvent(new ArmorPoseListener());

        // Initialize chat spam tracking for online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            chatManager.getOrCreatePlayer(player.getUniqueId());
        }

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
        registerCommand("togglecommand", new ToggleCommandCommand());
    }

    public void reloadConfigs() {
        playerConfig.reloadConfig();
        armorStandConfig.reloadConfig();
        reloadConfig();

        if (playerManager != null) playerManager.reload();
        if (chatManager != null) chatManager.reload();
        if (afkManager != null) afkManager.reload();
    }

    public void registerEvent(Listener event) {
        getServer().getPluginManager().registerEvents(event, this);
    }

    public void registerCommand(String command, CommandExecutor executor) {
        Objects.requireNonNull(getCommand(command)).setExecutor(executor);
    }

    public boolean isCommandEnabled(String command) {
        return getConfig().getBoolean("enabled-commands." + command, true); // Default to true
    }

    public TextComponent getPrefix() {
        String rawPrefix = getConfig().getString("chat-prefix");

        if (rawPrefix == null || rawPrefix.trim().isEmpty()) {
            rawPrefix = "&7[SurvivalUtilities] ";
        }

        return (TextComponent) ConfigUtil.formatMessage(rawPrefix);
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}