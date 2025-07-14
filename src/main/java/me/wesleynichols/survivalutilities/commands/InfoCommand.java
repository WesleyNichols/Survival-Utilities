package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.structures.BaseCommand;
import me.wesleynichols.survivalutilities.util.ConfigUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InfoCommand extends BaseCommand {

    @Override
    protected boolean executeCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("Only players can use this command.", NamedTextColor.RED)));
            return true;
        }

        List<String> infoLines = SurvivalUtilities.getInstance().getConfig().getStringList("info");
        if (infoLines.isEmpty()) {
            player.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("Information is not configured properly.", NamedTextColor.RED)));
            return true;
        }

        ConfigUtil.sendFormattedMessage(infoLines, player);
        return true;
    }
}
