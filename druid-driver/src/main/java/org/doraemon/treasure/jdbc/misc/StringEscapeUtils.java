package org.doraemon.treasure.jdbc.misc;


import java.util.HashMap;
import java.util.Map;

public class StringEscapeUtils {

    public static final char ESCAPE_PREFIX = '\\';

    private static final Map<Character, Character> escapeMap;

    static {
        escapeMap = new HashMap<>();
// TODO escape in metricstore rather than here, and at the time only escape \ and ' here
//        escapeMap.put('\0', '0');
//        escapeMap.put('\r', 'r');
//        escapeMap.put('\n', 'n');
        escapeMap.put('\\', '\\');
        escapeMap.put('\'', '\'');
//        escapeMap.put('\b', 'b');
//        escapeMap.put('\f', 'f');
//        escapeMap.put('\t', 't');
    }

    public static String escape(String str) {
        if(str == null) return null;

        StringBuffer buf = new StringBuffer((int) (str.length() * 1.1));

        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);

            Character escaped = escapeMap.get(c);

            if(escaped == null) {
                buf.append(c);
            } else {
                buf.append(ESCAPE_PREFIX);
                buf.append(escaped);
            }
        }

        return buf.toString();
    }

    public static boolean needEscape(String str) {
        if(str == null) return false;

        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);

            if(escapeMap.containsKey(c)) {
                return true;
            }
        }
        return false;
    }
}
