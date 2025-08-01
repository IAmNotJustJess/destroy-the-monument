package IAmNotJustJess.destroyTheMonument.arenas;

import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterManager;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClassManager;
import IAmNotJustJess.destroyTheMonument.teams.TeamColour;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

public class ArenaManager {
    public static HashMap<String, ArenaInstance> arenaList = new HashMap<>();
    public static HashMap<Player, String> playerArenaIdList = new HashMap<>();

    public static void playerJoin(Player player, String id) {
        if(!arenaList.containsKey(id)) return;

        ArenaInstance arenaInstance = arenaList.get(id);
        if(!arenaInstance.checkArenaJoinAvailabilityState()) return;

        playerArenaIdList.put(player, id);
        PlayerCharacterManager.getList().put(player, new PlayerCharacter(
                player,
                PlayerClassManager.getList().getFirst().clone(),
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

    public static boolean checkIfPlayerIsInGame(Player player)  {
        if(!PlayerCharacterManager.getList().containsKey(player)) return false;
        return playerArenaIdList.containsKey(player);
    }

    public static boolean checkIfPlayersAreInTheSameGame(Player playerOne, Player playerTwo) {
        if(!checkIfPlayerIsInGame(playerOne)) return false;
        if(!checkIfPlayerIsInGame(playerTwo)) return false;
        return Objects.equals(playerArenaIdList.get(playerOne), playerArenaIdList.get(playerTwo));
    }

    public static boolean checkIfPlayersCanAttackEachOther(Player playerOne, Player playerTwo) {
        if(!checkIfPlayersAreInTheSameGame(playerOne, playerTwo)) return false;
        return arenaList.get(playerArenaIdList.get(playerOne)).getArenaState() == ArenaState.RUNNING;
    }

}
