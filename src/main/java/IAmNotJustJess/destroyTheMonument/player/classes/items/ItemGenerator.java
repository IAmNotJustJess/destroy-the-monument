package IAmNotJustJess.destroyTheMonument.player.classes.items;

import IAmNotJustJess.destroyTheMonument.utility.MiniMessageSerializers;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemGenerator {

    public static ItemStack generateItem(Material material, int amount, String name, String description) {

        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        assert itemMeta != null;
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setItemName(MiniMessageSerializers.deserializeToString(name));
        itemMeta.setLore(MiniMessageSerializers.deserializeMultilineToString(description));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
