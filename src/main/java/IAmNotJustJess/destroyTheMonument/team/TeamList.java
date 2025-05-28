package IAmNotJustJess.destroyTheMonument.team;

import IAmNotJustJess.destroyTheMonument.configuration.TeamConfiguration;
import org.bukkit.Color;
import org.bukkit.Material;

import java.util.HashMap;

public class TeamList {

    public static HashMap<TeamColour, Team> list = new HashMap<>();

    public static void createListFromConfig() {
        list = new HashMap<>();
        for(Object teamObject : TeamConfiguration.teamConfiguration.getList("teams")) {
            String teamString = (String) teamObject;
            String teamName = TeamConfiguration.teamConfiguration.getString("settings."+teamString+".name");
            String teamTextColour = TeamConfiguration.teamConfiguration.getString("settings."+teamString+".text-colour");
            TeamColour teamColour = TeamColour.valueOf(TeamConfiguration.teamConfiguration.getString("settings."+teamString+".team-colour"));
            Material teamBlockType = Material.valueOf(TeamConfiguration.teamConfiguration.getString("settings."+teamString+".block-type"));
            Color teamArmourColor = Color.fromRGB(
                TeamConfiguration.teamConfiguration.getInt("settings."+teamString+".armour-colour.r"),
                TeamConfiguration.teamConfiguration.getInt("settings."+teamString+".armour-colour.g"),
                TeamConfiguration.teamConfiguration.getInt("settings."+teamString+".armour-colour.b")
            );
            Team team = new Team(teamName, teamTextColour, teamColour, teamBlockType, teamArmourColor);
            list.put(teamColour, team);
        }
    }
}
