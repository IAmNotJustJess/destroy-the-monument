package IAmNotJustJess.destroyTheMonument.classes;

import IAmNotJustJess.destroyTheMonument.utility.MiniMessageParser;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class Item {

    public static ItemStack createItem(Material material, int amount, String name, String description) {

        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setItemName(MiniMessageParser.Deserialize(name));
        itemMeta.setLore(MiniMessageParser.DeserializeMultiline(description));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
