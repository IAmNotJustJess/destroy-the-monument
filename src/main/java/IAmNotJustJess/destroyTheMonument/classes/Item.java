package IAmNotJustJess.destroyTheMonument.classes;

import IAmNotJustJess.destroyTheMonument.utility.MiniMessageParser;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Item {
    public ItemStack itemStack;

    Item(Material material, int amount, String name, String description) {

        itemStack = new ItemStack(material, amount);
        Objects.requireNonNull(itemStack.getItemMeta()).setUnbreakable(true);

        itemStack.getItemMeta().setItemName(MiniMessageParser.Deserialize(name));
        itemStack.getItemMeta().setLore(MiniMessageParser.DeserializeMultiline(description));
    }
}
