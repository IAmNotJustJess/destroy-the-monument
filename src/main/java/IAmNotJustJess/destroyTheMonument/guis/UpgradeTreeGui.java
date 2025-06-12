package IAmNotJustJess.destroyTheMonument.guis;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.Upgrade;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTree;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTreeLocation;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageParser;
import IAmNotJustJess.destroyTheMonument.utility.UpgradeTreeLocationConverter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpgradeTreeGui {
    public static void openGUI(PlayerCharacter player) {

        UpgradeTree upgradeTree = player.getChosenClass().upgradeTree;

        List<SimpleItem> items = new ArrayList<>();

        for(int i = 0; i < 8; i++) {
            Upgrade upgrade = upgradeTree.getUpgrade(UpgradeTreeLocationConverter.convertIntegerToLocation(i)).getFirst();
            String name = upgrade.getName();
            List<String> lore = new ArrayList<>();
            Material material = upgrade.getGuiMaterial();
            int amount = upgrade.getCurrentLevel();
            boolean set = false;
            switch(i) {
                case 1 -> {
                    if(upgradeTree.getUpgrade(UpgradeTreeLocation.BASIC_ONE).getFirst().getCurrentLevel() == 0) {
                        name = MainConfiguration.guiConfiguration.getString("upgrade-unavailable") + name;
                        lore.add(MainConfiguration.guiConfiguration.getString("upgrade-unavailable-text"));
                        material = Material.BARRIER;
                        set = true;
                    }
                }
                case 2, 4, 6 -> {
                    if(upgradeTree.getUpgrade(UpgradeTreeLocation.BASIC_TWO).getFirst().getCurrentLevel() == 0) {
                        name = MainConfiguration.guiConfiguration.getString("upgrade-unavailable") + name;
                        lore.add(MainConfiguration.guiConfiguration.getString("upgrade-unavailable-text"));
                        material = Material.BARRIER;
                        set = true;
                    }
                }
                case 3 -> {
                    if(upgradeTree.getUpgrade(UpgradeTreeLocation.SPECIAL_ONE).getFirst().getCurrentLevel() == 0) {
                        name = MainConfiguration.guiConfiguration.getString("upgrade-unavailable") + name;
                        lore.add(MainConfiguration.guiConfiguration.getString("upgrade-unavailable-text"));
                        material = Material.BARRIER;
                        set = true;
                    }
                }
                case 5 -> {
                    if(upgradeTree.getUpgrade(UpgradeTreeLocation.SKILL_ONE).getFirst().getCurrentLevel() == 0) {
                        name = MainConfiguration.guiConfiguration.getString("upgrade-unavailable") + name;
                        lore.add(MainConfiguration.guiConfiguration.getString("upgrade-unavailable-text"));
                        material = Material.BARRIER;
                        set = true;
                    }
                }
                case 7 -> {
                    if(upgradeTree.getUpgrade(UpgradeTreeLocation.ULTIMATE_ONE).getFirst().getCurrentLevel() == 0) {
                        name = MainConfiguration.guiConfiguration.getString("upgrade-unavailable") + name;
                        lore.add(MainConfiguration.guiConfiguration.getString("upgrade-unavailable-text"));
                        material = Material.BARRIER;
                        set = true;
                    }
                }
            }
            if(!set) {
                if(upgrade.getCurrentLevel() == upgrade.getMaxLevels()) {
                    name = MainConfiguration.guiConfiguration.getString("upgrade-maxed") + name;
                    lore.add(MainConfiguration.guiConfiguration.getString("upgrade-maxed-text"));
                }
                else if (player.getShards() >= upgrade.shardPricesPerLevelList.get(upgrade.getCurrentLevel())) {
                    name = MainConfiguration.guiConfiguration.getString("upgrade-available") + name;
                    lore.add(MainConfiguration.guiConfiguration.getString("upgrade-available-text"));
                }
                else {
                    name = MainConfiguration.guiConfiguration.getString("upgrade-not-enough-shards") + name;
                    lore.add(MainConfiguration.guiConfiguration.getString("upgrade-not-enough-shards-text"));
                }
            }
            lore.add(" ");
            lore.addAll(upgrade.cachedGuiDescriptionsForUpgrades.get(upgrade.getCurrentLevel()));
            if(amount < 0) amount = 1;
            name = MiniMessageParser.deserializeToString(name);
            SimpleItem simpleItem = new SimpleItem(
                new ItemBuilder(material, amount)
                    .setDisplayName(name)
                    .setLegacyLore(lore)
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
            );
            items.add(simpleItem);
        }

        SimpleItem border = new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""));

        Gui gui = Gui.normal() // Creates the GuiBuilder for a normal GUI
            .setStructure(
                "# . . . . . . . #",
                "# . 4 . 6 . 8 . #",
                "# . 3 . 5 . 7 . #",
                "# . . . 2 . . . #",
                "# . . . 1 . . . #",
                "# . . . . . . . #")
            .addIngredient('#', border)
            .addIngredient('1', items.get(0))
            .addIngredient('2', items.get(1))
            .addIngredient('3', items.get(2))
            .addIngredient('4', items.get(3))
            .addIngredient('5', items.get(4))
            .addIngredient('6', items.get(5))
            .addIngredient('7', items.get(6))
            .addIngredient('8', items.get(7))
            .build();

        Window window = Window.single()
                .setViewer(player.getPlayer())
                .setTitle("Upgrade your character!")
                .setGui(gui)
                .build();

        window.open();
    }
}
