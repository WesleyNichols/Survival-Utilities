package me.wesleynichols.survivalutilities.structures;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class BaseCommand implements CommandExecutor {

    @Override
    public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!SurvivalUtilities.getInstance().isCommandEnabled(label.toLowerCase())) {
            sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("This command is currently disabled.", NamedTextColor.RED)));
            return true;
        }
        return executeCommand(sender, command, label, args);
    }

    protected abstract boolean executeCommand(CommandSender sender, Command command, String label, String[] args);
}
