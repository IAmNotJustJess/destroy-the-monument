package IAmNotJustJess.destroyTheMonument.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.List;

public class MiniMessageSerializers {

    public static String deserializeToString(String string) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(MiniMessage.miniMessage().deserialize(string));
    }

    public static List<String> deserializeMultilineToString(String string) {
        String[] splitString = string.split("<newline>");
        List<String> stringList = new ArrayList<>();
        for (String stringFromSplit : splitString) {
            stringList.add(deserializeToString(stringFromSplit));
        }
        return stringList;
    }

    public static Component deserializeToComponent(String string) {
        return MiniMessage.miniMessage().deserialize(string);
    }
}
