package IAmNotJustJess.destroyTheMonument.utility;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

public class ConsoleDebugSending {

    public static void send(String comparableConfigString, Component message) {
        if(MainConfiguration.mainConfiguration.getBoolean("debug." + comparableConfigString)) {
            ((Audience) Bukkit.getConsoleSender()).sendMessage(message);
        }
    }

}
