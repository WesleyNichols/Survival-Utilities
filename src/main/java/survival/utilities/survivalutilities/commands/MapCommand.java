package survival.utilities.survivalutilities.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MapCommand implements CommandExecutor {

    public static String getCommand = "map";

    private static final String mapLink = "https://map.beeboxmc.com";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            player.sendMessage(Component.text("Click here to open the map!").color(NamedTextColor.GOLD).clickEvent(ClickEvent.openUrl(mapLink)));
            return true;
        }
        return false;
    }
}
