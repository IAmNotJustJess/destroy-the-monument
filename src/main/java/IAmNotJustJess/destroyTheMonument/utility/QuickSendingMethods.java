package IAmNotJustJess.destroyTheMonument.utility;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class QuickSendingMethods {

    public static void sendToPlayer(Player player, String string) {
        ((Audience) player).sendMessage(MiniMessageSerializers.deserializeToComponent(string));
    }

    public static void sendToConsole(String comparableConfigString, String message) {
        if(MainConfiguration.mainConfiguration.getBoolean("debug." + comparableConfigString)) {
            ((Audience) Bukkit.getConsoleSender()).sendMessage(MiniMessageSerializers.deserializeToComponent("<#c0c0c0>[DTM] " + message));
        }
    }

}
