package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0 && sender.hasPermission("survivalutil.broadcast")) {
            Bukkit.broadcast(SurvivalUtilities.getInstance().getPrefix()
                    .append(LegacyComponentSerializer.legacyAmpersand()
                            .deserialize((String.join(" ", args)))));
        }
        return true;
    }
}
