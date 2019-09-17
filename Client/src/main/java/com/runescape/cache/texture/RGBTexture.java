package com.runescape.cache.texture;

import com.runescape.net.RSStream;

public final class RGBTexture extends Texture {
    public int[] pixels;

    public RGBTexture(int width, int height, RSStream buffer) {
        super(width, height);
        int count = width * height;
        int[] pixels = this.pixels = new int[count];
        for (int i = 0; i != count; ++i) {
            int pixel = buffer.getTri();
            if (pixel != 0) {
                pixel |= 0xff000000;
            } else {
                opaque = false;
            }

            pixels[i] = pixel;
        }

    }

    public int getPixel(int i) {
        return pixels[i];
    }

    public String toString() {
        return "1	" + super.toString();
    }

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }
}
