package utils;

public class Utils {
    public static String bytesToString(byte[] bytes) {
        StringBuilder str = new StringBuilder();
        for (byte b : bytes) {
            str.append(b);
        }
        return str.toString();
    }
}
