package IAmNotJustJess.destroyTheMonument.player.classes.upgrades;

import java.util.ArrayList;
import java.util.List;

import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageParser;
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
        return MiniMessageParser.deserializeToString(this.name);
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

    public List<String> getDescription() {
        String string = description;
        for(int i = 0; i < descriptionTextReplacementList.get(currentLevel).size(); i++) {
            string = string.replaceAll("<"+i+">", descriptionTextReplacementList.get(currentLevel).get(i));
        }
        return MiniMessageParser.deserializeMultilineToString(string);
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
