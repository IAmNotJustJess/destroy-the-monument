package IAmNotJustJess.destroyTheMonument.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.*;

public class ObjectConverter {
    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = asList((Object[]) obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>) obj);
        }
        return list;
    }
}
