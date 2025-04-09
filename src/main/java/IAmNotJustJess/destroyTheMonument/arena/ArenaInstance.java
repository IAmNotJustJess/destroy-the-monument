package IAmNotJustJess.destroyTheMonument.arena;

import IAmNotJustJess.destroyTheMonument.team.TeamColour;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class ArenaInstance {
    public HashMap<Location, TeamColour> monumentList;
    public ArrayList<Player> playerList;
    public HashMap<TeamColour, ArrayList<Player>> playersInTeamsList;
}
