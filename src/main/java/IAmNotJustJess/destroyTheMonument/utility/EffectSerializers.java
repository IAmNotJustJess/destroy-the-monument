package IAmNotJustJess.destroyTheMonument.utility;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class EffectSerializers {

    public static void particleDeserialize(String serializedString, Location location) {

        // 1-2-3-4-5-6-7;1-2-3-4-5-6-7
        // 1: Particle%OptionalData
        // 2: Count
        // 3: Offset X
        // 4: Offset Y
        // 5: Offset Z
        // 6: Extra/Speed/Silly particle animation thing
        // 7: Delay

        // serializedString = "";

        String[] particleList = serializedString.split(";");
        for(String particle : particleList) {

            String[] elements = particle.split("-");
            if(elements.length != 7) {
                continue;
            }

            String optionalData = null;
            if(elements[0].contains("%")) {
                String[] subelements = elements[0].split("%");
                elements[0] = subelements[0];
                optionalData = subelements[1];
            }
            String finalOptionalType = optionalData;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(Objects.isNull(finalOptionalType)) {
                        Objects.requireNonNull(location.getWorld()).spawnParticle(
                            Particle.valueOf(elements[0]),
                            location,
                            Integer.parseInt(elements[1]),
                            Double.parseDouble(elements[2]),
                            Double.parseDouble(elements[3]),
                            Double.parseDouble(elements[4]),
                            Double.parseDouble(elements[5])
                        );
                    }
                    else {
                        Objects.requireNonNull(location.getWorld()).spawnParticle(
                            Particle.valueOf(elements[0]),
                            location,
                            Integer.parseInt(elements[1]),
                            Double.parseDouble(elements[2]),
                            Double.parseDouble(elements[3]),
                            Double.parseDouble(elements[4]),
                            Double.parseDouble(elements[5]),
                            finalOptionalType
                        );
                    }

                }
            }.runTaskLaterAsynchronously(JavaPlugin.getProvidingPlugin(DestroyTheMonument.class), Long.parseLong(elements[6]));
        }

    }

    public static void soundDeserialize(String serializedString, Location location) {

        // 1-2-3-4-5;1-2-3-4-5
        // 1: Spigot Sound - https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html
        // 2: SoundCategory
        // 3: Volume
        // 4: Pitch
        // 5: Delay

        // serializedString = "ENTITY_SHULKER_HURT-PLAYER-1-1-0;ENTITY_VINDICATOR_HURT-PLAYER-1-2-5";

        String[] sounds = serializedString.split(";");

        for (String sound : sounds) {

            String[] elements = sound.split("-");
            if(elements.length != 5) {
                continue;
            }

            new BukkitRunnable() {
                @Override public void run() {

                    Objects.requireNonNull(location.getWorld()).playSound(location, elements[0], SoundCategory.valueOf(elements[1]), Float.parseFloat(elements[2]), Float.parseFloat(elements[3]));
                }
            }.runTaskLaterAsynchronously(JavaPlugin.getProvidingPlugin(DestroyTheMonument.class), Long.parseLong(elements[4]));

        }

    }

}
