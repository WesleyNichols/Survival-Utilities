package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.managers.BaseCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RulesCommand extends BaseCommand {

    @Override
    protected boolean executeCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("Only players can use this command.", NamedTextColor.RED)));
            return true;
        }

        SurvivalUtilities plugin = SurvivalUtilities.getInstance();
        List<String> rules = plugin.getConfig().getStringList(label);

        if (rules.isEmpty()) {
            player.sendMessage(plugin.getPrefix()
                    .append(Component.text("Rules are not configured properly.", NamedTextColor.RED)));
            return true;
        }

        for (String line : rules) {
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(line));
        }

        return true;
    }
}
