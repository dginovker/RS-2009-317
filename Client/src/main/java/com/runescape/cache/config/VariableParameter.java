package com.runescape.cache.config;

import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;

public final class VariableParameter {

    public static VariableParameter[] parameters;
    private static int anInt702;
    private static int[] anIntArray703;
    public int anInt709;
    public boolean aBoolean713;

    private VariableParameter() {
        aBoolean713 = false;
    }

    public static void unpackConfig(CacheArchive cacheArchive) {
        RSStream stream = new RSStream(cacheArchive.getEntry("varp.dat"));
        anInt702 = 0;
        int cacheSize = stream.getShort();
        if (parameters == null) {
            parameters = new VariableParameter[cacheSize + 500];
        }
        if (anIntArray703 == null) {
            anIntArray703 = new int[cacheSize];
        }
        for (int index = 0; index < cacheSize; index++) {
            if (parameters[index] == null) {
                parameters[index] = new VariableParameter();
            }
            parameters[index].readValues(stream, index);
        }
        if (stream.currentPosition != stream.payload.length) {
            System.out.println("varptype load mismatch");
        }
        for (int index = 0; index < parameters.length; index++) {
            if (parameters[index] == null) {
                parameters[index] = new VariableParameter();
            }
        }
    }

    private void readValues(RSStream stream, int i) {
        do {
            int j = stream.getByte();
            if (j == 0) {
                return;
            }
            if (j == 1) {
                stream.getByte();
            } else if (j == 2) {
                stream.getByte();
            } else if (j == 3) {
                anIntArray703[anInt702++] = i;
            } else if (j == 4) {
            } else if (j == 5) {
                anInt709 = stream.getShort();
            } else if (j == 6) {
            } else if (j == 7) {
                stream.getInt();
            } else if (j == 8) {
                aBoolean713 = true;
            } else if (j == 10) {
                stream.getString();
            } else if (j == 11) {
                aBoolean713 = true;
            } else if (j == 12) {
                stream.getInt();
            } else if (j == 13) {
            } else {
                System.out.println("Error unrecognised config code: " + j);
            }
        } while (true);
    }
}
