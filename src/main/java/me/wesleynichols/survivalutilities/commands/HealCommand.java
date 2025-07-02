package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HealCommand implements CommandExecutor {

    /**
     Restore health and hunger on command

     @param args Player to heal, assumes sender if no target provided
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        //  Check if the command is disabled
        if (!SurvivalUtilities.getInstance().isCommandEnabled("heal")) {
            sender.sendMessage(Component.text("This command is currently disabled.", NamedTextColor.RED));
            return true;
        }

        // Assume sender is target
        if (sender instanceof Player target) {
            // Re-assign target if valid arg
            if (args.length == 1) {
                if (Bukkit.getOnlinePlayers().stream().anyMatch(e -> e.getName().equalsIgnoreCase(args[0]))) {
                    target = Bukkit.getPlayer(args[0]);
                } else {
                    target.sendMessage(Component.text(args[0] + " is not a valid target!", NamedTextColor.DARK_GREEN));
                    return false;
                }
            }
            // Heal target
            assert target != null;
            target.setHealth(20);
            target.setFoodLevel(20);
            target.sendMessage(Component.text("Healed " + target.getName(), NamedTextColor.GREEN));
            if (target != sender) { target.sendMessage(Component.text("Healed by " + target.getName(), NamedTextColor.GREEN)); }
            return true;
        }
        return false;
    }

}
