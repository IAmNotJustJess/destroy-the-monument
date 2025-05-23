package IAmNotJustJess.destroyTheMonument.arena;

import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterList;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClass;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClassList;
import IAmNotJustJess.destroyTheMonument.team.TeamColour;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ArenaManager {
    public static HashMap<Integer, ArenaInstance> arenaList = new HashMap<>();
    public static HashMap<Player, Integer> playerArenaIdList = new HashMap<>();

    public static void playerJoin(Player player, Integer id) {
        if(!arenaList.containsKey(id)) return;

        playerArenaIdList.put(player, id);
        ArenaInstance arenaInstance = arenaList.get(id);
        PlayerCharacterList.getList().put(player, new PlayerCharacter(
                player,
                (PlayerClass) PlayerClassList.getList().getFirst().clone(),
                TeamColour.NONE,
                1.0f
        ));
        arenaInstance.addPlayerToArena(player);
    }

    public static void playerLeave(Player player) {
        if(!playerArenaIdList.containsKey(player)) return;

        ArenaInstance arenaInstance = arenaList.get(playerArenaIdList.get(player));
        playerArenaIdList.remove(player);
        arenaInstance.removePlayerFromArena(player);
    }

}
