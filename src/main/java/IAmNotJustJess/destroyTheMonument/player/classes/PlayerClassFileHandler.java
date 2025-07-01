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
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTree;
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
                Float.parseFloat(fileConfiguration.getString("movementSpeed")),
                new Skill(
                    fileConfiguration.getString("passive.name"),
                    fileConfiguration.getString("passive.description"),
                    (ArrayList<String>) fileConfiguration.getStringList("passive.descriptionTextReplacements"),
                    SkillType.valueOf(fileConfiguration.getString("passive.skillType")),
                    fileConfiguration.getDouble("passive.cooldown"),
                    new Effect(
                        EffectType.valueOf(fileConfiguration.getString("passive.effect.0.effectType")),
                        EffectApplicationType.valueOf(fileConfiguration.getString("passive.effect.0.effectApplicationType")),
                        fileConfiguration.getDouble("passive.effect.0.strength"),
                        fileConfiguration.getDouble("passive.effect.0.range"),
                        fileConfiguration.getInt("passive.effect.0.tickEveryServerTicks"),
                        fileConfiguration.getInt("passive.effect.0.longevity"),
                        fileConfiguration.getLong("passive.effect.0.delay"),
                        fileConfiguration.getBoolean("passive.effect.0.removeOnDeath"),
                        fileConfiguration.getString("passive.effect.0.soundSerializedString"),
                        fileConfiguration.getString("passive.effect.0.particleSerializedString"),
                        EffectParticleSpawnLocation.valueOf("passive.effect.0.particleSpawnLocation")
                    )
                ),
                new Skill(
                    fileConfiguration.getString("active.name"),
                    fileConfiguration.getString("active.description"),
                    (ArrayList<String>) fileConfiguration.getStringList("active.descriptionTextReplacements"),
                    SkillType.valueOf(fileConfiguration.getString("active.skillType")),
                    fileConfiguration.getDouble("active.cooldown"),
                    new Effect(
                        EffectType.valueOf(fileConfiguration.getString("active.effect.0.effectType")),
                        EffectApplicationType.valueOf(fileConfiguration.getString("active.effect.0.effectApplicationType")),
                        fileConfiguration.getDouble("active.effect.0.strength"),
                        fileConfiguration.getDouble("active.effect.0.range"),
                        fileConfiguration.getInt("active.effect.0.tickEveryServerTicks"),
                        fileConfiguration.getInt("active.effect.0.longevity"),
                        fileConfiguration.getLong("active.effect.0.delay"),
                        fileConfiguration.getBoolean("active.effect.0.removeOnDeath"),
                        fileConfiguration.getString("active.effect.0.soundSerializedString"),
                        fileConfiguration.getString("active.effect.0.particleSerializedString"),
                        EffectParticleSpawnLocation.valueOf("active.effect.0.particleSpawnLocation")
                    )
                ),
                new Skill(
                    fileConfiguration.getString("ultimate.name"),
                    fileConfiguration.getString("ultimate.description"),
                    (ArrayList<String>) fileConfiguration.getStringList("ultimate.descriptionTextReplacements"),
                    SkillType.valueOf(fileConfiguration.getString("ultimate.skillType")),
                    fileConfiguration.getDouble("ultimate.cooldown"),
                    new Effect(
                        EffectType.valueOf(fileConfiguration.getString("ultimate.effect.0.effectType")),
                        EffectApplicationType.valueOf(fileConfiguration.getString("ultimate.effect.0.effectApplicationType")),
                        fileConfiguration.getDouble("ultimate.effect.0.strength"),
                        fileConfiguration.getDouble("ultimate.effect.0.range"),
                        fileConfiguration.getInt("ultimate.effect.0.tickEveryServerTicks"),
                        fileConfiguration.getInt("ultimate.effect.0.longevity"),
                        fileConfiguration.getLong("ultimate.effect.0.delay"),
                        fileConfiguration.getBoolean("ultimate.effect.0.removeOnDeath"),
                        fileConfiguration.getString("ultimate.effect.0.soundSerializedString"),
                        fileConfiguration.getString("ultimate.effect.0.particleSerializedString"),
                        EffectParticleSpawnLocation.valueOf("ultimate.effect.0.particleSpawnLocation")
                    )
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
        }

        // add stuff for more effects and such

    }

}
