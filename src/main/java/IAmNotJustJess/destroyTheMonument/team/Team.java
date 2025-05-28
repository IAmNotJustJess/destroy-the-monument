package IAmNotJustJess.destroyTheMonument.team;

import org.bukkit.Color;
import org.bukkit.Material;

public class Team {

    public String name;
    public String textColour;
    public TeamColour teamColour;
    public Material blockType;
    public Color armourColour;

    public Team(String name, String textColour, TeamColour teamColour, Material blockType, Color armourColour) {
        this.name = name;
        this.textColour = textColour;
        this.teamColour = teamColour;
        this.blockType = blockType;
        this.armourColour = armourColour;
    }
}
