package com.runescape.net;

import java.nio.ByteBuffer;


/**
 * Holds utility methods for reading/writing a byte buffer.
 *
 * @author Emperor
 */
public final class ByteBufferUtils {

    /**
     * Constructs a new {@code ByteBufferUtils} {@code Object}.
     */
    private ByteBufferUtils() {
        /*
         * empty.
		 */
    }

    /**
     * Gets a string from the byte buffer.
     *
     * @param buffer The byte buffer.
     * @return The string.
     */
    public static String getString(ByteBuffer buffer) {
        StringBuilder sb = new StringBuilder();
        byte b;
        while (buffer.remaining() > 0 && (b = buffer.get()) != 0) {
            sb.append((char) b);
        }
        return sb.toString();
    }

    /**
     * Reads a RuneScape string from a buffer.
     *
     * @param byteBuffer The buffer.
     * @return The string.
     */
    public static String getRS2String(ByteBuffer byteBuffer) {
        byte incomingByte;
        StringBuilder stringBuilder = new StringBuilder();
        while ((incomingByte = byteBuffer.get()) != 10) {
            stringBuilder.append((char) incomingByte);
        }
        return stringBuilder.toString();
    }

    /**
     * Puts a RuneScape string into a buffer.
     *
     * @param string     The string.
     * @param byteBuffer The buffer.
     */
    public static void putRS2String(String string, ByteBuffer byteBuffer) {
        try {
            byteBuffer.put(string.getBytes());
            byteBuffer.put((byte) 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a jag string.
     *
     * @return The string.
     */
    public static String getJagString(ByteBuffer byteBuffer) {
        byteBuffer.get();
        return getString(byteBuffer);
    }

    /**
     * Gets a smart from the buffer.
     *
     * @param buffer The buffer.
     * @return The value.
     */
    public static int getSmart(ByteBuffer buffer) {
        int peek = buffer.get() & 0xFF;
        if (peek <= Byte.MAX_VALUE) {
            return peek;
        }
        return ((peek << 8) | (buffer.get() & 0xFF)) - 32768;
    }

    /**
     * Gets a smart from the buffer.
     *
     * @param buffer The buffer.
     * @return The value.
     */
    public static int getBigSmart(ByteBuffer buffer) {
        int value = 0;
        int current = getSmart(buffer);
        while (current == 32767) {
            current = getSmart(buffer);
            value += 32767;
        }
        value += current;
        return value;
    }
}