package IAmNotJustJess.destroyTheMonument.player.classes;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;
import IAmNotJustJess.destroyTheMonument.player.classes.effects.EffectApplicationType;
import IAmNotJustJess.destroyTheMonument.player.classes.effects.EffectParticleSpawnLocation;
import IAmNotJustJess.destroyTheMonument.player.classes.effects.EffectType;
import IAmNotJustJess.destroyTheMonument.player.classes.items.Weapon;
import IAmNotJustJess.destroyTheMonument.player.classes.items.WeaponType;
import IAmNotJustJess.destroyTheMonument.player.classes.skills.Skill;
import IAmNotJustJess.destroyTheMonument.player.classes.skills.SkillType;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerClassFileHandler {

    public void save() {

    }

    public void read() {

        Plugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        File[] configFolder = new File(plugin.getDataFolder() + File.separator + "classes").listFiles();

        PlayerClassManager.getList().clear();

        for(File configFile : Objects.requireNonNull(configFolder)) {

            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

            PlayerClass playerClass = new PlayerClass(
                fileConfiguration.getString("name"),
                fileConfiguration.getString("description"),
                PlayerClassType.valueOf(fileConfiguration.getString("playerClassType")),
                fileConfiguration.getInt("hp"),
                Float.parseFloat(Objects.requireNonNull(fileConfiguration.getString("movementSpeed"))),
                new Skill(
                    fileConfiguration.getString("passive.name"),
                    fileConfiguration.getString("passive.description"),
                    (ArrayList<String>) fileConfiguration.getStringList("passive.descriptionTextReplacements"),
                    SkillType.valueOf(fileConfiguration.getString("passive.skillType")),
                    fileConfiguration.getDouble("passive.cooldown")
                ),
                new Skill(
                    fileConfiguration.getString("active.name"),
                    fileConfiguration.getString("active.description"),
                    (ArrayList<String>) fileConfiguration.getStringList("active.descriptionTextReplacements"),
                    SkillType.valueOf(fileConfiguration.getString("active.skillType")),
                    fileConfiguration.getDouble("active.cooldown")
                ),
                new Skill(
                    fileConfiguration.getString("ultimate.name"),
                    fileConfiguration.getString("ultimate.description"),
                    (ArrayList<String>) fileConfiguration.getStringList("ultimate.descriptionTextReplacements"),
                    SkillType.valueOf(fileConfiguration.getString("ultimate.skillType")),
                    fileConfiguration.getDouble("ultimate.cooldown")
                ),
                new Loadout(
                    fileConfiguration.getItemStack("loadout.helmet"),
                    fileConfiguration.getItemStack("loadout.chestplate"),
                    fileConfiguration.getItemStack("loadout.leggings"),
                    fileConfiguration.getItemStack("loadout.boots"),
                    new Weapon(
                        fileConfiguration.getString("loadout.mainWeapon.name"),
                        fileConfiguration.getString("loadout.mainWeapon.description"),
                        WeaponType.valueOf(fileConfiguration.getString("loadout.mainWeapon.weaponType")),
                        fileConfiguration.getItemStack("loadout.mainWeapon.itemStack"),
                        fileConfiguration.getInt("loadout.mainWeapon.damage"),
                        fileConfiguration.getDouble("loadout.mainWeapon.cooldown")
                    ),
                    new Weapon(
                        fileConfiguration.getString("loadout.secondaryWeapon.name"),
                        fileConfiguration.getString("loadout.secondaryWeapon.description"),
                        WeaponType.valueOf(fileConfiguration.getString("loadout.secondaryWeapon.weaponType")),
                        fileConfiguration.getItemStack("loadout.secondaryWeapon.itemStack"),
                        fileConfiguration.getInt("loadout.secondaryWeapon.damage"),
                        fileConfiguration.getDouble("loadout.secondaryWeapon.cooldown")
                    ),
                    fileConfiguration.getInt("loadout.blockAmount")
                ),
                new UpgradeTree(),
                Material.valueOf(fileConfiguration.getString("guiMaterial"))
            );

            if(!Objects.requireNonNull(fileConfiguration.getConfigurationSection("passive.effects")).getKeys(false).isEmpty()) {
                for(String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("passive.effects")).getKeys(false)) {
                    Effect effect = new Effect(
                        EffectType.valueOf(fileConfiguration.getString("passive.effects." + path + ".effectType")),
                        EffectApplicationType.valueOf(fileConfiguration.getString("passive.effects." + path + ".effectApplicationType")),
                        fileConfiguration.getDouble("passive.effects." + path + ".strength"),
                        fileConfiguration.getDouble("passive.effects." + path + ".range"),
                        fileConfiguration.getInt("passive.effects." + path + ".tickEveryServerTicks"),
                        fileConfiguration.getInt("passive.effects." + path + ".longevity"),
                        fileConfiguration.getLong("passive.effects." + path + ".delay"),
                        fileConfiguration.getBoolean("passive.effects." + path + ".removeOnDeath"),
                        fileConfiguration.getString("passive.effects." + path + ".soundSerializedString"),
                        fileConfiguration.getString("passive.effects." + path + ".particleSerializedString"),
                        EffectParticleSpawnLocation.valueOf("passive.effects." + path + ".particleSpawnLocation")
                    );
                    playerClass.passiveSkill.effectList.add(effect);
                }
            }
            
            if(!Objects.requireNonNull(fileConfiguration.getConfigurationSection("active.effects")).getKeys(false).isEmpty()) {
                for(String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("active.effects")).getKeys(false)) {
                    Effect effect = new Effect(
                        EffectType.valueOf(fileConfiguration.getString("active.effects." + path + ".effectType")),
                        EffectApplicationType.valueOf(fileConfiguration.getString("active.effects." + path + ".effectApplicationType")),
                        fileConfiguration.getDouble("active.effects." + path + ".strength"),
                        fileConfiguration.getDouble("active.effects." + path + ".range"),
                        fileConfiguration.getInt("active.effects." + path + ".tickEveryServerTicks"),
                        fileConfiguration.getInt("active.effects." + path + ".longevity"),
                        fileConfiguration.getLong("active.effects." + path + ".delay"),
                        fileConfiguration.getBoolean("active.effects." + path + ".removeOnDeath"),
                        fileConfiguration.getString("active.effects." + path + ".soundSerializedString"),
                        fileConfiguration.getString("active.effects." + path + ".particleSerializedString"),
                        EffectParticleSpawnLocation.valueOf("active.effects." + path + ".particleSpawnLocation")
                    );
                    playerClass.activeSkill.effectList.add(effect);
                }
            }

            if(!Objects.requireNonNull(fileConfiguration.getConfigurationSection("ultimate.effects")).getKeys(false).isEmpty()) {
                for(String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("ultimate.effects")).getKeys(false)) {
                    Effect effect = new Effect(
                        EffectType.valueOf(fileConfiguration.getString("ultimate.effects." + path + ".effectType")),
                        EffectApplicationType.valueOf(fileConfiguration.getString("ultimate.effects." + path + ".effectApplicationType")),
                        fileConfiguration.getDouble("ultimate.effects." + path + ".strength"),
                        fileConfiguration.getDouble("ultimate.effects." + path + ".range"),
                        fileConfiguration.getInt("ultimate.effects." + path + ".tickEveryServerTicks"),
                        fileConfiguration.getInt("ultimate.effects." + path + ".longevity"),
                        fileConfiguration.getLong("ultimate.effects." + path + ".delay"),
                        fileConfiguration.getBoolean("ultimate.effects." + path + ".removeOnDeath"),
                        fileConfiguration.getString("ultimate.effects." + path + ".soundSerializedString"),
                        fileConfiguration.getString("ultimate.effects." + path + ".particleSerializedString"),
                        EffectParticleSpawnLocation.valueOf("ultimate.effects." + path + ".particleSpawnLocation")
                    );
                    playerClass.ultimateSkill.effectList.add(effect);
                }
            }

            if(!Objects.requireNonNull(fileConfiguration.getConfigurationSection("loadout.mainWeapon.effects")).getKeys(false).isEmpty()) {
                for(String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("loadout.mainWeapon.effects")).getKeys(false)) {
                    Effect effect = new Effect(
                        EffectType.valueOf(fileConfiguration.getString("loadout.mainWeapon.effects." + path + ".effectType")),
                        EffectApplicationType.valueOf(fileConfiguration.getString("loadout.mainWeapon.effects." + path + ".effectApplicationType")),
                        fileConfiguration.getDouble("loadout.mainWeapon.effects." + path + ".strength"),
                        fileConfiguration.getDouble("loadout.mainWeapon.effects." + path + ".range"),
                        fileConfiguration.getInt("loadout.mainWeapon.effects." + path + ".tickEveryServerTicks"),
                        fileConfiguration.getInt("loadout.mainWeapon.effects." + path + ".longevity"),
                        fileConfiguration.getLong("loadout.mainWeapon.effects." + path + ".delay"),
                        fileConfiguration.getBoolean("loadout.mainWeapon.effects." + path + ".removeOnDeath"),
                        fileConfiguration.getString("loadout.mainWeapon.effects." + path + ".soundSerializedString"),
                        fileConfiguration.getString("loadout.mainWeapon.effects." + path + ".particleSerializedString"),
                        EffectParticleSpawnLocation.valueOf("loadout.mainWeapon.effects." + path + ".particleSpawnLocation")
                    );
                    playerClass.loadout.mainWeapon.effectList.add(effect);
                    playerClass.loadout.mainWeapon.specialEffectPropertyList.add(UpgradeSpecialEffectProperty.valueOf(fileConfiguration.getString("loadout.mainWeapon.effects." + path + ".specialEffectProperty")));
                }
            }

            if(!Objects.requireNonNull(fileConfiguration.getConfigurationSection("loadout.secondaryWeapon.effects")).getKeys(false).isEmpty()) {
                for(String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("loadout.secondaryWeapon.effects")).getKeys(false)) {
                    Effect effect = new Effect(
                        EffectType.valueOf(fileConfiguration.getString("loadout.secondaryWeapon.effects." + path + ".effectType")),
                        EffectApplicationType.valueOf(fileConfiguration.getString("loadout.secondaryWeapon.effects." + path + ".effectApplicationType")),
                        fileConfiguration.getDouble("loadout.secondaryWeapon.effects." + path + ".strength"),
                        fileConfiguration.getDouble("loadout.secondaryWeapon.effects." + path + ".range"),
                        fileConfiguration.getInt("loadout.secondaryWeapon.effects." + path + ".tickEveryServerTicks"),
                        fileConfiguration.getInt("loadout.secondaryWeapon.effects." + path + ".longevity"),
                        fileConfiguration.getLong("loadout.secondaryWeapon.effects." + path + ".delay"),
                        fileConfiguration.getBoolean("loadout.secondaryWeapon.effects." + path + ".removeOnDeath"),
                        fileConfiguration.getString("loadout.secondaryWeapon.effects." + path + ".soundSerializedString"),
                        fileConfiguration.getString("loadout.secondaryWeapon.effects." + path + ".particleSerializedString"),
                        EffectParticleSpawnLocation.valueOf("loadout.secondaryWeapon.effects." + path + ".particleSpawnLocation")
                    );
                    playerClass.loadout.mainWeapon.effectList.add(effect);
                    playerClass.loadout.mainWeapon.specialEffectPropertyList.add(UpgradeSpecialEffectProperty.valueOf(fileConfiguration.getString("loadout.secondaryWeapon.effects." + path + ".specialEffectProperty")));
                }
            }

            UpgradeTree upgradeTree = new UpgradeTree();

            if(!Objects.requireNonNull(fileConfiguration.getConfigurationSection("upgradeTree")).getKeys(false).isEmpty()) {
                for (String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("upgradeTree")).getKeys(false)) {
                    UpgradeTreeLocation upgradeTreeLocation = UpgradeTreeLocation.valueOf(path);
                    ArrayList<Upgrade> upgrades = new ArrayList<>();
                    if(!Objects.requireNonNull(fileConfiguration.getConfigurationSection("upgradeTree." + path)).getKeys(false).isEmpty()) {
                        for (String path2 : Objects.requireNonNull(fileConfiguration.getConfigurationSection("upgradeTree." + path)).getKeys(false)) {
                            Upgrade upgrade = new Upgrade(
                                fileConfiguration.getString("upgradeTree." + path + "." + path2 + ".name"),
                                fileConfiguration.getString("upgradeTree." + path + "." + path2 + ".description"),
                                fileConfiguration.getInt("upgradeTree." + path + "." + path2 + ".maxLevels"),
                                UpgradeAffection.valueOf(fileConfiguration.getString("upgradeTree." + path + "." + path2 + ".upgradeAffection")),
                                UpgradeType.valueOf(fileConfiguration.getString("upgradeTree." + path + "." + path2 + ".upgradeType")),
                                fileConfiguration.getInt("upgradeTree." + path + "." + path2 + ".maxStacks"),
                                Material.valueOf(fileConfiguration.getString("upgradeTree." + path + "." + path2 + ".upgradeType"))
                            );
                            upgrades.add(upgrade);
                            playerClass.passiveSkill.upgradeAffectingWhichEffectList.put(upgradeTreeLocation, (ArrayList<Integer>) fileConfiguration.getIntegerList("upgradeTree." + path + "." + path2 + "." + "passiveAffection"));
                            playerClass.activeSkill.upgradeAffectingWhichEffectList.put(upgradeTreeLocation, (ArrayList<Integer>) fileConfiguration.getIntegerList("upgradeTree." + path + "." + path2 + "." + "activeAffection"));
                            playerClass.ultimateSkill.upgradeAffectingWhichEffectList.put(upgradeTreeLocation, (ArrayList<Integer>) fileConfiguration.getIntegerList("upgradeTree." + path + "." + path2 + "." + "ultimateAffection"));
                            playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.put(upgradeTreeLocation, (ArrayList<Integer>) fileConfiguration.getIntegerList("upgradeTree." + path + "." + path2 + "." + "mainWeaponAffection"));
                            playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.put(upgradeTreeLocation, (ArrayList<Integer>) fileConfiguration.getIntegerList("upgradeTree." + path + "." + path2 + "." + "secondaryWeaponAffection"));
                        }
                    }
                    upgradeTree.setUpgrade(upgradeTreeLocation, upgrades);
                }
            }

        }

    }

}
