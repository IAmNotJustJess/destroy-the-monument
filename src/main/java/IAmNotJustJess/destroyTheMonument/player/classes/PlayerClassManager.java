package IAmNotJustJess.destroyTheMonument.player.classes;

import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTreeLocation;
import IAmNotJustJess.destroyTheMonument.utility.ConsoleDebugSending;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageSerializers;
import IAmNotJustJess.destroyTheMonument.utility.UpgradeTreeLocationConverter;

import java.util.ArrayList;

public class PlayerClassManager {
    private static final ArrayList<PlayerClass> list = new ArrayList<>();

    public static void cacheDescriptions() {
        ConsoleDebugSending.send("send-load-messages", MiniMessageSerializers.deserializeToComponent("<#dbd814>Caching upgrade descriptions!"));
        for(PlayerClass playerClass : list) {
            for(int i = 0; i < 8; i++) {
                UpgradeTreeLocation upgradeTreeLocation = UpgradeTreeLocationConverter.convertIntegerToLocation(i);
                playerClass.upgradeTree.getUpgrade(upgradeTreeLocation).getFirst().cacheDescriptions();
            }
            ConsoleDebugSending.send("send-load-messages", MiniMessageSerializers.deserializeToComponent("<#14db4c>Successfully cached "+ playerClass.name +" upgrades!"));
        }
    }

    public static ArrayList<PlayerClass> getList() {
        return list;
    }
}
