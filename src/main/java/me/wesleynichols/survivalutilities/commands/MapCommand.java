package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.managers.BaseCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MapCommand extends BaseCommand {

    @Override
    protected boolean executeCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        FileConfiguration config = SurvivalUtilities.getInstance().getConfig();
        String mapText = config.getString("map");
        String mapLink = config.getString("map-link");

        if (mapText == null || mapLink == null) {
            String missing = (mapText == null ? "map" : "") + (mapText == null && mapLink == null ? ", " : "")
                    + (mapLink == null ? "map-link" : "");
            player.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("Map information is not configured properly: ", NamedTextColor.RED)
                    .append(Component.text(missing, NamedTextColor.YELLOW))));
            return true;
        }

        Component message = SurvivalUtilities.getInstance().getPrefix()
                .append(LegacyComponentSerializer.legacyAmpersand().deserialize(mapText)
                        .clickEvent(ClickEvent.openUrl(mapLink))
                        .hoverEvent(HoverEvent.showText(
                                Component.text("Redirects to " + mapLink, NamedTextColor.GRAY)))
        );

        player.sendMessage(message);
        return true;
    }
}
