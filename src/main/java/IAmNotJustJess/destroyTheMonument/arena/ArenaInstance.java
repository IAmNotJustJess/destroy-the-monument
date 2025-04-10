package IAmNotJustJess.destroyTheMonument.arena;

import IAmNotJustJess.destroyTheMonument.team.TeamColour;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class ArenaInstance {
    private HashMap<Location, TeamColour> monumentList;
    private HashMap<TeamColour, Integer> monumentCount;
    private ArrayList<Player> playerList;
    private HashMap<TeamColour, ArrayList<Player>> playersInTeamsList;
    private int timer;
    private String timerString;
}
