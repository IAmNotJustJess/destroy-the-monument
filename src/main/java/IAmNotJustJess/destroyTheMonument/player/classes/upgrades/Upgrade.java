package IAmNotJustJess.destroyTheMonument.player.classes.upgrades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageSerializers;
import org.bukkit.Material;

public class Upgrade {
    private String name;
    private String description;
    private int currentLevel;
    private int maxLevels;
    private UpgradeAffection upgradeAffection;
    private UpgradeType upgradeType;
    private int maxStacks;
    private int stackCount;
    private Material guiMaterial;
    public HashMap<Integer, List<String>> cachedGuiDescriptionsForUpgrades;
    public List<String> cachedGuiDescriptionsForClassInfo;
    public ArrayList<ArrayList<String>> descriptionTextReplacementList;
    public ArrayList<ArrayList<Double>> strengthPerLevelList;
    public ArrayList<Integer> shardPricesPerLevelList;
    public ArrayList<ArrayList<Effect>> effectsPerLevelList;
    public ArrayList<ArrayList<UpgradeSpecialEffectProperty>> effectsSpecialPropertiesPerLevelList;

    public Upgrade(String name, String description, int maxLevels, UpgradeAffection upgradeAffection, UpgradeType upgradeType, int maxStacks, Material guiMaterial) {
        this.name = name;
        this.description = description;
        this.maxLevels = maxLevels;
        this.currentLevel = 0;
        this.upgradeAffection = upgradeAffection;
        this.upgradeType = upgradeType;
        this.maxStacks = maxStacks;
        this.stackCount = 0;
        this.guiMaterial = guiMaterial;
        this.descriptionTextReplacementList = new ArrayList<>();
        this.strengthPerLevelList = new ArrayList<>();
        this.shardPricesPerLevelList = new ArrayList<>();
        this.effectsPerLevelList = new ArrayList<>();
        this.effectsSpecialPropertiesPerLevelList = new ArrayList<>();
        for(int i = 0; i < maxLevels; i++) {
            this.descriptionTextReplacementList.add(new ArrayList<>());
            this.shardPricesPerLevelList.add(0);
            this.strengthPerLevelList.add(new ArrayList<>());
            this.effectsPerLevelList.add(new ArrayList<>());
            this.effectsSpecialPropertiesPerLevelList.add(new ArrayList<>());
        }
    }

    public String getName() {
        return MiniMessageSerializers.deserializeToString(this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxLevels() {
        return maxLevels;
    }

    public void setMaxLevels(int maxLevels) {
        int helper = this.maxLevels;
        this.maxLevels = maxLevels;
        for(int i = helper; i > maxLevels; i--) {
            this.descriptionTextReplacementList.removeLast();
            this.strengthPerLevelList.removeLast();
            this.shardPricesPerLevelList.removeLast();
            this.effectsPerLevelList.removeLast();
            this.effectsSpecialPropertiesPerLevelList.removeLast();
        }
        for(int i = helper; i < maxLevels; i++) {
            this.descriptionTextReplacementList.add(new ArrayList<>());
            this.strengthPerLevelList.add(new ArrayList<>());
            this.shardPricesPerLevelList.add(0);
            this.effectsPerLevelList.add(new ArrayList<>());
            this.effectsSpecialPropertiesPerLevelList.add(new ArrayList<>());
        }
    }

    public void cacheDescriptions() {
        cachedGuiDescriptionsForClassInfo = getGuiDescriptionForClassInfo();
        for(int i = 0; i <= maxLevels; i++) {
            cachedGuiDescriptionsForUpgrades.put(i, getDescriptionForUpgrades());
        }
    }

    private List<String> getDescription() {
        String string = description;
        for(int i = 0; i <= descriptionTextReplacementList.get(currentLevel).size(); i++) {
            string = string.replaceAll("<"+i+">",
                MainConfiguration.guiConfiguration.getString("upgrade-maxed")
                + "<b>"
                + descriptionTextReplacementList.get(currentLevel).get(i))
                + "<reset>";
        }
        return MiniMessageSerializers.deserializeMultilineToString(string);
    }

    private List<String> getDescriptionForUpgrades() {
        if(currentLevel == maxLevels) return getDescription();
        String string = MainConfiguration.guiConfiguration.getString("shards")
            + shardPricesPerLevelList.get(currentLevel)
            + " "
            + MainConfiguration.guiConfiguration.getString("shards-text")
            + "<newline><newline>";
        string += description;
        for(int i = 0; i < descriptionTextReplacementList.get(currentLevel).size(); i++) {
            String replacement = descriptionTextReplacementList.get(currentLevel).get(i);
            if(currentLevel == 0) replacement = "-";
            string = string.replaceAll("<"+i+">",
                MainConfiguration.guiConfiguration.getString("upgrade-unavailable")
                + replacement
                + " <b>-> <reset>"
                + MainConfiguration.guiConfiguration.getString("upgrade-available")
                + descriptionTextReplacementList.get(currentLevel + 1).get(i)
                + "<reset>"
            );
        }
        return MiniMessageSerializers.deserializeMultilineToString(string);
    }

    private List<String> getGuiDescriptionForClassInfo() {
        StringBuilder string = new StringBuilder();
        for(int i = 0; i < shardPricesPerLevelList.size() - 2; i++) {
            string
                .append(MainConfiguration.guiConfiguration.getString("shards"))
                .append(shardPricesPerLevelList.get(i))
                .append(" / ");
        }
        string
            .append(MainConfiguration.guiConfiguration.getString("shards"))
            .append(shardPricesPerLevelList.get(maxLevels - 1))
            .append(MainConfiguration.guiConfiguration.getString("shards-text"))
            .append("<newline><newline>")
            .append(description);
        for(int i = 0; i < descriptionTextReplacementList.get(currentLevel).size(); i++) {
            int j;
            for(j = 0; j < maxLevels - 2; j++) {
                String replacement = descriptionTextReplacementList.get(j).get(i);
                string = new StringBuilder(string.toString().replaceAll("<" + i + ">",
                    MainConfiguration.guiConfiguration.getString("upgrade-not-enough-shards")
                        + replacement
                        + " <b>/ <"
                        + i
                        + "><reset>"
                ));
            }
            j = maxLevels - 1;
            String replacement = descriptionTextReplacementList.get(j).get(i);
            string = new StringBuilder(string.toString().replaceAll("<" + i + ">",
                MainConfiguration.guiConfiguration.getString("upgrade-not-enough-shards")
                    + replacement
                    + "<reset>"
            ));
        }
        return MiniMessageSerializers.deserializeMultilineToString(string.toString());
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStackCount() {
        return stackCount;
    }

    public void setStackCount(int stackCount) {
        this.stackCount = stackCount;
    }

    public int getMaxStacks() {
        return maxStacks;
    }

    public void setMaxStacks(int maxStacks) {
        this.maxStacks = maxStacks;
    }

    public UpgradeAffection getUpgradeAffection() {
        return upgradeAffection;
    }

    public void setUpgradeAffection(UpgradeAffection upgradeAffection) {
        this.upgradeAffection = upgradeAffection;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public UpgradeType getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(UpgradeType upgradeType) {
        this.upgradeType = upgradeType;
    }

    public Material getGuiMaterial() {
        return guiMaterial;
    }

    public void setGuiMaterial(Material guiMaterial) {
        this.guiMaterial = guiMaterial;
    }
}
