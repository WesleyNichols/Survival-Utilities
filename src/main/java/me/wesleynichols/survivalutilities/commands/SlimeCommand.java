package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import me.wesleynichols.survivalutilities.structures.BaseCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SlimeCommand extends BaseCommand {

    private final Map<UUID, Integer> activeTasks = new HashMap<>();

    @Override
    protected boolean executeCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("Only players can use this command.", NamedTextColor.RED)));
            return true;
        }

        final int DURATION_SECONDS = 60; // duration slime display is active
        final int UPDATE_INTERVAL_SECONDS = 2; // update every 2 seconds
        final int TICKS_PER_SECOND = 20;

        UUID uuid = player.getUniqueId();

        if (activeTasks.containsKey(uuid)) {
            Bukkit.getScheduler().cancelTask(activeTasks.remove(uuid));
            player.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("Slime chunk display disabled")));
        } else {
            int durationTicks = DURATION_SECONDS * TICKS_PER_SECOND;
            int updateIntervalTicks = UPDATE_INTERVAL_SECONDS * TICKS_PER_SECOND;

            int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(SurvivalUtilities.getInstance(), new Runnable() {
                int elapsedTicks = 0;

                @Override
                public void run() {
                    Player p = Bukkit.getPlayer(uuid);
                    if (p == null || !p.isOnline()) {
                        Bukkit.getScheduler().cancelTask(activeTasks.remove(uuid));
                        return;
                    }

                    boolean isSlimeChunk = p.getChunk().isSlimeChunk();
                    Component msg = Component.text(isSlimeChunk ? "Slime Chunk" : "Not a Slime Chunk",
                            isSlimeChunk ? NamedTextColor.GREEN : NamedTextColor.RED);
                    p.sendActionBar(msg);

                    elapsedTicks += updateIntervalTicks;
                    if (elapsedTicks >= durationTicks) {
                        Bukkit.getScheduler().cancelTask(activeTasks.remove(uuid));
                        p.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                                .append(Component.text("Slime chunk display disabled")));
                    }
                }
            }, 0L, updateIntervalTicks);

            activeTasks.put(uuid, taskId);
            player.sendMessage(SurvivalUtilities.getInstance().getPrefix()
                    .append(Component.text("Slime chunk display enabled (" + DURATION_SECONDS + "s)")));
        }

        return true;
    }
}
