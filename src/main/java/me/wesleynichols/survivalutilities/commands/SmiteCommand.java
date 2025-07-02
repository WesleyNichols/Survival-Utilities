package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SmiteCommand implements CommandExecutor {

    /**
     * Smites (kills) a player with lightning (visual only)
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        //  Check if the command is disabled
        if (!SurvivalUtilities.getInstance().isCommandEnabled("smite")) {
            sender.sendMessage(Component.text("This command is currently disabled.", NamedTextColor.RED));
            return true;
        }

        Player target;

        if (args.length == 1) {
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(Component.text(args[0] + " is not a valid target!", NamedTextColor.DARK_RED));
                return true;
            }
        } else if (sender instanceof Player player) {
            target = player;
        } else {
            sender.sendMessage(Component.text("You must specify a target when using this command from console.", NamedTextColor.RED));
            return true;
        }

        Location loc = target.getLocation();
        World world = loc.getWorld();

        // Show lightning effect
        world.strikeLightningEffect(loc); // visual only

        sender.sendMessage(Component.text("You smite ", NamedTextColor.GOLD)
                .append(Component.text(target.getName(), NamedTextColor.YELLOW))
                .append(Component.text("!", NamedTextColor.GOLD)));
        target.sendMessage(Component.text("A divine force descends to smite you!", NamedTextColor.DARK_PURPLE));

        // Kill the player
        target.setHealth(0);
        return true;
    }
}