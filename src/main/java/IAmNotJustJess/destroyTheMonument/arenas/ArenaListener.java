package IAmNotJustJess.destroyTheMonument.arenas;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.configuration.MessagesConfiguration;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterManager;
import IAmNotJustJess.destroyTheMonument.teams.TeamColour;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageParser;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

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
            if(teamColour == PlayerCharacterManager.getList().get(event.getPlayer()).getTeam()
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

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player victim)) return;
        if(!ArenaManager.checkIfPlayerIsInGame(victim)) return;
        if(ArenaManager.arenaList.get(ArenaManager.playerArenaIdList.get(victim)).getArenaState() != ArenaState.RUNNING) {
            event.setCancelled(true);
            return;
        }
        PlayerCharacter victimPlayerCharacter = PlayerCharacterManager.getList().get(victim);

        int damageAmount;


        switch(event.getDamager()) {
            case Player damager -> {
                if(!ArenaManager.checkIfPlayerIsInGame(damager)) return;
                PlayerCharacter damagerPlayerCharacter = PlayerCharacterManager.getList().get(damager);
                if(victimPlayerCharacter.getTeam() == damagerPlayerCharacter.getTeam()) {
                    event.setCancelled(true);
                    return;
                }

                damageAmount = Objects.requireNonNull(Objects.requireNonNull(damagerPlayerCharacter.getPlayer().getInventory().getItemInMainHand().getItemMeta())
                    .getPersistentDataContainer()
                    .get(new NamespacedKey(JavaPlugin.getPlugin(DestroyTheMonument.class), "damage"), PersistentDataType.INTEGER));

                event.setDamage(0);
                damageAmount = (int) Math.round((damageAmount + damagerPlayerCharacter.getFlatDealDamageIncrease()) * damagerPlayerCharacter.getDealDamageMultiplier());
                victimPlayerCharacter.setLastAttacked(damagerPlayerCharacter);
                victimPlayerCharacter.dealDamage(damageAmount);
            }
            case Projectile projectile -> {
                if(projectile.getShooter() instanceof Player damager) {
                    if(!ArenaManager.checkIfPlayerIsInGame(damager)) return;
                    PlayerCharacter damagerPlayerCharacter = PlayerCharacterManager.getList().get(damager);
                    if(victimPlayerCharacter.getTeam() == damagerPlayerCharacter.getTeam()) {
                        event.setCancelled(true);
                        return;
                    }

                    damageAmount = Objects.requireNonNull(projectile
                        .getPersistentDataContainer()
                        .get(new NamespacedKey(JavaPlugin.getPlugin(DestroyTheMonument.class), "damage"), PersistentDataType.INTEGER));

                    event.setDamage(0);
                    damageAmount = (int) Math.round((damageAmount + damagerPlayerCharacter.getFlatDealDamageIncrease()) * damagerPlayerCharacter.getDealDamageMultiplier());
                    victimPlayerCharacter.setLastAttacked(damagerPlayerCharacter);
                    victimPlayerCharacter.dealDamage(damageAmount);
                }
            }
            default -> {}
        }
    }

    public ArenaListener() {

    }

}
