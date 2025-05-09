package IAmNotJustJess.destroyTheMonument.player.classes.skills;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterList;
import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;

import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTreeLocation;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageParser;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Skill {

    private String name;
    private String description;
    private ArrayList<String> descriptionTextReplacementList;
    public SkillType type;
    public boolean active;
    public double cooldown;
    public double baseCooldown;
    public double baseCooldownBeforeStacks;
    public ArrayList<Effect> effectList;
    public HashMap<UpgradeTreeLocation, ArrayList<Integer>> upgradeAffectingEffect;

    Skill(String name, String description, ArrayList<String> descriptionTextReplacementList, SkillType type, double cooldown, Effect effect) {

        this.name = name;
        this.description = description;
        this.descriptionTextReplacementList = descriptionTextReplacementList;
        this.type = type;
        this.cooldown = cooldown;
        this.baseCooldown = cooldown;
        this.baseCooldownBeforeStacks = cooldown;
        this.effectList = new ArrayList<>();
        this.upgradeAffectingEffect = new HashMap<>();
        this.active = false;
        this.effectList.add(effect);

    }

    public void useSkill(PlayerCharacter caster, Location location) {
        active = true;
        if (Objects.requireNonNull(type) == SkillType.ACTIVE) {
            Activate(caster, location);
        }
    }

    public void Activate(PlayerCharacter caster, Location location) {

        active = false;
        for(Effect effect : effectList) {

            new BukkitRunnable() {
                @Override
                public void run() {
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
            }.runTaskLaterAsynchronously(JavaPlugin.getProvidingPlugin(DestroyTheMonument.class), effect.delay);


        }
    }

    private void ActivateAbilityOnSelf(PlayerCharacter caster, Effect effect) {
        ActivateAbilityOnPlayer(caster, caster, effect);
    }
    private void ActivateAbilityOnEnemies(PlayerCharacter caster, Effect effect, Location location) {
        for (Entity entity : Objects.requireNonNull(location.getWorld()).getNearbyEntities(location, effect.range, effect.range, effect.range)) {

            if (!(entity instanceof Player)) return;
            PlayerCharacter loopedPlayer = PlayerCharacterList.getList().get(entity);
            loopedPlayer.setLastAttacked(caster.getPlayer());

            if(caster.getTeam() == loopedPlayer.getTeam()) return;

            ActivateAbilityOnPlayer(caster, loopedPlayer, effect);
        }
    }
    private void ActivateAbilityOnTeammates(PlayerCharacter caster, Effect effect, Location location) {
        for (Entity entity : Objects.requireNonNull(location.getWorld()).getNearbyEntities(location, effect.range, effect.range, effect.range)) {

            if (!(entity instanceof Player)) return;
            PlayerCharacter loopedPlayer = PlayerCharacterList.getList().get(entity);

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

    public List<String> getDescription() {
        String string = description;
        for(int i = 0; i < descriptionTextReplacementList.size(); i++) {
            string = string.replaceAll("<"+i+">", descriptionTextReplacementList.get(i));
        }
        return MiniMessageParser.DeserializeMultiline(string);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return MiniMessageParser.Deserialize(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getDescriptionTextReplacementList() {
        return descriptionTextReplacementList;
    }

    public void setDescriptionTextReplacementList(ArrayList<String> descriptionTextReplacementList) {
        this.descriptionTextReplacementList = descriptionTextReplacementList;
    }
}
