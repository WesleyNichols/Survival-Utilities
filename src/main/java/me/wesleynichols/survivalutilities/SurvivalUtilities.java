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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SurvivalUtilities extends JavaPlugin {

    private static SurvivalUtilities plugin;

    public static SurvivalUtilities getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        reloadConfigs();
        initChatSpam();

        registerEvent(new PlayerManager());
        registerEvent(new DenyInteract());
        registerEvent(new AFKListener());
        registerEvent(new ChatManager());
        registerEvent(new ArmorPoseListener());

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
        registerCommand("unaccept", new AcceptCommand());

        new AFKManager().runTaskTimer(this, 20L, 40L);
    }

    public void registerEvent(Listener event) {
        this.getServer().getPluginManager().registerEvents(event, this);
    }

    public void registerCommand(String command, CommandExecutor executor) {
        Objects.requireNonNull(getCommand(command)).setExecutor(executor);
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

    public TextComponent getPrefix() {
        return Component.text("", NamedTextColor.WHITE)
                .append(Component.text("[", NamedTextColor.GOLD)
                        .append(Component.text("BeeBox", NamedTextColor.YELLOW)
                                .append(Component.text("]", NamedTextColor.GOLD)
                                        .append(Component.text(" > ", NamedTextColor.YELLOW)))));
    }
}