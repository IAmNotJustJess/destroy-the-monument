package IAmNotJustJess.destroyTheMonument.player.classes.skills;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTreeLocation;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageSerializers;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Skill {

    public String name;
    public String description;
    public SkillType skillType;
    private double currentCooldown;
    public boolean active;
    public double cooldown;
    public double cooldownAfterUpgrades;
    public double baseCooldown;
    public ArrayList<Effect> effectList;
    public HashMap<UpgradeTreeLocation, ArrayList<Integer>> upgradeAffectingWhichEffectList;
    public BukkitTask cooldownTask;

    public Skill(String name, String description, SkillType skillType, double cooldown) {

        this.name = name;
        this.description = description;
        this.skillType = skillType;
        this.cooldown = cooldown;
        this.cooldownAfterUpgrades = cooldown;
        this.baseCooldown = cooldown;
        this.currentCooldown = 0;
        this.effectList = new ArrayList<>();
        this.upgradeAffectingWhichEffectList = new HashMap<>();
        this.active = false;

    }

    public void useSkill(PlayerCharacter caster, Location location) {
        active = true;
        switch (skillType) {
            case ACTIVE, ACTIVATING_ON_DEATH, ACTIVATING_ON_HIT, ULTIMATE, ULTIMATE_ON_DEATH, ULTIMATE_ON_HIT -> {
                if(currentCooldown <= 0.0) {
                    activate(caster, location);
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
            case PASSIVE -> {
                if(currentCooldown >= 0.0) {
                    if(cooldownTask == null) {
                        cooldownTask = new BukkitRunnable() {

                            public BukkitTask subSecondTask;

                            @Override
                            public void run() {
                                currentCooldown -= 1.0;
                                if(currentCooldown < 1.0) {

                                    subSecondTask = new BukkitRunnable() {

                                        @Override
                                        public void run() {
                                            currentCooldown = cooldown;
                                            activate(caster, location);
                                        }

                                    }.runTaskLaterAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), (long) ((currentCooldown) * 20));
                                }
                            }

                        }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), 0L, 20L);
                    }
                }
            }
        }
    }

    public ItemStack generateItem() {

        ItemStack itemStack;
        ItemMeta itemMeta;
        if(!active) {
            itemStack = new ItemStack(Material.GRAY_DYE);
            itemMeta = itemStack.getItemMeta().clone();
            itemMeta.setItemName(MiniMessageSerializers.deserializeToString("<#c0c0c0>" + name));
        }
        else {
            switch (skillType) {
                case ACTIVE, ACTIVATING_ON_DEATH, ACTIVATING_ON_HIT -> itemStack = new ItemStack(Material.LIME_DYE);
                case ULTIMATE, ULTIMATE_ON_DEATH, ULTIMATE_ON_HIT -> itemStack = new ItemStack(Material.NETHER_STAR);
                default -> {
                    itemStack = new ItemStack(Material.AIR);
                    return itemStack;
                }
            }
            itemMeta = itemStack.getItemMeta().clone();
            itemMeta.setItemName(MiniMessageSerializers.deserializeToString("<#14db4c>" + name));
        }

        Plugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setLore(MiniMessageSerializers.deserializeMultilineToString(description));
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "skillType"), PersistentDataType.STRING, skillType.name());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void activate(PlayerCharacter caster, Location location) {

        active = false;
        for(Effect effect : effectList) {

            effect.activate(caster, location);
        }
    }

    public List<String> getDescription() {
        String string = description;
        return MiniMessageSerializers.deserializeMultilineToString(string);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return MiniMessageSerializers.deserializeToString(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Skill clone() {

        Skill skill;

        try {
            skill = (Skill) super.clone();
        } catch (CloneNotSupportedException exception) {
            skill = new Skill(
                name,
                description,
                skillType,
                baseCooldown
            );
        }

        skill.effectList = new ArrayList<>() {{
            for(Effect effect : effectList) {
                add(effect.clone());
            }
        }};

        skill.upgradeAffectingWhichEffectList = new HashMap<>() {{
            for(UpgradeTreeLocation upgradeTreeLocation : upgradeAffectingWhichEffectList.keySet()) {
                put(
                    upgradeTreeLocation,
                    new ArrayList<>() {{
                        this.addAll(upgradeAffectingWhichEffectList.get(upgradeTreeLocation));
                    }}
                );
            }
        }};

        return skill;
    }
}
