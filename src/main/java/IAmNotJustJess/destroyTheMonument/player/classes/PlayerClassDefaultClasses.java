package IAmNotJustJess.destroyTheMonument.player.classes;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.player.classes.items.Weapon;
import IAmNotJustJess.destroyTheMonument.player.classes.items.WeaponType;
import IAmNotJustJess.destroyTheMonument.player.classes.skills.Skill;
import IAmNotJustJess.destroyTheMonument.player.classes.skills.SkillType;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.*;
import IAmNotJustJess.destroyTheMonument.utility.ConsoleDebugSending;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerClassDefaultClasses {
    public static void load() {
        Plugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        File folder = new File(plugin.getDataFolder() + File.separator + "classes");

        if(!(Objects.isNull(folder.listFiles()) && Objects.requireNonNull(folder.listFiles()).length == 0)) {
            ConsoleDebugSending.send("send-load-messages", "<#14db4c>Located player classes files!");
            return;
        }

        ConsoleDebugSending.send("send-load-messages", "<#dbd814>Couldn't locate any player classes files! Creating default configuration.");

        PlayerClass playerClass = new PlayerClass(
            "Knight",
            "A mighty warrior, master of swordplay.",
            PlayerClassType.ATTACK,
            100,
            1.0f,
            new Skill(
                "Might",
                "",
                SkillType.PASSIVE,
                5.0
            ),
            new Skill(
                "",
                "",
                SkillType.ACTIVE,
                12.5
            ),
            new Skill(
                "",
                "",
                SkillType.ACTIVE,
                75.0
            ),
            new Loadout(
                new ItemStack(Material.IRON_HELMET),
                new ItemStack(Material.IRON_CHESTPLATE),
                new ItemStack(Material.LEATHER_LEGGINGS),
                new ItemStack(Material.CHAINMAIL_BOOTS),
                new Weapon(
                    "",
                    "",
                    WeaponType.MAIN_MELEE,
                    new ItemStack(Material.IRON_SWORD),
                    25,
                    0.0
                ),
                new Weapon(
                    "",
                    "",
                    WeaponType.SECONDARY_MELEE,
                    new ItemStack(Material.AIR),
                    0,
                    0.0
                ),
                45
            ),
            new UpgradeTree() {{
                setUpgrade(UpgradeTreeLocation.BASIC_ONE, new ArrayList<>(){{
                    add(new Upgrade(
                        "Sharpened Blade",
                        "Increases the damage dealt by<newline> the main weapon, by <0>.",
                        3,
                        UpgradeAffection.MAIN_WEAPON_DAMAGE,
                        UpgradeType.FLAT,
                        0,
                        Material.FLINT
                    )); // finish
                }});
            }},
            Material.IRON_SWORD
        );

        PlayerClassManager.getList().add(playerClass);

        PlayerClassFileHandler.save();

    }
}
