package IAmNotJustJess.destroyTheMonument.player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerCharacterList {

    private static HashMap<UUID, PlayerCharacter> list = new HashMap<>();

    public static HashMap<UUID, PlayerCharacter> getList() {
        return list;
    }

}
