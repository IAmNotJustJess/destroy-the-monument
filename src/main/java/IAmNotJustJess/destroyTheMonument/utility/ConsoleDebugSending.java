package IAmNotJustJess.destroyTheMonument.utility;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;

public class ConsoleDebugSending {

    public static void send(String comparableConfigString, String message) {
        if(MainConfiguration.mainConfiguration.getBoolean("debug." + comparableConfigString)) {
            ((Audience) Bukkit.getConsoleSender()).sendMessage(MiniMessageSerializers.deserializeToComponent(message));
        }
    }

}
