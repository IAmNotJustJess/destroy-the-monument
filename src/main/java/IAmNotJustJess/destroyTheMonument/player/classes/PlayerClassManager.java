package IAmNotJustJess.destroyTheMonument.player.classes;

import IAmNotJustJess.destroyTheMonument.player.classes.upgrades.UpgradeTreeLocation;
import IAmNotJustJess.destroyTheMonument.utility.QuickSendingMethods;
import IAmNotJustJess.destroyTheMonument.utility.UpgradeTreeLocationConverter;

import java.util.ArrayList;

public class PlayerClassManager {
    private static final ArrayList<PlayerClass> list = new ArrayList<>();

    public static void cacheDescriptions() {
        QuickSendingMethods.sendToConsole(
            "send-load-messages",
            "<#dbd814>Caching upgrade descriptions!"
        );
        for(PlayerClass playerClass : list) {
            for(int i = 0; i < 8; i++) {
                UpgradeTreeLocation upgradeTreeLocation = UpgradeTreeLocationConverter.convertIntegerToLocation(i);
                playerClass.upgradeTree.getUpgrade(upgradeTreeLocation).getFirst().cacheDescriptions();
            }
            QuickSendingMethods.sendToConsole(
                "send-load-messages",
                "<#14db4c>Successfully cached <#ffffff>"+ playerClass.name +" <#14db4c>upgrades!"
            );
        }
    }

    public static ArrayList<PlayerClass> getList() {
        return list;
    }
}
