package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.managers.BaseCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SmiteCommand extends BaseCommand {

    @Override
    protected boolean executeCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Restrict sender to Player or Console - no repeating Command Block smiting here
        if (!(sender instanceof Player) && !(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("Only players or the console can use this command.", NamedTextColor.RED)));
            return true;
        }

        Player target;

        if (args.length == 1) {
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                        .append(Component.text(args[0] + " is not a valid target!", NamedTextColor.DARK_RED)));
                return true;
            }
        } else if (sender instanceof Player player) {
            target = player;
        } else {
            sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("You must specify a target when using this command from console.", NamedTextColor.RED)));
            return true;
        }

        Location loc = target.getLocation();
        World world = loc.getWorld();

        if (world == null) {
            sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("Target's world is not loaded.", NamedTextColor.RED)));
            return true;
        }

        world.strikeLightningEffect(loc); // visual only

        sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                .append(Component.text("You smite ", NamedTextColor.GOLD))
                .append(Component.text(target.getName(), NamedTextColor.YELLOW))
                .append(Component.text("!", NamedTextColor.GOLD)));

        target.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                .append(Component.text("A divine force descends to smite you!", NamedTextColor.DARK_PURPLE)));

        target.setHealth(0);
        return true;
    }

}
