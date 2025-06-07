package IAmNotJustJess.destroyTheMonument.player.classes;

import IAmNotJustJess.destroyTheMonument.player.classes.skills.Skill;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTree;
import com.google.gson.Gson;
import org.bukkit.Material;

public class PlayerClass implements Cloneable {

    public String name;
    public String description;
    public int HP;
    public float movementSpeed;
    public Skill passiveSkill;
    public Skill activeSkill;
    public Skill ultimateSkill;
    public Loadout loadout;
    public UpgradeTree upgradeTree;
    public Material guiMaterial;

    PlayerClass(String name, String description, int HP, float movementSpeed, Skill passiveSkill, Skill activeSkill, Skill ultimateSkill, Loadout loadout, UpgradeTree upgradeTree, Material guiMaterial) {
        this.name = name;
        this.description = description;
        this.HP = HP;
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
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(this), PlayerClass.class);
    }

}
