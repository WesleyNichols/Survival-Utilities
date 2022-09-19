package survival.utilities.survivalutilities;

import org.bukkit.plugin.java.JavaPlugin;

public final class SurvivalUtilities extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Enabling Survival Utilities");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Disabling Survival Utilities");
    }
}
