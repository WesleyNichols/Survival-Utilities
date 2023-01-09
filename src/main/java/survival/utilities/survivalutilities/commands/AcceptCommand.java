package survival.utilities.survivalutilities.commands;

import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shanerx.mojang.Mojang;
import survival.utilities.survivalutilities.config.CustomConfig;

//import java.util.Objects;
import java.util.UUID;

public class AcceptCommand implements CommandExecutor{

    public static String getCommand = "accept";
    private final Mojang mojang = new Mojang().connect();
    private static final LuckPerms luckPerms = LuckPermsProvider.get();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        FileConfiguration config = CustomConfig.get();

        if (!(args.length == 1)) { return false; }

        if (player.hasPermission("survivalutil.accept")) {
            OfflinePlayer user = Bukkit.getOfflinePlayer(args[0]);
            UUID uuid = user.getUniqueId();

            try {
                mojang.getUUIDOfUsername(user.getName());
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + user.getName() + " is not a valid user!");
                return true;
            }

            //  region Accept
            if (command.getName().equalsIgnoreCase(getCommand)) {
                if (config.contains(uuid.toString())) {
                    sender.sendMessage(ChatColor.RED + user.getName() + " is already accepted!");
                    return true;
                }

                if (user.hasPlayedBefore()) {
                    acceptUserByUUID(uuid);
                    sender.sendMessage(ChatColor.GOLD + "You accepted " + ChatColor.YELLOW + user.getName() + ChatColor.GOLD + " to the server!");
                    config.set(uuid.toString(), 1);
                } else {
                    sender.sendMessage(ChatColor.RED + user.getName() + " will be accepted the next time they join.");
                    config.set(uuid.toString(), 0);
                }

                CustomConfig.save();
                return true;
            }
            // endregion

            //  region Unaccept
            if (command.getName().equalsIgnoreCase("unaccept")) {
                if (!config.contains(uuid.toString())) {
                    sender.sendMessage(ChatColor.RED + user.getName() + " is not currently accepted!");
                    return true;
                }

                if (user.hasPlayedBefore()) {

                    sender.sendMessage(ChatColor.GOLD + "You unaccepted " + ChatColor.YELLOW + user.getName() + ChatColor.GOLD + " from the server!");
                } else {
                    sender.sendMessage(ChatColor.RED + user.getName() + " will no longer be accepted the next time they join.");
                }

                config.set(uuid.toString(), null);
                CustomConfig.save();
                return true;
            }
            //  endregion
        }
        return false;
    }

    public static void acceptUserByUUID(UUID uuid) {
        luckPerms.getUserManager().modifyUser(uuid, u -> {
            u.data().add(Node.builder("group.player").build());
            u.data().remove(Node.builder("group.default").build());
        });

        OfflinePlayer user = Bukkit.getOfflinePlayer(uuid);
        if (user.isOnline())
            Bukkit.broadcast(Component.text(ChatColor.GREEN + user.getName() + " was accepted as a member!"));
    }

//    public static boolean unacceptUserByUUID(UUID uuid, Player source) {
//        String group = Objects.requireNonNull(luckPerms.getUserManager().getUser(uuid)).getPrimaryGroup();
//        if (group.equals("moderator") || group.equals("administrator") || group.equals("developer")) {
//            source.sendMessage(ChatColor.DARK_RED + Bukkit.getOfflinePlayer(uuid).getName() + " can't be unaccepted, they're too powerful!");
//            return false;
//        }
//
//        luckPerms.getUserManager().modifyUser(uuid, u -> u.data().clear());
//        return true;
//    }
}
