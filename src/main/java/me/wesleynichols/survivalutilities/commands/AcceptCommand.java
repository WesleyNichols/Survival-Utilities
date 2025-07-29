package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.commands.template.BaseCommand;
import me.wesleynichols.survivalutilities.configs.PlayerConfig;
import me.wesleynichols.survivalutilities.managers.PageManager;
import me.wesleynichols.survivalutilities.managers.PlayerManager;
import me.wesleynichols.survivalutilities.model.PlayerStatus;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.shanerx.mojang.Mojang;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AcceptCommand extends BaseCommand {

    private final Mojang mojang = new Mojang().connect();
    private static final PlainTextComponentSerializer PLAIN_TEXT = PlainTextComponentSerializer.plainText();

    @Override
    protected boolean executeCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        PlayerConfig playerConfig = SurvivalUtilities.getInstance().getPlayerConfig();
        PlayerManager playerManager = SurvivalUtilities.getInstance().getPlayerManager();

        // /accept list
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

            List<Component> listPage = getAcceptedPlayerPage(playerConfig, page);
            listPage.forEach(sender::sendMessage);
            return true;
        }

        // /<accept|unaccept> <player>
        String username = args[0];

        // Validate UUID's existence from Mojang API
        String mojangId = mojang.getUUIDOfUsername(username);
        if (mojangId == null || mojangId.isEmpty()) {
            sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text(username + " is not a valid user!", NamedTextColor.RED)));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(username);

        boolean isAccepting = label.equalsIgnoreCase("accept");
        boolean isAccepted = playerConfig.hasStatus(target.getUniqueId());

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

    private List<Component> getAcceptedPlayerPage(PlayerConfig playerConfig, int page) {
        List<Component> entries = new ArrayList<>();

        for (String uuidStr : playerConfig.getAllPlayerUUIDs()) {
            UUID uuid;
            try {
                uuid = UUID.fromString(uuidStr);
            } catch (IllegalArgumentException e) {
                continue; // Skip invalid UUIDs
            }

            PlayerStatus status = playerConfig.getStatus(uuid);
            if (status == null) continue;

            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            String name = player.getName() != null ? player.getName() : uuidStr;

            Component entry = Component.text(name, NamedTextColor.YELLOW)
                    .append(Component.text(" (" + uuidStr + ")", NamedTextColor.GRAY));

            if (status == PlayerStatus.PENDING) {
                entry = entry.decorate(TextDecoration.ITALIC);
            }

            entries.add(entry);
        }

        // Sort alphabetically by plain text
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
