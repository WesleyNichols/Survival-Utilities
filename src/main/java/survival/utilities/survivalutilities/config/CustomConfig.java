package survival.utilities.survivalutilities.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import survival.utilities.survivalutilities.SurvivalUtilities;

import java.io.File;
import java.util.logging.Level;

public class CustomConfig {
    public static File file;
    private static FileConfiguration customFile;

    public static void load(String FilePath) {
        file = new File(SurvivalUtilities.getInstance().getDataFolder(), FilePath);
        try {
            customFile = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to load config at " + FilePath, e);
        }
    }

    public static FileConfiguration get() {
        return customFile;
    }

    public static void save() {
        try {
            customFile.save(file);
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config for " + customFile.getName(), e);
        }
    }

}
