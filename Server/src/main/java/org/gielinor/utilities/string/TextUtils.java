package org.gielinor.utilities.string;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.game.node.entity.player.Player;

/**
 * The string utils.
 *
 * @author Emperor
 */
public final class TextUtils {

    /**
     * The valid characters to be used in names/messages/...
     */
    public static final char[] VALID_CHARS = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    /**
     * Width of text characters.
     */
    public static final int[] TEXT_WIDTHS = new int[]{
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 7,
        4, 6, 8, 8, 14, 11, 3, 4, 4, 9, 7,
        3, 5, 4, 7, 9, 6, 9, 9, 8, 9, 9,
        7, 9, 9, 4, 4, 6, 6, 6, 7, 14, 9,
        11, 10, 13, 12, 10, 11, 12, 7, 8, 12, 9,
        15, 14, 12, 12, 12, 12, 9, 12, 11, 11, 16,
        12, 11, 10, 4, 7, 4, 5, 8, 4, 7, 7,
        7, 8, 7, 5, 8, 7, 3, 4, 7, 3, 12,
        9, 7, 7, 7, 7, 6, 5, 9, 7, 10, 8,
        9, 7, 5, 3, 5, 8, 3, 10, 3, 3, 3,
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
        3, 3, 3, 3, 3, 3, 3, 4, 7, 10, 8,
        13, 3, 8, 8, 11, 6, 7, 13, 6, 11, 7,
        6, 6, 6, 6, 6, 7, 12, 4, 5, 5, 6,
        7, 10, 10, 10, 7, 9, 9, 9, 9, 9, 9,
        16, 10, 10, 10, 10, 10, 6, 6, 6, 6, 13,
        14, 11, 11, 11, 11, 12, 7, 12, 10, 10, 10,
        11, 10, 8, 7, 7, 7, 7, 7, 7, 7, 10,
        6, 7, 7, 7, 7, 4, 4, 5, 3, 7, 7,
        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
        8, 6, 9 };
    /**
     * The decoding buffer for chat.
     */
    public static final char[] DECODE_BUFFER = new char[100];
    /**
     * Represents valid chat characters.
     */
    public static final char[] VALID_CHAT_CHARS = {
        ' ', 'e', 't', 'a', 'o', 'i', 'h',
        'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p',
        'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')',
        '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%',
        '"', '[', ']', '>', '<', '^', '/', '_', '{', '}', '|'
    };
    private static final String[] lowNames = {
        "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
        "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen" };
    private static final String[] tensNames = {
        "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety" };
    private static final String[] bigNames = {
        "thousand", "million", "billion" };
    /**
     * Character mapping.
     */
    public static char[] mapping = { '\n', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ' };
    /**
     * The an int array241.
     */
    public static int[] anIntArray241 = { 215, 203, 83, 158, 104, 101, 93, 84, 107, 103, 109, 95, 94, 98, 89, 86, 70, 41, 32, 27, 24, 23, -1, -2, 26, -3, -4, 31, 30, -5, -6, -7, 37, 38, 36, -8, -9, -10, 40, -11, -12, 55, 48, 46, 47, -13, -14, -15, 52, 51, -16, -17, 54, -18, -19, 63, 60, 59, -20, -21, 62, -22, -23, 67, 66, -24, -25, 69, -26, -27, 199, 132, 80, 77, 76, -28, -29, 79, -30, -31, 87, 85, -32, -33, -34, -35, -36, 197, -37, 91, -38, 134, -39, -40, -41, 97, -42, -43, 133, 106, -44, 117, -45, -46, 139, -47, -48, 110, -49, -50, 114, 113, -51, -52, 116, -53, -54, 135, 138, 136, 129, 125, 124, -55, -56, 130, 128, -57, -58, -59, 183, -60, -61, -62, -63, -64, 148, -65, -66, 153, 149, 145, 144, -67, -68, 147, -69, -70, -71, 152, 154, -72, -73, -74, 157, 171, -75, -76, 207, 184, 174, 167, 166, 165, -77, -78, -79, 172, 170, -80, -81, -82, 178, -83, 177, 182, -84, -85, 187, 181, -86, -87, -88, -89, 206, 221, -90, 189, -91, 198, 254, 262, 195, 196, -92, -93, -94, -95, -96, 252, 255, 250, -97, 211, 209, -98, -99, 212, -100, 213, -101, -102, -103, 224, -104, 232, 227, 220, 226, -105, -106, 246, 236, -107, 243, -108, -109, 231, 237, 235, -110, -111, 239, 238, -112, -113, -114, -115, -116, 241, -117, 244, -118, -119, 248, -120, 249, -121, -122, -123, 253, -124, -125, -126, -127, 259, 258, -128, -129, 261, -130, -131, 390, 327, 296, 281, 274, 271, 270, -132, -133, 273, -134, -135, 278, 277, -136, -137, 280, -138, -139, 289, 286, 285, -140, -141, 288, -142, -143, 293, 292, -144, -145, 295, -146, -147, 312, 305, 302, 301, -148, -149, 304, -150, -151, 309, 308, -152, -153, 311, -154, -155, 320, 317, 316, -156, -157, 319, -158, -159, 324, 323, -160, -161, 326, -162, -163, 359, 344, 337, 334, 333, -164, -165, 336, -166, -167, 341, 340, -168, -169, 343, -170, -171, 352, 349, 348, -172, -173, 351, -174, -175, 356, 355, -176, -177, 358, -178, -179, 375, 368, 365, 364, -180, -181, 367, -182, -183, 372, 371, -184, -185, 374, -186, -187, 383, 380, 379, -188, -189, 382, -190, -191, 387, 386, -192, -193, 389, -194, -195, 454, 423, 408, 401, 398, 397, -196, -197, 400, -198, -199, 405, 404, -200, -201, 407, -202, -203, 416, 413, 412, -204, -205, 415, -206, -207, 420, 419, -208, -209, 422, -210, -211, 439, 432, 429, 428, -212, -213, 431, -214, -215, 436, 435, -216, -217, 438, -218, -219, 447, 444, 443, -220, -221, 446, -222, -223, 451, 450, -224, -225, 453, -226, -227, 486, 471, 464, 461, 460, -228, -229, 463, -230, -231, 468, 467, -232, -233, 470, -234, -235, 479, 476, 475, -236, -237, 478, -238, -239, 483, 482, -240, -241, 485, -242, -243, 499, 495, 492, 491, -244, -245, 494, -246, -247, 497, -248, 502, -249, 506, 503, -250, -251, 505, -252, -253, 508, -254, 510, -255, -256, 0 };

    /**
     * Constructs a new {@code StringUtils.java} {@code Object}.
     */
    public TextUtils() {
        /**
         * empty.
         */
    }

    /**
     * Converts an integer number into words (american english).
     *
     * @author Christian d'Heureuse, Inventec Informatik AG, Switzerland, www.source-code.biz
     */
    public static String numberToString(int n) {
        if (n < 0) {
            return "minus " + numberToString(-n);
        }
        if (n <= 999) {
            return convert999(n);
        }
        String s = null;
        int t = 0;
        while (n > 0) {
            if (n % 1000 != 0) {
                String s2 = convert999(n % 1000);
                if (t > 0) {
                    s2 = s2 + " " + bigNames[t - 1];
                }
                if (s == null) {
                    s = s2;
                } else {
                    s = s2 + ", " + s;
                }
            }
            n /= 1000;
            t++;
        }
        return s;
    }

    // Range 0 to 999.
    private static String convert999(int n) {
        String s1 = lowNames[n / 100] + " hundred";
        String s2 = convert99(n % 100);
        if (n <= 99) {
            return s2;
        } else if (n % 100 == 0) {
            return s1;
        } else {
            return s1 + " " + s2;
        }
    }

    // Range 0 to 99.
    private static String convert99(int n) {
        if (n < 20) {
            return lowNames[n];
        }
        String s = tensNames[n / 10 - 2];
        if (n % 10 == 0) {
            return s;
        }
        return s + "-" + lowNames[n % 10];
    }

    public static String sql(Object var) {
        return "\"" + (var instanceof String ? escapeString(String.valueOf(var)) : var) + "\", ";
    }

    public static String getStackColor(int count) {
        String textColor = "FFFF00";
        if (count >= 100000) {
            textColor = "FFFFFF";
        }
        if (count >= 10000000) {
            textColor = "00FF80";
        }
        return textColor;
    }

    public static String intToKOrMil(int j) {
        if (j < 0x186a0) {
            return String.valueOf(j);
        }
        if (j < 0x989680) {
            return j / 1000 + "K";
        } else {
            return j / 0xf4240 + "M";
        }
    }

    /**
     * Gets the width of the given text.
     *
     * @param text The text.
     * @return The width.
     */
    public static int getTextWidth(String text) {
        if (text == null) {
            return 0;
        }
        int j = 0;
        for (int k = 0; k < text.length(); k++) {
            if (text.charAt(k) == '<' && k + 11 < text.length() && text.charAt(k + 11) == '>') {
                k += 11;
            } else if (text.charAt(k) == '<' && k + 5 < text.length() && text.charAt(k + 5) == '>') {
                k += 5;
            } else if (text.charAt(k) == '@' && k + 4 < text.length() && text.charAt(k + 4) == '@') {
                k += 4;
            } else {
                j += TEXT_WIDTHS[text.charAt(k)];
            }
        }
        return j;
    }

    /**
     * Explodes a String to an integer array.
     *
     * @param string     The string array.
     * @param identifier The identifier.
     * @return The new integer array.
     */
    public static int[] explode(String string, String identifier) {
        if (string == null || string.equalsIgnoreCase("null") || string.length() < 1) {
            return null;
        }
        List<Integer> integerList = new ArrayList<>();
        String[] string1 = string.split(identifier);
        for (String s : string1) {
            if (s == null) {
                continue;
            }
            integerList.add(Integer.valueOf(s));
        }
        int[] intArray = new int[integerList.size()];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = integerList.get(i);
        }
        return intArray;
    }

    /**
     * Converts a String array to an integer array.
     *
     * @param string The string array.
     * @return The new integer array.
     */
    public static int[] toIntArray(String[] string) {
        List<Integer> integerList = new ArrayList<>();
        for (String s : string) {
            if (s == null || !StringUtils.isNumeric(s)) {
                continue;
            }
            integerList.add(Integer.valueOf(s));
        }
        int[] intArray = new int[integerList.size()];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = integerList.get(i);
        }
        return intArray;
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
     * Converts a String array to a short array.
     *
     * @param string The string array.
     * @return The new integer array.
     */
    public static short[] toShortArray(String[] string) {
        List<Short> shortList = new ArrayList<>();
        for (String s : string) {
            if (s == null || !StringUtils.isNumeric(s)) {
                continue;
            }
            shortList.add(Short.valueOf(s));
        }
        short[] shortArray = new short[shortList.size()];
        for (int i = 0; i < shortArray.length; i++) {
            shortArray[i] = shortList.get(i);
        }
        return shortArray;
    }

    public static String implode(int[] data, char identifier) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i : data) {
            stringBuilder.append(i).append(identifier);
        }
        return format(stringBuilder.toString(), identifier);
    }

    public static String implode(Integer[] data, char identifier) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer i : data) {
            stringBuilder.append(i).append(identifier);
        }
        return format(stringBuilder.toString(), identifier);
    }

    public static String implode(String[] string, char identifier) {
        return implode(string, identifier, true);
    }

    public static String implode(String[] string, char identifier, boolean lowercase) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : string) {
            if (s == null) {
                s = "null";
            }
            stringBuilder.append(lowercase ? s.toLowerCase() : s).append(identifier);
        }
        return format(stringBuilder.toString(), identifier);
    }

    /**
     * Used to remove the last colon from an array string.
     *
     * @param string The string.
     * @return The formatted string.
     */
    public static String format(String string, char identifier) {
        return (string.length() > 0 && string.charAt(string.length() - 1) == identifier) ? string.substring(0, string.length() - 1) : string;
    }

    public static String createSQLParameters(Object... params) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < params.length; index++) {
            Object param = params[index];
            stringBuilder.append("\"").append(param instanceof String ?
                TextUtils.escapeString(String.valueOf(param)) :
                param).append("\"").append((index == params.length - 1) ? "" : ", ");
        }
        return stringBuilder.toString();
    }

    /**
     * Gets the combat difference colour.
     *
     * @param combatLevel      The first combat level.
     * @param otherCombatLevel The second combat level to compare.
     * @return The colour.
     */
    public static String getCombatDifferenceColour(int combatLevel, int otherCombatLevel) {
        int levelDifference = combatLevel - otherCombatLevel;
        if (levelDifference < -9) {
            return "<col=FF0000>";
        }
        if (levelDifference < -6) {
            return "<col=FF3000>";
        }
        if (levelDifference < -3) {
            return "<col=FF7000>";
        }
        if (levelDifference < 0) {
            return "<col=FFb000>";
        }
        if (levelDifference > 9) {
            return "<col=65280>";
        }
        if (levelDifference > 6) {
            return "<col=40FF00>";
        }
        if (levelDifference > 3) {
            return "<col=80FF00>";
        }
        if (levelDifference > 0) {
            return "<col=c0FF00>";
        }
        return "<col=FFFF00>";
    }

    /**
     * Capitalizes the first letter of the string
     *
     * @param string the string to capitalize first letter of
     * @return the formatted string
     */
    public static String uppercaseFirst(String string) {
        return uppercaseFirst(string, false);
    }

    /**
     * Capitalizes the first letter of the string
     *
     * @param string the string to capitalize first letter of
     * @return the formatted string
     */
    public static String uppercaseFirst(String string, boolean keepCase) {
        if (string == null || string.length() <= 0) {
            return string;
        }
        return string.substring(0, 1).toUpperCase() + (keepCase ? string.substring(1) : string.substring(1).toLowerCase());
    }

    /**
     * Method used to get the formatted number as a string from the integer inputed.
     *
     * @param amount the ammount.
     * @return the string value.
     */
    public static String getFormattedNumber(int amount) {
        return new DecimalFormat("#,###,##0").format(amount);
    }

    /**
     * Formats a name for use in the protocol.
     *
     * @param s The name.
     * @return The formatted name.
     */
    public static String formatNameForProtocol(String s) {
        return s.toLowerCase().replace(" ", "_");
    }

    /**
     * Checks if a name is valid.
     *
     * @param s The name.
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public static boolean isValidName(String s) {
        return formatNameForProtocol(s).matches("[a-z0-9_]+") && s.length() <= 12 && s.length() > 0;
    }

    /**
     * If a word starts with a e i o u h for grammar = a + n.
     *
     * @param word The word.
     * @return If the a should have +n <code>True</code>.
     */
    public static boolean isPlusN(String word) {
        if (word == null) {
            return false;
        }
        String s = word.toLowerCase();
        return s.charAt(0) == 'a' || s.charAt(0) == 'e' || s.charAt(0) == 'i' || s.charAt(0) == 'o' || s.charAt(0) == 'u' || (s.charAt(0) == 'h' && s.length() > 1 && s.charAt(1) != 'e');
    }

    /**
     * Method used to get the player name as a long.
     *
     * @param s the string.
     * @return the long.
     */
    public static long getPlayerNameLong(String s) {
        long l = 0L;
        for (int i = 0; i < s.length() && i < 12; i++) {
            char c = s.charAt(i);
            l *= 37L;
            if (c >= 'A' && c <= 'Z') {
                l += (1 + c) - 65;
            } else if (c >= 'a' && c <= 'z') {
                l += (1 + c) - 97;
            } else if (c >= '0' && c <= '9') {
                l += (27 + c) - 48;
            }
        }
        while (l % 37L == 0L && l != 0L) {
            l /= 37L;
        }
        return l;
    }

    /**
     * Method used to convert the string to a long.
     *
     * @param s the string.
     * @return the long.
     */
    public static long convertStringToLong(String s) {
        if (s.length() > 20) {
            throw new IllegalArgumentException("String is too long: " + s);
        }
        long out = 0L;
        for (int i = 0; i < s.length(); ++i) {
            long m = reducedMapping(s.codePointAt(i));
            if (m == -1) {
                throw new IllegalArgumentException("Unmapped Character in String: " + s);
            }
            m <<= ((9 - i) * 6) + 4;
            out |= m;
        }
        return out;
    }

    /**
     * Formats the string as display name.
     *
     * @param name The string to format.
     * @return The formatted name.
     */
    public static String formatDisplayName(String name) {
        char[] chars = name.toLowerCase().toCharArray();
        StringBuilder builder = new StringBuilder(chars.length);
        boolean capitalize = true;
        for (char c : chars) {
            // Deal with names that contain '_' so that we don't
            // need a String.replace call when most callers probably
            // already have names that are stripped for '_' already.
            if (c == '_')
                c = ' ';
            if (capitalize) {
                if (c >= 'a' && c <= 'z')
                    c = Character.toUpperCase(c);
                if (c != ' ')
                    capitalize = false;
            } else if (c == ' ')
                capitalize = true;
            builder.append(c);
        }
        return builder.toString();
    }

    /**
     * Gets the byte for the character.
     *
     * @param c The character.
     * @return The byte.
     */
    private static byte getByte(char c) {
        byte charByte;
        if (c > 0 && c < '\200' || c >= '\240' && c <= '\377') {
            charByte = (byte) c;
        } else if (c != '\u20AC') {
            if (c != '\u201A') {
                if (c != '\u0192') {
                    if (c == '\u201E') {
                        charByte = -124;
                    } else if (c != '\u2026') {
                        if (c != '\u2020') {
                            if (c == '\u2021') {
                                charByte = -121;
                            } else if (c == '\u02C6') {
                                charByte = -120;
                            } else if (c == '\u2030') {
                                charByte = -119;
                            } else if (c == '\u0160') {
                                charByte = -118;
                            } else if (c == '\u2039') {
                                charByte = -117;
                            } else if (c == '\u0152') {
                                charByte = -116;
                            } else if (c != '\u017D') {
                                if (c == '\u2018') {
                                    charByte = -111;
                                } else if (c != '\u2019') {
                                    if (c != '\u201C') {
                                        if (c == '\u201D') {
                                            charByte = -108;
                                        } else if (c != '\u2022') {
                                            if (c == '\u2013') {
                                                charByte = -106;
                                            } else if (c == '\u2014') {
                                                charByte = -105;
                                            } else if (c == '\u02DC') {
                                                charByte = -104;
                                            } else if (c == '\u2122') {
                                                charByte = -103;
                                            } else if (c != '\u0161') {
                                                if (c == '\u203A') {
                                                    charByte = -101;
                                                } else if (c != '\u0153') {
                                                    if (c == '\u017E') {
                                                        charByte = -98;
                                                    } else if (c != '\u0178') {
                                                        charByte = 63;
                                                    } else {
                                                        charByte = -97;
                                                    }
                                                } else {
                                                    charByte = -100;
                                                }
                                            } else {
                                                charByte = -102;
                                            }
                                        } else {
                                            charByte = -107;
                                        }
                                    } else {
                                        charByte = -109;
                                    }
                                } else {
                                    charByte = -110;
                                }
                            } else {
                                charByte = -114;
                            }
                        } else {
                            charByte = -122;
                        }
                    } else {
                        charByte = -123;
                    }
                } else {
                    charByte = -125;
                }
            } else {
                charByte = -126;
            }
        } else {
            charByte = -128;
        }
        return charByte;
    }

    /**
     * Gets the double value of this string
     *
     * @param s The string.
     * @return The double value.
     */
    public static double getDouble(String s) {
        s = s.replaceAll(", ", "").replaceAll(",", "");
        StringBuilder sb = new StringBuilder();
        char c;
        boolean foundStart = false;
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            if (Character.isDigit(c) || c == '-' || c == '.') {
                sb.append(c);
                foundStart = true;
            } else if (foundStart) {
                break;
            }
        }
        try {
            double amount = Double.parseDouble(sb.toString());
            return amount;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Gets the hash for the string.
     *
     * @param str The string.
     * @return The hash.
     */
    public static int getNameHash(String str) {
        str = str.toLowerCase();
        int hash = 0;
        for (int index = 0; index < str.length(); index++) {
            hash = getByte(str.charAt(index)) + ((hash << 5) - hash);
        }
        return hash;
    }

    /**
     * Gets the string value of this string (all html/... removed).
     *
     * @param s The string.
     * @return The string value.
     */
    public static String getString(String s) {
        String string = s.replaceAll("\\<.*?>", "").replaceAll("&#160;", "").replaceAll("Discontinued Item:", "");
        return string;
    }

    /**
     * Gets an integer value from a string.
     *
     * @param s the string.
     * @return The value;
     */
    public static int getValue(String s) {
        s = s.replaceAll(", ", "").replaceAll(",", "");
        StringBuilder sb = new StringBuilder();
        char c;
        boolean foundStart = false;
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            if (Character.isDigit(c) || c == '-') {
                sb.append(c);
                foundStart = true;
            } else if (foundStart) {
                break;
            }
        }
        try {
            int amount = Integer.parseInt(sb.toString());
            return amount;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Translate an encoded username to the formatted username.
     *
     * @param encoded The encoded username.
     * @return The formatted decoded username.
     */
    public static String longToString(long encoded) {
        char[] chars = new char[32];
        int len = 0;
        while (encoded != 0L) {
            long i = encoded;
            encoded /= 37L;
            chars[11 - len++] = VALID_CHARS[(int) (i - encoded * 37L)];
        }
        return formatDisplayName(new String(chars, 12 - len, len));
    }

    /**
     * Method used to...
     *
     * @param x idk,
     * @return the idk.
     */
    public static long reducedMapping(int x) {
        long out = -1;
        if (x >= 97 && x <= 122) {
            out = x - 96;
        } else if (x >= 65 && x <= 90) {
            out = x - 37;
        } else if (x >= 48 && x <= 57) {
            out = x - +5;
        } else if (x == 32) {
            out = 63L;
        }
        return out;
    }

    /**
     * Converts a string to a long.
     *
     * @param s The string.
     * @return The long.
     */
    public static long stringToLong(String s) {
        long l = 0L;
        for (int i = 0; i < s.length() && i < 12; i++) {
            char c = s.charAt(i);
            l *= 37L;
            if (c >= 'A' && c <= 'Z') {
                l += (1 + c) - 65;
            } else if (c >= 'a' && c <= 'z') {
                l += (1 + c) - 97;
            } else if (c >= '0' && c <= '9') {
                l += (27 + c) - 48;
            }
        }
        while (l % 37L == 0L && l != 0L) {
            l /= 37L;
        }
        return l;
    }

    /**
     * Filters invalid characters out of a string.
     *
     * @param s The string.
     * @return The filtered string.
     */
    public static String filterText(String s) {
        if (true) {
            return s;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : s.toLowerCase().toCharArray()) {
            boolean valid = false;
            for (char validChar : VALID_CHAT_CHARS) {
                if (validChar == c) {
                    valid = true;
                }
            }
            if (valid) {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }

    public static String decode(byte[] packedData, int textLength) {
        int length = 0;
        for (int index = 0; index < textLength; index++) {
            int character = packedData[index];
            DECODE_BUFFER[length] = VALID_CHAT_CHARS[character];
            length++;
        }
        boolean capitalize = true;
        for (int index = 0; index < length; index++) {
            char c = DECODE_BUFFER[index];
            if (capitalize && c >= 'a' && c <= 'z') {
                DECODE_BUFFER[index] += '\uFFE0';
                capitalize = false;
            }
            if (c == '.' || c == '!' || c == '?') {
                capitalize = true;
            }
        }
        return new String(DECODE_BUFFER, 0, length);
    }

    public static void encode(byte[] packedData, String text) {
        if (text.length() > 80) {
            text = text.substring(0, 80);
        }
        text = text.toLowerCase();
        int offset = 0;
        for (int index = 0; index < text.length(); index++) {
            char c = text.charAt(index);
            int k = 0;
            for (int character = 0; character < VALID_CHAT_CHARS.length; character++) {
                if (c != VALID_CHAT_CHARS[character]) {
                    continue;
                }
                k = character;
                break;
            }
            packedData[offset++] = (byte) k; // TODO index++
        }
    }

    /**
     * Formats a long to the {@code Jagex} date format ceiled.
     *
     * @param unixTime The time.
     * @return The {@code Jagex} date format.
     */
    public static int toJagexDateFormatCeil(long unixTime) {
        double i = (unixTime / 0x5265c00L) + 1;
        return (int) Math.floor(i);
    }

    /**
     * Formats a long to the {@code Jagex} date format floored.
     *
     * @param unixTime The time.
     * @return The {@code Jagex}date format.
     */
    public static int toJagexDateFormatFloor(long unixTime) {
        double i = (unixTime / 0x5265c00L);
        return (int) Math.floor(i);
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

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, " thousand");
        suffixes.put(1_000_000L, " million");
        suffixes.put(1_000_000_000L, " billion");
        suffixes.put(1_000_000_000_000L, " trillion");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static long getPlayTime(Player player) {
        return (System.currentTimeMillis() - player.getSavedData().getGlobalData().getStartTime()) +
            player.getSavedData().getGlobalData().getPlayTime();
    }

    public static long totalPlaytime(Player player) {
        return (getPlayTime(player) / 1000);
    }

    public static String getSmallPlaytime(Player player) {
        long DAY = (totalPlaytime(player) / 86400);
        long HR = (totalPlaytime(player) / 3600) - (DAY * 24);
        long MIN = (totalPlaytime(player) / 60) - (DAY * 1440) - (HR * 60);
        long SEC = totalPlaytime(player) - (DAY * 86400) - (HR * 3600) - (MIN * 60);
        return ("Hours: " + HR + " Minutes: " + MIN + " Seconds: " + SEC);
    }

    public static String getLongToFutureTime(long time) {
        long day = TimeUnit.MILLISECONDS.toDays(time);
        long hour = TimeUnit.MILLISECONDS.toHours(time) - (day * 24);
        long minute = TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.DAYS.toMinutes(day) - TimeUnit.HOURS.toMinutes(hour);
        long second = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.DAYS.toSeconds(day) - TimeUnit.HOURS.toSeconds(hour) - TimeUnit.MINUTES.toSeconds(minute);

        StringBuilder sb = new StringBuilder();

        if (day > 0) {
            sb.append(day + " " + isMoreThanOne(day, "day") + " ");
        }
        if (hour > 0) {
            sb.append(hour + " " + isMoreThanOne(hour, "hour") + " ");
        }
        if (minute > 0) {
            sb.append(minute + " " + isMoreThanOne(minute, "minute") + " ");
        }
        if (second > 0) {
            sb.append(second + " " + isMoreThanOne(second, "second") + " ");
        }

        return sb.toString().trim();
    }

    public static String isMoreThanOne(long amount, String input) {
        return input + (amount > 1 ? "s" : "");
    }

    /**
     * Converts the numerical value to a {@link String} abbreviated if possible.
     *
     * @param quantity the value expressed in coins.
     *
     * @return the a {@link String} representation.
     */
    public static String formatCoinValue(final long quantity) {
        DecimalFormat format = new DecimalFormat("#,###.#");
        if (quantity >= 1e12) return format.format(quantity / 1000000000000d) + "T";
        else if (quantity >= 1e9) return format.format(quantity / 1000000000d) + "B";
        else if (quantity >= 1e6) return format.format(quantity / 1000000d) + "M";
        else if (quantity >= 1e4) return format.format(quantity / 1000d) + "K";
        else return "" + quantity + " gp";
    }
}
