package IAmNotJustJess.destroyTheMonument.classes;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Loadout {

    public Item helmet;
    public Item chestplate;
    public Item leggings;
    public Item boots;
    public ArrayList<Item> items;
    public int blockAmount;

    public Loadout(Item helmet, Item chestplate, Item leggings, Item boots, ArrayList<Item> items, int blockAmount) {

        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.items = items;
        this.blockAmount = blockAmount;

    }
}
