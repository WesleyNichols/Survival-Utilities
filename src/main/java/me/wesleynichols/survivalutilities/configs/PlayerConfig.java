package me.wesleynichols.survivalutilities.configs;

import me.wesleynichols.survivalutilities.model.PlayerStatus;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class PlayerConfig {

    private final File file;
    private YamlConfiguration config;

    public PlayerConfig(JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "players.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public boolean hasStatus(UUID uuid) {
        return config.contains(uuid.toString());
    }

    public boolean isPending(UUID uuid) {
        return PlayerStatus.PENDING == getStatus(uuid);
    }

    public PlayerStatus getStatus(UUID uuid) {
        if (!hasStatus(uuid)) return null;

        String raw = config.getString(uuid.toString());
        try {
            return PlayerStatus.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void setStatus(UUID uuid, PlayerStatus status) {
        config.set(uuid.toString(), status.name().toLowerCase());
        save();
    }

    public void removePlayer(UUID uuid) {
        config.set(uuid.toString(), null);
        save();
    }

    public Set<String> getAllPlayerUUIDs() {
        return config.getKeys(false);
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
