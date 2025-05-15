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

public class GlobalGameRulesSettings {

    public static YamlDocument globalGameRulesConfiguration;

    public static void setConfig() {
        JavaPlugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);
        try {
            globalGameRulesConfiguration = YamlDocument.create(new File(plugin.getDataFolder(), "rules.yml"), plugin.getResource("rules.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
