package com.runescape.scene.map.object.tile;

import com.runescape.Game;
import com.runescape.cache.media.inter.InterfaceConfiguration;

public final class ShapedTile {
    public static final int[] screenX = new int[6];
    public static final int[] screenY = new int[6];
    public static final int[] screenZ = new int[6];
    public static final int[] viewSpaceX = new int[6];
    public static final int[] viewSpaceY = new int[6];
    public static final int[] viewSpaceZ = new int[6];
    private static final int[][] anIntArrayArray696 = {
            {
                    1, 3, 5, 7
            }, {
            1, 3, 5, 7
    }, {
            1, 3, 5, 7
    }, {
            1, 3, 5, 7, 6
    }, {
            1, 3, 5, 7, 6
    }, {
            1, 3, 5, 7, 6
    }, {
            1, 3, 5, 7, 6
    }, {
            1, 3, 5, 7, 2, 6
    }, {
            1, 3, 5, 7, 2, 8
    }, {
            1, 3, 5, 7, 2, 8
    }, {
            1, 3, 5, 7, 11, 12
    }, {
            1, 3, 5, 7, 11, 12
    }, {
            1, 3, 5, 7, 13, 14
    }
    };
    private static final int[][] shapedTileElementData = {
            {
                    0, 1, 2, 3, 0, 0, 1, 3
            }, {
            1, 1, 2, 3, 1, 0, 1, 3
    }, {
            0, 1, 2, 3, 1, 0, 1, 3
    }, {
            0, 0, 1, 2, 0, 0, 2, 4, 1, 0,
            4, 3
    }, {
            0, 0, 1, 4, 0, 0, 4, 3, 1, 1,
            2, 4
    }, {
            0, 0, 4, 3, 1, 0, 1, 2, 1, 0,
            2, 4
    }, {
            0, 1, 2, 4, 1, 0, 1, 4, 1, 0,
            4, 3
    }, {
            0, 4, 1, 2, 0, 4, 2, 5, 1, 0,
            4, 5, 1, 0, 5, 3
    }, {
            0, 4, 1, 2, 0, 4, 2, 3, 0, 4,
            3, 5, 1, 0, 4, 5
    }, {
            0, 0, 4, 5, 1, 4, 1, 2, 1, 4,
            2, 3, 1, 4, 3, 5
    }, {
            0, 0, 1, 5, 0, 1, 4, 5, 0, 1,
            2, 4, 1, 0, 5, 3, 1, 5, 4, 3,
            1, 4, 2, 3
    }, {
            1, 0, 1, 5, 1, 1, 4, 5, 1, 1,
            2, 4, 0, 0, 5, 3, 0, 5, 4, 3,
            0, 4, 2, 3
    }, {
            1, 0, 5, 4, 1, 0, 1, 5, 0, 0,
            4, 3, 0, 4, 5, 3, 0, 5, 2, 3,
            0, 1, 2, 5
    }
    };
    public final int[] origVertexX;
    public final int[] origVertexY;
    public final int[] origVertexZ;
    public final int[] triangleHslA;
    public final int[] triangleHslB;
    public final int[] triangleHslC;
    public final int[] triangleA;
    public final int[] triangleB;
    public final int[] triangleC;
    public final boolean flat;
    public final int shape;
    public final int rotation;
    public final int colourRGB;
    public final int colourRGBA;
    public int[] displayColor;
    public int color61;
    public int color71;
    public int color81;
    public int color91;
    public int color62;
    public int color72;
    public int color82;
    public int color92;
    public int[] triangleTexture;
    public boolean textured;

    public ShapedTile(int y, int hsl_shadow_a, int shadow_c, int zd, int overlay_texture, int hsl_shadow_d, int rotation,
                      int shadow_a, int rgb_bitset, int shadow_d, int zc, int zb, int za, int shape,
                      int hsl_shadow_c, int hsl_shadow_b, int shadow_b, int x, int rgb, int overlay_color, int underlay_texture,
                      int underlay_color, boolean tex) {
        flat = !(za != zb || za != zd || za != zc);
        this.shape = shape;
        this.rotation = rotation;
        colourRGB = rgb_bitset;
        colourRGBA = rgb;
        color61 = shadow_a;
        color71 = shadow_b;
        color81 = shadow_d;
        color91 = shadow_c;
        color62 = hsl_shadow_a;
        color72 = hsl_shadow_b;
        color82 = hsl_shadow_d;
        color92 = hsl_shadow_c;
        textured = tex;
        char c = '\200';
        int i5 = c / 2;
        int j5 = c / 4;
        int k5 = (c * 3) / 4;
        int ai[] = anIntArrayArray696[shape];
        int l5 = ai.length;
        origVertexX = new int[l5];
        origVertexY = new int[l5];
        origVertexZ = new int[l5];
        int tileShadow[] = new int[l5];
        int tileHslShadow[] = new int[l5];
        int i6 = x * c;
        int j6 = y * c;
        for (int k6 = 0; k6 < l5; k6++) {
            int l6 = ai[k6];
            if ((l6 & 1) == 0 && l6 <= 8) {
                l6 = (l6 - rotation - rotation - 1 & 7) + 1;
            }
            if (l6 > 8 && l6 <= 12) {
                l6 = (l6 - 9 - rotation & 3) + 9;
            }
            if (l6 > 12 && l6 <= 16) {
                l6 = (l6 - 13 - rotation & 3) + 13;
            }
            int i7;
            int k7;
            int i8;
            int shadow;
            int hsl_shadow;
            if (l6 == 1) {
                i7 = i6;
                k7 = j6;
                i8 = za;
                shadow = shadow_a;
                hsl_shadow = hsl_shadow_a;
            } else if (l6 == 2) {
                i7 = i6 + i5;
                k7 = j6;
                i8 = za + zb >> 1;
                shadow = shadow_a + shadow_b >> 1;
                hsl_shadow = hsl_shadow_a + hsl_shadow_b >> 1;
            } else if (l6 == 3) {
                i7 = i6 + c;
                k7 = j6;
                i8 = zb;
                shadow = shadow_b;
                hsl_shadow = hsl_shadow_b;
            } else if (l6 == 4) {
                i7 = i6 + c;
                k7 = j6 + i5;
                i8 = zb + zd >> 1;
                shadow = shadow_b + shadow_d >> 1;
                hsl_shadow = hsl_shadow_b + hsl_shadow_d >> 1;
            } else if (l6 == 5) {
                i7 = i6 + c;
                k7 = j6 + c;
                i8 = zd;
                shadow = shadow_d;
                hsl_shadow = hsl_shadow_d;
            } else if (l6 == 6) {
                i7 = i6 + i5;
                k7 = j6 + c;
                i8 = zd + zc >> 1;
                shadow = shadow_d + shadow_c >> 1;
                hsl_shadow = hsl_shadow_d + hsl_shadow_c >> 1;
            } else if (l6 == 7) {
                i7 = i6;
                k7 = j6 + c;
                i8 = zc;
                shadow = shadow_c;
                hsl_shadow = hsl_shadow_c;
            } else if (l6 == 8) {
                i7 = i6;
                k7 = j6 + i5;
                i8 = zc + za >> 1;
                shadow = shadow_c + shadow_a >> 1;
                hsl_shadow = hsl_shadow_c + hsl_shadow_a >> 1;
            } else if (l6 == 9) {
                i7 = i6 + i5;
                k7 = j6 + j5;
                i8 = za + zb >> 1;
                shadow = shadow_a + shadow_b >> 1;
                hsl_shadow = hsl_shadow_a + hsl_shadow_b >> 1;
            } else if (l6 == 10) {
                i7 = i6 + k5;
                k7 = j6 + i5;
                i8 = zb + zd >> 1;
                shadow = shadow_b + shadow_d >> 1;
                hsl_shadow = hsl_shadow_b + hsl_shadow_d >> 1;
            } else if (l6 == 11) {
                i7 = i6 + i5;
                k7 = j6 + k5;
                i8 = zd + zc >> 1;
                shadow = shadow_d + shadow_c >> 1;
                hsl_shadow = hsl_shadow_d + hsl_shadow_c >> 1;
            } else if (l6 == 12) {
                i7 = i6 + j5;
                k7 = j6 + i5;
                i8 = zc + za >> 1;
                shadow = shadow_c + shadow_a >> 1;
                hsl_shadow = hsl_shadow_c + hsl_shadow_a >> 1;
            } else if (l6 == 13) {
                i7 = i6 + j5;
                k7 = j6 + j5;
                i8 = za;
                shadow = shadow_a;
                hsl_shadow = hsl_shadow_a;
            } else if (l6 == 14) {
                i7 = i6 + k5;
                k7 = j6 + j5;
                i8 = zb;
                shadow = shadow_b;
                hsl_shadow = hsl_shadow_b;
            } else if (l6 == 15) {
                i7 = i6 + k5;
                k7 = j6 + k5;
                i8 = zd;
                shadow = shadow_d;
                hsl_shadow = hsl_shadow_d;
            } else {
                i7 = i6 + j5;
                k7 = j6 + k5;
                i8 = zc;
                shadow = shadow_c;
                hsl_shadow = hsl_shadow_c;
            }
            origVertexX[k6] = i7;
            origVertexY[k6] = i8;
            origVertexZ[k6] = k7;
            tileShadow[k6] = shadow;
            tileHslShadow[k6] = hsl_shadow;
        }

        // NEW
        int ai3[] = shapedTileElementData[shape];
        int j7 = ai3.length / 4;
        triangleA = new int[j7];
        triangleB = new int[j7];
        triangleC = new int[j7];
        triangleHslA = new int[j7];
        triangleHslB = new int[j7];
        triangleHslC = new int[j7];
        if (overlay_texture != -1 || underlay_texture != -1 && Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
            triangleTexture = new int[j7];
            if (Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
                displayColor = new int[j7];
            }
        }
        int l7 = 0;
        for (int j8 = 0; j8 < j7; j8++) {
            int l8 = ai3[l7];
            int k9 = ai3[l7 + 1];
            int i10 = ai3[l7 + 2];
            int k10 = ai3[l7 + 3];
            l7 += 4;
            if (k9 < 4) {
                k9 = k9 - rotation & 3;
            }
            if (i10 < 4) {
                i10 = i10 - rotation & 3;
            }
            if (k10 < 4) {
                k10 = k10 - rotation & 3;
            }
            triangleA[j8] = k9;
            triangleB[j8] = i10;
            triangleC[j8] = k10;
            if (l8 == 0) {
                triangleHslA[j8] = tileShadow[k9];
                triangleHslB[j8] = tileShadow[i10];
                triangleHslC[j8] = tileShadow[k10];
                if (!Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
                    if (triangleTexture != null) {
                        triangleTexture[j8] = -1;
                    }
                } else {
                    if (triangleTexture != null) {
                        triangleTexture[j8] = underlay_texture;//-1
                    }

                    if (displayColor != null) {
                        displayColor[j8] = underlay_color;
                    }
                }
            } else {
                triangleHslA[j8] = tileHslShadow[k9];
                triangleHslB[j8] = tileHslShadow[i10];
                triangleHslC[j8] = tileHslShadow[k10];
                if (Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
                    if (triangleTexture != null) {
                        triangleTexture[j8] = overlay_texture;
                    }

                    if (displayColor != null) {
                        displayColor[j8] = overlay_color;
                    }
                } else {
                    if (triangleTexture != null) {
                        triangleTexture[j8] = overlay_texture;
                    }
                }
            }
        }
        // END NEW

        int i9 = za;
        int l9 = zb;
        if (zb < i9) {
            i9 = zb;
        }
        if (zb > l9) {
            l9 = zb;
        }
        if (zd < i9) {
            i9 = zd;
        }
        if (zd > l9) {
            l9 = zd;
        }
        if (zc < i9) {
            i9 = zc;
        }
        if (zc > l9) {
            l9 = zc;
        }
        i9 /= 14;
        l9 /= 14;
        textured = Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES) ? tex : true;
    }
}