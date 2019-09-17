package com.runescape.media;

import com.runescape.net.RSStream;

public final class SkinList {

    public final int[] anIntArray342;
    public final int[][] anIntArrayArray343;

    public SkinList(RSStream stream) {
        int anInt341 = stream.getShort();
        anIntArray342 = new int[anInt341];
        anIntArrayArray343 = new int[anInt341][];
        for (int j = 0; j < anInt341; j++) {
            anIntArray342[j] = stream.getShort();
        }
        for (int j = 0; j < anInt341; j++) {
            anIntArrayArray343[j] = new int[stream.getShort()];
        }
        for (int j = 0; j < anInt341; j++) {
            for (int l = 0; l < anIntArrayArray343[j].length; l++) {
                anIntArrayArray343[j][l] = stream.getShort();
            }
        }
    }
}