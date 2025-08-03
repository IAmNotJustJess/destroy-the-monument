package IAmNotJustJess.destroyTheMonument.arenas;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterManager;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClassManager;
import IAmNotJustJess.destroyTheMonument.teams.TeamColour;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageSerializers;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;

public class ArenaManager {
    public static HashMap<String, ArenaInstance> arenaList = new HashMap<>();
    public static HashMap<Player, String> playerArenaIdList = new HashMap<>();
    public static HashMap<Player, Location> playerLocationsBeforeJoiningArena = new HashMap<>();
    public static HashMap<Player, HashMap<Integer, ItemStack>> playerInventoryBeforeJoiningArena = new HashMap<>();
    public static HashMap<Player, GameMode> playerGameModeBeforeJoiningArena = new HashMap<>();

    public static void playerJoin(Player player, String id) {
        if(!arenaList.containsKey(id)) return;

        ArenaInstance arenaInstance = arenaList.get(id);
        if(!arenaInstance.checkArenaJoinAvailabilityState()) return;

        playerLocationsBeforeJoiningArena.put(player, player.getLocation());

        playerInventoryBeforeJoiningArena.put(player, new HashMap<>() {{
            for(int i = 0; i <= 41; i++) {
                playerInventoryBeforeJoiningArena.get(player).put(i, player.getInventory().getItem(i));
            }
        }});

        playerGameModeBeforeJoiningArena.put(player, player.getGameMode());

        player.setGameMode(GameMode.ADVENTURE);
        for(int i = 0; i <= 41; i++) {
            player.getInventory().setItem(0, new ItemStack(Material.AIR));
        }

        Plugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        ItemStack itemStack = new ItemStack(Material.valueOf(MainConfiguration.guiConfiguration.getString("choose-class-item")), 1);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setItemName(MiniMessageSerializers.deserializeToString(MainConfiguration.guiConfiguration.getString("choose-class-item-name")));
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "specialOnClickProperty"), PersistentDataType.STRING, "chooseClass");

        itemStack.setItemMeta(itemMeta);

        player.getInventory().setItem(1, itemStack);

        itemStack = new ItemStack(Material.valueOf(MainConfiguration.guiConfiguration.getString("leave-game-item")), 1);

        itemMeta = itemStack.getItemMeta();

        itemMeta.setItemName(MiniMessageSerializers.deserializeToString(MainConfiguration.guiConfiguration.getString("leave-game-item-name")));
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "specialOnClickProperty"), PersistentDataType.STRING, "leave");

        itemStack.setItemMeta(itemMeta);

        player.getInventory().setItem(7, itemStack);

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

        player.teleport(playerLocationsBeforeJoiningArena.get(player));

        for(int i = 0; i <= 41; i++) {
            player.getInventory().setItem(i, playerInventoryBeforeJoiningArena.get(player).get(i));
        }

        player.setGameMode(playerGameModeBeforeJoiningArena.get(player));

        playerLocationsBeforeJoiningArena.remove(player);
        playerInventoryBeforeJoiningArena.remove(player);
        playerGameModeBeforeJoiningArena.remove(player);

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
