package me.wesleynichols.survivalutilities.config;

import me.wesleynichols.survivalutilities.SurvivalUtilities;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public class CustomConfig {

    private final SurvivalUtilities plugin = SurvivalUtilities.getInstance();
    private final String fileName;
    private File file;
    private FileConfiguration config;

    public CustomConfig(String fileName) {
        this.fileName = fileName;
        createConfigIfNotExists();
        loadConfig();
    }

    private void createConfigIfNotExists() {
        file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                if (!file.getParentFile().exists()) {
                    if (!file.getParentFile().mkdirs()) {
                        plugin.getLogger().log(Level.WARNING, "Could not create parent directories for " + fileName);
                    }
                }

                try (InputStream in = plugin.getResource(fileName)) {
                    if (in == null) {
                        plugin.getLogger().log(Level.WARNING, "Default config resource not found: " + fileName);
                        if (!file.createNewFile()) {
                            plugin.getLogger().log(Level.WARNING, "Could not create empty config file: " + fileName);
                        }
                    } else {
                        Files.copy(in, file.toPath());
                    }
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create config file: " + fileName, e);
            }
        }
    }

    private void loadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reloadConfig() {
        loadConfig();
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config file: " + fileName, e);
        }
    }
}
