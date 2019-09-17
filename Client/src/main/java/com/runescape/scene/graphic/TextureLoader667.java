package com.runescape.scene.graphic;

import com.runescape.cache.def.TextureDefinition;
import com.runescape.cache.texture.Texture;
import com.runescape.net.requester.ResourceProvider;

public class TextureLoader667 {

    public static int[] textureLastUsed;
    public static int textureGetCount;
    private static int loadedTextureCount;
    private static int textureTexelPoolPointer;
    private static int[][] texelArrayPool;
    private static int[][] texelCache;
    private static float brightness = 1.0F;

    public static void initTextures(int count, ResourceProvider resourceProvider) {
        Texture.init(count, resourceProvider);
        loadedTextureCount = count;
        textureLastUsed = new int[count];
        texelCache = new int[count][];
    }

    public static void clearTextureCache() {
        texelArrayPool = null;
        for (int i = 0; i < loadedTextureCount; i++) {
            texelCache[i] = null;
        }
    }

    public static void resetTextures() {
        if (texelArrayPool == null) {
            textureTexelPoolPointer = 50;//was parameter
            texelArrayPool = new int[textureTexelPoolPointer][0x10000];
            for (int k = 0; k < loadedTextureCount; k++) {
                texelCache[k] = null;
            }
        }
    }

    public static void resetTexture(int textureId) {
        if (texelCache[textureId] == null) {
            return;
        }
        texelArrayPool[textureTexelPoolPointer++] = texelCache[textureId];
        texelCache[textureId] = null;
    }

    public static int[] getTexturePixels(int textureId) {
        if (textureId == 0) {
            textureId = 24;
        }
        Texture texture = Texture.get(textureId);
        if (texture == null) {
            return null;
        }

        textureLastUsed[textureId] = textureGetCount++;
        if (texelCache[textureId] != null) {
            return texelCache[textureId];
        }

        int[] texels;
        //Start of mem management code
        if (textureTexelPoolPointer > 0) {    //Freed texture data arrays available
            texels = texelArrayPool[--textureTexelPoolPointer];
            texelArrayPool[textureTexelPoolPointer] = null;
        } else {   //No freed texture data arrays available, recycle least used texture's array
            int lastUsed = 0;
            int target = -1;
            for (int i = 0; i < loadedTextureCount; i++) {
                if (texelCache[i] != null && (textureLastUsed[i] < lastUsed || target == -1)) {
                    lastUsed = textureLastUsed[i];
                    target = i;
                }
            }

            texels = texelCache[target];
            texelCache[target] = null;
        }
        texelCache[textureId] = texels;
        //End of mem management code
        if (texture.width == 64) {
            for (int y = 0; y < 128; y++) {
                for (int x = 0; x < 128; x++) {
                    texels[x + (y << 7)] = texture.getPixel((x >> 1) + ((y >> 1) << 6));
                }
            }
        } else {
            for (int texelPtr = 0; texelPtr < 16384; texelPtr++) {
                texels[texelPtr] = texture.getPixel(texelPtr);
            }
        }
        TextureDefinition def = textureId >= 0 && textureId < TextureDefinition.textures.length ? TextureDefinition.textures[textureId] : null;
        int blendType = def != null ? def.anInt1226 : 0;
        if (blendType != 1 && blendType != 2) {
            blendType = 0;
        }

        for (int texelPtr = 0; texelPtr < 16384; texelPtr++) {
            int texel = texels[texelPtr];
            int alpha;
            if (blendType == 2) {
                alpha = texel >>> 24;
            } else if (blendType == 1) {
                alpha = texel != 0 ? 0xff : 0;
            } else {
                alpha = texel >>> 24;
                if (def != null && !def.aBoolean1223) {
                    alpha /= 5;
                }

            }
            texel &= 0xffffff;
            if (textureId == 1 || textureId == 24) {
                texel = adjustBrightnessLinear(texel, 190);//379
            } else {
                texel = adjustBrightnessLinear(texel, 179);
            }
            if (textureId == 1 || textureId == 24) {
               // texel = adjustBrightness(texel, 0.90093F);
            } else {
                texel = adjustBrightness(texel, brightness);
            }
            texel &= 0xf8f8ff;
            texels[texelPtr] = texel | (alpha << 24);
            texels[16384 + texelPtr] = ((texel - (texel >>> 3)) & 0xf8f8ff) | (alpha << 24);
            texels[32768 + texelPtr] = ((texel - (texel >>> 2)) & 0xf8f8ff) | (alpha << 24);
            texels[49152 + texelPtr] = ((texel - (texel >>> 3) - (texel >>> 3)) & 0xf8f8ff) | (alpha << 24);
        }
        return texels;
    }

    private static int adjustBrightness(int rgb, float brightness) {
        return ((int) ((float) Math.pow((double) ((float) (rgb >>> 16) / 256.0F), (double) brightness) * 256.0F) << 16) |
                ((int) ((float) Math.pow((double) ((float) ((rgb >>> 8) & 0xff) / 256.0F), (double) brightness) * 256.0F) << 8) |
                (int) ((float) Math.pow((double) ((float) (rgb & 0xff) / 256.0F), (double) brightness) * 256.0F);
    }

    private static int adjustBrightnessLinear(int rgb, int factor) {
        return ((((rgb >>> 16) * factor) & 0xff00) << 8) |
                ((((rgb >>> 8) & 0xff) * factor) & 0xff00) |
                (((rgb & 0xff) * factor) >> 8);
    }

    public static void calculateTexturePalette(double brightness) {
        for (int textureId = 0; textureId != loadedTextureCount; ++textureId) {
            resetTexture(textureId);
        }
    }

    public static void clear() {
        texelArrayPool = null;
        texelCache = null;
        textureLastUsed = null;
        brightness = 1.0F;
    }

    public boolean isValid() {
        return texelArrayPool != null && texelCache != null;
    }
}
