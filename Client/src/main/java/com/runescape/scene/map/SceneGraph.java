package com.runescape.scene.map;

import com.runescape.Constants;
import main.java.com.runescape.Game;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.collection.Deque;
import com.runescape.media.Raster;
import com.runescape.media.Vertex;
import com.runescape.media.renderable.ItemPile;
import com.runescape.media.renderable.Model;
import com.runescape.media.renderable.Renderable;
import com.runescape.media.renderable.StaticObject;
import com.runescape.scene.CullingCluster;
import com.runescape.scene.graphic.Rasterizer;
import com.runescape.scene.map.object.GroundDecoration;
import com.runescape.scene.map.object.Wall;
import com.runescape.scene.map.object.WallDecoration;
import com.runescape.scene.map.object.tile.ShapedTile;
import com.runescape.scene.map.object.tile.SimpleTile;
import com.runescape.scene.map.object.tile.Tile;

import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("all")
public final class SceneGraph {

    public static int mapViewDistance = 50;
    /**
     * The {@link java.util.logging.Logger} instance.
     */

    private static final Logger logger = Logger.getLogger(SceneGraph.class.getName());
    private static final int[] faceXoffset2 = {53, -53, -53, 53};
    private static final int[] faceYOffset2 = {-53, -53, 53, 53};
    private static final int[] faceXOffset3 = {-45, 45, 45, -45};
    private static final int[] faceYOffset3 = {45, 45, -45, -45};
    private static final int amountOfCullingClusters;
    private static final CullingCluster[] processedClusters = new CullingCluster[500];
    private static final int[] anIntArray478 = {19, 55, 38, 155, 255, 110, 137, 205, 76};
    private static final int[] anIntArray479 = {160, 192, 80, 96, 0, 144, 80, 48, 160};
    private static final int[] anIntArray480 = {76, 8, 137, 4, 0, 1, 38, 2, 19};
    private static final int[] anIntArray481 = {0, 0, 2, 0, 0, 2, 1, 1, 0};
    private static final int[] anIntArray482 = {2, 0, 0, 2, 0, 0, 0, 4, 4};
    private static final int[] anIntArray483 = {0, 4, 4, 8, 0, 0, 8, 0, 0};
    private static final int[] anIntArray484 = {1, 1, 0, 0, 0, 8, 0, 0, 8};
    private static final int[] textureRGBColour = {41, 39248, 41, 4643, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 43086, 41, 41, 41, 41, 41, 41, 41, 8602, 41, 28992, 41, 41, 41, 41, 41, 5056, 41, 41, 41, 7079, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 3131, 41, 41, 41};
    public static boolean lowMem = true;
    public static int clickedTileX = -1;
    public static int clickedTileY = -1;
    public static int viewDistance = 9;

    private static int anInt446;
    private static int plane__;
    private static int anInt448;
    private static int anInt449;
    private static int anInt450;
    private static int anInt451;
    private static int anInt452;
    private static int xCamPosTile;
    private static int yCamPosTile;
    private static int xCamPos;
    private static int zCamPos;
    private static int yCamPos;
    private static int yCurveSin;
    private static int yCurveCos;
    private static int xCurveSin;
    private static int xCurveCos;
    private static StaticObject[] staticObjects = new StaticObject[100];
    private static boolean isClicked;
    private static int clickX;
    private static int clickY;
    private static int[] cullingClusterPointer;
    private static CullingCluster[][] cullingClusters;
    private static int processedClusterPtr;
    private static Deque tileDeque = new Deque();
    public static boolean[][][][] tile_visibility_maps = new boolean[8][32][(mapViewDistance << 1) | 1][(mapViewDistance << 1) | 1];
    private static boolean[][] tile_visibility_map;
    private static int midX;
    private static int midY;
    private static int left;
    private static int top;
    private static int right;
    private static int bottom;

    static {
        amountOfCullingClusters = 4;
        cullingClusterPointer = new int[amountOfCullingClusters];
        cullingClusters = new CullingCluster[amountOfCullingClusters][500];
    }

    private final int zMapSize;
    private final int xMapSize;
    private final int yMapSize;
    private final int[][][] heightMap;
    private final Tile[][][] groundArray;
    private final StaticObject[] staticObjectCache;
    private final int[][][] anIntArrayArrayArray445;
    private final int[] anIntArray486;
    private final int[] anIntArray487;
    private final int[][] tileSHapePoints = {new int[16], {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1}, {1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0}, {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1}, {0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0}, {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1}, {1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1}};
    private final int[][] tileShapeIndices = {{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, {12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3}, {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0}, {3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12}};
    public boolean opaque_floor_texture = false;
    private boolean hdMinimap = true;
    private int currentHL;
    private int amountOfStaticObjects;
    private int anInt488;

    public SceneGraph(int ai[][][]) {
        int height = 104;// was parameter
        int width = 104;// was parameter
        int depth = 4;// was parameter
        staticObjectCache = new StaticObject[5000];
        anIntArray486 = new int[10000];
        anIntArray487 = new int[10000];
        zMapSize = depth;
        xMapSize = width;
        yMapSize = height;
        groundArray = new Tile[depth][width][height];
        anIntArrayArrayArray445 = new int[depth][width + 1][height + 1];
        heightMap = ai;
        initToNull();
    }

    public static void nullLoader() {
        staticObjects = null;
        cullingClusterPointer = null;
        cullingClusters = null;
        tileDeque = null;
        tile_visibility_maps = null;
        tile_visibility_map = null;
    }

    public static void createCullingCluster(int id, int tileStartX, int worldEndZ, int tileEndX, int tileEndY, int worldStartZ, int tileStartY, int searchMask) {
        CullingCluster cullingCluster = new CullingCluster();
        cullingCluster.tileStartX = tileStartX / 128;
        cullingCluster.tileEndX = tileEndX / 128;
        cullingCluster.tileStartY = tileStartY / 128;
        cullingCluster.tileEndY = tileEndY / 128;
        cullingCluster.searchMask = searchMask;
        cullingCluster.worldStartX = tileStartX;
        cullingCluster.worldEndX = tileEndX;
        cullingCluster.worldStartY = tileStartY;
        cullingCluster.worldEndY = tileEndY;
        cullingCluster.worldStartZ = worldStartZ;
        cullingCluster.worldEndZ = worldEndZ;
        cullingClusters[id][cullingClusterPointer[id]++] = cullingCluster;
    }

    public static void setupViewport(int minZ, int maxZ, int width, int height, int ai[]) {
        left = 0;
        top = 0;
        right = width;
        bottom = height;
        midX = width / 2;
        midY = height / 2;
        boolean isOnScreen[][][][] = new boolean[9][32][(mapViewDistance << 1) + 3][(mapViewDistance << 1) + 3];
        for (int yAngle = 128; yAngle <= 384; yAngle += 32) {
            for (int xAngle = 0; xAngle < 2048; xAngle += 64) {
                yCurveSin = Model.SINE[yAngle];
                yCurveCos = Model.COSINE[yAngle];
                xCurveSin = Model.SINE[xAngle];
                xCurveCos = Model.COSINE[xAngle];
                int l1 = (yAngle - 128) / 32;
                int j2 = xAngle / 64;
                for (int l2 = -(mapViewDistance - 1); l2 <= (mapViewDistance - 1); l2++) {
                    for (int j3 = -(mapViewDistance - 1); j3 <= (mapViewDistance - 1); j3++) {
                        int k3 = l2 * 128;
                        int i4 = j3 * 128;
                        boolean flag2 = false;
                        for (int k4 = -minZ; k4 <= maxZ; k4 += 128) {
                            if (!isOnScreen(ai[l1] + k4, i4, k3)) {
                                continue;
                            }
                            flag2 = true;
                            break;
                        }
                        isOnScreen[l1][j2][l2 + mapViewDistance + 1][j3 + mapViewDistance + 1] = flag2;
                    }
                }
            }
        }
        for (int k1 = 0; k1 < 8; k1++) {
            for (int i2 = 0; i2 < 32; i2++) {
                for (int k2 = -mapViewDistance; k2 < mapViewDistance; k2++) {
                    for (int i3 = -mapViewDistance; i3 < mapViewDistance; i3++) {
                        boolean flag1 = false;
                        label0:
                        for (int l3 = -1; l3 <= 1; l3++) {
                            for (int j4 = -1; j4 <= 1; j4++) {
                                if (isOnScreen[k1][i2][k2 + l3 + mapViewDistance + 1][i3 + j4 + mapViewDistance + 1]) {
                                    flag1 = true;
                                } else if (isOnScreen[k1][(i2 + 1) % 31][k2 + l3 + mapViewDistance + 1][i3 + j4 + mapViewDistance + 1]) {
                                    flag1 = true;
                                } else if (isOnScreen[k1 + 1][i2][k2 + l3 + mapViewDistance + 1][i3 + j4 + mapViewDistance + 1]) {
                                    flag1 = true;
                                } else {
                                    if (!isOnScreen[k1 + 1][(i2 + 1) % 31][k2 + l3 + mapViewDistance + 1][i3 + j4 + mapViewDistance + 1]) {
                                        continue;
                                    }
                                    flag1 = true;
                                }
                                break label0;
                            }

                        }
                        tile_visibility_maps[k1][i2][k2 + mapViewDistance][i3 + mapViewDistance] = flag1;
                    }
                }
            }
        }
    }

    private static boolean isOnScreen(int z, int y, int x) {
        int l = y * xCurveSin + x * xCurveCos >> 16;
        int i1 = y * xCurveCos - x * xCurveSin >> 16;
        int dist = z * yCurveSin + i1 * yCurveCos >> 16;
        int k1 = z * yCurveCos - i1 * yCurveSin >> 16;
        if (dist < (mapViewDistance << 1) || dist > 3500 * 2) {//todo test
            return false;
        }
        int l1 = midX + (l << viewDistance) / dist;
        int i2 = midY + (k1 << viewDistance) / dist;
        return l1 >= left && l1 <= right && i2 >= top && i2 <= bottom;
    }

    public Wall fetchWall(int i, int j, int k) {
        Tile tile = groundArray[i][j][k];
        if (tile == null || tile.wall == null) {
            return null;
        } else {
            return tile.wall;
        }
    }

    public WallDecoration fetchWallDecoration(int i, int j, int l) {
        Tile tile = groundArray[i][j][l];
        if (tile == null || tile.wallDecoration == null) {
            return null;
        } else {
            return tile.wallDecoration;
        }
    }

    public StaticObject fetchStaticObject(int z, int x, int y) {
        Tile tile = groundArray[z][x][y];
        if (tile == null) {
            return null;
        }
        for (int l = 0; l < tile.entityCount; l++) {
            StaticObject staticObject = tile.staticObjects[l];
            if (staticObject.tileLeft == x && staticObject.tileTop == y) {
                return staticObject;
            }
        }
        return null;
    }

    public GroundDecoration fetchGroundDecoration(int i, int j, int k) {
        Tile tile = groundArray[i][j][k];
        if (tile == null || tile.groundDecoration == null) {
            return null;
        } else {
            return tile.groundDecoration;
        }
    }

    public int fetchWallNewUID(int i, int j, int k) {
        Tile tile = groundArray[i][j][k];
        if (tile == null || tile.wall == null) {
            return 0;
        } else {
            return tile.wall.wallObjUID;
        }
    }

    public int fetchWallDecorationNewUID(int i, int j, int l) {
        Tile tile = groundArray[i][j][l];
        if (tile == null || tile.wallDecoration == null) {
            return 0;
        } else {
            return tile.wallDecoration.wallDecorUID;
        }
    }

    public int fetchObjectMeshNewUID(int z, int x, int y) {
        Tile tile = groundArray[z][x][y];
        if (tile == null) {
            return 0;
        }
        for (int l = 0; l < tile.entityCount; l++) {
            StaticObject staticObject = tile.staticObjects[l];
            if (staticObject.tileLeft == x && staticObject.tileTop == y) {
                return staticObject.interactiveObjUID;
            }
        }
        return 0;
    }

    public int getFloorDecorationKey(int i, int j, int k) {
        Tile tile = groundArray[i][j][k];
        if (tile == null || tile.groundDecoration == null) {
            return 0;
        } else {
            return tile.groundDecoration.groundDecorUID;
        }
    }

    public void initToNull() {
        for (int z = 0; z < zMapSize; z++) {
            for (int x = 0; x < xMapSize; x++) {
                for (int y = 0; y < yMapSize; y++) {
                    groundArray[z][x][y] = null;
                }
            }
        }
        for (int l = 0; l < amountOfCullingClusters; l++) {
            for (int j1 = 0; j1 < cullingClusterPointer[l]; j1++) {
                cullingClusters[l][j1] = null;
            }
            cullingClusterPointer[l] = 0;
        }

        for (int k1 = 0; k1 < amountOfStaticObjects; k1++) {
            staticObjectCache[k1] = null;
        }

        amountOfStaticObjects = 0;
        for (int l1 = 0; l1 < staticObjects.length; l1++) {
            staticObjects[l1] = null;
        }

    }

    public void initTiles(int hl) {
        currentHL = hl;
        for (int k = 0; k < xMapSize; k++) {
            for (int l = 0; l < yMapSize; l++) {
                if (groundArray[hl][k][l] == null) {
                    groundArray[hl][k][l] = new Tile(hl, k, l);
                }
            }
        }

    }

    public void applyBridgeMode(int y, int x) {
        Tile tile = groundArray[0][x][y];
        for (int l = 0; l < 3; l++) {
            Tile tile_ = groundArray[l][x][y] = groundArray[l + 1][x][y];
            if (tile_ != null) {
                tile_.tileZ--;
                for (int entityPtr = 0; entityPtr < tile_.entityCount; entityPtr++) {
                    StaticObject iObject = tile_.staticObjects[entityPtr];
                    if ((iObject.uid >> 29 & 3) == 2 && iObject.tileLeft == x && iObject.tileTop == y) {
                        iObject.zPos--;
                    }
                }

            }
        }
        if (groundArray[0][x][y] == null) {
            groundArray[0][x][y] = new Tile(0, x, y);
        }
        groundArray[0][x][y].tileBelowThisTile = tile;
        groundArray[3][x][y] = null;
    }

    public void setVisiblePlanesFor(int z, int x, int y, int logicHeight) {
        Tile tile = groundArray[z][x][y];
        if (tile != null) {
            groundArray[z][x][y].logicHeight = logicHeight;
        }
    }

    public void addTile(int plane, int x, int y, int shape, int rotation,
                        int texture, int zA, int zB, int zD, int zC, int colourA,
                        int colourB, int colourD, int colourC, int colourAA, int colourBA,
                        int colourDA, int colourCA, int rgbColour, int rgbColour_,
                        int color, int copy_texture, int copy_color, boolean tex) {
        if (shape == 434 && copy_texture != 24) {
            SimpleTile tile_1 = new SimpleTile(colourA, colourB, colourD, colourC, texture, rgbColour_, zA == zB && zA == zD && zA == zC, color, tex);
            for (int z = plane; z >= 0; z--) {
                if (groundArray[z][x][y] == null) {
                    groundArray[z][x][y] = new Tile(z, x, y);
                }
            }

            groundArray[plane][x][y].simpleTile = tile_1;
            return;
        }
        if (shape == 0) {
            SimpleTile tile = new SimpleTile(colourA, colourB, colourD, colourC, -1, rgbColour, false, color, tex);
            for (int z = plane; z >= 0; z--) {
                if (groundArray[z][x][y] == null) {
                    groundArray[z][x][y] = new Tile(z, x, y);
                }
            }

            groundArray[plane][x][y].simpleTile = tile;
            return;
        }
        if (shape == 1) {
            SimpleTile tile_1 = new SimpleTile(colourAA, colourBA, colourDA, colourCA, texture, rgbColour_, zA == zB && zA == zD && zA == zC, color, tex);
            for (int z = plane; z >= 0; z--) {
                if (groundArray[z][x][y] == null) {
                    groundArray[z][x][y] = new Tile(z, x, y);
                }
            }

            groundArray[plane][x][y].simpleTile = tile_1;
            return;
        }
        ShapedTile shapedTile = new ShapedTile(y, colourAA, colourC, zD, texture, colourDA, rotation, colourA, rgbColour, colourD, zC, zB, zA, shape, colourCA, colourBA, colourB, x, rgbColour_, color, copy_texture, copy_color, tex);
        for (int z = plane; z >= 0; z--) {
            if (groundArray[z][x][y] == null) {
                groundArray[z][x][y] = new Tile(z, x, y);
            }
        }

        groundArray[plane][x][y].shapedTile = shapedTile;
    }

    public void addGroundDecoration(int plane, int zPos, int yPos, Renderable animable, byte byte0, int uid, int xPos,
                                    int groundDecorUID) {
        if (animable == null) {
            return;
        }
        GroundDecoration groundDecoration = new GroundDecoration();
        groundDecoration.node = animable;
        groundDecoration.groundDecorUID = groundDecorUID;
        groundDecoration.xPos = xPos * 128 + 64;
        groundDecoration.yPos = yPos * 128 + 64;
        groundDecoration.zPos = zPos;
        groundDecoration.uid = uid;
        groundDecoration.objConfig = byte0;
        if (groundArray[plane][xPos][yPos] == null) {
            groundArray[plane][xPos][yPos] = new Tile(plane, xPos, yPos);
        }
        groundArray[plane][xPos][yPos].groundDecoration = groundDecoration;
    }

    public void addItemPileTile(int xPos, int uid, Renderable secondItem, int zPos, Renderable thirdItem,
                                Renderable firstItem, int plane, int yPos) {
        ItemPile itemPile = new ItemPile();
        itemPile.firstItemPile = firstItem;
        itemPile.xPos = xPos * 128 + 64;
        itemPile.yPos = yPos * 128 + 64;
        itemPile.zPos = zPos;
        itemPile.uid = uid;
        itemPile.secondItemPile = secondItem;
        itemPile.thirdItemPile = thirdItem;
        int isHighestPriority = 0;
        Tile tile = groundArray[plane][xPos][yPos];
        if (tile != null) {
            for (int k1 = 0; k1 < tile.entityCount; k1++) {
                if (tile.staticObjects[k1].node instanceof Model) {
                    int tempInt = ((Model) tile.staticObjects[k1].node).myPriority;
                    if (tempInt > isHighestPriority) {
                        isHighestPriority = tempInt;
                    }
                }
            }
        }
        itemPile.topItem = isHighestPriority;
        if (groundArray[plane][xPos][yPos] == null) {
            groundArray[plane][xPos][yPos] = new Tile(plane, xPos, yPos);
        }
        groundArray[plane][xPos][yPos].itemPile = itemPile;
    }

    public void addWall(int orientation, Renderable node, int uid, int yPos, byte objConfig, int xPos, Renderable node2,
                        int zPos, int orientation_2, int plane, int wallObjUID) {
        if (node == null && node2 == null) {
            return;
        }
        Wall wall = new Wall();
        wall.uid = uid;
        wall.objConfig = objConfig;
        wall.xPos = xPos * 128 + 64;
        wall.yPos = yPos * 128 + 64;
        wall.zPos = zPos;
        wall.node1 = node;
        wall.node2 = node2;
        wall.wallObjUID = wallObjUID;
        wall.orientation = orientation;
        wall.orientation1 = orientation_2;
        for (int zPtr = plane; zPtr >= 0; zPtr--) {
            if (groundArray[zPtr][xPos][yPos] == null) {
                groundArray[zPtr][xPos][yPos] = new Tile(zPtr, xPos, yPos);
            }
        }

        groundArray[plane][xPos][yPos].wall = wall;
    }

    public void addWallDecoration(int uid, int yPos, int rotation, int plane, int xOff, int zPos,
                                  Renderable node, int xPos, byte config, int yOff, int configBits, int wallDecorUID) {
        if (node == null) {
            return;
        }
        WallDecoration wallDecoration = new WallDecoration();
        wallDecoration.uid = uid;
        wallDecoration.objConfig = config;
        wallDecoration.xPos = xPos * 128 + 64 + xOff;
        wallDecoration.yPos = yPos * 128 + 64 + yOff;
        wallDecoration.zPos = zPos;
        wallDecoration.node = node;
        wallDecoration.wallDecorUID = wallDecorUID;
        wallDecoration.configurationBits = configBits;
        wallDecoration.rotation = rotation;
        for (int zPtr = plane; zPtr >= 0; zPtr--) {
            if (groundArray[zPtr][xPos][yPos] == null) {
                groundArray[zPtr][xPos][yPos] = new Tile(zPtr, xPos, yPos);
            }
        }

        groundArray[plane][xPos][yPos].wallDecoration = wallDecoration;
    }

    public boolean addInteractableEntity(int ui, byte config, int worldZ, int tileBottom, Renderable node, int tileRight, int z,
                                         int rotation, int tileTop, int tileLeft, int interactiveUID) {
        if (node == null) {
            return true;
        } else {
            int worldX = tileLeft * 128 + 64 * tileRight;
            int worldY = tileTop * 128 + 64 * tileBottom;
            return addEntity(z, tileLeft, tileTop, tileRight, tileBottom,
                    worldX, worldY, worldZ, node, rotation, false, ui, config, interactiveUID);
        }
    }

    public boolean addMutipleTileEntity(int z, int rotation, int worldZ, int ui, int worldY, int j1, int worldX,
                                        Renderable nodeToAdd, boolean flag) {
        if (nodeToAdd == null) {
            return true;
        }
        int tileLeft = worldX - j1;
        int tileTop = worldY - j1;
        int tileRight = worldX + j1;
        int tileBottom = worldY + j1;
        if (flag) {
            if (rotation > 640 && rotation < 1408) {
                tileBottom += 128;
            }
            if (rotation > 1152 && rotation < 1920) {
                tileRight += 128;
            }
            if (rotation > 1664 || rotation < 384) {
                tileTop -= 128;
            }
            if (rotation > 128 && rotation < 896) {
                tileLeft -= 128;
            }
        }
        tileLeft /= 128;
        tileTop /= 128;
        tileRight /= 128;
        tileBottom /= 128;
        return addEntity(z, tileLeft, tileTop, (tileRight - tileLeft) + 1, (tileBottom - tileTop) + 1,
                worldX, worldY, worldZ, nodeToAdd, rotation, true, ui, (byte) 0, 0);
    }

    public boolean addSingleTileEntity(int z, int worldY, Renderable node, int rotation, int tileBottom, int worldX, int worldZ, int tileLeft, int tileRight, int ui, int tileTop) {
        return node == null || addEntity(z, tileLeft, tileTop, (tileRight - tileLeft) + 1, (tileBottom - tileTop) + 1,
                worldX, worldY, worldZ, node, rotation, true, ui, (byte) 0, 0);
    }

    private boolean addEntity(int z, int tileLeft, int tileTop, int tileRight, int tileBottom, int worldX, int worldY,
                              int worldZ, Renderable node, int rotation, boolean flag, int ui, byte objConf, int interactiveObjUID) {
        /**
         * Max entities on coord is 5 i guess
         */
        for (int _x = tileLeft; _x < tileLeft + tileRight; _x++) {
            for (int _y = tileTop; _y < tileTop + tileBottom; _y++) {
                if (_x < 0 || _y < 0 || _x >= xMapSize || _y >= yMapSize) {
                    return false;
                }
                Tile tile = groundArray[z][_x][_y];
                if (tile != null && tile.entityCount >= 5) {
                    return false;
                }
            }
        }

        StaticObject staticObject = new StaticObject();
        staticObject.uid = ui;
        staticObject.objConf = objConf;
        staticObject.zPos = z;
        staticObject.worldX = worldX;
        staticObject.worldY = worldY;
        staticObject.interactiveObjUID = interactiveObjUID;
        staticObject.worldZ = worldZ;
        staticObject.node = node;
        staticObject.rotation = rotation;
        staticObject.tileLeft = tileLeft;
        staticObject.tileTop = tileTop;
        staticObject.tileRight = (tileLeft + tileRight) - 1;
        staticObject.tileBottom = (tileTop + tileBottom) - 1;
        for (int x = tileLeft; x < tileLeft + tileRight; x++) {
            for (int y = tileTop; y < tileTop + tileBottom; y++) {
                int position = 0;
                if (x > tileLeft) {
                    position++;
                }
                if (x < (tileLeft + tileRight) - 1) {
                    position += 4;
                }
                if (y > tileTop) {
                    position += 8;
                }
                if (y < (tileTop + tileBottom) - 1) {
                    position += 2;
                }
                for (int zPtr = z; zPtr >= 0; zPtr--) {
                    if (groundArray[zPtr][x][y] == null) {
                        groundArray[zPtr][x][y] = new Tile(zPtr, x, y);
                    }
                }

                Tile tile = groundArray[z][x][y];
                tile.staticObjects[tile.entityCount] = staticObject;
                tile.anIntArray1319[tile.entityCount] = position;
                tile.anInt1320 |= position;
                tile.entityCount++;
            }
        }

        if (flag) {
            staticObjectCache[amountOfStaticObjects++] = staticObject;
        }
        return true;
    }

    public void clearStaticObjects() {
        for (int i = 0; i < amountOfStaticObjects; i++) {
            StaticObject iObject = staticObjectCache[i];
            updateObjectEntities(iObject);
            staticObjectCache[i] = null;
        }

        amountOfStaticObjects = 0;
    }

    private void updateObjectEntities(StaticObject iObject) {
        for (int j = iObject.tileLeft; j <= iObject.tileRight; j++) {
            for (int k = iObject.tileTop; k <= iObject.tileBottom; k++) {
                Tile tile = groundArray[iObject.zPos][j][k];
                if (tile != null) {
                    for (int l = 0; l < tile.entityCount; l++) {
                        if (tile.staticObjects[l] != iObject) {
                            continue;
                        }
                        tile.entityCount--;
                        for (int entityPtr = l; entityPtr < tile.entityCount; entityPtr++) {
                            tile.staticObjects[entityPtr] = tile.staticObjects[entityPtr + 1];
                            tile.anIntArray1319[entityPtr] = tile.anIntArray1319[entityPtr + 1];
                        }

                        tile.staticObjects[tile.entityCount] = null;
                        break;
                    }

                    tile.anInt1320 = 0;
                    for (int j1 = 0; j1 < tile.entityCount; j1++) {
                        tile.anInt1320 |= tile.anIntArray1319[j1];
                    }

                }
            }
        }

    }

    public void moveWallDecoration(int y, int moveAmt, int x, int z) {
        Tile tile = groundArray[z][x][y];
        if (tile == null) {
            return;
        }
        WallDecoration wallDec = tile.wallDecoration;
        if (wallDec != null) {
            int xCoord = x * 128 + 64;
            int yCoord = y * 128 + 64;
            wallDec.xPos = xCoord + ((wallDec.xPos - xCoord) * moveAmt) / 16;
            wallDec.yPos = yCoord + ((wallDec.yPos - yCoord) * moveAmt) / 16;
        }
    }

    public void removeWall(int x, int y, int z) {
        Tile tile = groundArray[y][x][z];
        if (tile != null) {
            tile.wall = null;
        }
    }

    public void removeWallDecoration(int y, int z, int x) {
        Tile tile = groundArray[z][x][y];
        if (tile != null) {
            tile.wallDecoration = null;
        }
    }

    public void removeStaticObject(int z, int x, int y) {
        Tile tile = groundArray[z][x][y];
        if (tile == null) {
            return;
        }
        for (int i = 0; i < tile.entityCount; i++) {
            StaticObject staticObject = tile.staticObjects[i];
            if ((staticObject.uid >> 29 & 3) == 2 && staticObject.tileLeft == x && staticObject.tileTop == y) {
                updateObjectEntities(staticObject);
                return;
            }
        }

    }

    public void removeGroundDecoration(int z, int y, int x) {
        Tile tile = groundArray[z][x][y];
        if (tile == null) {
            return;
        }
        tile.groundDecoration = null;
    }

    public void removeItemPileFromTile(int z, int x, int y) {
        Tile tile = groundArray[z][x][y];
        if (tile != null) {
            tile.itemPile = null;
        }
    }

    public Wall getWall(int z, int x, int y) {
        Tile tile = groundArray[z][x][y];
        if (tile == null) {
            return null;
        } else {
            return tile.wall;
        }
    }

    public WallDecoration getWallDecoration(int x, int y, int z) {
        Tile tile = groundArray[z][x][y];
        if (tile == null) {
            return null;
        } else {
            return tile.wallDecoration;
        }
    }

    public StaticObject getStaticObject(int x, int y, int z) {
        Tile tile = groundArray[z][x][y];
        if (tile == null) {
            return null;
        }
        for (int l = 0; l < tile.entityCount; l++) {
            StaticObject subObject = tile.staticObjects[l];
            if ((subObject.uid >> 29 & 3) == 2 && subObject.tileLeft == x && subObject.tileTop == y) {
                return subObject;
            }
        }
        return null;
    }

    public GroundDecoration getGroundDecoration(int y, int x, int z) {
        Tile tile = groundArray[z][x][y];
        if (tile == null || tile.groundDecoration == null) {
            return null;
        } else {
            return tile.groundDecoration;
        }
    }

    public int getWallKey(int z, int x, int y) {
        Tile tile = groundArray[z][x][y];
        if (tile == null || tile.wall == null) {
            return 0;
        } else {
            return tile.wall.uid;
        }
    }

    public int getWallDecorationKey(int z, int x, int y) {
        Tile tile = groundArray[z][x][y];
        if (tile == null || tile.wallDecoration == null) {
            return 0;
        } else {
            return tile.wallDecoration.uid;
        }
    }

    public int getStaticObjectKey(int plane, int x, int y) {
        if (groundArray.length < plane || groundArray[plane].length < x || groundArray[plane][x].length < y) {
            return 0;
        }
        Tile tile = groundArray[plane][x][y];
        if (tile == null) {
            return 0;
        }
        for (int i = 0; i < tile.entityCount; i++) {
            StaticObject staticObject = tile.staticObjects[i];
            if (staticObject.tileLeft == x && staticObject.tileTop == y) {
                return staticObject.uid;
            }
        }

        return 0;
    }

    public int getGroundDecorationUID(int z, int x, int y) {
        Tile tile = groundArray[z][x][y];
        if (tile == null || tile.groundDecoration == null) {
            return 0;
        } else {
            return tile.groundDecoration.uid;
        }
    }

    public int getIDTagForXYZ(int z, int x, int y, int uidMatch) {
        Tile tile = groundArray[z][x][y];
        if (tile == null) {
            return -1;
        }
        if (tile.wall != null && tile.wall.uid == uidMatch) {
            return tile.wall.objConfig & 0xff;
        }
        if (tile.wallDecoration != null && tile.wallDecoration.uid == uidMatch) {
            return tile.wallDecoration.objConfig & 0xff;
        }
        if (tile.groundDecoration != null && tile.groundDecoration.uid == uidMatch) {
            return tile.groundDecoration.objConfig & 0xff;
        }
        for (int entityPtr = 0; entityPtr < tile.entityCount; entityPtr++) {
            if (tile.staticObjects[entityPtr].uid == uidMatch) {
                return tile.staticObjects[entityPtr].objConf & 0xff;
            }
        }
        return -1;
    }

    public void shadeModels(int i, int k, int i1) {
        int j = 100;
        int l = 5500;
        int j1 = (int) Math.sqrt(k * k + i * i + i1 * i1);
        int k1 = l >> 4;
        for (int l1 = 0; l1 < zMapSize; l1++) {
            for (int i2 = 0; i2 < xMapSize; i2++) {
                for (int j2 = 0; j2 < yMapSize; j2++) {
                    Tile class30_sub3 = groundArray[l1][i2][j2];
                    if (class30_sub3 != null) {
                        Wall class10 = class30_sub3.wall;
                        if (class10 != null && class10.node1 != null && class10.node1.vertexes != null) {
                            mergeModels(l1, 1, 1, i2, j2, (Model) class10.node1);
                            if (class10.node2 != null && class10.node2.vertexes != null) {
                                mergeModels(l1, 1, 1, i2, j2, (Model) class10.node2);
                                mergeNormals((Model) class10.node1, (Model) class10.node2, 0, 0, 0, false);
                                ((Model) class10.node2).method480(j, k1, k, i, i1);
                            }
                            ((Model) class10.node1).method480(j, k1, k, i, i1);
                        }
                        for (int k2 = 0; k2 < class30_sub3.entityCount; k2++) {
                            StaticObject class28 = class30_sub3.staticObjects[k2];
                            if (class28 != null && class28.node != null && class28.node.vertexes != null) {
                                mergeModels(l1, (class28.tileRight - class28.tileLeft) + 1, (class28.tileBottom - class28.tileTop) + 1, i2, j2, (Model) class28.node);
                                ((Model) class28.node).method480(j, k1, k, i, i1);
                            }
                        }

                        GroundDecoration class49 = class30_sub3.groundDecoration;
                        if (class49 != null && class49.node.vertexes != null) {
                            renderGrounDec(i2, l1, (Model) class49.node, j2);
                            ((Model) class49.node).method480(j, k1, k, i, i1);
                        }
                    }
                }
            }
        }
    }

    private void renderGrounDec(int i, int j, Model model, int k) {
        if (i < xMapSize) {
            Tile class30_sub3 = groundArray[j][i + 1][k];
            if (class30_sub3 != null && class30_sub3.groundDecoration != null && class30_sub3.groundDecoration.node.vertexes != null) {
                mergeNormals(model, (Model) class30_sub3.groundDecoration.node, 128, 0, 0, true);
            }
        }
        if (k < xMapSize) {
            Tile class30_sub3_1 = groundArray[j][i][k + 1];
            if (class30_sub3_1 != null && class30_sub3_1.groundDecoration != null && class30_sub3_1.groundDecoration.node.vertexes != null) {
                mergeNormals(model, (Model) class30_sub3_1.groundDecoration.node, 0, 0, 128, true);
            }
        }
        if (i < xMapSize && k < yMapSize) {
            Tile class30_sub3_2 = groundArray[j][i + 1][k + 1];
            if (class30_sub3_2 != null && class30_sub3_2.groundDecoration != null && class30_sub3_2.groundDecoration.node.vertexes != null) {
                mergeNormals(model, (Model) class30_sub3_2.groundDecoration.node, 128, 0, 128, true);
            }
        }
        if (i < xMapSize && k > 0) {
            Tile class30_sub3_3 = groundArray[j][i + 1][k - 1];
            if (class30_sub3_3 != null && class30_sub3_3.groundDecoration != null && class30_sub3_3.groundDecoration.node.vertexes != null) {
                mergeNormals(model, (Model) class30_sub3_3.groundDecoration.node, 128, 0, -128, true);
            }
        }
    }

    private void mergeModels(int z, int j, int k, int x, int y, Model model) {
        boolean flag = true;
        int j1 = x;
        int k1 = x + j;
        int l1 = y - 1;
        int i2 = y + k;
        for (int j2 = z; j2 <= z + 1; j2++) {
            if (j2 != zMapSize) {
                for (int k2 = j1; k2 <= k1; k2++) {
                    if (k2 >= 0 && k2 < xMapSize) {
                        for (int l2 = l1; l2 <= i2; l2++) {
                            if (l2 >= 0 && l2 < yMapSize && (!flag || k2 >= k1 || l2 >= i2 || l2 < y && k2 != x)) {
                                Tile class30_sub3 = groundArray[j2][k2][l2];
                                if (class30_sub3 != null) {
                                    int i3 = (heightMap[j2][k2][l2] + heightMap[j2][k2 + 1][l2] + heightMap[j2][k2][l2 + 1] + heightMap[j2][k2 + 1][l2 + 1]) / 4 - (heightMap[z][x][y] + heightMap[z][x + 1][y] + heightMap[z][x][y + 1] + heightMap[z][x + 1][y + 1]) / 4;
                                    Wall class10 = class30_sub3.wall;
                                    if (class10 != null && class10.node1 != null && class10.node1.vertexes != null) {
                                        mergeNormals(model, (Model) class10.node1, (k2 - x) * 128 + (1 - j) * 64, i3, (l2 - y) * 128 + (1 - k) * 64, flag);
                                    }
                                    if (class10 != null && class10.node2 != null && class10.node2.vertexes != null) {
                                        mergeNormals(model, (Model) class10.node2, (k2 - x) * 128 + (1 - j) * 64, i3, (l2 - y) * 128 + (1 - k) * 64, flag);
                                    }
                                    for (int j3 = 0; j3 < class30_sub3.entityCount; j3++) {
                                        StaticObject class28 = class30_sub3.staticObjects[j3];
                                        if (class28 != null && class28.node != null && class28.node.vertexes != null) {
                                            int k3 = (class28.tileRight - class28.tileLeft) + 1;
                                            int l3 = (class28.tileBottom - class28.tileTop) + 1;
                                            mergeNormals(model, (Model) class28.node, (class28.tileLeft - x) * 128 + (k3 - j) * 64, i3, (class28.tileTop - y) * 128 + (l3 - k) * 64, flag);
                                        }
                                    }

                                }
                            }
                        }

                    }
                }

                j1--;
                flag = false;
            }
        }

    }

    private void mergeNormals(Model model, Model model_1, int i, int j, int k, boolean flag) {
        anInt488++;
        int l = 0;
        int ai[] = model_1.verticesX;
        int amtOfVertices = model_1.vertexCount;
        for (int verticeId = 0; verticeId < model.vertexCount; verticeId++) {
            Vertex vertexNormal = model.vertexes[verticeId];
            Vertex vertexNormalOff = model.vertexOffset[verticeId];
            if (vertexNormalOff.anInt605 != 0) {
                int vertY = model.verticesY[verticeId] - j;
                if (vertY <= model_1.anInt1651) {
                    int vertX = model.verticesX[verticeId] - i;
                    if (vertX >= model_1.anInt1646 && vertX <= model_1.anInt1647) {
                        int vertZ = model.verticesZ[verticeId] - k;
                        if (vertZ >= model_1.anInt1649 && vertZ <= model_1.anInt1648) {
                            for (int vertId_1 = 0; vertId_1 < amtOfVertices; vertId_1++) {
                                Vertex class33_2 = model_1.vertexes[vertId_1];
                                Vertex class33_3 = model_1.vertexOffset[vertId_1];
                                if (vertX == ai[vertId_1] && vertZ == model_1.verticesZ[vertId_1] && vertY == model_1.verticesY[vertId_1] && class33_3.anInt605 != 0) {
                                    vertexNormal.anInt602 += class33_3.anInt602;
                                    vertexNormal.anInt603 += class33_3.anInt603;
                                    vertexNormal.anInt604 += class33_3.anInt604;
                                    vertexNormal.anInt605 += class33_3.anInt605;
                                    class33_2.anInt602 += vertexNormalOff.anInt602;
                                    class33_2.anInt603 += vertexNormalOff.anInt603;
                                    class33_2.anInt604 += vertexNormalOff.anInt604;
                                    class33_2.anInt605 += vertexNormalOff.anInt605;
                                    l++;
                                    anIntArray486[verticeId] = anInt488;
                                    anIntArray487[vertId_1] = anInt488;
                                }
                            }

                        }
                    }
                }
            }
        }

        if (l < 3 || !flag) {
            return;
        }
        for (int k1 = 0; k1 < model.triangleCount; k1++) {
            if (anIntArray486[model.trianglePointsX[k1]] == anInt488 && anIntArray486[model.trianglePointsY[k1]] == anInt488 && anIntArray486[model.trianglePointsZ[k1]] == anInt488) {
                model.texturePoints[k1] = -1;
            }
        }

        for (int l1 = 0; l1 < model_1.triangleCount; l1++) {
            if (anIntArray487[model_1.trianglePointsX[l1]] == anInt488 && anIntArray487[model_1.trianglePointsY[l1]] == anInt488 && anIntArray487[model_1.trianglePointsZ[l1]] == anInt488) {
                model_1.texturePoints[l1] = -1;
            }
        }

    }

    public void drawTileMinimap(int ai[], int i, int k, int l, int i1) {
        int j = 512;//was parameter
        Tile class30_sub3 = groundArray[k][l][i1];
        if (class30_sub3 == null) {
            return;
        }
        SimpleTile class43 = class30_sub3.simpleTile;
        if (class43 != null) {

            if (hdMinimap && class43.color1 != 12345678) {
                if (class43.rgbColour == 0) {
                    return;
                }
                int hs = class43.color1 & ~0x7f;
                int l1 = class43.color4 & 0x7f;
                int l2 = class43.color3 & 0x7f;
                int l3 = (class43.color1 & 0x7f) - l1;
                int l4 = (class43.color2 & 0x7f) - l2;
                l1 <<= 2;
                l2 <<= 2;
                for (int k1 = 0; k1 < 4; k1++) {
                    if (!class43.textured) {
                        ai[i] = Rasterizer.hsl2rgb[hs | (l1 >> 2)];
                        ai[i + 1] = Rasterizer.hsl2rgb[hs | (l1 * 3 + l2 >> 4)];
                        ai[i + 2] = Rasterizer.hsl2rgb[hs | (l1 + l2 >> 3)];
                        ai[i + 3] = Rasterizer.hsl2rgb[hs | (l1 + l2 * 3 >> 4)];
                    } else {
                        int j1 = class43.rgbColour;
                        int lig = 0xff - ((l1 >> 1) * (l1 >> 1) >> 8);
                        ai[i] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
                        lig = 0xff - ((l1 * 3 + l2 >> 3) * (l1 * 3 + l2 >> 3) >> 8);
                        ai[i + 1] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
                        lig = 0xff - ((l1 + l2 >> 2) * (l1 + l2 >> 2) >> 8);
                        ai[i + 2] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
                        lig = 0xff - ((l1 + l2 * 3 >> 3) * (l1 + l2 * 3 >> 3) >> 8);
                        ai[i + 3] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
                    }
                    l1 += l3;
                    l2 += l4;
                    i += 512;
                }
                return;
            }
            int j1 = class43.rgbColour;
            if (j1 == 0) {
                return;
            }
            for (int k1 = 0; k1 < 4; k1++) {
                ai[i] = j1;
                ai[i + 1] = j1;
                ai[i + 2] = j1;
                ai[i + 3] = j1;
                i += 512;
            }

            return;
        }
        ShapedTile class40 = class30_sub3.shapedTile;
        if (class40 == null) {
            return;
        }
        int l1 = class40.shape;
        int i2 = class40.rotation;
        int j2 = class40.colourRGB;
        int k2 = class40.colourRGBA;
        int ai1[] = tileSHapePoints[l1];
        int ai2[] = tileShapeIndices[i2];
        int l2 = 0;
        if (hdMinimap && class40.color62 != 12345678) {
            int hs1 = class40.color62 & ~0x7f;
            int l11 = class40.color92 & 0x7f;
            int l21 = class40.color82 & 0x7f;
            int l31 = (class40.color62 & 0x7f) - l11;
            int l41 = (class40.color72 & 0x7f) - l21;
            l11 <<= 2;
            l21 <<= 2;
            for (int k1 = 0; k1 < 4; k1++) {
                if (!class40.textured) {
                    if (ai1[ai2[l2++]] != 0) {
                        ai[i] = Rasterizer.hsl2rgb[hs1 | (l11 >> 2)];
                    }
                    if (ai1[ai2[l2++]] != 0) {
                        ai[i + 1] = Rasterizer.hsl2rgb[hs1 | (l11 * 3 + l21 >> 4)];
                    }
                    if (ai1[ai2[l2++]] != 0) {
                        ai[i + 2] = Rasterizer.hsl2rgb[hs1 | (l11 + l21 >> 3)];
                    }
                    if (ai1[ai2[l2++]] != 0) {
                        ai[i + 3] = Rasterizer.hsl2rgb[hs1 | (l11 + l21 * 3 >> 4)];
                    }
                } else {
                    int j1 = k2;
                    if (ai1[ai2[l2++]] != 0) {
                        int lig = 0xff - ((l11 >> 1) * (l11 >> 1) >> 8);
                        ai[i] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
                    }
                    if (ai1[ai2[l2++]] != 0) {
                        int lig = 0xff - ((l11 * 3 + l21 >> 3) * (l11 * 3 + l21 >> 3) >> 8);
                        ai[i + 1] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
                    }
                    if (ai1[ai2[l2++]] != 0) {
                        int lig = 0xff - ((l11 + l21 >> 2) * (l11 + l21 >> 2) >> 8);
                        ai[i + 2] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
                    }
                    if (ai1[ai2[l2++]] != 0) {
                        int lig = 0xff - ((l11 + l21 * 3 >> 3) * (l11 + l21 * 3 >> 3) >> 8);
                        ai[i + 3] = ((j1 & 0xff00ff) * lig & ~0xff00ff) + ((j1 & 0xff00) * lig & 0xff0000) >> 8;
                    }
                }
                l11 += l31;
                l21 += l41;
                i += 512;
            }
            if (j2 != 0 && class40.color61 != 12345678) {
                i -= 512 << 2;
                l2 -= 16;
                hs1 = class40.color61 & ~0x7f;
                l11 = class40.color91 & 0x7f;
                l21 = class40.color81 & 0x7f;
                l31 = (class40.color61 & 0x7f) - l11;
                l41 = (class40.color71 & 0x7f) - l21;
                l11 <<= 2;
                l21 <<= 2;
                for (int k1 = 0; k1 < 4; k1++) {
                    if (ai1[ai2[l2++]] == 0) {
                        ai[i] = Rasterizer.hsl2rgb[hs1 | (l11 >> 2)];
                    }
                    if (ai1[ai2[l2++]] == 0) {
                        ai[i + 1] = Rasterizer.hsl2rgb[hs1 | (l11 * 3 + l21 >> 4)];
                    }
                    if (ai1[ai2[l2++]] == 0) {
                        ai[i + 2] = Rasterizer.hsl2rgb[hs1 | (l11 + l21 >> 3)];
                    }
                    if (ai1[ai2[l2++]] == 0) {
                        ai[i + 3] = Rasterizer.hsl2rgb[hs1 | (l11 + l21 * 3 >> 4)];
                    }
                    l11 += l31;
                    l21 += l41;
                    i += 512;
                }
            }
            return;
        }
        if (j2 != 0) {
            for (int i3 = 0; i3 < 4; i3++) {
                ai[i] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                ai[i + 1] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                ai[i + 2] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                ai[i + 3] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                i += 512;
            }

            return;
        }
        for (int j3 = 0; j3 < 4; j3++) {
            if (ai1[ai2[l2++]] != 0) {
                ai[i] = k2;
            }
            if (ai1[ai2[l2++]] != 0) {
                ai[i + 1] = k2;
            }
            if (ai1[ai2[l2++]] != 0) {
                ai[i + 2] = k2;
            }
            if (ai1[ai2[l2++]] != 0) {
                ai[i + 3] = k2;
            }
            i += 512;
        }

    }

    public void request2DTrace(int x, int y) {
        isClicked = true;
        clickX = y;
        clickY = x;
        clickedTileX = -1;
        clickedTileY = -1;
    }

    public void render(int xCam, int yCam, int xCurve, int zCam, int plane, int yCurve) {
        Rasterizer.saveDepth = Game.INSTANCE.getInterfaceConfig(InterfaceConfiguration.FOG) == 1;
        Rasterizer.clearDepthBuffer();
        if (xCam < 0) {
            xCam = 0;
        } else if (xCam >= xMapSize * 128) {
            xCam = xMapSize * 128 - 1;
        }
        if (yCam < 0) {
            yCam = 0;
        } else if (yCam >= yMapSize * 128) {
            yCam = yMapSize * 128 - 1;
        }
        anInt448++;
        yCurveSin = Model.SINE[yCurve];
        yCurveCos = Model.COSINE[yCurve];
        xCurveSin = Model.SINE[xCurve];
        xCurveCos = Model.COSINE[xCurve];
        tile_visibility_map = tile_visibility_maps[(yCurve - 128) / 32][xCurve / 64];
        xCamPos = xCam;
        zCamPos = zCam;
        yCamPos = yCam;
        xCamPosTile = xCam / 128;
        yCamPosTile = yCam / 128;
        plane__ = plane;
        anInt449 = xCamPosTile - mapViewDistance;
        if (anInt449 < 0) {
            anInt449 = 0;
        }
        anInt451 = yCamPosTile - mapViewDistance;
        if (anInt451 < 0) {
            anInt451 = 0;
        }
        anInt450 = xCamPosTile + mapViewDistance;
        if (anInt450 > xMapSize) {
            anInt450 = xMapSize;
        }
        anInt452 = yCamPosTile + mapViewDistance;
        if (anInt452 > yMapSize) {
            anInt452 = yMapSize;
        }
        processCulling();
        anInt446 = 0;
        for (int k1 = currentHL; k1 < zMapSize; k1++) {
            Tile tiles[][] = groundArray[k1];
            for (int x_ = anInt449; x_ < anInt450; x_++) {
                for (int y_ = anInt451; y_ < anInt452; y_++) {
                    Tile tile = tiles[x_][y_];
                    if (tile != null) {
                        if (tile.logicHeight > plane || !tile_visibility_map[(x_ - xCamPosTile) + mapViewDistance][(y_ - yCamPosTile) + mapViewDistance] && heightMap[k1][x_][y_] - zCam < 2000) {
                            tile.aBoolean1322 = false;
                            tile.aBoolean1323 = false;
                            tile.anInt1325 = 0;
                        } else {
                            tile.aBoolean1322 = true;
                            tile.aBoolean1323 = true;
                            tile.aBoolean1324 = tile.entityCount > 0;
                            anInt446++;
                        }
                    }
                }

            }
        }

        for (int l1 = currentHL; l1 < zMapSize; l1++) {
            Tile tiles[][] = groundArray[l1];
            for (int l2 = -mapViewDistance; l2 <= 0; l2++) {
                int i3 = xCamPosTile + l2;
                int k3 = xCamPosTile - l2;
                if (i3 >= anInt449 || k3 < anInt450) {
                    for (int i4 = -mapViewDistance; i4 <= 0; i4++) {
                        int k4 = yCamPosTile + i4;
                        int i5 = yCamPosTile - i4;
                        if (i3 >= anInt449) {
                            if (k4 >= anInt451) {
                                Tile class30_sub3_1 = tiles[i3][k4];
                                if (class30_sub3_1 != null && class30_sub3_1.aBoolean1322) {
                                    renderTile(class30_sub3_1, true);
                                }
                            }
                            if (i5 < anInt452) {
                                Tile tileRender = tiles[i3][i5];
                                if (tileRender != null && tileRender.aBoolean1322) {
                                    renderTile(tileRender, true);
                                }
                            }
                        }
                        if (k3 < anInt450) {
                            if (k4 >= anInt451) {
                                Tile class30_sub3_3 = tiles[k3][k4];
                                if (class30_sub3_3 != null && class30_sub3_3.aBoolean1322) {
                                    renderTile(class30_sub3_3, true);
                                }
                            }
                            if (i5 < anInt452) {
                                Tile class30_sub3_4 = tiles[k3][i5];
                                if (class30_sub3_4 != null && class30_sub3_4.aBoolean1322) {
                                    renderTile(class30_sub3_4, true);
                                }
                            }
                        }
                        if (anInt446 == 0) {
                            isClicked = false;
                            Rasterizer.drawFog(Constants.FOG_BEGIN_DEPTH, Constants.FOG_END_DEPTH, Constants.FOG_COLOUR);
                            Rasterizer.saveDepth = false;
                            return;
                        }
                    }

                }
            }
        }

        for (int j2 = currentHL; j2 < zMapSize; j2++) {
            Tile aclass30_sub3_2[][] = groundArray[j2];
            for (int j3 = -mapViewDistance; j3 <= 0; j3++) {
                int l3 = xCamPosTile + j3;
                int j4 = xCamPosTile - j3;
                if (l3 >= anInt449 || j4 < anInt450) {
                    for (int l4 = -mapViewDistance; l4 <= 0; l4++) {
                        int j5 = yCamPosTile + l4;
                        int k5 = yCamPosTile - l4;
                        if (l3 >= anInt449) {
                            if (j5 >= anInt451) {
                                Tile class30_sub3_5 = aclass30_sub3_2[l3][j5];
                                if (class30_sub3_5 != null && class30_sub3_5.aBoolean1322) {
                                    renderTile(class30_sub3_5, false);
                                }
                            }
                            if (k5 < anInt452) {
                                Tile class30_sub3_6 = aclass30_sub3_2[l3][k5];
                                if (class30_sub3_6 != null && class30_sub3_6.aBoolean1322) {
                                    renderTile(class30_sub3_6, false);
                                }
                            }
                        }
                        if (j4 < anInt450) {
                            if (j5 >= anInt451) {
                                Tile class30_sub3_7 = aclass30_sub3_2[j4][j5];
                                if (class30_sub3_7 != null && class30_sub3_7.aBoolean1322) {
                                    renderTile(class30_sub3_7, false);
                                }
                            }
                            if (k5 < anInt452) {
                                Tile tile = aclass30_sub3_2[j4][k5];
                                if (tile != null && tile.aBoolean1322) {
                                    renderTile(tile, false);
                                }
                            }
                        }
                        if (anInt446 == 0) {
                            isClicked = false;
                            Rasterizer.drawFog(Constants.FOG_BEGIN_DEPTH, Constants.FOG_END_DEPTH, 0xc8c0a8);
                            Rasterizer.saveDepth = false;
                            return;
                        }
                    }

                }
            }
        }
        isClicked = false;
        Rasterizer.drawFog(Constants.FOG_BEGIN_DEPTH, Constants.FOG_END_DEPTH, 0xc8c0a8);
        Rasterizer.saveDepth = false;
    }

    private void renderTile(Tile mainTile, boolean flag) {
        tileDeque.insertHead(mainTile);
        do {
            Tile tile1;
            do {
                tile1 = (Tile) tileDeque.popHead();
                if (tile1 == null) {
                    return;
                }
            } while (!tile1.aBoolean1323);
            int i = tile1.tileX;
            int j = tile1.tileY;
            int k = tile1.tileZ;
            int l = tile1.plane;
            Tile tiles[][] = groundArray[k];
            if (tile1.aBoolean1322) {
                if (flag) {
                    if (k > 0) {
                        Tile tile = groundArray[k - 1][i][j];
                        if (tile != null && tile.aBoolean1323) {
                            continue;
                        }
                    }
                    if (i <= xCamPosTile && i > anInt449) {
                        Tile tile = tiles[i - 1][j];
                        if (tile != null && tile.aBoolean1323 && (tile.aBoolean1322 || (tile1.anInt1320 & 1) == 0)) {
                            continue;
                        }
                    }
                    if (i >= xCamPosTile && i < anInt450 - 1) {
                        Tile tile = tiles[i + 1][j];
                        if (tile != null && tile.aBoolean1323 && (tile.aBoolean1322 || (tile1.anInt1320 & 4) == 0)) {
                            continue;
                        }
                    }
                    if (j <= yCamPosTile && j > anInt451) {
                        Tile class30_sub3_5 = tiles[i][j - 1];
                        if (class30_sub3_5 != null && class30_sub3_5.aBoolean1323 && (class30_sub3_5.aBoolean1322 || (tile1.anInt1320 & 8) == 0)) {
                            continue;
                        }
                    }
                    if (j >= yCamPosTile && j < anInt452 - 1) {
                        Tile class30_sub3_6 = tiles[i][j + 1];
                        if (class30_sub3_6 != null && class30_sub3_6.aBoolean1323 && (class30_sub3_6.aBoolean1322 || (tile1.anInt1320 & 2) == 0)) {
                            continue;
                        }
                    }
                } else {
                    flag = true;
                }
                tile1.aBoolean1322 = false;
                if (tile1.tileBelowThisTile != null) {
                    Tile lowerTile = tile1.tileBelowThisTile;
                    if (lowerTile.simpleTile != null) {
                        if (!method320(0, i, j)) {
                            drawSimpleTile(lowerTile.simpleTile, 0, yCurveSin, yCurveCos, xCurveSin, xCurveCos, i, j);
                        }
                    } else if (lowerTile.shapedTile != null && !method320(0, i, j)) {
                        drawShapedTile(i, yCurveSin, xCurveSin, lowerTile.shapedTile, yCurveCos, j, xCurveCos);
                    }
                    Wall wall = lowerTile.wall;
                    if (wall != null) {
                        wall.node1.renderAtPoint(0, yCurveSin, yCurveCos, xCurveSin, xCurveCos, wall.xPos - xCamPos, wall.zPos - zCamPos, wall.yPos - yCamPos, wall.uid, wall.wallObjUID);
                    }
                    for (int i2 = 0; i2 < lowerTile.entityCount; i2++) {
                        StaticObject iObject = lowerTile.staticObjects[i2];
                        if (iObject != null) {
                            iObject.node.renderAtPoint(iObject.rotation, yCurveSin, yCurveCos, xCurveSin, xCurveCos, iObject.worldX - xCamPos, iObject.worldZ - zCamPos, iObject.worldY - yCamPos, iObject.uid, iObject.interactiveObjUID);
                        }
                    }

                }
                boolean flag1 = false;
                if (tile1.simpleTile != null) {
                    if (!method320(l, i, j)) {
                        flag1 = true;
                        drawSimpleTile(tile1.simpleTile, l, yCurveSin, yCurveCos, xCurveSin, xCurveCos, i, j);
                    }
                } else if (tile1.shapedTile != null && !method320(l, i, j)) {
                    flag1 = true;
                    drawShapedTile(i, yCurveSin, xCurveSin, tile1.shapedTile, yCurveCos, j, xCurveCos);
                }
                int j1 = 0;
                int j2 = 0;
                Wall class10_3 = tile1.wall;
                WallDecoration class26_1 = tile1.wallDecoration;
                if (class10_3 != null || class26_1 != null) {
                    if (xCamPosTile == i) {
                        j1++;
                    } else if (xCamPosTile < i) {
                        j1 += 2;
                    }
                    if (yCamPosTile == j) {
                        j1 += 3;
                    } else if (yCamPosTile > j) {
                        j1 += 6;
                    }
                    j2 = anIntArray478[j1];
                    tile1.anInt1328 = anIntArray480[j1];
                }
                if (class10_3 != null) {
                    if ((class10_3.orientation & anIntArray479[j1]) != 0) {
                        if (class10_3.orientation == 16) {
                            tile1.anInt1325 = 3;
                            tile1.anInt1326 = anIntArray481[j1];
                            tile1.anInt1327 = 3 - tile1.anInt1326;
                        } else if (class10_3.orientation == 32) {
                            tile1.anInt1325 = 6;
                            tile1.anInt1326 = anIntArray482[j1];
                            tile1.anInt1327 = 6 - tile1.anInt1326;
                        } else if (class10_3.orientation == 64) {
                            tile1.anInt1325 = 12;
                            tile1.anInt1326 = anIntArray483[j1];
                            tile1.anInt1327 = 12 - tile1.anInt1326;
                        } else {
                            tile1.anInt1325 = 9;
                            tile1.anInt1326 = anIntArray484[j1];
                            tile1.anInt1327 = 9 - tile1.anInt1326;
                        }
                    } else {
                        tile1.anInt1325 = 0;
                    }
                    if ((class10_3.orientation & j2) != 0 && !method321(l, i, j, class10_3.orientation)) {
                        class10_3.node1.renderAtPoint(0, yCurveSin, yCurveCos, xCurveSin, xCurveCos, class10_3.xPos - xCamPos, class10_3.zPos - zCamPos, class10_3.yPos - yCamPos, class10_3.uid, class10_3.wallObjUID);
                    }
                    if ((class10_3.orientation1 & j2) != 0 && !method321(l, i, j, class10_3.orientation1)) {
                        class10_3.node2.renderAtPoint(0, yCurveSin, yCurveCos, xCurveSin, xCurveCos, class10_3.xPos - xCamPos, class10_3.zPos - zCamPos, class10_3.yPos - yCamPos, class10_3.uid, class10_3.wallObjUID);
                    }
                }
                if (class26_1 != null && !method322(l, i, j, class26_1.node.modelHeight)) {
                    if ((class26_1.configurationBits & j2) != 0) {
                        class26_1.node.renderAtPoint(class26_1.rotation, yCurveSin, yCurveCos, xCurveSin, xCurveCos, class26_1.xPos - xCamPos, class26_1.zPos - zCamPos, class26_1.yPos - yCamPos, class26_1.uid, class26_1.wallDecorUID);
                    } else if ((class26_1.configurationBits & 0x300) != 0) {
                        int j4 = class26_1.xPos - xCamPos;
                        int l5 = class26_1.zPos - zCamPos;
                        int k6 = class26_1.yPos - yCamPos;
                        int i8 = class26_1.rotation;
                        int k9;
                        if (i8 == 1 || i8 == 2) {
                            k9 = -j4;
                        } else {
                            k9 = j4;
                        }
                        int k10;
                        if (i8 == 2 || i8 == 3) {
                            k10 = -k6;
                        } else {
                            k10 = k6;
                        }
                        if ((class26_1.configurationBits & 0x100) != 0 && k10 < k9) {
                            int i11 = j4 + faceXoffset2[i8];
                            int k11 = k6 + faceYOffset2[i8];
                            class26_1.node.renderAtPoint(i8 * 512 + 256, yCurveSin, yCurveCos, xCurveSin, xCurveCos, i11, l5, k11, class26_1.uid, class26_1.wallDecorUID);
                        }
                        if ((class26_1.configurationBits & 0x200) != 0 && k10 > k9) {
                            int j11 = j4 + faceXOffset3[i8];
                            int l11 = k6 + faceYOffset3[i8];
                            class26_1.node.renderAtPoint(i8 * 512 + 1280 & 0x7ff, yCurveSin, yCurveCos, xCurveSin, xCurveCos, j11, l5, l11, class26_1.uid, class26_1.wallDecorUID);
                        }
                    }
                }
                if (flag1) {
                    GroundDecoration class49 = tile1.groundDecoration;
                    if (class49 != null) {
                        class49.node.renderAtPoint(0, yCurveSin, yCurveCos, xCurveSin, xCurveCos, class49.xPos - xCamPos, class49.zPos - zCamPos, class49.yPos - yCamPos, class49.uid, class49.groundDecorUID);
                    }
                    ItemPile itemPile = tile1.itemPile;
                    if (itemPile != null && itemPile.topItem == 0) {
                        if (itemPile.secondItemPile != null) {
                            itemPile.secondItemPile.renderAtPoint(0, yCurveSin, yCurveCos, xCurveSin, xCurveCos, itemPile.xPos - xCamPos, itemPile.zPos - zCamPos, itemPile.yPos - yCamPos, itemPile.uid, itemPile.newUid);
                        }
                        if (itemPile.thirdItemPile != null) {
                            itemPile.thirdItemPile.renderAtPoint(0, yCurveSin, yCurveCos, xCurveSin, xCurveCos, itemPile.xPos - xCamPos, itemPile.zPos - zCamPos, itemPile.yPos - yCamPos, itemPile.uid, itemPile.newUid);
                        }
                        if (itemPile.firstItemPile != null) {
                            itemPile.firstItemPile.renderAtPoint(0, yCurveSin, yCurveCos, xCurveSin, xCurveCos, itemPile.xPos - xCamPos, itemPile.zPos - zCamPos, itemPile.yPos - yCamPos, itemPile.uid, itemPile.newUid);
                        }
                    }
                }
                int k4 = tile1.anInt1320;
                if (k4 != 0) {
                    if (i < xCamPosTile && (k4 & 4) != 0) {
                        Tile class30_sub3_17 = tiles[i + 1][j];
                        if (class30_sub3_17 != null && class30_sub3_17.aBoolean1323) {
                            tileDeque.insertHead(class30_sub3_17);
                        }
                    }
                    if (j < yCamPosTile && (k4 & 2) != 0) {
                        Tile class30_sub3_18 = tiles[i][j + 1];
                        if (class30_sub3_18 != null && class30_sub3_18.aBoolean1323) {
                            tileDeque.insertHead(class30_sub3_18);
                        }
                    }
                    if (i > xCamPosTile && (k4 & 1) != 0) {
                        Tile class30_sub3_19 = tiles[i - 1][j];
                        if (class30_sub3_19 != null && class30_sub3_19.aBoolean1323) {
                            tileDeque.insertHead(class30_sub3_19);
                        }
                    }
                    if (j > yCamPosTile && (k4 & 8) != 0) {
                        Tile class30_sub3_20 = tiles[i][j - 1];
                        if (class30_sub3_20 != null && class30_sub3_20.aBoolean1323) {
                            tileDeque.insertHead(class30_sub3_20);
                        }
                    }
                }
            }
            if (tile1.anInt1325 != 0) {
                boolean flag2 = true;
                for (int k1 = 0; k1 < tile1.entityCount; k1++) {
                    if (tile1.staticObjects[k1].height == anInt448 || (tile1.anIntArray1319[k1] & tile1.anInt1325) != tile1.anInt1326) {
                        continue;
                    }
                    flag2 = false;
                    break;
                }

                if (flag2) {
                    Wall class10_1 = tile1.wall;
                    if (!method321(l, i, j, class10_1.orientation)) {
                        class10_1.node1.renderAtPoint(0, yCurveSin, yCurveCos, xCurveSin, xCurveCos, class10_1.xPos - xCamPos, class10_1.zPos - zCamPos, class10_1.yPos - yCamPos, class10_1.uid, class10_1.wallObjUID);
                    }
                    tile1.anInt1325 = 0;
                }
            }
            if (tile1.aBoolean1324) {
                try {
                    int i1 = tile1.entityCount;
                    tile1.aBoolean1324 = false;
                    int l1 = 0;
                    label0:
                    for (int k2 = 0; k2 < i1; k2++) {
                        StaticObject class28_1 = tile1.staticObjects[k2];
                        if (class28_1.height == anInt448) {
                            continue;
                        }
                        for (int k3 = class28_1.tileLeft; k3 <= class28_1.tileRight; k3++) {
                            for (int l4 = class28_1.tileTop; l4 <= class28_1.tileBottom; l4++) {
                                Tile class30_sub3_21 = tiles[k3][l4];
                                if (class30_sub3_21.aBoolean1322) {
                                    tile1.aBoolean1324 = true;
                                } else {
                                    if (class30_sub3_21.anInt1325 == 0) {
                                        continue;
                                    }
                                    int l6 = 0;
                                    if (k3 > class28_1.tileLeft) {
                                        l6++;
                                    }
                                    if (k3 < class28_1.tileRight) {
                                        l6 += 4;
                                    }
                                    if (l4 > class28_1.tileTop) {
                                        l6 += 8;
                                    }
                                    if (l4 < class28_1.tileBottom) {
                                        l6 += 2;
                                    }
                                    if ((l6 & class30_sub3_21.anInt1325) != tile1.anInt1327) {
                                        continue;
                                    }
                                    tile1.aBoolean1324 = true;
                                }
                                continue label0;
                            }

                        }

                        staticObjects[l1++] = class28_1;
                        int i5 = xCamPosTile - class28_1.tileLeft;
                        int i6 = class28_1.tileRight - xCamPosTile;
                        if (i6 > i5) {
                            i5 = i6;
                        }
                        int i7 = yCamPosTile - class28_1.tileTop;
                        int j8 = class28_1.tileBottom - yCamPosTile;
                        if (j8 > i7) {
                            class28_1.anInt527 = i5 + j8;
                        } else {
                            class28_1.anInt527 = i5 + i7;
                        }
                    }

                    while (l1 > 0) {
                        int i3 = -(mapViewDistance << 1);
                        int l3 = -1;
                        for (int j5 = 0; j5 < l1; j5++) {
                            StaticObject class28_2 = staticObjects[j5];
                            if (class28_2.height != anInt448) {
                                if (class28_2.anInt527 > i3) {
                                    i3 = class28_2.anInt527;
                                    l3 = j5;
                                } else if (class28_2.anInt527 == i3) {
                                    int j7 = class28_2.worldX - xCamPos;
                                    int k8 = class28_2.worldY - yCamPos;
                                    int l9 = staticObjects[l3].worldX - xCamPos;
                                    int l10 = staticObjects[l3].worldY - yCamPos;
                                    if (j7 * j7 + k8 * k8 > l9 * l9 + l10 * l10) {
                                        l3 = j5;
                                    }
                                }
                            }
                        }

                        if (l3 == -1) {
                            break;
                        }
                        StaticObject class28_3 = staticObjects[l3];
                        class28_3.height = anInt448;
                        if (!method323(l, class28_3.tileLeft, class28_3.tileRight, class28_3.tileTop, class28_3.tileBottom, class28_3.node.modelHeight)) {
                            class28_3.node.renderAtPoint(class28_3.rotation, yCurveSin, yCurveCos, xCurveSin, xCurveCos, class28_3.worldX - xCamPos, class28_3.worldZ - zCamPos, class28_3.worldY - yCamPos, class28_3.uid, class28_3.interactiveObjUID);
                        }
                        for (int k7 = class28_3.tileLeft; k7 <= class28_3.tileRight; k7++) {
                            for (int l8 = class28_3.tileTop; l8 <= class28_3.tileBottom; l8++) {
                                Tile class30_sub3_22 = tiles[k7][l8];
                                if (class30_sub3_22.anInt1325 != 0) {
                                    tileDeque.insertHead(class30_sub3_22);
                                } else if ((k7 != i || l8 != j) && class30_sub3_22.aBoolean1323) {
                                    tileDeque.insertHead(class30_sub3_22);
                                }
                            }

                        }

                    }
                    if (tile1.aBoolean1324) {
                        continue;
                    }
                } catch (Exception _ex) {
                    _ex.printStackTrace();
                    tile1.aBoolean1324 = false;
                }
            }
            if (!tile1.aBoolean1323 || tile1.anInt1325 != 0) {
                continue;
            }
            if (i <= xCamPosTile && i > anInt449) {
                Tile class30_sub3_8 = tiles[i - 1][j];
                if (class30_sub3_8 != null && class30_sub3_8.aBoolean1323) {
                    continue;
                }
            }
            if (i >= xCamPosTile && i < anInt450 - 1) {
                Tile class30_sub3_9 = tiles[i + 1][j];
                if (class30_sub3_9 != null && class30_sub3_9.aBoolean1323) {
                    continue;
                }
            }
            if (j <= yCamPosTile && j > anInt451) {
                Tile class30_sub3_10 = tiles[i][j - 1];
                if (class30_sub3_10 != null && class30_sub3_10.aBoolean1323) {
                    continue;
                }
            }
            if (j >= yCamPosTile && j < anInt452 - 1) {
                Tile class30_sub3_11 = tiles[i][j + 1];
                if (class30_sub3_11 != null && class30_sub3_11.aBoolean1323) {
                    continue;
                }
            }
            tile1.aBoolean1323 = false;
            anInt446--;
            ItemPile object4 = tile1.itemPile;
            if (object4 != null && object4.topItem != 0) {
                if (object4.secondItemPile != null) {
                    object4.secondItemPile.renderAtPoint(0, yCurveSin, yCurveCos, xCurveSin, xCurveCos, object4.xPos - xCamPos, object4.zPos - zCamPos - object4.topItem, object4.yPos - yCamPos, object4.uid, object4.newUid);
                }
                if (object4.thirdItemPile != null) {
                    object4.thirdItemPile.renderAtPoint(0, yCurveSin, yCurveCos, xCurveSin, xCurveCos, object4.xPos - xCamPos, object4.zPos - zCamPos - object4.topItem, object4.yPos - yCamPos, object4.uid, object4.newUid);
                }
                if (object4.firstItemPile != null) {
                    object4.firstItemPile.renderAtPoint(0, yCurveSin, yCurveCos, xCurveSin, xCurveCos, object4.xPos - xCamPos, object4.zPos - zCamPos - object4.topItem, object4.yPos - yCamPos, object4.uid, object4.newUid);
                }
            }
            if (tile1.anInt1328 != 0) {
                WallDecoration class26 = tile1.wallDecoration;
                if (class26 != null && !method322(l, i, j, class26.node.modelHeight)) {
                    if ((class26.configurationBits & tile1.anInt1328) != 0) {
                        class26.node.renderAtPoint(class26.rotation, yCurveSin, yCurveCos, xCurveSin, xCurveCos, class26.xPos - xCamPos, class26.zPos - zCamPos, class26.yPos - yCamPos, class26.uid, class26.wallDecorUID);
                    } else if ((class26.configurationBits & 0x300) != 0) {
                        int l2 = class26.xPos - xCamPos;
                        int j3 = class26.zPos - zCamPos;
                        int i4 = class26.yPos - yCamPos;
                        int k5 = class26.rotation;
                        int j6;
                        if (k5 == 1 || k5 == 2) {
                            j6 = -l2;
                        } else {
                            j6 = l2;
                        }
                        int l7;
                        if (k5 == 2 || k5 == 3) {
                            l7 = -i4;
                        } else {
                            l7 = i4;
                        }
                        if ((class26.configurationBits & 0x100) != 0 && l7 >= j6) {
                            int i9 = l2 + faceXoffset2[k5];
                            int i10 = i4 + faceYOffset2[k5];
                            class26.node.renderAtPoint(k5 * 512 + 256, yCurveSin, yCurveCos, xCurveSin, xCurveCos, i9, j3, i10, class26.uid, class26.wallDecorUID);
                        }
                        if ((class26.configurationBits & 0x200) != 0 && l7 <= j6) {
                            int j9 = l2 + faceXOffset3[k5];
                            int j10 = i4 + faceYOffset3[k5];
                            class26.node.renderAtPoint(k5 * 512 + 1280 & 0x7ff, yCurveSin, yCurveCos, xCurveSin, xCurveCos, j9, j3, j10, class26.uid, class26.wallDecorUID);
                        }
                    }
                }
                Wall wall = tile1.wall;
                if (wall != null) {
                    if ((wall.orientation1 & tile1.anInt1328) != 0 && !method321(l, i, j, wall.orientation1)) {
                        wall.node2.renderAtPoint(0, yCurveSin, yCurveCos, xCurveSin, xCurveCos, wall.xPos - xCamPos, wall.zPos - zCamPos, wall.yPos - yCamPos, wall.uid, wall.wallObjUID);
                    }
                    if ((wall.orientation & tile1.anInt1328) != 0 && !method321(l, i, j, wall.orientation)) {
                        wall.node1.renderAtPoint(0, yCurveSin, yCurveCos, xCurveSin, xCurveCos, wall.xPos - xCamPos, wall.zPos - zCamPos, wall.yPos - yCamPos, wall.uid, wall.wallObjUID);
                    }
                }
            }
            if (k < zMapSize - 1) {
                Tile class30_sub3_12 = groundArray[k + 1][i][j];
                if (class30_sub3_12 != null && class30_sub3_12.aBoolean1323) {
                    tileDeque.insertHead(class30_sub3_12);
                }
            }
            if (i < xCamPosTile) {
                Tile class30_sub3_13 = tiles[i + 1][j];
                if (class30_sub3_13 != null && class30_sub3_13.aBoolean1323) {
                    tileDeque.insertHead(class30_sub3_13);
                }
            }
            if (j < yCamPosTile) {
                Tile class30_sub3_14 = tiles[i][j + 1];
                if (class30_sub3_14 != null && class30_sub3_14.aBoolean1323) {
                    tileDeque.insertHead(class30_sub3_14);
                }
            }
            if (i > xCamPosTile) {
                Tile class30_sub3_15 = tiles[i - 1][j];
                if (class30_sub3_15 != null && class30_sub3_15.aBoolean1323) {
                    tileDeque.insertHead(class30_sub3_15);
                }
            }
            if (j > yCamPosTile) {
                Tile class30_sub3_16 = tiles[i][j - 1];
                if (class30_sub3_16 != null && class30_sub3_16.aBoolean1323) {
                    tileDeque.insertHead(class30_sub3_16);
                }
            }
        } while (true);
    }

    private void drawSimpleTile(SimpleTile simpleTile, int tZ, int j, int k, int xSin, int xCos, int tX, int tY) {
        int xC;
        int xA = xC = (tX << 7) - xCamPos;
        int yB;
        int yA = yB = (tY << 7) - yCamPos;
        int xD;
        int xB = xD = xA + 128;
        int yC;
        int yD = yC = yA + 128;
        int zA = heightMap[tZ][tX][tY] - zCamPos;
        int zB = heightMap[tZ][tX + 1][tY] - zCamPos;
        int zD = heightMap[tZ][tX + 1][tY + 1] - zCamPos;
        int zC = heightMap[tZ][tX][tY + 1] - zCamPos;
        int angle = yA * xSin + xA * xCos >> 16;
        yA = yA * xCos - xA * xSin >> 16;
        xA = angle;
        angle = zA * k - yA * j >> 16;
        yA = zA * j + yA * k >> 16;
        zA = angle;
        if (yA < (mapViewDistance << 1)) {
            return;
        }
        angle = yB * xSin + xB * xCos >> 16;
        yB = yB * xCos - xB * xSin >> 16;
        xB = angle;
        angle = zB * k - yB * j >> 16;
        yB = zB * j + yB * k >> 16;
        zB = angle;
        if (yB < (mapViewDistance << 1)) {
            return;
        }
        angle = yD * xSin + xD * xCos >> 16;
        yD = yD * xCos - xD * xSin >> 16;
        xD = angle;
        angle = zD * k - yD * j >> 16;
        yD = zD * j + yD * k >> 16;
        zD = angle;
        if (yD < (mapViewDistance << 1)) {
            return;
        }
        angle = yC * xSin + xC * xCos >> 16;
        yC = yC * xCos - xC * xSin >> 16;
        xC = angle;
        angle = zC * k - yC * j >> 16;
        yC = zC * j + yC * k >> 16;
        zC = angle;
        if (yC < (mapViewDistance << 1)) {
            return;
        }
        int screenXA = Rasterizer.textureInt1 + (xA << viewDistance) / yA;
        int screenYA = Rasterizer.textureInt2 + (zA << viewDistance) / yA;
        int screenXB = Rasterizer.textureInt1 + (xB << viewDistance) / yB;
        int screenYB = Rasterizer.textureInt2 + (zB << viewDistance) / yB;
        int screenXD = Rasterizer.textureInt1 + (xD << viewDistance) / yD;
        int screenYD = Rasterizer.textureInt2 + (zD << viewDistance) / yD;
        int screenXC = Rasterizer.textureInt1 + (xC << viewDistance) / yC;
        int screenYC = Rasterizer.textureInt2 + (zC << viewDistance) / yC;
        Rasterizer.alpha = 0;
        if ((screenXD - screenXC) * (screenYB - screenYC) - (screenYD - screenYC) * (screenXB - screenXC) > 0) {
            Rasterizer.restrictEdges = screenXD < 0 || screenXC < 0 || screenXB < 0 || screenXD > Raster.viewportRX || screenXC > Raster.viewportRX || screenXB > Raster.viewportRX;
            if (isClicked && mouseWithinTriangle(clickX, clickY, screenYD, screenYC, screenYB, screenXD, screenXC, screenXB)) {
                clickedTileX = tX;
                clickedTileY = tY;
            }
            /**
             * Draws triangle CBD (first half)
             */
            if (!Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
                if (simpleTile.textureId == -1) {
                    if (simpleTile.colourD != 0xbc614e) {
                        Rasterizer.drawDepthShadedTriangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB, yD, yC, yB, simpleTile.colourD, simpleTile.colourC, simpleTile.colourB);
                    }
                } else if (!lowMem) {
                    if (simpleTile.isFlat) {
                        Rasterizer.drawDepthTexturedTriangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB, yD, yC, yB, simpleTile.colourD, simpleTile.colourC, simpleTile.colourB, xA, xB, xC, zA, zB, zC, yA, yB, yC, simpleTile.textureId);
                    } else {
                        Rasterizer.drawDepthTexturedTriangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB, yD, yC, yB, simpleTile.colourD, simpleTile.colourC, simpleTile.colourB, xD, xC, xB, zD, zC, zB, yD, yC, yB, simpleTile.textureId);
                    }
                } else {
                    int i7 = textureRGBColour[simpleTile.textureId];
                    Rasterizer.drawDepthShadedTriangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB, yD, yC, yB, mixColour(i7, simpleTile.colourD), mixColour(i7, simpleTile.colourC), mixColour(i7, simpleTile.colourB));
                }
            } else {
                if (simpleTile.textureId != -1) {
                    if (simpleTile.isFlat && simpleTile.colourD != 0xbc614e) {
                        Rasterizer.render_depth_texture_triangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB, yD, yC, yB, simpleTile.colourD, simpleTile.colourC, simpleTile.colourB, xA, xB, xC, zA, zB, zC, yA, yB, yC, !lowMem || simpleTile.rgbColour == -1 ? simpleTile.textureId : -1, simpleTile.color, true, opaque_floor_texture);
                    } else if (simpleTile.colourD != 0xbc614e) {
                        Rasterizer.render_depth_texture_triangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB, yD, yC, yB, simpleTile.colourD, simpleTile.colourC, simpleTile.colourB, xD, xC, xB, zD, zC, zB, yD, yC, yB, !lowMem || simpleTile.rgbColour == -1 ? simpleTile.textureId : -1, simpleTile.color, true, opaque_floor_texture);
                    }
                } else if (simpleTile.colourD != 0xbc614e) {
                    Rasterizer.drawDepthShadedTriangle(screenYD, screenYC, screenYB, screenXD, screenXC, screenXB, yD, yC, yB, simpleTile.colourD, simpleTile.colourC, simpleTile.colourB);
                }
            }
        }
        if ((screenXA - screenXB) * (screenYC - screenYB) - (screenYA - screenYB) * (screenXC - screenXB) > 0) {
            Rasterizer.restrictEdges = screenXA < 0 || screenXB < 0 || screenXC < 0 || screenXA > Raster.viewportRX || screenXB > Raster.viewportRX || screenXC > Raster.viewportRX;
            if (isClicked && mouseWithinTriangle(clickX, clickY, screenYA, screenYB, screenYC, screenXA, screenXB, screenXC)) {
                clickedTileX = tX;
                clickedTileY = tY;
            }
            /**
             * Draws triangle ACD (second half)
             */
            if (simpleTile.textureId == -1) {
                if (simpleTile.colourA != 0xbc614e) {
                    Rasterizer.drawDepthShadedTriangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC, yA, yB, yC, simpleTile.colourA, simpleTile.colourB, simpleTile.colourC);
                }
            } else {
                if (!lowMem) {
                    if (Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES) && simpleTile.colourA != 0xbc614e) {
                        Rasterizer.render_depth_texture_triangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC, yA, yB, yC, simpleTile.colourA, simpleTile.colourB, simpleTile.colourC, xA, xB, xC, zA, zB, zC, yA, yB, yC, !lowMem || simpleTile.color == -1 ? simpleTile.textureId : -1, simpleTile.color, true, opaque_floor_texture);
                    } else if (simpleTile.colourA != 0xbc614e) {
                        Rasterizer.drawDepthTexturedTriangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC, yA, yB, yC, simpleTile.colourA, simpleTile.colourB, simpleTile.colourC, xA, xB, xC, zA, zB, zC, yA, yB, yC, simpleTile.textureId);
                    }
                    return;
                }
                if (!Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
                    int j7 = textureRGBColour[simpleTile.textureId];
                    Rasterizer.drawDepthShadedTriangle(screenYA, screenYB, screenYC, screenXA, screenXB, screenXC, yA, yB, yC, mixColour(j7, simpleTile.colourA), mixColour(j7, simpleTile.colourB), mixColour(j7, simpleTile.colourC));
                }
            }
        }
    }

    private void drawShapedTile(int i, int j, int k, ShapedTile sTile, int l, int i1, int j1) {
        int k1 = sTile.origVertexX.length;
        for (int l1 = 0; l1 < k1; l1++) {
            int viewSpaceX = sTile.origVertexX[l1] - xCamPos;
            int viewSpaceY = sTile.origVertexY[l1] - zCamPos;
            int viewSpaceZ = sTile.origVertexZ[l1] - yCamPos;
            int viewSpaceDepth = viewSpaceZ * k + viewSpaceX * j1 >> 16;
            viewSpaceZ = viewSpaceZ * j1 - viewSpaceX * k >> 16;
            viewSpaceX = viewSpaceDepth;
            viewSpaceDepth = viewSpaceY * l - viewSpaceZ * j >> 16;
            viewSpaceZ = viewSpaceY * j + viewSpaceZ * l >> 16;
            viewSpaceY = viewSpaceDepth;
            if (viewSpaceZ < (mapViewDistance << 1)) {
                return;
            }
            if (sTile.triangleTexture != null) {
                ShapedTile.viewSpaceX[l1] = viewSpaceX;
                ShapedTile.viewSpaceY[l1] = viewSpaceY;
                ShapedTile.viewSpaceZ[l1] = viewSpaceZ;
            }
            ShapedTile.screenX[l1] = Rasterizer.textureInt1 + (viewSpaceX << viewDistance) / viewSpaceZ;
            ShapedTile.screenY[l1] = Rasterizer.textureInt2 + (viewSpaceY << viewDistance) / viewSpaceZ;
            ShapedTile.screenZ[l1] = viewSpaceZ;
        }

        Rasterizer.alpha = 0;
        k1 = sTile.triangleA.length;
        for (int j2 = 0; j2 < k1; j2++) {
            int indexA = sTile.triangleA[j2];
            int indexB = sTile.triangleB[j2];
            int indexC = sTile.triangleC[j2];
            int sXA = ShapedTile.screenX[indexA];
            int sXB = ShapedTile.screenX[indexB];
            int sXC = ShapedTile.screenX[indexC];
            int sYA = ShapedTile.screenY[indexA];
            int sYB = ShapedTile.screenY[indexB];
            int sYC = ShapedTile.screenY[indexC];
            int sZA = ShapedTile.screenZ[indexA];
            int sZB = ShapedTile.screenZ[indexB];
            int sZC = ShapedTile.screenZ[indexC];
            if ((sXA - sXB) * (sYC - sYB) - (sYA - sYB) * (sXC - sXB) > 0) {
                Rasterizer.restrictEdges = sXA < 0 || sXB < 0 || sXC < 0 || sXA > Raster.viewportRX || sXB > Raster.viewportRX || sXC > Raster.viewportRX;
                if (isClicked && mouseWithinTriangle(clickX, clickY, sYA, sYB, sYC, sXA, sXB, sXC)) {
                    clickedTileX = i;
                    clickedTileY = i1;
                }
                if (Game.INSTANCE.getInterfaceConfiguration(InterfaceConfiguration.HD_TEXTURES)) {
                    if (sTile.triangleTexture == null || sTile.triangleTexture[j2] == -1) {
                        if (sTile.triangleHslA[j2] != 0xbc614e) {
                            Rasterizer.drawDepthShadedTriangle(sYA, sYB, sYC, sXA, sXB, sXC, sZA, sZB, sZC, sTile.triangleHslA[j2], sTile.triangleHslB[j2], sTile.triangleHslC[j2]);
                        }
                    } else {
                        if (sTile.flat && sTile.triangleHslA[j2] != 0xbc614e) {
                            Rasterizer.render_depth_texture_triangle(sYA, sYB, sYC, sXA, sXB, sXC, sZA, sZB, sZC, sTile.triangleHslA[j2], sTile.triangleHslB[j2], sTile.triangleHslC[j2], ShapedTile.viewSpaceX[0], ShapedTile.viewSpaceX[1], ShapedTile.viewSpaceX[3], ShapedTile.viewSpaceY[0], ShapedTile.viewSpaceY[1], ShapedTile.viewSpaceY[3], ShapedTile.viewSpaceZ[0], ShapedTile.viewSpaceZ[1], ShapedTile.viewSpaceZ[3], !lowMem || sTile.displayColor[j2] == -1 ? sTile.triangleTexture[j2] : -1, sTile.displayColor[j2], true, opaque_floor_texture);
                        } else if (sTile.triangleHslA[j2] != 0xbc614e) {
                            Rasterizer.render_depth_texture_triangle(sYA, sYB, sYC, sXA, sXB, sXC, sZA, sZB, sZC, sTile.triangleHslA[j2], sTile.triangleHslB[j2], sTile.triangleHslC[j2], ShapedTile.viewSpaceX[indexA], ShapedTile.viewSpaceX[indexB], ShapedTile.viewSpaceX[indexC], ShapedTile.viewSpaceY[indexA], ShapedTile.viewSpaceY[indexB], ShapedTile.viewSpaceY[indexC], ShapedTile.viewSpaceZ[indexA], ShapedTile.viewSpaceZ[indexB], ShapedTile.viewSpaceZ[indexC], !lowMem || sTile.displayColor[j2] == -1 ? sTile.triangleTexture[j2] : -1, sTile.displayColor[j2], true, opaque_floor_texture);
                        }
                    }
                } else {
                    if (sTile.triangleTexture == null || sTile.triangleTexture[j2] == -1) {
                        if (sTile.triangleHslA[j2] != 0xbc614e) {
                            Rasterizer.drawDepthShadedTriangle(sYA, sYB, sYC, sXA, sXB, sXC, sZA, sZB, sZC, sTile.triangleHslA[j2], sTile.triangleHslB[j2], sTile.triangleHslC[j2]);
                        }
                    } else if (!lowMem) {
                        if (sTile.flat) {
                            Rasterizer.drawDepthTexturedTriangle(sYA, sYB, sYC, sXA, sXB, sXC, sZA, sZB, sZC, sTile.triangleHslA[j2], sTile.triangleHslB[j2], sTile.triangleHslC[j2], ShapedTile.viewSpaceX[0], ShapedTile.viewSpaceX[1], ShapedTile.viewSpaceX[3], ShapedTile.viewSpaceY[0], ShapedTile.viewSpaceY[1], ShapedTile.viewSpaceY[3], ShapedTile.viewSpaceZ[0], ShapedTile.viewSpaceZ[1], ShapedTile.viewSpaceZ[3], sTile.triangleTexture[j2]);
                        } else {
                            Rasterizer.drawDepthTexturedTriangle(sYA, sYB, sYC, sXA, sXB, sXC, sZA, sZB, sZC, sTile.triangleHslA[j2], sTile.triangleHslB[j2], sTile.triangleHslC[j2], ShapedTile.viewSpaceX[indexA], ShapedTile.viewSpaceX[indexB], ShapedTile.viewSpaceX[indexC], ShapedTile.viewSpaceY[indexA], ShapedTile.viewSpaceY[indexB], ShapedTile.viewSpaceY[indexC], ShapedTile.viewSpaceZ[indexA], ShapedTile.viewSpaceZ[indexB], ShapedTile.viewSpaceZ[indexC], sTile.triangleTexture[j2]);
                        }
                    } else {
                        int rgb = textureRGBColour[sTile.triangleTexture[j2]];
                        Rasterizer.drawDepthShadedTriangle(sYA, sYB, sYC, sXA, sXB, sXC, sZA, sZB, sZC, mixColour(rgb, sTile.triangleHslA[j2]), mixColour(rgb, sTile.triangleHslB[j2]), mixColour(rgb, sTile.triangleHslC[j2]));
                    }
                }
            }
        }
    }

    private int mixColour(int colour1, int colour2) {
        colour2 = 127 - colour2;
        colour2 = (colour2 * (colour1 & 0x7f)) / 160;
        if (colour2 < 2) {
            colour2 = 2;
        } else if (colour2 > 126) {
            colour2 = 126;
        }
        return (colour1 & 0xff80) + colour2;
    }

    private boolean mouseWithinTriangle(int mouseX, int mouseY, int triangleYA, int triangleYB, int triangleYC, int triangleXA, int triangleXB, int triangleXC) {
        if (mouseY < triangleYA && mouseY < triangleYB && mouseY < triangleYC) {
            return false;
        }
        if (mouseY > triangleYA && mouseY > triangleYB && mouseY > triangleYC) {
            return false;
        }
        if (mouseX < triangleXA && mouseX < triangleXB && mouseX < triangleXC) {
            return false;
        }
        if (mouseX > triangleXA && mouseX > triangleXB && mouseX > triangleXC) {
            return false;
        }
        int i2 = (mouseY - triangleYA) * (triangleXB - triangleXA) - (mouseX - triangleXA) * (triangleYB - triangleYA);
        int j2 = (mouseY - triangleYC) * (triangleXA - triangleXC) - (mouseX - triangleXC) * (triangleYA - triangleYC);
        int k2 = (mouseY - triangleYB) * (triangleXC - triangleXB) - (mouseX - triangleXB) * (triangleYC - triangleYB);
        return i2 * k2 > 0 && k2 * j2 > 0;
    }

    private void processCulling() {
        int count = cullingClusterPointer[plane__];
        CullingCluster clusters[] = cullingClusters[plane__];
        processedClusterPtr = 0;
        for (int k = 0; k < count; k++) {
            CullingCluster cullingCluster = clusters[k];
            if (cullingCluster.searchMask == 1) {
                int xDistFromCamStart = (cullingCluster.tileStartX - xCamPosTile) + mapViewDistance;
                if (xDistFromCamStart < 0 || xDistFromCamStart > (mapViewDistance << 1)) {
                    continue;
                }
                int yDistFromCamStart = (cullingCluster.tileStartY - yCamPosTile) + mapViewDistance;
                if (yDistFromCamStart < 0) {
                    yDistFromCamStart = 0;
                }
                int yDistFromCamEnd = (cullingCluster.tileEndY - yCamPosTile) + mapViewDistance;
                if (yDistFromCamEnd > (mapViewDistance << 1)) {
                    yDistFromCamEnd = (mapViewDistance << 1);
                }
                boolean visisble = false;
                while (yDistFromCamStart <= yDistFromCamEnd) {
                    if (tile_visibility_map[xDistFromCamStart][yDistFromCamStart++]) {
                        visisble = true;
                        break;
                    }
                }
                if (!visisble) {
                    continue;
                }
                int xDistFromCamStartReal = xCamPos - cullingCluster.worldStartX;
                if (xDistFromCamStartReal > 32) {
                    cullingCluster.tileDistance = 1;
                } else {
                    if (xDistFromCamStartReal >= -32) {
                        continue;
                    }
                    cullingCluster.tileDistance = 2;
                    xDistFromCamStartReal = -xDistFromCamStartReal;
                }
                cullingCluster.worldDistanceFromCameraStartY = (cullingCluster.worldStartY - yCamPos << 8) / xDistFromCamStartReal;
                cullingCluster.worldDistanceFromCameraEndY = (cullingCluster.worldEndY - yCamPos << 8) / xDistFromCamStartReal;
                cullingCluster.worldDistanceFromCameraStartZ = (cullingCluster.worldStartZ - zCamPos << 8) / xDistFromCamStartReal;
                cullingCluster.worldDistanceFromCameraEndZ = (cullingCluster.worldEndZ - zCamPos << 8) / xDistFromCamStartReal;
                processedClusters[processedClusterPtr++] = cullingCluster;
                continue;
            }
            if (cullingCluster.searchMask == 2) {
                int yDIstFromCamStart = (cullingCluster.tileStartY - yCamPosTile) + mapViewDistance;
                if (yDIstFromCamStart < 0 || yDIstFromCamStart > (mapViewDistance << 1)) {
                    continue;
                }
                int xDistFromCamStart = (cullingCluster.tileStartX - xCamPosTile) + mapViewDistance;
                if (xDistFromCamStart < 0) {
                    xDistFromCamStart = 0;
                }
                int xDistFromCamEnd = (cullingCluster.tileEndX - xCamPosTile) + mapViewDistance;
                if (xDistFromCamEnd > (mapViewDistance << 1)) {
                    xDistFromCamEnd = (mapViewDistance << 1);
                }
                boolean visible = false;
                while (xDistFromCamStart <= xDistFromCamEnd) {
                    if (tile_visibility_map[xDistFromCamStart++][yDIstFromCamStart]) {
                        visible = true;
                        break;
                    }
                }
                if (!visible) {
                    continue;
                }
                int yDistFromCamStartReal = yCamPos - cullingCluster.worldStartY;
                if (yDistFromCamStartReal > 32) {
                    cullingCluster.tileDistance = 3;
                } else {
                    if (yDistFromCamStartReal >= -32) {
                        continue;
                    }
                    cullingCluster.tileDistance = 4;
                    yDistFromCamStartReal = -yDistFromCamStartReal;
                }
                cullingCluster.worldDistanceFromCameraStartX = (cullingCluster.worldStartX - xCamPos << 8) / yDistFromCamStartReal;
                cullingCluster.worldDistanceFromCameraEndX = (cullingCluster.worldEndX - xCamPos << 8) / yDistFromCamStartReal;
                cullingCluster.worldDistanceFromCameraStartZ = (cullingCluster.worldStartZ - zCamPos << 8) / yDistFromCamStartReal;
                cullingCluster.worldDistanceFromCameraEndZ = (cullingCluster.worldEndZ - zCamPos << 8) / yDistFromCamStartReal;
                processedClusters[processedClusterPtr++] = cullingCluster;
            } else if (cullingCluster.searchMask == 4) {
                int yDistFromCamStartReal = cullingCluster.worldStartZ - zCamPos;
                if (yDistFromCamStartReal > 128) {
                    int yDistFromCamStart = (cullingCluster.tileStartY - yCamPosTile) + mapViewDistance;
                    if (yDistFromCamStart < 0) {
                        yDistFromCamStart = 0;
                    }
                    int yDistFromCamEnd = (cullingCluster.tileEndY - yCamPosTile) + mapViewDistance;
                    if (yDistFromCamEnd > (mapViewDistance << 1)) {
                        yDistFromCamEnd = (mapViewDistance << 1);
                    }
                    if (yDistFromCamStart <= yDistFromCamEnd) {
                        int xDistFromCamStart = (cullingCluster.tileStartX - xCamPosTile) + mapViewDistance;
                        if (xDistFromCamStart < 0) {
                            xDistFromCamStart = 0;
                        }
                        int xDistFromCamEnd = (cullingCluster.tileEndX - xCamPosTile) + mapViewDistance;
                        if (xDistFromCamEnd > (mapViewDistance << 1)) {
                            xDistFromCamEnd = (mapViewDistance << 1);
                        }
                        boolean visible = false;
                        label0:
                        for (int _x = xDistFromCamStart; _x <= xDistFromCamEnd; _x++) {
                            for (int _y = yDistFromCamStart; _y <= yDistFromCamEnd; _y++) {
                                if (!tile_visibility_map[_x][_y]) {
                                    continue;
                                }
                                visible = true;
                                break label0;
                            }

                        }

                        if (visible) {
                            cullingCluster.tileDistance = 5;
                            cullingCluster.worldDistanceFromCameraStartX = (cullingCluster.worldStartX - xCamPos << 8) / yDistFromCamStartReal;
                            cullingCluster.worldDistanceFromCameraEndX = (cullingCluster.worldEndX - xCamPos << 8) / yDistFromCamStartReal;
                            cullingCluster.worldDistanceFromCameraStartY = (cullingCluster.worldStartY - yCamPos << 8) / yDistFromCamStartReal;
                            cullingCluster.worldDistanceFromCameraEndY = (cullingCluster.worldEndY - yCamPos << 8) / yDistFromCamStartReal;
                            processedClusters[processedClusterPtr++] = cullingCluster;
                        }
                    }
                }
            }
        }

    }

    private boolean method320(int y, int x, int z) {
        int l = anIntArrayArrayArray445[y][x][z];
        if (l == -anInt448) {
            return false;
        }
        if (l == anInt448) {
            return true;
        }
        int i1 = x << 7;
        int j1 = z << 7;
        if (clusterProcessed(i1 + 1, heightMap[y][x][z], j1 + 1) && clusterProcessed((i1 + 128) - 1, heightMap[y][x + 1][z], j1 + 1) && clusterProcessed((i1 + 128) - 1, heightMap[y][x + 1][z + 1], (j1 + 128) - 1) && clusterProcessed(i1 + 1, heightMap[y][x][z + 1], (j1 + 128) - 1)) {
            anIntArrayArrayArray445[y][x][z] = anInt448;
            return true;
        } else {
            anIntArrayArrayArray445[y][x][z] = -anInt448;
            return false;
        }
    }

    private boolean method321(int z, int x, int y, int l) {
        if (!method320(z, x, y)) {
            return false;
        }
        int i1 = x << 7;
        int j1 = y << 7;
        int k1 = heightMap[z][x][y] - 1;
        int l1 = k1 - 120;
        int i2 = k1 - 230;
        int j2 = k1 - 238;
        if (l < 16) {
            if (l == 1) {
                if (i1 > xCamPos) {
                    if (!clusterProcessed(i1, k1, j1)) {
                        return false;
                    }
                    if (!clusterProcessed(i1, k1, j1 + 128)) {
                        return false;
                    }
                }
                if (z > 0) {
                    if (!clusterProcessed(i1, l1, j1)) {
                        return false;
                    }
                    if (!clusterProcessed(i1, l1, j1 + 128)) {
                        return false;
                    }
                }
                return clusterProcessed(i1, i2, j1) && clusterProcessed(i1, i2, j1 + 128);
            }
            if (l == 2) {
                if (j1 < yCamPos) {
                    if (!clusterProcessed(i1, k1, j1 + 128)) {
                        return false;
                    }
                    if (!clusterProcessed(i1 + 128, k1, j1 + 128)) {
                        return false;
                    }
                }
                if (z > 0) {
                    if (!clusterProcessed(i1, l1, j1 + 128)) {
                        return false;
                    }
                    if (!clusterProcessed(i1 + 128, l1, j1 + 128)) {
                        return false;
                    }
                }
                return clusterProcessed(i1, i2, j1 + 128) && clusterProcessed(i1 + 128, i2, j1 + 128);
            }
            if (l == 4) {
                if (i1 < xCamPos) {
                    if (!clusterProcessed(i1 + 128, k1, j1)) {
                        return false;
                    }
                    if (!clusterProcessed(i1 + 128, k1, j1 + 128)) {
                        return false;
                    }
                }
                if (z > 0) {
                    if (!clusterProcessed(i1 + 128, l1, j1)) {
                        return false;
                    }
                    if (!clusterProcessed(i1 + 128, l1, j1 + 128)) {
                        return false;
                    }
                }
                return clusterProcessed(i1 + 128, i2, j1) && clusterProcessed(i1 + 128, i2, j1 + 128);
            }
            if (l == 8) {
                if (j1 > yCamPos) {
                    if (!clusterProcessed(i1, k1, j1)) {
                        return false;
                    }
                    if (!clusterProcessed(i1 + 128, k1, j1)) {
                        return false;
                    }
                }
                if (z > 0) {
                    if (!clusterProcessed(i1, l1, j1)) {
                        return false;
                    }
                    if (!clusterProcessed(i1 + 128, l1, j1)) {
                        return false;
                    }
                }
                return clusterProcessed(i1, i2, j1) && clusterProcessed(i1 + 128, i2, j1);
            }
        }
        if (!clusterProcessed(i1 + 64, j2, j1 + 64)) {
            return false;
        }
        if (l == 16) {
            return clusterProcessed(i1, i2, j1 + 128);
        }
        if (l == 32) {
            return clusterProcessed(i1 + 128, i2, j1 + 128);
        }
        if (l == 64) {
            return clusterProcessed(i1 + 128, i2, j1);
        }
        if (l == 128) {
            return clusterProcessed(i1, i2, j1);
        } else {
            if (Constants.DEBUG_MODE) {
                logger.log(Level.WARNING, "Unsupported wall type {0}.", l);
            }
            return true;
        }
    }

    private boolean method322(int i, int j, int k, int l) {
        if (!method320(i, j, k)) {
            return false;
        }
        int i1 = j << 7;
        int j1 = k << 7;
        return clusterProcessed(i1 + 1, heightMap[i][j][k] - l, j1 + 1) && clusterProcessed((i1 + 128) - 1, heightMap[i][j + 1][k] - l, j1 + 1) && clusterProcessed((i1 + 128) - 1, heightMap[i][j + 1][k + 1] - l, (j1 + 128) - 1) && clusterProcessed(i1 + 1, heightMap[i][j][k + 1] - l, (j1 + 128) - 1);
    }

    private boolean method323(int y, int x, int k, int z, int i1, int j1) {
        if (x == k && z == i1) {
            if (!method320(y, x, z)) {
                return false;
            }
            int k1 = x << 7;
            int i2 = z << 7;
            return clusterProcessed(k1 + 1, heightMap[y][x][z] - j1, i2 + 1) && clusterProcessed((k1 + 128) - 1, heightMap[y][x + 1][z] - j1, i2 + 1) && clusterProcessed((k1 + 128) - 1, heightMap[y][x + 1][z + 1] - j1, (i2 + 128) - 1) && clusterProcessed(k1 + 1, heightMap[y][x][z + 1] - j1, (i2 + 128) - 1);
        }
        for (int l1 = x; l1 <= k; l1++) {
            for (int j2 = z; j2 <= i1; j2++) {
                if (anIntArrayArrayArray445[y][l1][j2] == -anInt448) {
                    return false;
                }
            }
        }

        int k2 = (x << 7) + 1;
        int l2 = (z << 7) + 2;
        int i3 = heightMap[y][x][z] - j1;
        if (!clusterProcessed(k2, i3, l2)) {
            return false;
        }
        int j3 = (k << 7) - 1;
        if (!clusterProcessed(j3, i3, l2)) {
            return false;
        }
        int k3 = (i1 << 7) - 1;
        return clusterProcessed(k2, i3, k3) && clusterProcessed(j3, i3, k3);
    }

    private boolean clusterProcessed(int x, int y, int z) {
        for (int l = 0; l < processedClusterPtr; l++) {
            CullingCluster cluster = processedClusters[l];
            if (cluster.tileDistance == 1) {
                int i1 = cluster.worldStartX - x;
                if (i1 > 0) {
                    int j2 = cluster.worldStartY + (cluster.worldDistanceFromCameraStartY * i1 >> 8);
                    int k3 = cluster.worldEndY + (cluster.worldDistanceFromCameraEndY * i1 >> 8);
                    int l4 = cluster.worldStartZ + (cluster.worldDistanceFromCameraStartZ * i1 >> 8);
                    int i6 = cluster.worldEndZ + (cluster.worldDistanceFromCameraEndZ * i1 >> 8);
                    if (z >= j2 && z <= k3 && y >= l4 && y <= i6) {
                        return true;
                    }
                }
            } else if (cluster.tileDistance == 2) {
                int j1 = x - cluster.worldStartX;
                if (j1 > 0) {
                    int k2 = cluster.worldStartY + (cluster.worldDistanceFromCameraStartY * j1 >> 8);
                    int l3 = cluster.worldEndY + (cluster.worldDistanceFromCameraEndY * j1 >> 8);
                    int i5 = cluster.worldStartZ + (cluster.worldDistanceFromCameraStartZ * j1 >> 8);
                    int j6 = cluster.worldEndZ + (cluster.worldDistanceFromCameraEndZ * j1 >> 8);
                    if (z >= k2 && z <= l3 && y >= i5 && y <= j6) {
                        return true;
                    }
                }
            } else if (cluster.tileDistance == 3) {
                int k1 = cluster.worldStartY - z;
                if (k1 > 0) {
                    int l2 = cluster.worldStartX + (cluster.worldDistanceFromCameraStartX * k1 >> 8);
                    int i4 = cluster.worldEndX + (cluster.worldDistanceFromCameraEndX * k1 >> 8);
                    int j5 = cluster.worldStartZ + (cluster.worldDistanceFromCameraStartZ * k1 >> 8);
                    int k6 = cluster.worldEndZ + (cluster.worldDistanceFromCameraEndZ * k1 >> 8);
                    if (x >= l2 && x <= i4 && y >= j5 && y <= k6) {
                        return true;
                    }
                }
            } else if (cluster.tileDistance == 4) {
                int l1 = z - cluster.worldStartY;
                if (l1 > 0) {
                    int i3 = cluster.worldStartX + (cluster.worldDistanceFromCameraStartX * l1 >> 8);
                    int j4 = cluster.worldEndX + (cluster.worldDistanceFromCameraEndX * l1 >> 8);
                    int k5 = cluster.worldStartZ + (cluster.worldDistanceFromCameraStartZ * l1 >> 8);
                    int l6 = cluster.worldEndZ + (cluster.worldDistanceFromCameraEndZ * l1 >> 8);
                    if (x >= i3 && x <= j4 && y >= k5 && y <= l6) {
                        return true;
                    }
                }
            } else if (cluster.tileDistance == 5) {
                int i2 = y - cluster.worldStartZ;
                if (i2 > 0) {
                    int j3 = cluster.worldStartX + (cluster.worldDistanceFromCameraStartX * i2 >> 8);
                    int k4 = cluster.worldEndX + (cluster.worldDistanceFromCameraEndX * i2 >> 8);
                    int l5 = cluster.worldStartY + (cluster.worldDistanceFromCameraStartY * i2 >> 8);
                    int i7 = cluster.worldEndY + (cluster.worldDistanceFromCameraEndY * i2 >> 8);
                    if (x >= j3 && x <= k4 && z >= l5 && z <= i7) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}