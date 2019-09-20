package com.runescape.scene.map;

import com.runescape.Game;
import com.runescape.cache.def.object.ObjectDefinition;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.cache.texture.TextureLoader317;
import com.runescape.media.renderable.Model;
import com.runescape.media.renderable.Renderable;
import com.runescape.net.RSStream;
import com.runescape.net.requester.ResourceProvider;
import com.runescape.scene.SceneObject;
import com.runescape.scene.graphic.Rasterizer;
import com.runescape.scene.map.object.tile.Floor;
import com.runescape.scene.map.object.tile.FloorOverlay;
import com.runescape.util.ChunkUtil;

public final class MapRegion {
    public static final int BRIDGE_TILE = 2;
    private static final int anIntArray137[] = {1, 0, -1, 0};
    private static final int anIntArray140[] = {16, 32, 64, 128};
    private static final int anIntArray144[] = {0, -1, 0, 1};
    private static final int anIntArray152[] = {1, 2, 4, 8};
    private static final int BLOCKED_TILE = 1;
    private static final int FORCE_LOWEST_PLANE = 8;
    public static int anInt131;
    public static int highestPlane = 99;
    public static boolean lowMem = false;
    private static int hueOffset = (int) (Math.random() * 17D) - 8;
    private static int offsetLightning = (int) (Math.random() * 33D) - 16;
    private final int[] anIntArray124;
    private final int[] anIntArray125;
    private final int[] anIntArray126;
    private final int[] anIntArray127;
    private final int[] anIntArray128;
    private final int[][][] anIntArrayArrayArray129;
    private final byte[][][] aByteArrayArrayArray130;
    private final byte[][][] aByteArrayArrayArray134;
    private final int[][][] tileCullingBitmap;
    private final byte[][][] tileShape;
    private final int[][] anIntArrayArray139;
    private final byte[][][] aByteArrayArrayArray142;
    private final int anInt146;
    private final int anInt147;
    private final byte[][][] tileShapeRotation;
    private final byte[][][] tileFlags;
    public int underlayFloorTexture;
    public int underlayFloorMapColor;

    public MapRegion(byte flags[][][], int tileHeights[][][]) {
        highestPlane = 99;
        anInt146 = 104;
        anInt147 = 104;
        anIntArrayArrayArray129 = tileHeights;
        tileFlags = flags;
        aByteArrayArrayArray142 = new byte[4][anInt146][anInt147];
        aByteArrayArrayArray130 = new byte[4][anInt146][anInt147];
        tileShape = new byte[4][anInt146][anInt147];
        tileShapeRotation = new byte[4][anInt146][anInt147];
        tileCullingBitmap = new int[4][anInt146 + 1][anInt147 + 1];
        aByteArrayArrayArray134 = new byte[4][anInt146 + 1][anInt147 + 1];
        anIntArrayArray139 = new int[anInt146 + 1][anInt147 + 1];
        anIntArray124 = new int[anInt147];
        anIntArray125 = new int[anInt147];
        anIntArray126 = new int[anInt147];
        anIntArray127 = new int[anInt147];
        anIntArray128 = new int[anInt147];
    }

    private static int perlinNoise(int x, int y) {
        int k = x + y * 57;
        k = k << 13 ^ k;
        int l = k * (k * k * 15731 + 0xc0ae5) + 0x5208dd0d & 0x7fffffff;
        return l >> 19 & 0xff;
    }

    private static int method172(int i, int j) {
        int k = (interpolatedNoise(i + 45365, j + 0x16713, 4) - 128) + (interpolatedNoise(i + 10294, j + 37821, 2) - 128 >> 1) + (interpolatedNoise(i, j, 1) - 128 >> 2);
        k = (int) ((double) k * 0.29999999999999999D) + 35;
        if (k < 10) {
            k = 10;
        } else if (k > 60) {
            k = 60;
        }
        return k;
    }

    public static void configureLandscape(RSStream rsStream, ResourceProvider resourceProvider) {
        label0:
        {
            int objectId = -1;
            do {
                int j = rsStream.getBigSmart();
                if (j == 0) {
                    break label0;
                }
                objectId += j;
                ObjectDefinition objectDefinition = ObjectDefinition.forId(objectId);
                objectDefinition.loadModels(resourceProvider);
                do {
                    int k = rsStream.getSmartB();
                    if (k == 0) {
                        break;
                    }
                    rsStream.getByte();
                } while (true);
            } while (true);
        }
    }

    private static int interpolatedNoise(int x, int y, int frequencyReciprocal) {
        int l = x / frequencyReciprocal;
        int i1 = x & frequencyReciprocal - 1;
        int j1 = y / frequencyReciprocal;
        int k1 = y & frequencyReciprocal - 1;
        int l1 = smoothNoise(l, j1);
        int i2 = smoothNoise(l + 1, j1);
        int j2 = smoothNoise(l, j1 + 1);
        int k2 = smoothNoise(l + 1, j1 + 1);
        int l2 = interpolate(l1, i2, i1, frequencyReciprocal);
        int i3 = interpolate(j2, k2, i1, frequencyReciprocal);
        return interpolate(l2, i3, k1, frequencyReciprocal);
    }

    public static boolean modelReady(int i, int j) {
        ObjectDefinition class46 = ObjectDefinition.forId(i);
        if (j == 11) {
            j = 10;
        }
        if (j >= 5 && j <= 8) {
            j = 4;
        }
        return class46.isCached(j);
    }

    private static int interpolate(int a, int b, int angle, int frequencyReciprocal) {
        int cosine = 0x10000 - Rasterizer.COSINE[(angle * 1024) / frequencyReciprocal] >> 1;
        return (a * (0x10000 - cosine) >> 16) + (b * cosine >> 16);
    }

    private static int smoothNoise(int x, int y) {
        int corners = perlinNoise(x - 1, y - 1) + perlinNoise(x + 1, y - 1) + perlinNoise(x - 1, y + 1) + perlinNoise(x + 1, y + 1);
        int sides = perlinNoise(x - 1, y) + perlinNoise(x + 1, y) + perlinNoise(x, y - 1) + perlinNoise(x, y + 1);
        int center = perlinNoise(x, y);
        return corners / 16 + sides / 8 + center / 4;
    }

    private static int getShadow(int i, int j) {
        if (i == -1) {
            return 0xbc614e;
        }
        j = (j * (i & 0x7f)) / 128;
        if (j < 2) {
            j = 2;
        } else if (j > 126) {
            j = 126;
        }
        return (i & 0xff80) + j;
    }

    public static void placeObject(SceneGraph worldController, int i, int j, int k, int l, CollisionMap class11, int ai[][][], int i1, int objectId, int k1) {
        int l1 = ai[l][i1][j];
        int i2 = ai[l][i1 + 1][j];
        int j2 = ai[l][i1 + 1][j + 1];
        int k2 = ai[l][i1][j + 1];
        int l2 = l1 + i2 + j2 + k2 >> 2;
        ObjectDefinition objectDefinition = ObjectDefinition.forId(objectId);
        int i3 = i1 + (j << 7) + (objectId << 14) + 0x40000000;
        if (!objectDefinition.isInteractive) {
            i3 += 0x80000000;
        }
        byte byte1 = (byte) ((i << 6) + k);
        if (k == 22) {
            Object obj;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj = objectDefinition.modelAt(22, i, l1, i2, j2, k2, -1);
            } else {
                obj = new SceneObject(objectId, i, 22, i2, j2, l1, k2, objectDefinition.animation, true);
            }
            worldController.addGroundDecoration(k1, l2, j, ((Renderable) (obj)), byte1, i3, i1, objectId);
            if (objectDefinition.projectileClipped && objectDefinition.isInteractive) {
                class11.block(i1, j);
            }
            return;
        }
        if (k == 10 || k == 11) {
            Object obj1;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj1 = objectDefinition.modelAt(10, i, l1, i2, j2, k2, -1);
            } else {
                obj1 = new SceneObject(objectId, i, 10, i2, j2, l1, k2, objectDefinition.animation, true);
            }
            if (obj1 != null) {
                int j5 = 0;
                if (k == 11) {
                    j5 += 256;
                }
                int k4;
                int i5;
                if (i == 1 || i == 3) {
                    k4 = objectDefinition.sizeY;
                    i5 = objectDefinition.sizeX;
                } else {
                    k4 = objectDefinition.sizeX;
                    i5 = objectDefinition.sizeY;
                }
                worldController.addInteractableEntity(i3, byte1, l2, i5, ((Renderable) (obj1)), k4, k1, j5, j, i1, objectId);
            }
            if (objectDefinition.projectileClipped) {
                class11.method212(objectDefinition.impenetrable, objectDefinition.sizeX, objectDefinition.sizeY, i1, j, i);
            }
            return;
        }
        if (k >= 12) {
            Object obj2;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj2 = objectDefinition.modelAt(k, i, l1, i2, j2, k2, -1);
            } else {
                obj2 = new SceneObject(objectId, i, k, i2, j2, l1, k2, objectDefinition.animation, true);
            }
            worldController.addInteractableEntity(i3, byte1, l2, 1, ((Renderable) (obj2)), 1, k1, 0, j, i1, objectId);
            if (objectDefinition.projectileClipped) {
                class11.method212(objectDefinition.impenetrable, objectDefinition.sizeX, objectDefinition.sizeY, i1, j, i);
            }
            return;
        }
        if (k == 0) {
            Object obj3;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj3 = objectDefinition.modelAt(0, i, l1, i2, j2, k2, -1);
            } else {
                obj3 = new SceneObject(objectId, i, 0, i2, j2, l1, k2, objectDefinition.animation, true);
            }
            worldController.addWall(anIntArray152[i], ((Renderable) (obj3)), i3, j, byte1, i1, null, l2, 0, k1, objectId);
            if (objectDefinition.projectileClipped) {
                class11.method211(j, i, i1, k, objectDefinition.impenetrable);
            }
            return;
        }
        if (k == 1) {
            Object obj4;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj4 = objectDefinition.modelAt(1, i, l1, i2, j2, k2, -1);
            } else {
                obj4 = new SceneObject(objectId, i, 1, i2, j2, l1, k2, objectDefinition.animation, true);
            }
            worldController.addWall(anIntArray140[i], ((Renderable) (obj4)), i3, j, byte1, i1, null, l2, 0, k1, objectId);
            if (objectDefinition.projectileClipped) {
                class11.method211(j, i, i1, k, objectDefinition.impenetrable);
            }
            return;
        }
        if (k == 2) {
            int j3 = i + 1 & 3;
            Object obj11;
            Object obj12;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj11 = objectDefinition.modelAt(2, 4 + i, l1, i2, j2, k2, -1);
                obj12 = objectDefinition.modelAt(2, j3, l1, i2, j2, k2, -1);
            } else {
                obj11 = new SceneObject(objectId, 4 + i, 2, i2, j2, l1, k2, objectDefinition.animation, true);
                obj12 = new SceneObject(objectId, j3, 2, i2, j2, l1, k2, objectDefinition.animation, true);
            }
            worldController.addWall(anIntArray152[i], ((Renderable) (obj11)), i3, j, byte1, i1, ((Renderable) (obj12)), l2, anIntArray152[j3], k1, objectId);
            if (objectDefinition.projectileClipped) {
                class11.method211(j, i, i1, k, objectDefinition.impenetrable);
            }
            return;
        }
        if (k == 3) {
            Object obj5;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj5 = objectDefinition.modelAt(3, i, l1, i2, j2, k2, -1);
            } else {
                obj5 = new SceneObject(objectId, i, 3, i2, j2, l1, k2, objectDefinition.animation, true);
            }
            worldController.addWall(anIntArray140[i], ((Renderable) (obj5)), i3, j, byte1, i1, null, l2, 0, k1, objectId);
            if (objectDefinition.projectileClipped) {
                class11.method211(j, i, i1, k, objectDefinition.impenetrable);
            }
            return;
        }
        if (k == 9) {
            Object obj6;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj6 = objectDefinition.modelAt(k, i, l1, i2, j2, k2, -1);
            } else {
                obj6 = new SceneObject(objectId, i, k, i2, j2, l1, k2, objectDefinition.animation, true);
            }
            worldController.addInteractableEntity(i3, byte1, l2, 1, ((Renderable) (obj6)), 1, k1, 0, j, i1, objectId);
            if (objectDefinition.projectileClipped) {
                class11.method212(objectDefinition.impenetrable, objectDefinition.sizeX, objectDefinition.sizeY, i1, j, i);
            }
            return;
        }
        if (objectDefinition.contouredGround) {
            if (i == 1) {
                int k3 = k2;
                k2 = j2;
                j2 = i2;
                i2 = l1;
                l1 = k3;
            } else if (i == 2) {
                int l3 = k2;
                k2 = i2;
                i2 = l3;
                l3 = j2;
                j2 = l1;
                l1 = l3;
            } else if (i == 3) {
                int i4 = k2;
                k2 = l1;
                l1 = i2;
                i2 = j2;
                j2 = i4;
            }
        }
        if (k == 4) {
            Object obj7;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj7 = objectDefinition.modelAt(4, 0, l1, i2, j2, k2, -1);
            } else {
                obj7 = new SceneObject(objectId, 0, 4, i2, j2, l1, k2, objectDefinition.animation, true);
            }
            worldController.addWallDecoration(i3, j, i * 512, k1, 0, l2, ((Renderable) (obj7)), i1, byte1, 0, anIntArray152[i], objectId);
            return;
        }
        if (k == 5) {
            int j4 = 16;
            int l4 = worldController.getWallKey(k1, i1, j);
            if (l4 > 0) {
                j4 = ObjectDefinition.forId(l4 >> 14 & 0x7fff).decorDisplacement;
            }
            Object obj13;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj13 = objectDefinition.modelAt(4, 0, l1, i2, j2, k2, -1);
            } else {
                obj13 = new SceneObject(objectId, 0, 4, i2, j2, l1, k2, objectDefinition.animation, true);
            }
            worldController.addWallDecoration(i3, j, i * 512, k1, anIntArray137[i] * j4, l2, ((Renderable) (obj13)), i1, byte1, anIntArray144[i] * j4, anIntArray152[i], objectId);
            return;
        }
        if (k == 6) {
            Object obj8;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj8 = objectDefinition.modelAt(4, 0, l1, i2, j2, k2, -1);
            } else {
                obj8 = new SceneObject(objectId, 0, 4, i2, j2, l1, k2, objectDefinition.animation, true);
            }
            worldController.addWallDecoration(i3, j, i, k1, 0, l2, ((Renderable) (obj8)), i1, byte1, 0, 256, objectId);
            return;
        }
        if (k == 7) {
            Object obj9;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj9 = objectDefinition.modelAt(4, 0, l1, i2, j2, k2, -1);
            } else {
                obj9 = new SceneObject(objectId, 0, 4, i2, j2, l1, k2, objectDefinition.animation, true);
            }
            worldController.addWallDecoration(i3, j, i, k1, 0, l2, ((Renderable) (obj9)), i1, byte1, 0, 512, objectId);
            return;
        }
        if (k == 8) {
            Object obj10;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj10 = objectDefinition.modelAt(4, 0, l1, i2, j2, k2, -1);
            } else {
                obj10 = new SceneObject(objectId, 0, 4, i2, j2, l1, k2, objectDefinition.animation, true);
            }
            worldController.addWallDecoration(i3, j, i, k1, 0, l2, ((Renderable) (obj10)), i1, byte1, 0, 768, objectId);
        }
    }

    public static boolean method189(int i, byte[] is, int i_250_) {
        boolean cachedModel = true;
        RSStream rsStream = new RSStream(is);
        int objectId = -1;
        for (; ; ) {
            int i_253_ = rsStream.getSmartB();
            if (i_253_ == 0) {
                break;
            }
            objectId += i_253_;
            int i_254_ = 0;
            boolean bool_255_ = false;
            for (; ; ) {
                if (bool_255_) {
                    int i_256_ = rsStream.getSmartB();
                    if (i_256_ == 0) {
                        break;
                    }
                    rsStream.getByte();
                } else {
                    int i_257_ = rsStream.getSmartB();
                    if (i_257_ == 0) {
                        break;
                    }
                    i_254_ += i_257_ - 1;
                    int i_258_ = i_254_ & 0x3f;
                    int i_259_ = i_254_ >> 6 & 0x3f;
                    int objectType = rsStream.getByte() >> 2;
                    int i_261_ = i_259_ + i;
                    int i_262_ = i_258_ + i_250_;
                    if (i_261_ > 0 && i_262_ > 0 && i_261_ < 103 && i_262_ < 103) {
                        ObjectDefinition objectDefinition = ObjectDefinition.forId(objectId);
                        if (objectType != 22 || !lowMem || objectDefinition.isInteractive || objectDefinition.obstructsGround) {
                            cachedModel &= objectDefinition.isModelCached();
                            bool_255_ = true;
                        }
                    }
                }
            }
        }
        return cachedModel;
    }

    public final void addTile(CollisionMap[] collisionMaps, SceneGraph sceneGraph) {
        try {
            for (int z = 0; z < 4; z++) {
                for (int x = 0; x < 104; x++) {
                    for (int y = 0; y < 104; y++) {
                        if ((tileFlags[z][x][y] & BLOCKED_TILE) == 1) {
                            int plane = z;
                            if ((tileFlags[1][x][y] & BRIDGE_TILE) == 2) {
                                plane--;
                            }
                            if (plane >= 0) {
                                collisionMaps[plane].block(x, y);
                            }
                        }
                    }
                }
            }
            hueOffset += (int) (Math.random() * 5D) - 2;
            if (hueOffset < -8) {
                hueOffset = -8;
            }
            if (hueOffset > 8) {
                hueOffset = 8;
            }
            offsetLightning += (int) (Math.random() * 5D) - 2;
            if (offsetLightning < -16) {
                offsetLightning = -16;
            }
            if (offsetLightning > 16) {
                offsetLightning = 16;
            }
            for (int z = 0; z < 4; z++) {
                byte abyte0[][] = aByteArrayArrayArray134[z];
                byte byte0 = 96;
                char c = '\u0300';
                byte byte1 = -50;
                byte byte2 = -10;
                byte byte3 = -50;
                int j3 = (int) Math.sqrt(byte1 * byte1 + byte2 * byte2 + byte3 * byte3);
                int l3 = c * j3 >> 8;
                for (int j4 = 1; j4 < anInt147 - 1; j4++) {
                    for (int j5 = 1; j5 < anInt146 - 1; j5++) {
                        int k6 = anIntArrayArrayArray129[z][j5 + 1][j4] - anIntArrayArrayArray129[z][j5 - 1][j4];
                        int l7 = anIntArrayArrayArray129[z][j5][j4 + 1] - anIntArrayArrayArray129[z][j5][j4 - 1];
                        int j9 = (int) Math.sqrt(k6 * k6 + 0x10000 + l7 * l7);
                        int k12 = (k6 << 8) / j9;
                        int l13 = 0x10000 / j9;
                        int j15 = (l7 << 8) / j9;
                        int j16 = byte0 + (byte1 * k12 + byte2 * l13 + byte3 * j15) / l3;
                        int j17 = (abyte0[j5 - 1][j4] >> 2) + (abyte0[j5 + 1][j4] >> 3) + (abyte0[j5][j4 - 1] >> 2) + (abyte0[j5][j4 + 1] >> 3) + (abyte0[j5][j4] >> 1);
                        anIntArrayArray139[j5][j4] = j16 - j17;
                    }
                }

                for (int k5 = 0; k5 < anInt147; k5++) {
                    anIntArray124[k5] = 0;
                    anIntArray125[k5] = 0;
                    anIntArray126[k5] = 0;
                    anIntArray127[k5] = 0;
                    anIntArray128[k5] = 0;
                }

                for (int x = -5; x < anInt146 + 5; x++) {
                    for (int tileY = 0; tileY < anInt147; tileY++) {
                        int k9 = x + 5;
                        if (k9 >= 0 && k9 < anInt146) {
                            int l12 = aByteArrayArrayArray142[z][k9][tileY] & 0xff;
                            if (l12 > 0) {
                                Floor floor = Floor.cache[l12 - 1];
                                anIntArray124[tileY] += floor.getAnInt397();
                                anIntArray125[tileY] += floor.getSaturation();
                                anIntArray126[tileY] += floor.getLightness();
                                anIntArray127[tileY] += floor.getAnInt398();
                                anIntArray128[tileY]++;
                            }
                        }
                        int i13 = x - 5;
                        if (i13 >= 0 && i13 < anInt146) {
                            int i14 = aByteArrayArrayArray142[z][i13][tileY] & 0xff;
                            if (i14 > 0) {
                                Floor floor = Floor.cache[i14 - 1];
                                anIntArray124[tileY] -= floor.getAnInt397();
                                anIntArray125[tileY] -= floor.getSaturation();
                                anIntArray126[tileY] -= floor.getLightness();
                                anIntArray127[tileY] -= floor.getAnInt398();
                                anIntArray128[tileY]--;
                            }
                        }
                    }

                    if (x >= 1 && x < anInt146 - 1) {
                        int blended_hue = 0;
                        int blended_saturation = 0;
                        int blended_lightness = 0;
                        int blended_hue_divisor = 0;
                        int blend_direction_tracker = 0;
                        for (int y = -5; y < anInt147 + 5; y++) {
                            int j18 = y + 5;
                            if (j18 >= 0 && j18 < anInt147) {
                                blended_hue += anIntArray124[j18];
                                blended_saturation += anIntArray125[j18];
                                blended_lightness += anIntArray126[j18];
                                blended_hue_divisor += anIntArray127[j18];
                                blend_direction_tracker += anIntArray128[j18];
                            }
                            int k18 = y - 5;
                            if (k18 >= 0 && k18 < anInt147) {
                                blended_hue -= anIntArray124[k18];
                                blended_saturation -= anIntArray125[k18];
                                blended_lightness -= anIntArray126[k18];
                                blended_hue_divisor -= anIntArray127[k18];
                                blend_direction_tracker -= anIntArray128[k18];
                            }
                            if (y >= 1 && y < anInt147 - 1 && (!lowMem || (tileFlags[0][x][y] & 2) != 0 || (tileFlags[z][x][y] & 0x10) == 0 && getCollisionPlane(y, z, x) == anInt131)) {
                                if (z < highestPlane) {
                                    highestPlane = z;
                                }
                                int underlay_floor_id = aByteArrayArrayArray142[z][x][y] & 0xff;
                                int overlay_floor_id = aByteArrayArrayArray130[z][x][y] & 0xff;

                                if (underlay_floor_id > 0 || overlay_floor_id > 0) {
                                    int tileZA = anIntArrayArrayArray129[z][x][y];
                                    int tileZB = anIntArrayArrayArray129[z][x + 1][y];
                                    int tileZD = anIntArrayArrayArray129[z][x + 1][y + 1];
                                    int tileZC = anIntArrayArrayArray129[z][x][y + 1];
                                    int tileShadowA = anIntArrayArray139[x][y];
                                    int tileShadowB = anIntArrayArray139[x + 1][y];
                                    int tileShadowD = anIntArrayArray139[x + 1][y + 1];
                                    int tileShadowC = anIntArrayArray139[x][y + 1];
                                    int hslBitsetUnmodified = -1;
                                    int hsl_bitset_randomized = -1;
                                    if (underlay_floor_id > 0 || overlay_floor_id != 0) {
                                        int hue = -1;
                                        int sat = 0;
                                        int lum = 0;
                                        if (underlay_floor_id == 0) {
                                            hue = -1;
                                            sat = 0;
                                            lum = 0;
                                        } else if (underlay_floor_id > 0) {
                                            if (blended_hue_divisor < 1) {
                                                blended_hue_divisor = 1;
                                            }

                                            hue = (blended_hue << 8) / blended_hue_divisor;
                                            sat = blended_saturation / blend_direction_tracker;
                                            lum = blended_lightness / blend_direction_tracker;
                                            hslBitsetUnmodified = encode(hue, sat, lum);
                                            hue = hue + hueOffset & 0xff;
                                            lum += offsetLightning;
                                            if (lum < 0) {
                                                lum = 0;
                                            } else if (lum > 255) {
                                                lum = 255;
                                            }

                                        } else {
                                            hue = underlay_floor_id;
                                            sat = 0;
                                            lum = 0;
                                        }
                                        if (hue != -1 && hsl_bitset_randomized == -1) {
                                            hsl_bitset_randomized = encode(hue, sat, lum);
                                        }

                                        if (hslBitsetUnmodified == -1) {
                                            hslBitsetUnmodified = hsl_bitset_randomized;
                                        }
                                    }
                                    if (z > 0) {
                                        boolean hide_underlay = true;
                                        if (underlay_floor_id == 0 && tileShape[z][x][y] != 0) {
                                            hide_underlay = false;
                                        }
                                        if (overlay_floor_id >= FloorOverlay.cache.length) {
                                            overlay_floor_id = FloorOverlay.cache.length - 1;
                                        }
                                        if (overlay_floor_id > 0
                                                && !FloorOverlay.cache[overlay_floor_id - 1].occludeOverlay) {
                                            hide_underlay = false;
                                        }
                                        if (hide_underlay && tileZA == tileZB && tileZA == tileZD && tileZA == tileZC) {
                                            tileCullingBitmap[z][x][y] |= 0x924;
                                        }
                                    }
                                    int rgbBitsetRandomized = 0;
                                    if (hslBitsetUnmodified != -1) {
                                        rgbBitsetRandomized = Rasterizer.hsl2rgb[getShadow(hsl_bitset_randomized, 96)];
                                    }
                                    if (overlay_floor_id == 0) {
                                        if (Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
                                            if (underlay_floor_id - 1 >= Floor.cache.length) {
                                                underlay_floor_id = Floor.cache.length - 1;
                                            }
                                            Floor floor = Floor.cache[underlay_floor_id - 1];
                                            int underlay_texture_id = floor.getTextureId();
                                            if (underlay_texture_id != -1) {
                                                underlay_texture_id = 154;
                                            }
                                            underlayFloorTexture = underlay_texture_id;
                                            underlayFloorMapColor = getOverlayShadow(hslBitsetUnmodified, 96);
                                            int tile_opcode = tileShape[z][x][y] + 1;
                                            if (tile_opcode == 1) {
                                                tile_opcode = 434;
                                            }
                                            byte tile_orientation = tileShapeRotation[z][x][y];
                                            /**
                                             * Adds underlay tile
                                             */
                                            int overlay_hsl = encode(floor.getHue(), floor.getSaturation(), floor.getLightness());
                                            sceneGraph.addTile(z, x, y,
                                                    tile_opcode, tile_orientation, underlay_texture_id, tileZA, tileZB, tileZD, tileZC,
                                                    getShadow(hslBitsetUnmodified, tileShadowA),
                                                    getShadow(hslBitsetUnmodified, tileShadowB),
                                                    getShadow(hslBitsetUnmodified, tileShadowD),
                                                    getShadow(hslBitsetUnmodified, tileShadowC),
                                                    getOverlayShadow(overlay_hsl, tileShadowA),
                                                    getOverlayShadow(overlay_hsl, tileShadowB),
                                                    getOverlayShadow(overlay_hsl, tileShadowD),
                                                    getOverlayShadow(overlay_hsl, tileShadowC),
                                                    rgbBitsetRandomized, rgbBitsetRandomized, underlayFloorMapColor,
                                                    underlayFloorTexture, underlayFloorMapColor, false);
                                        } else {
                                            sceneGraph.addTile(z, x, y, 0, 0, -1, tileZA, tileZB, tileZD, tileZC,
                                                    getShadow(hslBitsetUnmodified, tileShadowA),
                                                    getShadow(hslBitsetUnmodified, tileShadowB),
                                                    getShadow(hslBitsetUnmodified, tileShadowD),
                                                    getShadow(hslBitsetUnmodified, tileShadowC),
                                                    0,
                                                    0,
                                                    0,
                                                    0,
                                                    rgbBitsetRandomized, rgbBitsetRandomized, -1, 0, 0, true);
                                        }
                                    } else {
                                        int overlay_map_color = 0;
                                        int overlay_floor_texture_color = -1;
                                        int tile_opcode = tileShape[z][x][y] + 1;
                                        byte tile_orientation = tileShapeRotation[z][x][y];
                                        if (overlay_floor_id - 1 >= FloorOverlay.cache.length) {
                                            overlay_floor_id = FloorOverlay.cache.length - 1;
                                        }
                                        FloorOverlay floorOverlay = FloorOverlay.cache[overlay_floor_id - 1];
                                        int overlay_texture_id = floorOverlay.groundTextureOverlay;
                                        int overlay_hsl;
                                        int overlay_rgb = 0;
                                        if (overlay_texture_id > 50 && !Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
                                            overlay_texture_id = -1;
                                        }

                                        if (floorOverlay.groundColorOverlay == 0x333333) {
                                            overlay_rgb = 0;
                                            overlay_hsl = -1;
                                            overlay_texture_id = lowMem ? 31 : 40;
                                        } else if (overlay_texture_id >= 0) {
                                            if (overlay_texture_id < 51 || !Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
                                                overlay_rgb = TextureLoader317.getAverageTextureColour(overlay_texture_id);
                                            }
                                            overlay_hsl = -1;
                                        } else if (floorOverlay.groundColorOverlay == 0xff00ff) {
                                            overlay_rgb = 0;
                                            overlay_hsl = -2;
                                            overlay_texture_id = -1;
                                        } else {
                                            overlay_hsl = encode(floorOverlay.groundHueOverlay, floorOverlay.groundSaturationOverlay, floorOverlay.groundLightnessOverlay);
                                            overlay_rgb = Rasterizer.hsl2rgb[getOverlayShadow(floorOverlay.hslOverlayColor, 96)];
                                        }
//so sad man wtf is this happening 4....
                                        if ((overlay_floor_id - 1) == 54) {
                                            overlay_rgb = floorOverlay.groundColorOverlay = 0x8B8B83;
                                            overlay_hsl = -2;
                                        }
                                        if ((overlay_floor_id - 1) == 111) {
                                            overlay_rgb = TextureLoader317.getAverageTextureColour(1);
                                            overlay_hsl = -1;
                                            overlay_texture_id = 1;
                                        } else if (overlay_hsl == 6363) {
                                            overlay_rgb = 0x483B21;
                                            overlay_hsl = encode(25, 146, 24);
                                        } else if ((overlay_floor_id - 1) == 54) {
                                            overlay_rgb = floorOverlay.groundColorOverlay;
                                            overlay_hsl = -2;
                                            overlay_texture_id = -1;
                                        }
                                        if (Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
                                            if (floorOverlay.detailedColor != -1) {
                                                overlay_map_color = (Rasterizer.hsl2rgb[floorOverlay.detailedColor] != 1) ? Rasterizer.hsl2rgb[floorOverlay.detailedColor] : 0;
                                            }
                                            if ((!Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES) ? (overlay_texture_id >= 0 && overlay_texture_id < 51) : (overlay_texture_id >= 0))) {
                                                overlay_hsl = -1;
                                                if (floorOverlay.groundColorOverlay != 0xff00ff) {
                                                    overlay_hsl = floorOverlay.groundColorOverlay;
                                                    if (overlay_texture_id > 50) {
                                                        overlay_rgb = (overlay_hsl != -1 ? Rasterizer.hsl2rgb[overlay_hsl] : 0);
                                                    }
                                                    overlay_floor_texture_color = getOverlayShadow(floorOverlay.groundColorOverlay, 96);
                                                } else {
                                                    if (overlay_texture_id > 50) {
                                                        overlay_rgb = floorOverlay.detailedColor;
                                                    }
                                                    overlay_hsl = -2;
                                                    underlayFloorMapColor = -1;
                                                    overlay_floor_texture_color = -1;
                                                }
                                            } else if (floorOverlay.groundColorOverlay == -1) {
                                                if (overlay_texture_id > 50) {
                                                    overlay_rgb = overlay_map_color;
                                                }
                                                overlay_hsl = -2;
                                                if (z > 0) {
                                                    underlayFloorTexture = -1;
                                                }

                                                overlay_texture_id = -1;
                                            } else {
                                                overlay_floor_texture_color = getOverlayShadow(floorOverlay.groundColorOverlay, 96);
                                                overlay_hsl = floorOverlay.groundColorOverlay;
                                                if (overlay_texture_id > 50) {
                                                    overlay_rgb = Rasterizer.hsl2rgb[overlay_floor_texture_color];
                                                }
                                            }
                                        }
                                        if (Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
                                            sceneGraph.addTile(z, x, y,
                                                    tile_opcode, tile_orientation, overlay_texture_id, tileZA, tileZB, tileZD, tileZC,
                                                    getShadow(hslBitsetUnmodified, tileShadowA), getShadow(hslBitsetUnmodified, tileShadowB),
                                                    getShadow(hslBitsetUnmodified, tileShadowD), getShadow(hslBitsetUnmodified, tileShadowC),
                                                    getOverlayShadow(overlay_hsl, tileShadowA), getOverlayShadow(overlay_hsl, tileShadowB),
                                                    getOverlayShadow(overlay_hsl, tileShadowD), getOverlayShadow(overlay_hsl, tileShadowC),
                                                    rgbBitsetRandomized, overlay_rgb, overlay_floor_texture_color,
                                                    underlayFloorTexture, underlayFloorMapColor, false);
                                        } else {
                                            sceneGraph.addTile(z, x, y, tile_opcode,
                                                    tile_orientation, overlay_texture_id, tileZA, tileZB, tileZD, tileZC,
                                                    getShadow(hslBitsetUnmodified, tileShadowA), getShadow(hslBitsetUnmodified,
                                                            tileShadowB), getShadow(hslBitsetUnmodified, tileShadowD),
                                                    getShadow(hslBitsetUnmodified, tileShadowC), getOverlayShadow(overlay_hsl,
                                                            tileShadowA), getOverlayShadow(overlay_hsl, tileShadowB),
                                                    getOverlayShadow(overlay_hsl, tileShadowD), getOverlayShadow(overlay_hsl,
                                                            tileShadowC), rgbBitsetRandomized, overlay_rgb, -1, 0, 0, true);
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

                for (int j8 = 1; j8 < anInt147 - 1; j8++) {
                    for (int i10 = 1; i10 < anInt146 - 1; i10++) {
                        sceneGraph.setVisiblePlanesFor(z, i10, j8, getCollisionPlane(j8, z, i10));
                    }
                }
            }

            sceneGraph.shadeModels(-10, -50, -50);
            for (int j1 = 0; j1 < anInt146; j1++) {
                for (int l1 = 0; l1 < anInt147; l1++) {
                    if ((tileFlags[1][j1][l1] & 2) == 2) {
                        sceneGraph.applyBridgeMode(l1, j1);
                    }
                }
            }

            int i2 = 1;
            int j2 = 2;
            int k2 = 4;
            for (int l2 = 0; l2 < 4; l2++) {
                if (l2 > 0) {
                    i2 <<= 3;
                    j2 <<= 3;
                    k2 <<= 3;
                }
                for (int i3 = 0; i3 <= l2; i3++) {
                    for (int k3 = 0; k3 <= anInt147; k3++) {
                        for (int i4 = 0; i4 <= anInt146; i4++) {
                            if ((tileCullingBitmap[i3][i4][k3] & i2) != 0) {
                                int k4 = k3;
                                int l5 = k3;
                                int i7 = i3;
                                int k8 = i3;
                                for (; k4 > 0 && (tileCullingBitmap[i3][i4][k4 - 1] & i2) != 0; k4--) {
                                    ;
                                }
                                for (; l5 < anInt147 && (tileCullingBitmap[i3][i4][l5 + 1] & i2) != 0; l5++) {
                                    ;
                                }
                                label0:
                                for (; i7 > 0; i7--) {
                                    for (int j10 = k4; j10 <= l5; j10++) {
                                        if ((tileCullingBitmap[i7 - 1][i4][j10] & i2) == 0) {
                                            break label0;
                                        }
                                    }

                                }

                                label1:
                                for (; k8 < l2; k8++) {
                                    for (int k10 = k4; k10 <= l5; k10++) {
                                        if ((tileCullingBitmap[k8 + 1][i4][k10] & i2) == 0) {
                                            break label1;
                                        }
                                    }

                                }

                                int l10 = ((k8 + 1) - i7) * ((l5 - k4) + 1);
                                if (l10 >= 8) {
                                    char c1 = '\360';
                                    int k14 = anIntArrayArrayArray129[k8][i4][k4] - c1;
                                    int l15 = anIntArrayArrayArray129[i7][i4][k4];
                                    SceneGraph.createCullingCluster(l2, i4 * 128, l15, i4 * 128, l5 * 128 + 128, k14, k4 * 128, 1);
                                    for (int l16 = i7; l16 <= k8; l16++) {
                                        for (int l17 = k4; l17 <= l5; l17++) {
                                            tileCullingBitmap[l16][i4][l17] &= ~i2;
                                        }

                                    }

                                }
                            }
                            if ((tileCullingBitmap[i3][i4][k3] & j2) != 0) {
                                int l4 = i4;
                                int i6 = i4;
                                int j7 = i3;
                                int l8 = i3;
                                for (; l4 > 0 && (tileCullingBitmap[i3][l4 - 1][k3] & j2) != 0; l4--) {
                                    ;
                                }
                                for (; i6 < anInt146 && (tileCullingBitmap[i3][i6 + 1][k3] & j2) != 0; i6++) {
                                    ;
                                }
                                label2:
                                for (; j7 > 0; j7--) {
                                    for (int i11 = l4; i11 <= i6; i11++) {
                                        if ((tileCullingBitmap[j7 - 1][i11][k3] & j2) == 0) {
                                            break label2;
                                        }
                                    }

                                }

                                label3:
                                for (; l8 < l2; l8++) {
                                    for (int j11 = l4; j11 <= i6; j11++) {
                                        if ((tileCullingBitmap[l8 + 1][j11][k3] & j2) == 0) {
                                            break label3;
                                        }
                                    }

                                }

                                int k11 = ((l8 + 1) - j7) * ((i6 - l4) + 1);
                                if (k11 >= 8) {
                                    char c2 = '\360';
                                    int l14 = anIntArrayArrayArray129[l8][l4][k3] - c2;
                                    int i16 = anIntArrayArrayArray129[j7][l4][k3];
                                    SceneGraph.createCullingCluster(l2, l4 * 128, i16, i6 * 128 + 128, k3 * 128, l14, k3 * 128, 2);
                                    for (int i17 = j7; i17 <= l8; i17++) {
                                        for (int i18 = l4; i18 <= i6; i18++) {
                                            tileCullingBitmap[i17][i18][k3] &= ~j2;
                                        }

                                    }

                                }
                            }
                            if ((tileCullingBitmap[i3][i4][k3] & k2) != 0) {
                                int i5 = i4;
                                int j6 = i4;
                                int k7 = k3;
                                int i9 = k3;
                                for (; k7 > 0 && (tileCullingBitmap[i3][i4][k7 - 1] & k2) != 0; k7--) {
                                    ;
                                }
                                for (; i9 < anInt147 && (tileCullingBitmap[i3][i4][i9 + 1] & k2) != 0; i9++) {
                                    ;
                                }
                                label4:
                                for (; i5 > 0; i5--) {
                                    for (int l11 = k7; l11 <= i9; l11++) {
                                        if ((tileCullingBitmap[i3][i5 - 1][l11] & k2) == 0) {
                                            break label4;
                                        }
                                    }

                                }

                                label5:
                                for (; j6 < anInt146; j6++) {
                                    for (int i12 = k7; i12 <= i9; i12++) {
                                        if ((tileCullingBitmap[i3][j6 + 1][i12] & k2) == 0) {
                                            break label5;
                                        }
                                    }

                                }

                                if (((j6 - i5) + 1) * ((i9 - k7) + 1) >= 4) {
                                    int j12 = anIntArrayArrayArray129[i3][i5][k7];
                                    SceneGraph.createCullingCluster(l2, i5 * 128, j12, j6 * 128 + 128, i9 * 128 + 128, j12, k7 * 128, 4);
                                    for (int k13 = i5; k13 <= j6; k13++) {
                                        for (int i15 = k7; i15 <= i9; i15++) {
                                            tileCullingBitmap[i3][k13][i15] &= ~k2;
                                        }

                                    }

                                }
                            }
                        }

                    }

                }

            }
        } catch (Exception e) {
        }
    }

    public final void method174(int i, int j, int l, int i1) {
        for (int j1 = i; j1 <= i + j; j1++) {
            for (int k1 = i1; k1 <= i1 + l; k1++) {
                if (k1 >= 0 && k1 < anInt146 && j1 >= 0 && j1 < anInt147) {
                    aByteArrayArrayArray134[0][k1][j1] = 127;
                    if (k1 == i1 && k1 > 0) {
                        anIntArrayArrayArray129[0][k1][j1] = anIntArrayArrayArray129[0][k1 - 1][j1];
                    }
                    if (k1 == i1 + l && k1 < anInt146 - 1) {
                        anIntArrayArrayArray129[0][k1][j1] = anIntArrayArrayArray129[0][k1 + 1][j1];
                    }
                    if (j1 == i && j1 > 0) {
                        anIntArrayArrayArray129[0][k1][j1] = anIntArrayArrayArray129[0][k1][j1 - 1];
                    }
                    if (j1 == i + j && j1 < anInt147 - 1) {
                        anIntArrayArrayArray129[0][k1][j1] = anIntArrayArrayArray129[0][k1][j1 + 1];
                    }
                }
            }

        }
    }

    private void method175(int y, SceneGraph sceneGraph, CollisionMap collisionMap, int type, int z, int x, int id, int j1) {
        if (lowMem && (tileFlags[0][x][y] & BRIDGE_TILE) == 0) {
            if ((tileFlags[z][x][y] & 0x10) != 0) {
                return;
            }
            if (getCollisionPlane(y, z, x) != anInt131) {
                return;
            }
        }
        if (z < highestPlane) {
            highestPlane = z;
        }
        int center = anIntArrayArrayArray129[z][x][y];
        int east = anIntArrayArrayArray129[z][x + 1][y];
        int northEast = anIntArrayArrayArray129[z][x + 1][y + 1];
        int north = anIntArrayArrayArray129[z][x][y + 1];
        int mean = center + east + northEast + north >> 2;
        ObjectDefinition objectDefinition = ObjectDefinition.forId(id);
        int key = x + (y << 7) + (id << 14) + 0x40000000;
        if (!objectDefinition.isInteractive) {
            key += 0x80000000;
        }
        byte config = (byte) ((j1 << 6) + type);
        if (type == 22) {
            if (lowMem && !objectDefinition.isInteractive && !objectDefinition.obstructsGround) {
                return;
            }
            Object obj;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj = objectDefinition.modelAt(22, j1, center, east, northEast, north, -1);
            } else {
                obj = new SceneObject(id, j1, 22, east, northEast, center, north, objectDefinition.animation, true);
            }
            sceneGraph.addGroundDecoration(z, mean, y, ((Renderable) (obj)), config, key, x, id);
            if (objectDefinition.projectileClipped && objectDefinition.isInteractive && collisionMap != null) {
                collisionMap.block(x, y);
            }
            return;
        }
        if (type == 10 || type == 11) {
            Object obj1;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj1 = objectDefinition.modelAt(10, j1, center, east, northEast, north, -1);
            } else {
                obj1 = new SceneObject(id, j1, 10, east, northEast, center, north, objectDefinition.animation, true);
            }
            if (obj1 != null) {
                int i5 = 0;
                if (type == 11) {
                    i5 += 256;
                }
                int j4;
                int l4;
                if (j1 == 1 || j1 == 3) {
                    j4 = objectDefinition.sizeY;
                    l4 = objectDefinition.sizeX;
                } else {
                    j4 = objectDefinition.sizeX;
                    l4 = objectDefinition.sizeY;
                }
                if (sceneGraph.addInteractableEntity(key, config, mean, l4, ((Renderable) (obj1)), j4, z, i5, y, x, id) && objectDefinition.castsShadow) {
                    Model model;
                    if (obj1 instanceof Model) {
                        model = (Model) obj1;
                    } else {
                        model = objectDefinition.modelAt(10, j1, center, east, northEast, north, -1);
                    }
                    if (model != null) {
                        for (int j5 = 0; j5 <= j4; j5++) {
                            for (int k5 = 0; k5 <= l4; k5++) {
                                int l5 = model.anInt1650 / 4;
                                if (l5 > 30) {
                                    l5 = 30;
                                }
                                if (l5 > aByteArrayArrayArray134[z][x + j5][y + k5]) {
                                    aByteArrayArrayArray134[z][x + j5][y + k5] = (byte) l5;
                                }
                            }

                        }

                    }
                }
            }
            if (objectDefinition.projectileClipped && collisionMap != null) {
                collisionMap.method212(objectDefinition.impenetrable, objectDefinition.sizeX, objectDefinition.sizeY, x, y, j1);
            }
            return;
        }
        if (type >= 12) {
            Object obj2;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj2 = objectDefinition.modelAt(type, j1, center, east, northEast, north, -1);
            } else {
                obj2 = new SceneObject(id, j1, type, east, northEast, center, north, objectDefinition.animation, true);
            }
            sceneGraph.addInteractableEntity(key, config, mean, 1, ((Renderable) (obj2)), 1, z, 0, y, x, id);
            if (type >= 12 && type <= 17 && type != 13 && z > 0) {
                tileCullingBitmap[z][x][y] |= 0x924;
            }
            if (objectDefinition.projectileClipped && collisionMap != null) {
                collisionMap.method212(objectDefinition.impenetrable, objectDefinition.sizeX, objectDefinition.sizeY, x, y, j1);
            }
            return;
        }
        if (type == 0) {
            Object obj3;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj3 = objectDefinition.modelAt(0, j1, center, east, northEast, north, -1);
            } else {
                obj3 = new SceneObject(id, j1, 0, east, northEast, center, north, objectDefinition.animation, true);
            }
            sceneGraph.addWall(anIntArray152[j1], ((Renderable) (obj3)), key, y, config, x, null, mean, 0, z, id);
            if (j1 == 0) {
                if (objectDefinition.castsShadow) {
                    aByteArrayArrayArray134[z][x][y] = 50;
                    aByteArrayArrayArray134[z][x][y + 1] = 50;
                }
                if (objectDefinition.occludes) {
                    tileCullingBitmap[z][x][y] |= 0x249;
                }
            } else if (j1 == 1) {
                if (objectDefinition.castsShadow) {
                    aByteArrayArrayArray134[z][x][y + 1] = 50;
                    aByteArrayArrayArray134[z][x + 1][y + 1] = 50;
                }
                if (objectDefinition.occludes) {
                    tileCullingBitmap[z][x][y + 1] |= 0x492;
                }
            } else if (j1 == 2) {
                if (objectDefinition.castsShadow) {
                    aByteArrayArrayArray134[z][x + 1][y] = 50;
                    aByteArrayArrayArray134[z][x + 1][y + 1] = 50;
                }
                if (objectDefinition.occludes) {
                    tileCullingBitmap[z][x + 1][y] |= 0x249;
                }
            } else if (j1 == 3) {
                if (objectDefinition.castsShadow) {
                    aByteArrayArrayArray134[z][x][y] = 50;
                    aByteArrayArrayArray134[z][x + 1][y] = 50;
                }
                if (objectDefinition.occludes) {
                    tileCullingBitmap[z][x][y] |= 0x492;
                }
            }
            if (objectDefinition.projectileClipped && collisionMap != null) {
                collisionMap.method211(y, j1, x, type, objectDefinition.impenetrable);
            }
            if (objectDefinition.decorDisplacement != 16) {
                sceneGraph.moveWallDecoration(y, objectDefinition.decorDisplacement, x, z);
            }
            return;
        }
        if (type == 1) {
            Object obj4;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj4 = objectDefinition.modelAt(1, j1, center, east, northEast, north, -1);
            } else {
                obj4 = new SceneObject(id, j1, 1, east, northEast, center, north, objectDefinition.animation, true);
            }
            sceneGraph.addWall(anIntArray140[j1], ((Renderable) (obj4)), key, y, config, x, null, mean, 0, z, id);
            if (objectDefinition.castsShadow) {
                if (j1 == 0) {
                    aByteArrayArrayArray134[z][x][y + 1] = 50;
                } else if (j1 == 1) {
                    aByteArrayArrayArray134[z][x + 1][y + 1] = 50;
                } else if (j1 == 2) {
                    aByteArrayArrayArray134[z][x + 1][y] = 50;
                } else if (j1 == 3) {
                    aByteArrayArrayArray134[z][x][y] = 50;
                }
            }
            if (objectDefinition.projectileClipped && collisionMap != null) {
                collisionMap.method211(y, j1, x, type, objectDefinition.impenetrable);
            }
            return;
        }
        if (type == 2) {
            int i3 = j1 + 1 & 3;
            Object obj11;
            Object obj12;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj11 = objectDefinition.modelAt(2, 4 + j1, center, east, northEast, north, -1);
                obj12 = objectDefinition.modelAt(2, i3, center, east, northEast, north, -1);
            } else {
                obj11 = new SceneObject(id, 4 + j1, 2, east, northEast, center, north, objectDefinition.animation, true);
                obj12 = new SceneObject(id, i3, 2, east, northEast, center, north, objectDefinition.animation, true);
            }
            sceneGraph.addWall(anIntArray152[j1], ((Renderable) (obj11)), key, y, config, x, ((Renderable) (obj12)), mean, anIntArray152[i3], z, id);
            if (objectDefinition.occludes) {
                if (j1 == 0) {
                    tileCullingBitmap[z][x][y] |= 0x249;
                    tileCullingBitmap[z][x][y + 1] |= 0x492;
                } else if (j1 == 1) {
                    tileCullingBitmap[z][x][y + 1] |= 0x492;
                    tileCullingBitmap[z][x + 1][y] |= 0x249;
                } else if (j1 == 2) {
                    tileCullingBitmap[z][x + 1][y] |= 0x249;
                    tileCullingBitmap[z][x][y] |= 0x492;
                } else if (j1 == 3) {
                    tileCullingBitmap[z][x][y] |= 0x492;
                    tileCullingBitmap[z][x][y] |= 0x249;
                }
            }
            if (objectDefinition.projectileClipped && collisionMap != null) {
                collisionMap.method211(y, j1, x, type, objectDefinition.impenetrable);
            }
            if (objectDefinition.decorDisplacement != 16) {
                sceneGraph.moveWallDecoration(y, objectDefinition.decorDisplacement, x, z);
            }
            return;
        }
        if (type == 3) {
            Object obj5;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj5 = objectDefinition.modelAt(3, j1, center, east, northEast, north, -1);
            } else {
                obj5 = new SceneObject(id, j1, 3, east, northEast, center, north, objectDefinition.animation, true);
            }
            sceneGraph.addWall(anIntArray140[j1], ((Renderable) (obj5)), key, y, config, x, null, mean, 0, z, id);
            if (objectDefinition.castsShadow) {
                if (j1 == 0) {
                    aByteArrayArrayArray134[z][x][y + 1] = 50;
                } else if (j1 == 1) {
                    aByteArrayArrayArray134[z][x + 1][y + 1] = 50;
                } else if (j1 == 2) {
                    aByteArrayArrayArray134[z][x + 1][y] = 50;
                } else if (j1 == 3) {
                    aByteArrayArrayArray134[z][x][y] = 50;
                }
            }
            if (objectDefinition.projectileClipped && collisionMap != null) {
                collisionMap.method211(y, j1, x, type, objectDefinition.impenetrable);
            }
            return;
        }
        if (type == 9) {
            Object obj6;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj6 = objectDefinition.modelAt(type, j1, center, east, northEast, north, -1);
            } else {
                obj6 = new SceneObject(id, j1, type, east, northEast, center, north, objectDefinition.animation, true);
            }
            sceneGraph.addInteractableEntity(key, config, mean, 1, ((Renderable) (obj6)), 1, z, 0, y, x, id);
            if (objectDefinition.projectileClipped && collisionMap != null) {
                collisionMap.method212(objectDefinition.impenetrable, objectDefinition.sizeX, objectDefinition.sizeY, x, y, j1);
            }
            return;
        }
        if (objectDefinition.contouredGround) {
            if (j1 == 1) {
                int j3 = north;
                north = northEast;
                northEast = east;
                east = center;
                center = j3;
            } else if (j1 == 2) {
                int k3 = north;
                north = east;
                east = k3;
                k3 = northEast;
                northEast = center;
                center = k3;
            } else if (j1 == 3) {
                int l3 = north;
                north = center;
                center = east;
                east = northEast;
                northEast = l3;
            }
        }
        if (type == 4) {
            Object obj7;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj7 = objectDefinition.modelAt(4, 0, center, east, northEast, north, -1);
            } else {
                obj7 = new SceneObject(id, 0, 4, east, northEast, center, north, objectDefinition.animation, true);
            }
            sceneGraph.addWallDecoration(key, y, j1 * 512, z, 0, mean, ((Renderable) (obj7)), x, config, 0, anIntArray152[j1], id);
            return;
        }
        if (type == 5) {
            int i4 = 16;
            int k4 = sceneGraph.getWallKey(z, x, y);
            if (k4 > 0) {
                i4 = ObjectDefinition.forId(k4 >> 14 & 0x7fff).decorDisplacement;
            }
            Object obj13;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj13 = objectDefinition.modelAt(4, 0, center, east, northEast, north, -1);
            } else {
                obj13 = new SceneObject(id, 0, 4, east, northEast, center, north, objectDefinition.animation, true);
            }
            sceneGraph.addWallDecoration(key, y, j1 * 512, z, anIntArray137[j1] * i4, mean, ((Renderable) (obj13)), x, config, anIntArray144[j1] * i4, anIntArray152[j1], id);
            return;
        }
        if (type == 6) {
            Object obj8;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj8 = objectDefinition.modelAt(4, 0, center, east, northEast, north, -1);
            } else {
                obj8 = new SceneObject(id, 0, 4, east, northEast, center, north, objectDefinition.animation, true);
            }
            sceneGraph.addWallDecoration(key, y, j1, z, 0, mean, ((Renderable) (obj8)), x, config, 0, 256, id);
            return;
        }
        if (type == 7) {
            Object obj9;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj9 = objectDefinition.modelAt(4, 0, center, east, northEast, north, -1);
            } else {
                obj9 = new SceneObject(id, 0, 4, east, northEast, center, north, objectDefinition.animation, true);
            }
            sceneGraph.addWallDecoration(key, y, j1, z, 0, mean, ((Renderable) (obj9)), x, config, 0, 512, id);
            return;
        }
        if (type == 8) {
            Object obj10;
            if (objectDefinition.animation == -1 && objectDefinition.childrenIds == null) {
                obj10 = objectDefinition.modelAt(4, 0, center, east, northEast, north, -1);
            } else {
                obj10 = new SceneObject(id, 0, 4, east, northEast, center, north, objectDefinition.animation, true);
            }
            sceneGraph.addWallDecoration(key, y, j1, z, 0, mean, ((Renderable) (obj10)), x, config, 0, 768, id);
        }
    }

    /**
     * Encodes the hue, saturation, and luminance into a colour value.
     *
     * @param hue        The hue.
     * @param saturation The saturation.
     * @param luminance  The luminance.
     * @return The colour.
     */
    private int encode(int hue, int saturation, int luminance) {
        if (luminance > 179) {
            saturation /= 2;
        }
        if (luminance > 192) {
            saturation /= 2;
        }
        if (luminance > 217) {
            saturation /= 2;
        }
        if (luminance > 243) {
            saturation /= 2;
        }
        return (hue / 4 << 10) + (saturation / 32 << 7) + luminance / 2;
    }

    public final void method179(int i, int j, CollisionMap aclass11[], int l, int i1, byte abyte0[], int j1, int k1, int l1) {
        for (int i2 = 0; i2 < 8; i2++) {
            for (int j2 = 0; j2 < 8; j2++) {
                if (l + i2 > 0 && l + i2 < 103 && l1 + j2 > 0 && l1 + j2 < 103) {
                    aclass11[k1].adjacencies[l + i2][l1 + j2] &= 0xfeffffff;
                }
            }

        }
        RSStream stream = new RSStream(abyte0);
        for (int l2 = 0; l2 < 4; l2++) {
            for (int i3 = 0; i3 < 64; i3++) {
                for (int j3 = 0; j3 < 64; j3++) {
                    if (l2 == i && i3 >= i1 && i3 < i1 + 8 && j3 >= j1 && j3 < j1 + 8) {
                        method181(l1 + ChunkUtil.method156(j3 & 7, j, i3 & 7), 0, stream, l + ChunkUtil.method155(j, j3 & 7, i3 & 7), k1, j, 0);
                    } else {
                        method181(-1, 0, stream, -1, 0, 0, 0);
                    }
                }

            }

        }

    }

    public final void method180(byte abyte0[], int i, int j, int k, int l, CollisionMap aclass11[]) {
        for (int i1 = 0; i1 < 4; i1++) {
            for (int j1 = 0; j1 < 64; j1++) {
                for (int k1 = 0; k1 < 64; k1++) {
                    if (j + j1 > 0 && j + j1 < 103 && i + k1 > 0 && i + k1 < 103) {
                        aclass11[i1].adjacencies[j + j1][i + k1] &= 0xfeffffff;
                    }
                }
            }

        }

        RSStream stream = new RSStream(abyte0);
        for (int l1 = 0; l1 < 4; l1++) {
            for (int i2 = 0; i2 < 64; i2++) {
                for (int j2 = 0; j2 < 64; j2++) {
                    method181(j2 + i, l, stream, i2 + j, l1, 0, k);
                }

            }

        }
    }

    private void method181(int i, int j, RSStream stream, int k, int l, int i1, int k1) {
        if (k >= 0 && k < 104 && i >= 0 && i < 104) {
            tileFlags[l][k][i] = 0;
            do {
                int l1 = stream.getByte();
                if (l1 == 0) {
                    if (l == 0) {
                        anIntArrayArrayArray129[0][k][i] = -method172(0xe3b7b + k + k1,
                                0x87cce + i + j) * 8;
                        return;
                    } else {
                        anIntArrayArrayArray129[l][k][i] = anIntArrayArrayArray129[l - 1][k][i]
                                - 240;
                        return;
                    }
                }
                if (l1 == 1) {
                    int j2 = stream.getByte();
                    if (j2 == 1) {
                        j2 = 0;
                    }
                    if (l == 0) {
                        anIntArrayArrayArray129[0][k][i] = -j2 * 8;
                        return;
                    } else {
                        anIntArrayArrayArray129[l][k][i] = anIntArrayArrayArray129[l - 1][k][i]
                                - j2 * 8;
                        return;
                    }
                }
                if (l1 <= 49) {
                    aByteArrayArrayArray130[l][k][i] = stream.getSignedByte();//overlay?
                    tileShape[l][k][i] = (byte) ((l1 - 2) / 4);
                    tileShapeRotation[l][k][i] = (byte) ((l1 - 2) + i1 & 3);
                } else if (l1 <= 81) {
                    tileFlags[l][k][i] = (byte) (l1 - 49);
                } else {//underlay?
                    aByteArrayArrayArray142[l][k][i] = (byte) (l1 - 81);
                }
            } while (true);
        }
        do {
            int i2 = stream.getByte();
            if (i2 == 0) {
                break;
            }
            if (i2 == 1) {
                stream.getByte();
                return;
            }
            if (i2 <= 49) {
                stream.getByte();
            }
        } while (true);
    }

    /**
     * Returns the plane that actually contains the collision flag, to adjust for objects such as bridges.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     * @return The correct z coordinate.
     */
    private int getCollisionPlane(int y, int z, int x) {
        if ((tileFlags[z][x][y] & FORCE_LOWEST_PLANE) != 0) {
            return 0;
        }
        if (z > 0 && (tileFlags[1][x][y] & BRIDGE_TILE) != 0) {
            return z - 1;
        } else {
            return z;
        }
    }

    public final void parseLandscape(CollisionMap[] collisionMaps, SceneGraph worldController, int myHeight, int j, int myY, int l, byte abyte0[], int myX, int j1, int k1) {
        label0:
        {
            RSStream stream = new RSStream(abyte0);
            int objectId = -1;
            do {
                int i2 = stream.getBigSmart();
                if (i2 == 0) {
                    break label0;
                }
                objectId += i2;
                int location = 0;
                do {
                    int k2 = stream.getSmartB();
                    if (k2 == 0) {
                        break;
                    }
                    location += k2 - 1;
                    int y = location & 0x3f;
                    int x = location >> 6 & 0x3f;
                    int height = location >> 12;
                    int configuration = stream.getByte();
                    int type = configuration >> 2;
                    int rotation = configuration & 3;
                    if (height == myHeight && x >= myX && x < myX + 8 && y >= myY && y < myY + 8) {
                        ObjectDefinition objectDefinition = ObjectDefinition.forId(objectId);
                        int j4 = j + ChunkUtil.method157(j1, objectDefinition.sizeY, x & 7, y & 7, objectDefinition.sizeX);
                        int k4 = k1 + ChunkUtil.method158(y & 7, objectDefinition.sizeY, j1, objectDefinition.sizeX, x & 7);
                        if (j4 > 0 && k4 > 0 && j4 < 103 && k4 < 103) {
                            int z = height;
                            if ((tileFlags[1][j4][k4] & 2) == 2) {
                                z--;
                            }
                            CollisionMap class11 = null;
                            if (z >= 0) {
                                class11 = collisionMaps[z];
                            }
                            method175(k4, worldController, class11, type, l, j4, objectId, rotation + j1 & 3);
                        }
                    }
                } while (true);
            } while (true);
        }
    }

    private int getOverlayShadow(int color, int light) {
        if (color == -2) {
            return 0xbc614e;
        }
        if (color == -1) {
            if (light < 0) {
                light = 0;
            } else if (light > 127) {
                light = 127;
            }
            light = 127 - light;
            return light;
        }
        light = (light * (color & 0x7f)) / 128;
        if (light < 2) {
            light = 2;
        } else if (light > 126) {
            light = 126;
        }
        return (color & 0xff80) + light;
    }

    public final void parseMapscape(int i, CollisionMap collisionMaps[], int j, SceneGraph worldController, byte bytes[]) {
        label0:
        {
            RSStream stream = new RSStream(bytes);
            int objectId = -1;
            do {
                int i1 = stream.getSmartB();
                if (i1 == 0) {
                    break label0;
                }
                objectId += i1;
                int location = 0;
                do {
                    int offset = stream.getSmartB();
                    if (offset == 0) {
                        break;
                    }
                    location += offset - 1;
                    int y = location & 0x3f;
                    int x = location >> 6 & 0x3f;
                    int z = location >> 12;

                    int configuration = stream.getByte();
                    int rotation = configuration & 0x3;
                    int type = configuration >> 2;
                    int j3 = x + i;
                    int k3 = y + j;
                    if (j3 > 0 && k3 > 0 && j3 < 103 && k3 < 103 && z >= 0 && z < 4) {
                        int l3 = z;
                        if ((tileFlags[1][j3][k3] & 2) == 2) {
                            l3--;
                        }
                        CollisionMap collisionMap = null;
                        if (l3 >= 0) {
                            collisionMap = collisionMaps[l3];
                        }
                        method175(k3, worldController, collisionMap, type, z, j3, objectId, rotation);
                    }
                } while (true);
            } while (true);
        }
    }
}
