package com.runescape.cache.def;

import com.runescape.net.CacheArchive;
import com.runescape.net.RSStream;

/**
 * Represents a texture definition.
 *
 * @author <a href="http://runescape.com/">RuneScape</a>
 */
public final class TextureDefinition {
    public static TextureDefinition[] textures;
    public boolean aBoolean1223;
    public int anInt1226;
    boolean aBoolean1204;
    boolean aBoolean1205;
    byte aByte1217;
    byte aByte1225;
    byte aByte1214;
    byte aByte1213;
    short aShort1221;
    byte aByte1211;
    byte aByte1203;
    boolean aBoolean1222;
    boolean aBoolean1216;
    byte aByte1207;
    boolean aBoolean1212;
    boolean aBoolean1210;
    boolean aBoolean1215;
    int anInt1202;
    int anInt1206;

    private TextureDefinition() {
    }

    public static void unpackConfig(CacheArchive cacheArchive) {
        RSStream rsStream = new RSStream(cacheArchive.getEntry("texture.dat"));
        int count = rsStream.getShort();
        textures = new TextureDefinition[count];
        for (int i = 0; i != count; ++i) {
            if (rsStream.getByte() == 1) {
                textures[i] = new TextureDefinition();
            }
        }

        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aBoolean1223 = rsStream.getByte() == 1;
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aBoolean1204 = rsStream.getByte() == 1;
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aBoolean1205 = rsStream.getByte() == 1;
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aByte1217 = rsStream.getSignedByte();
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aByte1225 = rsStream.getSignedByte();
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aByte1214 = rsStream.getSignedByte();
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aByte1213 = rsStream.getSignedByte();
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aShort1221 = (short) rsStream.getShort();
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aByte1211 = rsStream.getSignedByte();
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aByte1203 = rsStream.getSignedByte();
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aBoolean1222 = rsStream.getByte() == 1;
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aBoolean1216 = rsStream.getByte() == 1;
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aByte1207 = rsStream.getSignedByte();
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aBoolean1212 = rsStream.getByte() == 1;
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aBoolean1210 = rsStream.getByte() == 1;
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].aBoolean1215 = rsStream.getByte() == 1;
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].anInt1202 = rsStream.getByte();
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].anInt1206 = rsStream.getInt();
            }
        }


        for (int i = 0; i != count; ++i) {
            if (textures[i] != null) {
                textures[i].anInt1226 = rsStream.getByte();
            }
        }


    }

    public static void nullLoader() {
        textures = null;
    }
}
