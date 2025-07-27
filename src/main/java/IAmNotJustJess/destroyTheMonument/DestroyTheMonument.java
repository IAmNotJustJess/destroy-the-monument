package IAmNotJustJess.destroyTheMonument;

import IAmNotJustJess.destroyTheMonument.arenas.ArenaFileHandler;
import IAmNotJustJess.destroyTheMonument.arenas.ArenaListener;
import IAmNotJustJess.destroyTheMonument.arenas.ArenaSettings;
import IAmNotJustJess.destroyTheMonument.commands.ArenaSetupCommand;
import IAmNotJustJess.destroyTheMonument.commands.GuiDebugCommand;
import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.configuration.MessagesConfiguration;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClassFileHandler;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClassManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DestroyTheMonument extends JavaPlugin {

    @Override
    public void onEnable() {

        this.getCommand("testgui").setExecutor(new GuiDebugCommand());
        this.getCommand("dtm").setExecutor(new ArenaSetupCommand());

        MainConfiguration.setConfig();
        MessagesConfiguration.setConfig();

        ArenaSettings.loadSettings();

        ArenaFileHandler.load();
        PlayerClassFileHandler.load();
        PlayerClassManager.cacheDescriptions();

        Bukkit.getServer().getPluginManager().registerEvents(new ArenaListener(), this);

    }

    @Override
    public void onDisable() {
        ArenaFileHandler.save();
        PlayerClassFileHandler.save();
        // Plugin shutdown logic
    }
}
