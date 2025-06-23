package IAmNotJustJess.destroyTheMonument.guis.items;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterManager;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClass;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageSerializers;
import org.bukkit.Material;
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

public class PlayerClassGuiItem extends AbstractItem {

    private PlayerClass playerClass;

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.BARRIER).setDisplayName("Not this method...");
    }

    public ItemProvider getItemProvider(PlayerClass playerClass) {
        this.playerClass = playerClass;
        List<String> lore = new ArrayList<>();
        lore.add(MiniMessageSerializers.deserializeToString(MainConfiguration.guiConfiguration.getString("click-to-select")));
        lore.add(MiniMessageSerializers.deserializeToString(MainConfiguration.guiConfiguration.getString("click-to-get-info")));
        lore.addAll(MiniMessageSerializers.deserializeMultilineToString(playerClass.description));
        return new ItemBuilder(playerClass.guiMaterial, 1).setDisplayName(MiniMessageSerializers.deserializeToString(playerClass.name)).setLegacyLore(lore).addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        switch(clickType) {
            case LEFT -> {
                PlayerCharacterManager.getList().get(player).setChosenClass((PlayerClass) playerClass.clone());
            }
            case RIGHT -> {

            }
        }
        notifyWindows(); // this will update the ItemStack that is displayed to the player
    }

}
