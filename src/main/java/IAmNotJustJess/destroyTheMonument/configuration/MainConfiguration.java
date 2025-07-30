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

public class MainConfiguration {

    public static YamlDocument mainConfiguration;
    public static YamlDocument globalGameRulesConfiguration;
    public static YamlDocument guiConfiguration;
    public static YamlDocument teamConfiguration;

    public static void setConfig() {

        JavaPlugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        try {
            mainConfiguration = YamlDocument.create(new File(plugin.getDataFolder(), "config.yml"), Objects.requireNonNull(plugin.getResource("config.yml")),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            globalGameRulesConfiguration = YamlDocument.create(new File(plugin.getDataFolder(), "rules.yml"), plugin.getResource("rules.yml"),
                GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            guiConfiguration = YamlDocument.create(new File(plugin.getDataFolder(), "gui.yml"), plugin.getResource("gui.yml"),
                GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            teamConfiguration = YamlDocument.create(new File(plugin.getDataFolder(), "teams.yml"), Objects.requireNonNull(plugin.getResource("teams.yml")),
                GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }


}
