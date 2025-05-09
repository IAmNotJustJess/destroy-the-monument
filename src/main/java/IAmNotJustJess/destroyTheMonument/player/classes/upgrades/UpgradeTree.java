package IAmNotJustJess.destroyTheMonument.player.classes.upgrades;

import java.util.ArrayList;
import java.util.HashMap;

public class UpgradeTree {
    private HashMap<UpgradeTreeLocation, ArrayList<Upgrade>> upgradeTree;

    public UpgradeTree() {
        this.upgradeTree = new HashMap<>();
    }

    public ArrayList<Upgrade> getUpgrade(UpgradeTreeLocation location) {
        return this.upgradeTree.get(location);
    }

    public void setUpgrade(UpgradeTreeLocation location, ArrayList<Upgrade> upgrade) {
        upgradeTree.put(location, upgrade);
    }
}
