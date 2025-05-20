package IAmNotJustJess.destroyTheMonument.configuration;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.team.TeamList;
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

public class TeamConfiguration {
    public static YamlDocument teamConfiguration;

    public static void setConfig() {

        JavaPlugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        try {
            teamConfiguration = YamlDocument.create(new File(plugin.getDataFolder(), "teams.yml"), Objects.requireNonNull(plugin.getResource("teams.yml")),
                GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }

        TeamList.createListFromConfig();
    }
}
