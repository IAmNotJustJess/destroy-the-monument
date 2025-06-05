package IAmNotJustJess.destroyTheMonument;

import IAmNotJustJess.destroyTheMonument.arena.ArenaListener;
import IAmNotJustJess.destroyTheMonument.arena.ArenaSettings;
import IAmNotJustJess.destroyTheMonument.configuration.GlobalGameRulesConfiguration;
import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.configuration.MessagesConfiguration;
import IAmNotJustJess.destroyTheMonument.configuration.TeamConfiguration;
import mc.obliviate.inventory.InventoryAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DestroyTheMonument extends JavaPlugin {

    @Override
    public void onEnable() {

        new InventoryAPI(this).init();

        MainConfiguration.setConfig();
        MessagesConfiguration.setConfig();
        TeamConfiguration.setConfig();

        GlobalGameRulesConfiguration.setConfig();
        ArenaSettings.loadSettings();

        Bukkit.getServer().getPluginManager().registerEvents(new ArenaListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
