package IAmNotJustJess.destroyTheMonument.player;

import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClass;
import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.Upgrade;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTreeLocation;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeType;
import IAmNotJustJess.destroyTheMonument.team.TeamColour;
import IAmNotJustJess.destroyTheMonument.utility.UpgradeTreeLocationConverter;
import org.bukkit.entity.Player;

import java.util.ArrayList;

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
    private ArrayList<Player> assistList;
    private Player lastAttacked;
    private int baseMaxHealth;
    private float baseMovementSpeed;
    private int baseMaxHealthBeforeStacks;
    private float baseMovementSpeedBeforeStacks;

    public PlayerCharacter(Player player, PlayerClass chosenPlayerClass, TeamColour team, float movementSpeed) {
        this.player = player;
        this.chosenPlayerClass = chosenPlayerClass;
        this.team = team;
        this.maxHealth = chosenPlayerClass.HP;
        this.health = maxHealth;
        this.baseMaxHealth = maxHealth;
        this.baseMaxHealthBeforeStacks = maxHealth;
        this.movementSpeed = movementSpeed;
        this.baseMovementSpeed = movementSpeed;
        this.baseMovementSpeedBeforeStacks = movementSpeed;
        this.effectList = new ArrayList<>();
        this.assistList = new ArrayList<>();
        this.shards = 0;
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
        this.chosenPlayerClass = chosenPlayerClass;
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

    private void updatePlayerSpeed() {
        player.setWalkSpeed(movementSpeed / 5);
    }

    public int buyUpgrade(UpgradeTreeLocation location) {

        Upgrade firstUpgrade = chosenPlayerClass.upgradeTree.getUpgrade(location).getFirst();
        ArrayList<Upgrade> upgradeList = chosenPlayerClass.upgradeTree.getUpgrade(location);

        if(firstUpgrade.getCurrentLevel() == firstUpgrade.getMaxLevels()) return 1;
        if(shards < firstUpgrade.shardPricesLevels.get(firstUpgrade.getCurrentLevel() + 1)) return 2;

        shards -= firstUpgrade.shardPricesLevels.get(firstUpgrade.getCurrentLevel() + 1);
        firstUpgrade.setCurrentLevel(firstUpgrade.getCurrentLevel() + 1);
        double value = firstUpgrade.strengthLevels.get(firstUpgrade.getCurrentLevel()).getFirst();

        for(Upgrade upgrade : upgradeList) {
            switch(upgrade.getUpgradeAffection()) {
                case MAX_HP -> {
                    switch(upgrade.getUpgradeType()) {
                        case FLAT -> {
                            maxHealth += (int) value;
                            health += (int) value;
                            baseMaxHealthBeforeStacks += (int) value;
                        }
                        case PERCENTAGE -> {
                            maxHealth += (int) (value * baseMaxHealth);
                            health += (int) (value * baseMaxHealth);
                            baseMaxHealthBeforeStacks += (int) (value * baseMaxHealth);
                        }
                    }
                }
            }
        }

        return 0;
    }

    public void onEnemyKill() {

        for(int i = 0; i < 8; i++) {
            UpgradeTreeLocation location = UpgradeTreeLocationConverter.convertIntegerToLocation(i);
            ArrayList<Upgrade> upgradeList = chosenPlayerClass.upgradeTree.getUpgrade(location);

            for(Upgrade upgrade : upgradeList) {
                if (upgrade.getCurrentLevel() == 0) continue;

                upgrade.setStackCount(upgrade.getStackCount() + 1);
                UpgradeType upgradeType = upgrade.getUpgradeType();

                switch(upgradeType) {
                    case FLAT, PERCENTAGE -> {
                        continue;
                    }
                }

                double value = upgrade.strengthLevels.get(upgrade.getCurrentLevel()).get(upgrade.getStackCount());

                switch(upgrade.getUpgradeAffection()) {

                    case MAX_HP -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                maxHealth += (int) value;
                                health += (int) value;
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                maxHealth += (int) (value * baseMaxHealth);
                                health += (int) (value * baseMaxHealth);
                            }
                        }
                    }

                    case MOVEMENT_SPEED -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                movementSpeed += (float) value;
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                movementSpeed += (float) (value * baseMovementSpeed);
                            }
                        }
                        updatePlayerSpeed();
                    }

                    case ULTIMATE_STRENGTH -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingEffect.get(location)) {
                                    chosenPlayerClass.ultimateSkill.effectList.get(integer).strength += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingEffect.get(location)) {
                                    chosenPlayerClass.ultimateSkill.effectList.get(integer).strength += (value * chosenPlayerClass.ultimateSkill.effectList.get(integer).baseStrengthBeforeStacks);
                                }
                            }
                        }
                    }

                    case ULTIMATE_LONGEVITY -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingEffect.get(location)) {
                                    chosenPlayerClass.ultimateSkill.effectList.get(integer).longevity += (int) value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingEffect.get(location)) {
                                    chosenPlayerClass.ultimateSkill.effectList.get(integer).longevity += (int) (value * chosenPlayerClass.ultimateSkill.effectList.get(integer).baseLongevityBeforeStacks);
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
                                chosenPlayerClass.ultimateSkill.cooldown += (int) (value * chosenPlayerClass.ultimateSkill.baseCooldownBeforeStacks);
                            }
                        }
                    }

                    case ACTIVE_STRENGTH -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingEffect.get(location)) {
                                    chosenPlayerClass.activeSkill.effectList.get(integer).strength += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingEffect.get(location)) {
                                    chosenPlayerClass.activeSkill.effectList.get(integer).strength += (value * chosenPlayerClass.activeSkill.effectList.get(integer).baseStrengthBeforeStacks);
                                }
                            }
                        }
                    }

                    case ACTIVE_LONGEVITY -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingEffect.get(location)) {
                                    chosenPlayerClass.activeSkill.effectList.get(integer).longevity += (int) value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingEffect.get(location)) {
                                    chosenPlayerClass.activeSkill.effectList.get(integer).longevity += (int) (value * chosenPlayerClass.activeSkill.effectList.get(integer).baseLongevityBeforeStacks);
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
                                chosenPlayerClass.activeSkill.cooldown += (int) (value * chosenPlayerClass.activeSkill.baseCooldownBeforeStacks);
                            }
                        }
                    }

                    case PASSIVE_STRENGTH -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingEffect.get(location)) {
                                    chosenPlayerClass.passiveSkill.effectList.get(integer).strength += value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingEffect.get(location)) {
                                    chosenPlayerClass.passiveSkill.effectList.get(integer).strength += value * chosenPlayerClass.passiveSkill.effectList.get(integer).baseStrengthBeforeStacks;
                                }
                            }
                        }
                    }

                    case PASSIVE_LONGEVITY -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingEffect.get(location)) {
                                    chosenPlayerClass.passiveSkill.effectList.get(integer).longevity += (int) value;
                                }
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingEffect.get(location)) {
                                    chosenPlayerClass.passiveSkill.effectList.get(integer).longevity += (int) (value * chosenPlayerClass.passiveSkill.effectList.get(integer).baseLongevityBeforeStacks);
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
                                chosenPlayerClass.passiveSkill.cooldown += (int) (value * chosenPlayerClass.passiveSkill.baseCooldownBeforeStacks);
                            }
                        }
                    }


                    case MAIN_WEAPON_DAMAGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                chosenPlayerClass.loadout.mainWeapon.damage += (int) value;
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                chosenPlayerClass.loadout.mainWeapon.damage += (int) (value * chosenPlayerClass.loadout.mainWeapon.baseDamageBeforeStacks);
                            }
                        }
                    }

                    case SECONDARY_WEAPON_DAMAGE -> {
                        switch (upgradeType) {
                            case STACKING_FLAT_PER_KILL -> {
                                chosenPlayerClass.loadout.secondaryWeapon.damage += (int) value;
                            }
                            case STACKING_PERCENTAGE_PER_KILL -> {
                                chosenPlayerClass.loadout.secondaryWeapon.damage += (int) (value * chosenPlayerClass.loadout.secondaryWeapon.baseDamageBeforeStacks);
                            }
                        }
                    }

                    default -> {}
                }
            }
        }
    }

    public void onDeath() {

        for(int i = 0; i < 8; i++) {
            UpgradeTreeLocation location = UpgradeTreeLocationConverter.convertIntegerToLocation(i);
            ArrayList<Upgrade> upgradeList = chosenPlayerClass.upgradeTree.getUpgrade(location);
            for(Upgrade upgrade : upgradeList) {
                if(upgrade.getCurrentLevel() == 0) continue;
                if(upgrade.getUpgradeType() != UpgradeType.STACKING_FLAT_PER_KILL) continue;

                for(int j = upgrade.getStackCount() - 1; j > 0; j--) {
                    double value = upgrade.strengthLevels.get(upgrade.getCurrentLevel()).get(j);
                    switch(upgrade.getUpgradeAffection()) {
                        case MAX_HP -> {
                            maxHealth -= (int) value;
                        }
                        case MOVEMENT_SPEED -> {
                            movementSpeed -= (int) value;
                        }
                        case ULTIMATE_STRENGTH -> {
                            for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingEffect.get(location)) {
                                chosenPlayerClass.ultimateSkill.effectList.get(integer).strength -= value;
                            }
                        }
                        case ULTIMATE_LONGEVITY -> {
                            for(Integer integer : chosenPlayerClass.ultimateSkill.upgradeAffectingEffect.get(location)) {
                                chosenPlayerClass.ultimateSkill.effectList.get(integer).longevity -= (int) value;
                            }
                        }
                        case ACTIVE_STRENGTH -> {
                            for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingEffect.get(location)) {
                                chosenPlayerClass.activeSkill.effectList.get(integer).strength -= value;
                            }
                        }
                        case ACTIVE_LONGEVITY -> {
                            for(Integer integer : chosenPlayerClass.activeSkill.upgradeAffectingEffect.get(location)) {
                                chosenPlayerClass.activeSkill.effectList.get(integer).longevity -= (int) value;
                            }
                        }
                        case PASSIVE_STRENGTH -> {
                            for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingEffect.get(location)) {
                                chosenPlayerClass.passiveSkill.effectList.get(integer).strength -= value;
                            }
                        }
                        case PASSIVE_LONGEVITY -> {
                            for(Integer integer : chosenPlayerClass.passiveSkill.upgradeAffectingEffect.get(location)) {
                                chosenPlayerClass.passiveSkill.effectList.get(integer).longevity -= (int) value;
                            }
                        }
                        default -> {}
                    }
                }
                upgrade.setStackCount(0);
            }
        }

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
                    }
                    case DAMAGE_OVER_TIME_PERCENTAGE -> {
                        this.dealDamage((int) effect.strength * this.maxHealth);
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

    public ArrayList<Player> getAssistList() {
        return assistList;
    }

    public void addAssist(Player player) {
        if(!this.assistList.contains(player)) this.assistList.add(player);
    }

    public void setAssistList(ArrayList<Player> assistList) {
        this.assistList = assistList;
    }

    public Player getLastAttacked() {
        return lastAttacked;
    }

    public void setLastAttacked(Player lastAttacked) {
        this.lastAttacked = lastAttacked;
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

    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public double getDealDamageMultiplier() {
        return dealDamageMultiplier;
    }

    public void setDealDamageMultiplier(double dealDamageMultiplier) {
        this.dealDamageMultiplier = dealDamageMultiplier;
    }

    public int getFlatDealDamageIncrease() {
        return flatDealDamageIncrease;
    }

    public void setFlatDealDamageIncrease(int flatDealDamageIncrease) {
        this.flatDealDamageIncrease = flatDealDamageIncrease;
    }

    public int getShards() {
        return shards;
    }
}
