package IAmNotJustJess.destroyTheMonument.player;

import IAmNotJustJess.destroyTheMonument.arena.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerCharacterListener implements Listener {

    @EventHandler
    public void onPlayerTakenDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        if (!(ArenaManager.playerArenaIdList.containsKey(damager) || !(ArenaManager.playerArenaIdList.containsKey((victim))))) return;
    }

}
