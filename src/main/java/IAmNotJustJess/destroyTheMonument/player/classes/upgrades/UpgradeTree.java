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

    public UpgradeTree clone() {

        UpgradeTree upgradeTree1;

        try {
            upgradeTree1 = (UpgradeTree) super.clone();
        } catch (CloneNotSupportedException exception) {
            upgradeTree1 = new UpgradeTree();
        }

        for(UpgradeTreeLocation upgradeTreeLocation : upgradeTree.keySet()) {
            upgradeTree1.setUpgrade(
                upgradeTreeLocation,
                new ArrayList<>() {{
                    for(Upgrade upgrade : upgradeTree.get(upgradeTreeLocation)) {
                        add(upgrade.clone());
                    }
                }}
            );
        }

        return upgradeTree1;
    }
}
