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
import java.util.Objects;

public class MessagesConfiguration {

    public static YamlDocument upgradesMessagesConfiguration;
    public static YamlDocument playerMessagesConfiguration;
    public static YamlDocument arenaMessagesConfiguration;

    public static void setConfig() {
        JavaPlugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        try {
            MessagesConfiguration.upgradesMessagesConfiguration = YamlDocument.create(new File(plugin.getDataFolder(), "messages/upgrades.yml"), Objects.requireNonNull(plugin.getResource("messages/upgrades.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            MessagesConfiguration.playerMessagesConfiguration = YamlDocument.create(new File(plugin.getDataFolder(), "messages/players.yml"), Objects.requireNonNull(plugin.getResource("messages/players.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            MessagesConfiguration.arenaMessagesConfiguration = YamlDocument.create(new File(plugin.getDataFolder(), "messages/arena.yml"), Objects.requireNonNull(plugin.getResource("messages/arena.yml")),
                GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
