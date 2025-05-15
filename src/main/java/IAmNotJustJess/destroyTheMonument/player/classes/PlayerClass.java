package IAmNotJustJess.destroyTheMonument.player.classes;

import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;
import IAmNotJustJess.destroyTheMonument.player.classes.skills.Skill;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTree;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerClass implements Cloneable {

    public int HP;
    public float movementSpeed;
    public Skill passiveSkill;
    public Skill activeSkill;
    public Skill ultimateSkill;
    public Loadout loadout;
    public UpgradeTree upgradeTree;

    PlayerClass(int HP, float movementSpeed, Skill passiveSkill, Skill activeSkill, Skill ultimateSkill, Loadout loadout, UpgradeTree upgradeTree) {
        this.HP = HP;
        this.movementSpeed = movementSpeed;
        this.passiveSkill = passiveSkill;
        this.activeSkill = activeSkill;
        this.ultimateSkill = ultimateSkill;
        this.loadout = loadout;
        this.upgradeTree = upgradeTree;
    }

    @Override
    public Object clone(){
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(this), PlayerClass.class);
    }

}
