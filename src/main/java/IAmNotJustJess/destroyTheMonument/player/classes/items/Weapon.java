package IAmNotJustJess.destroyTheMonument.player.classes.items;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeSpecialEffectProperty;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTreeLocation;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageParser;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class Weapon {

    public String name;
    public String description;
    public WeaponType weaponType;
    public ItemStack item;
    public int damage;
    public int damageAfterUpgrades;
    public int baseDamage;
    private double currentCooldown;
    public double cooldown;
    public double cooldownAfterUpgrades;
    public double baseCooldown;
    public ArrayList<Effect> effectList;
    public HashMap<UpgradeTreeLocation, ArrayList<Integer>> upgradeAffectingWhichEffectList;
    public ArrayList<UpgradeSpecialEffectProperty> specialEffectPropertyList;
    public BukkitTask cooldownTask;

    Weapon(String name, String description, WeaponType weaponType, ItemStack item, Integer damage, double cooldown) {
        this.name = name;
        this.description = description;
        this.weaponType = weaponType;
        this.item = item;
        this.damage = damage;
        this.damageAfterUpgrades = damage;
        this.baseDamage = damage;
        this.effectList = new ArrayList<>();
        this.upgradeAffectingWhichEffectList = new HashMap<>();
        this.specialEffectPropertyList = new ArrayList<>();
        this.currentCooldown = cooldown;
        this.cooldownAfterUpgrades = cooldown;
        this.baseCooldown = cooldown;
        this.cooldown = cooldown;
    }

    public void addEffect(Effect effect, UpgradeSpecialEffectProperty activateOnAttack) {
        this.effectList.add(effect);
        this.specialEffectPropertyList.add(activateOnAttack);
    }

    public void useWeapon(PlayerCharacter caster, Location location) {
        switch(weaponType) {
            case MAIN_MELEE, SECONDARY_MELEE -> {
                return;
            }
        }
        if(currentCooldown <= 0.0) {

            for(Effect effect : effectList) {
                effect.activate(caster, location);
            }

            currentCooldown = cooldown + 1.0;
            cooldownTask = new BukkitRunnable() {

                public BukkitTask subSecondTask;

                @Override
                public void run() {
                    currentCooldown -= 1.0;
                    if(currentCooldown < 1.0) {

                        subSecondTask = new BukkitRunnable() {

                            @Override
                            public void run() {
                                currentCooldown = 0.0;
                                cooldownTask.cancel();
                            }

                        }.runTaskLaterAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), (long) ((currentCooldown) * 20));
                    }
                }

            }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), 0L, 20L);
        }
    }

    public ItemStack generateItem() {

        ItemStack itemStack = item.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();

        String descriptionBeforeChanges = description;

        descriptionBeforeChanges = descriptionBeforeChanges.replaceAll("<dmg>", String.valueOf(damage));
        descriptionBeforeChanges = descriptionBeforeChanges.replaceAll("<cooldown>", String.valueOf(cooldown));

        assert itemMeta != null;
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setItemName(MiniMessageParser.deserializeToString(name));
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(JavaPlugin.getPlugin(DestroyTheMonument.class), "damage"), PersistentDataType.STRING, weaponType.name());
        itemMeta.addAttributeModifier(Attribute.ATTACK_SPEED, new AttributeModifier(new NamespacedKey(JavaPlugin.getPlugin(DestroyTheMonument.class), "dtm.attackSpeed"), cooldown, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.ANY));
        itemMeta.setLore(MiniMessageParser.deserializeMultilineToString(descriptionBeforeChanges));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
