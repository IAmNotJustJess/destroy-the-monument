package IAmNotJustJess.destroyTheMonument.player.classes.skills;

import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterList;
import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Skill {

    public String name;
    public SkillType type;
    public double cooldown;
    public ArrayList<Effect> effectList;

    Skill(String name, SkillType type, double cooldown, Effect effect) {

        this.name = name;
        this.type = type;
        this.cooldown = cooldown;
        this.effectList = new ArrayList<Effect>();
        this.effectList.add(effect);

    }

    public void Activate(PlayerCharacter caster, PlayerCharacter attacker, Location location) {

        for(Effect effect : effectList) {
            switch (effect.effectApplicationType) {
                case APPLY_SELF -> {
                    ActivateAbilityOnSelf(caster, effect);
                }
                case APPLY_ENEMIES_IN_RANGE -> {
                    ActivateAbilityOnEnemies(caster, effect, location);
                }
                case APPLY_ENEMIES_IN_RANGE_OF_CASTER -> {
                    ActivateAbilityOnEnemies(caster, effect, caster.getPlayer().getLocation());
                }
                case APPLY_TEAMMATES_IN_RANGE -> {
                    ActivateAbilityOnTeammates(caster, effect, location);
                }
                case APPLY_TEAMMATES_IN_RANGE_OF_CASTER -> {
                    ActivateAbilityOnTeammates(caster, effect, caster.getPlayer().getLocation());
                }
            }
        }
    }

    private void ActivateAbilityOnSelf(PlayerCharacter caster, Effect effect) {
        ActivateAbilityOnPlayer(caster, caster, effect);
    }
    private void ActivateAbilityOnEnemies(PlayerCharacter caster, Effect effect, Location location) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, effect.range, effect.range, effect.range)) {

            if (!(entity instanceof LivingEntity) || !(entity instanceof Player)) return;
            PlayerCharacter loopedPlayer = PlayerCharacterList.getList().get(entity.getUniqueId());
            loopedPlayer.setLastAttacked(caster.getPlayer());

            if(caster.getTeam() == loopedPlayer.getTeam()) return;

            ActivateAbilityOnPlayer(caster, loopedPlayer, effect);
        }
    }
    private void ActivateAbilityOnTeammates(PlayerCharacter caster, Effect effect, Location location) {
        for (Entity entity : location.getWorld().getNearbyEntities(location, effect.range, effect.range, effect.range)) {

            if (!(entity instanceof Player)) return;
            PlayerCharacter loopedPlayer = PlayerCharacterList.getList().get(entity.getUniqueId());

            if(caster.getTeam() != loopedPlayer.getTeam()) return;

            ActivateAbilityOnPlayer(caster, loopedPlayer, effect);
        }
    }
    private void ActivateAbilityOnPlayer(PlayerCharacter caster, PlayerCharacter affectedPlayer, Effect effect) {
        switch (effect.effectType) {
            case PUSH_UP -> {
                caster.getPlayer().getVelocity().add(new Vector(0, 1, 0).normalize().multiply(effect.strength));
            }
            case PUSH_AWAY -> {
                Vector vector = caster.getPlayer().getLocation().toVector().subtract(affectedPlayer.getPlayer().getLocation().toVector()).normalize().multiply(effect.strength);
                affectedPlayer.getPlayer().getVelocity().add(vector);
            }
            case HEAL_FLAT -> {
                affectedPlayer.heal((int) effect.strength);
            }
            case HEAL_PERCENTAGE -> {
                affectedPlayer.heal((int) (affectedPlayer.getMaxHealth() * effect.strength));
            }
            case POTION_EFFECT -> {
                affectedPlayer.getPlayer().addPotionEffect(effect.potionEffect);
            }
            case DEAL_DAMAGE_FLAT -> {
                affectedPlayer.dealDamage((int) effect.strength);
            }
            case DEAL_DAMAGE_PERCENTAGE -> {
                affectedPlayer.dealDamage((int) (affectedPlayer.getMaxHealth() * effect.strength));
            }
            default -> {
                affectedPlayer.getEffectList().add(effect);
                affectedPlayer.checkForMultiplierChange();
            }
        }
    }
}
