package IAmNotJustJess.destroyTheMonument.player;

import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClass;
import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.Upgrade;
import IAmNotJustJess.destroyTheMonument.team.TeamColour;
import IAmNotJustJess.destroyTheMonument.utility.UpgradeTreeLocationCoverter;
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
    private int baseMovementSpeed;

    public PlayerCharacter(Player player, PlayerClass chosenPlayerClass, TeamColour team, float movementSpeed) {
        this.player = player;
        this.chosenPlayerClass = chosenPlayerClass;
        this.team = team;
        this.maxHealth = chosenPlayerClass.HP;
        this.health = this.maxHealth;
        this.movementSpeed = movementSpeed;
        this.effectList = new ArrayList<>();
        this.assistList = new ArrayList<>();
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

    public void readThroughEffectList() {

        for(int i = 0; i < 8; i++) {
            Upgrade upgrade = chosenPlayerClass.upgradeTree.getUpgrade(UpgradeTreeLocationCoverter.convertIntegerToLocation(i));
        }

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
                        break;
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
}
