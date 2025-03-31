package IAmNotJustJess.destroyTheMonument.logic;

import IAmNotJustJess.destroyTheMonument.classes.PlayerClass;
import IAmNotJustJess.destroyTheMonument.classes.skills.Effect;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerCharacter {

    private Player player;
    private int maxHealth;
    private int health;
    private PlayerClass chosenPlayerClass;
    private int team;
    private ArrayList<Effect> effectList;

    public PlayerCharacter(Player player, PlayerClass chosenPlayerClass, int team) {
        this.player = player;
        this.chosenPlayerClass = chosenPlayerClass;
        this.team = team;
        this.maxHealth = chosenPlayerClass.HP;
        this.health = this.maxHealth;
        this.effectList = new ArrayList<Effect>();
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

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public ArrayList<Effect> getEffectList() {
        return effectList;
    }

    public void setEffectList(ArrayList<Effect> effectList) {
        this.effectList = effectList;
    }
}
