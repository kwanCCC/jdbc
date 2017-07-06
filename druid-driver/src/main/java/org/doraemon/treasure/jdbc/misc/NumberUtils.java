package org.doraemon.treasure.jdbc.misc;

public class NumberUtils {


    public static boolean isNumber(String str) {
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
