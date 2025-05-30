package IAmNotJustJess.destroyTheMonument.arena;

import IAmNotJustJess.destroyTheMonument.configuration.MessagesConfiguration;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterList;
import IAmNotJustJess.destroyTheMonument.team.TeamColour;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageParser;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class ArenaListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(!ArenaManager.playerArenaIdList.containsKey(event.getPlayer())) return;
        ArenaInstance arenaInstance = ArenaManager.arenaList.get(ArenaManager.playerArenaIdList.get(event.getPlayer()));
        Location location = event.getBlock().getLocation();
        if(!arenaInstance.getPlayerPlacedBlocksLocations().contains(location))  {
            event.setCancelled(true);
            return;
        }
        for(TeamColour teamColour : arenaInstance.getMonumentRemainingCount().keySet()) {
            if(teamColour == PlayerCharacterList.getList().get(event.getPlayer()).getTeam()
            && arenaInstance.getMonumentList().get(teamColour).contains(location)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(MiniMessageParser.deserializeToString(
                        MessagesConfiguration.arenaMessagesConfiguration.getString("breaking-own-monument")
                    )
                );
                return;
            }
            if(arenaInstance.getMonumentList().get(teamColour).contains(location)) {
                event.setCancelled(true);
                arenaInstance.breakMonument(event.getPlayer(), teamColour, event.getBlock().getLocation());
                return;
            }
        }
        arenaInstance.getPlayerPlacedBlocksLocations().remove(location);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!ArenaManager.playerArenaIdList.containsKey(event.getPlayer())) return;
        ArenaInstance arenaInstance = ArenaManager.arenaList.get(ArenaManager.playerArenaIdList.get(event.getPlayer()));
        Location location = event.getBlock().getLocation();
        arenaInstance.getPlayerPlacedBlocksLocations().add(location);
    }

    public ArenaListener() {

    }

}
