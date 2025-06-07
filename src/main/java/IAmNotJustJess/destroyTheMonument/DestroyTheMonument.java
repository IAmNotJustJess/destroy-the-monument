package IAmNotJustJess.destroyTheMonument;

import IAmNotJustJess.destroyTheMonument.arena.ArenaListener;
import IAmNotJustJess.destroyTheMonument.arena.ArenaSettings;
import IAmNotJustJess.destroyTheMonument.commands.GuiDebugCommand;
import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.configuration.MessagesConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DestroyTheMonument extends JavaPlugin {

    @Override
    public void onEnable() {

        this.getCommand("testgui").setExecutor(new GuiDebugCommand());

        MainConfiguration.setConfig();
        MessagesConfiguration.setConfig();

        ArenaSettings.loadSettings();

        Bukkit.getServer().getPluginManager().registerEvents(new ArenaListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
