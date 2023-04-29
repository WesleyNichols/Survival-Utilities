package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SlimeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.getChunk().isSlimeChunk()) {
                sender.sendMessage(SurvivalUtilities.getInstance().getPrefix().append(Component.text("This is a slime chunk!")));
            } else {
                sender.sendMessage(SurvivalUtilities.getInstance().getPrefix().append(Component.text("This is not a slime chunk!")));
            }
            return true;
        }
        return false;
    }
}
