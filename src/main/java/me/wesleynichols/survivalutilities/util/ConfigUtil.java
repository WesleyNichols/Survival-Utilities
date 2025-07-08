package me.wesleynichols.survivalutilities.util;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public class ConfigUtil {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();

    public static Component formatMessage(String template, Player player) {
        if (template == null) return Component.empty();

        String replaced = template
                .replace("%player%", player.getName())
                .replace("%uuid%", player.getUniqueId().toString())
                .replace("%world%", player.getWorld().getName());

        return LEGACY.deserialize(replaced);
    }

    public static Component formatMessage(String template) {
        if (template == null) return Component.empty();
        return LEGACY.deserialize(template);
    }

    public static void sendFormattedMessage(List<String> lines, Player player) {
        for (String line : lines) {
            player.sendMessage(formatMessage(line, player));
        }
    }

    public static void playConfigSound(ConfigurationSection soundConfig, Player player) {
        if (soundConfig == null || player == null) return;

        String sound = soundConfig.getString("sound");
        if (sound == null || sound.isBlank()) {
            SurvivalUtilities.getInstance().getLogger().warning("Missing or empty 'sound' in sound config.");
            return;
        }

        float volume = (float) soundConfig.getDouble("volume", 1.0);
        float pitch = (float) soundConfig.getDouble("pitch", 1.0);

        try {
            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            SurvivalUtilities.getInstance().getLogger().warning("Invalid sound: " + sound);
        }
    }
}
