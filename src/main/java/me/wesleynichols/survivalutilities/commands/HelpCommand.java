package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.managers.BaseCommand;
import me.wesleynichols.survivalutilities.managers.PageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class HelpCommand extends BaseCommand {

    @Override
    protected boolean executeCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("Only players can use this command.", NamedTextColor.RED)));
            return true;
        }

        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                        .append(Component.text("'" + args[0] + "' is not a number!", NamedTextColor.RED)));
                return true;
            }
        }

        for (Component line : PageManager.getPage(page, label)) {
            player.sendMessage(line);
        }

        return true;
    }
}
