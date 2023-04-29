package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.managers.PlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.shanerx.mojang.Mojang;

import java.util.Objects;

public class AcceptCommand implements CommandExecutor{

    private final Mojang mojang = new Mojang().connect();

    /**
     Accepts or un-accepts users

     @param args target player
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            try {
                mojang.getUUIDOfUsername(args[0]);
            } catch (Exception e) {
                sender.sendMessage(Component.text(args[0] + " is not a valid user!", NamedTextColor.RED));
                return true;
            }

            //  region Accept
            if (label.equals("accept")) {
                if (PlayerManager.playerStatus(target)) {
                    sender.sendMessage(Component.text(target.getName() + " is already accepted!", NamedTextColor.RED));
                    return true;
                }

                if (PlayerManager.playerAccept(target)) {
                    sender.sendMessage(Component.text("You accepted ", NamedTextColor.GOLD)
                            .append(Component.text(Objects.requireNonNull(target.getName()), NamedTextColor.YELLOW)
                                    .append(Component.text(" to the server!", NamedTextColor.GOLD))));
                    return true;
                } else {
                    sender.sendMessage(Component.text("Failed to accept + " + target.getName() + "!", NamedTextColor.RED));
                    return false;
                }
            }
            // endregion

            //  region Unaccept
            if (label.equals("unaccept")) {
                if (!PlayerManager.playerStatus(target)) {
                    sender.sendMessage(Component.text(target.getName() + " is already not accepted!", NamedTextColor.RED));
                    return true;
                }

                if (PlayerManager.playerUnaccept(target)) {
                    sender.sendMessage(Component.text("You un-accepted ", NamedTextColor.GOLD)
                            .append(Component.text(Objects.requireNonNull(target.getName()), NamedTextColor.YELLOW)
                                    .append(Component.text(" from the server!", NamedTextColor.GOLD))));
                    return true;
                } else {
                    sender.sendMessage(Component.text("Failed to un-accept + " + target.getName() + "!", NamedTextColor.RED));
                    return false;
                }
            }
                //  endregion
        }
        return false;
    }
}
