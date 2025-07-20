package IAmNotJustJess.destroyTheMonument.player;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.arenas.ArenaManager;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class PlayerCharacterListener implements Listener {

    @EventHandler
    public void onPlayerTakenDamage(EntityDamageByEntityEvent event) {
        if ((!(event.getDamager() instanceof Player damager) || !(event.getDamager() instanceof Projectile projectile)) || !(event.getEntity() instanceof Player victim)) return;

        if((PlayerCharacterManager.getPlayerTeam(damager) == PlayerCharacterManager.getPlayerTeam(victim)) ||
            (PlayerCharacterManager.getPlayerTeam((Player) projectile.getShooter()) == PlayerCharacterManager.getPlayerTeam(victim))) {
            event.setCancelled(true);
            return;
        }

        PlayerCharacter victimCharacter = PlayerCharacterManager.getList().get(victim);

        int damage;
        Plugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        switch(event.getCause()) {
            case PROJECTILE -> {
                if(!(ArenaManager.playerArenaIdList.containsKey((Player) projectile.getShooter()) || !(ArenaManager.playerArenaIdList.containsKey((victim))))) return;
                damage = Objects.requireNonNull(projectile.getPersistentDataContainer().get(new NamespacedKey(plugin, "projectileDamage"), PersistentDataType.INTEGER));
                event.setDamage(0.0);
                victimCharacter.setLastAttacked(PlayerCharacterManager.getList().get((Player) projectile.getShooter()));
                victimCharacter.dealDamage(damage);
            }
            case ENTITY_ATTACK -> {
                if (!(ArenaManager.playerArenaIdList.containsKey(damager) || !(ArenaManager.playerArenaIdList.containsKey((victim))))) return;
                damage = Objects.requireNonNull(Objects.requireNonNull(damager.getInventory().getItemInMainHand().getItemMeta()).getPersistentDataContainer().get(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER));
                event.setDamage(0.0);
                victimCharacter.setLastAttacked(PlayerCharacterManager.getList().get(damager));
                victimCharacter.dealDamage(damage);
            }
            case ENTITY_SWEEP_ATTACK -> {
                if (!(ArenaManager.playerArenaIdList.containsKey(damager) || !(ArenaManager.playerArenaIdList.containsKey((victim))))) return;
                damage = Objects.requireNonNull(Objects.requireNonNull(damager.getInventory().getItemInMainHand().getItemMeta()).getPersistentDataContainer().get(new NamespacedKey(plugin, "damage"), PersistentDataType.INTEGER));
                event.setDamage(0.0);
                victimCharacter.setLastAttacked(PlayerCharacterManager.getList().get(damager));
                victimCharacter.dealDamage(damage / 5);
            }
        }
    }

    @EventHandler
    public void onAbilityUse(PlayerInteractEvent event) {

        if(!PlayerCharacterManager.getList().containsKey(event.getPlayer())) return;

        PlayerCharacter playerCharacter = PlayerCharacterManager.getList().get(event.getPlayer());
        Plugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        if (event.getItem() == null) return;

        String abilityType = Objects.requireNonNull(event.getItem().getItemMeta()).getPersistentDataContainer().get(new NamespacedKey(plugin, "abilityType"), PersistentDataType.STRING);

        switch (abilityType) {
            case "active" -> {
                playerCharacter.getChosenClass().activeSkill.useSkill(playerCharacter, event.getPlayer().getLocation());
            }
            case "ultimate" -> {
                playerCharacter.getChosenClass().ultimateSkill.useSkill(playerCharacter, event.getPlayer().getLocation());
            }
            case null, default -> {}
        }
    }

    @EventHandler
    public void onProjectileAbilityUse(ProjectileHitEvent event) {

        if(!(event.getEntity().getShooter() instanceof Player player)) return;
        if(!PlayerCharacterManager.getList().containsKey(player)) return;

        Location location;

        if(!Objects.isNull(event.getHitBlock())) location = event.getHitBlock().getLocation();
        else if(!Objects.isNull(event.getHitEntity())) location = event.getHitEntity().getLocation();
        else location = event.getEntity().getLocation();

        PlayerCharacter playerCharacter = PlayerCharacterManager.getList().get(player);
        Plugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        String abilityType = Objects.requireNonNull(event.getEntity().getPersistentDataContainer().get(new NamespacedKey(plugin, "abilityType"), PersistentDataType.STRING));

        switch (abilityType) {
            case "active" -> {
                playerCharacter.getChosenClass().activeSkill.useSkill(playerCharacter, location);
            }
            case "ultimate" -> {
                playerCharacter.getChosenClass().ultimateSkill.useSkill(playerCharacter, location);
            }
        }

    }
}
