package com.runescape.util;

import com.runescape.net.RSStream;

public final class ChatMessageCodec {

    public static String decode(int textLength, RSStream rsStream) {
        int length = 0;
        for (int index = 0; index < textLength; index++) {
            int character = rsStream.getByte();
            TextConstants.DECODE_BUFFER[length] = TextConstants.VALID_CHAT_CHARS[character];
            length++;
        }
        boolean capitalize = true;
        for (int index = 0; index < length; index++) {
            char c = TextConstants.DECODE_BUFFER[index];
            if (capitalize && c >= 'a' && c <= 'z') {
                TextConstants.DECODE_BUFFER[index] += '\uFFE0';
                capitalize = false;
            }
            if (c == '.' || c == '!' || c == '?') {
                capitalize = true;
            }
        }
        return new String(TextConstants.DECODE_BUFFER, 0, length);
    }

    public static void encode(String text, RSStream rsStream) {
        if (text.length() > 80) {
            text = text.substring(0, 80);
        }
        text = text.toLowerCase();
        for (int index = 0; index < text.length(); index++) {
            char c = text.charAt(index);
            int k = 0;
            for (int character = 0; character < TextConstants.VALID_CHAT_CHARS.length; character++) {
                if (c != TextConstants.VALID_CHAT_CHARS[character]) {
                    continue;
                }
                k = character;
                break;
            }
            rsStream.writeByte(k);
        }
    }

    public static String processText(String text) {
        TextConstants.CHAT_STREAM.currentPosition = 0;
        encode(text, TextConstants.CHAT_STREAM);
        int j = TextConstants.CHAT_STREAM.currentPosition;
        TextConstants.CHAT_STREAM.currentPosition = 0;
        return decode(j, TextConstants.CHAT_STREAM);
    }
}
