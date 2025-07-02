package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (sender instanceof Player player) {
            player.sendMessage(runReload());
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage(runReload());
        }
        return true;
    }

    public TextComponent runReload() {
        try {
            SurvivalUtilities.getInstance().reloadConfigs();
        } catch (Exception e) {
            return Component.text("[SurvivalUtilities] Failed to reload configs!", NamedTextColor.RED);
        }
        return Component.text("[SurvivalUtilities] Configs reloaded!", NamedTextColor.GREEN);
    }

}
