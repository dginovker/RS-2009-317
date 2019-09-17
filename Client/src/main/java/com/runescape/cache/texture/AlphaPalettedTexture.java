package com.runescape.cache.texture;

import com.runescape.net.RSStream;

public final class AlphaPalettedTexture extends PalettedTexture {

    private final byte[] alpha;

    public AlphaPalettedTexture(int width, int height, RSStream buffer) {
        super(width, height, buffer, true);
        int count = width * height;
        byte[] alpha = this.alpha = new byte[count];
        for (int i = 0; i != count; ++i) {
            alpha[i] = buffer.getSignedByte();
            if (alpha[i] == 0) {
                indices[i] = 0;
                opaque = false;
            } else if (alpha[i] != -1) {
                opaque = false;
                hasAlpha = true;
            }
        }

    }

    public String getID() {
        return "2";
    }

    public int getPixel(int i) {
        return ((alpha[i] & 0xff) << 24) | palette[indices[i] & 0xff];
    }
}
