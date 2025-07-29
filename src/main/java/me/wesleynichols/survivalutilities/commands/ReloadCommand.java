package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.commands.template.BaseCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class ReloadCommand extends BaseCommand {

    @Override
    protected boolean executeCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(reloadConfigWithStatusMessage());
        return true;
    }

    public Component reloadConfigWithStatusMessage() {
        try {
            SurvivalUtilities.getInstance().reloadConfigs();
            return SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("Config reloaded!", NamedTextColor.GREEN));
        } catch (Exception e) {
            SurvivalUtilities.getInstance().getLogger().log(Level.SEVERE, "Failed to reload config", e);
            return SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("Failed to reload config!", NamedTextColor.RED));
        }
    }
}
