package IAmNotJustJess.destroyTheMonument.team;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import org.bukkit.Color;
import org.bukkit.Material;

import java.util.HashMap;

public class TeamList {

    public static HashMap<TeamColour, Team> list = new HashMap<>();

    public static void createListFromConfig() {
        list = new HashMap<>();
        for(Object teamObject : MainConfiguration.teamConfiguration.getList("teams")) {
            String teamString = (String) teamObject;
            String teamName = MainConfiguration.teamConfiguration.getString("settings."+teamString+".name");
            String teamTextColour = MainConfiguration.teamConfiguration.getString("settings."+teamString+".text-colour");
            TeamColour teamColour = TeamColour.valueOf(MainConfiguration.teamConfiguration.getString("settings."+teamString+".team-colour"));
            Material teamBlockType = Material.valueOf(MainConfiguration.teamConfiguration.getString("settings."+teamString+".block-type"));
            Color teamArmourColor = Color.fromRGB(
                MainConfiguration.teamConfiguration.getInt("settings."+teamString+".armour-colour.r"),
                MainConfiguration.teamConfiguration.getInt("settings."+teamString+".armour-colour.g"),
                MainConfiguration.teamConfiguration.getInt("settings."+teamString+".armour-colour.b")
            );
            Team team = new Team(teamName, teamTextColour, teamColour, teamBlockType, teamArmourColor);
            list.put(teamColour, team);
        }
    }
}
