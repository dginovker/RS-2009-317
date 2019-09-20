package com.runescape.media.renderable;

import main.java.com.runescape.Game;
import com.runescape.cache.media.SequenceFrame;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.media.Raster;
import com.runescape.media.SkinList;
import com.runescape.media.Vertex;
import com.runescape.net.RSStream;
import com.runescape.net.requester.Provider;
import com.runescape.scene.graphic.Rasterizer;
import com.runescape.scene.map.SceneGraph;

public class Model extends Renderable {
    public static Model EMPTY_MODEL = new Model(true);
    public static boolean[] NEW_MODELS;
    public static boolean[] OSRS_NEW_MODELS;
    public static int anInt1620;
    public boolean osrs;
    public static ModelHeader[] modelHeaders;
    public static ModelHeader[] osrsModelHeaders;
    public static boolean aBoolean1684;
    public static int anInt1685;
    public static int anInt1686;
    public static int objectsRendered;
    public static int[] mapObjectData = new int[1000];
    public static int[] mapObjectIds = new int[1000];
    public static int[] SINE;
    public static int[] COSINE;
    static Provider resourceProvider;
    static boolean hasAnEdgeToRestrict[] = new boolean[8000];
    static boolean outOfReach[] = new boolean[8000];
    static int projected_vertex_x[] = new int[8000];
    static int projected_vertex_y[] = new int[8000];
    static int projected_vertex_z[] = new int[8000];
    static int anIntArray1667[] = new int[8000];
    static int camera_vertex_y[] = new int[8000];
    static int camera_vertex_x[] = new int[8000];
    static int camera_vertex_z[] = new int[8000];
    static int anIntArray1671[] = new int[1500];
    static int anIntArrayArray1672[][] = new int[1500][512];
    static int anIntArray1673[] = new int[12];
    static int anIntArrayArray1674[][] = new int[12][2000];
    static int anIntArray1675[] = new int[2000];
    static int anIntArray1676[] = new int[2000];
    static int anIntArray1677[] = new int[12];
    static int anIntArray1678[] = new int[10];
    static int anIntArray1679[] = new int[10];
    static int anIntArray1680[] = new int[10];
    static int anInt1681;
    static int anInt1682;
    static int anInt1683;
    static int[] hsl2rgb;
    static int[] lightDecay;

    static {
        SINE = Rasterizer.SINE;
        COSINE = Rasterizer.COSINE;
        hsl2rgb = Rasterizer.hsl2rgb;
        lightDecay = Rasterizer.lightDecay;
    }

    private static int anIntArray1622[] = new int[2000];
    private static int anIntArray1623[] = new int[2000];
    private static int anIntArray1624[] = new int[2000];
    private static int anIntArray1625[] = new int[2000];
    public int vertexCount;
    public int verticesX[];
    public int verticesY[];
    public int verticesZ[];
    public int triangleCount;
    public int trianglePointsX[];
    public int trianglePointsY[];
    public int trianglePointsZ[];
    public int faceShadeA[];
    public int faceShadeB[];
    public int faceShadeC[];
    public int texturePoints[];
    public int trianglePriorities[];
    public int triangleAlphaValues[];
    public int triangleColorValues[];
    public int anInt1641;
    public int anInt1642;
    public int texturesFaceA[];
    public int texturesFaceB[];
    public int texturesFaceC[];
    public int anInt1646;
    public int anInt1647;
    public int anInt1648;
    public int anInt1649;
    public int anInt1650;
    public int anInt1651;
    public int anInt1652;
    public int anInt1653;
    public int myPriority;
    public int vertexSkins[];
    public int triangleSkinValues[];
    public int vertexGroups[][];
    public int faceGroups[][];
    public boolean fitsOnSingleSquare;
    public Vertex vertexOffset[];
    private boolean aBoolean1618;

    public Model(int modelId, boolean osrs) {
        this.osrs = osrs;
        byte[] is = osrs ? osrsModelHeaders[modelId].modelData : modelHeaders[modelId].modelData;
        try {
            if (is[is.length - 1] == -1 && is[is.length - 2] == -1) {
                read622Model(is, modelId, osrs);
            } else {
                readOldModel(modelId, osrs);
            }
            if (!osrs && NEW_MODELS[modelId]) {
                scale2(4);
            }
            if (osrs && OSRS_NEW_MODELS[modelId]) {
                scale2(4);
            }
            if (modelId == 48841 || modelId == 48825 || modelId == 48817
                    || modelId == 48802 || modelId == 48840 || modelId == 45536
                    || modelId == 38284 || modelId == 45522 || modelId == 45517
                    || modelId == 45514 || modelId == 45490 || modelId == 48790
                    || modelId == 59583) {
                transformScale(32, 32, 32);
                translate(0, 6, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Model(boolean flag) {
        this.osrs = false;
        aBoolean1618 = true;
        fitsOnSingleSquare = false;
        if (!flag) {
            aBoolean1618 = !aBoolean1618;
        }
    }

    public Model(int modelIds, Model[] model1) {
        aBoolean1618 = true;
        fitsOnSingleSquare = false;
        anInt1620++;
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        vertexCount = 0;
        triangleCount = 0;
        anInt1642 = 0;
        anInt1641 = -1;
        for (int k = 0; k < modelIds; k++) {
            Model model = model1[k];
            if (model != null) {
                vertexCount += model.vertexCount;
                triangleCount += model.triangleCount;
                anInt1642 += model.anInt1642;
                flag |= model.texturePoints != null;
                if (model.trianglePriorities != null) {
                    flag1 = true;
                } else {
                    if (anInt1641 == -1) {
                        anInt1641 = model.anInt1641;
                    }
                    if (anInt1641 != model.anInt1641) {
                        flag1 = true;
                    }
                }
                flag2 |= model.triangleAlphaValues != null;
                flag3 |= model.triangleSkinValues != null;
            }
        }

        verticesX = new int[vertexCount];
        verticesY = new int[vertexCount];
        verticesZ = new int[vertexCount];
        vertexSkins = new int[vertexCount];
        trianglePointsX = new int[triangleCount];
        trianglePointsY = new int[triangleCount];
        trianglePointsZ = new int[triangleCount];
        texturesFaceA = new int[anInt1642];
        texturesFaceB = new int[anInt1642];
        texturesFaceC = new int[anInt1642];
        if (flag) {
            texturePoints = new int[triangleCount];
        }
        if (flag1) {
            trianglePriorities = new int[triangleCount];
        }
        if (flag2) {
            triangleAlphaValues = new int[triangleCount];
        }
        if (flag3) {
            triangleSkinValues = new int[triangleCount];
        }
        triangleColorValues = new int[triangleCount];
        vertexCount = 0;
        triangleCount = 0;
        anInt1642 = 0;
        int l = 0;
        for (int i1 = 0; i1 < modelIds; i1++) {
            Model model_1 = model1[i1];
            if (model_1 != null) {
                for (int j1 = 0; j1 < model_1.triangleCount; j1++) {
                    if (flag) {
                        if (model_1.texturePoints == null) {
                            texturePoints[triangleCount] = 0;
                        } else {
                            int k1 = model_1.texturePoints[j1];
                            if ((k1 & 2) == 2) {
                                k1 += l << 2;
                            }
                            texturePoints[triangleCount] = k1;
                        }
                    }
                    if (flag1) {
                        if (model_1.trianglePriorities == null) {
                            trianglePriorities[triangleCount] = model_1.anInt1641;
                        } else {
                            trianglePriorities[triangleCount] = model_1.trianglePriorities[j1];
                        }
                    }
                    if (flag2) {
                        if (model_1.triangleAlphaValues == null) {
                            triangleAlphaValues[triangleCount] = 0;
                        } else {
                            triangleAlphaValues[triangleCount] = model_1.triangleAlphaValues[j1];
                        }
                    }

                    if (flag3 && model_1.triangleSkinValues != null) {
                        triangleSkinValues[triangleCount] = model_1.triangleSkinValues[j1];
                    }
                    triangleColorValues[triangleCount] = model_1.triangleColorValues[j1];
                    trianglePointsX[triangleCount] = method465(model_1,
                            model_1.trianglePointsX[j1]);
                    trianglePointsY[triangleCount] = method465(model_1,
                            model_1.trianglePointsY[j1]);
                    trianglePointsZ[triangleCount] = method465(model_1,
                            model_1.trianglePointsZ[j1]);
                    triangleCount++;
                }

                for (int l1 = 0; l1 < model_1.anInt1642; l1++) {
                    texturesFaceA[anInt1642] = method465(model_1,
                            model_1.texturesFaceA[l1]);
                    texturesFaceB[anInt1642] = method465(model_1,
                            model_1.texturesFaceB[l1]);
                    texturesFaceC[anInt1642] = method465(model_1,
                            model_1.texturesFaceC[l1]);
                    anInt1642++;
                }

                l += model_1.anInt1642;
            }
        }
    }

    public Model(Model[] models) {
        int i = 2;
        aBoolean1618 = true;
        fitsOnSingleSquare = false;
        anInt1620++;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;
        vertexCount = 0;
        triangleCount = 0;
        anInt1642 = 0;
        anInt1641 = -1;
        for (int k = 0; k < i; k++) {
            Model model = models[k];
            if (model != null) {
                vertexCount += model.vertexCount;
                triangleCount += model.triangleCount;
                anInt1642 += model.anInt1642;
                flag1 |= model.texturePoints != null;
                if (model.trianglePriorities != null) {
                    flag2 = true;
                } else {
                    if (anInt1641 == -1) {
                        anInt1641 = model.anInt1641;
                    }
                    if (anInt1641 != model.anInt1641) {
                        flag2 = true;
                    }
                }
                flag3 |= model.triangleAlphaValues != null;
                flag4 |= model.triangleColorValues != null;
            }
        }

        verticesX = new int[vertexCount];
        verticesY = new int[vertexCount];
        verticesZ = new int[vertexCount];
        trianglePointsX = new int[triangleCount];
        trianglePointsY = new int[triangleCount];
        trianglePointsZ = new int[triangleCount];
        faceShadeA = new int[triangleCount];
        faceShadeB = new int[triangleCount];
        faceShadeC = new int[triangleCount];
        texturesFaceA = new int[anInt1642];
        texturesFaceB = new int[anInt1642];
        texturesFaceC = new int[anInt1642];
        if (flag1) {
            texturePoints = new int[triangleCount];
        }
        if (flag2) {
            trianglePriorities = new int[triangleCount];
        }
        if (flag3) {
            triangleAlphaValues = new int[triangleCount];
        }
        if (flag4) {
            triangleColorValues = new int[triangleCount];
        }
        vertexCount = 0;
        triangleCount = 0;
        anInt1642 = 0;
        int i1 = 0;
        for (int j1 = 0; j1 < i; j1++) {
            Model model_1 = models[j1];
            if (model_1 != null) {
                int k1 = vertexCount;
                for (int l1 = 0; l1 < model_1.vertexCount; l1++) {
                    verticesX[vertexCount] = model_1.verticesX[l1];
                    verticesY[vertexCount] = model_1.verticesY[l1];
                    verticesZ[vertexCount] = model_1.verticesZ[l1];
                    vertexCount++;
                }

                for (int i2 = 0; i2 < model_1.triangleCount; i2++) {
                    trianglePointsX[triangleCount] = model_1.trianglePointsX[i2] + k1;
                    trianglePointsY[triangleCount] = model_1.trianglePointsY[i2] + k1;
                    trianglePointsZ[triangleCount] = model_1.trianglePointsZ[i2] + k1;
                    faceShadeA[triangleCount] = model_1.faceShadeA[i2];
                    faceShadeB[triangleCount] = model_1.faceShadeB[i2];
                    faceShadeC[triangleCount] = model_1.faceShadeC[i2];
                    if (flag1) {
                        if (model_1.texturePoints == null) {
                            texturePoints[triangleCount] = 0;
                        } else {
                            int j2 = model_1.texturePoints[i2];
                            if ((j2 & 2) == 2) {
                                j2 += i1 << 2;
                            }
                            texturePoints[triangleCount] = j2;
                        }
                    }
                    if (flag2) {
                        if (model_1.trianglePriorities == null) {
                            trianglePriorities[triangleCount] = model_1.anInt1641;
                        } else {
                            trianglePriorities[triangleCount] = model_1.trianglePriorities[i2];
                        }
                    }
                    if (flag3) {
                        if (model_1.triangleAlphaValues == null) {
                            triangleAlphaValues[triangleCount] = 0;
                        } else {
                            triangleAlphaValues[triangleCount] = model_1.triangleAlphaValues[i2];
                        }
                    }
                    if (flag4 && model_1.triangleColorValues != null) {
                        triangleColorValues[triangleCount] = model_1.triangleColorValues[i2];
                    }

                    triangleCount++;
                }

                for (int k2 = 0; k2 < model_1.anInt1642; k2++) {
                    texturesFaceA[anInt1642] = model_1.texturesFaceA[k2] + k1;
                    texturesFaceB[anInt1642] = model_1.texturesFaceB[k2] + k1;
                    texturesFaceC[anInt1642] = model_1.texturesFaceC[k2] + k1;
                    anInt1642++;
                }

                i1 += model_1.anInt1642;
            }
        }

        method466();
    }

    public Model(boolean flag, boolean flag1, boolean flag2, Model model) {
        this.osrs = model.osrs;
        aBoolean1618 = true;
        fitsOnSingleSquare = false;
        anInt1620++;
        vertexCount = model.vertexCount;
        triangleCount = model.triangleCount;
        anInt1642 = model.anInt1642;
        if (flag2) {
            verticesX = model.verticesX;
            verticesY = model.verticesY;
            verticesZ = model.verticesZ;
        } else {
            verticesX = new int[vertexCount];
            verticesY = new int[vertexCount];
            verticesZ = new int[vertexCount];
            for (int j = 0; j < vertexCount; j++) {
                verticesX[j] = model.verticesX[j];
                verticesY[j] = model.verticesY[j];
                verticesZ[j] = model.verticesZ[j];
            }

        }
        if (flag) {
            triangleColorValues = model.triangleColorValues;
        } else {
            triangleColorValues = new int[triangleCount];
            for (int k = 0; k < triangleCount; k++) {
                triangleColorValues[k] = model.triangleColorValues[k];
            }

        }
        if (flag1) {
            triangleAlphaValues = model.triangleAlphaValues;
        } else {
            triangleAlphaValues = new int[triangleCount];
            if (model.triangleAlphaValues == null) {
                for (int l = 0; l < triangleCount; l++) {
                    triangleAlphaValues[l] = 0;
                }

            } else {
                for (int i1 = 0; i1 < triangleCount; i1++) {
                    triangleAlphaValues[i1] = model.triangleAlphaValues[i1];
                }

            }
        }
        vertexSkins = model.vertexSkins;
        triangleSkinValues = model.triangleSkinValues;
        texturePoints = model.texturePoints;
        trianglePointsX = model.trianglePointsX;
        trianglePointsY = model.trianglePointsY;
        trianglePointsZ = model.trianglePointsZ;
        trianglePriorities = model.trianglePriorities;
        anInt1641 = model.anInt1641;
        texturesFaceA = model.texturesFaceA;
        texturesFaceB = model.texturesFaceB;
        texturesFaceC = model.texturesFaceC;
    }

    public Model(boolean flag, boolean flag1, Model model) {
        this.osrs = model.osrs;
        aBoolean1618 = true;
        fitsOnSingleSquare = false;
        anInt1620++;
        vertexCount = model.vertexCount;
        triangleCount = model.triangleCount;
        anInt1642 = model.anInt1642;
        if (flag) {
            verticesY = new int[vertexCount];
            for (int j = 0; j < vertexCount; j++) {
                verticesY[j] = model.verticesY[j];
            }

        } else {
            verticesY = model.verticesY;
        }
        if (flag1) {
            faceShadeA = new int[triangleCount];
            faceShadeB = new int[triangleCount];
            faceShadeC = new int[triangleCount];
            for (int k = 0; k < triangleCount; k++) {
                faceShadeA[k] = model.faceShadeA[k];
                faceShadeB[k] = model.faceShadeB[k];
                faceShadeC[k] = model.faceShadeC[k];
            }

            texturePoints = new int[triangleCount];
            if (model.texturePoints == null) {
                for (int l = 0; l < triangleCount; l++) {
                    texturePoints[l] = 0;
                }

            } else {
                for (int i1 = 0; i1 < triangleCount; i1++) {
                    texturePoints[i1] = model.texturePoints[i1];
                }

            }
            super.vertexes = new Vertex[vertexCount];
            for (int j1 = 0; j1 < vertexCount; j1++) {
                Vertex class33 = super.vertexes[j1] = new Vertex();
                Vertex class33_1 = model.vertexes[j1];
                class33.anInt602 = class33_1.anInt602;
                class33.anInt603 = class33_1.anInt603;
                class33.anInt604 = class33_1.anInt604;
                class33.anInt605 = class33_1.anInt605;
            }

            vertexOffset = model.vertexOffset;
        } else {
            faceShadeA = model.faceShadeA;
            faceShadeB = model.faceShadeB;
            faceShadeC = model.faceShadeC;
            texturePoints = model.texturePoints;
        }
        verticesX = model.verticesX;
        verticesZ = model.verticesZ;
        triangleColorValues = model.triangleColorValues;
        triangleAlphaValues = model.triangleAlphaValues;
        trianglePriorities = model.trianglePriorities;
        anInt1641 = model.anInt1641;
        trianglePointsX = model.trianglePointsX;
        trianglePointsY = model.trianglePointsY;
        trianglePointsZ = model.trianglePointsZ;
        texturesFaceA = model.texturesFaceA;
        texturesFaceB = model.texturesFaceB;
        texturesFaceC = model.texturesFaceC;
        super.modelHeight = model.modelHeight;

        anInt1650 = model.anInt1650;
        anInt1653 = model.anInt1653;
        anInt1652 = model.anInt1652;
        anInt1646 = model.anInt1646;
        anInt1648 = model.anInt1648;
        anInt1649 = model.anInt1649;
        anInt1647 = model.anInt1647;
    }

    public static void nullLoader() {
        modelHeaders = null;
        osrsModelHeaders = null;
        hasAnEdgeToRestrict = null;
        outOfReach = null;
        projected_vertex_y = null;
        anIntArray1667 = null;
        camera_vertex_y = null;
        camera_vertex_x = null;
        camera_vertex_z = null;
        anIntArray1671 = null;
        anIntArrayArray1672 = null;
        anIntArray1673 = null;
        anIntArrayArray1674 = null;
        anIntArray1675 = null;
        anIntArray1676 = null;
        anIntArray1677 = null;
        SINE = null;
        COSINE = null;
        hsl2rgb = null;
        lightDecay = null;
    }

    public static void setModelData(byte[] modelData, int modelId) {
        if (modelData == null) {
            ModelHeader modelHeader = modelHeaders[modelId] = new ModelHeader(false);
            modelHeader.vertexCount = 0;
            modelHeader.triangleCount = 0;
            modelHeader.texturedTriangleCount = 0;
            return;
        }
        RSStream stream = new RSStream(modelData);
        stream.currentPosition = modelData.length - 18;
        ModelHeader modelHeader = modelHeaders[modelId] = new ModelHeader(false);
        modelHeader.modelData = modelData;
        modelHeader.vertexCount = stream.getShort();
        modelHeader.triangleCount = stream.getShort();
        modelHeader.texturedTriangleCount = stream.getByte();
        int k = stream.getByte();
        int l = stream.getByte();
        int i1 = stream.getByte();
        int j1 = stream.getByte();
        int k1 = stream.getByte();
        int l1 = stream.getShort();
        int i2 = stream.getShort();
        int j2 = stream.getShort();
        int k2 = stream.getShort();
        int l2 = 0;
        modelHeader.anInt372 = l2;
        l2 += modelHeader.vertexCount;
        modelHeader.anInt378 = l2;
        l2 += modelHeader.triangleCount;
        modelHeader.anInt381 = l2;
        if (l == 255) {
            l2 += modelHeader.triangleCount;
        } else {
            modelHeader.anInt381 = -l - 1;
        }
        modelHeader.anInt383 = l2;
        if (j1 == 1) {
            l2 += modelHeader.triangleCount;
        } else {
            modelHeader.anInt383 = -1;
        }
        modelHeader.anInt380 = l2;
        if (k == 1) {
            l2 += modelHeader.triangleCount;
        } else {
            modelHeader.anInt380 = -1;
        }
        modelHeader.anInt376 = l2;
        if (k1 == 1) {
            l2 += modelHeader.vertexCount;
        } else {
            modelHeader.anInt376 = -1;
        }
        modelHeader.anInt382 = l2;
        if (i1 == 1) {
            l2 += modelHeader.triangleCount;
        } else {
            modelHeader.anInt382 = -1;
        }
        modelHeader.anInt377 = l2;
        l2 += k2;
        modelHeader.anInt379 = l2;
        l2 += modelHeader.triangleCount * 2;
        modelHeader.anInt384 = l2;
        l2 += modelHeader.texturedTriangleCount * 6;
        modelHeader.anInt373 = l2;
        l2 += l1;
        modelHeader.anInt374 = l2;
        l2 += i2;
        modelHeader.anInt375 = l2;
        l2 += j2;
    }

    public static void setOSRSModelData(byte[] modelData, int modelId) {
        if (modelData == null) {
            ModelHeader modelHeader = osrsModelHeaders[modelId] = new ModelHeader(true);
            modelHeader.vertexCount = 0;
            modelHeader.triangleCount = 0;
            modelHeader.texturedTriangleCount = 0;
            return;
        }
        RSStream rsStream = new RSStream(modelData);
        rsStream.currentPosition = modelData.length - 18;
        ModelHeader modelHeader = osrsModelHeaders[modelId] = new ModelHeader(true);
        modelHeader.modelData = modelData;
        modelHeader.vertexCount = rsStream.getShort();
        modelHeader.triangleCount = rsStream.getShort();
        modelHeader.texturedTriangleCount = rsStream.getByte();
        int k = rsStream.getByte();
        int l = rsStream.getByte();
        int i1 = rsStream.getByte();
        int j1 = rsStream.getByte();
        int k1 = rsStream.getByte();
        int l1 = rsStream.getShort();
        int i2 = rsStream.getShort();
        int j2 = rsStream.getShort();
        int k2 = rsStream.getShort();
        int l2 = 0;
        modelHeader.anInt372 = l2;
        l2 += modelHeader.vertexCount;
        modelHeader.anInt378 = l2;
        l2 += modelHeader.triangleCount;
        modelHeader.anInt381 = l2;
        if (l == 255) {
            l2 += modelHeader.triangleCount;
        } else {
            modelHeader.anInt381 = -l - 1;
        }
        modelHeader.anInt383 = l2;
        if (j1 == 1) {
            l2 += modelHeader.triangleCount;
        } else {
            modelHeader.anInt383 = -1;
        }
        modelHeader.anInt380 = l2;
        if (k == 1) {
            l2 += modelHeader.triangleCount;
        } else {
            modelHeader.anInt380 = -1;
        }
        modelHeader.anInt376 = l2;
        if (k1 == 1) {
            l2 += modelHeader.vertexCount;
        } else {
            modelHeader.anInt376 = -1;
        }
        modelHeader.anInt382 = l2;
        if (i1 == 1) {
            l2 += modelHeader.triangleCount;
        } else {
            modelHeader.anInt382 = -1;
        }
        modelHeader.anInt377 = l2;
        l2 += k2;
        modelHeader.anInt379 = l2;
        l2 += modelHeader.triangleCount * 2;
        modelHeader.anInt384 = l2;
        l2 += modelHeader.texturedTriangleCount * 6;
        modelHeader.anInt373 = l2;
        l2 += l1;
        modelHeader.anInt374 = l2;
        l2 += i2;
        modelHeader.anInt375 = l2;
        l2 += j2;
    }

    public static void init(int i, Provider provider) {
        modelHeaders = new ModelHeader[80000];
        osrsModelHeaders = new ModelHeader[80000];
        NEW_MODELS = new boolean[100000];
        OSRS_NEW_MODELS = new boolean[100000];
        resourceProvider = provider;
    }

    public static void clearModel(int modelId, boolean osrs) {
        if (osrs) {
            osrsModelHeaders[modelId] = null;
            OSRS_NEW_MODELS[modelId] = false;
            return;
        }
        modelHeaders[modelId] = null;
        NEW_MODELS[modelId] = false;
    }

    public static Model getOSRSModel(int modelId) {
        if (osrsModelHeaders == null) {
            return null;
        }
        ModelHeader modelHeader = osrsModelHeaders[modelId];
        if (modelHeader == null) {
            resourceProvider.provide(0, modelId);
            return null;
        } else {
            return new Model(modelId, true);
        }
    }

    public static Model getModel(int modelId, boolean osrs) {
        if (osrs) {
            return getOSRSModel(modelId);
        }
        if (modelHeaders == null) {
            return null;
        }
        ModelHeader modelHeader = modelHeaders[modelId];
        if (modelHeader == null) {
            resourceProvider.provide(4, modelId);
            return null;
        } else {
            return new Model(modelId, false);
        }
    }

    public static boolean isOSRSCached(int modelId) {
        if (osrsModelHeaders == null) {
            return false;
        }
        ModelHeader modelHeader = osrsModelHeaders[modelId];
        if (modelHeader == null) {
            resourceProvider.provide(0, modelId);
            return false;
        } else {
            return true;
        }
    }


    public static boolean isCached(int modelId, boolean osrs) {
        if (osrs) {
            return isOSRSCached(modelId);
        }
        if (modelHeaders == null) {
            return false;
        }
        ModelHeader modelHeader = modelHeaders[modelId];
        if (modelHeader == null) {
            resourceProvider.provide(4, modelId);
            return false;
        } else {
            return true;
        }
    }

    public static int method481(int i, int j, int k) {
        if (i == 65535) {
            return 0;
        }
        if ((k & 2) == 2) {
            if (j < 0) {
                j = 0;
            } else if (j > 127) {
                j = 127;
            }
            j = 127 - j;
            return j;
        }

        j = j * (i & 0x7f) >> 7;
        if (j < 2) {
            j = 2;
        } else if (j > 126) {
            j = 126;
        }
        return (i & 0xff80) + j;
    }

    public void read525Model(byte[] modelData, int modelId, boolean osrs) {
        RSStream nc1 = new RSStream(modelData);
        RSStream nc2 = new RSStream(modelData);
        RSStream nc3 = new RSStream(modelData);
        RSStream nc4 = new RSStream(modelData);
        RSStream nc5 = new RSStream(modelData);
        RSStream nc6 = new RSStream(modelData);
        RSStream nc7 = new RSStream(modelData);
        nc1.currentPosition = modelData.length - 23;
        int numVertices = nc1.getShort();
        int numTriangles = nc1.getShort();
        int numTexTriangles = nc1.getByte();
        ModelHeader modelHeader = null;
        if (osrs) {
            modelHeader = osrsModelHeaders[modelId] = new ModelHeader(true);
        } else {
            modelHeader = modelHeaders[modelId] = new ModelHeader(false);
        }
        modelHeader.modelData = modelData;
        modelHeader.vertexCount = numVertices;
        modelHeader.triangleCount = numTriangles;
        modelHeader.texturedTriangleCount = numTexTriangles;
        int l1 = nc1.getByte();
        boolean bool = (0x1 & l1 ^ 0xffffffff) == -2;
        int i2 = nc1.getByte();
        int j2 = nc1.getByte();
        int k2 = nc1.getByte();
        int l2 = nc1.getByte();
        int i3 = nc1.getByte();
        int j3 = nc1.getShort();
        int k3 = nc1.getShort();
        int l3 = nc1.getShort();
        int i4 = nc1.getShort();
        int j4 = nc1.getShort();
        int k4 = 0;
        int l4 = 0;
        int i5 = 0;
        byte[] x = null;
        byte[] O = null;
        byte[] J = null;
        byte[] F = null;
        byte[] cb = null;
        byte[] gb = null;
        byte[] lb = null;
        int[] kb = null;
        int[] y = null;
        int[] N = null;
        short[] D = null;
        int[] triangleColours2 = new int[numTriangles];
        if (numTexTriangles > 0) {
            O = new byte[numTexTriangles];
            nc1.currentPosition = 0;
            for (int j5 = 0; j5 < numTexTriangles; j5++) {
                byte byte0 = O[j5] = nc1.getSignedByte();
                if (byte0 == 0) {
                    k4++;
                }
                if (byte0 >= 1 && byte0 <= 3) {
                    l4++;
                }
                if (byte0 == 2) {
                    i5++;
                }
            }
        }
        int k5 = numTexTriangles;
        int l5 = k5;
        k5 += numVertices;
        int i6 = k5;
        if (l1 == 1) {
            k5 += numTriangles;
        }
        int j6 = k5;
        k5 += numTriangles;
        int k6 = k5;
        if (i2 == 255) {
            k5 += numTriangles;
        }
        int l6 = k5;
        if (k2 == 1) {
            k5 += numTriangles;
        }
        int i7 = k5;
        if (i3 == 1) {
            k5 += numVertices;
        }
        int j7 = k5;
        if (j2 == 1) {
            k5 += numTriangles;
        }
        int k7 = k5;
        k5 += i4;
        int l7 = k5;
        if (l2 == 1) {
            k5 += numTriangles * 2;
        }
        int i8 = k5;
        k5 += j4;
        int j8 = k5;
        k5 += numTriangles * 2;
        int k8 = k5;
        k5 += j3;
        int l8 = k5;
        k5 += k3;
        int i9 = k5;
        k5 += l3;
        int j9 = k5;
        k5 += k4 * 6;
        int k9 = k5;
        k5 += l4 * 6;
        int l9 = k5;
        k5 += l4 * 6;
        int i10 = k5;
        k5 += l4;
        int j10 = k5;
        k5 += l4;
        int k10 = k5;
        k5 += l4 + i5 * 2;
        int[] vertexX = new int[numVertices];
        int[] vertexY = new int[numVertices];
        int[] vertexZ = new int[numVertices];
        int[] facePoint1 = new int[numTriangles];
        int[] facePoint2 = new int[numTriangles];
        int[] facePoint3 = new int[numTriangles];
        vertexSkins = new int[numVertices];
        texturePoints = new int[numTriangles];
        trianglePriorities = new int[numTriangles];
        triangleAlphaValues = new int[numTriangles];
        triangleSkinValues = new int[numTriangles];
        if (i3 == 1) {
            vertexSkins = new int[numVertices];
        }
        if (bool) {
            texturePoints = new int[numTriangles];
        }
        if (i2 == 255) {
            trianglePriorities = new int[numTriangles];
        }
        if (j2 == 1) {
            triangleAlphaValues = new int[numTriangles];
        }
        if (k2 == 1) {
            triangleSkinValues = new int[numTriangles];
        }
        if (l2 == 1) {
            D = new short[numTriangles];
        }
        if (l2 == 1 && numTexTriangles > 0) {
            x = new byte[numTriangles];
        }
        triangleColours2 = new int[numTriangles];
        int[] texTrianglesPoint1 = null;
        int[] texTrianglesPoint2 = null;
        int[] texTrianglesPoint3 = null;
        if (numTexTriangles > 0) {
            texTrianglesPoint1 = new int[numTexTriangles];
            texTrianglesPoint2 = new int[numTexTriangles];
            texTrianglesPoint3 = new int[numTexTriangles];
            if (l4 > 0) {
                kb = new int[l4];
                N = new int[l4];
                y = new int[l4];
                gb = new byte[l4];
                lb = new byte[l4];
                F = new byte[l4];
            }
            if (i5 > 0) {
                cb = new byte[i5];
                J = new byte[i5];
            }
        }
        nc1.currentPosition = l5;
        nc2.currentPosition = k8;
        nc3.currentPosition = l8;
        nc4.currentPosition = i9;
        nc5.currentPosition = i7;
        int l10 = 0;
        int i11 = 0;
        int j11 = 0;
        for (int k11 = 0; k11 < numVertices; k11++) {
            int l11 = nc1.getByte();
            int j12 = 0;
            if ((l11 & 1) != 0) {
                j12 = nc2.getSmartA();
            }
            int l12 = 0;
            if ((l11 & 2) != 0) {
                l12 = nc3.getSmartA();
            }
            int j13 = 0;
            if ((l11 & 4) != 0) {
                j13 = nc4.getSmartA();
            }
            vertexX[k11] = l10 + j12;
            vertexY[k11] = i11 + l12;
            vertexZ[k11] = j11 + j13;
            l10 = vertexX[k11];
            i11 = vertexY[k11];
            j11 = vertexZ[k11];
            if (vertexSkins != null) {
                vertexSkins[k11] = nc5.getByte();
            }
        }
        nc1.currentPosition = j8;
        nc2.currentPosition = i6;
        nc3.currentPosition = k6;
        nc4.currentPosition = j7;
        nc5.currentPosition = l6;
        nc6.currentPosition = l7;
        nc7.currentPosition = i8;
        for (int i12 = 0; i12 < numTriangles; i12++) {
            triangleColours2[i12] = nc1.getShort();
            if (l1 == 1) {
                texturePoints[i12] = nc2.getSignedByte();
                if (texturePoints[i12] == 2) {
                    triangleColours2[i12] = 65535;
                }
                texturePoints[i12] = 0;
            }
            if (i2 == 255) {
                trianglePriorities[i12] = nc3.getSignedByte();
            }
            if (j2 == 1) {
                triangleAlphaValues[i12] = nc4.getSignedByte();
                if (triangleAlphaValues[i12] < 0) {
                    triangleAlphaValues[i12] = (256 + triangleAlphaValues[i12]);
                }
            }
            if (k2 == 1) {
                triangleSkinValues[i12] = nc5.getByte();
            }
            if (l2 == 1) {
                D[i12] = (short) (nc6.getShort() - 1);
            }
            if (x != null) {
                if (D[i12] != -1) {
                    x[i12] = (byte) (nc7.getByte() - 1);
                } else {
                    x[i12] = -1;
                }
            }
        }
        nc1.currentPosition = k7;
        nc2.currentPosition = j6;
        int k12 = 0;
        int i13 = 0;
        int k13 = 0;
        int l13 = 0;
        for (int i14 = 0; i14 < numTriangles; i14++) {
            int j14 = nc2.getByte();
            if (j14 == 1) {
                k12 = nc1.getSmartA() + l13;
                l13 = k12;
                i13 = nc1.getSmartA() + l13;
                l13 = i13;
                k13 = nc1.getSmartA() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
            if (j14 == 2) {
                i13 = k13;
                k13 = nc1.getSmartA() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
            if (j14 == 3) {
                k12 = k13;
                k13 = nc1.getSmartA() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
            if (j14 == 4) {
                int l14 = k12;
                k12 = i13;
                i13 = l14;
                k13 = nc1.getSmartA() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
        }
        nc1.currentPosition = j9;
        nc2.currentPosition = k9;
        nc3.currentPosition = l9;
        nc4.currentPosition = i10;
        nc5.currentPosition = j10;
        nc6.currentPosition = k10;
        for (int k14 = 0; k14 < numTexTriangles; k14++) {
            int i15 = O[k14] & 0xff;
            if (i15 == 0) {
                texTrianglesPoint1[k14] = nc1.getShort();
                texTrianglesPoint2[k14] = nc1.getShort();
                texTrianglesPoint3[k14] = nc1.getShort();
            }
            if (i15 == 1) {
                texTrianglesPoint1[k14] = nc2.getShort();
                texTrianglesPoint2[k14] = nc2.getShort();
                texTrianglesPoint3[k14] = nc2.getShort();
                kb[k14] = nc3.getShort();
                N[k14] = nc3.getShort();
                y[k14] = nc3.getShort();
                gb[k14] = nc4.getSignedByte();
                lb[k14] = nc5.getSignedByte();
                F[k14] = nc6.getSignedByte();
            }
            if (i15 == 2) {
                texTrianglesPoint1[k14] = nc2.getShort();
                texTrianglesPoint2[k14] = nc2.getShort();
                texTrianglesPoint3[k14] = nc2.getShort();
                kb[k14] = nc3.getShort();
                N[k14] = nc3.getShort();
                y[k14] = nc3.getShort();
                gb[k14] = nc4.getSignedByte();
                lb[k14] = nc5.getSignedByte();
                F[k14] = nc6.getSignedByte();
                cb[k14] = nc6.getSignedByte();
                J[k14] = nc6.getSignedByte();
            }
            if (i15 == 3) {
                texTrianglesPoint1[k14] = nc2.getShort();
                texTrianglesPoint2[k14] = nc2.getShort();
                texTrianglesPoint3[k14] = nc2.getShort();
                kb[k14] = nc3.getShort();
                N[k14] = nc3.getShort();
                y[k14] = nc3.getShort();
                gb[k14] = nc4.getSignedByte();
                lb[k14] = nc5.getSignedByte();
                F[k14] = nc6.getSignedByte();
            }
        }
        if (i2 != 255) {
            for (int i12 = 0; i12 < numTriangles; i12++) {
                trianglePriorities[i12] = i2;
            }
        }
        triangleColorValues = triangleColours2;
        vertexCount = numVertices;
        triangleCount = numTriangles;
        verticesX = vertexX;
        verticesY = vertexY;
        verticesZ = vertexZ;
        trianglePointsX = facePoint1;
        trianglePointsY = facePoint2;
        trianglePointsZ = facePoint3;
    }

    public void scale2(int i) {
        for (int i1 = 0; i1 < vertexCount; i1++) {
            verticesX[i1] = verticesX[i1] / i;
            verticesY[i1] = verticesY[i1] / i;
            verticesZ[i1] = verticesZ[i1] / i;
        }
    }

    public void read622Model(byte[] modelData, int modelId, boolean osrs) {
        RSStream nc1 = new RSStream(modelData);
        RSStream nc2 = new RSStream(modelData);
        RSStream nc3 = new RSStream(modelData);
        RSStream nc4 = new RSStream(modelData);
        RSStream nc5 = new RSStream(modelData);
        RSStream nc6 = new RSStream(modelData);
        RSStream nc7 = new RSStream(modelData);
        nc1.currentPosition = modelData.length - 23;
        int numVertices = nc1.getShort();
        int numTriangles = nc1.getShort();
        int numTexTriangles = nc1.getByte();
        ModelHeader modelHeader = null;
        if (osrs) {
            modelHeader = osrsModelHeaders[modelId] = new ModelHeader(true);
        } else {
            modelHeader = modelHeaders[modelId] = new ModelHeader(false);
        }
        modelHeader.modelData = modelData;
        modelHeader.vertexCount = numVertices;
        modelHeader.triangleCount = numTriangles;
        modelHeader.texturedTriangleCount = numTexTriangles;
        int l1 = nc1.getByte();
        boolean bool = (0x1 & l1 ^ 0xffffffff) == -2;
        boolean bool_26_ = (0x8 & l1) == 8;
        if (!bool_26_) {
            read525Model(modelData, modelId, osrs);
            return;
        }
        int newFormat = 0;
        if (bool_26_) {
            nc1.currentPosition -= 7;
            newFormat = nc1.getByte();
            nc1.currentPosition += 6;
        }
        if (newFormat == 15) {
            if (osrs) {
                OSRS_NEW_MODELS[modelId] = true;
            } else {
                NEW_MODELS[modelId] = true;
            }
        }
        int i2 = nc1.getByte();
        int j2 = nc1.getByte();
        int k2 = nc1.getByte();
        int l2 = nc1.getByte();
        int i3 = nc1.getByte();
        int j3 = nc1.getShort();
        int k3 = nc1.getShort();
        int l3 = nc1.getShort();
        int i4 = nc1.getShort();
        int j4 = nc1.getShort();
        int k4 = 0;
        int l4 = 0;
        int i5 = 0;
        byte[] x = null;
        byte[] O = null;
        byte[] J = null;
        byte[] F = null;
        byte[] cb = null;
        byte[] gb = null;
        byte[] lb = null;
        int[] kb = null;
        int[] y = null;
        int[] N = null;
        short[] D = null;
        int[] triangleColours2 = new int[numTriangles];
        if (numTexTriangles > 0) {
            O = new byte[numTexTriangles];
            nc1.currentPosition = 0;
            for (int j5 = 0; j5 < numTexTriangles; j5++) {
                byte byte0 = O[j5] = nc1.getSignedByte();
                if (byte0 == 0) {
                    k4++;
                }
                if (byte0 >= 1 && byte0 <= 3) {
                    l4++;
                }
                if (byte0 == 2) {
                    i5++;
                }
            }
        }
        int k5 = numTexTriangles;
        int l5 = k5;
        k5 += numVertices;
        int i6 = k5;
        if (bool) {
            k5 += numTriangles;
        }
        if (l1 == 1) {
            k5 += numTriangles;
        }
        int j6 = k5;
        k5 += numTriangles;
        int k6 = k5;
        if (i2 == 255) {
            k5 += numTriangles;
        }
        int l6 = k5;
        if (k2 == 1) {
            k5 += numTriangles;
        }
        int i7 = k5;
        if (i3 == 1) {
            k5 += numVertices;
        }
        int j7 = k5;
        if (j2 == 1) {
            k5 += numTriangles;
        }
        int k7 = k5;
        k5 += i4;
        int l7 = k5;
        if (l2 == 1) {
            k5 += numTriangles * 2;
        }
        int i8 = k5;
        k5 += j4;
        int j8 = k5;
        k5 += numTriangles * 2;
        int k8 = k5;
        k5 += j3;
        int l8 = k5;
        k5 += k3;
        int i9 = k5;
        k5 += l3;
        int j9 = k5;
        k5 += k4 * 6;
        int k9 = k5;
        k5 += l4 * 6;
        int i_59_ = 6;
        if (newFormat != 14) {
            if (newFormat >= 15) {
                i_59_ = 9;
            }
        } else {
            i_59_ = 7;
        }
        int l9 = k5;
        k5 += i_59_ * l4;
        int i10 = k5;
        k5 += l4;
        int j10 = k5;
        k5 += l4;
        int k10 = k5;
        k5 += l4 + i5 * 2;
        int[] vertexX = new int[numVertices];
        int[] vertexY = new int[numVertices];
        int[] vertexZ = new int[numVertices];
        int[] facePoint1 = new int[numTriangles];
        int[] facePoint2 = new int[numTriangles];
        int[] facePoint3 = new int[numTriangles];
        vertexSkins = new int[numVertices];
        texturePoints = new int[numTriangles];
        trianglePriorities = new int[numTriangles];
        triangleAlphaValues = new int[numTriangles];
        triangleSkinValues = new int[numTriangles];
        if (i3 == 1) {
            vertexSkins = new int[numVertices];
        }
        if (bool) {
            texturePoints = new int[numTriangles];
        }
        if (i2 == 255) {
            trianglePriorities = new int[numTriangles];
        }
        if (j2 == 1) {
            triangleAlphaValues = new int[numTriangles];
        }
        if (k2 == 1) {
            triangleSkinValues = new int[numTriangles];
        }
        if (l2 == 1) {
            D = new short[numTriangles];
        }
        if (l2 == 1 && numTexTriangles > 0) {
            x = new byte[numTriangles];
        }
        triangleColours2 = new int[numTriangles];
        int[] texTrianglesPoint1 = null;
        int[] texTrianglesPoint2 = null;
        int[] texTrianglesPoint3 = null;
        if (numTexTriangles > 0) {
            texTrianglesPoint1 = new int[numTexTriangles];
            texTrianglesPoint2 = new int[numTexTriangles];
            texTrianglesPoint3 = new int[numTexTriangles];
            if (l4 > 0) {
                kb = new int[l4];
                N = new int[l4];
                y = new int[l4];
                gb = new byte[l4];
                lb = new byte[l4];
                F = new byte[l4];
            }
            if (i5 > 0) {
                cb = new byte[i5];
                J = new byte[i5];
            }
        }
        nc1.currentPosition = l5;
        nc2.currentPosition = k8;
        nc3.currentPosition = l8;
        nc4.currentPosition = i9;
        nc5.currentPosition = i7;
        int l10 = 0;
        int i11 = 0;
        int j11 = 0;
        for (int k11 = 0; k11 < numVertices; k11++) {
            int l11 = nc1.getByte();
            int j12 = 0;
            if ((l11 & 1) != 0) {
                j12 = nc2.getSmartA();
            }
            int l12 = 0;
            if ((l11 & 2) != 0) {
                l12 = nc3.getSmartA();
            }
            int j13 = 0;
            if ((l11 & 4) != 0) {
                j13 = nc4.getSmartA();
            }
            vertexX[k11] = l10 + j12;
            vertexY[k11] = i11 + l12;
            vertexZ[k11] = j11 + j13;
            l10 = vertexX[k11];
            i11 = vertexY[k11];
            j11 = vertexZ[k11];
            if (vertexSkins != null) {
                vertexSkins[k11] = nc5.getByte();
            }
        }
        nc1.currentPosition = j8;
        nc2.currentPosition = i6;
        nc3.currentPosition = k6;
        nc4.currentPosition = j7;
        nc5.currentPosition = l6;
        nc6.currentPosition = l7;
        nc7.currentPosition = i8;
        for (int i12 = 0; i12 < numTriangles; i12++) {
            triangleColours2[i12] = nc1.getShort();
            if (l1 == 1) {
                texturePoints[i12] = nc2.getSignedByte();
                if (texturePoints[i12] == 2) {
                    triangleColours2[i12] = 65535;
                }
                texturePoints[i12] = 0;
            }
            if (i2 == 255) {
                trianglePriorities[i12] = nc3.getSignedByte();
            }
            if (j2 == 1) {
                triangleAlphaValues[i12] = nc4.getSignedByte();
                if (triangleAlphaValues[i12] < 0) {
                    triangleAlphaValues[i12] = (256 + triangleAlphaValues[i12]);
                }
            }
            if (k2 == 1) {
                triangleSkinValues[i12] = nc5.getByte();
            }
            if (l2 == 1) {
                D[i12] = (short) (nc6.getShort() - 1);
            }
            if (x != null) {
                if (D[i12] != -1) {
                    x[i12] = (byte) (nc7.getByte() - 1);
                } else {
                    x[i12] = -1;
                }
            }
        }
        nc1.currentPosition = k7;
        nc2.currentPosition = j6;
        int k12 = 0;
        int i13 = 0;
        int k13 = 0;
        int l13 = 0;
        for (int i14 = 0; i14 < numTriangles; i14++) {
            int j14 = nc2.getByte();
            if (j14 == 1) {
                k12 = nc1.getSmartA() + l13;
                l13 = k12;
                i13 = nc1.getSmartA() + l13;
                l13 = i13;
                k13 = nc1.getSmartA() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
            if (j14 == 2) {
                i13 = k13;
                k13 = nc1.getSmartA() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
            if (j14 == 3) {
                k12 = k13;
                k13 = nc1.getSmartA() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
            if (j14 == 4) {
                int l14 = k12;
                k12 = i13;
                i13 = l14;
                k13 = nc1.getSmartA() + l13;
                l13 = k13;
                facePoint1[i14] = k12;
                facePoint2[i14] = i13;
                facePoint3[i14] = k13;
            }
        }
        nc1.currentPosition = j9;
        nc2.currentPosition = k9;
        nc3.currentPosition = l9;
        nc4.currentPosition = i10;
        nc5.currentPosition = j10;
        nc6.currentPosition = k10;
        for (int k14 = 0; k14 < numTexTriangles; k14++) {
            int i15 = O[k14] & 0xff;
            if (i15 == 0) {
                texTrianglesPoint1[k14] = nc1.getShort();
                texTrianglesPoint2[k14] = nc1.getShort();
                texTrianglesPoint3[k14] = nc1.getShort();
            }
            if (i15 == 1) {
                texTrianglesPoint1[k14] = nc2.getShort();
                texTrianglesPoint2[k14] = nc2.getShort();
                texTrianglesPoint3[k14] = nc2.getShort();
                if (newFormat < 15) {
                    kb[k14] = nc3.getShort();
                    if (newFormat >= 14) {
                        N[k14] = nc3.get24BitInt(-1);
                    } else {
                        N[k14] = nc3.getShort();
                    }
                    y[k14] = nc3.getShort();
                } else {
                    kb[k14] = nc3.get24BitInt(-1);
                    N[k14] = nc3.get24BitInt(-1);
                    y[k14] = nc3.get24BitInt(-1);
                }
                gb[k14] = nc4.getSignedByte();
                lb[k14] = nc5.getSignedByte();
                F[k14] = nc6.getSignedByte();
            }
            if (i15 == 2) {
                texTrianglesPoint1[k14] = nc2.getShort();
                texTrianglesPoint2[k14] = nc2.getShort();
                texTrianglesPoint3[k14] = nc2.getShort();
                if (newFormat >= 15) {
                    kb[k14] = nc3.get24BitInt(-1);
                    N[k14] = nc3.get24BitInt(-1);
                    y[k14] = nc3.get24BitInt(-1);
                } else {
                    kb[k14] = nc3.getShort();
                    if (newFormat < 14) {
                        N[k14] = nc3.getShort();
                    } else {
                        N[k14] = nc3.get24BitInt(-1);
                    }
                    y[k14] = nc3.getShort();
                }
                gb[k14] = nc4.getSignedByte();
                lb[k14] = nc5.getSignedByte();
                F[k14] = nc6.getSignedByte();
                cb[k14] = nc6.getSignedByte();
                J[k14] = nc6.getSignedByte();
            }
            if (i15 == 3) {
                texTrianglesPoint1[k14] = nc2.getShort();
                texTrianglesPoint2[k14] = nc2.getShort();
                texTrianglesPoint3[k14] = nc2.getShort();
                if (newFormat < 15) {
                    kb[k14] = nc3.getShort();
                    if (newFormat < 14) {
                        N[k14] = nc3.getShort();
                    } else {
                        N[k14] = nc3.get24BitInt(-1);
                    }
                    y[k14] = nc3.getShort();
                } else {
                    kb[k14] = nc3.get24BitInt(-1);
                    N[k14] = nc3.get24BitInt(-1);
                    y[k14] = nc3.get24BitInt(-1);
                }
                gb[k14] = nc4.getSignedByte();
                lb[k14] = nc5.getSignedByte();
                F[k14] = nc6.getSignedByte();
            }
        }
        if (i2 != 255) {
            for (int i12 = 0; i12 < numTriangles; i12++) {
                trianglePriorities[i12] = i2;
            }
        }
        triangleColorValues = triangleColours2;
        vertexCount = numVertices;
        triangleCount = numTriangles;
        this.verticesX = vertexX;
        this.verticesY = vertexY;
        this.verticesZ = vertexZ;
        trianglePointsX = facePoint1;
        trianglePointsY = facePoint2;
        trianglePointsZ = facePoint3;
    }

    private void readOldModel(int modelId, boolean osrs) {
        int j = -870;
        aBoolean1618 = true;
        fitsOnSingleSquare = false;
        anInt1620++;
        ModelHeader modelHeader = osrs ? osrsModelHeaders[modelId] : modelHeaders[modelId];
        vertexCount = modelHeader.vertexCount;
        triangleCount = modelHeader.triangleCount;
        anInt1642 = modelHeader.texturedTriangleCount;
        verticesX = new int[vertexCount];
        verticesY = new int[vertexCount];
        verticesZ = new int[vertexCount];
        trianglePointsX = new int[triangleCount];
        trianglePointsY = new int[triangleCount];
        while (j >= 0) {
            aBoolean1618 = !aBoolean1618;
        }
        trianglePointsZ = new int[triangleCount];
        texturesFaceA = new int[anInt1642];
        texturesFaceB = new int[anInt1642];
        texturesFaceC = new int[anInt1642];
        if (modelHeader.anInt376 >= 0) {
            vertexSkins = new int[vertexCount];
        }
        if (modelHeader.anInt380 >= 0) {
            texturePoints = new int[triangleCount];
        }
        if (modelHeader.anInt381 >= 0) {
            trianglePriorities = new int[triangleCount];
        } else {
            anInt1641 = -modelHeader.anInt381 - 1;
        }
        if (modelHeader.anInt382 >= 0) {
            triangleAlphaValues = new int[triangleCount];
        }
        if (modelHeader.anInt383 >= 0) {
            triangleSkinValues = new int[triangleCount];
        }
        triangleColorValues = new int[triangleCount];
        RSStream stream = new RSStream(modelHeader.modelData);
        stream.currentPosition = modelHeader.anInt372;
        RSStream stream_1 = new RSStream(modelHeader.modelData);
        stream_1.currentPosition = modelHeader.anInt373;
        RSStream stream_2 = new RSStream(modelHeader.modelData);
        stream_2.currentPosition = modelHeader.anInt374;
        RSStream stream_3 = new RSStream(modelHeader.modelData);
        stream_3.currentPosition = modelHeader.anInt375;
        RSStream stream_4 = new RSStream(modelHeader.modelData);
        stream_4.currentPosition = modelHeader.anInt376;
        int k = 0;
        int l = 0;
        int i1 = 0;
        for (int j1 = 0; j1 < vertexCount; j1++) {
            int k1 = stream.getByte();
            int i2 = 0;
            if ((k1 & 1) != 0) {
                i2 = stream_1.getSmartA();
            }
            int k2 = 0;
            if ((k1 & 2) != 0) {
                k2 = stream_2.getSmartA();
            }
            int i3 = 0;
            if ((k1 & 4) != 0) {
                i3 = stream_3.getSmartA();
            }
            verticesX[j1] = k + i2;
            verticesY[j1] = l + k2;
            verticesZ[j1] = i1 + i3;
            k = verticesX[j1];
            l = verticesY[j1];
            i1 = verticesZ[j1];
            if (vertexSkins != null) {
                vertexSkins[j1] = stream_4.getByte();
            }
        }
        stream.currentPosition = modelHeader.anInt379;
        stream_1.currentPosition = modelHeader.anInt380;
        stream_2.currentPosition = modelHeader.anInt381;
        stream_3.currentPosition = modelHeader.anInt382;
        stream_4.currentPosition = modelHeader.anInt383;
        for (int l1 = 0; l1 < triangleCount; l1++) {
            triangleColorValues[l1] = stream.getShort();
            if (texturePoints != null) {
                texturePoints[l1] = stream_1.getByte();
            }
            if (trianglePriorities != null) {
                trianglePriorities[l1] = stream_2.getByte();
            }
            if (triangleAlphaValues != null) {
                triangleAlphaValues[l1] = stream_3.getByte();
            }
            if (triangleSkinValues != null) {
                triangleSkinValues[l1] = stream_4.getByte();
            }
        }
        stream.currentPosition = modelHeader.anInt377;
        stream_1.currentPosition = modelHeader.anInt378;
        int j2 = 0;
        int l2 = 0;
        int j3 = 0;
        int k3 = 0;
        for (int l3 = 0; l3 < triangleCount; l3++) {
            int i4 = stream_1.getByte();
            if (i4 == 1) {
                j2 = stream.getSmartA() + k3;
                k3 = j2;
                l2 = stream.getSmartA() + k3;
                k3 = l2;
                j3 = stream.getSmartA() + k3;
                k3 = j3;
                trianglePointsX[l3] = j2;
                trianglePointsY[l3] = l2;
                trianglePointsZ[l3] = j3;
            }
            if (i4 == 2) {
                l2 = j3;
                j3 = stream.getSmartA() + k3;
                k3 = j3;
                trianglePointsX[l3] = j2;
                trianglePointsY[l3] = l2;
                trianglePointsZ[l3] = j3;
            }
            if (i4 == 3) {
                j2 = j3;
                j3 = stream.getSmartA() + k3;
                k3 = j3;
                trianglePointsX[l3] = j2;
                trianglePointsY[l3] = l2;
                trianglePointsZ[l3] = j3;
            }
            if (i4 == 4) {
                int k4 = j2;
                j2 = l2;
                l2 = k4;
                j3 = stream.getSmartA() + k3;
                k3 = j3;
                trianglePointsX[l3] = j2;
                trianglePointsY[l3] = l2;
                trianglePointsZ[l3] = j3;
            }
        }
        stream.currentPosition = modelHeader.anInt384;
        for (int j4 = 0; j4 < anInt1642; j4++) {
            texturesFaceA[j4] = stream.getShort();
            texturesFaceB[j4] = stream.getShort();
            texturesFaceC[j4] = stream.getShort();
        }
    }

    public void method464(Model model, boolean flag) {
        vertexCount = model.vertexCount;
        triangleCount = model.triangleCount;
        anInt1642 = model.anInt1642;
        if (anIntArray1622.length < vertexCount) {
            anIntArray1622 = new int[vertexCount + 10000];
            anIntArray1623 = new int[vertexCount + 10000];
            anIntArray1624 = new int[vertexCount + 10000];
        }
        verticesX = anIntArray1622;
        verticesY = anIntArray1623;
        verticesZ = anIntArray1624;
        for (int k = 0; k < vertexCount; k++) {
            verticesX[k] = model.verticesX[k];
            verticesY[k] = model.verticesY[k];
            verticesZ[k] = model.verticesZ[k];
        }

        if (flag) {
            triangleAlphaValues = model.triangleAlphaValues;
        } else {
            if (anIntArray1625.length < triangleCount) {
                anIntArray1625 = new int[triangleCount + 100];
            }
            triangleAlphaValues = anIntArray1625;
            if (model.triangleAlphaValues == null) {
                for (int l = 0; l < triangleCount; l++) {
                    triangleAlphaValues[l] = 0;
                }

            } else {
                for (int i1 = 0; i1 < triangleCount; i1++) {
                    triangleAlphaValues[i1] = model.triangleAlphaValues[i1];
                }

            }
        }
        texturePoints = model.texturePoints;
        triangleColorValues = model.triangleColorValues;
        trianglePriorities = model.trianglePriorities;
        anInt1641 = model.anInt1641;
        faceGroups = model.faceGroups;
        vertexGroups = model.vertexGroups;
        trianglePointsX = model.trianglePointsX;
        trianglePointsY = model.trianglePointsY;
        trianglePointsZ = model.trianglePointsZ;
        faceShadeA = model.faceShadeA;
        faceShadeB = model.faceShadeB;
        faceShadeC = model.faceShadeC;
        texturesFaceA = model.texturesFaceA;
        texturesFaceB = model.texturesFaceB;
        texturesFaceC = model.texturesFaceC;
    }

    private int method465(Model model, int i) {
        int j = -1;
        int k = model.verticesX[i];
        int l = model.verticesY[i];
        int i1 = model.verticesZ[i];
        for (int j1 = 0; j1 < vertexCount; j1++) {
            if (k != verticesX[j1] || l != verticesY[j1]
                    || i1 != verticesZ[j1]) {
                continue;
            }
            j = j1;
            break;
        }

        if (j == -1) {
            verticesX[vertexCount] = k;
            verticesY[vertexCount] = l;
            verticesZ[vertexCount] = i1;
            if (model.vertexSkins != null) {
                vertexSkins[vertexCount] = model.vertexSkins[i];
            }
            j = vertexCount++;
        }
        return j;
    }

    public void method466() {
        super.modelHeight = 0;
        anInt1650 = 0;
        anInt1651 = 0;
        for (int i = 0; i < vertexCount; i++) {
            int j = verticesX[i];
            int k = verticesY[i];
            int l = verticesZ[i];
            if (-k > super.modelHeight) {
                super.modelHeight = -k;
            }
            if (k > anInt1651) {
                anInt1651 = k;
            }
            int i1 = j * j + l * l;
            if (i1 > anInt1650) {
                anInt1650 = i1;
            }
        }
        anInt1650 = (int) (Math.sqrt(anInt1650) + 0.98999999999999999D);
        anInt1653 = (int) (Math.sqrt(anInt1650 * anInt1650 + super.modelHeight
                * super.modelHeight) + 0.98999999999999999D);
        anInt1652 = anInt1653
                + (int) (Math.sqrt(anInt1650 * anInt1650 + anInt1651
                * anInt1651) + 0.98999999999999999D);
    }

    public void computeSphericalBounds() {
        super.modelHeight = 0;
        anInt1651 = 0;
        for (int i = 0; i < vertexCount; i++) {
            int j = verticesY[i];
            if (-j > super.modelHeight) {
                super.modelHeight = -j;
            }
            if (j > anInt1651) {
                anInt1651 = j;
            }
        }
        anInt1653 = (int) (Math.sqrt(anInt1650 * anInt1650 + super.modelHeight
                * super.modelHeight) + 0.98999999999999999D);
        anInt1652 = anInt1653
                + (int) (Math.sqrt(anInt1650 * anInt1650 + anInt1651
                * anInt1651) + 0.98999999999999999D);
    }

    public void method468(int i) {
        super.modelHeight = 0;
        anInt1650 = 0;
        anInt1651 = 0;
        anInt1646 = 0xf423f;
        anInt1647 = 0xfff0bdc1;
        anInt1648 = 0xfffe7961;
        anInt1649 = 0x1869f;
        for (int j = 0; j < vertexCount; j++) {
            int k = verticesX[j];
            int l = verticesY[j];
            int i1 = verticesZ[j];
            if (k < anInt1646) {
                anInt1646 = k;
            }
            if (k > anInt1647) {
                anInt1647 = k;
            }
            if (i1 < anInt1649) {
                anInt1649 = i1;
            }
            if (i1 > anInt1648) {
                anInt1648 = i1;
            }
            if (-l > super.modelHeight) {
                super.modelHeight = -l;
            }
            if (l > anInt1651) {
                anInt1651 = l;
            }
            int j1 = k * k + i1 * i1;
            if (j1 > anInt1650) {
                anInt1650 = j1;
            }
        }

        anInt1650 = (int) Math.sqrt(anInt1650);
        anInt1653 = (int) Math.sqrt(anInt1650 * anInt1650 + super.modelHeight
                * super.modelHeight);
        if (i != 21073) {
            return;
        } else {
            anInt1652 = anInt1653
                    + (int) Math.sqrt(anInt1650 * anInt1650 + anInt1651
                    * anInt1651);
            return;
        }
    }

    public void skin() {
        if (vertexSkins != null) {
            int ai[] = new int[256];
            int j = 0;
            for (int l = 0; l < vertexCount; l++) {
                int j1 = vertexSkins[l];
                ai[j1]++;
                if (j1 > j) {
                    j = j1;
                }
            }

            vertexGroups = new int[j + 1][];
            for (int k1 = 0; k1 <= j; k1++) {
                vertexGroups[k1] = new int[ai[k1]];
                ai[k1] = 0;
            }

            for (int j2 = 0; j2 < vertexCount; j2++) {
                int l2 = vertexSkins[j2];
                vertexGroups[l2][ai[l2]++] = j2;
            }

            vertexSkins = null;
        }
        if (triangleSkinValues != null) {
            int ai1[] = new int[256];
            int k = 0;
            for (int i1 = 0; i1 < triangleCount; i1++) {
                int l1 = triangleSkinValues[i1];
                ai1[l1]++;
                if (l1 > k) {
                    k = l1;
                }
            }

            faceGroups = new int[k + 1][];
            for (int i2 = 0; i2 <= k; i2++) {
                faceGroups[i2] = new int[ai1[i2]];
                ai1[i2] = 0;
            }

            for (int k2 = 0; k2 < triangleCount; k2++) {
                int i3 = triangleSkinValues[k2];
                faceGroups[i3][ai1[i3]++] = k2;
            }

            triangleSkinValues = null;
        }
    }

    public void method470(int frame, int nextFrame, int end, int cycle, boolean osrs) {
        if (Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.TWEENING)) {
            apply(frame, osrs);
            return;
        }
        if (vertexGroups != null && frame != -1) {
            SequenceFrame currentAnimation = SequenceFrame.fetchFileInfo(frame, osrs);
            if (currentAnimation == null) {
                return;
            }
            SkinList skinList = currentAnimation.skinList;
            anInt1681 = 0;
            anInt1682 = 0;
            anInt1683 = 0;
            SequenceFrame nextAnimation = null;
            SkinList list2 = null;
            if (nextFrame != -1) {
                nextAnimation = SequenceFrame.fetchFileInfo(nextFrame, osrs);
                if (nextAnimation.skinList != skinList) {
                    nextAnimation = null;
                }
                list2 = nextAnimation.skinList;
            }
            if (nextAnimation == null || list2 == null) {
                for (int i_263_ = 0; i_263_ < currentAnimation.anInt638; i_263_++) {
                    int i_264_ = currentAnimation.anIntArray639[i_263_];
                    method472(skinList.anIntArray342[i_264_], skinList.anIntArrayArray343[i_264_],
                            currentAnimation.anIntArray640[i_263_],
                            currentAnimation.anIntArray641[i_263_],
                            currentAnimation.anIntArray642[i_263_]);

                }
            } else {
                for (int i1 = 0; i1 < currentAnimation.anInt638; i1++) {
                    int n1 = currentAnimation.anIntArray639[i1];
                    int opcode = skinList.anIntArray342[n1];
                    int[] skin = skinList.anIntArrayArray343[n1];
                    int x = currentAnimation.anIntArray640[i1];
                    int y = currentAnimation.anIntArray641[i1];
                    int z = currentAnimation.anIntArray642[i1];
                    boolean found = false;
                    for (int i2 = 0; i2 < nextAnimation.anInt638; i2++) {
                        int n2 = nextAnimation.anIntArray639[i2];
                        if (list2.anIntArrayArray343[n2].equals(skin)) {
                            if (opcode != 2) {
                                x += (nextAnimation.anIntArray640[i2] - x) * cycle
                                        / end;
                                y += (nextAnimation.anIntArray641[i2] - y) * cycle
                                        / end;
                                z += (nextAnimation.anIntArray642[i2] - z) * cycle
                                        / end;
                            } else {
                                x &= 0xff;
                                y &= 0xff;
                                z &= 0xff;
                                int dx = nextAnimation.anIntArray640[i2] - x & 0xff;
                                int dy = nextAnimation.anIntArray641[i2] - y & 0xff;
                                int dz = nextAnimation.anIntArray642[i2] - z & 0xff;
                                if (dx >= 128) {
                                    dx -= 256;
                                }
                                if (dy >= 128) {
                                    dy -= 256;
                                }
                                if (dz >= 128) {
                                    dz -= 256;
                                }
                                x = x + dx * cycle / end & 0xff;
                                y = y + dy * cycle / end & 0xff;
                                z = z + dz * cycle / end & 0xff;
                            }
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        if (opcode != 3 && opcode != 2) {
                            x = x * (end - cycle) / end;
                            y = y * (end - cycle) / end;
                            z = z * (end - cycle) / end;
                        } else if (opcode == 3) {
                            x = (x * (end - cycle) + (cycle << 7)) / end;
                            y = (y * (end - cycle) + (cycle << 7)) / end;
                            z = (z * (end - cycle) + (cycle << 7)) / end;
                        } else {
                            x &= 0xff;
                            y &= 0xff;
                            z &= 0xff;
                            int dx = -x & 0xff;
                            int dy = -y & 0xff;
                            int dz = -z & 0xff;
                            if (dx >= 128) {
                                dx -= 256;
                            }
                            if (dy >= 128) {
                                dy -= 256;
                            }
                            if (dz >= 128) {
                                dz -= 256;
                            }
                            x = x + dx * cycle / end & 0xff;
                            y = y + dy * cycle / end & 0xff;
                            z = z + dz * cycle / end & 0xff;
                        }
                    }
                    method472(opcode, skin, x, y, z);
                }
            }
        }
    }

    public void apply(int i, boolean osrs) {
        if (vertexGroups == null) {
            return;
        }
        if (i == -1) {
            return;
        }
        SequenceFrame sequenceFrame = SequenceFrame.fetchFileInfo(i, osrs);
        if (sequenceFrame == null) {
            return;
        }
        SkinList class18 = sequenceFrame.skinList;
        anInt1681 = 0;
        anInt1682 = 0;
        anInt1683 = 0;
        for (int k = 0; k < sequenceFrame.anInt638; k++) {
            int l = sequenceFrame.anIntArray639[k];
            method472(class18.anIntArray342[l], class18.anIntArrayArray343[l],
                    sequenceFrame.anIntArray640[k], sequenceFrame.anIntArray641[k],
                    sequenceFrame.anIntArray642[k]);
        }

    }

    public void method471(int[] ai, int j, int k, boolean osrs) {
        if (k == -1) {
            return;
        }
        if (ai == null || j == -1) {
            apply(k, osrs);
            return;
        }
        SequenceFrame class36 = SequenceFrame.fetchFileInfo(k, osrs);
        if (class36 == null) {
            return;
        }
        SequenceFrame class36_1 = SequenceFrame.fetchFileInfo(j, osrs);
        if (class36_1 == null) {
            apply(k, osrs);
            return;
        }
        SkinList class18 = class36.skinList;
        anInt1681 = 0;
        anInt1682 = 0;
        anInt1683 = 0;
        int l = 0;
        if (ai == null || ai.length == 0) {
            return;
        }
        int i1 = ai[l++];
        for (int j1 = 0; j1 < class36.anInt638; j1++) {
            int k1;
            for (k1 = class36.anIntArray639[j1]; k1 > i1; i1 = ai[l++]) {
                ;
            }
            if (k1 != i1 || class18.anIntArray342[k1] == 0) {
                method472(class18.anIntArray342[k1],
                        class18.anIntArrayArray343[k1],
                        class36.anIntArray640[j1], class36.anIntArray641[j1],
                        class36.anIntArray642[j1]);
            }
        }

        anInt1681 = 0;
        anInt1682 = 0;
        anInt1683 = 0;
        l = 0;
        i1 = ai[l++];
        for (int l1 = 0; l1 < class36_1.anInt638; l1++) {
            int i2;
            for (i2 = class36_1.anIntArray639[l1]; i2 > i1; i1 = ai[l++]) {
                ;
            }
            if (i2 == i1 || class18.anIntArray342[i2] == 0) {
                method472(class18.anIntArray342[i2],
                        class18.anIntArrayArray343[i2],
                        class36_1.anIntArray640[l1],
                        class36_1.anIntArray641[l1],
                        class36_1.anIntArray642[l1]);
            }
        }

    }

    private void method472(int i, int ai[], int j, int k, int l) {
        int i1 = ai.length;
        if (i == 0) {
            int j1 = 0;
            anInt1681 = 0;
            anInt1682 = 0;
            anInt1683 = 0;
            for (int k2 = 0; k2 < i1; k2++) {
                int l3 = ai[k2];
                if (l3 < vertexGroups.length) {
                    int ai5[] = vertexGroups[l3];
                    for (int i5 = 0; i5 < ai5.length; i5++) {
                        int j6 = ai5[i5];
                        anInt1681 += verticesX[j6];
                        anInt1682 += verticesY[j6];
                        anInt1683 += verticesZ[j6];
                        j1++;
                    }

                }
            }

            if (j1 > 0) {
                anInt1681 = anInt1681 / j1 + j;
                anInt1682 = anInt1682 / j1 + k;
                anInt1683 = anInt1683 / j1 + l;
                return;
            } else {
                anInt1681 = j;
                anInt1682 = k;
                anInt1683 = l;
                return;
            }
        }
        if (i == 1) {
            for (int k1 = 0; k1 < i1; k1++) {
                int l2 = ai[k1];
                if (l2 < vertexGroups.length) {
                    int ai1[] = vertexGroups[l2];
                    for (int i4 = 0; i4 < ai1.length; i4++) {
                        int j5 = ai1[i4];
                        verticesX[j5] += j;
                        verticesY[j5] += k;
                        verticesZ[j5] += l;
                    }

                }
            }

            return;
        }
        if (i == 2) {
            for (int l1 = 0; l1 < i1; l1++) {
                int i3 = ai[l1];
                if (i3 < vertexGroups.length) {
                    int ai2[] = vertexGroups[i3];
                    for (int j4 = 0; j4 < ai2.length; j4++) {
                        int k5 = ai2[j4];
                        verticesX[k5] -= anInt1681;
                        verticesY[k5] -= anInt1682;
                        verticesZ[k5] -= anInt1683;
                        int k6 = (j & 0xff) * 8;
                        int l6 = (k & 0xff) * 8;
                        int i7 = (l & 0xff) * 8;
                        if (i7 != 0) {
                            int j7 = SINE[i7];
                            int i8 = COSINE[i7];
                            int l8 = verticesY[k5] * j7 + verticesX[k5] * i8 >> 16;
                            verticesY[k5] = verticesY[k5] * i8 - verticesX[k5] * j7 >> 16;
                            verticesX[k5] = l8;
                        }
                        if (k6 != 0) {
                            int k7 = SINE[k6];
                            int j8 = COSINE[k6];
                            int i9 = verticesY[k5] * j8 - verticesZ[k5] * k7 >> 16;
                            verticesZ[k5] = verticesY[k5] * k7 + verticesZ[k5] * j8 >> 16;
                            verticesY[k5] = i9;
                        }
                        if (l6 != 0) {
                            int l7 = SINE[l6];
                            int k8 = COSINE[l6];
                            int j9 = verticesZ[k5] * l7 + verticesX[k5] * k8 >> 16;
                            verticesZ[k5] = verticesZ[k5] * k8 - verticesX[k5] * l7 >> 16;
                            verticesX[k5] = j9;
                        }
                        verticesX[k5] += anInt1681;
                        verticesY[k5] += anInt1682;
                        verticesZ[k5] += anInt1683;
                    }

                }
            }
            return;
        }
        if (i == 3) {
            for (int i2 = 0; i2 < i1; i2++) {
                int j3 = ai[i2];
                if (j3 < vertexGroups.length) {
                    int ai3[] = vertexGroups[j3];
                    for (int k4 = 0; k4 < ai3.length; k4++) {
                        int l5 = ai3[k4];
                        verticesX[l5] -= anInt1681;
                        verticesY[l5] -= anInt1682;
                        verticesZ[l5] -= anInt1683;
                        verticesX[l5] = (verticesX[l5] * j) / 128;
                        verticesY[l5] = (verticesY[l5] * k) / 128;
                        verticesZ[l5] = (verticesZ[l5] * l) / 128;
                        verticesX[l5] += anInt1681;
                        verticesY[l5] += anInt1682;
                        verticesZ[l5] += anInt1683;
                    }
                }
            }
            return;
        }
        if (i == 5 && faceGroups != null && triangleAlphaValues != null) {
            for (int j2 = 0; j2 < i1; j2++) {
                int k3 = ai[j2];
                if (k3 < faceGroups.length) {
                    int ai4[] = faceGroups[k3];
                    for (int l4 = 0; l4 < ai4.length; l4++) {
                        int i6 = ai4[l4];
                        triangleAlphaValues[i6] += j * 8;
                        if (triangleAlphaValues[i6] < 0) {
                            triangleAlphaValues[i6] = 0;
                        }
                        if (triangleAlphaValues[i6] > 255) {
                            triangleAlphaValues[i6] = 255;
                        }
                    }
                }
            }
        }
    }

    public void method473() {
        for (int j = 0; j < vertexCount; j++) {
            int k = verticesX[j];
            verticesX[j] = verticesZ[j];
            verticesZ[j] = -k;
        }
    }

    public void method474(int i) {
        int k = SINE[i];
        int l = COSINE[i];
        for (int i1 = 0; i1 < vertexCount; i1++) {
            int j1 = verticesY[i1] * l - verticesZ[i1] * k >> 16;
            verticesZ[i1] = verticesY[i1] * k + verticesZ[i1] * l >> 16;
            verticesY[i1] = j1;
        }
    }

    public void translate(int i, int j, int l) {
        for (int i1 = 0; i1 < vertexCount; i1++) {
            verticesX[i1] += i;
            verticesY[i1] += j;
            verticesZ[i1] += l;
        }
    }


    public void recolor(int i, int j) {
        for (int k = 0; k < triangleCount; k++) {
            if (triangleColorValues[k] == i) {
                triangleColorValues[k] = j;
            }
        }
    }

    public void retexture(int currentTexture, int newTexture) {
        for (int k = 0; k < triangleCount; k++) {
            if (triangleColorValues[k] == currentTexture) {
                triangleColorValues[k] = newTexture;
            }
        }
    }

    public void method477() {
        for (int j = 0; j < vertexCount; j++) {
            verticesZ[j] = -verticesZ[j];
        }
        for (int k = 0; k < triangleCount; k++) {
            int l = trianglePointsX[k];
            trianglePointsX[k] = trianglePointsZ[k];
            trianglePointsZ[k] = l;
        }
    }

    public void scale(int x, int y, int z) {
        scaleX(x);
        scaleY(y);
        scaleZ(z);
    }

    public void scaleT(int i, int j, int l) {
        for (int i1 = 0; i1 < vertexCount; i1++) {
            verticesX[i1] = (verticesX[i1] * i) / 128;
            verticesY[i1] = (verticesY[i1] * l) / 128;
            verticesZ[i1] = (verticesZ[i1] * j) / 128;
        }
    }


    public void transformScale(int x, int y, int z) {
        for (int i1 = 0; i1 < vertexCount; i1++) {
            verticesX[i1] += (verticesX[i1] * x) / 128;
            verticesY[i1] += (verticesY[i1] * y) / 128;
            verticesZ[i1] += (verticesZ[i1] * z) / 128;
        }
    }

    public void scaleX(int x) {
        for (int i1 = 0; i1 < vertexCount; i1++) {
            verticesX[i1] = (verticesX[i1] * x) / 128;
        }
    }

    public void scaleY(int y) {
        for (int i1 = 0; i1 < vertexCount; i1++) {
            verticesY[i1] = (verticesY[i1] * y) / 128;
        }
    }

    public void scaleZ(int z) {
        for (int i1 = 0; i1 < vertexCount; i1++) {
            verticesZ[i1] = (verticesZ[i1] * z) / 128;
        }
    }

    public final void light(int i, int j, int k, int l, int i1, boolean flag) {
        int j1 = (int) Math.sqrt(k * k + l * l + i1 * i1);
        int k1 = j * j1 >> 8;
        if (faceShadeA == null) {
            faceShadeA = new int[triangleCount];
            faceShadeB = new int[triangleCount];
            faceShadeC = new int[triangleCount];
        }
        if (super.vertexes == null) {
            super.vertexes = new Vertex[vertexCount];
            for (int l1 = 0; l1 < vertexCount; l1++) {
                super.vertexes[l1] = new Vertex();
            }

        }
        for (int i2 = 0; i2 < triangleCount; i2++) {
            if (triangleColorValues != null && triangleAlphaValues != null) {
                if (triangleColorValues[i2] == 65535 || triangleColorValues[i2] == 16705) {
                    triangleAlphaValues[i2] = 255;
                }
            }
            int j2 = trianglePointsX[i2];
            int l2 = trianglePointsY[i2];
            int i3 = trianglePointsZ[i2];
            int j3 = verticesX[l2] -
                    verticesX[j2];
            int k3 = verticesY[l2] - verticesY[j2];
            int l3 = verticesZ[l2] - verticesZ[j2];
            int i4 = verticesX[i3] - verticesX[j2];
            int j4 = verticesY[i3] - verticesY[j2];
            int k4 = verticesZ[i3] - verticesZ[j2];
            int l4 = k3 * k4 - j4 * l3;
            int i5 = l3 * i4 - k4 * j3;
            int j5;
            for (j5 = j3 * j4 - i4 * k3; l4 > 8192 || i5 > 8192 || j5 > 8192
                    || l4 < -8192 || i5 < -8192 || j5 < -8192; j5 >>= 1) {
                l4 >>= 1;
                i5 >>= 1;
            }

            int k5 = (int) Math.sqrt(l4 * l4 + i5 * i5 + j5 * j5);
            if (k5 <= 0) {
                k5 = 1;
            }
            l4 = (l4 * 256) / k5;
            i5 = (i5 * 256) / k5;
            j5 = (j5 * 256) / k5;

            if (texturePoints == null || (texturePoints[i2] & 1) == 0) {
                Vertex class33_2 = super.vertexes[j2];
                class33_2.anInt602 += l4;
                class33_2.anInt603 += i5;
                class33_2.anInt604 += j5;
                class33_2.anInt605++;
                class33_2 = super.vertexes[l2];
                class33_2.anInt602 += l4;
                class33_2.anInt603 += i5;
                class33_2.anInt604 += j5;
                class33_2.anInt605++;
                class33_2 = super.vertexes[i3];
                class33_2.anInt602 += l4;
                class33_2.anInt603 += i5;
                class33_2.anInt604 += j5;
                class33_2.anInt605++;
            } else {
                int l5 = i + (k * l4 + l * i5 + i1 * j5) / (k1 + k1 / 2);
                faceShadeA[i2] = method481(triangleColorValues[i2], l5,
                        texturePoints[i2]);
            }
        }

        if (flag) {
            method480(i, k1, k, l, i1);
        } else {
            vertexOffset = new Vertex[vertexCount];
            for (int k2 = 0; k2 < vertexCount; k2++) {
                Vertex class33 = super.vertexes[k2];
                Vertex class33_1 = vertexOffset[k2] = new Vertex();
                class33_1.anInt602 = class33.anInt602;
                class33_1.anInt603 = class33.anInt603;
                class33_1.anInt604 = class33.anInt604;
                class33_1.anInt605 = class33.anInt605;
            }

        }
        if (flag) {
            method466();
            return;
        } else {
            method468(21073);
            return;
        }
    }

    public final void method480(int i, int j, int k, int l, int i1) {
        try {
            for (int j1 = 0; j1 < triangleCount; j1++) {
                int k1 = trianglePointsX[j1];
                int i2 = trianglePointsY[j1];
                int j2 = trianglePointsZ[j1];
                if (texturePoints == null) {
                    int i3 = triangleColorValues[j1];
                    Vertex class33 = super.vertexes[k1];
                    int k2 = i
                            + (k * class33.anInt602 + l * class33.anInt603 + i1
                            * class33.anInt604) / (j * class33.anInt605);
                    faceShadeA[j1] = method481(i3, k2, 0);
                    class33 = super.vertexes[i2];
                    k2 = i
                            + (k * class33.anInt602 + l * class33.anInt603 + i1
                            * class33.anInt604) / (j * class33.anInt605);
                    faceShadeB[j1] = method481(i3, k2, 0);
                    class33 = super.vertexes[j2];
                    k2 = i
                            + (k * class33.anInt602 + l * class33.anInt603 + i1
                            * class33.anInt604) / (j * class33.anInt605);
                    faceShadeC[j1] = method481(i3, k2, 0);
                } else if ((texturePoints[j1] & 1) == 0) {
                    int j3 = triangleColorValues[j1];
                    int k3 = texturePoints[j1];
                    Vertex class33_1 = super.vertexes[k1];
                    int l2 = i
                            + (k * class33_1.anInt602 + l * class33_1.anInt603 + i1
                            * class33_1.anInt604)
                            / (j * class33_1.anInt605);
                    faceShadeA[j1] = method481(j3, l2, k3);
                    class33_1 = super.vertexes[i2];
                    l2 = i
                            + (k * class33_1.anInt602 + l * class33_1.anInt603 + i1
                            * class33_1.anInt604)
                            / (j * class33_1.anInt605);
                    faceShadeB[j1] = method481(j3, l2, k3);
                    class33_1 = super.vertexes[j2];
                    l2 = i
                            + (k * class33_1.anInt602 + l * class33_1.anInt603 + i1
                            * class33_1.anInt604)
                            / (j * class33_1.anInt605);
                    faceShadeC[j1] = method481(j3, l2, k3);
                }
            }

            super.vertexes = null;
            vertexOffset = null;
            vertexSkins = null;
            triangleSkinValues = null;
            if (texturePoints != null) {
                for (int l1 = 0; l1 < triangleCount; l1++) {
                    if ((texturePoints[l1] & 2) == 2) {
                        return;
                    }
                }

            }
            triangleColorValues = null;
        } catch (Exception ex) {
        }
    }

    public final void renderSingle(int j, int k, int l, int i1, int j1, int k1) {
        int i = 0;
        int l1 = Rasterizer.textureInt1;
        int i2 = Rasterizer.textureInt2;
        int j2 = SINE[i];
        int k2 = COSINE[i];
        int l2 = SINE[j];
        int i3 = COSINE[j];
        int j3 = SINE[k];
        int k3 = COSINE[k];
        int l3 = SINE[l];
        int i4 = COSINE[l];
        int j4 = j1 * l3 + k1 * i4 >> 16;
        for (int k4 = 0; k4 < vertexCount; k4++) {
            int l4 = verticesX[k4];
            int i5 = verticesY[k4];
            int j5 = verticesZ[k4];
            if (k != 0) {
                int k5 = i5 * j3 + l4 * k3 >> 16;
                i5 = i5 * k3 - l4 * j3 >> 16;
                l4 = k5;
            }
            if (i != 0) {
                int l5 = i5 * k2 - j5 * j2 >> 16;
                j5 = i5 * j2 + j5 * k2 >> 16;
                i5 = l5;
            }
            if (j != 0) {
                int i6 = j5 * l2 + l4 * i3 >> 16;
                j5 = j5 * i3 - l4 * l2 >> 16;
                l4 = i6;
            }
            l4 += i1;
            i5 += j1;
            j5 += k1;
            int j6 = i5 * i4 - j5 * l3 >> 16;
            j5 = i5 * l3 + j5 * i4 >> 16;
            i5 = j6;
            anIntArray1667[k4] = j5 - j4;
            projected_vertex_x[k4] = l1 + (l4 << 9) / j5;
            projected_vertex_y[k4] = i2 + (i5 << 9) / j5;
            projected_vertex_z[k4] = j5;
            if (anInt1642 > 0) {
                camera_vertex_y[k4] = l4;
                camera_vertex_x[k4] = i5;
                camera_vertex_z[k4] = j5;
            }
        }

        try {
            method483(false, false, 0, 0);
            return;
        } catch (Exception _ex) {
            return;
        }
    }

    public final void renderAtPoint(int i, int j, int k, int l, int i1, int j1,
                                    int k1, int l1, int i2, int id) {
        int j2 = l1 * i1 - j1 * l >> 16;
        int k2 = k1 * j + j2 * k >> 16;
        int l2 = anInt1650 * k >> 16;
        int i3 = k2 + l2;
        if (i3 <= (SceneGraph.mapViewDistance << 1) || k2 >= 3500 * 2) {
            return;
        }
        int j3 = l1 * l + j1 * i1 >> 16;
        int k3 = j3 - anInt1650 << SceneGraph.viewDistance;
        if (k3 / i3 >= Raster.viewport_centerX) {
            return;
        }
        int l3 = j3 + anInt1650 << SceneGraph.viewDistance;
        if (l3 / i3 <= -Raster.viewport_centerX) {
            return;
        }
        int i4 = k1 * k - j2 * j >> 16;
        int j4 = anInt1650 * j >> 16;
        int k4 = i4 + j4 << SceneGraph.viewDistance;
        if (k4 / i3 <= -Raster.anInt1387) {
            return;
        }
        int l4 = j4 + (super.modelHeight * k >> 16);
        int i5 = i4 - l4 << SceneGraph.viewDistance;
        if (i5 / i3 >= Raster.anInt1387) {
            return;
        }
        int j5 = l2 + (super.modelHeight * j >> 16);
        boolean flag = false;
        if (k2 - j5 <= (SceneGraph.mapViewDistance << 1)) {
            flag = true;
        }
        boolean flag1 = false;
        if (i2 > 0 && aBoolean1684) {
            int k5 = k2 - l2;
            if (k5 <= (SceneGraph.mapViewDistance << 1)) {
                k5 = (SceneGraph.mapViewDistance << 1);
            }
            if (j3 > 0) {
                k3 /= i3;
                l3 /= k5;
            } else {
                l3 /= i3;
                k3 /= k5;
            }
            if (i4 > 0) {
                i5 /= i3;
                k4 /= k5;
            } else {
                k4 /= i3;
                i5 /= k5;
            }
            int i6 = anInt1685 - Rasterizer.textureInt1;
            int k6 = anInt1686 - Rasterizer.textureInt2;
            if (i6 > k3 && i6 < l3 && k6 > i5 && k6 < k4) {
                if (fitsOnSingleSquare) {
                    mapObjectIds[objectsRendered] = id;
                    mapObjectData[objectsRendered++] = i2;
                } else {
                    flag1 = true;
                }
            }
        }
        int l5 = Rasterizer.textureInt1;
        int j6 = Rasterizer.textureInt2;
        int l6 = 0;
        int i7 = 0;
        if (i != 0) {
            l6 = SINE[i];
            i7 = COSINE[i];
        }
        for (int j7 = 0; j7 < vertexCount; j7++) {
            int k7 = verticesX[j7];
            int l7 = verticesY[j7];
            int i8 = verticesZ[j7];
            if (i != 0) {
                int j8 = i8 * l6 + k7 * i7 >> 16;
                i8 = i8 * i7 - k7 * l6 >> 16;
                k7 = j8;
            }
            k7 += j1;
            l7 += k1;
            i8 += l1;
            int k8 = i8 * l + k7 * i1 >> 16;
            i8 = i8 * i1 - k7 * l >> 16;
            k7 = k8;
            k8 = l7 * k - i8 * j >> 16;
            i8 = l7 * j + i8 * k >> 16;
            l7 = k8;
            try {
                anIntArray1667[j7] = i8 - k2;
            } catch (Exception e) {
                System.out.println("OSRS??? " + osrs);
                e.printStackTrace();
            }
            //vertexPerspectiveZAbs[j7] = i8;
            if (i8 >= (SceneGraph.mapViewDistance << 1)) {
                projected_vertex_x[j7] = l5 + (k7 << SceneGraph.viewDistance) / i8;
                projected_vertex_y[j7] = j6 + (l7 << SceneGraph.viewDistance) / i8;
                projected_vertex_z[j7] = i8;
            } else {
                projected_vertex_x[j7] = -5000;
                flag = true;
            }
            if (flag || anInt1642 > 0) {
                camera_vertex_y[j7] = k7;
                camera_vertex_x[j7] = l7;
                camera_vertex_z[j7] = i8;
            }
        }

        try {
            method483(flag, flag1, i2, id);
            return;
        } catch (Exception _ex) {
            return;
        }
    }

    private void method483(boolean flag, boolean flag1, int i, int id) {
        for (int j = 0; j < anInt1652; j++) {
            anIntArray1671[j] = 0;
        }

        for (int k = 0; k < triangleCount; k++) {
            if (texturePoints == null || texturePoints[k] != -1) {
                int l = trianglePointsX[k];
                int k1 = trianglePointsY[k];
                int j2 = trianglePointsZ[k];
                int i3 = projected_vertex_x[l];
                int l3 = projected_vertex_x[k1];
                int k4 = projected_vertex_x[j2];
                if (flag && (i3 == -5000 || l3 == -5000 || k4 == -5000)) {
                    outOfReach[k] = true;
                    int j5 = (anIntArray1667[l] + anIntArray1667[k1] + anIntArray1667[j2])
                            / 3 + anInt1653;
                    anIntArrayArray1672[j5][anIntArray1671[j5]++] = k;
                } else {
                    if (flag1
                            && method486(anInt1685, anInt1686,
                            projected_vertex_y[l], projected_vertex_y[k1],
                            projected_vertex_y[j2], i3, l3, k4)) {
                        mapObjectIds[objectsRendered] = id;
                        mapObjectData[objectsRendered++] = i;
                        flag1 = false;
                    }
                    if ((i3 - l3) * (projected_vertex_y[j2] - projected_vertex_y[k1])
                            - (projected_vertex_y[l] - projected_vertex_y[k1])
                            * (k4 - l3) > 0) {
                        outOfReach[k] = false;
                        hasAnEdgeToRestrict[k] = i3 < 0 || l3 < 0 || k4 < 0
                                || i3 > Raster.viewportRX
                                || l3 > Raster.viewportRX
                                || k4 > Raster.viewportRX;
                        int k5 = (anIntArray1667[l] + anIntArray1667[k1] + anIntArray1667[j2])
                                / 3 + anInt1653;
                        anIntArrayArray1672[k5][anIntArray1671[k5]++] = k;
                    }
                }
            }
        }

        if (trianglePriorities == null) {
            for (int i1 = anInt1652 - 1; i1 >= 0; i1--) {
                int l1 = anIntArray1671[i1];
                if (l1 > 0) {
                    int ai[] = anIntArrayArray1672[i1];
                    for (int j3 = 0; j3 < l1; j3++) {
                        rasterise(ai[j3]);
                    }

                }
            }

            return;
        }
        for (int j1 = 0; j1 < 12; j1++) {
            anIntArray1673[j1] = 0;
            anIntArray1677[j1] = 0;
        }

        for (int i2 = anInt1652 - 1; i2 >= 0; i2--) {
            int k2 = anIntArray1671[i2];
            if (k2 > 0) {
                int ai1[] = anIntArrayArray1672[i2];
                for (int i4 = 0; i4 < k2; i4++) {
                    int l4 = ai1[i4];
                    int l5 = trianglePriorities[l4];
                    int j6 = anIntArray1673[l5]++;
                    anIntArrayArray1674[l5][j6] = l4;
                    if (l5 < 10) {
                        anIntArray1677[l5] += i2;
                    } else if (l5 == 10) {
                        anIntArray1675[j6] = i2;
                    } else {
                        anIntArray1676[j6] = i2;
                    }
                }

            }
        }

        int l2 = 0;
        if (anIntArray1673[1] > 0 || anIntArray1673[2] > 0) {
            l2 = (anIntArray1677[1] + anIntArray1677[2])
                    / (anIntArray1673[1] + anIntArray1673[2]);
        }
        int k3 = 0;
        if (anIntArray1673[3] > 0 || anIntArray1673[4] > 0) {
            k3 = (anIntArray1677[3] + anIntArray1677[4])
                    / (anIntArray1673[3] + anIntArray1673[4]);
        }
        int j4 = 0;
        if (anIntArray1673[6] > 0 || anIntArray1673[8] > 0) {
            j4 = (anIntArray1677[6] + anIntArray1677[8])
                    / (anIntArray1673[6] + anIntArray1673[8]);
        }
        int i6 = 0;
        int k6 = anIntArray1673[10];
        int ai2[] = anIntArrayArray1674[10];
        int ai3[] = anIntArray1675;
        if (i6 == k6) {
            i6 = 0;
            k6 = anIntArray1673[11];
            ai2 = anIntArrayArray1674[11];
            ai3 = anIntArray1676;
        }
        int i5;
        if (i6 < k6) {
            i5 = ai3[i6];
        } else {
            i5 = -1000;
        }
        for (int l6 = 0; l6 < 10; l6++) {
            while (l6 == 0 && i5 > l2) {
                rasterise(ai2[i6++]);
                if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if (i6 < k6) {
                    i5 = ai3[i6];
                } else {
                    i5 = -1000;
                }
            }
            while (l6 == 3 && i5 > k3) {
                rasterise(ai2[i6++]);
                if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if (i6 < k6) {
                    i5 = ai3[i6];
                } else {
                    i5 = -1000;
                }
            }
            while (l6 == 5 && i5 > j4) {
                rasterise(ai2[i6++]);
                if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                    i6 = 0;
                    k6 = anIntArray1673[11];
                    ai2 = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if (i6 < k6) {
                    i5 = ai3[i6];
                } else {
                    i5 = -1000;
                }
            }
            int i7 = anIntArray1673[l6];
            int ai4[] = anIntArrayArray1674[l6];
            for (int j7 = 0; j7 < i7; j7++) {
                rasterise(ai4[j7]);
            }

        }

        while (i5 != -1000) {
            rasterise(ai2[i6++]);
            if (i6 == k6 && ai2 != anIntArrayArray1674[11]) {
                i6 = 0;
                ai2 = anIntArrayArray1674[11];
                k6 = anIntArray1673[11];
                ai3 = anIntArray1676;
            }
            if (i6 < k6) {
                i5 = ai3[i6];
            } else {
                i5 = -1000;
            }
        }
    }

    private void rasterise(int modelId) {
        if (outOfReach[modelId]) {
            reduce(modelId);
            return;
        }
        int j = trianglePointsX[modelId];
        int k = trianglePointsY[modelId];
        int l = trianglePointsZ[modelId];

        Rasterizer.restrictEdges = hasAnEdgeToRestrict[modelId];
        if (triangleAlphaValues == null) {
            Rasterizer.alpha = 0;
        } else {
            Rasterizer.alpha = triangleAlphaValues[modelId];
        }
        int i1;
        if (texturePoints == null) {
            i1 = 0;
        } else {
            i1 = texturePoints[modelId] & 3;
        }
        if (i1 == 0) {
            Rasterizer.drawDepthShadedTriangle(projected_vertex_y[j], projected_vertex_y[k],
                    projected_vertex_y[l], projected_vertex_x[j],
                    projected_vertex_x[k], projected_vertex_x[l],
                    projected_vertex_z[j],
                    projected_vertex_z[k], projected_vertex_z[l],
                    faceShadeA[modelId], faceShadeB[modelId], faceShadeC[modelId]);
            return;
        }
        if (i1 == 1) {
            Rasterizer.drawDepthFlatTriangle(projected_vertex_y[j], projected_vertex_y[k],
                    projected_vertex_y[l], projected_vertex_x[j],
                    projected_vertex_x[k], projected_vertex_x[l], projected_vertex_z[j],
                    projected_vertex_z[k], projected_vertex_z[l],
                    hsl2rgb[faceShadeA[modelId]]);
            return;
        }
        if (i1 == 2) {
            int j1 = texturePoints[modelId] >> 2;
            int l1 = texturesFaceA[j1];
            int j2 = texturesFaceB[j1];
            int l2 = texturesFaceC[j1];
            if (Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
                Rasterizer.render_depth_texture_triangle(projected_vertex_y[j], projected_vertex_y[k],
                        projected_vertex_y[l], projected_vertex_x[j],
                        projected_vertex_x[k], projected_vertex_x[l], projected_vertex_z[j],
                        projected_vertex_z[k], projected_vertex_z[l],
                        faceShadeA[modelId], faceShadeB[modelId], faceShadeC[modelId],
                        camera_vertex_y[l1], camera_vertex_y[j2],
                        camera_vertex_y[l2], camera_vertex_x[l1],
                        camera_vertex_x[j2], camera_vertex_x[l2],
                        camera_vertex_z[l1], camera_vertex_z[j2],
                        camera_vertex_z[l2], triangleColorValues[modelId], triangleColorValues[modelId], false, false);
            } else {
                Rasterizer.drawDepthTexturedTriangle(projected_vertex_y[j], projected_vertex_y[k],
                        projected_vertex_y[l], projected_vertex_x[j],
                        projected_vertex_x[k], projected_vertex_x[l], projected_vertex_z[j],
                        projected_vertex_z[k], projected_vertex_z[l],
                        faceShadeA[modelId], faceShadeB[modelId], faceShadeC[modelId],
                        camera_vertex_y[l1], camera_vertex_y[j2],
                        camera_vertex_y[l2], camera_vertex_x[l1],
                        camera_vertex_x[j2], camera_vertex_x[l2],
                        camera_vertex_z[l1], camera_vertex_z[j2],
                        camera_vertex_z[l2], triangleColorValues[modelId]);
            }
            return;
        }
        if (i1 == 3) {
            int k1 = texturePoints[modelId] >> 2;
            int i2 = texturesFaceA[k1];
            int k2 = texturesFaceB[k1];
            int i3 = texturesFaceC[k1];
            if (Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
                Rasterizer.render_depth_texture_triangle(projected_vertex_y[j], projected_vertex_y[k],
                        projected_vertex_y[l], projected_vertex_x[j],
                        projected_vertex_x[k], projected_vertex_x[l], projected_vertex_z[j],
                        projected_vertex_z[k], projected_vertex_z[l],
                        faceShadeA[modelId], faceShadeA[modelId], faceShadeA[modelId],
                        camera_vertex_y[i2], camera_vertex_y[k2],
                        camera_vertex_y[i3], camera_vertex_x[i2],
                        camera_vertex_x[k2], camera_vertex_x[i3],
                        camera_vertex_z[i2], camera_vertex_z[k2],
                        camera_vertex_z[i3], triangleColorValues[modelId], triangleColorValues[modelId], false, false);
            } else {
                Rasterizer.drawDepthTexturedTriangle(projected_vertex_y[j], projected_vertex_y[k],
                        projected_vertex_y[l], projected_vertex_x[j],
                        projected_vertex_x[k], projected_vertex_x[l], projected_vertex_z[j],
                        projected_vertex_z[k], projected_vertex_z[l],
                        faceShadeA[modelId], faceShadeA[modelId], faceShadeA[modelId],
                        camera_vertex_y[i2], camera_vertex_y[k2],
                        camera_vertex_y[i3], camera_vertex_x[i2],
                        camera_vertex_x[k2], camera_vertex_x[i3],
                        camera_vertex_z[i2], camera_vertex_z[k2],
                        camera_vertex_z[i3], triangleColorValues[modelId]);
            }
        }
    }

    private void reduce(int i) {
        if (triangleColorValues != null) {
            if (triangleColorValues[i] == 65535) {
                return;
            }
        }
        int j = Rasterizer.textureInt1;
        int k = Rasterizer.textureInt2;
        int l = 0;
        int i1 = trianglePointsX[i];
        int j1 = trianglePointsY[i];
        int k1 = trianglePointsZ[i];
        int l1 = camera_vertex_z[i1];
        int i2 = camera_vertex_z[j1];
        int j2 = camera_vertex_z[k1];

        if (l1 >= (SceneGraph.mapViewDistance << 1)) {
            anIntArray1678[l] = projected_vertex_x[i1];
            anIntArray1679[l] = projected_vertex_y[i1];
            anIntArray1680[l++] = faceShadeA[i];
        } else {
            int k2 = camera_vertex_y[i1];
            int k3 = camera_vertex_x[i1];
            int k4 = faceShadeA[i];
            if (j2 >= (SceneGraph.mapViewDistance << 1)) {
                int k5 = ((SceneGraph.mapViewDistance << 1) - l1) * lightDecay[j2 - l1];
                anIntArray1678[l] = j
                        + (k2 + ((camera_vertex_y[k1] - k2) * k5 >> 16) << 9)
                        / (SceneGraph.mapViewDistance << 1);
                anIntArray1679[l] = k
                        + (k3 + ((camera_vertex_x[k1] - k3) * k5 >> 16) << 9)
                        / (SceneGraph.mapViewDistance << 1);
                anIntArray1680[l++] = k4
                        + ((faceShadeC[i] - k4) * k5 >> 16);
            }
            if (i2 >= (SceneGraph.mapViewDistance << 1)) {
                int l5 = ((SceneGraph.mapViewDistance << 1) - l1) * lightDecay[i2 - l1];
                anIntArray1678[l] = j
                        + (k2 + ((camera_vertex_y[j1] - k2) * l5 >> 16) << 9)
                        / (SceneGraph.mapViewDistance << 1);
                anIntArray1679[l] = k
                        + (k3 + ((camera_vertex_x[j1] - k3) * l5 >> 16) << 9)
                        / (SceneGraph.mapViewDistance << 1);
                anIntArray1680[l++] = k4
                        + ((faceShadeB[i] - k4) * l5 >> 16);
            }
        }
        if (i2 >= (SceneGraph.mapViewDistance << 1)) {
            anIntArray1678[l] = projected_vertex_x[j1];
            anIntArray1679[l] = projected_vertex_y[j1];
            anIntArray1680[l++] = faceShadeB[i];
        } else {
            int l2 = camera_vertex_y[j1];
            int l3 = camera_vertex_x[j1];
            int l4 = faceShadeB[i];
            if (l1 >= (SceneGraph.mapViewDistance << 1)) {
                int i6 = ((SceneGraph.mapViewDistance << 1) - i2) * lightDecay[l1 - i2];
                anIntArray1678[l] = j
                        + (l2 + ((camera_vertex_y[i1] - l2) * i6 >> 16) << 9)
                        / (SceneGraph.mapViewDistance << 1);
                anIntArray1679[l] = k
                        + (l3 + ((camera_vertex_x[i1] - l3) * i6 >> 16) << 9)
                        / (SceneGraph.mapViewDistance << 1);
                anIntArray1680[l++] = l4
                        + ((faceShadeA[i] - l4) * i6 >> 16);
            }
            if (j2 >= (SceneGraph.mapViewDistance << 1)) {
                int j6 = ((SceneGraph.mapViewDistance << 1) - i2) * lightDecay[j2 - i2];
                anIntArray1678[l] = j
                        + (l2 + ((camera_vertex_y[k1] - l2) * j6 >> 16) << 9)
                        / (SceneGraph.mapViewDistance << 1);
                anIntArray1679[l] = k
                        + (l3 + ((camera_vertex_x[k1] - l3) * j6 >> 16) << 9)
                        / (SceneGraph.mapViewDistance << 1);
                anIntArray1680[l++] = l4
                        + ((faceShadeC[i] - l4) * j6 >> 16);
            }
        }
        if (j2 >= (SceneGraph.mapViewDistance << 1)) {
            anIntArray1678[l] = projected_vertex_x[k1];
            anIntArray1679[l] = projected_vertex_y[k1];
            anIntArray1680[l++] = faceShadeC[i];
        } else {
            int i3 = camera_vertex_y[k1];
            int i4 = camera_vertex_x[k1];
            int i5 = faceShadeC[i];
            if (i2 >= (SceneGraph.mapViewDistance << 1)) {
                int k6 = ((SceneGraph.mapViewDistance << 1) - j2) * lightDecay[i2 - j2];
                anIntArray1678[l] = j
                        + (i3 + ((camera_vertex_y[j1] - i3) * k6 >> 16) << 9)
                        / (SceneGraph.mapViewDistance << 1);
                anIntArray1679[l] = k
                        + (i4 + ((camera_vertex_x[j1] - i4) * k6 >> 16) << 9)
                        / (SceneGraph.mapViewDistance << 1);
                anIntArray1680[l++] = i5
                        + ((faceShadeB[i] - i5) * k6 >> 16);
            }
            if (l1 >= (SceneGraph.mapViewDistance << 1)) {
                int l6 = ((SceneGraph.mapViewDistance << 1) - j2) * lightDecay[l1 - j2];
                anIntArray1678[l] = j
                        + (i3 + ((camera_vertex_y[i1] - i3) * l6 >> 16) << 9)
                        / (SceneGraph.mapViewDistance << 1);
                anIntArray1679[l] = k
                        + (i4 + ((camera_vertex_x[i1] - i4) * l6 >> 16) << 9)
                        / (SceneGraph.mapViewDistance << 1);
                anIntArray1680[l++] = i5
                        + ((faceShadeA[i] - i5) * l6 >> 16);
            }
        }
        int j3 = anIntArray1678[0];
        int j4 = anIntArray1678[1];
        int j5 = anIntArray1678[2];
        int i7 = anIntArray1679[0];
        int j7 = anIntArray1679[1];
        int k7 = anIntArray1679[2];
        if ((j3 - j4) * (k7 - j7) - (i7 - j7) * (j5 - j4) > 0) {
            Rasterizer.restrictEdges = false;
            if (l == 3) {
                if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > Raster.viewportRX
                        || j4 > Raster.viewportRX || j5 > Raster.viewportRX) {
                    Rasterizer.restrictEdges = true;
                }
                int l7;
                if (texturePoints == null) {
                    l7 = 0;
                } else {
                    l7 = texturePoints[i] & 3;
                }
                if (l7 == 0) {
                    Rasterizer.drawShadedTriangle(i7, j7, k7, j3, j4, j5,
                            anIntArray1680[0], anIntArray1680[1],
                            anIntArray1680[2]);
                } else if (l7 == 1) {
                    Rasterizer.drawFlatTriangle(i7, j7, k7, j3, j4, j5,
                            hsl2rgb[faceShadeA[i]]);
                } else if (l7 == 2) {
                    int j8 = texturePoints[i] >> 2;
                    int k9 = texturesFaceA[j8];
                    int k10 = texturesFaceB[j8];
                    int k11 = texturesFaceC[j8];
                    Rasterizer.drawDepthTexturedTriangle(i7, j7, k7, j3, j4, j5,
                            anIntArray1680[0], anIntArray1680[1],
                            anIntArray1680[2], camera_vertex_y[k9],
                            camera_vertex_y[k10], camera_vertex_y[k11],
                            camera_vertex_x[k9], camera_vertex_x[k10],
                            camera_vertex_x[k11], camera_vertex_z[k9],
                            camera_vertex_z[k10], camera_vertex_z[k11],
                            triangleColorValues[i]);
                    // vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
                } else if (l7 == 3) {
                    int k8 = texturePoints[i] >> 2;
                    int l9 = texturesFaceA[k8];
                    int l10 = texturesFaceB[k8];
                    int l11 = texturesFaceC[k8];
                    Rasterizer.drawDepthTexturedTriangle(i7, j7, k7, j3, j4, j5,
                            faceShadeA[i], faceShadeA[i],
                            faceShadeA[i], camera_vertex_y[l9],
                            camera_vertex_y[l10], camera_vertex_y[l11],
                            camera_vertex_x[l9], camera_vertex_x[l10],
                            camera_vertex_x[l11], camera_vertex_z[l9],
                            camera_vertex_z[l10], camera_vertex_z[l11],
                            triangleColorValues[i]);
                    //, vertexPerspectiveZAbs[i1], vertexPerspectiveZAbs[j1], vertexPerspectiveZAbs[k1]);
                }
            }
            if (l == 4) {
                if (j3 < 0 || j4 < 0 || j5 < 0 || j3 > Raster.viewportRX
                        || j4 > Raster.viewportRX || j5 > Raster.viewportRX
                        || anIntArray1678[3] < 0
                        || anIntArray1678[3] > Raster.viewportRX) {
                    Rasterizer.restrictEdges = true;
                }
                int i8;
                if (texturePoints == null) {
                    i8 = 0;
                } else {
                    i8 = texturePoints[i] & 3;
                }
                if (i8 == 0) {
                    Rasterizer.drawShadedTriangle(i7, j7, k7, j3, j4, j5,
                            anIntArray1680[0], anIntArray1680[1],
                            anIntArray1680[2]);
                    Rasterizer.drawShadedTriangle(i7, k7, anIntArray1679[3], j3, j5,
                            anIntArray1678[3], anIntArray1680[0],
                            anIntArray1680[2], anIntArray1680[3]);
                    return;
                }
                if (i8 == 1) {
                    int l8 = hsl2rgb[faceShadeA[i]];
                    Rasterizer.drawFlatTriangle(i7, j7, k7, j3, j4, j5, l8);
                    Rasterizer.drawFlatTriangle(i7, k7, anIntArray1679[3], j3, j5,
                            anIntArray1678[3], l8);
                    return;
                }
                if (i8 == 2) {
                    int i9 = texturePoints[i] >> 2;
                    int i10 = texturesFaceA[i9];
                    int i11 = texturesFaceB[i9];
                    int i12 = texturesFaceC[i9];
                    Rasterizer.drawDepthTexturedTriangle(i7, j7, k7, j3, j4, j5,
                            anIntArray1680[0], anIntArray1680[1],
                            anIntArray1680[2], camera_vertex_y[i10],
                            camera_vertex_y[i11], camera_vertex_y[i12],
                            camera_vertex_x[i10], camera_vertex_x[i11],
                            camera_vertex_x[i12], camera_vertex_z[i10],
                            camera_vertex_z[i11], camera_vertex_z[i12],
                            triangleColorValues[i]);
                    Rasterizer.drawDepthTexturedTriangle(i7, k7, anIntArray1679[3], j3, j5,
                            anIntArray1678[3], anIntArray1680[0],
                            anIntArray1680[2], anIntArray1680[3],
                            camera_vertex_y[i10], camera_vertex_y[i11],
                            camera_vertex_y[i12], camera_vertex_x[i10],
                            camera_vertex_x[i11], camera_vertex_x[i12],
                            camera_vertex_z[i10], camera_vertex_z[i11],
                            camera_vertex_z[i12], triangleColorValues[i]);
                    return;
                }
                if (i8 == 3) {
                    int j9 = texturePoints[i] >> 2;
                    int j10 = texturesFaceA[j9];
                    int j11 = texturesFaceB[j9];
                    int j12 = texturesFaceC[j9];
                    Rasterizer.drawDepthTexturedTriangle(i7, j7, k7, j3, j4, j5,
                            faceShadeA[i], faceShadeA[i],
                            faceShadeA[i], camera_vertex_y[j10],
                            camera_vertex_y[j11], camera_vertex_y[j12],
                            camera_vertex_x[j10], camera_vertex_x[j11],
                            camera_vertex_x[j12], camera_vertex_z[j10],
                            camera_vertex_z[j11], camera_vertex_z[j12],
                            triangleColorValues[i]);
                    Rasterizer.drawDepthTexturedTriangle(i7, k7, anIntArray1679[3], j3, j5,
                            anIntArray1678[3], faceShadeA[i],
                            faceShadeA[i], faceShadeA[i],
                            camera_vertex_y[j10], camera_vertex_y[j11],
                            camera_vertex_y[j12], camera_vertex_x[j10],
                            camera_vertex_x[j11], camera_vertex_x[j12],
                            camera_vertex_z[j10], camera_vertex_z[j11],
                            camera_vertex_z[j12], triangleColorValues[i]);
                }
            }
        }
    }

    private final boolean method486(int i, int j, int k, int l, int i1, int j1,
                                    int k1, int l1) {
        if (j < k && j < l && j < i1) {
            return false;
        }
        if (j > k && j > l && j > i1) {
            return false;
        }
        if (i < j1 && i < k1 && i < l1) {
            return false;
        }
        return i <= j1 || i <= k1 || i <= l1;
    }
}