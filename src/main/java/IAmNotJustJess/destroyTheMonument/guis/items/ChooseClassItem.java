package IAmNotJustJess.destroyTheMonument.guis.items;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterManager;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClass;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageSerializers;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChooseClassItem extends AbstractItem {

    private PlayerClass playerClass;
    private Player player;

    @Override
    public ItemProvider getItemProvider() {
        String name = playerClass.name;
        List<String> lore = new ArrayList<>();
        Material material = playerClass.guiMaterial;
        name = MiniMessageSerializers.deserializeToString(name);
        boolean set = false;
        if(Objects.equals(PlayerCharacterManager.getList().get(player).getChosenClass().name, playerClass.name)) {
            lore.add(MiniMessageSerializers.deserializeToString(MainConfiguration.guiConfiguration.getString("selected-class")));
            set = true;
        }
        switch(playerClass.playerClassType) {
            case ATTACK -> {
                lore.add(MiniMessageSerializers.deserializeToString(MainConfiguration.guiConfiguration.getString("attack-class")));
            }
            case SUPPORT -> {
                lore.add(MiniMessageSerializers.deserializeToString(MainConfiguration.guiConfiguration.getString("support-class")));
            }
            case DEFENCE -> {
                lore.add(MiniMessageSerializers.deserializeToString(MainConfiguration.guiConfiguration.getString("defence-class")));
            }
        }
        lore.add(" ");
        lore.addAll(MiniMessageSerializers.deserializeMultilineToString(playerClass.description));
        ItemBuilder itemBuilder = new ItemBuilder(material, 1).setDisplayName(name).setLegacyLore(lore).addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if(set) itemBuilder.addEnchantment(Enchantment.UNBREAKING, 10, true);
        return itemBuilder;
    }

    public ItemProvider getItemProvider(PlayerClass playerClass, Player player) {
        this.playerClass = playerClass;
        this.player = player;
        return getItemProvider();
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        PlayerCharacterManager.getList().get(player).changeClass(playerClass);
        notifyWindows(); // this will update the ItemStack that is displayed to the player
    }

}
