package IAmNotJustJess.destroyTheMonument.utility;

public class NumberConverters {

    public static String convertAmountOfThingsToEnglish(Integer count, String one, String many) {
        count = Math.abs(count);
        if (count == 1) {
            return one;
        }
        return many;
    }

    public static String convertAmountOfThingsToPolish(Integer count, String one, String two, String many) {
        count = Math.abs(count);
        int modulo = count % 10;
        if(count == 1) return one;
        else if(count >= 5 && count <= 21) return many;
        else if(modulo >= 2 && modulo <= 4) return two;
        else return many;
    }
}