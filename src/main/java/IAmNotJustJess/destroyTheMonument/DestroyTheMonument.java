package IAmNotJustJess.destroyTheMonument;

import IAmNotJustJess.destroyTheMonument.arena.ArenaSettings;
import IAmNotJustJess.destroyTheMonument.configuration.GlobalGameRulesSettings;
import IAmNotJustJess.destroyTheMonument.configuration.MainConfig;
import IAmNotJustJess.destroyTheMonument.configuration.MessagesConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class DestroyTheMonument extends JavaPlugin {

    @Override
    public void onEnable() {

        MainConfig.setConfig();
        MessagesConfig.setConfig();

        GlobalGameRulesSettings.setConfig();
        ArenaSettings.loadSettings();

        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
