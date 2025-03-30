package IAmNotJustJess.destroyTheMonument.classes.skills;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Vector;

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

    public void Activate(Player caster, Player attacker, Location location) {

        for(Effect effect : effectList) {
            switch (effect.effectType) {
                case PUSH_UP -> {
                    switch (effect.effectApplicationType) {
                        case APPLY_SELF -> {
                            caster.getVelocity().add(new org.bukkit.util.Vector(0, effect.strength, 0));
                        }
                    }

                }
            }
        }

    }
}
