package me.wesleynichols.survivalutilities.commands;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SlimeCommand implements CommandExecutor {

    private final Map<UUID, Integer> activeTasks = new HashMap<>();
    private final int DURATION = 60; // 1 minute in ticks
    private final int UPDATE_INTERVAL = 2; // every 2 seconds

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }

        UUID uuid = player.getUniqueId();
        if (activeTasks.containsKey(uuid)) {
            // Cancel the task if the command is run while already active
            Bukkit.getScheduler().cancelTask(activeTasks.remove(uuid));
            player.sendMessage(SurvivalUtilities.getInstance().getPrefix().append(Component.text("Slime chunk display disabled")));
        } else {
            // Start the repeating task
            int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(SurvivalUtilities.getInstance(), new Runnable() {
                private int ticks = 0;

                @Override
                public void run() {
                    if (!player.isOnline()) {
                        Bukkit.getScheduler().cancelTask(activeTasks.remove(uuid));
                        return;
                    }

                    boolean isSlime = player.getChunk().isSlimeChunk();
                    Component msg = Component.text(isSlime ? "Slime Chunk" : "Not a Slime Chunk", isSlime ? NamedTextColor.GREEN : NamedTextColor.RED);
                    player.sendActionBar(msg);

                    ticks += UPDATE_INTERVAL * 20;
                    if (ticks >= DURATION * 20) {
                        Bukkit.getScheduler().cancelTask(activeTasks.remove(uuid));
                        player.sendMessage(SurvivalUtilities.getInstance().getPrefix().append(Component.text("Slime chunk display disabled")));
                    }
                }
            }, 0L, UPDATE_INTERVAL * 20);
            activeTasks.put(uuid, taskId);
            player.sendMessage(SurvivalUtilities.getInstance().getPrefix().append(Component.text("Slime chunk display enabled for " + DURATION + " seconds")));
        }

        return true;
    }
}
