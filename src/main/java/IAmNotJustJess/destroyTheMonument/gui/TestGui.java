package IAmNotJustJess.destroyTheMonument.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

public class TestGui {
    public static void openTestGUI(Player player) {

        Gui gui = Gui.normal() // Creates the GuiBuilder for a normal GUI
            .setStructure(
                "# # # # # # # # #",
                "# . . . . . . . #",
                "# . . . . . . . #",
                "# # # # # # # # #")
            .addIngredient('#', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)))
            .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle("InvUI")
                .setGui(gui)
                .build();

        window.open();
    }
}
