package IAmNotJustJess.destroyTheMonument.player;

import IAmNotJustJess.destroyTheMonument.teams.Team;
import IAmNotJustJess.destroyTheMonument.teams.TeamManager;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerCharacterManager {

    private static final HashMap<Player, PlayerCharacter> list = new HashMap<>();

    public static HashMap<Player, PlayerCharacter> getList() {
        return list;
    }

    public static Team getPlayerTeam(Player player) {
        return TeamManager.list.get(list.get(player).getTeam());
    }

}
