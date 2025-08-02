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
import org.bukkit.inventory.ItemStack;
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
    private PlayerClass playerClass;
    private TeamColour team;
    private ArrayList<Effect> effectList;
    private double dealDamageMultiplier = 1;
    private double takeDamageMultiplier = 1;
    private int flatDealDamageIncrease = 0;
    private int flatTakeDamageIncrease = 0;
    private int shards;
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

    public PlayerCharacter(Player player, PlayerClass playerClass, TeamColour team, float movementSpeed) {
        this.player = player;
        this.playerClass = playerClass;
        this.team = team;
        this.maxHealth = playerClass.healthPoints;
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
        return playerClass;
    }

    public void setChosenClass(PlayerClass chosenPlayerClass) {
        this.playerClass = chosenPlayerClass.clone();
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

    public void obtainLoadout() {

        player.getInventory().clear();

        player.getInventory().setHelmet(playerClass.loadout.helmet);
        player.getInventory().setChestplate(playerClass.loadout.chestplate);
        player.getInventory().setLeggings(playerClass.loadout.leggings);
        player.getInventory().setBoots(playerClass.loadout.boots);

        player.getInventory().setItem(0, playerClass.loadout.mainWeapon.generateItem());
        player.getInventory().setItem(1, playerClass.loadout.secondaryWeapon.generateItem());

        player.getInventory().setItem(2, new ItemStack(TeamManager.list.get(team).blockType, playerClass.loadout.blockAmount));
        player.getInventory().setItem(3, playerClass.activeSkill.generateItem());
        player.getInventory().setItem(4, playerClass.passiveSkill.generateItem());

        int i = 5;
        for(ItemStack itemStack : playerClass.loadout.additionalItems) {
            player.getInventory().setItem(i, itemStack);
            i++;
        }

    }

    public void buyUpgrade(UpgradeTreeLocation location) {

        Upgrade firstUpgrade = playerClass.upgradeTree.getUpgrade(location).getFirst();
        ArrayList<Upgrade> upgradeList = playerClass.upgradeTree.getUpgrade(location);

        if(firstUpgrade.getCurrentLevel() == firstUpgrade.getMaxLevels()) {
            player.sendMessage(MiniMessageSerializers.deserializeToString(
                            MessagesConfiguration.upgradesMessagesConfiguration.getString("maxed-out")
                                    .replace("<upgrade>", firstUpgrade.getName())
                                    .replace("<maxLevel>", String.valueOf(firstUpgrade.shardPricesPerLevelList.get(firstUpgrade.getMaxLevels()))
                                    )
                    )
            );
            return;
        }
        if(shards < firstUpgrade.shardPricesPerLevelList.get(firstUpgrade.getCurrentLevel())) {
            player.sendMessage(MiniMessageSerializers.deserializeToString(
                    MessagesConfiguration.upgradesMessagesConfiguration.getString("insufficient-shards")
                            .replace("<cost>", String.valueOf(firstUpgrade.shardPricesPerLevelList.get(firstUpgrade.getCurrentLevel()))
                            )
                            .replace("<upgrade>", firstUpgrade.getName())
                    )
            );
            return;
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

        int i = 0;
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
                            for(Integer integer : playerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.ultimateSkill.effectList.get(integer).strength += value;
                                playerClass.ultimateSkill.effectList.get(integer).strengthAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : playerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.ultimateSkill.effectList.get(integer).strength += (value * playerClass.ultimateSkill.effectList.get(integer).baseStrength);
                                playerClass.ultimateSkill.effectList.get(integer).strengthAfterUpgrades += (value * playerClass.ultimateSkill.effectList.get(integer).baseStrength);
                            }
                        }
                    }
                }

                case ULTIMATE_EFFECT_LONGEVITY -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : playerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.ultimateSkill.effectList.get(integer).longevity += (int) value;
                                playerClass.ultimateSkill.effectList.get(integer).longevityAfterUpgrades += (int) value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : playerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.ultimateSkill.effectList.get(integer).longevity += (int) (value * playerClass.ultimateSkill.effectList.get(integer).baseLongevity);
                                playerClass.ultimateSkill.effectList.get(integer).longevityAfterUpgrades += (int) (value * playerClass.ultimateSkill.effectList.get(integer).baseLongevity);
                            }
                        }
                    }
                }

                case ULTIMATE_EFFECT_RANGE -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : playerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.ultimateSkill.effectList.get(integer).range += value;
                                playerClass.ultimateSkill.effectList.get(integer).rangeAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : playerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.ultimateSkill.effectList.get(integer).range += value * playerClass.ultimateSkill.effectList.get(integer).baseRange;
                                playerClass.ultimateSkill.effectList.get(integer).rangeAfterUpgrades += value * playerClass.ultimateSkill.effectList.get(integer).baseRange;
                            }
                        }
                    }
                }

                case ULTIMATE_COOLDOWN -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            playerClass.ultimateSkill.cooldown += (int) value;
                            playerClass.ultimateSkill.cooldownAfterUpgrades += (int) value;
                        }
                        case PERCENTAGE -> {
                            playerClass.ultimateSkill.cooldown += (int) (value * playerClass.ultimateSkill.baseCooldown);
                            playerClass.ultimateSkill.cooldownAfterUpgrades += (int) (value * playerClass.ultimateSkill.baseCooldown);
                        }
                    }
                }

                case ULTIMATE_ADD_NEW_EFFECT -> playerClass.ultimateSkill.effectList.addAll(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()));

                case ACTIVE_EFFECT_STRENGTH -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : playerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.activeSkill.effectList.get(integer).strength += value;
                                playerClass.activeSkill.effectList.get(integer).strengthAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : playerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.activeSkill.effectList.get(integer).strength += (value * playerClass.activeSkill.effectList.get(integer).baseStrength);
                                playerClass.activeSkill.effectList.get(integer).strengthAfterUpgrades += (value * playerClass.activeSkill.effectList.get(integer).baseStrength);
                            }
                        }
                    }
                }

                case ACTIVE_EFFECT_LONGEVITY -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : playerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.activeSkill.effectList.get(integer).longevity += (int) value;
                                playerClass.activeSkill.effectList.get(integer).longevityAfterUpgrades += (int) value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : playerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.activeSkill.effectList.get(integer).longevity += (int) (value * playerClass.activeSkill.effectList.get(integer).baseLongevity);
                                playerClass.activeSkill.effectList.get(integer).longevityAfterUpgrades += (int) (value * playerClass.activeSkill.effectList.get(integer).baseLongevity);
                            }
                        }
                    }
                }

                case ACTIVE_EFFECT_RANGE -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : playerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.activeSkill.effectList.get(integer).range += value;
                                playerClass.activeSkill.effectList.get(integer).rangeAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : playerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.activeSkill.effectList.get(integer).range += value * playerClass.activeSkill.effectList.get(integer).baseRange;
                                playerClass.activeSkill.effectList.get(integer).rangeAfterUpgrades += value * playerClass.activeSkill.effectList.get(integer).baseRange;
                            }
                        }
                    }
                }

                case ACTIVE_COOLDOWN -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            playerClass.activeSkill.cooldown += (int) value;
                            playerClass.activeSkill.cooldownAfterUpgrades += (int) value;
                        }
                        case PERCENTAGE -> {
                            playerClass.activeSkill.cooldown += (int) (value * playerClass.activeSkill.baseCooldown);
                            playerClass.activeSkill.cooldownAfterUpgrades += (int) (value * playerClass.activeSkill.baseCooldown);
                        }
                    }
                }

                case ACTIVE_ADD_NEW_EFFECT -> playerClass.activeSkill.effectList.addAll(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()));

                case PASSIVE_EFFECT_STRENGTH -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : playerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.passiveSkill.effectList.get(integer).strength += value;
                                playerClass.passiveSkill.effectList.get(integer).strengthAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : playerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.passiveSkill.effectList.get(integer).strength += value * playerClass.passiveSkill.effectList.get(integer).baseStrength;
                                playerClass.passiveSkill.effectList.get(integer).strengthAfterUpgrades += value * playerClass.passiveSkill.effectList.get(integer).baseStrength;
                            }
                        }
                    }
                }

                case PASSIVE_EFFECT_LONGEVITY -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : playerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.passiveSkill.effectList.get(integer).longevity += (int) value;
                                playerClass.passiveSkill.effectList.get(integer).longevityAfterUpgrades += (int) value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : playerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.passiveSkill.effectList.get(integer).longevity += (int) (value * playerClass.passiveSkill.effectList.get(integer).baseLongevity);
                                playerClass.passiveSkill.effectList.get(integer).longevityAfterUpgrades += (int) (value * playerClass.passiveSkill.effectList.get(integer).baseLongevity);
                            }
                        }
                    }
                }

                case PASSIVE_EFFECT_RANGE -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : playerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.passiveSkill.effectList.get(integer).range += value;
                                playerClass.passiveSkill.effectList.get(integer).rangeAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : playerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.passiveSkill.effectList.get(integer).range += value * playerClass.passiveSkill.effectList.get(integer).baseRange;
                                playerClass.passiveSkill.effectList.get(integer).rangeAfterUpgrades += value * playerClass.passiveSkill.effectList.get(integer).baseRange;
                            }
                        }
                    }
                }

                case PASSIVE_COOLDOWN -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            playerClass.passiveSkill.cooldown += (int) value;
                            playerClass.passiveSkill.cooldownAfterUpgrades += (int) value;
                        }
                        case PERCENTAGE -> {
                            playerClass.passiveSkill.cooldown += (int) (value * playerClass.passiveSkill.baseCooldown);
                            playerClass.passiveSkill.cooldownAfterUpgrades += (int) (value * playerClass.passiveSkill.baseCooldown);
                        }
                    }
                }

                case PASSIVE_ADD_NEW_EFFECT -> playerClass.passiveSkill.effectList.addAll(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()));

                case MAIN_WEAPON_DAMAGE -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            playerClass.loadout.mainWeapon.damage += (int) value;
                            playerClass.loadout.mainWeapon.damageAfterUpgrades += (int) value;
                        }
                        case PERCENTAGE -> {
                            playerClass.loadout.mainWeapon.damage += (int) (value * playerClass.loadout.mainWeapon.baseDamage);
                            playerClass.loadout.mainWeapon.damageAfterUpgrades += (int) (value * playerClass.loadout.mainWeapon.baseDamage);
                        }
                    }
                }

                case MAIN_WEAPON_COOLDOWN -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            playerClass.loadout.mainWeapon.cooldown += (int) value;
                            playerClass.loadout.mainWeapon.cooldownAfterUpgrades += (int) value;
                        }
                        case PERCENTAGE -> {
                            playerClass.loadout.mainWeapon.cooldown += (int) (value * playerClass.loadout.mainWeapon.baseCooldown);
                            playerClass.loadout.mainWeapon.cooldownAfterUpgrades += (int) (value * playerClass.loadout.mainWeapon.baseCooldown);
                        }
                    }
                }

                case MAIN_WEAPON_ADD_NEW_EFFECT -> {
                    for(Effect effect : upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel())) {
                        playerClass.loadout.mainWeapon.addEffect(effect, upgrade.effectsSpecialPropertiesPerLevelList.get(upgrade.getCurrentLevel()).getFirst());
                    }
                }

                case MAIN_WEAPON_EFFECT_STRENGTH -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.loadout.mainWeapon.effectList.get(integer).strength += value;
                                playerClass.loadout.mainWeapon.effectList.get(integer).strengthAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.loadout.mainWeapon.effectList.get(integer).strength += value * playerClass.loadout.mainWeapon.effectList.get(integer).baseStrength;
                                playerClass.loadout.mainWeapon.effectList.get(integer).strengthAfterUpgrades += value * playerClass.loadout.mainWeapon.effectList.get(integer).baseStrength;
                            }
                        }
                    }
                }

                case MAIN_WEAPON_EFFECT_LONGEVITY -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.loadout.mainWeapon.effectList.get(integer).longevity += (int) value;
                                playerClass.loadout.mainWeapon.effectList.get(integer).longevityAfterUpgrades += (int) value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.loadout.mainWeapon.effectList.get(integer).longevity += (int) (value * playerClass.loadout.mainWeapon.effectList.get(integer).baseLongevity);
                                playerClass.loadout.mainWeapon.effectList.get(integer).longevityAfterUpgrades += (int) (value * playerClass.loadout.mainWeapon.effectList.get(integer).baseLongevity);
                            }
                        }
                    }
                }

                case MAIN_WEAPON_EFFECT_RANGE -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.loadout.mainWeapon.effectList.get(integer).range += value;
                                playerClass.loadout.mainWeapon.effectList.get(integer).rangeAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.loadout.mainWeapon.effectList.get(integer).range += value * playerClass.loadout.mainWeapon.effectList.get(integer).baseRange;
                                playerClass.loadout.mainWeapon.effectList.get(integer).rangeAfterUpgrades += (int) (value * playerClass.loadout.mainWeapon.effectList.get(integer).baseRange);
                            }
                        }
                    }
                }

                case SECONDARY_WEAPON_DAMAGE -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            playerClass.loadout.secondaryWeapon.damage += (int) value;
                            playerClass.loadout.secondaryWeapon.damageAfterUpgrades += (int) value;
                        }
                        case PERCENTAGE -> {
                            playerClass.loadout.secondaryWeapon.damage += (int) (value * playerClass.loadout.secondaryWeapon.baseDamage);
                            playerClass.loadout.secondaryWeapon.damageAfterUpgrades += (int) (value * playerClass.loadout.secondaryWeapon.baseDamage);
                        }
                    }
                }

                case SECONDARY_WEAPON_COOLDOWN -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            playerClass.loadout.secondaryWeapon.cooldown += (int) value;
                            playerClass.loadout.secondaryWeapon.cooldownAfterUpgrades += (int) value;
                        }
                        case PERCENTAGE -> {
                            playerClass.loadout.secondaryWeapon.cooldown += (int) (value * playerClass.loadout.secondaryWeapon.baseCooldown);
                            playerClass.loadout.secondaryWeapon.cooldownAfterUpgrades += (int) (value * playerClass.loadout.secondaryWeapon.baseCooldown);
                        }
                    }
                }

                case SECONDARY_WEAPON_ADD_NEW_EFFECT -> {
                    for(Effect effect : upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel())) {
                        playerClass.loadout.secondaryWeapon.addEffect(effect, upgrade.effectsSpecialPropertiesPerLevelList.get(upgrade.getCurrentLevel()).getFirst());
                    }
                }

                case SECONDARY_WEAPON_EFFECT_STRENGTH -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.loadout.secondaryWeapon.effectList.get(integer).strength += value;
                                playerClass.loadout.secondaryWeapon.effectList.get(integer).strengthAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.loadout.secondaryWeapon.effectList.get(integer).strength += value * playerClass.loadout.secondaryWeapon.effectList.get(integer).baseStrength;
                                playerClass.loadout.secondaryWeapon.effectList.get(integer).strengthAfterUpgrades += value * playerClass.loadout.secondaryWeapon.effectList.get(integer).baseStrength;
                            }
                        }
                    }
                }

                case SECONDARY_WEAPON_EFFECT_LONGEVITY -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.loadout.secondaryWeapon.effectList.get(integer).longevity += (int) value;
                                playerClass.loadout.secondaryWeapon.effectList.get(integer).longevityAfterUpgrades += (int) value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.loadout.secondaryWeapon.effectList.get(integer).longevity += (int) (value * playerClass.loadout.secondaryWeapon.effectList.get(integer).baseLongevity);
                                playerClass.loadout.secondaryWeapon.effectList.get(integer).longevityAfterUpgrades += (int) (value * playerClass.loadout.secondaryWeapon.effectList.get(integer).baseLongevity);
                            }
                        }
                    }
                }

                case SECONDARY_WEAPON_EFFECT_RANGE -> {
                    switch (upgradeType) {
                        case FLAT -> {
                            for(Integer integer : playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.loadout.secondaryWeapon.effectList.get(integer).range += value;
                                playerClass.loadout.secondaryWeapon.effectList.get(integer).rangeAfterUpgrades += value;
                            }
                        }
                        case PERCENTAGE -> {
                            for(Integer integer : playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                if(i != integer) continue;
                                playerClass.loadout.secondaryWeapon.effectList.get(integer).range += value * playerClass.loadout.secondaryWeapon.effectList.get(integer).baseRange;
                                playerClass.loadout.secondaryWeapon.effectList.get(integer).rangeAfterUpgrades += (int) (value * playerClass.loadout.secondaryWeapon.effectList.get(integer).baseRange);
                            }
                        }
                    }
                }
            }
            i++;
        }

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

        this.shards += ArenaSettings.shardsAwardedPerKill;
        player.sendMessage(MiniMessageSerializers.deserializeToString(
                MessagesConfiguration.playerMessagesConfiguration.getString("assist-shards")
                        .replace("<player>", enemy.getName())
                        .replace("<award>", String.valueOf(ArenaSettings.shardsAwardedPerKill))
                        .replace("<totalShards>", String.valueOf(this.shards))
        ));

        for(int i = 0; i < 8; i++) {
            UpgradeTreeLocation location = UpgradeTreeLocationConverter.convertIntegerToLocation(i);
            ArrayList<Upgrade> upgradeList = playerClass.upgradeTree.getUpgrade(location);

            int j = 0;
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
                            case STACKING_FLAT_PER_KILL -> movementSpeed += (float) value;
                            case STACKING_PERCENTAGE_PER_KILL -> movementSpeed += (float) (value * movementSpeedAfterUpgrades);
                        }
                        updatePlayerSpeed();
                    }

                    case ULTIMATE_EFFECT_STRENGTH -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : playerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.ultimateSkill.effectList.get(integer).strength += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : playerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.ultimateSkill.effectList.get(integer).strength += (value * playerClass.ultimateSkill.effectList.get(integer).baseStrength);
                                }
                            }
                        }
                    }

                    case ULTIMATE_EFFECT_LONGEVITY -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : playerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.ultimateSkill.effectList.get(integer).longevity += (int) value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : playerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.ultimateSkill.effectList.get(integer).longevity += (int) (value * playerClass.ultimateSkill.effectList.get(integer).baseLongevity);
                                }
                            }
                        }
                    }

                    case ULTIMATE_EFFECT_RANGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : playerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.ultimateSkill.effectList.get(integer).range += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : playerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.ultimateSkill.effectList.get(integer).range += value * playerClass.ultimateSkill.effectList.get(integer).baseRange;
                                }
                            }
                        }
                    }

                    case ULTIMATE_COOLDOWN -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> playerClass.ultimateSkill.cooldown += (int) value;
                            case STACKING_PERCENTAGE_PER_KILL -> playerClass.ultimateSkill.cooldown += (int) (value * playerClass.ultimateSkill.baseCooldown);
                        }
                    }

                    case ULTIMATE_ADD_NEW_EFFECT -> playerClass.ultimateSkill.effectList.add(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount()));

                    case ACTIVE_EFFECT_STRENGTH -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : playerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.activeSkill.effectList.get(integer).strength += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : playerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.activeSkill.effectList.get(integer).strength += (value * playerClass.activeSkill.effectList.get(integer).baseStrength);
                                }
                            }
                        }
                    }

                    case ACTIVE_EFFECT_LONGEVITY -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : playerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.activeSkill.effectList.get(integer).longevity += (int) value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : playerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.activeSkill.effectList.get(integer).longevity += (int) (value * playerClass.activeSkill.effectList.get(integer).baseLongevity);
                                }
                            }
                        }
                    }

                    case ACTIVE_EFFECT_RANGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : playerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.activeSkill.effectList.get(integer).range += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : playerClass.activeSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.activeSkill.effectList.get(integer).range += value * playerClass.activeSkill.effectList.get(integer).baseRange;
                                }
                            }
                        }
                    }

                    case ACTIVE_COOLDOWN -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> playerClass.activeSkill.cooldown += (int) value;
                            case STACKING_PERCENTAGE_PER_KILL -> playerClass.activeSkill.cooldown += (int) (value * playerClass.activeSkill.baseCooldown);
                        }
                    }

                    case ACTIVE_ADD_NEW_EFFECT -> playerClass.activeSkill.effectList.add(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount()));

                    case PASSIVE_EFFECT_STRENGTH -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : playerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.passiveSkill.effectList.get(integer).strength += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : playerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.passiveSkill.effectList.get(integer).strength += value * playerClass.passiveSkill.effectList.get(integer).baseStrength;
                                }
                            }
                        }
                    }

                    case PASSIVE_EFFECT_LONGEVITY -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : playerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.passiveSkill.effectList.get(integer).longevity += (int) value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : playerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.passiveSkill.effectList.get(integer).longevity += (int) (value * playerClass.passiveSkill.effectList.get(integer).baseLongevity);
                                }
                            }
                        }
                    }

                    case PASSIVE_EFFECT_RANGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : playerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.passiveSkill.effectList.get(integer).range += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : playerClass.passiveSkill.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.passiveSkill.effectList.get(integer).range += value * playerClass.passiveSkill.effectList.get(integer).baseRange;
                                }
                            }
                        }
                    }

                    case PASSIVE_COOLDOWN -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> playerClass.passiveSkill.cooldown += (int) value;
                            case STACKING_PERCENTAGE_PER_KILL -> playerClass.passiveSkill.cooldown += (int) (value * playerClass.passiveSkill.baseCooldown);
                        }
                    }

                    case PASSIVE_ADD_NEW_EFFECT -> playerClass.passiveSkill.effectList.add(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount()));

                    case MAIN_WEAPON_DAMAGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> playerClass.loadout.mainWeapon.damage += (int) value;
                            case STACKING_PERCENTAGE_PER_KILL -> playerClass.loadout.mainWeapon.damage += (int) (value * playerClass.loadout.mainWeapon.baseDamage);
                        }
                    }

                    case MAIN_WEAPON_COOLDOWN -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> playerClass.loadout.mainWeapon.cooldown += (int) value;
                            case STACKING_PERCENTAGE_PER_KILL -> playerClass.loadout.mainWeapon.cooldown += (int) (value * playerClass.loadout.mainWeapon.baseCooldown);
                        }
                    }

                    case MAIN_WEAPON_ADD_NEW_EFFECT -> playerClass.loadout.mainWeapon.addEffect(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount()), upgrade.effectsSpecialPropertiesPerLevelList.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount()));

                    case MAIN_WEAPON_EFFECT_STRENGTH -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.loadout.mainWeapon.effectList.get(integer).strength += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.loadout.mainWeapon.effectList.get(integer).strength += value * playerClass.loadout.mainWeapon.effectList.get(integer).baseStrength;
                                }
                            }
                        }
                    }

                    case MAIN_WEAPON_EFFECT_LONGEVITY -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.loadout.mainWeapon.effectList.get(integer).longevity += (int) value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.loadout.mainWeapon.effectList.get(integer).longevity += (int) (value * playerClass.loadout.mainWeapon.effectList.get(integer).baseLongevity);
                                }
                            }
                        }
                    }

                    case MAIN_WEAPON_EFFECT_RANGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.loadout.mainWeapon.effectList.get(integer).range += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.loadout.mainWeapon.effectList.get(integer).range += value * playerClass.loadout.mainWeapon.effectList.get(integer).baseRange;
                                }
                            }
                        }
                    }

                    case SECONDARY_WEAPON_DAMAGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> playerClass.loadout.secondaryWeapon.damage += (int) value;
                            case STACKING_PERCENTAGE_PER_KILL -> playerClass.loadout.secondaryWeapon.damage += (int) (value * playerClass.loadout.secondaryWeapon.baseDamage);
                        }
                    }

                    case SECONDARY_WEAPON_COOLDOWN -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> playerClass.loadout.secondaryWeapon.cooldown += (int) value;
                            case STACKING_PERCENTAGE_PER_KILL -> playerClass.loadout.secondaryWeapon.cooldown += (int) (value * playerClass.loadout.secondaryWeapon.baseCooldown);
                        }
                    }

                    case SECONDARY_WEAPON_ADD_NEW_EFFECT -> playerClass.loadout.secondaryWeapon.addEffect(upgrade.effectsPerLevelList.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount()), upgrade.effectsSpecialPropertiesPerLevelList.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount()));

                    case SECONDARY_WEAPON_EFFECT_STRENGTH -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.loadout.secondaryWeapon.effectList.get(integer).strength += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.loadout.secondaryWeapon.effectList.get(integer).strength += value * playerClass.loadout.secondaryWeapon.effectList.get(integer).baseStrength;
                                }
                            }
                        }
                    }

                    case SECONDARY_WEAPON_EFFECT_LONGEVITY -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.loadout.secondaryWeapon.effectList.get(integer).longevity += (int) value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.loadout.secondaryWeapon.effectList.get(integer).longevity += (int) (value * playerClass.loadout.secondaryWeapon.effectList.get(integer).baseLongevity);
                                }
                            }
                        }
                    }

                    case SECONDARY_WEAPON_EFFECT_RANGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.loadout.secondaryWeapon.effectList.get(integer).range += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(location)) {
                                    if(j != integer) continue;
                                    playerClass.loadout.secondaryWeapon.effectList.get(integer).range += value * playerClass.loadout.secondaryWeapon.effectList.get(integer).baseRange;
                                }
                            }
                        }
                    }
                }
                j++;
            }
        }
    }

    public void onAssist(Player enemy) {

        this.shards += ArenaSettings.shardsAwardedPerAssist;
        player.sendMessage(MiniMessageSerializers.deserializeToString(
                MessagesConfiguration.playerMessagesConfiguration.getString("assist-shards")
                        .replace("<player>", enemy.getName())
                        .replace("<award>", String.valueOf(ArenaSettings.shardsAwardedPerAssist))
                        .replace("<totalShards>", String.valueOf(this.shards))
        ));
    }

    public void onRespawnLogin() {

        playerClass.loadout.mainWeapon.effectList.removeIf(effect -> effect.removeOnDeath);
        playerClass.loadout.secondaryWeapon.effectList.removeIf(effect -> effect.removeOnDeath);
        playerClass.ultimateSkill.effectList.removeIf(effect -> effect.removeOnDeath);
        playerClass.activeSkill.effectList.removeIf(effect -> effect.removeOnDeath);
        playerClass.passiveSkill.effectList.removeIf(effect -> effect.removeOnDeath);

        playerClass.loadout.mainWeapon.cooldown = playerClass.loadout.mainWeapon.cooldownAfterUpgrades;
        playerClass.loadout.mainWeapon.damage = playerClass.loadout.mainWeapon.damageAfterUpgrades;
        playerClass.loadout.secondaryWeapon.cooldown = playerClass.loadout.secondaryWeapon.cooldownAfterUpgrades;
        playerClass.loadout.secondaryWeapon.damage = playerClass.loadout.secondaryWeapon.damageAfterUpgrades;

        maxHealth = maxHealthAfterUpgrades;
        movementSpeed = movementSpeedAfterUpgrades;
        updatePlayerSpeed();

        playerClass.passiveSkill.cooldown = playerClass.passiveSkill.cooldownAfterUpgrades;
        for(Effect effect : playerClass.passiveSkill.effectList) {
            effect.longevity = effect.longevityAfterUpgrades;
            effect.strength = effect.strengthAfterUpgrades;
            effect.range = effect.rangeAfterUpgrades;
        }

        playerClass.activeSkill.cooldown = playerClass.activeSkill.cooldownAfterUpgrades;
        for(Effect effect : playerClass.activeSkill.effectList) {
            effect.longevity = effect.longevityAfterUpgrades;
            effect.strength = effect.strengthAfterUpgrades;
            effect.range = effect.rangeAfterUpgrades;
        }

        playerClass.ultimateSkill.cooldown = playerClass.ultimateSkill.cooldownAfterUpgrades;
        for(Effect effect : playerClass.ultimateSkill.effectList) {
            effect.longevity = effect.longevityAfterUpgrades;
            effect.strength = effect.strengthAfterUpgrades;
            effect.range = effect.rangeAfterUpgrades;
        }

        for(int i = 0; i < 8; i++) {
            UpgradeTreeLocation location = UpgradeTreeLocationConverter.convertIntegerToLocation(i);
            ArrayList<Upgrade> upgradeList = playerClass.upgradeTree.getUpgrade(location);
            for(Upgrade upgrade : upgradeList) {
                if(upgrade.getCurrentLevel() == 0) continue;
                if(upgrade.getUpgradeType() != UpgradeType.STACKING_FLAT_PER_KILL || upgrade.getUpgradeType() != UpgradeType.STACKING_PERCENTAGE_PER_KILL) continue;
                upgrade.setStackCount(0);
            }
        }

        if(changeClassOnRespawn) {
            for(int i = 0; i < 8; i++) {
                UpgradeTreeLocation upgradeTreeLocation = UpgradeTreeLocationConverter.convertIntegerToLocation(i);
                for(int j = 0; j < playerClass.upgradeTree.getUpgrade(upgradeTreeLocation).getFirst().getCurrentLevel(); j++) {
                    shards += playerClass.upgradeTree.getUpgrade(upgradeTreeLocation).getFirst().shardPricesPerLevelList.get(j);
                }
            }

            playerClass = changeClassTo.clone();
            changeClassOnRespawn = false;
        }

        player.setGameMode(GameMode.SURVIVAL);
        obtainLoadout();

        ArenaManager.arenaList.get(ArenaManager.playerArenaIdList.get(player)).teleportPlayerToArena(player);

    }

    public void readThroughEffectList() {

        for(Effect effect : this.getEffectList()) {

            if(effect.longevity == -1) continue;

            effect.longevity -= 1;

            if(effect.longevity % effect.tickEveryServerTicks == 0) {
                switch (effect.effectType) {
                    case HEAL_OVER_TIME_FLAT -> this.heal((int) effect.strength);
                    case HEAL_OVER_TIME_PERCENTAGE -> this.heal((int) effect.strength * this.maxHealth);
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
                case DAMAGE_INCREASE_FLAT -> this.flatDealDamageIncrease += (int) effect.strength;
                case DAMAGE_VULNERABILITY_FLAT -> this.flatTakeDamageIncrease += (int) effect.strength;
                case DAMAGE_INCREASE_MULTIPLIER -> this.dealDamageMultiplier *= effect.strength;
                case DAMAGE_VULNERABILITY_MULTIPLIER -> this.takeDamageMultiplier *= effect.strength;
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

        if(Objects.equals(this.playerClass.name, playerClass.name)) {
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
