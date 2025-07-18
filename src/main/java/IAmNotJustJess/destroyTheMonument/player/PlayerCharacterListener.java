package IAmNotJustJess.destroyTheMonument.player;

import IAmNotJustJess.destroyTheMonument.arenas.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerCharacterListener implements Listener {

    @EventHandler
    public void onPlayerTakenDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager) || !(event.getEntity() instanceof Player victim)) return;

        if (!(ArenaManager.playerArenaIdList.containsKey(damager) || !(ArenaManager.playerArenaIdList.containsKey((victim))))) return;

        switch(event.getCause()) {
            case PROJECTILE -> {

            }
            case ENTITY_ATTACK -> {

            }
            case ENTITY_SWEEP_ATTACK -> {

            }
        }
    }

    @EventHandler
    public void onAbilityUse(PlayerInteractEvent event) {

    }

}
