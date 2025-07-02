package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.util.ConfigUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        //  Check if the command is disabled
        if (!SurvivalUtilities.getInstance().isCommandEnabled("info")) {
            sender.sendMessage(Component.text("This command is currently disabled.", NamedTextColor.RED));
            return true;
        }

        if (sender instanceof Player) {
            ConfigUtil.sendFormattedMessage(SurvivalUtilities.getInstance().getConfig().getStringList("info"), (Player) sender);
        }
        return true;
    }
}
