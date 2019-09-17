package com.runescape.cache.texture;

import com.runescape.net.RSStream;

public class PalettedTexture extends Texture {
    public int[] palette;
    public byte[] indices;

    public PalettedTexture(int width, int height, RSStream buffer, boolean alpha) {
        super(width, height);
        int paletteSize = buffer.getByte();
        int[] palette = this.palette = new int[1 + paletteSize];
        for (int i = 0; i != paletteSize; ++i) {
            int pixel = buffer.getTri();
            if (!alpha) {
                pixel |= 0xff000000;
            }

            palette[1 + i] = pixel;
        }

        int count = width * height;
        byte[] indices = this.indices = new byte[count];
        for (int i = 0; i != count; ++i) {
            indices[i] = buffer.getSignedByte();
            if (!alpha && indices[i] == 0) {
                opaque = false;
            }

        }

    }

    public int getPixel(int i) {
        return palette[indices[i] & 0xff];
    }

    public byte[] getIndices() {
        return indices;
    }

    public void setIndices(byte[] indices) {
        this.indices = indices;
    }

    public String getType() {
        return "0";
    }

    public String toString() {
        return getType() + "	" + super.toString() + "	" + (palette.length - 1);
    }
}
