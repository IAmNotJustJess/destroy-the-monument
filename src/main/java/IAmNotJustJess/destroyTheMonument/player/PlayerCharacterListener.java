package IAmNotJustJess.destroyTheMonument.player;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.arenas.ArenaManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class PlayerCharacterListener implements Listener {

    @EventHandler
    public void onPlayerTakenDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager) || !(event.getDamager() instanceof Projectile projectile) || !(event.getEntity() instanceof Player victim)) return;

        if((PlayerCharacterManager.getPlayerTeam(damager) == PlayerCharacterManager.getPlayerTeam(victim)) ||
            (PlayerCharacterManager.getPlayerTeam((Player) projectile.getShooter()) == PlayerCharacterManager.getPlayerTeam(victim))) {
            event.setCancelled(true);
            return;
        }

        int damage;
        Plugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        switch(event.getCause()) {
            case PROJECTILE -> {
                if(!(ArenaManager.playerArenaIdList.containsKey((Player) projectile.getShooter()) || !(ArenaManager.playerArenaIdList.containsKey((victim))))) return;
                damage = Objects.requireNonNull(projectile.getPersistentDataContainer().get(new NamespacedKey(plugin, "projectileDamage"), PersistentDataType.INTEGER));
                event.setDamage(0.0);
                PlayerCharacterManager.getList().get(victim).dealDamage(damage);
            }
            case ENTITY_ATTACK -> {
                if (!(ArenaManager.playerArenaIdList.containsKey(damager) || !(ArenaManager.playerArenaIdList.containsKey((victim))))) return;
                damage = Objects.requireNonNull(Objects.requireNonNull(damager.getInventory().getItemInMainHand().getItemMeta()).getPersistentDataContainer().get(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER));
                event.setDamage(0.0);
                PlayerCharacterManager.getList().get(victim).dealDamage(damage);
            }
            case ENTITY_SWEEP_ATTACK -> {
                if (!(ArenaManager.playerArenaIdList.containsKey(damager) || !(ArenaManager.playerArenaIdList.containsKey((victim))))) return;
                damage = Objects.requireNonNull(Objects.requireNonNull(damager.getInventory().getItemInMainHand().getItemMeta()).getPersistentDataContainer().get(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER));
                event.setDamage(0.0);
                PlayerCharacterManager.getList().get(victim).dealDamage(damage / 5);
            }
        }
    }

    @EventHandler
    public void onAbilityUse(PlayerInteractEvent event) {

    }

}
