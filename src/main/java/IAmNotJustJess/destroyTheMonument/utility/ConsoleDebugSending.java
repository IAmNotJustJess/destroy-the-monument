package IAmNotJustJess.destroyTheMonument.utility;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import org.bukkit.Bukkit;

public class ConsoleDebugSending {

    public static void send(String comparableConfigString, String message) {
        if(MainConfiguration.mainConfiguration.getBoolean("debug." + comparableConfigString)) {
            Bukkit.getConsoleSender().sendMessage(message);
        }
    }

}
