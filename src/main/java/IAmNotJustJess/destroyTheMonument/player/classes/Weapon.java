package IAmNotJustJess.destroyTheMonument.player.classes;

import org.bukkit.inventory.ItemStack;

import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;

import java.util.ArrayList;

public class Weapon {

    public String name;
    public ItemStack item;
    public int damage;
    public ArrayList<Effect> effects;

    Weapon(String name, ItemStack item, int damage) {
        this.name = name;
        this.item = item;
        this.damage = damage;
        this.effects = new ArrayList<>();
    }

}
