package IAmNotJustJess.destroyTheMonument.guis.items;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterManager;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClass;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.Upgrade;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTree;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTreeLocation;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageParser;
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
        return new ItemBuilder(playerClass.guiMaterial, 1).setDisplayName(MiniMessageParser.deserializeToString(playerClass.name)).setLegacyLore(MiniMessageParser.deserializeMultilineToString(playerClass.description)).addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        PlayerCharacterManager.getList().get(player).setChosenClass((PlayerClass) playerClass.clone());
        notifyWindows(); // this will update the ItemStack that is displayed to the player
    }

}
