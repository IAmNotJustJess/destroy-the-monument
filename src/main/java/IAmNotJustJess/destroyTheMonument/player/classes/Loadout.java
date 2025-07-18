package IAmNotJustJess.destroyTheMonument.player.classes;

import IAmNotJustJess.destroyTheMonument.player.classes.items.Weapon;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Loadout {

    public ItemStack helmet;
    public ItemStack chestplate;
    public ItemStack leggings;
    public ItemStack boots;
    public Weapon mainWeapon;
    public Weapon secondaryWeapon;
    public ArrayList<ItemStack> additionalItems;
    public int blockAmount;

    public Loadout(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, Weapon mainWeapon, Weapon secondaryWeapon, ArrayList<ItemStack> additionalItems, int blockAmount) {

        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.mainWeapon = mainWeapon;
        this.secondaryWeapon = secondaryWeapon;
        this.additionalItems = additionalItems;
        this.blockAmount = blockAmount;

    }
}
