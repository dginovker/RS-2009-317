package com.runescape.util;

import com.runescape.media.font.GameFont;
import com.runescape.net.RSStream;

import java.util.ArrayList;

/**
 * Represents text constants.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class TextConstants {

    /**
     * The normal text effect for chat.
     */
    public static int EFFECT_NORMAL = 0;
    /**
     * The wave text effect for chat - moves x.
     */
    public static int EFFECT_WAVE = 1;
    /**
     * The wave text effect for chat - moves x y.
     */
    public static int EFFECT_WAVE_2 = 2;
    /**
     * The shake text effect for chat.
     */
    public static int EFFECT_SHAKE = 3;
    /**
     * The scroll text effect for chat.
     */
    public static int EFFECT_SCROLL = 4;
    /**
     * The slide text effect for chat.
     */
    public static int EFFECT_SLIDE = 5;

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
     * Wraps text by the character widths.
     *
     * @param string     The string.
     * @param wrapLength The length to wrap.
     * @param gameFont   The game font.
     * @return The wrapped string.
     */
    public static String wrap(String string, int wrapLength, GameFont gameFont) {
        if (string == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        string = string.replaceAll("\\n", "<br>");
        ArrayList<String> strings = new ArrayList<>();
        String[] words = string.split(" ");
        int index = 0;
        int lastWords = -1;
        for (String word : words) {
            stringBuilder.append(word).append(" ");
            if (gameFont.getTextWidth(stringBuilder.toString()) >= wrapLength) {
                stringBuilder.append("<br>");
                strings.add(stringBuilder.toString());
                stringBuilder.setLength(0);
                lastWords = (index + 1);
            }
            index++;
        }
        stringBuilder.setLength(0);
        for (String s : strings) {
            stringBuilder.append(s);
        }
        if (lastWords != -1 && lastWords < words.length) {
            for (index = lastWords; index < words.length; index++) {
                stringBuilder.append(words[index]).append(" ");
            }
        }
        return stringBuilder.toString().replaceAll("<br>", "\\\\n");
    }

    /**
     * The chat {@link com.runescape.net.RSStream}.
     */
    public static final RSStream CHAT_STREAM = new RSStream(new byte[100]);

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

    /**
     * Capitalizes the first letter of the string
     *
     * @param string the string to capitalize first letter of
     * @return the formatted string
     */
    public static String uppercaseFirst(String string) {
        if (string == null || string.length() <= 0) {
            return string;
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
