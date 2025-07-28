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
import IAmNotJustJess.destroyTheMonument.utility.ConsoleDebugSending;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageSerializers;
import IAmNotJustJess.destroyTheMonument.utility.UpgradeTreeLocationConverter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerClassFileHandler {

    public static void save() {

        Plugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);
        ConsoleDebugSending.send(
            "send-save-messages",
            MiniMessageSerializers.deserializeToComponent("<#dbd814>Saving Player Classes...")
        );

        for(PlayerClass playerClass : PlayerClassManager.getList()) {

            File configFile = new File(plugin.getDataFolder() + File.separator + "classes" + File.separator + playerClass.name + ".yml");
            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

            fileConfiguration.set("name", playerClass.name);
            fileConfiguration.set("description", playerClass.description);
            fileConfiguration.set("playerClassType", playerClass.playerClassType);
            fileConfiguration.set("hp", playerClass.healthPoints);
            fileConfiguration.set("movementSpeed", playerClass.movementSpeed);

            fileConfiguration.set("passive.name", playerClass.passiveSkill.name);
            fileConfiguration.set("passive.description", playerClass.passiveSkill.description);
            fileConfiguration.set("passive.skillType", playerClass.passiveSkill.skillType);
            fileConfiguration.set("passive.cooldown", playerClass.passiveSkill.cooldown);

            fileConfiguration.set("active.name", playerClass.activeSkill.name);
            fileConfiguration.set("active.description", playerClass.activeSkill.description);
            fileConfiguration.set("active.skillType", playerClass.activeSkill.skillType);
            fileConfiguration.set("active.cooldown", playerClass.activeSkill.cooldown);

            fileConfiguration.set("ultimate.name", playerClass.ultimateSkill.name);
            fileConfiguration.set("ultimate.description", playerClass.ultimateSkill.description);
            fileConfiguration.set("ultimate.skillType", playerClass.ultimateSkill.skillType);
            fileConfiguration.set("ultimate.cooldown", playerClass.ultimateSkill.cooldown);

            fileConfiguration.set("loadout.helmet", playerClass.loadout.helmet);
            fileConfiguration.set("loadout.chestplate", playerClass.loadout.chestplate);
            fileConfiguration.set("loadout.leggings", playerClass.loadout.leggings);
            fileConfiguration.set("loadout.boots", playerClass.loadout.boots);

            fileConfiguration.set("loadout.mainWeapon.name", playerClass.loadout.mainWeapon.name);
            fileConfiguration.set("loadout.mainWeapon.description", playerClass.loadout.mainWeapon.description);
            fileConfiguration.set("loadout.mainWeapon.weaponType", playerClass.loadout.mainWeapon.weaponType);
            fileConfiguration.set("loadout.mainWeapon.itemStack", playerClass.loadout.mainWeapon.item);
            fileConfiguration.set("loadout.mainWeapon.cooldown", playerClass.loadout.mainWeapon.cooldown);

            fileConfiguration.set("loadout.secondaryWeapon.name", playerClass.loadout.secondaryWeapon.name);
            fileConfiguration.set("loadout.secondaryWeapon.description", playerClass.loadout.secondaryWeapon.description);
            fileConfiguration.set("loadout.secondaryWeapon.weaponType", playerClass.loadout.secondaryWeapon.weaponType);
            fileConfiguration.set("loadout.secondaryWeapon.itemStack", playerClass.loadout.secondaryWeapon.item);
            fileConfiguration.set("loadout.secondaryWeapon.cooldown", playerClass.loadout.secondaryWeapon.cooldown);

            fileConfiguration.set("loadout.blockAmount", playerClass.loadout.blockAmount);

            int i = 0;
            for(Effect effect : playerClass.passiveSkill.effectList) {
                fileConfiguration.set("passive.effects." + i + ".effectType", effect.effectType);
                fileConfiguration.set("passive.effects." + i + ".effectApplicationType", effect.effectApplicationType);
                fileConfiguration.set("passive.effects." + i + ".strength", effect.strength);
                fileConfiguration.set("passive.effects." + i + ".range", effect.range);
                fileConfiguration.set("passive.effects." + i + ".tickEveryServerTicks", effect.tickEveryServerTicks);
                fileConfiguration.set("passive.effects." + i + ".longevity", effect.longevity);
                fileConfiguration.set("passive.effects." + i + ".delay", effect.delay);
                fileConfiguration.set("passive.effects." + i + ".removeOnDeath", effect.removeOnDeath);
                fileConfiguration.set("passive.effects." + i + ".soundSerializedString", effect.soundSerializedString);
                fileConfiguration.set("passive.effects." + i + ".particleSerializedString", effect.particleSerializedString);
                fileConfiguration.set("passive.effects." + i + ".particleSpawnLocation", effect.particleSpawnLocation);
                i++;
            }

            i = 0;
            for(Effect effect : playerClass.activeSkill.effectList) {
                fileConfiguration.set("active.effects." + i + ".effectType", effect.effectType);
                fileConfiguration.set("active.effects." + i + ".effectApplicationType", effect.effectApplicationType);
                fileConfiguration.set("active.effects." + i + ".strength", effect.strength);
                fileConfiguration.set("active.effects." + i + ".range", effect.range);
                fileConfiguration.set("active.effects." + i + ".tickEveryServerTicks", effect.tickEveryServerTicks);
                fileConfiguration.set("active.effects." + i + ".longevity", effect.longevity);
                fileConfiguration.set("active.effects." + i + ".delay", effect.delay);
                fileConfiguration.set("active.effects." + i + ".removeOnDeath", effect.removeOnDeath);
                fileConfiguration.set("active.effects." + i + ".soundSerializedString", effect.soundSerializedString);
                fileConfiguration.set("active.effects." + i + ".particleSerializedString", effect.particleSerializedString);
                fileConfiguration.set("active.effects." + i + ".particleSpawnLocation", effect.particleSpawnLocation);
                i++;
            }

            i = 0;
            for(Effect effect : playerClass.ultimateSkill.effectList) {
                fileConfiguration.set("ultimate.effects." + i + ".effectType", effect.effectType);
                fileConfiguration.set("ultimate.effects." + i + ".effectApplicationType", effect.effectApplicationType);
                fileConfiguration.set("ultimate.effects." + i + ".strength", effect.strength);
                fileConfiguration.set("ultimate.effects." + i + ".range", effect.range);
                fileConfiguration.set("ultimate.effects." + i + ".tickEveryServerTicks", effect.tickEveryServerTicks);
                fileConfiguration.set("ultimate.effects." + i + ".longevity", effect.longevity);
                fileConfiguration.set("ultimate.effects." + i + ".delay", effect.delay);
                fileConfiguration.set("ultimate.effects." + i + ".removeOnDeath", effect.removeOnDeath);
                fileConfiguration.set("ultimate.effects." + i + ".soundSerializedString", effect.soundSerializedString);
                fileConfiguration.set("ultimate.effects." + i + ".particleSerializedString", effect.particleSerializedString);
                fileConfiguration.set("ultimate.effects." + i + ".particleSpawnLocation", effect.particleSpawnLocation);
                i++;
            }

            i = 0;
            for(Effect effect : playerClass.loadout.mainWeapon.effectList) {
                fileConfiguration.set("loadout.mainWeapon.effects." + i + ".effectType", effect.effectType);
                fileConfiguration.set("loadout.mainWeapon.effects." + i + ".effectApplicationType", effect.effectApplicationType);
                fileConfiguration.set("loadout.mainWeapon.effects." + i + ".strength", effect.strength);
                fileConfiguration.set("loadout.mainWeapon.effects." + i + ".range", effect.range);
                fileConfiguration.set("loadout.mainWeapon.effects." + i + ".tickEveryServerTicks", effect.tickEveryServerTicks);
                fileConfiguration.set("loadout.mainWeapon.effects." + i + ".longevity", effect.longevity);
                fileConfiguration.set("loadout.mainWeapon.effects." + i + ".delay", effect.delay);
                fileConfiguration.set("loadout.mainWeapon.effects." + i + ".removeOnDeath", effect.removeOnDeath);
                fileConfiguration.set("loadout.mainWeapon.effects." + i + ".soundSerializedString", effect.soundSerializedString);
                fileConfiguration.set("loadout.mainWeapon.effects." + i + ".particleSerializedString", effect.particleSerializedString);
                fileConfiguration.set("loadout.mainWeapon.effects." + i + ".particleSpawnLocation", effect.particleSpawnLocation);
                i++;
            }

            i = 0;
            for(Effect effect : playerClass.loadout.secondaryWeapon.effectList) {
                fileConfiguration.set("loadout.secondaryWeapon.effects." + i + ".effectType", effect.effectType);
                fileConfiguration.set("loadout.secondaryWeapon.effects." + i + ".effectApplicationType", effect.effectApplicationType);
                fileConfiguration.set("loadout.secondaryWeapon.effects." + i + ".strength", effect.strength);
                fileConfiguration.set("loadout.secondaryWeapon.effects." + i + ".range", effect.range);
                fileConfiguration.set("loadout.secondaryWeapon.effects." + i + ".tickEveryServerTicks", effect.tickEveryServerTicks);
                fileConfiguration.set("loadout.secondaryWeapon.effects." + i + ".longevity", effect.longevity);
                fileConfiguration.set("loadout.secondaryWeapon.effects." + i + ".delay", effect.delay);
                fileConfiguration.set("loadout.secondaryWeapon.effects." + i + ".removeOnDeath", effect.removeOnDeath);
                fileConfiguration.set("loadout.secondaryWeapon.effects." + i + ".soundSerializedString", effect.soundSerializedString);
                fileConfiguration.set("loadout.secondaryWeapon.effects." + i + ".particleSerializedString", effect.particleSerializedString);
                fileConfiguration.set("loadout.secondaryWeapon.effects." + i + ".particleSpawnLocation", effect.particleSpawnLocation);
                i++;
            }

            for(int j = 0; j < 8; j++) {
                i = 0;
                UpgradeTreeLocation upgradeTreeLocation = UpgradeTreeLocationConverter.convertIntegerToLocation(i);
                for(Upgrade upgrade : playerClass.upgradeTree.getUpgrade(upgradeTreeLocation)) {
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".name", upgrade.name);
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".description", upgrade.description);
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".maxLevels", upgrade.maxLevels);
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".upgradeAffection", upgrade.upgradeAffection);
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".upgradeType", upgrade.upgradeType);
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".maxStacks", upgrade.maxStacks);
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".guiMaterial", upgrade.guiMaterial);

                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".passiveAffection", playerClass.passiveSkill.upgradeAffectingWhichEffectList.get(upgradeTreeLocation));
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".activeAffection", playerClass.activeSkill.upgradeAffectingWhichEffectList.get(upgradeTreeLocation));
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".ultimateAffection", playerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(upgradeTreeLocation));
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".mainWeaponAffection", playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(upgradeTreeLocation));
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".secondaryWeaponAffection", playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(upgradeTreeLocation));
                    i++;
                }
            }

            try {
                fileConfiguration.save(configFile);
                ConsoleDebugSending.send(
                    "send-save-messages",
                    MiniMessageSerializers.deserializeToComponent("<#14db4c>Successfully saved the<#ffffff>" + playerClass.name + "<#14db4c>player class!")
                );
            } catch (IOException e) {
                ConsoleDebugSending.send(
                    "send-save-messages",
                    MiniMessageSerializers.deserializeToComponent("<#cc2b2b>Failed to save the<#ffffff>" + playerClass.name + "<#cc2b2b>player class!")
                );
                throw new RuntimeException(e);
            }

        }

    }

    public static void load() {

        Plugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        try {
            Files.createDirectories(Paths.get(plugin.getDataFolder() + File.separator + "classes"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(!PlayerClassDefaultClasses.load()) return;

        ConsoleDebugSending.send(
            "send-load-messages",
            MiniMessageSerializers.deserializeToComponent("<#dbd814>Loading Player Classes...")
        );

        File[] configFolder = new File(plugin.getDataFolder() + File.separator + "classes").listFiles();

        PlayerClassManager.getList().clear();

        for(File configFile : configFolder) {

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
                    SkillType.valueOf(fileConfiguration.getString("passive.skillType")),
                    fileConfiguration.getDouble("passive.cooldown")
                ),
                new Skill(
                    fileConfiguration.getString("active.name"),
                    fileConfiguration.getString("active.description"),
                    SkillType.valueOf(fileConfiguration.getString("active.skillType")),
                    fileConfiguration.getDouble("active.cooldown")
                ),
                new Skill(
                    fileConfiguration.getString("ultimate.name"),
                    fileConfiguration.getString("ultimate.description"),
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
                    new ArrayList<>() {{
                        for(String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("loadout.additionalItems")).getKeys(false)) {
                            add(fileConfiguration.getItemStack("loadout.additionalItems." + path));
                        }
                    }},
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
                    playerClass.loadout.secondaryWeapon.effectList.add(effect);
                    playerClass.loadout.secondaryWeapon.specialEffectPropertyList.add(UpgradeSpecialEffectProperty.valueOf(fileConfiguration.getString("loadout.secondaryWeapon.effects." + path + ".specialEffectProperty")));
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
                                Material.valueOf(fileConfiguration.getString("upgradeTree." + path + "." + path2 + ".guiMaterial"))
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

            playerClass.upgradeTree = upgradeTree;
            PlayerClassManager.getList().add(playerClass);
            ConsoleDebugSending.send(
                "send-load-messages",
                MiniMessageSerializers.deserializeToComponent("<#14db4c>Successfully loaded the<#ffffff>" + playerClass.name + "<#14db4c>player class!")
            );
        }

    }

}
