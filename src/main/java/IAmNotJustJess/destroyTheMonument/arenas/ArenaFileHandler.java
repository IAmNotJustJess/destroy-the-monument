package IAmNotJustJess.destroyTheMonument.arenas;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.teams.TeamColour;
import IAmNotJustJess.destroyTheMonument.utility.QuickSendingMethods;
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
        QuickSendingMethods.sendToConsole(
            "send-save-messages",
            "<#dbd814>Saving arena instances..."
        );

        for(ArenaInstance arenaInstance : ArenaManager.arenaList.values()) {

            File configFile = new File(plugin.getDataFolder() + File.separator + "arenas" + File.separator + arenaInstance.getArenaName() + ".yml");
            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

            fileConfiguration.set("name", arenaInstance.getArenaName());
            fileConfiguration.set("lobby-location", arenaInstance.getLobbyLocation());
            fileConfiguration.set("teams.0.colour", arenaInstance.getTeamColours().getFirst().toString());
            fileConfiguration.set("teams.1.colour", arenaInstance.getTeamColours().get(1).toString());

            int i = 0;
            if(arenaInstance.getMonumentList().get(arenaInstance.getFirstTeam()) != null) {
                for(Location location : arenaInstance.getMonumentList().get(arenaInstance.getFirstTeam())) {
                    fileConfiguration.set("teams.0.monument-locations." + i, location);
                    i++;
                }
            }

            i = 0;
            if(arenaInstance.getMonumentList().get(arenaInstance.getFirstTeam()) != null) {
                for (Location location : arenaInstance.getMonumentList().get(arenaInstance.getSecondTeam())) {
                    fileConfiguration.set("teams.1.monument-locations." + i, location);
                    i++;
                }
            }

            i = 0;
            if(arenaInstance.getSpawnLocations().get(arenaInstance.getSecondTeam()) != null) {
                for (Location location : arenaInstance.getSpawnLocations().get(arenaInstance.getFirstTeam())) {
                    fileConfiguration.set("teams.0.spawn-locations." + i, location);
                    i++;
                }
            }

            i = 0;
            if(arenaInstance.getSpawnLocations().get(arenaInstance.getFirstTeam()) != null) {
                for (Location location : arenaInstance.getSpawnLocations().get(arenaInstance.getSecondTeam())) {
                    fileConfiguration.set("teams.1.spawn-locations." + i, location);
                    i++;
                }
            }

            i = 0;
            if(!arenaInstance.getShopLocations().isEmpty()) {
                for (Location location : arenaInstance.getShopLocations()) {
                    fileConfiguration.set("shop-locations." + i, location);
                    i++;
                }
            }

            try {
                fileConfiguration.save(configFile);
                QuickSendingMethods.sendToConsole(
                    "send-save-messages",
                    "<#14db4c>Successfully saved the <#ffffff>" + arenaInstance.getArenaName() + " <#14db4c>arena instance!"
                );
            } catch (IOException e) {
                QuickSendingMethods.sendToConsole(
                    "send-save-messages",
                    "<#cc2b2b>Failed to save the <#ffffff>" + arenaInstance.getArenaName() + " <#cc2b2b>arena instance!"
                );
                throw new RuntimeException(e);
            }
        }

    }

    public static void load() {

        Plugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        QuickSendingMethods.sendToConsole(
            "send-load-messages",
            "<#dbd814>Loading arena instances..."
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

            arenaInstance.setFirstTeam(teamColour0);
            arenaInstance.setSecondTeam(teamColour1);
            arenaInstance.setLobbyLocation(fileConfiguration.getLocation("lobby-location"));

            arenaInstance.getTeamColours().add(teamColour0);
            arenaInstance.getTeamColours().add(teamColour1);

            arenaInstance.getMonumentList().put(teamColour0, new ArrayList<>());
            arenaInstance.getMonumentList().put(teamColour1, new ArrayList<>());

            arenaInstance.getSpawnLocations().put(teamColour0, new ArrayList<>());
            arenaInstance.getSpawnLocations().put(teamColour1, new ArrayList<>());

            if(fileConfiguration.getConfigurationSection("teams.0.monument-locations") != null) {
                if (!fileConfiguration.getConfigurationSection("teams.0.monument-locations").getKeys(false).isEmpty()) {
                    for (String path : fileConfiguration.getConfigurationSection("teams.0.monument-locations").getKeys(false)) {
                        arenaInstance.getMonumentList().get(teamColour0).add(fileConfiguration.getLocation("teams.0.monument-locations." + path));
                    }
                }
            }

            if(fileConfiguration.getConfigurationSection("teams.1.monument-locations") != null) {
                if (!fileConfiguration.getConfigurationSection("teams.1.monument-locations").getKeys(false).isEmpty()) {
                    for (String path : fileConfiguration.getConfigurationSection("teams.1.monument-locations").getKeys(false)) {
                        arenaInstance.getMonumentList().get(teamColour1).add(fileConfiguration.getLocation("teams.1.monument-locations." + path));
                    }
                }
            }

            if(fileConfiguration.getConfigurationSection("teams.0.spawn-locations") != null) {
                if (!fileConfiguration.getConfigurationSection("teams.0.spawn-locations").getKeys(false).isEmpty()) {
                    for (String path : fileConfiguration.getConfigurationSection("teams.0.spawn-locations").getKeys(false)) {
                        arenaInstance.getSpawnLocations().get(teamColour0).add(fileConfiguration.getLocation("teams.0.spawn-locations." + path));
                    }
                }
            }

            if(fileConfiguration.getConfigurationSection("teams.1.spawn-locations") != null) {
                if (!fileConfiguration.getConfigurationSection("teams.1.spawn-locations").getKeys(false).isEmpty()) {
                    for (String path : fileConfiguration.getConfigurationSection("teams.1.spawn-locations").getKeys(false)) {
                        arenaInstance.getSpawnLocations().get(teamColour1).add(fileConfiguration.getLocation("teams.1.spawn-locations." + path));
                    }
                }
            }

            if(fileConfiguration.getConfigurationSection("shop-locations") != null) {
                if (!fileConfiguration.getConfigurationSection("shop-locations").getKeys(false).isEmpty()) {
                    for (String path : fileConfiguration.getConfigurationSection("shop-locations").getKeys(false)) {
                        arenaInstance.getShopLocations().add(fileConfiguration.getLocation("shop-locations." + path));
                    }
                }
            }

            ArenaManager.arenaList.put(arenaInstance.getArenaName(), arenaInstance);

            QuickSendingMethods.sendToConsole(
                "send-load-messages",
                "<#14db4c>Successfully loaded the <#ffffff>" + arenaInstance.getArenaName() + " <#14db4c>arena instance!"
            );
        }
    }

}
