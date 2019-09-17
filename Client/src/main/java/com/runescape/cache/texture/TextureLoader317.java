package com.runescape.cache.texture;

import com.runescape.Constants;
import com.runescape.Game;
import com.runescape.cache.media.Background;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.net.CacheArchive;
import com.runescape.scene.graphic.Rasterizer;
import com.runescape.scene.graphic.TextureLoader667;


public class TextureLoader317 {
    public static Background[] textureImages = new Background[51];
    public static boolean[] textureIsTransparent = new boolean[51];
    public static int[] textureLastUsed = new int[51];
    public static int textureGetCount;
    private static int[][] texturePalettes = new int[51][];
    private static int loadedTextureCount;
    private static int[] averageTextureColour = new int[51];
    private static int[][] texelCache = new int[51][];
    private static int textureTexelPoolPointer;
    private static int[][] texelArrayPool;

    public static void clear() {
        textureImages = null;
        textureIsTransparent = null;
        averageTextureColour = null;
        texelArrayPool = null;
        texelCache = null;
        textureLastUsed = null;
        texturePalettes = null;
    }

    public static void calculateTexturePalette(double brightness) {
        for (int l = 0; l < 51; l++) {
            if (textureImages[l] != null) {
                int ai[] = textureImages[l].anIntArray1451;
                texturePalettes[l] = new int[ai.length];
                for (int j1 = 0; j1 < ai.length; j1++) {
                    texturePalettes[l][j1] = Rasterizer.adjustBrightness(ai[j1], brightness);
                    if ((texturePalettes[l][j1] & 0xf8f8ff) == 0 && j1 != 0) {
                        texturePalettes[l][j1] = 1;
                    }
                }
            }
        }

        for (int i1 = 0; i1 < 51; i1++) {
            resetTexture(i1);
        }
    }

    public static void clearTextureCache() {
        texelArrayPool = null;
        for (int j = 0; j < 51; j++) {
            texelCache[j] = null;
        }
    }

    public static void resetTextures() {
        if (Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
            TextureLoader667.resetTextures();
            return;
        }
        if (texelArrayPool == null) {
            textureTexelPoolPointer = 20;// was parameter
            if (Rasterizer.lowMem) {
                texelArrayPool = new int[textureTexelPoolPointer][16384];
            } else {
                texelArrayPool = new int[textureTexelPoolPointer][0x10000];
            }
            for (int k = 0; k < 51; k++) {
                texelCache[k] = null;
            }

        }
    }

    public static void unpackTextures(CacheArchive cacheArchive) {
        loadedTextureCount = 0;
        for (int j = 0; j < 51; j++) {
            try {
                textureImages[j] = new Background(cacheArchive, String.valueOf(j), 0);
                if (Rasterizer.lowMem && textureImages[j].anInt1456 == 128) {
                    textureImages[j].reduceSetOffset();
                } else {
                    textureImages[j].setOffset();
                }
                loadedTextureCount++;
            } catch (Exception _ex) {
                if (Constants.DEBUG_MODE) {
                    _ex.printStackTrace();
                }
            }
        }
    }

    public static int getAverageTextureColour(int textureId) {
        if (averageTextureColour[textureId] != 0) {
            return averageTextureColour[textureId];
        }
        int red = 0;
        int green = 0;
        int blue = 0;
        int colourCount = texturePalettes[textureId].length;

        for (int k1 = 0; k1 < colourCount; k1++) {
            red += texturePalettes[textureId][k1] >> 16 & 0xff;
            green += texturePalettes[textureId][k1] >> 8 & 0xff;
            blue += texturePalettes[textureId][k1] & 0xff;
        }

        int rgb = (red / colourCount << 16) + (green / colourCount << 8) + blue / colourCount;
        rgb = Rasterizer.adjustBrightness(rgb, 1.3999999999999999D);
        if (rgb == 0) {
            rgb = 1;
        }
        averageTextureColour[textureId] = rgb;
        return rgb;
    }

    public static void resetTexture(int textureId) {
        if (texelCache[textureId] == null) {
            return;
        }
        texelArrayPool[textureTexelPoolPointer++] = texelCache[textureId];
        texelCache[textureId] = null;
    }

    public static int[] getTexturePixels(int textureId) {
        if (textureId == 1) {
            textureId = 24;
        }
        textureLastUsed[textureId] = textureGetCount++;
        if (texelCache[textureId] != null) {
            return texelCache[textureId];
        }
        int ai[];
        if (textureTexelPoolPointer > 0) {
            ai = texelArrayPool[--textureTexelPoolPointer];
            texelArrayPool[textureTexelPoolPointer] = null;
        } else {
            int j = 0;
            int k = -1;
            for (int l = 0; l < loadedTextureCount; l++) {
                if (texelCache[l] != null && (textureLastUsed[l] < j || k == -1)) {
                    j = textureLastUsed[l];
                    k = l;
                }
            }

            ai = texelCache[k];
            texelCache[k] = null;
        }
        texelCache[textureId] = ai;
        Background background = textureImages[textureId];
        int ai1[] = texturePalettes[textureId];
        if (Rasterizer.lowMem) {
            textureIsTransparent[textureId] = false;
            for (int i1 = 0; i1 < 4096; i1++) {
                int i2 = ai[i1] = ai1[background.aByteArray1450[i1]] & 0xf8f8ff;
                if (i2 == 0) {
                    textureIsTransparent[textureId] = true;
                }
                ai[4096 + i1] = i2 - (i2 >>> 3) & 0xf8f8ff;
                ai[8192 + i1] = i2 - (i2 >>> 2) & 0xf8f8ff;
                ai[12288 + i1] = i2 - (i2 >>> 2) - (i2 >>> 3) & 0xf8f8ff;
            }

        } else {
            if (background.anInt1452 == 64) {
                for (int j1 = 0; j1 < 128; j1++) {
                    for (int j2 = 0; j2 < 128; j2++) {
                        ai[j2 + (j1 << 7)] = ai1[background.aByteArray1450[(j2 >> 1) + ((j1 >> 1) << 6)]];
                    }

                }

            } else {
                for (int k1 = 0; k1 < 16384; k1++) {
                    ai[k1] = ai1[background.aByteArray1450[k1]];
                }

            }
            textureIsTransparent[textureId] = false;
            for (int l1 = 0; l1 < 16384; l1++) {
                ai[l1] &= 0xf8f8ff;
                int k2 = ai[l1];
                if (k2 == 0) {
                    textureIsTransparent[textureId] = true;
                }
                ai[16384 + l1] = k2 - (k2 >>> 3) & 0xf8f8ff;
                ai[32768 + l1] = k2 - (k2 >>> 2) & 0xf8f8ff;
                ai[49152 + l1] = k2 - (k2 >>> 2) - (k2 >>> 3) & 0xf8f8ff;
            }

        }
        return ai;
    }
}
