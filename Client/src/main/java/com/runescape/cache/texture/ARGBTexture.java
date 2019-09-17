package com.runescape.cache.texture;

import com.runescape.net.RSStream;

public final class ARGBTexture extends Texture {
    public int[] pixels;

    public ARGBTexture(int width, int height, RSStream rsStream) {
        super(width, height);
        int count = width * height;
        int[] pixels = this.pixels = new int[count];
        for (int i = 0; i != count; ++i) {
            int pixel = rsStream.getInt();
            if ((pixel & 0xff000000) == 0) {
                pixel = 0;
            }

            int alpha = pixel & 0xff000000;
            if (alpha != 0xff000000) {
                opaque = false;
                if (alpha != 0) {
                    hasAlpha = true;
                }

            }
            pixels[i] = pixel;
        }

    }

    public int getPixel(int i) {
        return pixels[i];
    }

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public String toString() {
        return "3	" + super.toString();
    }
}
