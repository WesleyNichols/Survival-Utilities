package survival.utilities.survivalutilities.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class PortalCommand implements CommandExecutor {

    public static String getCommand = "portal";
    /**
     Provides coordinates to build a linkable portal between the Nether and Overworld
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase(getCommand) && sender instanceof Player player) {
            if (player.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
                sender.sendMessage(Component.text("You can't link portals from The End!"));
                return true;
            }
            boolean normalWorld = player.getWorld().getEnvironment().equals(World.Environment.NORMAL);
            Location loc =  player.getLocation().toBlockLocation().multiply(normalWorld ? 1F/8 : 8);

            int[] pos = {
                    loc.getBlockX(),
                    loc.getBlockZ()
            };

            sender.sendMessage(Component.text("Build a portal in the "
                    + (normalWorld ? "Nether" : "Overworld") + " at "
                    + Arrays.toString(pos) + " to link the two portals together").color(NamedTextColor.YELLOW));
        }
        return true;
    }
}
