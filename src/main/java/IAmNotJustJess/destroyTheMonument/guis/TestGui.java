package IAmNotJustJess.destroyTheMonument.guis;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

public class TestGui {
    public static void openTestGUI(Player player) {

        SimpleItem simpleItem = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(""));
        SimpleItem simpleItem1 = new SimpleItem(new ItemBuilder(Material.DIAMOND).setDisplayName(player.getDisplayName()));

        Gui gui = Gui.normal() // Creates the GuiBuilder for a normal GUI
            .setStructure(
                "# # # # D # # # #",
                "# . . . . . . . #",
                "# . . . . . . . #",
                "# # # # # # # # #")
            .addIngredient('#', simpleItem)
            .addIngredient('D', simpleItem1)
            .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle("InvUI")
                .setGui(gui)
                .build();

        window.open();
    }
}
