package IAmNotJustJess.destroyTheMonument.guis.items;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageParser;
import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class PreviousPageGuiItem extends PageItem {

    public PreviousPageGuiItem() {
        super(true);
    }

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {
        ItemBuilder builder = new ItemBuilder(gui.hasPreviousPage()
            ? Material.PAPER
            : Material.BARRIER);
        builder.setDisplayName(MiniMessageParser.deserializeToString(MainConfiguration.guiConfiguration.getString("previous-page-item-text")))
            .addLoreLines(gui.hasPreviousPage()
                ? MiniMessageParser.deserializeToString(MainConfiguration.guiConfiguration.getString("previous-page-text") + MainConfiguration.guiConfiguration.getString("page-count-colour") + (gui.getCurrentPage() + 2) + "/" + gui.getPageAmount())
                : MiniMessageParser.deserializeToString(MainConfiguration.guiConfiguration.getString("no-previous-pages-text")));

        return builder;
    }

}
