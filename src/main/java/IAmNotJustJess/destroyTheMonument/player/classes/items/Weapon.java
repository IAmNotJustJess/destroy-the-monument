package IAmNotJustJess.destroyTheMonument.player.classes.items;

import org.bukkit.inventory.ItemStack;

import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;

import java.util.ArrayList;

public class Weapon {

    public String name;
    public ItemStack item;
    public Integer damage;
    public ArrayList<Effect> effects;

    Weapon(String name, ItemStack item, Integer damage) {
        this.name = name;
        this.item = item;
        this.damage = damage;
        this.effects = new ArrayList<>();
    }

}
