package com.runescape.cache.texture;

import com.runescape.net.RSStream;
import com.runescape.net.requester.ResourceProvider;

public class Texture {
    static ResourceProvider resourceProvider;
    private static boolean[] loaded;
    private static Texture[] textures;
    public final int width;
    public final int height;
    public boolean opaque;
    public boolean hasAlpha;

    public Texture(int width, int height) {
        this.width = width;
        this.height = height;
        opaque = true;
    }

    public static Texture get(int id) {
        return null;
     /*   if (id < 0 || id >= textures.length) {
            return null;
        }
        if (loaded[id]) {
            return textures[id];
        }
        load(id, null);
        resourceProvider.provide(Constants.TEXTURE_INDEX - 1, id);
        return textures[id];*/
    }

    public static void init(int count, ResourceProvider resourceProvider1) {
        textures = new Texture[count];
        loaded = new boolean[count];
        resourceProvider = resourceProvider1;
    }

    public static void load(int id, byte[] data) {
        loaded[id] = true;
        if (data != null && data.length >= 5) {
            RSStream rsStream = new RSStream(data);
            int type = rsStream.getByte();
            int width = rsStream.getShort();
            int height = rsStream.getShort();
            if (type == 0) {
                textures[id] = new PalettedTexture(width, height, rsStream, false);
            } else if (type == 1) {
                textures[id] = new RGBTexture(width, height, rsStream);
            } else if (type == 2) {
                textures[id] = new AlphaPalettedTexture(width, height, rsStream);
            } else if (type == 3) {
                textures[id] = new ARGBTexture(width, height, rsStream);
            }
        }
    }

    public static void nullLoader() {
        loaded = null;
        textures = null;
        resourceProvider = null;
    }

    public static int getTotal() {
        return textures.length;
    }

    public int getPixel(int i) {
        return 0xffffffff;
    }

    public String toString() {
        return width + " X " + height + "	" + (opaque ? "+opaque" : "-opaque") + "	" + (hasAlpha ? "+alpha" : "-alpha");
    }
}