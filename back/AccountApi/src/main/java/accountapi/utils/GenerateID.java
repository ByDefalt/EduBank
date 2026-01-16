package accountapi.utils;

public class GenerateID {

    public static long generateId() {
        return System.currentTimeMillis() / 10;
    }
}
