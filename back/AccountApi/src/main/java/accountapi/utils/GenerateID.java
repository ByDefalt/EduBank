package accountapi.utils;

public class GenerateID {

    public static String generateId() {
        return String.valueOf(System.currentTimeMillis());
    }
}
