package IAmNotJustJess.destroyTheMonument.arenas;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.teams.TeamColour;
import IAmNotJustJess.destroyTheMonument.utility.ConsoleDebugSending;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class ArenaFileHandler {

    public static void save() {

        Plugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);
        ConsoleDebugSending.send(
            "send-save-messages",
            "<#dbd814>Saving Arena Instances..."
        );

        for(ArenaInstance arenaInstance : ArenaManager.arenaList.values()) {

            File configFile = new File(plugin.getDataFolder() + File.separator + "arenas" + File.separator + arenaInstance.getArenaName() + ".yml");
            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

            fileConfiguration.set("name", arenaInstance.getArenaName());
            fileConfiguration.set("teams.0.colour", arenaInstance.getTeamColours().getFirst());
            fileConfiguration.set("teams.1.colour", arenaInstance.getTeamColours().get(1));

            int i = 0;
            for(Location location : arenaInstance.getMonumentList().get(arenaInstance.getTeamColours().getFirst())) {
                fileConfiguration.set("teams.0.monumentsLocations." + i, location);
                i++;
            }

            i = 0;
            for(Location location : arenaInstance.getMonumentList().get(arenaInstance.getTeamColours().get(1))) {
                fileConfiguration.set("teams.1.monumentsLocations." + i, location);
                i++;
            }

            i = 0;
            for(Location location : arenaInstance.getSpawnLocations().get(arenaInstance.getTeamColours().getFirst())) {
                fileConfiguration.set("teams.0.spawnLocations." + i, location);
                i++;
            }

            i = 0;
            for(Location location : arenaInstance.getSpawnLocations().get(arenaInstance.getTeamColours().get(1))) {
                fileConfiguration.set("teams.1.spawnLocations." + i, location);
                i++;
            }

            try {
                fileConfiguration.save(configFile);
                ConsoleDebugSending.send(
                    "send-save-messages",
                    "<#14db4c>Successfully saved the<#ffffff>" + arenaInstance.getArenaName() + "<#14db4c>arena instance!"
                );
            } catch (IOException e) {
                ConsoleDebugSending.send(
                    "send-save-messages",
                    "<#cc2b2b>Failed to save the<#ffffff>" + arenaInstance.getArenaName() + "<#cc2b2b>arena instance!"
                );
                throw new RuntimeException(e);
            }
        }

    }

    public static void load() {

        Plugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        ConsoleDebugSending.send(
            "send-load-messages",
            "<#dbd814>Loading Arena Instances..."
        );

        try {
            Files.createDirectories(Paths.get(plugin.getDataFolder() + File.separator + "arenas"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File[] configFolder = new File(plugin.getDataFolder() + File.separator + "arenas").listFiles();

        ArenaManager.arenaList.clear();

        for(File configFile : configFolder) {

            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
            ArenaInstance arenaInstance = new ArenaInstance(fileConfiguration.getString("name"));

            TeamColour teamColour0 = TeamColour.valueOf(fileConfiguration.getString("teams.0.colour"));
            TeamColour teamColour1 = TeamColour.valueOf(fileConfiguration.getString("teams.1.colour"));

            arenaInstance.getTeamColours().add(teamColour0);
            arenaInstance.getTeamColours().add(teamColour1);

            arenaInstance.getMonumentList().put(teamColour0, new ArrayList<>());
            arenaInstance.getMonumentList().put(teamColour1, new ArrayList<>());

            arenaInstance.getSpawnLocations().put(teamColour0, new ArrayList<>());
            arenaInstance.getSpawnLocations().put(teamColour1, new ArrayList<>());

            if(!Objects.requireNonNull(fileConfiguration.getConfigurationSection("teams.0.monumentLocations")).getKeys(false).isEmpty()) {
                for(String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("teams.0.monumentLocations")).getKeys(false)) {
                    arenaInstance.getMonumentList().get(teamColour0).add(fileConfiguration.getLocation("teams.0.monumentLocations."+path));
                }
            }

            if(!Objects.requireNonNull(fileConfiguration.getConfigurationSection("teams.1.monumentLocations")).getKeys(false).isEmpty()) {
                for(String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("teams.1.monumentLocations")).getKeys(false)) {
                    arenaInstance.getMonumentList().get(teamColour1).add(fileConfiguration.getLocation("teams.1.monumentLocations."+path));
                }
            }

            if(!Objects.requireNonNull(fileConfiguration.getConfigurationSection("teams.0.spawnLocations")).getKeys(false).isEmpty()) {
                for(String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("teams.0.spawnLocations")).getKeys(false)) {
                    arenaInstance.getMonumentList().get(teamColour0).add(fileConfiguration.getLocation("teams.0.spawnLocations."+path));
                }
            }

            if(!Objects.requireNonNull(fileConfiguration.getConfigurationSection("teams.1.spawnLocations")).getKeys(false).isEmpty()) {
                for(String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("teams.1.spawnLocations")).getKeys(false)) {
                    arenaInstance.getMonumentList().get(teamColour1).add(fileConfiguration.getLocation("teams.1.spawnLocations."+path));
                }
            }

            ArenaManager.arenaList.put(arenaInstance.getArenaName(), arenaInstance);

            ConsoleDebugSending.send(
                "send-load-messages",
                "<#14db4c>Successfully loaded the<#ffffff>" + arenaInstance.getArenaName() + "<#14db4c>arena instance!"
            );
        }
    }

}
