package IAmNotJustJess.destroyTheMonument.player.classes.items;

import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTreeLocation;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageParser;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class Weapon {

    public String name;
    public String description;
    public WeaponType weaponType;
    public ItemStack item;
    public int damage;
    public int baseDamage;
    public int baseDamageBeforeStacks;
    public ArrayList<Effect> effects;
    public HashMap<UpgradeTreeLocation, ArrayList<Integer>> upgradeAffectingEffect;
    public ArrayList<Boolean> effectOnAttackOrHit; // attack - hit przeciwnika - true, hit - projectile hit - false

    Weapon(String name, String description, WeaponType weaponType, ItemStack item, Integer damage) {
        this.name = name;
        this.description = description;
        this.weaponType = weaponType;
        this.item = item;
        this.damage = damage;
        this.baseDamage = damage;
        this.baseDamageBeforeStacks = damage;
        this.effects = new ArrayList<>();
        this.upgradeAffectingEffect = new HashMap<>();
        this.effectOnAttackOrHit = new ArrayList<>();
    }

    public void addEffect(Effect effect, Boolean activateOnAttack) {
        this.effects.add(effect);
        this.effectOnAttackOrHit.add(activateOnAttack);
    }

    public ItemStack generateItem() {

        ItemStack itemStack = item.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();

        assert itemMeta != null;
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setItemName(MiniMessageParser.Deserialize(name));
        itemMeta.setLore(MiniMessageParser.DeserializeMultiline(description));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
