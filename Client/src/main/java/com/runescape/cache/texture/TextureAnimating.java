package com.runescape.cache.texture;

import com.runescape.Game;
import com.runescape.cache.media.Background;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.scene.graphic.Rasterizer;
import com.runescape.scene.graphic.TextureLoader667;

public class TextureAnimating {

    private static final int[] ANIMATED_TEXTURES = {17, 34, 40, 1};

    private static byte[] pixels = new byte[16384];
    private static int[] hdPixels = new int[16384];

    /**
     * Animates on screen textures with a certain id.
     */
    public static void animateTexture() {
        try {
            if (!Rasterizer.lowMem) {
                for (int tex : ANIMATED_TEXTURES) {
                    if (Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
                        Texture t = Texture.get(tex);
                        if (t instanceof RGBTexture) {
                            RGBTexture rgbT = (RGBTexture) t;
                            int indexes = rgbT.width * rgbT.height - 1;
                            int noise = rgbT.width * Game.INSTANCE.cycleTimer * 2;
                            int current[] = rgbT.pixels;
                            int next[] = hdPixels;
                            for (int i2 = 0; i2 <= indexes; i2++) {
                                next[i2] = current[i2 - noise & indexes];
                            }

                            rgbT.setPixels(next);
                            hdPixels = current;
                            TextureLoader667.resetTexture(tex);
                        } else if (t instanceof ARGBTexture) {
                            ARGBTexture rgbT = (ARGBTexture) t;
                            int indexes = rgbT.width * rgbT.height - 1;
                            int noise = rgbT.width * Game.INSTANCE.cycleTimer * 2;
                            int current[] = rgbT.pixels;
                            int next[] = hdPixels;
                            for (int i2 = 0; i2 <= indexes; i2++) {
                                next[i2] = current[i2 - noise & indexes];
                            }

                            rgbT.setPixels(next);
                            hdPixels = current;
                            TextureLoader667.resetTexture(tex);
                        } else if (t instanceof PalettedTexture || t instanceof AlphaPalettedTexture) {
                            PalettedTexture rgbT = (PalettedTexture) t;
                            int indexes = rgbT.width * rgbT.height - 1;
                            int noise = rgbT.width * Game.INSTANCE.cycleTimer * 2;
                            byte current[] = rgbT.getIndices();
                            byte next[] = pixels;
                            for (int i2 = 0; i2 <= indexes; i2++) {
                                next[i2] = current[i2 - noise & indexes];
                            }

                            rgbT.setIndices(next);
                            pixels = current;
                            TextureLoader667.resetTexture(tex);
                        }
                    } else {
                        Background background = TextureLoader317.textureImages[tex];
                        int indexes = background.anInt1452 * background.anInt1453 - 1;
                        int noise = background.anInt1452 * Game.INSTANCE.cycleTimer * 2;
                        byte current[] = background.aByteArray1450;
                        byte next[] = pixels;
                        for (int i2 = 0; i2 <= indexes; i2++) {
                            next[i2] = current[i2 - noise & indexes];
                        }

                        background.aByteArray1450 = next;
                        pixels = current;
                        TextureLoader317.resetTexture(tex);
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
