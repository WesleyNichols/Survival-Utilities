package me.wesleynichols.survivalutilities.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ConfigUtil {

    private static Plugin plugin;

    /**
     * Convert a config's message using legacy ampersands and filling placeholders
     */
    public static Component formatMessage(String template, Player player) {
        if (template == null) {
            return Component.empty();
        }

        // Replace placeholders
        String replaced = template
                .replace("%player%", player.getName())
                .replace("%uuid%", player.getUniqueId().toString())
                .replace("%world%", player.getWorld().getName());

        // Parse legacy color codes with Adventure
        return LegacyComponentSerializer.legacyAmpersand().deserialize(replaced);
    }

    /**
     * Convert a config's message using legacy ampersands
     */
    public static Component formatMessage(String template) {
        if (template == null) {
            return Component.empty();
        }

        // Parse legacy color codes with Adventure
        return LegacyComponentSerializer.legacyAmpersand().deserialize(template);
    }

    /**
     * Send a formatted message, reading each line in the message
     */
    public static void sendFormattedMessage(Object raw, Player player) {
        if (raw instanceof List<?>) {
            for (Object line : (List<?>) raw) {
                if (line instanceof String str) {
                    player.sendMessage(formatMessage(str, player));
                }
            }
        } else if (raw instanceof String str) {
            player.sendMessage(formatMessage(str, player));
        }
    }

    /**
     * Read and play a sound using config values
     */
    public static void playConfigSound (ConfigurationSection soundConfig, Player player) {
        try {
            String sound = soundConfig.getString("sound");
            float volume = (float) soundConfig.getDouble("volume");
            float pitch = (float) soundConfig.getDouble("pitch");

            assert sound != null;

            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid sound specified in config: " + soundConfig.getString("sound"));
        }
    }
}
