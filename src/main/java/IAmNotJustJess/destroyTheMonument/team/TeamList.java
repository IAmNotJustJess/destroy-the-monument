package IAmNotJustJess.destroyTheMonument.team;

import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;

import java.util.HashMap;
import java.util.UUID;

public class TeamList {

    private static HashMap<TeamColour, Team> list = new HashMap<>();

    public static HashMap<TeamColour, Team> getList() {
        return list;
    }

}
