package com.runescape.scene.map.object.tile;

import com.runescape.collection.Linkable;
import com.runescape.media.renderable.ItemPile;
import com.runescape.media.renderable.StaticObject;
import com.runescape.scene.map.object.GroundDecoration;
import com.runescape.scene.map.object.Wall;
import com.runescape.scene.map.object.WallDecoration;

public final class Tile extends Linkable {

    public final int tileX;
    public final int tileY;
    public final int plane;
    public final StaticObject[] staticObjects;
    public final int[] anIntArray1319;
    public int tileZ;
    public SimpleTile simpleTile;
    public ShapedTile shapedTile;
    public Wall wall;
    public WallDecoration wallDecoration;
    public GroundDecoration groundDecoration;
    public ItemPile itemPile;
    public int entityCount;
    public int anInt1320;
    public int logicHeight;
    public boolean aBoolean1322;
    public boolean aBoolean1323;
    public boolean aBoolean1324;
    public int anInt1325;
    public int anInt1326;
    public int anInt1327;
    public int anInt1328;
    public Tile tileBelowThisTile;

    public Tile(int i, int j, int k) {
        staticObjects = new StaticObject[5];
        anIntArray1319 = new int[5];
        plane = tileZ = i;
        tileX = j;
        tileY = k;
    }
}
