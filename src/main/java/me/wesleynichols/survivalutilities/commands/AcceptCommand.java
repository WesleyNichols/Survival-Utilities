package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.managers.BaseCommand;
import me.wesleynichols.survivalutilities.managers.PageManager;
import me.wesleynichols.survivalutilities.managers.PlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.shanerx.mojang.Mojang;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AcceptCommand extends BaseCommand {

    private final Mojang mojang = new Mojang().connect();
    private static final PlainTextComponentSerializer PLAIN_TEXT = PlainTextComponentSerializer.plainText();

    @Override
    protected boolean executeCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        PlayerManager playerManager = SurvivalUtilities.getInstance().getPlayerManager();

        //  /accept list
        if (args.length > 0 && args[0].equalsIgnoreCase("list")) {
            int page = 1;
            if (args.length > 1) {
                try {
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Component.text("Invalid page number!", NamedTextColor.RED));
                    return true;
                }
            }

            List<Component> listPage = getAcceptedPlayerPage(playerManager, page);
            listPage.forEach(sender::sendMessage);
            return true;
        }

        // /accept <player>
        String username = args[0];
        OfflinePlayer target = Bukkit.getOfflinePlayer(username);

        try {
            mojang.getUUIDOfUsername(username); // validate player exists
        } catch (Exception e) {
            sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text(username + " is not a valid user!", NamedTextColor.RED)));
            return true;
        }

        boolean isAccepting = label.equalsIgnoreCase("accept");
        boolean isAccepted = playerManager.hasBeenAccepted(target);

        if (isAccepting) {
            if (isAccepted) {
                sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                        .append(Component.text(target.getName() + " is already accepted!", NamedTextColor.RED)));
                return true;
            }

            if (playerManager.playerAccept(target)) {
                sendConfirmation(sender, "You accepted ", target.getName(), " to the server!");
                return true;
            } else {
                sender.sendMessage(Component.text("Failed to accept " + target.getName() + "!", NamedTextColor.RED));
                return false;
            }
        } else { // unaccept
            if (!isAccepted) {
                sender.sendMessage(Component.text(target.getName() + " is already not accepted!", NamedTextColor.RED));
                return true;
            }

            if (playerManager.playerUnaccept(target)) {
                sendConfirmation(sender, "You un-accepted ", target.getName(), " from the server!");
                return true;
            } else {
                sender.sendMessage(Component.text("Failed to un-accept " + target.getName() + "!", NamedTextColor.RED));
                return false;
            }
        }
    }

    private List<Component> getAcceptedPlayerPage(PlayerManager playerManager, int page) {
        List<Component> entries = new ArrayList<>();

        FileConfiguration config = playerManager.getConfig();
        for (String uuidStr : config.getKeys(false)) {
            List<?> data = config.getList(uuidStr);
            if (data != null && data.size() >= 2) {
                Object nameObj = data.get(0);
                Object statusObj = data.get(1);

                if (nameObj instanceof String name && statusObj instanceof Integer status && (status == 0 || status == 1)) {
                    Component entry = Component.text(name, NamedTextColor.YELLOW)
                            .append(Component.text(" (" + uuidStr + ")", NamedTextColor.GRAY));

                    if (status == 0) { // pending
                        entry = entry.decorate(TextDecoration.ITALIC);
                    }

                    entries.add(entry);
                }
            }
        }

        entries.sort((c1, c2) ->
                PLAIN_TEXT.serialize(c1).compareToIgnoreCase(PLAIN_TEXT.serialize(c2))
        );

        return PageManager.getPageFromList(entries, page, "Accepted Players", "accept list");
    }

    private void sendConfirmation(CommandSender sender, String prefix, String name, String suffix) {
        sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                .append(Component.text(prefix, NamedTextColor.GOLD)
                        .append(Component.text(Objects.requireNonNullElse(name, "Unknown"), NamedTextColor.YELLOW))
                        .append(Component.text(suffix, NamedTextColor.GOLD))));
    }
}
