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
import IAmNotJustJess.destroyTheMonument.utility.QuickSendingMethods;
import IAmNotJustJess.destroyTheMonument.utility.UpgradeTreeLocationConverter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
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
        QuickSendingMethods.sendToConsole(
            "send-save-messages",
            "<#dbd814>Saving player classes..."
        );

        for(PlayerClass playerClass : PlayerClassManager.getList()) {

            File configFile = new File(plugin.getDataFolder() + File.separator + "classes" + File.separator + playerClass.name.toLowerCase() + ".yml");
            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

            fileConfiguration.set("name", playerClass.name);
            fileConfiguration.set("description", playerClass.description);
            fileConfiguration.set("player-class-type", playerClass.playerClassType.toString());
            fileConfiguration.set("hp", playerClass.healthPoints);
            fileConfiguration.set("movement-speed", playerClass.movementSpeed);

            fileConfiguration.set("passive.name", playerClass.passiveSkill.name);
            fileConfiguration.set("passive.description", playerClass.passiveSkill.description);
            fileConfiguration.set("passive.skill-type", playerClass.passiveSkill.skillType.toString());
            fileConfiguration.set("passive.cooldown", playerClass.passiveSkill.cooldown);

            fileConfiguration.set("active.name", playerClass.activeSkill.name);
            fileConfiguration.set("active.description", playerClass.activeSkill.description);
            fileConfiguration.set("active.skill-type", playerClass.activeSkill.skillType.toString());
            fileConfiguration.set("active.cooldown", playerClass.activeSkill.cooldown);

            fileConfiguration.set("ultimate.name", playerClass.ultimateSkill.name);
            fileConfiguration.set("ultimate.description", playerClass.ultimateSkill.description);
            fileConfiguration.set("ultimate.skill-type", playerClass.ultimateSkill.skillType.toString());
            fileConfiguration.set("ultimate.cooldown", playerClass.ultimateSkill.cooldown);

            fileConfiguration.set("loadout.helmet", playerClass.loadout.helmet);
            fileConfiguration.set("loadout.chestplate", playerClass.loadout.chestplate);
            fileConfiguration.set("loadout.leggings", playerClass.loadout.leggings);
            fileConfiguration.set("loadout.boots", playerClass.loadout.boots);

            fileConfiguration.set("loadout.main-weapon.name", playerClass.loadout.mainWeapon.name);
            fileConfiguration.set("loadout.main-weapon.description", playerClass.loadout.mainWeapon.description);
            fileConfiguration.set("loadout.main-weapon.weapon-type", playerClass.loadout.mainWeapon.weaponType.toString());
            fileConfiguration.set("loadout.main-weapon.item-stack", playerClass.loadout.mainWeapon.item);
            fileConfiguration.set("loadout.main-weapon.cooldown", playerClass.loadout.mainWeapon.cooldown);

            fileConfiguration.set("loadout.secondary-weapon.name", playerClass.loadout.secondaryWeapon.name);
            fileConfiguration.set("loadout.secondary-weapon.description", playerClass.loadout.secondaryWeapon.description);
            fileConfiguration.set("loadout.secondary-weapon.weapon-type", playerClass.loadout.secondaryWeapon.weaponType.toString());
            fileConfiguration.set("loadout.secondary-weapon.item-stack", playerClass.loadout.secondaryWeapon.item);
            fileConfiguration.set("loadout.secondary-weapon.cooldown", playerClass.loadout.secondaryWeapon.cooldown);

            fileConfiguration.set("loadout.block-amount", playerClass.loadout.blockAmount);

            fileConfiguration.set("gui-material", playerClass.guiMaterial.toString());

            int i = 0;
            for(ItemStack itemStack : playerClass.loadout.additionalItems) {
                fileConfiguration.set("loadout.additional-items."+i, itemStack);
                i++;
            }

            i = 0;
            for(Effect effect : playerClass.passiveSkill.effectList) {
                fileConfiguration.set("passive.effects." + i + ".effect-type", effect.effectType.toString());
                fileConfiguration.set("passive.effects." + i + ".effect-application-type", effect.effectApplicationType.toString());
                fileConfiguration.set("passive.effects." + i + ".strength", effect.strength);
                fileConfiguration.set("passive.effects." + i + ".range", effect.range);
                fileConfiguration.set("passive.effects." + i + ".tick-every-server-ticks", effect.tickEveryServerTicks);
                fileConfiguration.set("passive.effects." + i + ".longevity", effect.longevity);
                fileConfiguration.set("passive.effects." + i + ".delay", effect.delay);
                fileConfiguration.set("passive.effects." + i + ".remove-on-death", effect.removeOnDeath);
                fileConfiguration.set("passive.effects." + i + ".sound-serialized-string", effect.soundSerializedString);
                fileConfiguration.set("passive.effects." + i + ".particle-sSerialized-string", effect.particleSerializedString);
                fileConfiguration.set("passive.effects." + i + ".particle-spawn-location", effect.particleSpawnLocation.toString());
                i++;
            }

            i = 0;
            for(Effect effect : playerClass.activeSkill.effectList) {
                fileConfiguration.set("active.effects." + i + ".effect-type", effect.effectType.toString());
                fileConfiguration.set("active.effects." + i + ".effect-application-type", effect.effectApplicationType.toString());
                fileConfiguration.set("active.effects." + i + ".strength", effect.strength);
                fileConfiguration.set("active.effects." + i + ".range", effect.range);
                fileConfiguration.set("active.effects." + i + ".tick-every-server-ticks", effect.tickEveryServerTicks);
                fileConfiguration.set("active.effects." + i + ".longevity", effect.longevity);
                fileConfiguration.set("active.effects." + i + ".delay", effect.delay);
                fileConfiguration.set("active.effects." + i + ".remove-on-death", effect.removeOnDeath);
                fileConfiguration.set("active.effects." + i + ".sound-serialized-string", effect.soundSerializedString);
                fileConfiguration.set("active.effects." + i + ".particle-serialized-string", effect.particleSerializedString);
                fileConfiguration.set("active.effects." + i + ".particle-spawn-location", effect.particleSpawnLocation.toString());
                i++;
            }

            i = 0;
            for(Effect effect : playerClass.ultimateSkill.effectList) {
                fileConfiguration.set("ultimate.effects." + i + ".effect-type", effect.effectType.toString());
                fileConfiguration.set("ultimate.effects." + i + ".effect-application-type", effect.effectApplicationType.toString());
                fileConfiguration.set("ultimate.effects." + i + ".strength", effect.strength);
                fileConfiguration.set("ultimate.effects." + i + ".range", effect.range);
                fileConfiguration.set("ultimate.effects." + i + ".tick-every-server-ticks", effect.tickEveryServerTicks);
                fileConfiguration.set("ultimate.effects." + i + ".longevity", effect.longevity);
                fileConfiguration.set("ultimate.effects." + i + ".delay", effect.delay);
                fileConfiguration.set("ultimate.effects." + i + ".remove-on-death", effect.removeOnDeath);
                fileConfiguration.set("ultimate.effects." + i + ".sound-serialized-string", effect.soundSerializedString);
                fileConfiguration.set("ultimate.effects." + i + ".particle-serialized-string", effect.particleSerializedString);
                fileConfiguration.set("ultimate.effects." + i + ".particle-spawn-location", effect.particleSpawnLocation.toString());
                i++;
            }

            i = 0;
            for(Effect effect : playerClass.loadout.mainWeapon.effectList) {
                fileConfiguration.set("loadout.main-weapon.effects." + i + ".effect-type", effect.effectType.toString());
                fileConfiguration.set("loadout.main-weapon.effects." + i + ".effect-application-type", effect.effectApplicationType.toString());
                fileConfiguration.set("loadout.main-weapon.effects." + i + ".strength", effect.strength);
                fileConfiguration.set("loadout.main-weapon.effects." + i + ".range", effect.range);
                fileConfiguration.set("loadout.main-weapon.effects." + i + ".tick-every-server-ticks", effect.tickEveryServerTicks);
                fileConfiguration.set("loadout.main-weapon.effects." + i + ".longevity", effect.longevity);
                fileConfiguration.set("loadout.main-weapon.effects." + i + ".delay", effect.delay);
                fileConfiguration.set("loadout.main-weapon.effects." + i + ".remove-on-death", effect.removeOnDeath);
                fileConfiguration.set("loadout.main-weapon.effects." + i + ".sound-serialized-string", effect.soundSerializedString);
                fileConfiguration.set("loadout.main-weapon.effects." + i + ".particle-serialized-string", effect.particleSerializedString);
                fileConfiguration.set("loadout.main-weapon.effects." + i + ".particle-spawn-location", effect.particleSpawnLocation.toString());
                i++;
            }

            i = 0;
            for(Effect effect : playerClass.loadout.secondaryWeapon.effectList) {
                fileConfiguration.set("loadout.secondary-weapon.effects." + i + ".effect-type", effect.effectType.toString());
                fileConfiguration.set("loadout.secondary-weapon.effects." + i + ".effect-application-type", effect.effectApplicationType.toString());
                fileConfiguration.set("loadout.secondary-weapon.effects." + i + ".strength", effect.strength);
                fileConfiguration.set("loadout.secondary-weapon.effects." + i + ".range", effect.range);
                fileConfiguration.set("loadout.secondary-weapon.effects." + i + ".tick-every-server-ticks", effect.tickEveryServerTicks);
                fileConfiguration.set("loadout.secondary-weapon.effects." + i + ".longevity", effect.longevity);
                fileConfiguration.set("loadout.secondary-weapon.effects." + i + ".delay", effect.delay);
                fileConfiguration.set("loadout.secondary-weapon.effects." + i + ".remove-on-death", effect.removeOnDeath);
                fileConfiguration.set("loadout.secondary-weapon.effects." + i + ".sound-serialized-string", effect.soundSerializedString);
                fileConfiguration.set("loadout.secondary-weapon.effects." + i + ".particle-serialized-string", effect.particleSerializedString);
                fileConfiguration.set("loadout.secondary-weapon.effects." + i + ".particle-spawn-location", effect.particleSpawnLocation.toString());
                i++;
            }

            for(int j = 0; j < 8; j++) {
                i = 0;
                UpgradeTreeLocation upgradeTreeLocation = UpgradeTreeLocationConverter.convertIntegerToLocation(j);
                for(Upgrade upgrade : playerClass.upgradeTree.getUpgrade(upgradeTreeLocation)) {
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".name", upgrade.name);
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".description", upgrade.description);
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".max-levels", upgrade.maxLevels);
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".upgrade-affection", upgrade.upgradeAffection.toString());
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".upgrade-type", upgrade.upgradeType.toString());
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".max-stacks", upgrade.maxStacks);
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".gui-material", upgrade.guiMaterial.toString());

                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".passive-affection", playerClass.passiveSkill.upgradeAffectingWhichEffectList.get(upgradeTreeLocation));
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".active-affection", playerClass.activeSkill.upgradeAffectingWhichEffectList.get(upgradeTreeLocation));
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".ultimate-affection", playerClass.ultimateSkill.upgradeAffectingWhichEffectList.get(upgradeTreeLocation));
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".main-weapon-affection", playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.get(upgradeTreeLocation));
                    fileConfiguration.set("upgradeTree." + upgradeTreeLocation + "." + i + ".secondary-weapon-affection", playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.get(upgradeTreeLocation));
                    i++;
                }
            }

            try {
                fileConfiguration.save(configFile);
                QuickSendingMethods.sendToConsole(
                    "send-save-messages",
                    "<#14db4c>Successfully saved the <#ffffff>" + playerClass.name + " <#14db4c>player class!"
                );
            } catch (IOException e) {
                QuickSendingMethods.sendToConsole(
                    "send-save-messages",
                    "<#cc2b2b>Failed to save the <#ffffff>" + playerClass.name + " <#cc2b2b>player class!"
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

        PlayerClassDefaultClasses.load();

        QuickSendingMethods.sendToConsole(
            "send-load-messages",
            "<#dbd814>Loading player classes..."
        );

        File[] configFolder = new File(plugin.getDataFolder() + File.separator + "classes").listFiles();

        PlayerClassManager.getList().clear();

        assert configFolder != null;
        for(File configFile : configFolder) {

            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

            PlayerClass playerClass = new PlayerClass(
                fileConfiguration.getString("name"),
                fileConfiguration.getString("description"),
                PlayerClassType.valueOf(fileConfiguration.getString("player-class-type")),
                fileConfiguration.getInt("hp"),
                Float.parseFloat(Objects.requireNonNull(fileConfiguration.getString("movement-speed"))),
                new Skill(
                    fileConfiguration.getString("passive.name"),
                    fileConfiguration.getString("passive.description"),
                    SkillType.valueOf(fileConfiguration.getString("passive.skill-type")),
                    fileConfiguration.getDouble("passive.cooldown")
                ),
                new Skill(
                    fileConfiguration.getString("active.name"),
                    fileConfiguration.getString("active.description"),
                    SkillType.valueOf(fileConfiguration.getString("active.skill-type")),
                    fileConfiguration.getDouble("active.cooldown")
                ),
                new Skill(
                    fileConfiguration.getString("ultimate.name"),
                    fileConfiguration.getString("ultimate.description"),
                    SkillType.valueOf(fileConfiguration.getString("ultimate.skill-type")),
                    fileConfiguration.getDouble("ultimate.cooldown")
                ),
                new Loadout(
                    fileConfiguration.getItemStack("loadout.helmet"),
                    fileConfiguration.getItemStack("loadout.chestplate"),
                    fileConfiguration.getItemStack("loadout.leggings"),
                    fileConfiguration.getItemStack("loadout.boots"),
                    new Weapon(
                        fileConfiguration.getString("loadout.main-weapon.name"),
                        fileConfiguration.getString("loadout.main-weapon.description"),
                        WeaponType.valueOf(fileConfiguration.getString("loadout.main-weapon.weapon-type")),
                        fileConfiguration.getItemStack("loadout.main-weapon.item-stack"),
                        fileConfiguration.getInt("loadout.main-weapon.damage"),
                        fileConfiguration.getDouble("loadout.main-weapon.cooldown")
                    ),
                    new Weapon(
                        fileConfiguration.getString("loadout.secondary-weapon.name"),
                        fileConfiguration.getString("loadout.secondary-weapon.description"),
                        WeaponType.valueOf(fileConfiguration.getString("loadout.secondary-weapon.weapon-type")),
                        fileConfiguration.getItemStack("loadout.secondary-weapon.item-stack"),
                        fileConfiguration.getInt("loadout.secondary-weapon.damage"),
                        fileConfiguration.getDouble("loadout.secondary-weapon.cooldown")
                    ),
                    new ArrayList<>() {{
                        for(String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("loadout.additional-items")).getKeys(false)) {
                            add(fileConfiguration.getItemStack("loadout.additional-items." + path));
                        }
                    }},
                    fileConfiguration.getInt("loadout.block-amount")
                ),
                new UpgradeTree(),
                Material.valueOf(fileConfiguration.getString("gui-material"))
            );

            if(fileConfiguration.getConfigurationSection("passive.effects") != null) {
                if(!Objects.requireNonNull(fileConfiguration.getConfigurationSection("passive.effects")).getKeys(false).isEmpty()) {
                    for(String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("passive.effects")).getKeys(false)) {
                        Effect effect = new Effect(
                            EffectType.valueOf(fileConfiguration.getString("passive.effects." + path + ".effect-type")),
                            EffectApplicationType.valueOf(fileConfiguration.getString("passive.effects." + path + ".effect-application-type")),
                            fileConfiguration.getDouble("passive.effects." + path + ".strength"),
                            fileConfiguration.getDouble("passive.effects." + path + ".range"),
                            fileConfiguration.getInt("passive.effects." + path + ".tick-every-server-ticks"),
                            fileConfiguration.getInt("passive.effects." + path + ".longevity"),
                            fileConfiguration.getLong("passive.effects." + path + ".delay"),
                            fileConfiguration.getBoolean("passive.effects." + path + ".remove-on-death"),
                            fileConfiguration.getString("passive.effects." + path + ".sound-serialized-string"),
                            fileConfiguration.getString("passive.effects." + path + ".particle-serialized-string"),
                            EffectParticleSpawnLocation.valueOf(fileConfiguration.getString("passive.effects." + path + ".particle-spawn-location"))
                        );
                        playerClass.passiveSkill.effectList.add(effect);
                    }
                }
            }

            if(fileConfiguration.getConfigurationSection("active.effects") != null) {
                if (!Objects.requireNonNull(fileConfiguration.getConfigurationSection("active.effects")).getKeys(false).isEmpty()) {
                    for (String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("active.effects")).getKeys(false)) {
                        Effect effect = new Effect(
                            EffectType.valueOf(fileConfiguration.getString("active.effects." + path + ".effect-type")),
                            EffectApplicationType.valueOf(fileConfiguration.getString("active.effects." + path + ".effect-application-type")),
                            fileConfiguration.getDouble("active.effects." + path + ".strength"),
                            fileConfiguration.getDouble("active.effects." + path + ".range"),
                            fileConfiguration.getInt("active.effects." + path + ".tick-every-server-ticks"),
                            fileConfiguration.getInt("active.effects." + path + ".longevity"),
                            fileConfiguration.getLong("active.effects." + path + ".delay"),
                            fileConfiguration.getBoolean("active.effects." + path + ".remove-on-death"),
                            fileConfiguration.getString("active.effects." + path + ".sound-serialized-string"),
                            fileConfiguration.getString("active.effects." + path + ".particle-serialized-string"),
                            EffectParticleSpawnLocation.valueOf(fileConfiguration.getString("active.effects." + path + ".particle-spawn-location"))
                        );
                        playerClass.activeSkill.effectList.add(effect);
                    }
                }
            }

            if(fileConfiguration.getConfigurationSection("ultimate.effects") != null) {
                if (!Objects.requireNonNull(fileConfiguration.getConfigurationSection("ultimate.effects")).getKeys(false).isEmpty()) {
                    for (String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("ultimate.effects")).getKeys(false)) {
                        Effect effect = new Effect(
                            EffectType.valueOf(fileConfiguration.getString("ultimate.effects." + path + ".effect-type")),
                            EffectApplicationType.valueOf(fileConfiguration.getString("ultimate.effects." + path + ".effect-application-type")),
                            fileConfiguration.getDouble("ultimate.effects." + path + ".strength"),
                            fileConfiguration.getDouble("ultimate.effects." + path + ".range"),
                            fileConfiguration.getInt("ultimate.effects." + path + ".tick-every-server-ticks"),
                            fileConfiguration.getInt("ultimate.effects." + path + ".longevity"),
                            fileConfiguration.getLong("ultimate.effects." + path + ".delay"),
                            fileConfiguration.getBoolean("ultimate.effects." + path + ".remove-on-death"),
                            fileConfiguration.getString("ultimate.effects." + path + ".sound-serialized-string"),
                            fileConfiguration.getString("ultimate.effects." + path + ".particle-serialized-string"),
                            EffectParticleSpawnLocation.valueOf(fileConfiguration.getString("ultimate.effects." + path + ".particle-spawn-location"))
                        );
                        playerClass.ultimateSkill.effectList.add(effect);
                    }
                }
            }

            if(fileConfiguration.getConfigurationSection("loadout.main-weapon.effects") != null) {
                if (!Objects.requireNonNull(fileConfiguration.getConfigurationSection("loadout.main-weapon.effects")).getKeys(false).isEmpty()) {
                    for (String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("loadout.main-weapon.effects")).getKeys(false)) {
                        Effect effect = new Effect(
                            EffectType.valueOf(fileConfiguration.getString("loadout.main-weapon.effects." + path + ".effect-type")),
                            EffectApplicationType.valueOf(fileConfiguration.getString("loadout.main-weapon.effects." + path + ".effect-application-type")),
                            fileConfiguration.getDouble("loadout.main-weapon.effects." + path + ".strength"),
                            fileConfiguration.getDouble("loadout.main-weapon.effects." + path + ".range"),
                            fileConfiguration.getInt("loadout.main-weapon.effects." + path + ".tick-every-server-ticks"),
                            fileConfiguration.getInt("loadout.main-weapon.effects." + path + ".longevity"),
                            fileConfiguration.getLong("loadout.main-weapon.effects." + path + ".delay"),
                            fileConfiguration.getBoolean("loadout.main-weapon.effects." + path + ".remove-on-death"),
                            fileConfiguration.getString("loadout.main-weapon.effects." + path + ".sound-serialized-string"),
                            fileConfiguration.getString("loadout.main-weapon.effects." + path + ".particle-serialized-string"),
                            EffectParticleSpawnLocation.valueOf(fileConfiguration.getString("loadout.main-weapon.effects." + path + ".particle-spawn-location"))
                        );
                        playerClass.loadout.mainWeapon.effectList.add(effect);
                        playerClass.loadout.mainWeapon.specialEffectPropertyList.add(UpgradeSpecialEffectProperty.valueOf(fileConfiguration.getString("loadout.main-weapon.effects." + path + ".specialEffectProperty")));
                    }
                }
            }

            if(fileConfiguration.getConfigurationSection("loadout.secondary-weapon.effects") != null) {
                if (!Objects.requireNonNull(fileConfiguration.getConfigurationSection("loadout.secondary-weapon.effects")).getKeys(false).isEmpty()) {
                    for (String path : Objects.requireNonNull(fileConfiguration.getConfigurationSection("loadout.secondary-weapon.effects")).getKeys(false)) {
                        Effect effect = new Effect(
                            EffectType.valueOf(fileConfiguration.getString("loadout.secondary-weapon.effects." + path + ".effect-type")),
                            EffectApplicationType.valueOf(fileConfiguration.getString("loadout.secondary-weapon.effects." + path + ".effect-application-type")),
                            fileConfiguration.getDouble("loadout.secondary-weapon.effects." + path + ".strength"),
                            fileConfiguration.getDouble("loadout.secondary-weapon.effects." + path + ".range"),
                            fileConfiguration.getInt("loadout.secondary-weapon.effects." + path + ".tick-every-server-ticks"),
                            fileConfiguration.getInt("loadout.secondary-weapon.effects." + path + ".longevity"),
                            fileConfiguration.getLong("loadout.secondary-weapon.effects." + path + ".delay"),
                            fileConfiguration.getBoolean("loadout.secondary-weapon.effects." + path + ".remove-on-death"),
                            fileConfiguration.getString("loadout.secondary-weapon.effects." + path + ".sound-serialized-string"),
                            fileConfiguration.getString("loadout.secondary-weapon.effects." + path + ".particle-serialized-string"),
                            EffectParticleSpawnLocation.valueOf(fileConfiguration.getString("loadout.secondary-weapon.effects." + path + ".particle-spawn-location"))
                        );
                        playerClass.loadout.secondaryWeapon.effectList.add(effect);
                        playerClass.loadout.secondaryWeapon.specialEffectPropertyList.add(UpgradeSpecialEffectProperty.valueOf(fileConfiguration.getString("loadout.secondary-weapon.effects." + path + ".specialEffectProperty")));
                    }
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
                                fileConfiguration.getInt("upgradeTree." + path + "." + path2 + ".max-levels"),
                                UpgradeAffection.valueOf(fileConfiguration.getString("upgradeTree." + path + "." + path2 + ".upgrade-affection")),
                                UpgradeType.valueOf(fileConfiguration.getString("upgradeTree." + path + "." + path2 + ".upgrade-type")),
                                fileConfiguration.getInt("upgradeTree." + path + "." + path2 + ".max-stacks"),
                                Material.valueOf(fileConfiguration.getString("upgradeTree." + path + "." + path2 + ".gui-material"))
                            );
                            upgrades.add(upgrade);
                            playerClass.passiveSkill.upgradeAffectingWhichEffectList.put(upgradeTreeLocation, (ArrayList<Integer>) fileConfiguration.getIntegerList("upgradeTree." + path + "." + path2 + "." + "passive-affection"));
                            playerClass.activeSkill.upgradeAffectingWhichEffectList.put(upgradeTreeLocation, (ArrayList<Integer>) fileConfiguration.getIntegerList("upgradeTree." + path + "." + path2 + "." + "active-affection"));
                            playerClass.ultimateSkill.upgradeAffectingWhichEffectList.put(upgradeTreeLocation, (ArrayList<Integer>) fileConfiguration.getIntegerList("upgradeTree." + path + "." + path2 + "." + "ultimate-affection"));
                            playerClass.loadout.mainWeapon.upgradeAffectingWhichEffectList.put(upgradeTreeLocation, (ArrayList<Integer>) fileConfiguration.getIntegerList("upgradeTree." + path + "." + path2 + "." + "main-weapon-affection"));
                            playerClass.loadout.secondaryWeapon.upgradeAffectingWhichEffectList.put(upgradeTreeLocation, (ArrayList<Integer>) fileConfiguration.getIntegerList("upgradeTree." + path + "." + path2 + "." + "secondary-weapon-affection"));
                        }
                    }
                    upgradeTree.setUpgrade(upgradeTreeLocation, upgrades);
                }
            }

            playerClass.upgradeTree = upgradeTree;
            PlayerClassManager.getList().add(playerClass);
            QuickSendingMethods.sendToConsole(
                "send-load-messages",
                "<#14db4c>Successfully loaded the<#ffffff> " + playerClass.name + " <#14db4c>player class!"
            );
        }

    }

}
