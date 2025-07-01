package IAmNotJustJess.destroyTheMonument.player.classes.effects;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterManager;
import IAmNotJustJess.destroyTheMonument.utility.EffectSerializers;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Effect {

    public EffectType effectType;
    public EffectApplicationType effectApplicationType;
    public double strength;
    public double range;
    public int longevity;
    public int tickEveryServerTicks;
    public PotionEffect potionEffect;
    public long delay;
    public double rangeAfterUpgrades;
    public double baseRange;
    public double strengthAfterUpgrades;
    public double baseStrength;
    public int longevityAfterUpgrades;
    public int baseLongevity;
    public boolean removeOnDeath;
    public String soundSerliaziedString;
    public String particleSerializedString;
    public EffectParticleSpawnLocation particleSpawnLocation;

    public Effect(EffectType effectType, EffectApplicationType effectApplicationType,
                  double strength, double range, int tickEveryServerTicks, int longevity, long delay, boolean removeOnDeath,
                  String soundSerliaziedString, String particleSerializedString, EffectParticleSpawnLocation particleSpawnLocation) {
        this.effectType = effectType;
        this.effectApplicationType = effectApplicationType;
        this.longevity = longevity;
        this.longevityAfterUpgrades = longevity;
        this.baseLongevity = longevity;
        this.strength = strength;
        this.strengthAfterUpgrades = strength;
        this.baseStrength = strength;
        this.range = range;
        this.rangeAfterUpgrades = range;
        this.baseRange = range;
        this.tickEveryServerTicks = tickEveryServerTicks;
        this.removeOnDeath = removeOnDeath;
        this.delay = delay;
        this.soundSerliaziedString = soundSerliaziedString;
        this.particleSerializedString = particleSerializedString;
        this.particleSpawnLocation = particleSpawnLocation;
        this.potionEffect = null;
    }

    Effect(EffectType effectType, EffectApplicationType effectApplicationType,
           double strength, double range, int tickEveryServerTicks, int longevity, long delay, boolean removeOnDeath,
           String soundSerliaziedString, String particleSerializedString, EffectParticleSpawnLocation particleSpawnLocation,
           PotionEffect potionEffect) {
        this.effectType = effectType;
        this.effectApplicationType = effectApplicationType;
        this.longevity = longevity;
        this.longevityAfterUpgrades = longevity;
        this.baseLongevity = longevity;
        this.strength = strength;
        this.strengthAfterUpgrades = strength;
        this.baseStrength = strength;
        this.range = range;
        this.rangeAfterUpgrades = range;
        this.baseRange = range;
        this.tickEveryServerTicks = tickEveryServerTicks;
        this.removeOnDeath = removeOnDeath;
        this.delay = delay;
        this.soundSerliaziedString = soundSerliaziedString;
        this.particleSerializedString = particleSerializedString;
        this.particleSpawnLocation = particleSpawnLocation;
        this.potionEffect = potionEffect;
    }

    public void activate(PlayerCharacter caster, Location location) {

        new BukkitRunnable() {
            @Override
            public void run() {
                EffectSerializers.soundDeserialize(soundSerliaziedString, caster.getPlayer().getLocation());
                switch(particleSpawnLocation) {
                    case USER -> {
                        EffectSerializers.particleDeserialize(particleSerializedString, caster.getPlayer().getLocation());
                    }
                    case LOCATION -> {
                        EffectSerializers.particleDeserialize(particleSerializedString, location);
                    }
                }
                switch (effectApplicationType) {
                    case APPLY_SELF -> {
                        activateAbilityOnSelf(caster);
                    }
                    case APPLY_ENEMIES_IN_RANGE -> {
                        activateAbilityOnEnemies(caster, location);
                    }
                    case APPLY_ENEMIES_IN_RANGE_OF_CASTER -> {
                        activateAbilityOnEnemies(caster, caster.getPlayer().getLocation());
                    }
                    case APPLY_TEAMMATES_IN_RANGE -> {
                        activateAbilityOnTeammates(caster, location);
                    }
                    case APPLY_TEAMMATES_IN_RANGE_OF_CASTER -> {
                        activateAbilityOnTeammates(caster, caster.getPlayer().getLocation());
                    }
                }
            }
        }.runTaskLaterAsynchronously(JavaPlugin.getProvidingPlugin(DestroyTheMonument.class), delay);
    }

    private void activateAbilityOnSelf(PlayerCharacter caster) {
        activateAbilityOnPlayer(caster, caster);
    }

    private void activateAbilityOnEnemies(PlayerCharacter caster, Location location) {
        for (Entity entity : Objects.requireNonNull(location.getWorld()).getNearbyEntities(location, range, range, range)) {

            if (!(entity instanceof Player)) continue;

            PlayerCharacter loopedPlayer = PlayerCharacterManager.getList().get(entity);

            if(caster.getTeam() == loopedPlayer.getTeam()) continue;

            loopedPlayer.setLastAttacked(caster);
            if(particleSpawnLocation == EffectParticleSpawnLocation.AFFECTED) {
                EffectSerializers.particleDeserialize(particleSerializedString, loopedPlayer.getPlayer().getLocation());
            }

            activateAbilityOnPlayer(caster, loopedPlayer);
        }
    }

    private void activateAbilityOnTeammates(PlayerCharacter caster, Location location) {
        for (Entity entity : Objects.requireNonNull(location.getWorld()).getNearbyEntities(location, range, range, range)) {

            if (!(entity instanceof Player)) continue;

            PlayerCharacter loopedPlayer = PlayerCharacterManager.getList().get(entity);

            if(caster.getTeam() != loopedPlayer.getTeam()) continue;

            if(particleSpawnLocation == EffectParticleSpawnLocation.AFFECTED) {
                EffectSerializers.particleDeserialize(particleSerializedString, loopedPlayer.getPlayer().getLocation());
            }

            activateAbilityOnPlayer(caster, loopedPlayer);
        }
    }

    private void activateAbilityOnPlayer(PlayerCharacter caster, PlayerCharacter affectedPlayer) {
        switch (effectType) {
            case PUSH_UP -> {
                affectedPlayer.getPlayer().getVelocity().add(new Vector(0, 1, 0).normalize().multiply(strength));
            }
            case PUSH_AWAY -> {
                Vector vector = caster.getPlayer().getLocation().toVector().subtract(affectedPlayer.getPlayer().getLocation().toVector()).normalize().multiply(strength);
                affectedPlayer.getPlayer().getVelocity().add(vector);
            }
            case HEAL_FLAT -> {
                affectedPlayer.heal((int) strength);
            }
            case HEAL_PERCENTAGE -> {
                affectedPlayer.heal((int) (affectedPlayer.getMaxHealth() * strength));
            }
            case POTION_EFFECT -> {
                affectedPlayer.getPlayer().addPotionEffect(potionEffect);
            }
            case DEAL_DAMAGE_FLAT -> {
                affectedPlayer.dealDamage((int) strength);
                affectedPlayer.getPlayer().damage(0.0, caster.getPlayer());
            }
            case DEAL_DAMAGE_PERCENTAGE -> {
                affectedPlayer.dealDamage((int) (affectedPlayer.getMaxHealth() * strength));
                affectedPlayer.getPlayer().damage(0.0, caster.getPlayer());
            }
            default -> {
                affectedPlayer.getEffectList().add(this);
                affectedPlayer.checkForMultiplierChange();
            }
        }
    }
}
