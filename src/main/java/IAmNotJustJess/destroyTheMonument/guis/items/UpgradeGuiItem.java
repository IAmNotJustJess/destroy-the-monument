package IAmNotJustJess.destroyTheMonument.guis.items;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterManager;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.Upgrade;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTree;
import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTreeLocation;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageParser;
import IAmNotJustJess.destroyTheMonument.utility.UpgradeTreeLocationConverter;
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

public class UpgradeGuiItem extends AbstractItem {

    private UpgradeTreeLocation upgradeTreeLocation;

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.BARRIER).setDisplayName("Not this method...");
    }

    public ItemProvider getItemProvider(UpgradeTreeLocation upgradeTreeLocation, PlayerCharacter playerCharacter) {
        this.upgradeTreeLocation = upgradeTreeLocation;
        UpgradeTree upgradeTree = playerCharacter.getChosenClass().upgradeTree;
        Upgrade upgrade = upgradeTree.getUpgrade(upgradeTreeLocation).getFirst();
        String name = upgrade.getName();
        List<String> lore = new ArrayList<>();
        Material material = upgrade.getGuiMaterial();
        int amount = upgrade.getCurrentLevel();
        boolean set = false;
        switch(upgradeTreeLocation) {
            case BASIC_TWO -> {
                if(upgradeTree.getUpgrade(UpgradeTreeLocation.BASIC_ONE).getFirst().getCurrentLevel() == 0) {
                    name = MainConfiguration.guiConfiguration.getString("upgrade-unavailable-colour") + name;
                    lore.add(MainConfiguration.guiConfiguration.getString("upgrade-unavailable-text"));
                    material = Material.BARRIER;
                    set = true;
                }
            }
            case SPECIAL_ONE, SKILL_ONE, ULTIMATE_ONE -> {
                if(upgradeTree.getUpgrade(UpgradeTreeLocation.BASIC_TWO).getFirst().getCurrentLevel() == 0) {
                    name = MainConfiguration.guiConfiguration.getString("upgrade-unavailable-colour") + name;
                    lore.add(MainConfiguration.guiConfiguration.getString("upgrade-unavailable-text"));
                    material = Material.BARRIER;
                    set = true;
                }
            }
            case SPECIAL_TWO -> {
                if(upgradeTree.getUpgrade(UpgradeTreeLocation.SPECIAL_ONE).getFirst().getCurrentLevel() == 0) {
                    name = MainConfiguration.guiConfiguration.getString("upgrade-unavailable-colour") + name;
                    lore.add(MainConfiguration.guiConfiguration.getString("upgrade-unavailable-text"));
                    material = Material.BARRIER;
                    set = true;
                }
            }
            case SKILL_TWO -> {
                if(upgradeTree.getUpgrade(UpgradeTreeLocation.SKILL_ONE).getFirst().getCurrentLevel() == 0) {
                    name = MainConfiguration.guiConfiguration.getString("upgrade-unavailable-colour") + name;
                    lore.add(MainConfiguration.guiConfiguration.getString("upgrade-unavailable-text"));
                    material = Material.BARRIER;
                    set = true;
                }
            }
            case ULTIMATE_TWO -> {
                if(upgradeTree.getUpgrade(UpgradeTreeLocation.ULTIMATE_ONE).getFirst().getCurrentLevel() == 0) {
                    name = MainConfiguration.guiConfiguration.getString("upgrade-unavailable-colour") + name;
                    lore.add(MainConfiguration.guiConfiguration.getString("upgrade-unavailable-text"));
                    material = Material.BARRIER;
                    set = true;
                }
            }
        }
        if(!set) {
            if(upgrade.getCurrentLevel() == upgrade.getMaxLevels()) {
                name = MainConfiguration.guiConfiguration.getString("upgrade-maxed-colour") + name;
                lore.add(MainConfiguration.guiConfiguration.getString("upgrade-maxed-text"));
            }
            else if (playerCharacter.getShards() >= upgrade.shardPricesPerLevelList.get(upgrade.getCurrentLevel())) {
                name = MainConfiguration.guiConfiguration.getString("upgrade-available-colour") + name;
                lore.add(MainConfiguration.guiConfiguration.getString("upgrade-available-text"));
            }
            else {
                name = MainConfiguration.guiConfiguration.getString("upgrade-not-enough-shards-colour") + name;
                lore.add(MainConfiguration.guiConfiguration.getString("upgrade-not-enough-shards-text"));
            }
            lore.add(" ");
            lore.addAll(upgrade.cachedGuiDescriptionsForUpgrades.get(upgrade.getCurrentLevel()));
        }
        if(amount < 0) amount = 1;
        name = MiniMessageParser.deserializeToString(name);
        return new ItemBuilder(material, amount).setDisplayName(name).setLegacyLore(lore).addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        PlayerCharacterManager.getList().get(player).buyUpgrade(upgradeTreeLocation);
        notifyWindows(); // this will update the ItemStack that is displayed to the player
    }

}
