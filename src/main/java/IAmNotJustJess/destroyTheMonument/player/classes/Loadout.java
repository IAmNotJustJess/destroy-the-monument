package IAmNotJustJess.destroyTheMonument.player.classes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Loadout {

    public ItemStack helmet;
    public ItemStack chestplate;
    public ItemStack leggings;
    public ItemStack boots;
    public ArrayList<ItemStack> items;
    public int blockAmount;

    public Loadout(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ArrayList<ItemStack> items, int blockAmount) {

        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.items = items;
        this.blockAmount = blockAmount;

    }
}
