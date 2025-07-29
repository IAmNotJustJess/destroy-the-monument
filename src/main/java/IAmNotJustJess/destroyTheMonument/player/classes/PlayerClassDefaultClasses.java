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
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerClassDefaultClasses {
    public static boolean load() {
        Plugin plugin = JavaPlugin.getPlugin(DestroyTheMonument.class);

        File folder = new File(plugin.getDataFolder() + File.separator + "classes");

        if(!Objects.isNull(folder.listFiles()) && Objects.requireNonNull(folder.listFiles()).length > 1) {
            ConsoleDebugSending.send(
                "send-load-messages",
                "<#14db4c>Located player classes files!"
            );
            return false;
        }

        ConsoleDebugSending.send(
            "send-load-messages",
            "<#dbd814>Couldn't locate any player classes files! Creating default configuration."
        );

        PlayerClass playerClass = new PlayerClass(
            "Knight",
            "A mighty warrior, master of swordplay.",
            PlayerClassType.ATTACK,
            100,
            1.0f,
            new Skill(
                "Self-Heal",
                "Passively heal 5 Health Points every 5 seconds.",
                SkillType.PASSIVE,
                5.0
            ){{
                effectList.add(new Effect(
                    EffectType.HEAL_FLAT,
                    EffectApplicationType.APPLY_SELF,
                    5.0,
                    0.0,
                    0,
                    0,
                    0,
                    false,
                    "",
                    "",
                    EffectParticleSpawnLocation.USER
                ));

                upgradeAffectingWhichEffectList.put(UpgradeTreeLocation.SPECIAL_TWO, new ArrayList<>() {{
                    add(0);
                }});
            }},
            new Skill(
                "Pushback",
                "Deal 20 Damage to enemies around you in a 3 meter radius.<newline> At the same time push them away with a strength of 2.",
                SkillType.ACTIVE,
                12.5
            ) {{
                effectList.add(new Effect(
                    EffectType.DEAL_DAMAGE_FLAT,
                    EffectApplicationType.APPLY_ENEMIES_IN_RANGE_OF_CASTER,
                    20.0,
                    3.0,
                    0,
                    0,
                    0,
                    false,
                    "",
                    "",
                    EffectParticleSpawnLocation.USER
                ));
                effectList.add(new Effect(
                    EffectType.PUSH_AWAY,
                    EffectApplicationType.APPLY_ENEMIES_IN_RANGE_OF_CASTER,
                    2,
                    3.0,
                    0,
                    0,
                    0,
                    false,
                    "",
                    "",
                    EffectParticleSpawnLocation.USER
                ));
                upgradeAffectingWhichEffectList.put(UpgradeTreeLocation.BASIC_TWO, new ArrayList<>() {{
                    add(0);
                    add(1);
                }});
            }},
            new Skill(
                "Might",
                "",
                SkillType.ACTIVE,
                75.0
            ) {{
                effectList.add(new Effect(
                    EffectType.DAMAGE_INCREASE_MULTIPLIER,
                    EffectApplicationType.APPLY_SELF,
                    1.5,
                    0,
                    0,
                    15,
                    0,
                    false,
                    "",
                    "",
                    EffectParticleSpawnLocation.USER
                ));
                upgradeAffectingWhichEffectList.put(UpgradeTreeLocation.ULTIMATE_ONE, new ArrayList<>() {{
                    add(0);
                }});
                upgradeAffectingWhichEffectList.put(UpgradeTreeLocation.ULTIMATE_TWO, new ArrayList<>() {{
                    add(0);
                }});
            }},
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
                new ArrayList<>() {{
                    add(new ItemStack(Material.IRON_PICKAXE, 1));
                    add(new ItemStack(Material.STONE_AXE, 1));
                }},
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
                    ) {{
                        shardPricesPerLevelList.add(100);
                        shardPricesPerLevelList.add(200);
                        shardPricesPerLevelList.add(300);

                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(5.0);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(5.0);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(5.0);
                        }});

                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("5");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("10");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("15");
                        }});
                    }}); // finish
                }});
                setUpgrade(UpgradeTreeLocation.BASIC_TWO, new ArrayList<>(){{
                    add(new Upgrade(
                        "Increased Health",
                        "Increase the health by <0>%.",
                        5,
                        UpgradeAffection.MAX_HP,
                        UpgradeType.PERCENTAGE,
                        0,
                        Material.RED_DYE
                    ) {{
                        shardPricesPerLevelList.add(100);
                        shardPricesPerLevelList.add(150);
                        shardPricesPerLevelList.add(200);
                        shardPricesPerLevelList.add(250);
                        shardPricesPerLevelList.add(300);

                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(1.1);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(1.1);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(1.1);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(1.1);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(1.1);
                        }});

                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("10");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("20");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("30");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("40");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("50");
                        }});
                    }});
                }});
                setUpgrade(UpgradeTreeLocation.SKILL_ONE, new ArrayList<>(){{
                    add(new Upgrade(
                        "Lower Cooldown",
                        "Reduces Skill's Cooldown by <0> seconds.",
                        4,
                        UpgradeAffection.ACTIVE_COOLDOWN,
                        UpgradeType.FLAT,
                        0,
                        Material.CLOCK
                    ) {{
                        shardPricesPerLevelList.add(200);
                        shardPricesPerLevelList.add(275);
                        shardPricesPerLevelList.add(350);
                        shardPricesPerLevelList.add(425);

                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(-0.5);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(-0.5);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(-0.5);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(-0.5);
                        }});

                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("0.5");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("1.0");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("1.5");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("2.0");
                        }});
                    }});
                }});
                setUpgrade(UpgradeTreeLocation.SKILL_TWO, new ArrayList<>(){{
                    add(new Upgrade(
                        "More Power!",
                        "Increases damage dealt by the Skill, by <0>%. <newline>Additionally increases the push force strength by <1>.",
                        2,
                        UpgradeAffection.ACTIVE_EFFECT_STRENGTH,
                        UpgradeType.PERCENTAGE,
                        0,
                        Material.DIAMOND
                    ) {{
                        shardPricesPerLevelList.add(500);
                        shardPricesPerLevelList.add(755);

                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(1.25);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(1.25);
                        }});

                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("25");
                            add("0.5");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("50");
                            add("1.0");
                        }});
                    }});
                    add(new Upgrade(
                        "",
                        "",
                        2,
                        UpgradeAffection.ACTIVE_EFFECT_STRENGTH,
                        UpgradeType.FLAT,
                        0,
                        Material.AIR
                    ) {{
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(0.5);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(0.5);
                        }});
                    }});
                }});
                setUpgrade(UpgradeTreeLocation.SPECIAL_ONE, new ArrayList<>() {{
                    add(new Upgrade(
                        "Additional Skill Effect",
                        "Your Skill additionally applies <0> for <1> seconds to yourself.",
                        1,
                        UpgradeAffection.ACTIVE_ADD_NEW_EFFECT,
                        UpgradeType.FLAT,
                        0,
                        Material.AMETHYST_SHARD
                    ) {{
                        shardPricesPerLevelList.add(500);

                        effectsPerLevelList.add(new ArrayList<>() {{
                            add(new Effect(
                                EffectType.POTION_EFFECT,
                                EffectApplicationType.APPLY_SELF,
                                0.0,
                                0.0,
                                0,
                                0,
                                0,
                                false,
                                "",
                                "",
                                EffectParticleSpawnLocation.USER,
                                new PotionEffect(PotionEffectType.JUMP_BOOST, 100, 1, true, false)
                            ));
                        }});

                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("Jump Boost II");
                            add("5");
                        }});
                    }});
                }});
                setUpgrade(UpgradeTreeLocation.SPECIAL_TWO, new ArrayList<>() {{
                    add(new Upgrade(
                        "Faster Health Regeneration",
                        "Your Passive additionally heals <0> Health Points per tick.",
                        3,
                        UpgradeAffection.PASSIVE_EFFECT_STRENGTH,
                        UpgradeType.FLAT,
                        0,
                        Material.REDSTONE
                    ) {{
                        shardPricesPerLevelList.add(500);
                        shardPricesPerLevelList.add(550);
                        shardPricesPerLevelList.add(600);

                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(3.0);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(4.0);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(3.0);
                        }});

                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("3");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("7");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("10");
                        }});
                    }});
                }});
                setUpgrade(UpgradeTreeLocation.ULTIMATE_ONE, new ArrayList<>() {{
                    add(new Upgrade(
                        "Might Eternal",
                        "Increase the duration of might by <0> seconds.",
                        5,
                        UpgradeAffection.ULTIMATE_EFFECT_LONGEVITY,
                        UpgradeType.FLAT,
                        0,
                        Material.STICK
                    ) {{
                        shardPricesPerLevelList.add(400);
                        shardPricesPerLevelList.add(450);
                        shardPricesPerLevelList.add(500);
                        shardPricesPerLevelList.add(550);
                        shardPricesPerLevelList.add(600);

                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(3.0);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(3.0);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(3.0);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(3.0);
                        }});
                        strengthPerLevelList.add(new ArrayList<>() {{
                            add(3.0);
                        }});

                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("3.0");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("6.0");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("9.0");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("12.0");
                        }});
                        descriptionTextReplacementList.add(new ArrayList<>() {{
                            add("15.0");
                        }});
                    }});
                    setUpgrade(UpgradeTreeLocation.ULTIMATE_TWO, new ArrayList<>() {{
                        add(new Upgrade(
                            "Empowering Might",
                            "Every kill increases the multiplier of The Ultimate<newline>by <0>, up to <1>. Stack reset on death.",
                            3,
                            UpgradeAffection.ULTIMATE_EFFECT_STRENGTH,
                            UpgradeType.STACKING_FLAT_PER_KILL,
                            5,
                            Material.DIAMOND_SWORD
                        ) {{
                            shardPricesPerLevelList.add(700);
                            shardPricesPerLevelList.add(900);
                            shardPricesPerLevelList.add(1100);

                            strengthPerLevelList.add(new ArrayList<>() {{
                                add(0.03);
                                add(0.03);
                                add(0.03);
                                add(0.03);
                                add(0.03);
                            }});
                            strengthPerLevelList.add(new ArrayList<>() {{
                                add(0.07);
                                add(0.07);
                                add(0.07);
                                add(0.07);
                                add(0.07);
                            }});
                            strengthPerLevelList.add(new ArrayList<>() {{
                                add(0.1);
                                add(0.1);
                                add(0.1);
                                add(0.1);
                                add(0.1);
                            }});

                            descriptionTextReplacementList.add(new ArrayList<>() {{
                                add("0.03");
                                add("0.15");
                            }});
                            descriptionTextReplacementList.add(new ArrayList<>() {{
                                add("0.07");
                                add("0.35");
                            }});
                            descriptionTextReplacementList.add(new ArrayList<>() {{
                                add("0.1");
                                add("0.5");
                            }});
                        }});
                    }});
                }});
            }},
            Material.IRON_SWORD
        );

        PlayerClassManager.getList().add(playerClass);

        PlayerClassFileHandler.save();

        return true;
    }
}
