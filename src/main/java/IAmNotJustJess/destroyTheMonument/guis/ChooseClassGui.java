package IAmNotJustJess.destroyTheMonument.guis;

import IAmNotJustJess.destroyTheMonument.guis.items.ChooseClassItem;
import IAmNotJustJess.destroyTheMonument.guis.items.NextPageGuiItem;
import IAmNotJustJess.destroyTheMonument.guis.items.PreviousPageGuiItem;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClass;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClassManager;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClassType;
import org.bukkit.Material;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.List;

public class ChooseClassGui {

    public static void openGUI(PlayerCharacter player, PlayerClassType playerClassType) {

        Item border = new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""));

        List<Item> items = new ArrayList<>();

        switch(playerClassType) {
            case ATTACK, DEFENCE, SUPPORT -> {
                for(PlayerClass playerClass : PlayerClassManager.getList()) {
                    if(playerClass.playerClassType == playerClassType) {
                        items.add((Item) new ChooseClassItem().getItemProvider(playerClass, player.getPlayer()));
                    }
                }
            }
            case null, default -> {
                for(PlayerClass playerClass : PlayerClassManager.getList()) {
                    items.add((Item) new ChooseClassItem().getItemProvider(playerClass, player.getPlayer()));
                }
            }
        }


        Gui gui = PagedGui.items()
            .setStructure(
                "p . . a b c . . n",
                "# # # # # # # # #",
                ". . . . . . . . .",
                ". x x x x x x x .",
                ". x x x x x x x .",
                ". . . . . . . . ."
                )

            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL) // where paged items should be put
            .addIngredient('#', border)
            .addIngredient('p', new PreviousPageGuiItem())
            .addIngredient('n', new NextPageGuiItem())
            .setContent(items)
            .build();

        Window window = Window.single()
            .setViewer(player.getPlayer())
            .setTitle("Select your class!")
            .setGui(gui)
            .build();

        window.open();
    }
}
