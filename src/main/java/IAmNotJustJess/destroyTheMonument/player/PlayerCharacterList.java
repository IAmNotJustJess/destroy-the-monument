package IAmNotJustJess.destroyTheMonument.player;

import IAmNotJustJess.destroyTheMonument.team.Team;
import IAmNotJustJess.destroyTheMonument.team.TeamColour;
import IAmNotJustJess.destroyTheMonument.team.TeamList;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerCharacterList {

    private static final HashMap<Player, PlayerCharacter> list = new HashMap<>();

    public static HashMap<Player, PlayerCharacter> getList() {
        return list;
    }

    public static Team getPlayerTeam(Player player) {
        return TeamList.list.get(list.get(player).getTeam());
    }

}
