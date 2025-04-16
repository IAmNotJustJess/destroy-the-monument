package IAmNotJustJess.destroyTheMonument.player.classes.upgrades;

import java.util.ArrayList;
import java.util.List;

import IAmNotJustJess.destroyTheMonument.player.classes.effects.Effect;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageParser;

public class Upgrade {
    private String name;
    private String description;
    private int maxLevels;
    private UpgradeAffection upgradeAffection;
    private int maxStacks;
    private int stackCount;
    public ArrayList<ArrayList<String>> descriptionTextReplacementList;
    public ArrayList<ArrayList<Double>> strengthLevels;
    public ArrayList<Integer> shardPricesLevels;
    public ArrayList<ArrayList<Effect>> effectsLevels;

    public String getName() {
        return MiniMessageParser.Deserialize(this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxLevels() {
        return maxLevels;
    }

    public void setMaxLevels(int maxLevels) {
        this.maxLevels = maxLevels;
    }

    public List<String> getDescription() {
        String string = description;
        for(int i = 0; i < descriptionTextReplacementList.size(); i++) {
            for(int j = 0; j < descriptionTextReplacementList.get(i).size(); j++) {
                string.replaceAll("<"+i+">", descriptionTextReplacementList.get(i).get(j));
            }
        }
        return MiniMessageParser.DeserializeMultiline(string);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Upgrade(String name, String description, int maxLevels, UpgradeAffection upgradeAffection, int maxStacks) {
        this.name = name;
        this.description = name;
        this.maxLevels = maxLevels;
        this.upgradeAffection = upgradeAffection;
        this.maxStacks = maxStacks;
        this.stackCount = 0;
        this.descriptionTextReplacementList = new ArrayList<ArrayList<String>>();
        this.strengthLevels = new ArrayList<ArrayList<Double>>();
        this.shardPricesLevels = new ArrayList<Integer>();
        this.effectsLevels = new ArrayList<ArrayList<Effect>>();
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
}
