package IAmNotJustJess.destroyTheMonument.player.classes.upgrades;

import java.util.HashMap;

public class UpgradeTree {
    private HashMap<UpgradeTreeLocation, Upgrade> upgradeTree;
    private HashMap<UpgradeTreeLocation, Integer> boughtLevels;

    public UpgradeTree() {
        this.upgradeTree = new HashMap<UpgradeTreeLocation, Upgrade>();
        this.boughtLevels = new HashMap<UpgradeTreeLocation, Integer>();

        this.boughtLevels.put(UpgradeTreeLocation.BASIC_ONE, 0);
        this.boughtLevels.put(UpgradeTreeLocation.BASIC_TWO, 0);
        this.boughtLevels.put(UpgradeTreeLocation.SPECIAL_ONE, 0);
        this.boughtLevels.put(UpgradeTreeLocation.SPECIAL_TWO, 0);
        this.boughtLevels.put(UpgradeTreeLocation.SKILL_ONE, 0);
        this.boughtLevels.put(UpgradeTreeLocation.SKILL_TWO, 0);
        this.boughtLevels.put(UpgradeTreeLocation.ULTIMATE_ONE, 0);
        this.boughtLevels.put(UpgradeTreeLocation.ULTIMATE_TWO, 0);
    }

    public int getBoughtLevel(UpgradeTreeLocation location) {
        return this.boughtLevels.get(location);
    }

    public void setUpgrade(UpgradeTreeLocation location, Upgrade upgrade) {
        upgradeTree.put(location, upgrade);
    }
}
