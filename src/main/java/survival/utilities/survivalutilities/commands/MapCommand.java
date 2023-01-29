package survival.utilities.survivalutilities.commands;

import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import survival.utilities.survivalutilities.SurvivalUtilities;

import java.util.Objects;

public class MapCommand implements CommandExecutor {

    public static String getCommand = "map";

    /**
     Displays a link to the map
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            FileConfiguration config = SurvivalUtilities.getInstance().getConfig();
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand()
                    .deserialize(Objects.requireNonNull(config.getString("map")))
                    .clickEvent(ClickEvent.openUrl(Objects.requireNonNull(config.getString("map-link"))))
                    .hoverEvent(HoverEvent.showText(LegacyComponentSerializer.legacyAmpersand()
                            .deserialize(Objects.requireNonNull(config.getString("map-hover"))))));
            return true;
        }
        return false;
    }
}
