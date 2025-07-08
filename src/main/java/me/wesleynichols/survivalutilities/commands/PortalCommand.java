package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.managers.BaseCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PortalCommand extends BaseCommand {

    @Override
    protected boolean executeCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("Only players can use this command.", NamedTextColor.RED)));
            return true;
        }

        World.Environment env = player.getWorld().getEnvironment();
        if (env == World.Environment.THE_END) {
            sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(SurvivalUtilities.getInstance().getPrefix()
                            .append(Component.text("You can't link portals from The End!", NamedTextColor.RED))));
            return true;
        }

        boolean inOverworld = env == World.Environment.NORMAL;
        Location current = player.getLocation();
        double targetX = inOverworld ? current.getX() / 8 : current.getX() * 8;
        double targetZ = inOverworld ? current.getZ() / 8 : current.getZ() * 8;
        double targetY = current.getY(); // Y is not scaled between dimensions, we'll use theirs
        int blockX = (int) Math.floor(targetX);
        int blockY = (int) Math.floor(targetY);
        int blockZ = (int) Math.floor(targetZ);

        // Construct the full message as a single component
        Component message = Component.text()
                .append(SurvivalUtilities.getInstance().getPrefix())
                .append(Component.text("To link a portal at this location, build one in the "))
                .append(Component.text(inOverworld ? "Nether" : "Overworld", NamedTextColor.GOLD))
                .append(Component.text(" at "))
                .append(Component.text("X: " + blockX + ", Z: " + blockZ, NamedTextColor.YELLOW))
                .build()
                .clickEvent(ClickEvent.suggestCommand(blockX + " " + blockY + " " + blockZ))
                .hoverEvent(HoverEvent.showText(Component.text("Click to paste in chat", NamedTextColor.GRAY)));

        player.sendMessage(message);
        return true;
    }
}
