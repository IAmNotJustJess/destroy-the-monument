package IAmNotJustJess.destroyTheMonument.guis;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.Upgrade;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTree;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageParser;
import IAmNotJustJess.destroyTheMonument.utility.UpgradeTreeLocationConverter;
import org.bukkit.Material;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;

public class UpgradeTreeGui {
    public static void openTestGUI(PlayerCharacter player) {

        UpgradeTree upgradeTree = player.getChosenClass().upgradeTree;

        List<String> upgradeNames, upgradeLore;
        List<SimpleItem> items;

        for(int i = 0; i < 8; i++) {
            Upgrade upgrade = upgradeTree.getUpgrade(UpgradeTreeLocationConverter.convertIntegerToLocation(i)).getFirst();
            String name = upgrade.getName();
            switch(i) {
                case 0 -> {
                    if(upgrade.getCurrentLevel() < upgrade.getMaxLevels())
                    name = MainConfiguration.guiConfiguration.getString("") + name;
                }
            }
            name = MiniMessageParser.deserializeToString(name);
        }

        SimpleItem border = new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""));
        SimpleItem basicOne = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE));

        Gui gui = Gui.normal() // Creates the GuiBuilder for a normal GUI
            .setStructure(
                "# . . . . . . . #",
                "# . B . C . D . #",
                "# . b . c . d . #",
                "# . . . A . . . #",
                "# . . . a . . . #",
                "# . . . . . . . #")
            .addIngredient('#', border)
            .build();

        Window window = Window.single()
                .setViewer(player.getPlayer())
                .setTitle("InvUI")
                .setGui(gui)
                .build();

        window.open();
    }
}
