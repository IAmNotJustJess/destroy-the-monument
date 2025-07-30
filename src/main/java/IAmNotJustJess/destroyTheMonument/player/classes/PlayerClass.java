package IAmNotJustJess.destroyTheMonument.player.classes;

import IAmNotJustJess.destroyTheMonument.player.classes.skills.Skill;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTree;
import com.google.gson.Gson;
import org.bukkit.Material;

public class PlayerClass implements Cloneable {

    public String name;
    public String description;
    public PlayerClassType playerClassType;
    public int healthPoints;
    public float movementSpeed;
    public Skill passiveSkill;
    public Skill activeSkill;
    public Skill ultimateSkill;
    public Loadout loadout;
    public UpgradeTree upgradeTree;
    public Material guiMaterial;

    PlayerClass(String name, String description, PlayerClassType playerClassType, int healthPoints, float movementSpeed, Skill passiveSkill, Skill activeSkill, Skill ultimateSkill, Loadout loadout, UpgradeTree upgradeTree, Material guiMaterial) {
        this.name = name;
        this.description = description;
        this.playerClassType = playerClassType;
        this.healthPoints = healthPoints;
        this.movementSpeed = movementSpeed;
        this.passiveSkill = passiveSkill;
        this.activeSkill = activeSkill;
        this.ultimateSkill = ultimateSkill;
        this.loadout = loadout;
        this.upgradeTree = upgradeTree;
        this.guiMaterial = guiMaterial;
    }

    @Override
    public Object clone(){
        try {
            Object o = super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(this), PlayerClass.class);
    }

}
