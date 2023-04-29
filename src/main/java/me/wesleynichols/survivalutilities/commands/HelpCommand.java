package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.managers.PageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            try {
                for (Component line : PageManager.getPage(args.length > 0 ? Integer.parseInt(args[0]) : 1, "help")) {
                    sender.sendMessage(line);
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("'" + args[0] + "' is not a number!", NamedTextColor.RED));
                return false;
            }
        }
        return true;
    }
}
