package IAmNotJustJess.destroyTheMonument;

import IAmNotJustJess.destroyTheMonument.arenas.ArenaFileHandler;
import IAmNotJustJess.destroyTheMonument.arenas.ArenaListener;
import IAmNotJustJess.destroyTheMonument.arenas.ArenaSettings;
import IAmNotJustJess.destroyTheMonument.commands.ArenaSetupCommand;
import IAmNotJustJess.destroyTheMonument.commands.GuiDebugCommand;
import IAmNotJustJess.destroyTheMonument.commands.tabCompleters.ArenaSetupTabCompleter;
import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.configuration.MessagesConfiguration;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClassFileHandler;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClassManager;
import IAmNotJustJess.destroyTheMonument.teams.TeamManager;
import IAmNotJustJess.destroyTheMonument.utility.QuickSendingMethods;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DestroyTheMonument extends JavaPlugin {

    @Override
    public void onEnable() {

        QuickSendingMethods.sendToConsole(
            "<#dbd814>Loading basic configuration..."
        );

        MainConfiguration.setConfig();
        MessagesConfiguration.setConfig();

        TeamManager.createListFromConfig();

        QuickSendingMethods.sendToConsole(
            "send-load-messages",
            "<#14db4c>Successfully loaded the basic configuration!"
        );

        QuickSendingMethods.sendToConsole(
            "send-load-messages",
            "<#dbd814>Loading global settings..."
        );

        ArenaSettings.loadSettings();

        QuickSendingMethods.sendToConsole(
            "send-load-messages",
            "<#dbd814>Loading command executors and tab completers..."
        );

        try {
            this.getCommand("testgui").setExecutor(new GuiDebugCommand());
            QuickSendingMethods.sendToConsole(
                "send-load-messages",
                "<#14db4c>Successfully loaded the <#ffffff>/testgui <#14db4c>command executor!"
            );
        }
        catch (NullPointerException nullPointerException) {
            QuickSendingMethods.sendToConsole(
                "send-load-messages",
                "<#cc2b2b>Failed to load the <#ffffff>/testgui <#cc2b2b>command executor!"
            );
        }

        try {
            this.getCommand("dtm").setExecutor(new ArenaSetupCommand());
            this.getCommand("dtm").setTabCompleter(new ArenaSetupTabCompleter());
            QuickSendingMethods.sendToConsole(
                "send-load-messages",
                "<#14db4c>Successfully loaded the <#ffffff>/dtm <#14db4c>command executor and tab completer!"
            );
        }
        catch (NullPointerException nullPointerException) {
            QuickSendingMethods.sendToConsole(
                "send-load-messages",
                "<#cc2b2b>Failed to load the <#ffffff>/dtm <#cc2b2b>command executor and tab completer!"
            );
        }

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
