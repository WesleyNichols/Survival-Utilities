package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PortalCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        //  Check if the command is disabled
        if (!SurvivalUtilities.getInstance().isCommandEnabled("portal")) {
            sender.sendMessage(Component.text("This command is currently disabled.", NamedTextColor.RED));
            return true;
        }

        if (sender instanceof Player player) {
            if (player.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
                sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                        .append(Component.text("You can't link portals from The End!")));
                return true;
            }
            boolean normalWorld = player.getWorld().getEnvironment().equals(World.Environment.NORMAL);
            Location loc =  player.getLocation().toBlockLocation().multiply(normalWorld ? 1F/8 : 8);

            sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("To link a portal at this location build a portal in the "
                            + (normalWorld ? "Nether" : "Overworld") + " at X:" + loc.getBlockX() + ", Z:" + loc.getBlockZ())));
        }
        return true;
    }
}
