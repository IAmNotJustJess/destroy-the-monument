package IAmNotJustJess.destroyTheMonument.player;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.arenas.ArenaManager;
import IAmNotJustJess.destroyTheMonument.arenas.ArenaSettings;
import IAmNotJustJess.destroyTheMonument.arenas.ArenaState;
import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.configuration.MessagesConfiguration;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClass;
import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.Upgrade;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTreeLocation;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeType;
import IAmNotJustJess.destroyTheMonument.teams.TeamColour;
import IAmNotJustJess.destroyTheMonument.teams.TeamManager;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageSerializers;
import IAmNotJustJess.destroyTheMonument.utility.UpgradeTreeLocationConverter;
import net.kyori.adventure.audience.Audience;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class PlayerCharacter {

    private Player player;
    private int maxHealth;
    private int health;
    private float movementSpeed;
    private PlayerClass chosenPlayerClass;
    private TeamColour team;
    private ArrayList<Effect> effectList;
    private double dealDamageMultiplier = 1;
    private double takeDamageMultiplier = 1;
    private int flatDealDamageIncrease = 0;
    private int flatTakeDamageIncrease = 0;
    private int shards = 0;
    private HashSet<Player> assistList;
    private Player lastAttacked;
    private Instant lastAttackedDate;
    private int maxHealthAfterUpgrades;
    private float movementSpeedAfterUpgrades;
    private int baseMaxHealth;
    private float baseMovementSpeed;
    private long totalDamageDealt;
    private long totalDamageTaken;
    private int totalKills;
    private int totalMonumentsBroken;
    private PlayerClass changeClassTo;
    private boolean changeClassOnRespawn;

    public PlayerCharacter(Player player, PlayerClass chosenPlayerClass, TeamColour team, float movementSpeed) {
        this.player = player;
        this.chosenPlayerClass = chosenPlayerClass;
        this.team = team;
        this.maxHealth = chosenPlayerClass.healthPoints;
        this.health = maxHealth;
        this.baseMaxHealth = maxHealth;
        this.maxHealthAfterUpgrades = maxHealth;
        this.movementSpeed = movementSpeed;
        this.baseMovementSpeed = movementSpeed;
        this.movementSpeedAfterUpgrades = movementSpeed;
        this.effectList = new ArrayList<>();
        this.assistList = new HashSet<>();
        this.shards = 0;
        this.totalDamageDealt = 0;
        this.totalDamageTaken = 0;
        this.totalKills = 0;
        this.totalMonumentsBroken = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public PlayerClass getChosenClass() {
        return chosenPlayerClass;
    }

    public void setChosenClass(PlayerClass chosenPlayerClass) {
        this.chosenPlayerClass = (PlayerClass) chosenPlayerClass.clone();
    }

    public TeamColour getTeam() {
        return team;
    }

    public void setTeam(TeamColour team) {
        this.team = team;
    }

    public ArrayList<Effect> getEffectList() {
        return effectList;
    }

    public void setEffectList(ArrayList<Effect> effectList) {
        this.effectList = effectList;
    }

    public void updatePlayerSpeed() {
        player.setWalkSpeed(movementSpeed / 5);
    }

    public int buyUpgrade(UpgradeTreeLocation location) {

        Upgrade firstUpgrade = chosenPlayerClass.upgradeTree.getUpgrade(location).getFirst();
        ArrayList<Upgrade> upgradeList = chosenPlayerClass.upgradeTree.getUpgrade(location);

        if(firstUpgrade.getCurrentLevel() == firstUpgrade.getMaxLevels()) {
            player.sendMessage(MiniMessageSerializers.deserializeToString(
                            MessagesConfiguration.upgradesMessagesConfiguration.getString("maxed-out")
                                    .replace("<upgrade>", firstUpgrade.getName())
                                    .replace("<maxLevel>", String.valueOf(firstUpgrade.shardPricesPerLevelList.get(firstUpgrade.getMaxLevels()))
                                    )
                    )
            );
            return 1;
        }
        if(shards < firstUpgrade.shardPricesPerLevelList.get(firstUpgrade.getCurrentLevel())) {
            player.sendMessage(MiniMessageSerializers.deserializeToString(
                    MessagesConfiguration.upgradesMessagesConfiguration.getString("insufficient-shards")
                            .replace("<cost>", String.valueOf(firstUpgrade.shardPricesPerLevelList.get(firstUpgrade.getCurrentLevel()))
                            )
                            .replace("<upgrade>", firstUpgrade.getName())
                    )
            );
            return 2;
        }

        shards -= firstUpgrade.shardPricesPerLevelList.get(firstUpgrade.getCurrentLevel());

        player.sendMessage(MiniMessageSerializers.deserializeToString(
                MessagesConfiguration.upgradesMessagesConfiguration.getString("bought")
                        .replace("<cost>", String.valueOf(firstUpgrade.shardPricesPerLevelList.get(firstUpgrade.getCurrentLevel()))
                        )
                        .replace("<totalShards>", String.valueOf((shards)))
                        .replace("<upgrade>", firstUpgrade.getName())
                        .replace("<level>", String.valueOf(firstUpgrade.getCurrentLevel() + 1))
        ));


        firstUpgrade.setCurrentLevel(firstUpgrade.getCurrentLevel() + 1);

        for(Upgrade upgrade : upgradeList) {
            double value = upgrade.strengthPerLevelList.get(firstUpgrade.getCurrentLevel()).getFirst();
            UpgradeType upgradeType = upgrade.getUpgradeType();
            switch(upgrade.getUpgradeAffection()) {

                case MAX_HP -> {
                    switch(upgradeType) {
                        case FLAT -> {
                            maxHealth += (int) value;
                            health += (int) value;
                            baseMaxHealth += (int) value;
                        }
                        case PERCENTAGE -> {
                            maxHealth += (int) (value * baseMaxHealth);
                            health += (int) (value * baseMaxHealth);
                            maxHealthAfterUpgrades += (int) (value * baseMaxHealth);
                        }
                    }
                }

                case MOVEMENT_SPEED -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            movementSpeed += (float) value;
                            baseMovementSpeed += (float) value;
                        }
                        case PERCENTAGE -> {
                            movementSpeed += (float) (value * baseMovementSpeed);
                            movementSpeedAfterUpgrades += (float) (value * baseMovementSpeed);
                        }
                    }
                    updatePlayerSpeed();
                }

                case ULTIMATE_EFFECT_STRENGTH -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.ultimateSkill.effectList.get(integer).strength += value;
                                chosenPlayerClass.ultimateSkill.effectList.get(integer).strengthAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.ultimateSkill.effectList.get(integer).strength += (value * chosenPlayerClass.ultimateSkill.effectList.get(integer).baseStrength);
                                chosenPlayerClass.ultimateSkill.effectList.get(integer).strengthAfterUpgrades += (value * chosenPlayerClass.ultimateSkill.effectList.get(integer).baseStrength);
                            }
                        }
                    }
                }

                case ULTIMATE_EFFECT_LONGEVITY -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.ultimateSkill.effectList.get(integer).longevity += (int) value;
                                chosenPlayerClass.ultimateSkill.effectList.get(integer).longevityAfterUpgrades += (int) value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.ultimateSkill.effectList.get(integer).longevity += (int) (value * chosenPlayerClass.ultimateSkill.effectList.get(integer).baseLongevity);
                                chosenPlayerClass.ultimateSkill.effectList.get(integer).longevityAfterUpgrades += (int) (value * chosenPlayerClass.ultimateSkill.effectList.get(integer).baseLongevity);
                            }
                        }
                    }
                }

                case ULTIMATE_EFFECT_RANGE -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.ultimateSkill.effectList.get(integer).range += value;
                                chosenPlayerClass.ultimateSkill.effectList.get(integer).rangeAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.ultimateSkill.effectList.get(integer).range += value * chosenPlayerClass.ultimateSkill.effectList.get(integer).baseRange;
                                chosenPlayerClass.ultimateSkill.effectList.get(integer).rangeAfterUpgrades += value * chosenPlayerClass.ultimateSkill.effectList.get(integer).baseRange;
                            }
                        }
                    }
                }

                case ULTIMATE_COOLDOWN -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            chosenPlayerClass.ultimateSkill.cooldown += (int) value;
                            chosenPlayerClass.ultimateSkill.cooldownAfterUpgrades += (int) value;
                        }
                        case PERCENTAGE -> {
                            chosenPlayerClass.ultimateSkill.cooldown += (int) (value * chosenPlayerClass.ultimateSkill.baseCooldown);
                            chosenPlayerClass.ultimateSkill.cooldownAfterUpgrades += (int) (value * chosenPlayerClass.ultimateSkill.baseCooldown);
                        }
                    }
                }

                case ULTIMATE_ADD_NEW_EFFECT -> {
                    chosenPlayerClass.ultimateSkill.effectList.addAll(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()));
                }

                case ACTIVE_EFFECT_STRENGTH -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.activeSkill.effectList.get(integer).strength += value;
                                chosenPlayerClass.activeSkill.effectList.get(integer).strengthAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.activeSkill.effectList.get(integer).strength += (value * chosenPlayerClass.activeSkill.effectList.get(integer).baseStrength);
                                chosenPlayerClass.activeSkill.effectList.get(integer).strengthAfterUpgrades += (value * chosenPlayerClass.activeSkill.effectList.get(integer).baseStrength);
                            }
                        }
                    }
                }

                case ACTIVE_EFFECT_LONGEVITY -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.activeSkill.effectList.get(integer).longevity += (int) value;
                                chosenPlayerClass.activeSkill.effectList.get(integer).longevityAfterUpgrades += (int) value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.activeSkill.effectList.get(integer).longevity += (int) (value * chosenPlayerClass.activeSkill.effectList.get(integer).baseLongevity);
                                chosenPlayerClass.activeSkill.effectList.get(integer).longevityAfterUpgrades += (int) (value * chosenPlayerClass.activeSkill.effectList.get(integer).baseLongevity);
                            }
                        }
                    }
                }

                case ACTIVE_EFFECT_RANGE -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.activeSkill.effectList.get(integer).range += value;
                                chosenPlayerClass.activeSkill.effectList.get(integer).rangeAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.activeSkill.effectList.get(integer).range += value * chosenPlayerClass.activeSkill.effectList.get(integer).baseRange;
                                chosenPlayerClass.activeSkill.effectList.get(integer).rangeAfterUpgrades += value * chosenPlayerClass.activeSkill.effectList.get(integer).baseRange;
                            }
                        }
                    }
                }

                case ACTIVE_COOLDOWN -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            chosenPlayerClass.activeSkill.cooldown += (int) value;
                            chosenPlayerClass.activeSkill.cooldownAfterUpgrades += (int) value;
                        }
                        case PERCENTAGE -> {
                            chosenPlayerClass.activeSkill.cooldown += (int) (value * chosenPlayerClass.activeSkill.baseCooldown);
                            chosenPlayerClass.activeSkill.cooldownAfterUpgrades += (int) (value * chosenPlayerClass.activeSkill.baseCooldown);
                        }
                    }
                }

                case ACTIVE_ADD_NEW_EFFECT -> {
                    chosenPlayerClass.activeSkill.effectList.addAll(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()));
                }

                case PASSIVE_EFFECT_STRENGTH -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.passiveSkill.effectList.get(integer).strength += value;
                                chosenPlayerClass.passiveSkill.effectList.get(integer).strengthAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.passiveSkill.effectList.get(integer).strength += value * chosenPlayerClass.passiveSkill.effectList.get(integer).baseStrength;
                                chosenPlayerClass.passiveSkill.effectList.get(integer).strengthAfterUpgrades += value * chosenPlayerClass.passiveSkill.effectList.get(integer).baseStrength;
                            }
                        }
                    }
                }

                case PASSIVE_EFFECT_LONGEVITY -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.passiveSkill.effectList.get(integer).longevity += (int) value;
                                chosenPlayerClass.passiveSkill.effectList.get(integer).longevityAfterUpgrades += (int) value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.passiveSkill.effectList.get(integer).longevity += (int) (value * chosenPlayerClass.passiveSkill.effectList.get(integer).baseLongevity);
                                chosenPlayerClass.passiveSkill.effectList.get(integer).longevityAfterUpgrades += (int) (value * chosenPlayerClass.passiveSkill.effectList.get(integer).baseLongevity);
                            }
                        }
                    }
                }

                case PASSIVE_EFFECT_RANGE -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.passiveSkill.effectList.get(integer).range += value;
                                chosenPlayerClass.passiveSkill.effectList.get(integer).rangeAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.passiveSkill.effectList.get(integer).range += value * chosenPlayerClass.passiveSkill.effectList.get(integer).baseRange;
                                chosenPlayerClass.passiveSkill.effectList.get(integer).rangeAfterUpgrades += value * chosenPlayerClass.passiveSkill.effectList.get(integer).baseRange;
                            }
                        }
                    }
                }

                case PASSIVE_COOLDOWN -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            chosenPlayerClass.passiveSkill.cooldown += (int) value;
                            chosenPlayerClass.passiveSkill.cooldownAfterUpgrades += (int) value;
                        }
                        case PERCENTAGE -> {
                            chosenPlayerClass.passiveSkill.cooldown += (int) (value * chosenPlayerClass.passiveSkill.baseCooldown);
                            chosenPlayerClass.passiveSkill.cooldownAfterUpgrades += (int) (value * chosenPlayerClass.passiveSkill.baseCooldown);
                        }
                    }
                }

                case PASSIVE_ADD_NEW_EFFECT -> {
                    chosenPlayerClass.passiveSkill.effectList.addAll(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()));
                }

                case MAIN_WEAPON_DAMAGE -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            chosenPlayerClass.loadout.mainWeapon.damage += (int) value;
                            chosenPlayerClass.loadout.mainWeapon.damageAfterUpgrades += (int) value;
                        }
                        case PERCENTAGE -> {
                            chosenPlayerClass.loadout.mainWeapon.damage += (int) (value * chosenPlayerClass.loadout.mainWeapon.baseDamage);
                            chosenPlayerClass.loadout.mainWeapon.damageAfterUpgrades += (int) (value * chosenPlayerClass.loadout.mainWeapon.baseDamage);
                        }
                    }
                }

                case MAIN_WEAPON_COOLDOWN -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            chosenPlayerClass.loadout.mainWeapon.cooldown += (int) value;
                            chosenPlayerClass.loadout.mainWeapon.cooldownAfterUpgrades += (int) value;
                        }
                        case PERCENTAGE -> {
                            chosenPlayerClass.loadout.mainWeapon.cooldown += (int) (value * chosenPlayerClass.loadout.mainWeapon.baseCooldown);
                            chosenPlayerClass.loadout.mainWeapon.cooldownAfterUpgrades += (int) (value * chosenPlayerClass.loadout.mainWeapon.baseCooldown);
                        }
                    }
                }

                case MAIN_WEAPON_ADD_NEW_EFFECT -> {
                    for(Effect effect : upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel())) {
                        chosenPlayerClass.loadout.mainWeapon.addEffect(effect, upgrade.effectsSpecialPropertiesPerLevelList.get(upgrade.getCurrentLevel()).getFirst());
                    }
                }

                case MAIN_WEAPON_EFFECT_STRENGTH -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : chosenPlayerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).strength += value;
                                chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).strengthAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : chosenPlayerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).strength += value * chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).baseStrength;
                                chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).strengthAfterUpgrades += value * chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).baseStrength;
                            }
                        }
                    }
                }

                case MAIN_WEAPON_EFFECT_LONGEVITY -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : chosenPlayerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).longevity += (int) value;
                                chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).longevityAfterUpgrades += (int) value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : chosenPlayerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).longevity += (int) (value * chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).baseLongevity);
                                chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).longevityAfterUpgrades += (int) (value * chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).baseLongevity);
                            }
                        }
                    }
                }

                case MAIN_WEAPON_EFFECT_RANGE -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : chosenPlayerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).range += value;
                                chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).rangeAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : chosenPlayerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).range += value * chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).baseRange;
                                chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).rangeAfterUpgrades += (int) (value * chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).baseRange);
                            }
                        }
                    }
                }

                case SECONDARY_WEAPON_DAMAGE -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            chosenPlayerClass.loadout.secondaryWeapon.damage += (int) value;
                            chosenPlayerClass.loadout.secondaryWeapon.damageAfterUpgrades += (int) value;
                        }
                        case PERCENTAGE -> {
                            chosenPlayerClass.loadout.secondaryWeapon.damage += (int) (value * chosenPlayerClass.loadout.secondaryWeapon.baseDamage);
                            chosenPlayerClass.loadout.secondaryWeapon.damageAfterUpgrades += (int) (value * chosenPlayerClass.loadout.secondaryWeapon.baseDamage);
                        }
                    }
                }

                case SECONDARY_WEAPON_COOLDOWN -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            chosenPlayerClass.loadout.secondaryWeapon.cooldown += (int) value;
                            chosenPlayerClass.loadout.secondaryWeapon.cooldownAfterUpgrades += (int) value;
                        }
                        case PERCENTAGE -> {
                            chosenPlayerClass.loadout.secondaryWeapon.cooldown += (int) (value * chosenPlayerClass.loadout.secondaryWeapon.baseCooldown);
                            chosenPlayerClass.loadout.secondaryWeapon.cooldownAfterUpgrades += (int) (value * chosenPlayerClass.loadout.secondaryWeapon.baseCooldown);
                        }
                    }
                }

                case SECONDARY_WEAPON_ADD_NEW_EFFECT -> {
                    for(Effect effect : upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel())) {
                        chosenPlayerClass.loadout.secondaryWeapon.addEffect(effect, upgrade.effectsSpecialPropertiesPerLevelList.get(upgrade.getCurrentLevel()).getFirst());
                    }
                }

                case SECONDARY_WEAPON_EFFECT_STRENGTH -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : chosenPlayerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).strength += value;
                                chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).strengthAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : chosenPlayerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).strength += value * chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).baseStrength;
                                chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).strengthAfterUpgrades += value * chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).baseStrength;
                            }
                        }
                    }
                }

                case SECONDARY_WEAPON_EFFECT_LONGEVITY -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : chosenPlayerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).longevity += (int) value;
                                chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).longevityAfterUpgrades += (int) value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : chosenPlayerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).longevity += (int) (value * chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).baseLongevity);
                                chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).longevityAfterUpgrades += (int) (value * chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).baseLongevity);
                            }
                        }
                    }
                }

                case SECONDARY_WEAPON_EFFECT_RANGE -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : chosenPlayerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).range += value;
                                chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).rangeAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : chosenPlayerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).range += value * chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).baseRange;
                                chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).rangeAfterUpgrades += (int) (value * chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).baseRange);
                            }
                        }
                    }
                }
            }
        }

        return 0;
    }

    public void addShards(int shards, String message) {
        Audience audience = (Audience) player;
        this.shards += shards;
        audience.sendMessage(MiniMessageSerializers.deserializeToComponent(message
                .replace("<award>", String.valueOf(shards))
                .replace("<totalShards>", String.valueOf(this.shards))
        ));
    }

    public void onEnemyKill(Player enemy) {

        this.shards += ArenaSettings.shardsPerKill;
        player.sendMessage(MiniMessageSerializers.deserializeToString(
                MessagesConfiguration.playerMessagesConfiguration.getString("assist-shards")
                        .replace("<player>", enemy.getName())
                        .replace("<award>", String.valueOf(ArenaSettings.shardsPerKill))
                        .replace("<totalShards>", String.valueOf(this.shards))
        ));

        for(int i = 0; i < 8; i++) {
            UpgradeTreeLocation location = UpgradeTreeLocationConverter.convertIntegerToLocation(i);
            ArrayList<Upgrade> upgradeList = chosenPlayerClass.upgradeTree.getUpgrade(location);

            for(Upgrade upgrade : upgradeList) {
                if (upgrade.getCurrentLevel() == 0) continue;
                if (upgrade.getMaxStacks() == upgrade.getStackCount()) continue;

                double value = upgrade.strengthPerLevelList.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount());
                upgrade.setStackCount(upgrade.getStackCount() + 1);
                UpgradeType upgradeType = upgrade.getUpgradeType();

                switch(upgradeType) {
                    case FLAT, PERCENTAGE -> {
                        continue;
                    }
                }

                switch(upgrade.getUpgradeAffection()) {

                    case MAX_HP -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                maxHealth += (int) value;
                                health += (int) value;
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                maxHealth += (int) (value * maxHealthAfterUpgrades);
                                health += (int) (value * maxHealthAfterUpgrades);
                            }
                        }
                    }

                    case MOVEMENT_SPEED -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                movementSpeed += (float) value;
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                movementSpeed += (float) (value * movementSpeedAfterUpgrades);
                            }
                        }
                        updatePlayerSpeed();
                    }

                    case ULTIMATE_EFFECT_STRENGTH -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.ultimateSkill.effectList.get(integer).strength += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.ultimateSkill.effectList.get(integer).strength += (value * chosenPlayerClass.ultimateSkill.effectList.get(integer).baseStrength);
                                }
                            }
                        }
                    }

                    case ULTIMATE_EFFECT_LONGEVITY -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.ultimateSkill.effectList.get(integer).longevity += (int) value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.ultimateSkill.effectList.get(integer).longevity += (int) (value * chosenPlayerClass.ultimateSkill.effectList.get(integer).baseLongevity);
                                }
                            }
                        }
                    }

                    case ULTIMATE_EFFECT_RANGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.ultimateSkill.effectList.get(integer).range += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.ultimateSkill.effectList.get(integer).range += value * chosenPlayerClass.ultimateSkill.effectList.get(integer).baseRange;
                                }
                            }
                        }
                    }

                    case ULTIMATE_COOLDOWN -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                chosenPlayerClass.ultimateSkill.cooldown += (int) value;
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                chosenPlayerClass.ultimateSkill.cooldown += (int) (value * chosenPlayerClass.ultimateSkill.baseCooldown);
                            }
                        }
                    }

                    case ULTIMATE_ADD_NEW_EFFECT -> {
                        chosenPlayerClass.ultimateSkill.effectList.add(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount()));
                    }

                    case ACTIVE_EFFECT_STRENGTH -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.activeSkill.effectList.get(integer).strength += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.activeSkill.effectList.get(integer).strength += (value * chosenPlayerClass.activeSkill.effectList.get(integer).baseStrength);
                                }
                            }
                        }
                    }

                    case ACTIVE_EFFECT_LONGEVITY -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.activeSkill.effectList.get(integer).longevity += (int) value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.activeSkill.effectList.get(integer).longevity += (int) (value * chosenPlayerClass.activeSkill.effectList.get(integer).baseLongevity);
                                }
                            }
                        }
                    }

                    case ACTIVE_EFFECT_RANGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.activeSkill.effectList.get(integer).range += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.activeSkill.effectList.get(integer).range += value * chosenPlayerClass.activeSkill.effectList.get(integer).baseRange;
                                }
                            }
                        }
                    }

                    case ACTIVE_COOLDOWN -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                chosenPlayerClass.activeSkill.cooldown += (int) value;
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                chosenPlayerClass.activeSkill.cooldown += (int) (value * chosenPlayerClass.activeSkill.baseCooldown);
                            }
                        }
                    }

                    case ACTIVE_ADD_NEW_EFFECT -> {
                        chosenPlayerClass.activeSkill.effectList.add(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount()));
                    }

                    case PASSIVE_EFFECT_STRENGTH -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.passiveSkill.effectList.get(integer).strength += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.passiveSkill.effectList.get(integer).strength += value * chosenPlayerClass.passiveSkill.effectList.get(integer).baseStrength;
                                }
                            }
                        }
                    }

                    case PASSIVE_EFFECT_LONGEVITY -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.passiveSkill.effectList.get(integer).longevity += (int) value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.passiveSkill.effectList.get(integer).longevity += (int) (value * chosenPlayerClass.passiveSkill.effectList.get(integer).baseLongevity);
                                }
                            }
                        }
                    }

                    case PASSIVE_EFFECT_RANGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.passiveSkill.effectList.get(integer).range += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.passiveSkill.effectList.get(integer).range += value * chosenPlayerClass.passiveSkill.effectList.get(integer).baseRange;
                                }
                            }
                        }
                    }

                    case PASSIVE_COOLDOWN -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                chosenPlayerClass.passiveSkill.cooldown += (int) value;
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                chosenPlayerClass.passiveSkill.cooldown += (int) (value * chosenPlayerClass.passiveSkill.baseCooldown);
                            }
                        }
                    }

                    case PASSIVE_ADD_NEW_EFFECT -> {
                        chosenPlayerClass.passiveSkill.effectList.add(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount()));
                    }

                    case MAIN_WEAPON_DAMAGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                chosenPlayerClass.loadout.mainWeapon.damage += (int) value;
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                chosenPlayerClass.loadout.mainWeapon.damage += (int) (value * chosenPlayerClass.loadout.mainWeapon.baseDamage);
                            }
                        }
                    }

                    case MAIN_WEAPON_COOLDOWN -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                chosenPlayerClass.loadout.mainWeapon.cooldown += (int) value;
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                chosenPlayerClass.loadout.mainWeapon.cooldown += (int) (value * chosenPlayerClass.loadout.mainWeapon.baseCooldown);
                            }
                        }
                    }

                    case MAIN_WEAPON_ADD_NEW_EFFECT -> {
                        chosenPlayerClass.loadout.mainWeapon.addEffect(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount()), upgrade.effectsSpecialPropertiesPerLevelList.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount()));
                    }

                    case MAIN_WEAPON_EFFECT_STRENGTH -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).strength += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).strength += value * chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).baseStrength;
                                }
                            }
                        }
                    }

                    case MAIN_WEAPON_EFFECT_LONGEVITY -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).longevity += (int) value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).longevity += (int) (value * chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).baseLongevity);
                                }
                            }
                        }
                    }

                    case MAIN_WEAPON_EFFECT_RANGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).range += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).range += value * chosenPlayerClass.loadout.mainWeapon.effectList.get(integer).baseRange;
                                }
                            }
                        }
                    }

                    case SECONDARY_WEAPON_DAMAGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                chosenPlayerClass.loadout.secondaryWeapon.damage += (int) value;
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                chosenPlayerClass.loadout.secondaryWeapon.damage += (int) (value * chosenPlayerClass.loadout.secondaryWeapon.baseDamage);
                            }
                        }
                    }

                    case SECONDARY_WEAPON_COOLDOWN -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                chosenPlayerClass.loadout.secondaryWeapon.cooldown += (int) value;
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                chosenPlayerClass.loadout.secondaryWeapon.cooldown += (int) (value * chosenPlayerClass.loadout.secondaryWeapon.baseCooldown);
                            }
                        }
                    }

                    case SECONDARY_WEAPON_ADD_NEW_EFFECT -> {
                        chosenPlayerClass.loadout.secondaryWeapon.addEffect(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount()), upgrade.effectsSpecialPropertiesPerLevelList.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount()));
                    }

                    case SECONDARY_WEAPON_EFFECT_STRENGTH -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).strength += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).strength += value * chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).baseStrength;
                                }
                            }
                        }
                    }

                    case SECONDARY_WEAPON_EFFECT_LONGEVITY -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).longevity += (int) value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).longevity += (int) (value * chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).baseLongevity);
                                }
                            }
                        }
                    }

                    case SECONDARY_WEAPON_EFFECT_RANGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).range += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).range += value * chosenPlayerClass.loadout.secondaryWeapon.effectList.get(integer).baseRange;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void onAssist(Player enemy) {

        this.shards += ArenaSettings.shardsPerAssist;
        player.sendMessage(MiniMessageSerializers.deserializeToString(
                MessagesConfiguration.playerMessagesConfiguration.getString("assist-shards")
                        .replace("<player>", enemy.getName())
                        .replace("<award>", String.valueOf(ArenaSettings.shardsPerAssist))
                        .replace("<totalShards>", String.valueOf(this.shards))
        ));
    }

    public void onRespawnLogin() {

        chosenPlayerClass.loadout.mainWeapon.effectList.removeIf(effect -> effect.removeOnDeath);
        chosenPlayerClass.loadout.secondaryWeapon.effectList.removeIf(effect -> effect.removeOnDeath);
        chosenPlayerClass.ultimateSkill.effectList.removeIf(effect -> effect.removeOnDeath);
        chosenPlayerClass.activeSkill.effectList.removeIf(effect -> effect.removeOnDeath);
        chosenPlayerClass.passiveSkill.effectList.removeIf(effect -> effect.removeOnDeath);

        chosenPlayerClass.loadout.mainWeapon.cooldown = chosenPlayerClass.loadout.mainWeapon.cooldownAfterUpgrades;
        chosenPlayerClass.loadout.mainWeapon.damage = chosenPlayerClass.loadout.mainWeapon.damageAfterUpgrades;
        chosenPlayerClass.loadout.secondaryWeapon.cooldown = chosenPlayerClass.loadout.secondaryWeapon.cooldownAfterUpgrades;
        chosenPlayerClass.loadout.secondaryWeapon.damage = chosenPlayerClass.loadout.secondaryWeapon.damageAfterUpgrades;

        maxHealth = maxHealthAfterUpgrades;
        movementSpeed = movementSpeedAfterUpgrades;
        updatePlayerSpeed();

        chosenPlayerClass.passiveSkill.cooldown = chosenPlayerClass.passiveSkill.cooldownAfterUpgrades;
        for(Effect effect : chosenPlayerClass.passiveSkill.effectList) {
            effect.longevity = effect.longevityAfterUpgrades;
            effect.strength = effect.strengthAfterUpgrades;
            effect.range = effect.rangeAfterUpgrades;
        }

        chosenPlayerClass.activeSkill.cooldown = chosenPlayerClass.activeSkill.cooldownAfterUpgrades;
        for(Effect effect : chosenPlayerClass.activeSkill.effectList) {
            effect.longevity = effect.longevityAfterUpgrades;
            effect.strength = effect.strengthAfterUpgrades;
            effect.range = effect.rangeAfterUpgrades;
        }

        chosenPlayerClass.ultimateSkill.cooldown = chosenPlayerClass.ultimateSkill.cooldownAfterUpgrades;
        for(Effect effect : chosenPlayerClass.ultimateSkill.effectList) {
            effect.longevity = effect.longevityAfterUpgrades;
            effect.strength = effect.strengthAfterUpgrades;
            effect.range = effect.rangeAfterUpgrades;
        }

        for(int i = 0; i < 8; i++) {
            UpgradeTreeLocation location = UpgradeTreeLocationConverter.convertIntegerToLocation(i);
            ArrayList<Upgrade> upgradeList = chosenPlayerClass.upgradeTree.getUpgrade(location);
            for(Upgrade upgrade : upgradeList) {
                if(upgrade.getCurrentLevel() == 0) continue;
                if(upgrade.getUpgradeType() != UpgradeType.STACKING_FLAT_PER_KILL || upgrade.getUpgradeType() != UpgradeType.STACKING_PERCENTAGE_PER_KILL) continue;
                upgrade.setStackCount(0);
            }
        }

        if(changeClassOnRespawn) {
            for(int i = 0; i < 8; i++) {
                UpgradeTreeLocation upgradeTreeLocation = UpgradeTreeLocationConverter.convertIntegerToLocation(i);
                for(int j = 0; j < chosenPlayerClass.upgradeTree.getUpgrade(upgradeTreeLocation).getFirst().getCurrentLevel(); j++) {
                    shards += chosenPlayerClass.upgradeTree.getUpgrade(upgradeTreeLocation).getFirst().shardPricesPerLevelList.get(j);
                }
            }

            chosenPlayerClass = (PlayerClass) changeClassTo.clone();
            changeClassOnRespawn = false;
        }

        player.setGameMode(GameMode.SURVIVAL);

        ArenaManager.arenaList.get(ArenaManager.playerArenaIdList.get(player)).teleportPlayerToArena(player);

    }

    public void readThroughEffectList() {

        for(Effect effect : this.getEffectList()) {

            if(effect.longevity == -1) continue;

            effect.longevity -= 1;

            if(effect.longevity % effect.tickEveryServerTicks == 0) {
                switch (effect.effectType) {
                    case HEAL_OVER_TIME_FLAT -> {
                        this.heal((int) effect.strength);
                    }
                    case HEAL_OVER_TIME_PERCENTAGE -> {
                        this.heal((int) effect.strength * this.maxHealth);
                    }
                    case DAMAGE_OVER_TIME_FLAT -> {
                        this.dealDamage((int) effect.strength);
                        getPlayer().damage(0.0);
                    }
                    case DAMAGE_OVER_TIME_PERCENTAGE -> {
                        this.dealDamage((int) effect.strength * this.maxHealth);
                        getPlayer().damage(0.0);
                    }
                    default -> {
                    }
                }
            }

            if(effect.longevity < 1) {
                this.getEffectList().remove(effect);
                this.checkForMultiplierChange();
            }

        }
    }

    public void checkForMultiplierChange() {

        this.dealDamageMultiplier = 1;
        this.takeDamageMultiplier = 1;
        this.flatDealDamageIncrease = 0;
        this.flatTakeDamageIncrease = 0;

        for(Effect effect : this.getEffectList()) {
            switch(effect.effectType) {
                case DAMAGE_INCREASE_FLAT -> {
                    this.flatDealDamageIncrease += (int) effect.strength;
                }
                case DAMAGE_VULNERABILITY_FLAT -> {
                    this.flatTakeDamageIncrease += (int) effect.strength;
                }
                case DAMAGE_INCREASE_MULTIPLIER -> {
                    this.dealDamageMultiplier *= effect.strength;
                }
                case DAMAGE_VULNERABILITY_MULTIPLIER -> {
                    this.takeDamageMultiplier *= effect.strength;
                }
                default -> {
                    break;
                }
            }
        }
    }

    public HashSet<Player> getAssistList() {
        return assistList;
    }

    public void setAssistList(HashSet<Player> assistList) {
        this.assistList = assistList;
    }

    public Player getLastAttacked() {
        return lastAttacked;
    }

    public void setLastAttacked(PlayerCharacter lastAttacked) {
        if(lastAttacked.getTeam() == this.getTeam()) return;
        if(lastAttacked.getLastAttacked() != null) assistList.add(lastAttacked.getPlayer());
        assistList.remove(lastAttacked.getPlayer());
        this.lastAttacked = lastAttacked.getPlayer();
        this.lastAttackedDate = Instant.now();
    }

    public void dealDamage(int damageAmount) {
        damageAmount = (int) Math.round((damageAmount + this.flatTakeDamageIncrease) * this.takeDamageMultiplier);
        this.setHealth(this.getHealth() - damageAmount);
        if(this.getHealth() <= 0) this.kill();
    }

    public void heal(int healAmount) {
        this.setHealth(this.getHealth() + healAmount);
        if(this.getHealth() >= this.getMaxHealth()) this.setHealth(this.getMaxHealth());
    }

    public void kill() {
        player.setGameMode(GameMode.SPECTATOR);

        String message;

        if(lastAttackedDate.until(Instant.now(), ChronoUnit.SECONDS) > 15) {
            assistList.add(lastAttacked);
            lastAttacked = null;
            lastAttackedDate = null;
        }
        if(!Objects.isNull(lastAttacked)) {
            PlayerCharacter lastAttackedPlayer = PlayerCharacterManager.getList().get(lastAttacked);
            lastAttackedPlayer.onEnemyKill(player);
            message = MessagesConfiguration.arenaMessagesConfiguration.getString("player-killed-message");
        }
        else {
            message = MessagesConfiguration.arenaMessagesConfiguration.getString("player-died");
        }

        message = message.replace("<teamColour>", TeamManager.list.get(team).textColour)
                .replace("<player>", player.getDisplayName())
                .replace("<enemyTeamColour>", lastAttacked.getDisplayName())
                .replace("<enemyPlayer>", TeamManager.list.get(PlayerCharacterManager.getList().get(lastAttacked).team).textColour);

        ArenaManager.arenaList.get(ArenaManager.playerArenaIdList.get(player)).sendMessageGlobally(message);

        for(Player player : getAssistList()) {
            PlayerCharacterManager.getList().get(player).onAssist(player);
        }

        setAssistList(new HashSet<>());

        if(!effectList.isEmpty()) effectList.clear();

        int seconds = MainConfiguration.globalGameRulesConfiguration.getInt("player-respawn-interval");
        String title = MiniMessageSerializers.deserializeToString(MessagesConfiguration.arenaMessagesConfiguration.getString("player-killed-title"));
        String subtitle = MessagesConfiguration.arenaMessagesConfiguration.getString("player-killed-subtitle");
        for(int i = seconds; i >= 0; i--) {
            String timerSubtitle = subtitle.replace("<phase>", Double.toString((double) i / (seconds))
                .replace("<seconds>", Integer.toString(i)));
            int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(ArenaManager.arenaList.get(ArenaManager.playerArenaIdList.get(player)).getArenaState().equals(ArenaState.RUNNING)) {
                        player.sendTitle(title, MiniMessageSerializers.deserializeToString(timerSubtitle), 0, 25, 0);
                    } else if (finalI == 0) {
                        onRespawnLogin();
                    }
                }
            }.runTaskLaterAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), (seconds - i) * 20L);
        }
    }

    public void changeClass(PlayerClass playerClass) {

        if(Objects.equals(chosenPlayerClass.name, playerClass.name)) {
            //send message cancelling
            return;
        }
        changeClassTo = playerClass;
        changeClassOnRespawn = true;

    }

    public int getShards() {
        return shards;
    }

    public long getTotalDamageDealt() {
        return totalDamageDealt;
    }

    public void setTotalDamageDealt(long totalDamageDealt) {
        this.totalDamageDealt = totalDamageDealt;
    }

    public long getTotalDamageTaken() {
        return totalDamageTaken;
    }

    public void setTotalDamageTaken(long totalDamageTaken) {
        this.totalDamageTaken = totalDamageTaken;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public int getTotalMonumentsBroken() {
        return totalMonumentsBroken;
    }

    public void setTotalMonumentsBroken(int totalMonumentsBroken) {
        this.totalMonumentsBroken = totalMonumentsBroken;
    }

    public int getFlatDealDamageIncrease() {
        return flatDealDamageIncrease;
    }

    public double getDealDamageMultiplier() {
        return dealDamageMultiplier;
    }
}
