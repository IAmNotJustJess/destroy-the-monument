package IAmNotJustJess.destroyTheMonument.classes.skills;

import java.util.ArrayList;

public class Skill {

    public String name;
    public SkillType type;
    public double cooldown;
    public ArrayList<Effect> effectList;

    Skill(String name, SkillType type, double cooldown, Effect effect) {
        this.name = name;
        this.type = type;
        this.cooldown = cooldown;
        this.effectList = new ArrayList<Effect>();
        this.effectList.add(effect);
    }

}
