package IAmNotJustJess.destroyTheMonument.configuration;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class MessagesConfiguration {

    public static YamlDocument upgradesMessagesConfiguration;
    public static YamlDocument playerMessagesConfiguration;
    public static YamlDocument arenaMessagesConfiguration;

    public static void setConfig() {
        JavaPlugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        try {
            MessagesConfiguration.upgradesMessagesConfiguration = YamlDocument.create(new File(plugin.getDataFolder()+File.separator+"messages", "upgrades.yml"), plugin.getResource("upgrades.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            MessagesConfiguration.playerMessagesConfiguration = YamlDocument.create(new File(plugin.getDataFolder()+File.separator+"messages", "players.yml"), plugin.getResource("players.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            MessagesConfiguration.arenaMessagesConfiguration = YamlDocument.create(new File(plugin.getDataFolder()+File.separator+"messages", "arena.yml"), plugin.getResource("arena.yml"),
                GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
