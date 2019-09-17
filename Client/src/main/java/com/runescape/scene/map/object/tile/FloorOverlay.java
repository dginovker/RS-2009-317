package com.runescape.scene.map.object.tile;

import com.runescape.Constants;
import com.runescape.net.CacheArchive;
import com.runescape.util.FileUtility;

import java.nio.ByteBuffer;

public final class FloorOverlay {

    public static FloorOverlay[] cache;
    public boolean aBoolean393;//To add
    public int groundTextureOverlay = -1;
    public int groundColorOverlay;
    public boolean occludeOverlay;
    public int detailedColor;
    public int detailedTexture9;
    public boolean boolean_10;
    public int detailedTexture11;
    public boolean boolean_12;
    public int detailedColor13;
    public int detailedTexture14;
    public int int_15;
    public int alpha;

    /**
     * Hsl
     */
    public int groundHueOverlay;
    public int groundSaturationOverlay;
    public int groundLightnessOverlay;
    public int weighted_hue;
    public int chroma_overlay;
    public int hslOverlayColor;
    public int anInt390;


    public static void unpackConfig(CacheArchive cacheArchive) {
        ByteBuffer osrsStream = ByteBuffer.wrap(FileUtility.getBytes(Constants.getCachePath(true) + "/config/flo.dat"));
        osrsStream.position(512);
        int countOSRS = osrsStream.getShort();
        cache = new FloorOverlay[countOSRS];
        for (int i = 0; i < countOSRS; i++) {
            if (cache[i] == null) {
                cache[i] = new FloorOverlay();
            }

                cache[i].parseOSRS(osrsStream);
                if (i == 113) {
                    cache[i].groundTextureOverlay = 25;
                }
                if (i == 151) { // Lava
                    cache[i].groundTextureOverlay = 40;
                }

        }
    }

    private void parseOSRS(ByteBuffer buffer) {
        for (;;) {
            int opcode = buffer.get();
            if (opcode == 0) {
                break;
            } else if (opcode == 1) {
                groundColorOverlay = ((buffer.get() & 0xff) << 16) + ((buffer.get() & 0xff) << 8) + (buffer.get() & 0xff);
                rgbhsl(groundColorOverlay);
            } else if (opcode == 2) {
                groundTextureOverlay = buffer.get() & 0xff;
            } else if (opcode == 5) {
                occludeOverlay = false;
            } else if (opcode == 7) {
                detailedColor = ((buffer.get() & 0xff) << 16) + ((buffer.get() & 0xff) << 8) + (buffer.get() & 0xff);
                rgbhsl(detailedColor);
            } else {
                System.out.println("Error unrecognised overlay code: " + opcode);
            }
        }
    }

    private void rgbhsl(int color) {
        double red = (double) (color >> 16 & 0xff) / 255.0;
        double green = (double) (color >> 8 & 0xff) / 255.0;
        double blue = (double) (color & 0xff) / 255.0;
        double min = red;
        if (green < min) {
            min = green;
        }
        if (blue < min) {
            min = blue;
        }
        double max = red;
        if (green > max) {
            max = green;
        }
        if (blue > max) {
            max = blue;
        }
        double hue = 0.0;
        double saturation = 0.0;
        double luminance = (min + max) / 2.0;
        if (min != max) {
            if (luminance < 0.5) {
                saturation = (max - min) / (max + min);
            }
            if (luminance >= 0.5) {
                saturation = (max - min) / (2.0 - max - min);
            }
            if (red == max) {
                hue = (green - blue) / (max - min);
            } else if (green == max) {
                hue = 2.0 + (blue - red) / (max - min);
            } else if (blue == max) {
                hue = 4.0 + (red - green) / (max - min);
            }
        }
        hue /= 6.0;
        groundHueOverlay = (int) (hue * 255.0);
        groundSaturationOverlay = (int) (saturation * 255.0);
        groundLightnessOverlay = (int) (luminance * 255.0);
        int hue_overlay = groundHueOverlay;
        int sat_overlay = groundSaturationOverlay;
        int lum_overlay = groundLightnessOverlay;
        if (sat_overlay < 0) {
            sat_overlay = 0;
        } else if (sat_overlay > 255) {
            sat_overlay = 255;
        }
        if (lum_overlay < 0) {
            lum_overlay = 0;
        } else if (lum_overlay > 255) {
            lum_overlay = 255;
        }
        if (luminance > 0.5) {
            chroma_overlay = (int) ((1.0 - luminance) * saturation * 512.0);
        } else {
            chroma_overlay = (int) (luminance * saturation * 512.0);
        }
        if (chroma_overlay < 1) {
            chroma_overlay = 1;
        }
        weighted_hue = (int) (hue * (double) chroma_overlay);
        int hue_offset = hue_overlay;
        int sat_offset = sat_overlay;
        int lum_offset = lum_overlay;
        hslOverlayColor = encode(hue_offset, sat_offset, lum_offset);
    }

    private final int encode(int arg0, int arg1, int arg2) {
        if (arg2 > 179) {
            arg1 /= 2;
        }
        if (arg2 > 192) {
            arg1 /= 2;
        }
        if (arg2 > 217) {
            arg1 /= 2;
        }
        if (arg2 > 243) {
            arg1 /= 2;
        }
        int i = (arg0 / 4 << 10) + (arg1 / 32 << 7) + arg2 / 2;
        return i;
    }

}