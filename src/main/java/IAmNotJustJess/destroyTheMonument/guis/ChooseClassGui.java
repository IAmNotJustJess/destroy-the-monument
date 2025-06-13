package IAmNotJustJess.destroyTheMonument.guis;

import IAmNotJustJess.destroyTheMonument.guis.items.NextPageGuiItem;
import IAmNotJustJess.destroyTheMonument.guis.items.PreviousPageGuiItem;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import org.bukkit.Material;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChooseClassGui {

    public static void openGUI(PlayerCharacter player) {

        Item border = new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""));

        List<Item> items = Arrays.stream(Material.values())
            .filter(material -> !material.isAir() && material.isItem())
            .map(material -> new SimpleItem(new ItemBuilder(material)))
            .collect(Collectors.toList());

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
    }
}
