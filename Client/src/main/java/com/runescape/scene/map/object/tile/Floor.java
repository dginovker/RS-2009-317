package com.runescape.scene.map.object.tile;

import com.runescape.Constants;
import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;
import com.runescape.util.FileUtility;

/**
 * Represents a floor texture.
 *
 * @author <a href="https://runescape.com/">RuneScape</a>
 */
public class Floor {

    public static Floor cache[];
    private int rgb;
    private int textureId;
    private boolean occlude;
    private int hue;
    private int saturation;
    private int lightness;
    private int anInt397;
    private int anInt398;
    private int anInt399;

    private Floor() {
        textureId = -1;
        occlude = true;
    }

    public static void unpackConfig(CacheArchive cacheArchive) {
        RSStream osrsStream = new RSStream(FileUtility.getBytes(Constants.getCachePath(true) + "/config/flo.dat"));
        int cacheSizeOSRS = osrsStream.getShort();

        if (cache == null) {
            cache = new Floor[cacheSizeOSRS];
        }

        for (int id = 0; id < cacheSizeOSRS; id++) {
            if (cache[id] == null) {
                cache[id] = new Floor();
            }
            cache[id].readValuesOSRS(osrsStream);
        }
    }

    private void readValuesOSRS(RSStream buffer) {
        for (;;) {
            int opcode = buffer.getByte();
            if (opcode == 0) {
                break;
            } else if (opcode == 1) {
                rgb = buffer.getTri();
                method262(rgb);
            } else {
                System.out.println("Error unrecognised underlay code: " + opcode);
            }
        }
    }

    private void method262(int i) {
        double d = (double) (i >> 16 & 0xff) / 256D;
        double d1 = (double) (i >> 8 & 0xff) / 256D;
        double d2 = (double) (i & 0xff) / 256D;
        double d3 = d;
        if (d1 < d3) {
            d3 = d1;
        }
        if (d2 < d3) {
            d3 = d2;
        }
        double d4 = d;
        if (d1 > d4) {
            d4 = d1;
        }
        if (d2 > d4) {
            d4 = d2;
        }
        double d5 = 0.0D;
        double d6 = 0.0D;
        double d7 = (d3 + d4) / 2D;
        if (d3 != d4) {
            if (d7 < 0.5D) {
                d6 = (d4 - d3) / (d4 + d3);
            }
            if (d7 >= 0.5D) {
                d6 = (d4 - d3) / (2D - d4 - d3);
            }
            if (d == d4) {
                d5 = (d1 - d2) / (d4 - d3);
            } else if (d1 == d4) {
                d5 = 2D + (d2 - d) / (d4 - d3);
            } else if (d2 == d4) {
                d5 = 4D + (d - d1) / (d4 - d3);
            }
        }
        d5 /= 6D;
        hue = (int) (d5 * 256D);
        saturation = (int) (d6 * 256D);
        lightness = (int) (d7 * 256D);
        if (d7 > 0.5D) {
            anInt398 = (int) ((1.0D - d7) * d6 * 512D);
        } else {
            anInt398 = (int) (d7 * d6 * 512D);
        }
        if (anInt398 < 1) {
            anInt398 = 1;
        }
        anInt397 = (int) (d5 * (double) anInt398);
        int k = hue;
        int l = saturation;
        int i1 = lightness;
        anInt399 = method263(k, l, i1);
    }

    private int method263(int i, int j, int k) {
        if (k > 179) {
            j /= 2;
        }
        if (k > 192) {
            j /= 2;
        }
        if (k > 217) {
            j /= 2;
        }
        if (k > 243) {
            j /= 2;
        }
        return (i / 4 << 10) + (j / 32 << 7) + k / 2;
    }

    public int getTextureId() {
        return textureId;
    }

    public boolean isOcclude() {
        return occlude;
    }

    public int getRGB() {
        return rgb;
    }

    public int setRGB(int rgb) {
        this.rgb = rgb;
        return rgb;
    }

    public int getHue() {
        return hue;
    }

    public int getSaturation() {
        return saturation;
    }

    public int getLightness() {
        return lightness;
    }

    public int getAnInt397() {
        return anInt397;
    }

    public int getAnInt398() {
        return anInt398;
    }

    public int getAnInt399() {
        return anInt399;
    }
}
