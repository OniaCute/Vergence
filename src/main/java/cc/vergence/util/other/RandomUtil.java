package cc.vergence.util.other;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {
    private static final String SYMBOL = "!<*.,/?>;:#%()[]_-+=|`~^";
    private static final String FULL = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
    private static final String LOWER = "qwertyuiopasdfghjklzxcvbnm";
    private static final String UPPER = "QWERTYUIOPASDFGHJKLZXCVBNM";
    private static final String NUMBER = "1234567890";

    public static int getInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static double getDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static float getFloat(float min, float max) {
        return (float) getDouble(min, max);
    }

    public static char getChar(String chars) {
        return chars.charAt(getInt(0, chars.length() - 1));
    }

    public static String getString(String chars, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(getChar(chars));
        }
        return sb.toString();
    }
}
