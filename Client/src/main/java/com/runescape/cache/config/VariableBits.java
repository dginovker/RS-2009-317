package com.runescape.cache.config;

import com.runescape.Constants;
import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;
import com.runescape.util.FileUtility;

public final class VariableBits {

    public static VariableBits[] cache;
    public int setting;
    public int low;
    public int high;

    private VariableBits() {
    }

    public static void unpackConfig(CacheArchive cacheArchive) {
        RSStream stream = new RSStream(FileUtility.getBytes(Constants.getCachePath(true) + "/config/varbit159.dat"));
        int size = stream.getShort();
        if (cache == null) {
            cache = new VariableBits[size];
        }
        for (int index = 0; index < size; index++) {
            if (cache[index] == null) {
                cache[index] = new VariableBits();
            }
            cache[index].readValues(stream);
        }

        if (stream.currentPosition != stream.payload.length) {
            System.out.println("varbit load mismatch");
        }
    }

    private void readValues(RSStream stream) {
        do {
            int opcode = stream.getByte();
            if (opcode == 0) {
                return;
            }
            if (opcode == 1) {
                setting = stream.getShort();
                low = stream.getByte();
                high = stream.getByte();
            }
        } while (true);
    }

    public int getSetting() {
        return setting;
    }

    public int getLow() {
        return low;
    }

    public int getHigh() {
        return high;
    }

}
