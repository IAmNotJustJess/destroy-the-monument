package IAmNotJustJess.destroyTheMonument.guis;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.guis.items.UpgradeGuiItem;
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

        SimpleItem border = new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""));

        Gui gui = Gui.normal()
            .setStructure(
                "# . . . . . . . #",
                "# . 4 . 6 . 8 . #",
                "# . 3 . 5 . 7 . #",
                "# . . . 2 . . . #",
                "# . . . 1 . . . #",
                "# . . . . . . . #")
            .addIngredient('#', border)
            .addIngredient('1', new UpgradeGuiItem().getItemProvider(UpgradeTreeLocation.BASIC_ONE, player))
            .addIngredient('2', new UpgradeGuiItem().getItemProvider(UpgradeTreeLocation.BASIC_TWO, player))
            .addIngredient('3', new UpgradeGuiItem().getItemProvider(UpgradeTreeLocation.SPECIAL_ONE, player))
            .addIngredient('4', new UpgradeGuiItem().getItemProvider(UpgradeTreeLocation.SPECIAL_TWO, player))
            .addIngredient('5', new UpgradeGuiItem().getItemProvider(UpgradeTreeLocation.SKILL_ONE, player))
            .addIngredient('6', new UpgradeGuiItem().getItemProvider(UpgradeTreeLocation.SKILL_TWO, player))
            .addIngredient('7', new UpgradeGuiItem().getItemProvider(UpgradeTreeLocation.ULTIMATE_ONE, player))
            .addIngredient('8', new UpgradeGuiItem().getItemProvider(UpgradeTreeLocation.ULTIMATE_TWO, player))
            .build();

        Window window = Window.single()
                .setViewer(player.getPlayer())
                .setTitle("Upgrade your character!")
                .setGui(gui)
                .build();

        window.open();
    }
}
