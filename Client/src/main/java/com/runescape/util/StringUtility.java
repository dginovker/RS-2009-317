package com.runescape.util;

import com.runescape.media.font.GameFont;
import com.runescape.sign.SignLink;

/**
 * A string utility class used by jagex to encode/decode, and format text
 * to the user.
 *
 * @note Renamed by SeVen/7Winds.
 */
public final class StringUtility {
    private static final char[] BASE_37_CHARACTERS = {'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * Uppercases the first letter of a string.
     *
     * @param s The string.
     * @return The string with an uppercased first letter.
     */
    public static String uppercaseFirst(String s) {
        if (s == null || s.isEmpty() || s.length() < 1) {
            return s;
        }
        return (s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase()).trim();
    }

    public static synchronized String escapeString(String str) {
        String data = null;
        if (str != null && str.length() > 0) {
            str = str.replace("\\", "\\\\");
            str = str.replace("'", "\\'");
            str = str.replace("\0", "\\0");
            str = str.replace("\n", "\\n");
            str = str.replace("\r", "\\r");
            str = str.replace("\"", "\\\"");
            str = str.replace("\\x1a", "\\Z");
            data = str;
        }
        return data;
    }

    public static String cleanString(String string) {
        String colourName;
        for (int i1 = 0; i1 < string.length(); i1++) {
            if (string.charAt(i1) == '@' && i1 + 4 < string.length() && string.charAt(i1 + 4) == '@') {
                colourName = string.substring(i1 + 1, i1 + 4);
                String colour = GameFont.getColorByName(colourName);
                string = string.replaceAll("@" + colourName + "@", "<col=" + colour + ">");
                i1 += 4;
            }
        }
        return string;
    }

    public static int method151(String playerName, String playerName2) {
        int size;
        int charSize = playerName.length();
        int charSize2 = playerName2.length();
        byte[] stringBytes = playerName.getBytes();
        byte[] stringBytes2 = playerName2.getBytes();
        if ((charSize ^ 0xffffffff) >= (charSize2 ^ 0xffffffff)) {
            size = charSize;
        } else {
            size = charSize2;
        }
        for (int index = 0; (index ^ 0xffffffff) > (size ^ 0xffffffff); index++) {
            if ((stringBytes[index] & 0xff) < (stringBytes2[index] & 0xff)) {
                return -1;
            }
            if ((stringBytes2[index] & 0xff) < (stringBytes[index] & 0xff)) {
                return 1;
            }
        }
        if ((charSize2 ^ 0xffffffff) < (charSize ^ 0xffffffff)) {
            return -1;
        }
        if ((charSize ^ 0xffffffff) < (charSize2 ^ 0xffffffff)) {
            return 1;
        }
        return 0;
    }

    public static long encodeBase37(String string) {
        long encoded = 0L;
        for (int index = 0; index < string.length() && index < 12; index++) {
            char c = string.charAt(index);
            encoded *= 37L;
            if (c >= 'A' && c <= 'Z') {
                encoded += (1 + c) - 65;
            } else if (c >= 'a' && c <= 'z') {
                encoded += (1 + c) - 97;
            } else if (c >= '0' && c <= '9') {
                encoded += (27 + c) - 48;
            }
        }

        for (; encoded % 37L == 0L && encoded != 0L; encoded /= 37L) {
            ;
        }
        return encoded;
    }

    public static String decodeBase37(long encoded) {
        try {
            if (encoded <= 0L || encoded >= 0x5b5b57f8a98a5dd1L) {
                return "invalid_name";
            }
            if (encoded % 37L == 0L) {
                return "invalid_name";
            }
            int length = 0;
            char chars[] = new char[12];
            while (encoded != 0L) {
                long l1 = encoded;
                encoded /= 37L;
                chars[11 - length++] = BASE_37_CHARACTERS[(int) (l1 - encoded * 37L)];
            }
            return new String(chars, 12 - length, length);
        } catch (RuntimeException runtimeexception) {
            SignLink.reporterror("81570, " + encoded + ", " + (byte) -99 + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    public static long hashSpriteName(String name) {
        name = name.toUpperCase();
        long hash = 0L;
        for (int index = 0; index < name.length(); index++) {
            hash = (hash * 61L + (long) name.charAt(index)) - 32L;
            hash = hash + (hash >> 56) & 0xffffffffffffffL;
        }
        return hash;
    }

    /**
     * Used to format a users ip address on the welcome screen.
     */
    public static String decodeIp(int ip) {
        return (ip >> 24 & 0xff) + "." + (ip >> 16 & 0xff) + "." + (ip >> 8 & 0xff) + "." + (ip & 0xff);
    }

    /**
     * Used to format a players name.
     */
    public static String formatUsername(String name) {
        if (name.length() > 0) {
            char chars[] = name.toCharArray();
            for (int index = 0; index < chars.length; index++) {
                if (chars[index] == '_') {
                    chars[index] = ' ';
                    if (index + 1 < chars.length && chars[index + 1] >= 'a' && chars[index + 1] <= 'z') {
                        chars[index + 1] = (char) ((chars[index + 1] + 65) - 97);
                    }
                }
            }

            if (chars[0] >= 'a' && chars[0] <= 'z') {
                chars[0] = (char) ((chars[0] + 65) - 97);
            }
            return new String(chars);
        } else {
            return name;
        }
    }

    /**
     * Used for the login screen to hide a users password
     */
    public static String passwordAsterisks(String password) {
        StringBuffer stringbuffer = new StringBuffer();
        for (int index = 0; index < password.length(); index++) {
            stringbuffer.append("*");
        }
        return stringbuffer.toString();
    }
}
