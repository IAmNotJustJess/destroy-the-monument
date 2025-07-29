package IAmNotJustJess.destroyTheMonument.teams;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.utility.QuickSendingMethods;
import org.bukkit.Color;
import org.bukkit.Material;

import java.util.HashMap;

public class TeamManager {

    public static HashMap<TeamColour, Team> list = new HashMap<>();

    public static void createListFromConfig() {
        list = new HashMap<>();
        for(Object teamObject : MainConfiguration.teamConfiguration.getSection("teams").getKeys()) {
            String teamString = teamObject.toString();
            String teamName = MainConfiguration.teamConfiguration.getString("teams."+teamString+".name");
            String teamTextColour = MainConfiguration.teamConfiguration.getString("teams."+teamString+".text-colour");
            TeamColour teamColour = TeamColour.valueOf(MainConfiguration.teamConfiguration.getString("teams."+teamString+".team-colour"));
            Material teamBlockType = Material.valueOf(MainConfiguration.teamConfiguration.getString("teams."+teamString+".block-type"));
            Color teamArmourColor = Color.fromRGB(
                MainConfiguration.teamConfiguration.getInt("teams."+teamString+".armour-colour.r"),
                MainConfiguration.teamConfiguration.getInt("teams."+teamString+".armour-colour.g"),
                MainConfiguration.teamConfiguration.getInt("teams."+teamString+".armour-colour.b")
            );
            list.put(teamColour, new Team(teamName, teamTextColour, teamColour, teamBlockType, teamArmourColor));
        }
    }
}
