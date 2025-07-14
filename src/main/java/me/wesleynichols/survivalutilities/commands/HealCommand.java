package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.structures.BaseCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class HealCommand extends BaseCommand {

    private static final int RADIUS_LIMIT = 100;

    @Override
    protected boolean executeCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        TextComponent prefix = SurvivalUtilities.getInstance().getPrefix();

        // /heal (self)
        if (args.length == 0) {
            if (sender instanceof Player player) {
                healEntity(player, player);
            } else {
                sender.sendMessage(prefix.append(Component.text("Console must specify a player, selector, or radius.", NamedTextColor.RED)));
            }
            return true;
        }

        String input = args[0];

        // /heal radius
        if (isInteger(input)) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(prefix.append(Component.text("Only players can use radius-based healing.", NamedTextColor.RED)));
                return true;
            }

            int radius = Integer.parseInt(input);
            if (radius <= 0) {
                sender.sendMessage(prefix.append(Component.text("Radius must be a positive integer.", NamedTextColor.RED)));
                return true;
            } else if (radius > RADIUS_LIMIT) {
                sender.sendMessage(prefix.append(Component.text("Radius limit of " + RADIUS_LIMIT + ".", NamedTextColor.RED)));
                return true;
            }

            int healed = 0;
            for (LivingEntity entity : player.getLocation().getNearbyLivingEntities(radius)) {
                if (entity.getUniqueId().equals(player.getUniqueId())) {
                    continue;
                }

                healEntity(entity, sender);
                healed++;
            }

            sender.sendMessage(prefix.append(
                    Component.text("Healed " + healed + (healed == 1 ? " entity" : " entities") + " within radius " + radius + ".", NamedTextColor.GREEN)
            ));
            return true;
        }

        // /heal @<selector> (e.g., @a, @e[type=zombie], @p[distance=..5])
        if (input.startsWith("@")) {
            List<Entity> targets;
            try {
                targets = Bukkit.selectEntities(sender, input);
            } catch (IllegalArgumentException e) {
                sender.sendMessage(prefix.append(Component.text("Invalid selector: " + input, NamedTextColor.RED)));
                return true;
            }

            if (targets.isEmpty()) {
                sender.sendMessage(prefix.append(Component.text("No valid targets found for selector: " + input, NamedTextColor.RED)));
                return true;
            }

            int healed = 0;
            for (Entity entity : targets) {
                if (entity instanceof LivingEntity living) {
                    if (sender instanceof Player senderPlayer && entity.getUniqueId().equals(senderPlayer.getUniqueId())) {
                        continue;
                    }

                    healEntity(living, sender);
                    healed++;
                }
            }

            sender.sendMessage(prefix.append(
                    Component.text("Healed " + healed + (healed == 1 ? " entity" : " entities") + " using selector.", NamedTextColor.GREEN)
            ));
            return true;
        }

        // /heal <player>
        Player target = Bukkit.getPlayerExact(input);
        if (target != null && target.isOnline()) {
            healEntity(target, sender);
            return true;
        }

        sender.sendMessage(prefix.append(Component.text(input + " is not a valid player, radius, or selector!", NamedTextColor.RED)));
        return true;
    }

    private void healEntity(LivingEntity entity) {
        double maxHealth = Objects.requireNonNull(entity.getAttribute(Attribute.MAX_HEALTH)).getValue();
        entity.setHealth(maxHealth);

        if (entity instanceof Player player) {
            player.setFoodLevel(20);
            player.setSaturation(20);
        }
    }

    private void healEntity(LivingEntity entity, CommandSender sender) {
        TextComponent prefix = SurvivalUtilities.getInstance().getPrefix();

        healEntity(entity);

        if (entity instanceof Player healedPlayer) {
            if (sender instanceof Player senderPlayer) {
                if (!senderPlayer.getUniqueId().equals(healedPlayer.getUniqueId())) {
                    healedPlayer.sendMessage(prefix.append(Component.text("You were healed by " + senderPlayer.getName() + ".", NamedTextColor.GREEN)));
                    senderPlayer.sendMessage(prefix.append(Component.text("Healed " + healedPlayer.getName() + ".", NamedTextColor.GREEN)));
                } else {
                    healedPlayer.sendMessage(prefix.append(Component.text("You have been healed.", NamedTextColor.GREEN)));
                }
            } else {
                healedPlayer.sendMessage(prefix.append(Component.text("You were healed by the server.", NamedTextColor.GREEN)));
                sender.sendMessage(prefix.append(Component.text("Healed " + healedPlayer.getName() + ".", NamedTextColor.GREEN)));
            }
        }
    }

    private boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
