package IAmNotJustJess.destroyTheMonument.player.classes.skills;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;

import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTreeLocation;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageSerializers;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Skill {

    public String name;
    public String description;
    private ArrayList<String> descriptionTextReplacementList;
    public SkillType type;
    private double currentCooldown;
    public boolean active;
    public double cooldown;
    public double cooldownAfterUpgrades;
    public double baseCooldown;
    public ArrayList<Effect> effectList;
    public HashMap<UpgradeTreeLocation, ArrayList<Integer>> upgradeAffectingWhichEffectList;
    public BukkitTask cooldownTask;

    Skill(String name, String description, ArrayList<String> descriptionTextReplacementList, SkillType type, double cooldown, Effect effect) {

        this.name = name;
        this.description = description;
        this.descriptionTextReplacementList = descriptionTextReplacementList;
        this.type = type;
        this.cooldown = cooldown;
        this.cooldownAfterUpgrades = cooldown;
        this.baseCooldown = cooldown;
        this.currentCooldown = 0;
        this.effectList = new ArrayList<>();
        this.upgradeAffectingWhichEffectList = new HashMap<>();
        this.active = false;
        this.effectList.add(effect);

    }

    public void useSkill(PlayerCharacter caster, Location location) {
        active = true;
        switch (type) {
            case ACTIVE, ACTIVATING_ON_DEATH, ACTIVATING_ON_HIT -> {
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

    public void activate(PlayerCharacter caster, Location location) {

        active = false;
        for(Effect effect : effectList) {

            effect.activate(caster, location);
        }
    }

    public List<String> getDescription() {
        String string = description;
        for(int i = 0; i < descriptionTextReplacementList.size(); i++) {
            string = string.replaceAll("<"+i+">", descriptionTextReplacementList.get(i));
        }
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

    public ArrayList<String> getDescriptionTextReplacementList() {
        return descriptionTextReplacementList;
    }

    public void setDescriptionTextReplacementList(ArrayList<String> descriptionTextReplacementList) {
        this.descriptionTextReplacementList = descriptionTextReplacementList;
    }
}
