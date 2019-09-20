package com.runescape.media.gameframe.impl;

import com.runescape.Constants;
import com.runescape.Game;
import com.runescape.cache.config.ge.GrandExchange;
import com.runescape.cache.def.npc.NPCDefinition;
import com.runescape.cache.def.object.ObjectDefinition;
import com.runescape.cache.media.Background;
import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.Sprite;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.chat.ChatMessageType;
import com.runescape.chat.ClanChatMember;
import com.runescape.collection.Deque;
import com.runescape.media.ImageProducer;
import com.runescape.media.Raster;
import com.runescape.media.Scrollbar;
import com.runescape.media.gameframe.Gameframe;
import com.runescape.media.gameframe.StatusOrb;
import com.runescape.media.renderable.Model;
import com.runescape.media.renderable.entity.NPC;
import com.runescape.media.renderable.entity.Player;
import com.runescape.net.CacheArchive;
import com.runescape.scene.graphic.Rasterizer;
import com.runescape.scene.map.SceneGraph;
import com.runescape.util.StringUtility;

import java.text.DecimalFormat;

/**
 * Represents the 530 {@link com.runescape.media.gameframe.Gameframe}.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class Gameframe530 extends Gameframe {
    private final int[] sideIconsX = {17, 49, 83, 114, 146, 180, 214, 16, 49, 82, 116, 148, 184, 216};
    private final int[] sideIconsY = {9, 7, 7, 5, 2, 3, 7, 303, 306, 306, 302, 305, 303, 303, 303};
    private final int[] sideIconsId = {0, 1, 2, 3, 4, 5, 6, 10, 8, 9, 7, 11, 12, 13};
    private final int[] sideIconsTab = {0, 1, 2, 3, 4, 5, 6, 10, 8, 9, 7, 11, 12, 13};
    private final int[] redStonesX = {6, 44, 77, 110, 143, 176, 209, 6, 44, 77, 110, 143, 176, 209};
    private final int[] redStonesY = {0, 0, 0, 0, 0, 0, 0, 298, 298, 298, 298, 298, 298, 298};
    private final int[] redStonesId = {0, 4, 4, 4, 4, 4, 1, 2, 4, 4, 4, 4, 4, 3};
    private final int[] tabClickX = {38, 33, 33, 33, 33, 33, 38, 38, 33, 33,
            33, 33, 33, 38}, tabClickStart = {522, 560, 593, 625, 659, 692,
            724, 522, 560, 593, 625, 659, 692, 724}, tabClickY = {169, 169,
            169, 169, 169, 169, 169, 466, 466, 466, 466, 466, 466, 466};
    /**
     * The chat back {@link Sprite}.
     */
    public Sprite chatBack;
    /**
     * The world sprite.
     */
    public Sprite world;
    /**
     * The world hover sprite.
     */
    public Sprite worldHover;
    /**
     * The world map back sprite.
     */
    public Sprite worldBack;
    int[] id = {20, 89, 21, 22, 23, 24, 25, 26, 95, 28, 29, 27, 31,
            32, 33, 90, 149};
    boolean summoningHover = false;
    private boolean faceNorthClick;
    private boolean worldMapClick;
    private boolean prayersClick;
    private boolean runClick;
    private boolean hitpointsClick;
    private boolean xpDropClick;
    private boolean poisoned;
    private boolean logoutClick;
    /**
     * The black mapback {@link com.runescape.cache.media.Sprite}.
     */
    private Sprite blackMapBack;
    /**
     * The mapback {@link com.runescape.cache.media.Sprite}.
     */
    private Sprite mapBack;
    /**
     * The compass back {@link com.runescape.cache.media.Sprite}.
     */
    private Sprite compassBack;
    /**
     * The full screen mapback {@link com.runescape.cache.media.Sprite}.
     */
    private Sprite fullScreenMapBack;
    /**
     * The full screen mapback black {@link com.runescape.cache.media.Sprite}.
     */
    private Sprite fullScreenMapBackBlack;
    /**
     * The logout X {@link com.runescape.cache.media.Sprite}.
     */
    private Sprite logoutX;
    /**
     * The hovered logout X {@link com.runescape.cache.media.Sprite}.
     */
    private Sprite logoutXHover;
    /**
     * The status orb {@link com.runescape.cache.media.Sprite}s.
     */
    private Sprite[] orbs = new Sprite[17];
    /**
     * The status orb back {@link com.runescape.cache.media.Sprite}.
     */
    private Sprite orbBack;
    /**
     * The red stone {@link com.runescape.cache.media.Sprite}s.
     */
    private Sprite[] redStones = new Sprite[5];
    /**
     * The side icon {@link com.runescape.cache.media.Sprite}s.
     */
    private Sprite[] sideIcons = new Sprite[15];
    /**
     * The tab back {@link com.runescape.cache.media.Sprite}.
     */
    private Sprite tabBack;
    /**
     * The empty tab back {@link com.runescape.cache.media.Sprite}.
     */
    private Sprite tabBackEmpty;
    /**
     * The tab border {@link com.runescape.cache.media.Sprite}.
     */
    private Sprite tabBorder;
    /**
     * The tab stone {@link com.runescape.cache.media.Sprite}.
     */
    private Sprite tabStone;
    /**
     * The xp drop orb {@link com.runescape.cache.media.Sprite}s.
     */
    private Sprite[] xpDrop = new Sprite[4];
    /**
     * The xp drop status frame {@link com.runescape.cache.media.Sprite}.
     */
    private Sprite xpDropStatus;

    /**
     * Constructs a new <code>GameFrame</code>.
     *
     * @param game The {@link Game} instance.
     */
    public Gameframe530(Game game) {
        super(game);
    }

    @Override
    public void setImageProducers() {
        setChatImageProducer(new ImageProducer(519, 165));
        setMinimapImageProducer(new ImageProducer(249, 168));
        Raster.clear();
        drawMinimapSprite();
        setTabImageProducer(new ImageProducer(250, 335));
        setGameScreenImageProducer(new ImageProducer(Game.isFixed() ? 512 : Game.getFrameWidth(),
                Game.isFixed() ? 334 : Game.getFrameHeight()));
        Raster.clear();
    }

    @Override
    public int getId() {
        return 530;
    }

    @Override
    public void setup() {
        resetImageProducers();
        setImageProducers();
        if (Game.currentTabId == 15) {
            Game.setTab(13, false);
        }
        getGame().spriteTimers[1][0] = 10;
        getGame().spriteTimers[1][1] = 150;
        getGame().spriteAlphaValues[1][1] = 8;
    }

    @Override
    public void registerSprites(CacheArchive cacheArchive) {
        this.blackMapBack = ImageLoader.forName("MAPBACK_BLACK");
        this.mapBack = ImageLoader.forName("MAPBACK");
        this.compassBack = ImageLoader.forName("COMPASS_BACK");
        this.fullScreenMapBack = ImageLoader.forName("MAPBACK_FULL");
        this.fullScreenMapBackBlack = ImageLoader.forName("MAPBACK_BLACK");
        this.logoutX = ImageLoader.forName("LOGOUT_X");
        this.logoutXHover = ImageLoader.forName("LOGOUT_X_HOVER");
        for (int index = 0; index < 17; index++) {
            this.orbs[index] = ImageLoader.forName("ORBS_" + index);
        }
        this.orbBack = ImageLoader.forName("ORB_BACK");
        for (int index = 0; index < 5; index++) {
            this.redStones[index] = new Sprite(cacheArchive, "redstone1", index);
        }
        for (int index = 0; index < 15; index++) {
            this.sideIcons[index] = new Sprite(cacheArchive, "sideicons", index);
        }
        this.tabBack = ImageLoader.forName("530_TAB_BACK");
        this.tabBackEmpty = ImageLoader.forName("TAB_BACK_EMPTY");
        this.tabBorder = ImageLoader.forName("TAB_BORDER");
        this.tabStone = ImageLoader.forName("TAB_STONE");
        this.xpDrop[0] = ImageLoader.forName("xpdrop_on");
        this.xpDrop[1] = ImageLoader.forName("xpdrop_on_hover");
        this.xpDrop[2] = ImageLoader.forName("xpdrop_off");
        this.xpDrop[3] = ImageLoader.forName("xpdrop_off_hover");
        this.xpDropStatus = ImageLoader.forName("xpdrop_status");
        this.chatBack = ImageLoader.forName("CHAT_BACK");
        this.world = ImageLoader.forName("WORLD");
        this.worldHover = ImageLoader.forName("WORLD_HOVER");
        this.worldBack = ImageLoader.forName("WORLD_MAP_BACK");
    }

    @Override
    public void drawMinimapArea() {
        int xPosOffset = Game.isFixed() ? 0 : Game.getFrameWidth() - 246;
        if (Game.isFixed()) {
            getMinimapImageProducer().initDrawingArea();
        }
        if (getGame().minimapState == 2) {
            if (Game.isFixed()) {
                blackMapBack.drawSprite(xPosOffset, 0);
            } else {
                fullScreenMapBack.drawSprite(Game.getFrameWidth() - 167, 0);
                compassBack.drawSprite(Game.getFrameWidth() - 172, 0);
                fullScreenMapBackBlack.drawSprite(Game.getFrameWidth() - 158, 7);
            }
            (logoutClick ? logoutXHover : logoutX).drawSprite(Game.isFixed() ? 224 : Game.getFrameWidth() - 25, Game.isFixed() ? 3 : 2);
            if (getGame().getInterfaceConfiguration(InterfaceConfiguration.ORBS_ENABLED)) {
                drawXPTracker(Game.isFixed() ? 0 : Game.getFrameWidth() - 217);
                try {
                    loadAllOrbs(Game.isFixed() ? 0 : Game.getFrameWidth() - 217);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            drawWorld();
            getGame().compass.rotate(33, getGame().cameraHorizontal, getGame().anIntArray1057, 256,
                    getGame().anIntArray968, 25, (Game.isFixed() ? 8 : 5),
                    (Game.isFixed() ? 8 + xPosOffset : Game.getFrameWidth() - 167), 33,
                    25);
            if (getGame().isMenuOpen()) {
                getGame().drawMenu(Game.isScreenMode(Game.ScreenMode.FIXED) ? 516 : 0, 0);
            }
            if (Game.isFixed()) {
                getMinimapImageProducer().initDrawingArea();
            }
            return;
        }
        int angle = getGame().cameraHorizontal + getGame().minimapRotation & 0x7ff;
        int centreX = 48 + Game.getLocalPlayer().x / 32;
        int centreY = 464 - Game.getLocalPlayer().y / 32;
        getGame().minimapImage.rotate2(156, angle, getGame().minimapLineWidth,
                256 + getGame().minimapZoom, getGame().minimapLeft, centreY, (Game.isFixed() ? 8
                        : 4/* 5 */),
                (Game.isFixed() ? 32 : Game.getFrameWidth() - 162),
                Game.isFixed() ? 156 : 156, centreX);
        for (int icon = 0; icon < getGame().minimapCount; icon++) {
            int mapX = (getGame().minimapHintX[icon] * 4 + 2) - Game.getLocalPlayer().x / 32;
            int mapY = (getGame().minimapHintY[icon] * 4 + 2) - Game.getLocalPlayer().y / 32;
            markMinimap(getGame().minimapHint[icon], mapX, mapY);
        }
        for (int[] minimapIcon : Constants.MINIMAP_ICONS) {
            Sprite mapFunction = minimapIcon[0] > 100 ? getGame().mapFunctions2[minimapIcon[0] - 101] : getGame().mapFunctions[minimapIcon[0]];
            markMinimap(mapFunction, ((minimapIcon[1] - Game.regionBaseX) * 4 + 2) -
                    Game.getLocalPlayer().x / 32, ((minimapIcon[2] - Game.regionBaseY) * 4 + 2) - Game.getLocalPlayer().y / 32);
        }
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                Deque class19 = getGame().groundItems[getGame().plane][x][y];
                if (class19 != null) {
                    int mapX = (x * 4 + 2) - Game.getLocalPlayer().x / 32;
                    int mapY = (y * 4 + 2) - Game.getLocalPlayer().y / 32;
                    markMinimap(getGame().mapDotItem, mapX, mapY);
                }
            }
        }
        for (int n = 0; n < getGame().npcCount; n++) {
            NPC npc = getGame().npcs[getGame().npcIndices[n]];
            if (npc != null && npc.isVisible()) {
                NPCDefinition entityDef = npc.desc;
                if (entityDef.childrenIDs != null) {
                    entityDef = entityDef.morph();
                }
                if (entityDef != null && entityDef.drawMinimapDot
                        && entityDef.clickable) {
                    int mapX = npc.x / 32 - Game.getLocalPlayer().x / 32;
                    int mapY = npc.y / 32 - Game.getLocalPlayer().y / 32;
                    markMinimap(getGame().mapDotNPC, mapX, mapY);
                }
            }
        }
        for (int j6 = 0;
             j6 < getGame().playerCount;
             j6++) {
            Player player = getGame().players[getGame().playerIndices[j6]];
            if (player != null && player.isVisible()) {
                int iconX = player.x / 32 - Game.getLocalPlayer().x / 32;
                int iconY = player.y / 32 - Game.getLocalPlayer().y / 32;
                boolean flag1 = false;
                boolean isClanMember = false;
                if (getGame().getClanChatMembers() != null) {
                    for (ClanChatMember clanChatMember : getGame().getClanChatMembers()) {
                        if (clanChatMember == null) {
                            continue;
                        }
                        if (clanChatMember.getName() == StringUtility.encodeBase37(Game.getLocalPlayer().name)) {
                            continue;
                        }
                        if (clanChatMember.getName() != StringUtility.encodeBase37(player.name)) {
                            continue;
                        }
                        isClanMember = true;
                        break;
                    }
                }
                long l6 = StringUtility.encodeBase37(player.name);
                for (int k6 = 0;
                     k6 < getGame().friendsCount;
                     k6++) {
                    if (l6 != getGame().friendsListAsLongs[k6] || getGame().friendsNodeIDs[k6] == 0) {
                        continue;
                    }
                    flag1 = true;
                    break;
                }
                boolean flag2 = false;
                if (Game.getLocalPlayer().team != 0 && player.team != 0 && Game.getLocalPlayer().team == player.team) {
                    flag2 = true;
                }
                if (flag1) {
                    markMinimap(getGame().mapDotFriend, iconX, iconY);
                } else if (isClanMember) {
                    markMinimap(getGame().mapDotClan, iconX, iconY);
                } else if (flag2) {
                    markMinimap(getGame().mapDotTeam, iconX, iconY);
                } else {
                    markMinimap(getGame().mapDotPlayer, iconX, iconY);
                }
            }
        }
        if (getGame().hintIconDrawType != 0 && Game.loopCycle % 20 < 10) {
            if (getGame().hintIconDrawType == 1 && getGame().hintIconNpcId >= 0
                    && getGame().hintIconNpcId < getGame().npcs.length) {
                NPC npc = getGame().npcs[getGame().hintIconNpcId];
                if (npc != null) {
                    int mapX = npc.x / 32 - Game.getLocalPlayer().x / 32;
                    int mapY = npc.y / 32 - Game.getLocalPlayer().y / 32;
                    refreshMinimap(getGame().mapMarker, mapY, mapX);
                }
            }
            if (getGame().hintIconDrawType == 2) {
                int mapX = ((getGame().hintIconX - getGame().getRegionBaseX()) * 4 + 2) - Game.getLocalPlayer().x
                        / 32;
                int mapY = ((getGame().hintIconY - getGame().getRegionBaseY()) * 4 + 2) - Game.getLocalPlayer().y
                        / 32;
                refreshMinimap(getGame().mapMarker, mapY, mapX);
            }
            if (getGame().hintIconDrawType == 10 && getGame().hintIconPlayerId >= 0
                    && getGame().hintIconPlayerId < getGame().players.length) {
                Player player = getGame().players[getGame().hintIconPlayerId];
                if (player != null) {
                    int mapX = player.x / 32 - Game.getLocalPlayer().x / 32;
                    int mapY = player.y / 32 - Game.getLocalPlayer().y / 32;
                    refreshMinimap(getGame().mapMarker, mapY, mapX);
                }
            }
        }
        if (getGame().destX != 0) {
            int mapX = (getGame().destX * 4 + 2) - Game.getLocalPlayer().x / 32;
            int mapY = (getGame().destY * 4 + 2) - Game.getLocalPlayer().y / 32;
            markMinimap(getGame().mapFlag, mapX, mapY);
        }
        Raster.drawPixels(3, (Game.isFixed() ? 84 : 80), (Game.isFixed() ? 107 + xPosOffset : Game.getFrameWidth() - 88), 0xFFFFFF, 3);
        if (Game.isFixed()) {
            mapBack.drawSprite(xPosOffset, 0);
        } else {
            fullScreenMapBack.drawSprite(Game.getFrameWidth() - 167, 0);
            compassBack.drawSprite(Game.getFrameWidth() - 172, 0);
        }
        getGame().compass.rotate(33, getGame().cameraHorizontal, getGame().anIntArray1057, 256, getGame().anIntArray968,
                25, (Game.isFixed() ? 8 : 5),
                (Game.isFixed() ? 8 + xPosOffset : Game.getFrameWidth() - 167), 33, 25);
        (logoutClick ? logoutXHover : logoutX).drawSprite(Game.isFixed() ? 224 : Game.getFrameWidth() - 25, Game.isFixed() ? 3 : 2);
        if ((getGame().getInterfaceConfiguration(InterfaceConfiguration.ORBS_ENABLED))) {
            try {
                loadAllOrbs(Game.isFixed() ? 0 : Game.getFrameWidth() - 217);
            } catch (Exception e) {
                e.printStackTrace();
            }
            drawXPTracker(Game.isFixed() ? 0 : Game.getFrameWidth() - 217);
        }
        drawWorld();
        if (getGame().isMenuOpen()) {
            getGame().drawMenu(Game.isScreenMode(Game.ScreenMode.FIXED) ? 519 : 0, 0);
        }
        if (Game.isFixed()) {
            getGameScreenImageProducer().initDrawingArea();
        }
    }

    @Override
    public void drawMinimapSprite() {
        mapBack.drawSprite(0, 0);
    }

    @Override
    public void renderMapScene(int currentPlane) {
        int ai[] = getGame().minimapImage.myPixels;
        int j = ai.length;
        for (int k = 0;
             k < j;
             k++) {
            ai[k] = 0;
        }
        for (int l = 1; l < 103; l++) {
            int i1 = 24628 + (103 - l) * 512 * 4;
            for (int k1 = 1; k1 < 103; k1++) {
                if ((getGame().tileFlags[currentPlane][k1][l] & 0x18) == 0) {
                    getGame().scene.drawTileMinimap(ai, i1, currentPlane, k1, l);
                }
                if (currentPlane < 3 && (getGame().tileFlags[currentPlane + 1][k1][l] & 8) != 0) {
                    getGame().scene.drawTileMinimap(ai, i1, currentPlane + 1, k1, l);
                }
                i1 += 4;
            }
        }
        int j1 = 0xFFFFFF;
        int l1 = 0xEE0000;
        getGame().minimapImage.initDrawingArea();
        for (int i2 = 1; i2 < 103; i2++) {
            for (int j2 = 1; j2 < 103; j2++) {
                if ((getGame().tileFlags[currentPlane][j2][i2] & 0x18) == 0) {
                    drawMapScenes(i2, j1, j2, l1, currentPlane);
                }
                if (currentPlane < 3 && (getGame().tileFlags[currentPlane + 1][j2][i2] & 8) != 0) {
                    drawMapScenes(i2, j1, j2, l1, currentPlane + 1);
                }
            }
        }
        if (Game.loggedIn) {
            getGameScreenImageProducer().initDrawingArea();
        }
        getGame().minimapCount = 0;
        for (int tileX = 0; tileX < 104; tileX++) {
            for (int tileY = 0; tileY < 104; tileY++) {
                int objectId = getGame().scene.getFloorDecorationKey(getGame().plane, tileX, tileY);
                if (objectId != 0) {
                    ObjectDefinition objectDefinition = ObjectDefinition.forId(objectId);
                    if (objectDefinition == null) {
                        objectDefinition = ObjectDefinition.forId(objectId >> 14 & 0x7fff);
                    }
                    if (objectDefinition == null) {
                        continue;
                    }
                    int mapIcon = objectDefinition.mapIcon;
                    if (mapIcon >= 15 && mapIcon <= 67) {
                        mapIcon -= 2;
                    } else if (mapIcon >= 68 && mapIcon <= 84) {
                        mapIcon -= 1;
                    }
                    if (mapIcon >= 0) {
                        Sprite mapFunction = mapIcon > 100 ? getGame().mapFunctions2[mapIcon - 101] : getGame().mapFunctions[mapIcon];
                        getGame().minimapHint[getGame().minimapCount] = mapFunction;
                        getGame().minimapHintX[getGame().minimapCount] = tileX;
                        getGame().minimapHintY[getGame().minimapCount] = tileY;
                        getGame().minimapCount++;
                    }
                }
            }
        }
    }

    @Override
    public void refreshMinimap(Sprite sprite, int yOffset, int xOffset) {
        int l = xOffset * xOffset + yOffset * yOffset;
        if (l > 4225 && l < 0x15f90) {
            int i1 = getGame().cameraHorizontal + getGame().minimapRotation & 0x7ff;
            int j1 = Model.SINE[i1];
            int k1 = Model.COSINE[i1];
            j1 = (j1 * 256) / (getGame().minimapZoom + 256);
            k1 = (k1 * 256) / (getGame().minimapZoom + 256);
            int l1 = yOffset * j1 + xOffset * k1 >> 16;
            int i2 = yOffset * k1 - xOffset * j1 >> 16;
            double d = Math.atan2(l1, i2);
            @SuppressWarnings("unused")
            int j2 = (int) (Math.sin(d) * 63D);
            @SuppressWarnings("unused")
            int k2 = (int) (Math.cos(d) * 57D);
// TODO
//getGame().mapEdge.rotate(83 - k2 - 20, d, (94 + j2 + 4) - 10);
        } else {
            markMinimap(sprite, xOffset, yOffset);
        }
    }

    @Override
    public void drawMapScenes(int y, int primaryColor, int x, int secondaryColor, int z) {
        int uid = getGame().scene.getWallKey(z, x, y);
        if (uid != 0) {
            int l1 = getGame().scene.getIDTagForXYZ(z, x, y, uid);
            int k2 = l1 >> 6 & 3;
            int i3 = l1 & 0x1f;
            int k3 = primaryColor;
            if (uid > 0) {
                k3 = secondaryColor;
            }
            int ai[] = getGame().minimapImage.myPixels;
            int k4 = 24624 + x * 4 + (103 - y) * 512 * 4;
            int i5 = uid >> 14 & 0x7fff;
            ObjectDefinition mapGameObject = ObjectDefinition.forId(i5);
            if (mapGameObject.mapscene != -1) {
                Background background_2 = getGame().mapScenes[mapGameObject.mapscene];
                if (background_2 != null) {
                    int i6 = (mapGameObject.sizeX * 4 - background_2.anInt1452) / 2;
                    int j6 = (mapGameObject.sizeY * 4 - background_2.anInt1453) / 2;
                    background_2.drawBackground(48 + x * 4 + i6, 48
                            + (104 - y - mapGameObject.sizeY) * 4 + j6);
                }
            } else {
                if (i3 == 0 || i3 == 2) {
                    if (k2 == 0) {
                        ai[k4] = k3;
                        ai[k4 + 512] = k3;
                        ai[k4 + 1024] = k3;
                        ai[k4 + 1536] = k3;
                    } else if (k2 == 1) {
                        ai[k4] = k3;
                        ai[k4 + 1] = k3;
                        ai[k4 + 2] = k3;
                        ai[k4 + 3] = k3;
                    } else if (k2 == 2) {
                        ai[k4 + 3] = k3;
                        ai[k4 + 3 + 512] = k3;
                        ai[k4 + 3 + 1024] = k3;
                        ai[k4 + 3 + 1536] = k3;
                    } else if (k2 == 3) {
                        ai[k4 + 1536] = k3;
                        ai[k4 + 1536 + 1] = k3;
                        ai[k4 + 1536 + 2] = k3;
                        ai[k4 + 1536 + 3] = k3;
                    }
                }
                if (i3 == 3) {
                    if (k2 == 0) {
                        ai[k4] = k3;
                    } else if (k2 == 1) {
                        ai[k4 + 3] = k3;
                    } else if (k2 == 2) {
                        ai[k4 + 3 + 1536] = k3;
                    } else if (k2 == 3) {
                        ai[k4 + 1536] = k3;
                    }
                }
                if (i3 == 2) {
                    if (k2 == 3) {
                        ai[k4] = k3;
                        ai[k4 + 512] = k3;
                        ai[k4 + 1024] = k3;
                        ai[k4 + 1536] = k3;
                    } else if (k2 == 0) {
                        ai[k4] = k3;
                        ai[k4 + 1] = k3;
                        ai[k4 + 2] = k3;
                        ai[k4 + 3] = k3;
                    } else if (k2 == 1) {
                        ai[k4 + 3] = k3;
                        ai[k4 + 3 + 512] = k3;
                        ai[k4 + 3 + 1024] = k3;
                        ai[k4 + 3 + 1536] = k3;
                    } else if (k2 == 2) {
                        ai[k4 + 1536] = k3;
                        ai[k4 + 1536 + 1] = k3;
                        ai[k4 + 1536 + 2] = k3;
                        ai[k4 + 1536 + 3] = k3;
                    }
                }
            }
        }
        uid = getGame().scene.getStaticObjectKey(z, x, y);
        if (uid != 0) {
            int i2 = getGame().scene.getIDTagForXYZ(z, x, y, uid);
            int l2 = i2 >> 6 & 3;
            int j3 = i2 & 0x1f;
            int l3 = uid >> 14 & 0x7fff;
            ObjectDefinition interactedObject = ObjectDefinition.forId(l3);
            if (interactedObject.mapscene != -1) {
                Background background_1 = getGame().mapScenes[interactedObject.mapscene];
                if (background_1 != null) {
                    int j5 = (interactedObject.sizeX * 4 - background_1.anInt1452) / 2;
                    int k5 = (interactedObject.sizeY * 4 - background_1.anInt1453) / 2;
                    background_1.drawBackground(48 + x * 4 + j5, 48
                            + (104 - y - interactedObject.sizeY) * 4 + k5);
                }
            } else if (j3 == 9) {
                int l4 = 0xeeeeee;
                if (uid > 0) {
                    l4 = 0xee0000;
                }
                int ai1[] = getGame().minimapImage.myPixels;
                int l5 = 24624 + x * 4 + (103 - y) * 512 * 4;
                if (l2 == 0 || l2 == 2) {
                    ai1[l5 + 1536] = l4;
                    ai1[l5 + 1024 + 1] = l4;
                    ai1[l5 + 512 + 2] = l4;
                    ai1[l5 + 3] = l4;
                } else {
                    ai1[l5] = l4;
                    ai1[l5 + 512 + 1] = l4;
                    ai1[l5 + 1024 + 2] = l4;
                    ai1[l5 + 1536 + 3] = l4;
                }
            }
        }
        uid = getGame().scene.getFloorDecorationKey(z, x, y);
        if (uid != 0) {
            int j2 = uid >> 14 & 0x7fff;
            ObjectDefinition objectDefinition = ObjectDefinition.forId(j2);
            if (objectDefinition.mapscene != -1) {
                Background background = getGame().mapScenes[objectDefinition.mapscene];
                if (background != null) {
                    int i4 = (objectDefinition.sizeX * 4 - background.anInt1452) / 2;
                    int j4 = (objectDefinition.sizeY * 4 - background.anInt1453) / 2;
                    background.drawBackground(48 + x * 4 + i4, 48
                            + (104 - y - objectDefinition.sizeY) * 4 + j4);
                }
            }
        }
    }

    @Override
    public void markMinimap(Sprite sprite, int iconX, int iconY) {
        if (sprite == null) {
            return;
        }
        try {
            int offX = Game.isFixed() ? 0 : Game.getFrameWidth() - 249;
            int k = getGame().cameraHorizontal + getGame().minimapRotation & 0x7ff;
            int l = iconX * iconX + iconY * iconY;
            if (l > 6400) {
                return;
            }
            int i1 = Model.SINE[k];
            int j1 = Model.COSINE[k];
            i1 = (i1 * 256) / (getGame().minimapZoom + 256);
            j1 = (j1 * 256) / (getGame().minimapZoom + 256);
            int k1 = iconY * i1 + iconX * j1 >> 16;
            int l1 = iconY * j1 - iconX * i1 >> 16;
            if (Game.isFixed()) {
                sprite.drawSprite(
                        ((105 + k1) - sprite.maxWidth / 2) + 4 + offX, 88 - l1
                                - sprite.maxHeight / 2 - 4);
            } else {
                sprite.drawSprite(((77 + k1) - sprite.maxWidth / 2) + 4
                        + (Game.getFrameWidth() - 167), 85 - l1 - sprite.maxHeight / 2
                        - 4);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public int getMinimapProducerWidth() {
        return 519;
    }

    @Override
    public void drawTabArea() {
        final int xOffset = Game.isFixed() ? 0 : Game.getFrameWidth() - 241;
        final int yOffset = Game.isFixed() ? 0 : Game.getFrameHeight() - 336;
        if (Game.isFixed()) {
            getTabImageProducer().initDrawingArea();
        }
        Rasterizer.lineOffsets = Game.anIntArray1181;
        if (Game.isFixed()) {
            tabBack.drawSprite(0, 0);
            if (getGame().getOpenInterfaceId() == GrandExchange.GRAND_EXCHANGE_OFFER_INTERFACE_ID &&
                    getGame().getInterfaceConfiguration(InterfaceConfiguration.GRAND_EXCHANGE_OFFER_TYPE) && !getGame().getInterfaceConfiguration(InterfaceConfiguration.GRAND_EXCHANGE_VIEW_SCREEN)) {
                ImageLoader.forName("GE_INVENTORY_BACK").drawARGBSprite(31, 36, getGame().spriteAlphaValues[0][0]);
            }
        } else if (!Game.isFixed() && !getGame().getInterfaceConfiguration(InterfaceConfiguration.SIDE_STONES_ARRANGEMENT)) {
            Raster.fillRectangle2(0x3E3529, Game.getFrameHeight() - 304, 195, 270,
                    getGame().getInterfaceConfiguration(InterfaceConfiguration.TRANSPARENT_SIDE_PANEL) ? 80 : 256, Game.getFrameWidth() - 217);
            if (getGame().getOpenInterfaceId() == GrandExchange.GRAND_EXCHANGE_OFFER_INTERFACE_ID &&
                    getGame().getInterfaceConfiguration(InterfaceConfiguration.GRAND_EXCHANGE_OFFER_TYPE) && !getGame().getInterfaceConfiguration(InterfaceConfiguration.GRAND_EXCHANGE_VIEW_SCREEN)) {
                Raster.fillRectangle2(0x6F6434, Game.getFrameHeight() - 304, 195, 270, getGame().spriteAlphaValues[0][0], Game.getFrameWidth() - 217);
            }
            tabBackEmpty.drawSprite(xOffset, yOffset);
        } else {
            if (Game.getFrameWidth() >= 1000) {
                if (Game.showTabComponents) {
                    Raster.fillRectangle2(0x3E3529, Game.getFrameHeight() - 304, 197, 265,
                            getGame().getInterfaceConfiguration(InterfaceConfiguration.TRANSPARENT_SIDE_PANEL) ? 80 : 256, Game.getFrameWidth() - 197);
                    tabBorder.drawSprite(Game.getFrameWidth() - 204,
                            Game.getFrameHeight() - 311);
                    if (getGame().getOpenInterfaceId() == GrandExchange.GRAND_EXCHANGE_OFFER_INTERFACE_ID &&
                            getGame().getInterfaceConfiguration(InterfaceConfiguration.GRAND_EXCHANGE_OFFER_TYPE) && !getGame().getInterfaceConfiguration(InterfaceConfiguration.GRAND_EXCHANGE_VIEW_SCREEN)) {
                        Raster.fillRectangle2(0x6F6434, Game.getFrameHeight() - 304, 189, 260, getGame().spriteAlphaValues[0][0], Game.getFrameWidth() - 197);
                    }
                }
                for (int x = Game.getFrameWidth() - 417, y = Game.getFrameHeight() - 37, index = 0;
                     x <= Game.getFrameWidth() - 30
                             && index < 13;
                     x += 32, index++) {
                    tabStone.drawSprite(x, y);
                }
            } else if (Game.getFrameWidth() < 1000) {
                if (Game.showTabComponents) {
                    Raster.fillRectangle2(0x3E3529, Game.getFrameHeight() - 341, 195, 265,
                            getGame().getInterfaceConfiguration(InterfaceConfiguration.TRANSPARENT_SIDE_PANEL) ? 80 : 256, Game.getFrameWidth() - 197);
                    tabBorder.drawSprite(Game.getFrameWidth() - 204,
                            Game.getFrameHeight() - 348);
                    if (getGame().getOpenInterfaceId() == GrandExchange.GRAND_EXCHANGE_OFFER_INTERFACE_ID &&
                            getGame().getInterfaceConfiguration(InterfaceConfiguration.GRAND_EXCHANGE_OFFER_TYPE) && !getGame().getInterfaceConfiguration(InterfaceConfiguration.GRAND_EXCHANGE_VIEW_SCREEN)) {
                        Raster.fillRectangle2(0x6F6434, Game.getFrameHeight() - 342, 189, 260, getGame().spriteAlphaValues[0][0], Game.getFrameWidth() - 197);
                    }
                }
                for (int x = Game.getFrameWidth() - 226, y = Game.getFrameHeight() - 73, index = 0;
                     x <= Game.getFrameWidth() - 32
                             && index < 7;
                     x += 32, index++) {
                    tabStone.drawSprite(x, y);
                }
                for (int x = Game.getFrameWidth() - 226, y = Game.getFrameHeight() - 37, index = 0;
                     x <= Game.getFrameWidth() - 32
                             && index < 7;
                     x += 32, index++) {
                    tabStone.drawSprite(x, y);
                }
            }
        }
        if (getGame().overlayInterfaceId == -1) {
            drawRedStones();
            drawSideIcons();
        }
        if (Game.showTabComponents) {
            int x = Game.isFixed() ? 31 : Game.getFrameWidth() - 215;
            int y = Game.isFixed() ? 37 : Game.getFrameHeight() - 299;
            if (getGame().getInterfaceConfiguration(InterfaceConfiguration.SIDE_STONES_ARRANGEMENT) && !Game.isFixed()) {
                x = Game.getFrameWidth() - 197;
                y = Game.getFrameWidth() >= 1000 ? Game.getFrameHeight() - 303 : Game.getFrameHeight() - 340;
            }
            if (getGame().overlayInterfaceId != -1) {
                getGame().getComponentDrawing().drawComponent(RSComponent.forId(getGame().overlayInterfaceId), x, y, 0, 0);
            } else if (Game.tabInterfaceIDs[Game.currentTabId] != -1) {
                getGame().getComponentDrawing().drawComponent(RSComponent.forId(Game.tabInterfaceIDs[Game.currentTabId]), x, y, 0, 0);
            }
        }
        if (getGame().isMenuOpen()) {
            getGame().drawMenu(Game.isScreenMode(Game.ScreenMode.FIXED) ? 516 : 0, Game.isScreenMode(Game.ScreenMode.FIXED) ? 168 : 0);
        }
        if (Game.isFixed()) {
            getTabImageProducer().drawGraphics(168, getGame().graphics, 519);
            getGameScreenImageProducer().initDrawingArea();
        }
        Rasterizer.lineOffsets = Game.anIntArray1182;
    }

    @Override
    public void processTabArea() {
    }

    @Override
    public void processTabHover() {
    }

    @Override
    public void processTabs() {
    }

    public void drawRedStones() {
        int xOffset = Game.isFixed() ? 0 : Game.getFrameWidth() - 247;
        int yOffset = Game.isFixed() ? 0 : Game.getFrameHeight() - 336;
        boolean sideStoneArrangement = getGame().getInterfaceConfiguration(InterfaceConfiguration.SIDE_STONES_ARRANGEMENT);
        int fix = Game.isFixed() ? -3 : (!sideStoneArrangement) ? 0 : 3;
        if (Game.currentTabId == 14) {
            return;
        }
        if (Game.isFixed() || !Game.isFixed() && !sideStoneArrangement) {
            if (Game.currentTabId == 7) {
                redStones[4].drawSprite(Game.isFixed() ? 107 : Game.getFrameWidth() - 130 + fix - (!sideStoneArrangement ? 6 : 0), Game.isFixed() ? 298 : Game.getFrameHeight() - 37);
            } else if (Game.tabInterfaceIDs[Game.currentTabId] != -1 && Game.currentTabId != 10) {
                redStones[redStonesId[Game.currentTabId]].drawSprite(redStonesX[Game.currentTabId] + fix + xOffset,
                        redStonesY[Game.currentTabId] + yOffset);
            }
        } else if (sideStoneArrangement && Game.getFrameWidth() < 1000) {
            int[] stoneX = {226, 194, 162, 130, 99, 65, 34, 219, 195, 161, 130, 98, 65, 33};
            int[] stoneY = {73, 73, 73, 73, 73, 73, 73, -1, 37, 37, 37, 37, 37, 37, 37};
            if (Game.tabInterfaceIDs[Game.currentTabId] != -1 && Game.currentTabId != 10 && Game.showTabComponents) {
                if (Game.currentTabId == 7) {
                    redStones[4].drawSprite(Game.getFrameWidth() - 130, Game.getFrameHeight() - 37);
                } else if (Game.currentTabId != 10) {
                    redStones[4].drawSprite(Game.getFrameWidth() + (Game.isFixed() ? 0 : 3) - stoneX[Game.currentTabId], Game.getFrameHeight() - stoneY[Game.currentTabId]);
                }
            }
        } else if (sideStoneArrangement && Game.getFrameWidth() >= 1000) {
            int[] stoneX = {417, 385, 353, 321, 289, 256, 224, 129, 193, 161, 130, 98, 65, 33};
            if (Game.tabInterfaceIDs[Game.currentTabId] != -1 && Game.currentTabId != 10 && Game.showTabComponents) {
                redStones[4].drawSprite(Game.getFrameWidth() - stoneX[Game.currentTabId], Game.getFrameHeight() - 37);
            }
        }
    }

    @Override
    public void drawSideIcons() {
        int xOffset = Game.isFixed() ? 0 : Game.getFrameWidth() - 247;
        int yOffset = Game.isFixed() ? 0 : Game.getFrameHeight() - 336;
        if (Game.isFixed() || !Game.isFixed() && !getGame().getInterfaceConfiguration(InterfaceConfiguration.SIDE_STONES_ARRANGEMENT)) {
            for (int i = 0; i < sideIconsTab.length; i++) {
                if (Game.tabInterfaceIDs[sideIconsTab[i]] != -1) {
                    if (sideIconsId[i] == 10) {
                        continue;
                    }
                    if (sideIconsId[i] != -1) {
                        Sprite sprite = sideIcons[sideIconsId[i]];
                        if (i == 2) {
                            if (Game.tabInterfaceIDs[sideIconsTab[2]] == 22697) {
                                sprite = getGame().informationTabIcon;
                            }
                            if (Game.tabInterfaceIDs[sideIconsTab[2]] == 29300) {
                                sprite = getGame().achievementTabIcon;
                            }
                            if (Game.tabInterfaceIDs[sideIconsTab[2]] == 638) {
                                sprite = getGame().questTabIcon;
                            }
                        }
                        sprite.drawSprite(sideIconsX[i] - (Game.isFixed() ? 4 : 0) + xOffset, sideIconsY[i] + yOffset);
                    }
                }
            }
        } else if (getGame().getInterfaceConfiguration(InterfaceConfiguration.SIDE_STONES_ARRANGEMENT) && Game.getFrameWidth() < 1000) {
            int[] iconId = {0, 1, 2, 3, 4, 5, 6, -1, 8, 9, 7, 11, 12, 13};
            int[] iconX = {219, 189, 156, 126, 93, 62, 30, 219, 189, 156, 124, 92, 59, 28};
            int[] iconY = {67, 69, 67, 69, 72, 72, 69, 32, 29, 29, 32, 30, 33, 31, 32};
            for (int i = 0; i < sideIconsTab.length; i++) {
                if (Game.tabInterfaceIDs[sideIconsTab[i]] != -1) {
                    if (iconId[i] != -1) {
                        Sprite sprite = sideIcons[iconId[i]];
                        if (i == 2) {
                            if (Game.tabInterfaceIDs[sideIconsTab[2]] == 22697) {
                                sprite = getGame().informationTabIcon;
                            }
                            if (Game.tabInterfaceIDs[sideIconsTab[2]] == 29300) {
                                sprite = getGame().achievementTabIcon;
                            }
                            if (Game.tabInterfaceIDs[sideIconsTab[2]] == 638) {
                                sprite = getGame().questTabIcon;
                            }
                        }
                        sprite.drawSprite(Game.getFrameWidth() - iconX[i], Game.getFrameHeight() - iconY[i]);
                    }
                }
            }
        } else if (getGame().getInterfaceConfiguration(InterfaceConfiguration.SIDE_STONES_ARRANGEMENT) && Game.getFrameWidth() >= 1000) {
            int[] iconId = {0, 1, 2, 3, 4, 5, 6, -1, 8, 9, 7, 11, 12, 13};
            int[] iconX = {50, 80, 114, 143, 176, 208, 240, 242, 273, 306,
                    338, 370, 404, 433};
            int[] iconY = {30, 32, 30, 32, 34, 34, 32, 32, 29, 29, 32, 31, 32,
                    32, 32};
            for (int i = 0; i < sideIconsTab.length; i++) {
                if (Game.tabInterfaceIDs[sideIconsTab[i]] != -1) {
                    if (iconId[i] != -1) {
                        Sprite sprite = sideIcons[iconId[i]];
                        if (i == 2) {
                            if (Game.tabInterfaceIDs[sideIconsTab[2]] == 22697) {
                                sprite = getGame().informationTabIcon;
                            }
                            if (Game.tabInterfaceIDs[sideIconsTab[2]] == 29300) {
                                sprite = getGame().achievementTabIcon;
                            }
                            if (Game.tabInterfaceIDs[sideIconsTab[2]] == 638) {
                                sprite = getGame().questTabIcon;
                            }
                        }
                        sprite.drawSprite(Game.getFrameWidth() - 461 + iconX[i], Game.getFrameHeight() - iconY[i]);
                    }
                }
            }
        }
    }

    @Override
    public void processTabAreaHovers() {
    }

    @Override
    public void processTabAreaClick() {
        if (getGame().clickMode3 == 1) {
            if (Game.isFixed() || !Game.isFixed() && !getGame().getInterfaceConfiguration(InterfaceConfiguration.SIDE_STONES_ARRANGEMENT)) {
                int xOffset = Game.isFixed() ? 0
                        : Game.getFrameWidth() - 765;
                int yOffset = Game.isFixed() ? 0
                        : Game.getFrameHeight() - 503;
                for (int tabIndex = 0; tabIndex < tabClickX.length; tabIndex++) {
                    if (getGame().mouseX >= tabClickStart[tabIndex] + xOffset
                            && getGame().mouseX <= tabClickStart[tabIndex] + tabClickX[tabIndex] - 4 + xOffset
                            && getGame().mouseY >= tabClickY[tabIndex] + yOffset
                            && getGame().mouseY < tabClickY[tabIndex] + 37 + yOffset
                            && Game.tabInterfaceIDs[tabIndex] != -1) {
                        if (tabIndex == 10) {
                            Game.setTab(7, false);
                            break;
                        }
                        if (tabIndex == 7) {
                            break;
                        }
                        Game.setTab(tabIndex, false);
                        break;
                    }
                }
            } else if (getGame().getInterfaceConfiguration(InterfaceConfiguration.SIDE_STONES_ARRANGEMENT) && Game.getFrameWidth() < 1000) {
                if (getGame().saveClickX >= Game.getFrameWidth() - 226
                        && getGame().saveClickX <= Game.getFrameWidth() - 195
                        && getGame().saveClickY >= Game.getFrameHeight() - 72
                        && getGame().saveClickY < Game.getFrameHeight() - 40
                        && Game.tabInterfaceIDs[0] != -1) {
                    if (Game.currentTabId == 0) {
                        Game.showTabComponents = !Game.showTabComponents;
                    } else {
                        Game.showTabComponents = true;
                    }
                    Game.setTab(0, false);
                }
                if (getGame().saveClickX >= Game.getFrameWidth() - 194
                        && getGame().saveClickX <= Game.getFrameWidth() - 163
                        && getGame().saveClickY >= Game.getFrameHeight() - 72
                        && getGame().saveClickY < Game.getFrameHeight() - 40
                        && Game.tabInterfaceIDs[1] != -1) {
                    if (Game.currentTabId == 1) {
                        Game.showTabComponents = !Game.showTabComponents;
                    } else {
                        Game.showTabComponents = true;
                    }
                    Game.setTab(1, false);
                }
                if (getGame().saveClickX >= Game.getFrameWidth() - 162
                        && getGame().saveClickX <= Game.getFrameWidth() - 131
                        && getGame().saveClickY >= Game.getFrameHeight() - 72
                        && getGame().saveClickY < Game.getFrameHeight() - 40
                        && Game.tabInterfaceIDs[2] != -1) {
                    if (Game.currentTabId == 2) {
                        Game.showTabComponents = !Game.showTabComponents;
                    } else {
                        Game.showTabComponents = true;
                    }
                    Game.setTab(2, false);
                }
                if (getGame().saveClickX >= Game.getFrameWidth() - 129
                        && getGame().saveClickX <= Game.getFrameWidth() - 98
                        && getGame().saveClickY >= Game.getFrameHeight() - 72
                        && getGame().saveClickY < Game.getFrameHeight() - 40
                        && Game.tabInterfaceIDs[3] != -1) {
                    if (Game.currentTabId == 3) {
                        Game.showTabComponents = !Game.showTabComponents;
                    } else {
                        Game.showTabComponents = true;
                    }
                    Game.setTab(3, false);
                }
                if (getGame().saveClickX >= Game.getFrameWidth() - 97
                        && getGame().saveClickX <= Game.getFrameWidth() - 66
                        && getGame().saveClickY >= Game.getFrameHeight() - 72
                        && getGame().saveClickY < Game.getFrameHeight() - 40
                        && Game.tabInterfaceIDs[4] != -1) {
                    if (Game.currentTabId == 4) {
                        Game.showTabComponents = !Game.showTabComponents;
                    } else {
                        Game.showTabComponents = true;
                    }
                    Game.setTab(4, false);
                }
                if (getGame().saveClickX >= Game.getFrameWidth() - 65
                        && getGame().saveClickX <= Game.getFrameWidth() - 34
                        && getGame().saveClickY >= Game.getFrameHeight() - 72
                        && getGame().saveClickY < Game.getFrameHeight() - 40
                        && Game.tabInterfaceIDs[5] != -1) {
                    if (Game.currentTabId == 5) {
                        Game.showTabComponents = !Game.showTabComponents;
                    } else {
                        Game.showTabComponents = true;
                    }
                    Game.setTab(5, false);
                }
                if (getGame().saveClickX >= Game.getFrameWidth() - 33
                        && getGame().saveClickX <= Game.getFrameWidth()
                        && getGame().saveClickY >= Game.getFrameHeight() - 72
                        && getGame().saveClickY < Game.getFrameHeight() - 40
                        && Game.tabInterfaceIDs[6] != -1) {
                    if (Game.currentTabId == 6) {
                        Game.showTabComponents = !Game.showTabComponents;
                    } else {
                        Game.showTabComponents = true;
                    }
                    Game.setTab(6, false);
                }
                if (getGame().saveClickX >= Game.getFrameWidth() - 194
                        && getGame().saveClickX <= Game.getFrameWidth() - 163
                        && getGame().saveClickY >= Game.getFrameHeight() - 37
                        && getGame().saveClickY < Game.getFrameHeight() - 0
                        && Game.tabInterfaceIDs[8] != -1) {
                    if (Game.currentTabId == 8) {
                        Game.showTabComponents = !Game.showTabComponents;
                    } else {
                        Game.showTabComponents = true;
                    }
                    Game.setTab(8, false);
                }
                if (getGame().saveClickX >= Game.getFrameWidth() - 162
                        && getGame().saveClickX <= Game.getFrameWidth() - 131
                        && getGame().saveClickY >= Game.getFrameHeight() - 37
                        && getGame().saveClickY < Game.getFrameHeight() - 0
                        && Game.tabInterfaceIDs[9] != -1) {
                    if (Game.currentTabId == 9) {
                        Game.showTabComponents = !Game.showTabComponents;
                    } else {
                        Game.showTabComponents = true;
                    }
                    Game.setTab(9, false);
                }
                if (getGame().saveClickX >= Game.getFrameWidth() - 129
                        && getGame().saveClickX <= Game.getFrameWidth() - 98
                        && getGame().saveClickY >= Game.getFrameHeight() - 37
                        && getGame().saveClickY < Game.getFrameHeight() - 0
                        && Game.tabInterfaceIDs[10] != -1) {
                    if (Game.currentTabId == 7) {
                        Game.showTabComponents = !Game.showTabComponents;
                    } else {
                        Game.showTabComponents = true;
                    }
                    Game.setTab(7, false);
                }
                if (getGame().saveClickX >= Game.getFrameWidth() - 97
                        && getGame().saveClickX <= Game.getFrameWidth() - 66
                        && getGame().saveClickY >= Game.getFrameHeight() - 37
                        && getGame().saveClickY < Game.getFrameHeight() - 0
                        && Game.tabInterfaceIDs[11] != -1) {
                    if (Game.currentTabId == 11) {
                        Game.showTabComponents = !Game.showTabComponents;
                    } else {
                        Game.showTabComponents = true;
                    }
                    Game.setTab(11, false);
                }
                if (getGame().saveClickX >= Game.getFrameWidth() - 65
                        && getGame().saveClickX <= Game.getFrameWidth() - 34
                        && getGame().saveClickY >= Game.getFrameHeight() - 37
                        && getGame().saveClickY < Game.getFrameHeight() - 0
                        && Game.tabInterfaceIDs[12] != -1) {
                    if (Game.currentTabId == 12) {
                        Game.showTabComponents = !Game.showTabComponents;
                    } else {
                        Game.showTabComponents = true;
                    }
                    Game.setTab(12, false);
                }
                if (getGame().saveClickX >= Game.getFrameWidth() - 33
                        && getGame().saveClickX <= Game.getFrameWidth()
                        && getGame().saveClickY >= Game.getFrameHeight() - 37
                        && getGame().saveClickY < Game.getFrameHeight() - 0
                        && Game.tabInterfaceIDs[13] != -1) {
                    if (Game.currentTabId == 13) {
                        Game.showTabComponents = !Game.showTabComponents;
                    } else {
                        Game.showTabComponents = true;
                    }
                    Game.setTab(13, false);
                }
            } else if (getGame().getInterfaceConfiguration(InterfaceConfiguration.SIDE_STONES_ARRANGEMENT) && Game.getFrameWidth() >= 1000) {
                if (getGame().mouseY >= Game.getFrameHeight() - 37
                        && getGame().mouseY <= Game.getFrameHeight()) {
                    if (getGame().mouseX >= Game.getFrameWidth() - 417
                            && getGame().mouseX <= Game.getFrameWidth() - 386) {
                        if (Game.currentTabId == 0) {
                            Game.showTabComponents = !Game.showTabComponents;
                        } else {
                            Game.showTabComponents = true;
                        }
                        Game.setTab(0, false);
                    }
                    if (getGame().mouseX >= Game.getFrameWidth() - 385
                            && getGame().mouseX <= Game.getFrameWidth() - 354) {
                        if (Game.currentTabId == 1) {
                            Game.showTabComponents = !Game.showTabComponents;
                        } else {
                            Game.showTabComponents = true;
                        }
                        Game.setTab(1, false);
                    }
                    if (getGame().mouseX >= Game.getFrameWidth() - 353
                            && getGame().mouseX <= Game.getFrameWidth() - 322) {
                        if (Game.currentTabId == 2) {
                            Game.showTabComponents = !Game.showTabComponents;
                        } else {
                            Game.showTabComponents = true;
                        }
                        Game.setTab(2, false);
                    }
                    if (getGame().mouseX >= Game.getFrameWidth() - 321
                            && getGame().mouseX <= Game.getFrameWidth() - 290) {
                        if (Game.currentTabId == 3) {
                            Game.showTabComponents = !Game.showTabComponents;
                        } else {
                            Game.showTabComponents = true;
                        }
                        Game.setTab(3, false);
                    }
                    if (getGame().mouseX >= Game.getFrameWidth() - 289
                            && getGame().mouseX <= Game.getFrameWidth() - 258) {
                        if (Game.currentTabId == 4) {
                            Game.showTabComponents = !Game.showTabComponents;
                        } else {
                            Game.showTabComponents = true;
                        }
                        Game.setTab(4, false);
                    }
                    if (getGame().mouseX >= Game.getFrameWidth() - 257
                            && getGame().mouseX <= Game.getFrameWidth() - 226) {
                        if (Game.currentTabId == 5) {
                            Game.showTabComponents = !Game.showTabComponents;
                        } else {
                            Game.showTabComponents = true;
                        }
                        Game.setTab(5, false);
                    }
                    if (getGame().mouseX >= Game.getFrameWidth() - 225
                            && getGame().mouseX <= Game.getFrameWidth() - 194) {
                        if (Game.currentTabId == 6) {
                            Game.showTabComponents = !Game.showTabComponents;
                        } else {
                            Game.showTabComponents = true;
                        }
                        Game.setTab(6, false);
                    }
                    if (getGame().mouseX >= Game.getFrameWidth() - 193
                            && getGame().mouseX <= Game.getFrameWidth() - 163) {
                        if (Game.currentTabId == 8) {
                            Game.showTabComponents = !Game.showTabComponents;
                        } else {
                            Game.showTabComponents = true;
                        }
                        Game.setTab(8, false);
                    }
                    if (getGame().mouseX >= Game.getFrameWidth() - 162
                            && getGame().mouseX <= Game.getFrameWidth() - 131) {
                        if (Game.currentTabId == 9) {
                            Game.showTabComponents = !Game.showTabComponents;
                        } else {
                            Game.showTabComponents = true;
                        }
                        Game.setTab(9, false);
                    }
                    if (getGame().saveClickX >= Game.getFrameWidth() - 129
                            && getGame().saveClickX <= Game.getFrameWidth() - 98
                            && getGame().saveClickY >= Game.getFrameHeight() - 37
                            && getGame().saveClickY < Game.getFrameHeight() - 0
                            && Game.tabInterfaceIDs[10] != -1) {
                        if (Game.currentTabId == 7) {
                            Game.showTabComponents = !Game.showTabComponents;
                        } else {
                            Game.showTabComponents = true;
                        }
                        Game.setTab(7, false);
                    }
                    if (getGame().mouseX >= Game.getFrameWidth() - 98
                            && getGame().mouseX <= Game.getFrameWidth() - 67) {
                        if (Game.currentTabId == 11) {
                            Game.showTabComponents = !Game.showTabComponents;
                        } else {
                            Game.showTabComponents = true;
                        }
                        Game.setTab(11, false);
                    }
                    if (getGame().mouseX >= Game.getFrameWidth() - 66
                            && getGame().mouseX <= Game.getFrameWidth() - 45) {
                        if (Game.currentTabId == 12) {
                            Game.showTabComponents = !Game.showTabComponents;
                        } else {
                            Game.showTabComponents = true;
                        }
                        Game.setTab(12, false);
                    }
                    if (getGame().mouseX >= Game.getFrameWidth() - 31
                            && getGame().mouseX <= Game.getFrameWidth()) {
                        if (Game.currentTabId == 13) {
                            Game.showTabComponents = !Game.showTabComponents;
                        } else {
                            Game.showTabComponents = true;
                        }
                        Game.setTab(13, false);
                    }
                }
            }
        }
    }

    @Override
    public void drawChatArea() {
        int yOffset = Game.isScreenMode(Game.ScreenMode.FIXED) ? 0 : Game.getFrameHeight() - 165;
        if (Game.isScreenMode(Game.ScreenMode.FIXED)) {
            getChatImageProducer().initDrawingArea();
        }
        Rasterizer.lineOffsets = Game.getAnIntArray1180();
        if (getGame().chatStateCheck()) {
            Game.showChatComponents = true;
            chatBack.drawSprite(0, yOffset);
        }
        if (Game.showChatComponents && !getGame().isMediaMode()) {
            if ((getGame().getInterfaceConfiguration(InterfaceConfiguration.TRANSPARENT_CHAT) && !Game.isScreenMode(Game.ScreenMode.FIXED)) && !getGame().chatStateCheck()) {
                Raster.drawLinePixels(7 + yOffset, 0x575757, 506, 7);
                Raster.drawAlphaGradient(7, 7 + yOffset, 506, 135, 0, 0xFFFFFF, 20);
            } else {
                chatBack.drawSprite(0, yOffset);
            }
        }
        if (!Game.showChatComponents || (getGame().getInterfaceConfiguration(InterfaceConfiguration.TRANSPARENT_CHAT) && !Game.isScreenMode(Game.ScreenMode.FIXED))) {
            Raster.drawAlphaPixels(7, Game.getFrameHeight() - 23, 506, 24, 0, 100);
        }
        getGame().drawChannelButtons(0, yOffset);
        if (getGame().isMessagePromptRaised()) {
            getGame().boldFont.drawCenteredString(getGame().getPromptMessage(), 259, 60 + yOffset, 0, -1);
            getGame().boldFont.drawCenteredString(getGame().getPromptInput() + "*", 259, 80 + yOffset, 128, -1, false);
        } else if (getGame().inputState == 1) {
            getGame().boldFont.drawCenteredString(getGame().getChatInputContext(), 259, yOffset + 60, 0, -1);
            getGame().boldFont.drawCenteredString(getGame().getAmountOrNameInput() + "*", 259, 80 + yOffset, 128, -1, false);
        } else if (getGame().inputState == 2) {
            getGame().boldFont.drawCenteredString(getGame().getChatInputContext(), 259, 60 + yOffset, 0, -1);
            getGame().boldFont.drawCenteredString(getGame().getAmountOrNameInput() + "*", 259, 80 + yOffset, 128, -1, false);
        } else if (getGame().inputState == 3) {
            getGame().getGrandExchange().displayItemSearch();
        } else if (getGame().getClickToContinueString() != null) {
            getGame().boldFont.drawCenteredString(getGame().getClickToContinueString(), 259, 60 + yOffset, 0, -1);
            getGame().boldFont.drawCenteredString("Click to continue", 259, 80 + yOffset, 128, -1);
        } else if (getGame().backDialogueId != -1) {
            getGame().getComponentDrawing().drawComponent(RSComponent.forId(getGame().backDialogueId), 20, 20 + yOffset, 0, 0);
        } else if (getGame().dialogueId != -1) {
            getGame().getComponentDrawing().drawComponent(RSComponent.forId(getGame().dialogueId), 20, 20 + yOffset, 0, 0);
        } else if (Game.showChatComponents && !getGame().isMediaMode()) {
            int drawingOffsetY = -3;
            int j = 0;
            int shadow = (getGame().getInterfaceConfiguration(InterfaceConfiguration.TRANSPARENT_CHAT) && !Game.isScreenMode(Game.ScreenMode.FIXED)) ? 0 : -1;
            Raster.setDrawingArea(122 + yOffset, 8, 497, 7 + yOffset);
            for (int k = 0; k < 500; k++) {
                if (getGame().getChatMessages()[k] != null) {
                    int yPos = (70 - drawingOffsetY * 14) + Game.chatScrollPosition + 5;
                    ChatMessageType chatMessageType = getGame().getChatMessages()[k].getChatMessageType();
                    if (chatMessageType != null) {
                        if (chatMessageType.canDraw(getGame().getChatMessages()[k], getGame())) {
                            chatMessageType.draw(getGame().getChatMessages()[k], getGame(), yPos);
                            j++;
                            drawingOffsetY++;
                        }
                    }
                }
            }
            Raster.defaultDrawingAreaSize();
            Game.chatScrollMax = j * 14 + 7 + 5;
            if (Game.chatScrollMax < 111) {
                Game.chatScrollMax = 111;
            }
            Scrollbar.draw(getGame(), 114, Game.chatScrollMax - Game.chatScrollPosition - 113, 7 + yOffset,
                    497, Game.chatScrollMax, (getGame().getInterfaceConfiguration(InterfaceConfiguration.TRANSPARENT_CHAT) && !Game.isScreenMode(Game.ScreenMode.FIXED)), false);
            String s;
            if (Game.getLocalPlayer() != null && Game.getLocalPlayer().name != null) {
                s = (Game.getLocalPlayer().isTitleSuffix() ? "" : Game.getLocalPlayer().getTitle()) +
                        Game.getLocalPlayer().name + (!Game.getLocalPlayer().isTitleSuffix() ? "" :
                        Game.getLocalPlayer().getTitle());
            } else {
                s = StringUtility.formatUsername(getGame().getLoginUsername());
            }
            Raster.setDrawingArea(140 + yOffset, 8, 509, 120 + yOffset);
            getGame().regularFont.drawBasicString(s + ":", 11, 133 + yOffset, (getGame().getInterfaceConfiguration(InterfaceConfiguration.TRANSPARENT_CHAT) && !Game.isScreenMode(Game.ScreenMode.FIXED)) ? 0xFFFFFF : 0, shadow);
            getGame().regularFont.drawBasicString(getGame().getInputString() + "*", 12 + getGame().regularFont.getTextWidth(s + ": "), 133 + yOffset,
                    (getGame().getInterfaceConfiguration(InterfaceConfiguration.TRANSPARENT_CHAT) && !Game.isScreenMode(Game.ScreenMode.FIXED)) ? 0x7FA9FF : 255, shadow, false);
            Raster.drawLinePixels(121 + yOffset, (getGame().getInterfaceConfiguration(InterfaceConfiguration.TRANSPARENT_CHAT) && !Game.isScreenMode(Game.ScreenMode.FIXED)) ?
                    0x575757 : 0x807660, 506, 7);
            Raster.defaultDrawingAreaSize();
        }
        if (getGame().isMenuOpen()) {
            getGame().drawMenu(0, Game.isScreenMode(Game.ScreenMode.FIXED) ? 338 : 0);
        }
        if (Game.isScreenMode(Game.ScreenMode.FIXED)) {
            getChatImageProducer().drawGraphics(338, getGame().graphics, 0);
        }
        getGameScreenImageProducer().initDrawingArea();
        Rasterizer.lineOffsets = Game.getAnIntArray1182();
    }

    @Override
    public void processHoverAndClicks() {
        int mouseX = getGame().mouseX;
        int mouseY = getGame().mouseY;
/**
 * Map area.
 */
        faceNorthClick = (getGame().mouseInRegion(Game.getFrameWidth() - (Game.isFixed() ? 242 : 174), (Game.isFixed() ? 6 : 0),
                Game.getFrameWidth() - (Game.isFixed() ? 203 : 130), (Game.isFixed() ? 42 : 37)));
        worldMapClick = (Game.isFixed() ?
                getGame().mouseInRegion(527, 126, 560, 159) :
                getGame().mouseInRegion(getOrbX(5), getOrbY(5), (Game.isFixed() ? Game.getFrameWidth() - 74
                        : getOrbX(5)) + 57, getOrbY(5) + 34));
        prayersClick = (getGame().mouseInRegion(Game.isFixed() ? Game.getFrameWidth() - 58 : getOrbX(1),
                getOrbY(1),
                (Game.isFixed() ? Game.getFrameWidth() - 58 : getOrbX(1)) + 57,
                getOrbY(1) + 34));
        runClick = (getGame().mouseInRegion(Game.isFixed() ? Game.getFrameWidth() - 58 : getOrbX(2), getOrbY(2),
                (Game.isFixed() ? Game.getFrameWidth() - 58 : getOrbX(2)) + 57, getOrbY(2) + 34));

        summoningHover = getGame().getInterfaceConfiguration(InterfaceConfiguration.HAS_FAMILIAR) &&
                (Game.isFixed() ? (mouseX >= 694 && mouseX <= 749 && mouseY >= 132 && mouseY < 164) :

                        (mouseX >= Game.getFrameWidth() - 71 && mouseX <= Game.getFrameWidth() - 14 && mouseY >= 139 && mouseY < 172));

        hitpointsClick = (getGame().mouseInRegion(Game.isFixed() ? 692 : getOrbX(0), getOrbY(0), (Game.isFixed() ? 748 : getOrbX(0) + 57), getOrbY(0) + 30));
        xpDropClick = (getGame().mouseInRegion(Game.getFrameWidth() - (Game.isFixed() ? 249 : 217),
                Game.isFixed() ? 46 : 12,
                Game.getFrameWidth() - (Game.isFixed() ? 249 : 217) + 34,
                (Game.isFixed() ? 46 : 12) + 28));
        logoutClick = (mouseX >= Game.getFrameWidth() - 26 && mouseX <= Game.getFrameWidth() - 1 && mouseY >= 2 && mouseY <= 24);
        if (faceNorthClick) {
            getGame().setMenuAction(new int[]{696}, "Face North");
        }
        if (worldMapClick) {
            getGame().setMenuAction(new int[]{697}, "Teleports");
        }
/**
 * Orb clicking.
 */
        if (getGame().getInterfaceConfiguration(InterfaceConfiguration.ORBS_ENABLED)) {
            if (prayersClick) {
                getGame().setMenuAction(new int[]{1506, 1500}, "Select quick-prayers",
                        (getGame().getInterfaceConfiguration(InterfaceConfiguration.QUICK_PRAYERS) ? "Turn quick-prayers off" : "Turn quick-prayers on"));
            }
            if (runClick) {
                getGame().setMenuAction(new int[]{1050}, "Toggle Run");
            }
            if (hitpointsClick && poisoned) {
                getGame().setMenuAction(new int[]{10002}, "Cure me"); // TODO "Cure me", or "Cure poison"?
            }
            if (xpDropClick) {
                getGame().setMenuAction(new int[]{1053, 1052, 1051},
                        "Reset <col=FF981F>XP drops",
                        (getGame().getInterfaceConfiguration(InterfaceConfiguration.XP_DROP_LOCK) ? "Unlock" : "Lock") + " <col=FF981F>XP drops",
                        getGame().getInterfaceConfiguration(InterfaceConfiguration.XP_DROPS) ? "Hide" : "Show"
                );
            }
            if (summoningHover) {
                int row = 4000;
                int menuActionRow = 1;
                for (String summoningOption : getGame().SUMMONING_OPTIONS) {
                    if (summoningOption == null || summoningOption.isEmpty()) {
                        row++;
                        continue;
                    }
                    getGame().menuActionName[menuActionRow] = summoningOption;
                    getGame().menuActionId[menuActionRow++] = row;
                    row++;
                }
                getGame().menuActionRow = menuActionRow;
            }
        }
        if (logoutClick) {
            getGame().setMenuAction(new int[]{700}, "Logout");
        }
/**
 * End map area.
 */
    }

    @Override
    public void processMainScreenClick() {
        if (getGame().minimapState != 0) {
            return;
        }
        if (getGame().clickMode3 == 1) {
            int clickX = getGame().saveClickX - 3 - (Game.isFixed() ? Game.getFrameWidth() - 214 : Game.getFrameWidth() - 163);
            int clickY = getGame().saveClickY - (Game.isFixed() ? 9 : 6);
// if (i >= 0 && j >= 0 && i < 152 && j < 152 && canClickMap()) {
            if (getGame().inCircle(0, 0, clickX, clickY, 76)) {
                clickX -= 73;
                clickY -= 75;
                int k = getGame().cameraHorizontal + getGame().minimapRotation & 0x7ff;
                int i1 = Rasterizer.SINE[k];
                int j1 = Rasterizer.COSINE[k];
                i1 = i1 * (getGame().minimapZoom + 256) >> 8;
                j1 = j1 * (getGame().minimapZoom + 256) >> 8;
                int k1 = clickY * i1 + clickX * j1 >> 11;
                int l1 = clickY * j1 - clickX * i1 >> 11;
                int i2 = Game.getLocalPlayer().x + k1 >> 7;
                int j2 = Game.getLocalPlayer().y - l1 >> 7;
                boolean flag1 = getGame().doWalkTo(1, 0, 0, 0, Game.getLocalPlayer().pathY[0], 0, 0,
                        j2, Game.getLocalPlayer().pathX[0], true, i2);
                if (flag1) {
                    getGame().getOutgoing().writeByte(clickX);
                    getGame().getOutgoing().writeByte(clickY);
                    getGame().getOutgoing().putShort(getGame().cameraHorizontal);
                    getGame().getOutgoing().writeByte(57);
                    getGame().getOutgoing().writeByte(getGame().minimapRotation);
                    getGame().getOutgoing().writeByte(getGame().minimapZoom);
                    getGame().getOutgoing().writeByte(89);
                    getGame().getOutgoing().putShort(Game.getLocalPlayer().x);
                    getGame().getOutgoing().putShort(Game.getLocalPlayer().y);
                    getGame().getOutgoing().writeByte(getGame().anInt1264);
                    getGame().getOutgoing().writeByte(63);
                }
            }
            Game.anInt1117++;
            if (Game.anInt1117 > 1151) {
                Game.anInt1117 = 0;
                getGame().getOutgoing().putOpcode(246);
                getGame().getOutgoing().writeByte(0);
                int l = getGame().getOutgoing().currentPosition;
                if ((int) (Math.random() * 2D) == 0) {
                    getGame().getOutgoing().writeByte(101);
                }
                getGame().getOutgoing().writeByte(197);
                getGame().getOutgoing().putShort((int) (Math.random() * 65536D));
                getGame().getOutgoing().writeByte((int) (Math.random() * 256D));
                getGame().getOutgoing().writeByte(67);
                getGame().getOutgoing().putShort(14214);
                if ((int) (Math.random() * 2D) == 0) {
                    getGame().getOutgoing().putShort(29487);
                }
                getGame().getOutgoing().putShort((int) (Math.random() * 65536D));
                if ((int) (Math.random() * 2D) == 0) {
                    getGame().getOutgoing().writeByte(220);
                }
                getGame().getOutgoing().writeByte(180);
                getGame().getOutgoing().writeBytes(getGame().getOutgoing().currentPosition - l);
            }
        }
    }

    @Override
    public void updateGameArea() {
        Game.antialiasingPixels = new int[Game.screenAreaWidth * Game.screenAreaHeight << 2];
        Rasterizer.setBounds(Game.screenAreaWidth << 1, Game.screenAreaHeight << 1);
        Game.antialiasingOffsets = Rasterizer.lineOffsets;
        Rasterizer.setBounds(Game.getFrameWidth(), Game.getFrameHeight());
        Game.fullScreenTextureArray = Rasterizer.lineOffsets;
        Rasterizer.setBounds(Game.isFixed() ?
                (getChatImageProducer() != null ? getChatImageProducer().canvasWidth : 519)
                : Game.getFrameWidth(), Game.isFixed() ? (getChatImageProducer() != null
                ? getChatImageProducer().canvasHeight : 165) : Game.getFrameHeight());
        Game.anIntArray1180 = Rasterizer.lineOffsets;
        Rasterizer.setBounds(Game.isFixed() ?
                (getTabImageProducer() != null ? getTabImageProducer().canvasWidth : 249)
                : Game.getFrameWidth(), Game.isFixed() ? (getTabImageProducer() != null
                ? getTabImageProducer().canvasHeight : 335) : Game.getFrameHeight());
        Game.anIntArray1181 = Rasterizer.lineOffsets;
        Rasterizer.setBounds(Game.screenAreaWidth, Game.screenAreaHeight);
        Game.anIntArray1182 = Rasterizer.lineOffsets;
        int ai[] = new int[9];
        for (int i8 = 0;
             i8 < 9;
             i8++) {
            int k8 = 128 + i8 * 32 + 15;
            int l8 = 600 + k8 * 3;
            int i9 = Rasterizer.SINE[k8];
            ai[i8] = l8 * i9 >> 16;
        }
        if (Game.isScreenMode(Game.ScreenMode.RESIZABLE) && (Game.getFrameWidth() >= 766) && (Game.getFrameWidth() <= 1025) && (Game.getFrameHeight() >= 504) && (Game.getFrameHeight() <= 850)) {
            SceneGraph.viewDistance = 9;
            Game.cameraZoom = 575;
        } else if (Game.isFixed()) {
            Game.cameraZoom = 600;
        } else if (!Game.isFixed()) {
            SceneGraph.viewDistance = 10;
            Game.cameraZoom = 600;
        }
        SceneGraph.setupViewport(500, 800, Game.screenAreaWidth, Game.screenAreaHeight, ai);
        if (Game.loggedIn) {
            setGameScreenImageProducer(new ImageProducer(Game.screenAreaWidth, Game.screenAreaHeight));
        } else {
// getGame().setTitleScreen(new ImageProducer(Game.getFrameWidth(), Game.getFrameHeight()));
        }
        Raster.width = Game.getFrameWidth();
        Raster.height = Game.getFrameHeight();
        Raster.pixels = new int[Game.getFrameWidth() * Game.getFrameHeight()];
    }

    @Override
    public boolean drawRightFrame() {
        return true;
    }

    @Override
    public boolean canClickArea() {
        if (getGame().mouseInRegion(Game.getFrameWidth() - (Game.getFrameWidth() <= 1000 ? 240 : 420), Game.getFrameHeight() - (Game.getFrameWidth() <= 1000 ? 90 : 37), Game.getFrameWidth(), Game.getFrameHeight())) {
            return false;
        }
        if ((Game.showChatComponents &&
                !getGame().getInterfaceConfiguration(InterfaceConfiguration.CLICK_THROUGH_CHAT)) ||
                getGame().backDialogueId != -1) {
            if (getGame().mouseX > 0 && getGame().mouseX < 519
                    && getGame().mouseY > Game.getFrameHeight() - 165
                    && getGame().mouseY < Game.getFrameHeight()
                    || getGame().mouseX > Game.getFrameWidth() - 220
                    && getGame().mouseX < Game.getFrameWidth() && getGame().mouseY > 0
                    && getGame().mouseY < 165) {
                return false;
            }
        }
        if (getGame().mouseInRegion(Game.getFrameWidth() - 216, 0, Game.getFrameWidth(), 172)) {
            return false;
        }
        if (!(getGame().getInterfaceConfiguration(InterfaceConfiguration.SIDE_STONES_ARRANGEMENT) && !Game.isFixed())) {
            return getGame().mouseX > 0 && getGame().mouseY > 0 && getGame().mouseY < Game.getFrameWidth() && getGame().mouseY < Game.getFrameHeight() && !(getGame().mouseX >= Game.getFrameWidth() - 242 && getGame().mouseY >= Game.getFrameHeight() - 335);
        }
        if (Game.showTabComponents) {
            if (Game.getFrameWidth() > 1000) {
                if (getGame().mouseX >= Game.getFrameWidth() - 420 && getGame().mouseX <= Game.getFrameWidth() && getGame().mouseY >= Game.getFrameHeight() - 37 && getGame().mouseY <= Game.getFrameHeight() || getGame().mouseX > Game.getFrameWidth() - 225 && getGame().mouseX < Game.getFrameWidth() && getGame().mouseY > Game.getFrameHeight() - 37 - 274 && getGame().mouseY < Game.getFrameHeight()) {
                    return false;
                }
            } else {
                if (getGame().mouseX >= Game.getFrameWidth() - 210 && getGame().mouseX <= Game.getFrameWidth() && getGame().mouseY >= Game.getFrameHeight() - 74 && getGame().mouseY <= Game.getFrameHeight() || getGame().mouseX > Game.getFrameWidth() - 225 && getGame().mouseX < Game.getFrameWidth() && getGame().mouseY > Game.getFrameHeight() - 74 - 274 && getGame().mouseY < Game.getFrameHeight()) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getOrbX(int orb) {
        switch (orb) {
            case 0:
                return !Game.isFixed() ? Game.getFrameWidth() - 212 : 172;
            case 1:
                return !Game.isFixed() ? Game.getFrameWidth() - 215 : 188;
            case 2:
                return !Game.isFixed() ? Game.getFrameWidth() - 203 : 188;
            case 3:
                return !Game.isFixed() ? Game.getFrameWidth() - 180 : 172;
            case 4:
                return 4;
            case 5:
                return !Game.isFixed() ? Game.getFrameWidth() - 50 : 7;
        }
        return 0;
    }

    public int getOrbY(int orb) {
        switch (orb) {
            case 0:
                return !Game.isFixed() ? 39 : 15;
            case 1:
                return !Game.isFixed() ? 73 : 54;
            case 2:
                return !Game.isFixed() ? 107 : 93;
            case 3:
                return !Game.isFixed() ? 141 : 128;
            case 4:
                return 120;
            case 5:
                return !Game.isFixed() ? 134 : 124;
        }
        return 0;
    }

    @Override
    public void drawXPTracker(int xOffset) {
        int dropId = getGame().getInterfaceConfiguration(InterfaceConfiguration.XP_DROPS) ? (xpDropClick ? 1 : 0) : (xpDropClick ? 3 : 2);
        xpDrop[dropId].drawSprite(Game.isFixed() ? -1 :
                Game.getFrameWidth() - 212, Game.isFixed() ? 46 : 19);
    }

    @Override
    public void drawXPTrackerFrame(int xOffset) {
        xpDropStatus.drawAdvancedSprite((391 + xOffset), 2);
        String xp = new DecimalFormat("#,###,##0").format(getGame().xpDropped);
        getGame().smallFont.drawBasicString(xp, (506 + xOffset) - getGame().smallFont.getTextWidth(xp), 20, 0xFFFFFF, 0);
        if (getGame().xpDrawn < 0) {
            getGame().xpDrawn = 0;
        }
        for (int index = 0;
             index < getGame().xpDrops.length;
             index++) {
            if (getGame().xpDrops[index] < 1) {
                continue;
            }
            if (getGame().floatingDrops[index] < 1) {
                getGame().floatingDrops[index] = getGame().xpDrops[index];
                getGame().xpDrawn += 1;
                continue;
            }
            getGame().xpDrops[index] = 0;
        }
        int iconBaseY = 238;
        int drawBaseY = 250;
// TODO Convert to class in future?
        for (int index = 0; index < getGame().floatingDrops.length; index++) {
            if (getGame().floatingDrops[index] < 1) {
                continue;
            }
            getGame().xpDrops[index] = 0;
            String dropped = new DecimalFormat("#,###,##0").format(getGame().floatingDrops[index]);
//            if (index == Skills.HUNTER) {
//                index = 21;
//            }
            Sprite sprite = getGame().skillIcons[index];
            if (sprite != null) {
                iconBaseY -= 24;
                sprite.drawSprite((487 + xOffset) - getGame().smallFont.getTextWidth(dropped), iconBaseY - getGame().xpDropPosition[index]);
            }
            drawBaseY -= 24;
            getGame().smallFont.drawBasicString(dropped, (508 + xOffset) - getGame().smallFont.getTextWidth(dropped), drawBaseY - getGame().xpDropPosition[index], 0xFFFFFF, 0);
            getGame().xpDrawing[index]++;
            if ((getGame().xpDrawing[index] % 3) == 0) {
                getGame().xpDropPosition[index] += 5;
            }
            if (getGame().xpDrawing[index] >= 70) {
                getGame().floatingDrops[index] = 0;
                getGame().xpDropPosition[index] = 0;
                getGame().xpDrawn -= 1;
                getGame().xpDrawing[index] = 0;
            }
        }
    }

    @Override
    public void determineMenuSize() {
        int boxLength = Game.INSTANCE.boldFont.getTextWidth("Choose option");
        for (int row = 0; row < Game.INSTANCE.menuActionRow; row++) {
            int actionLength = Game.INSTANCE.boldFont.getTextWidth(Game.INSTANCE.menuActionName[row]);
            if (actionLength > boxLength) {
                boxLength = actionLength;
            }
        }
        boxLength += 8;
        int offset = 15 * Game.INSTANCE.menuActionRow + 21;
        if (Game.INSTANCE.saveClickX > 0 && Game.INSTANCE.saveClickY > 0
                && Game.INSTANCE.saveClickX < Game.getFrameWidth()
                && Game.INSTANCE.saveClickY < Game.getFrameHeight()) {
            int xClick = Game.INSTANCE.saveClickX - boxLength / 2;
            if (xClick + boxLength > Game.getFrameWidth() - 4) {
                xClick = Game.getFrameWidth() - 4 - boxLength;
            }
            if (xClick < 0) {
                xClick = 0;
            }
            int yClick = Game.INSTANCE.saveClickY;
            if (yClick + offset > Game.getFrameHeight() - 6) {
                yClick = Game.getFrameHeight() - 6 - offset;
            }
            if (yClick < 0) {
                yClick = 0;
            }
            Game.INSTANCE.setMenuOpen(true);
            Game.INSTANCE.setMenuOffsetX(xClick);
            Game.INSTANCE.setMenuOffsetY(yClick);
            Game.INSTANCE.setMenuWidth(boxLength);
            Game.INSTANCE.setMenuHeight(15 * Game.INSTANCE.menuActionRow + 22);
        }
    }

    public void drawWorld() {
        if (!Game.isFixed()) {
            worldBack.drawSprite(getOrbX(5) + 5, getOrbY(5) + 5);
        }
        world.drawSprite((Game.isFixed()) ? 7 : getOrbX(5) + 9, (Game.isFixed()) ? 124 : getOrbY(5) + 9);
    }

    /**
     * Gets the sprite data.
     *
     * @return The data.
     */
    public int[][] getSpriteData() {
        // Static, coloured, sprite, level text, maximum level text, orb x, orb y, orb icon x, orb icon y,
        // full screen orb x, full screen orb y, full screen icon x, full screen icon y
        return new int[][]{
                /**
                 * Hitpoints
                 */
                {poisoned && hitpointsClick ? 8 : 7, 0, 9, 24635, 24659, 172, 15, 181, 26, 1, 8, 39, 41, 49},
                /**
                 * Prayers.
                 */
                {prayersClick ? 8 : 7, getGame().getInterfaceConfiguration(InterfaceConfiguration.QUICK_PRAYERS) ? 2 : 1, 10, 24646, 24670, 188, 54, 195, 61, 1, 5, 73, 35, 80},
                /**
                 * Run.
                 */
                {runClick ? 8 : 7, getGame().runToggled ? 4 : 3, (getGame().runToggled ? 12 : 11), 149, 149, 188, 93, 198, 100, 0, 12, 107, 45, 115},
                /**
                 * Summoning.
                 */
                {
                        summoningHover ? 8 : 7,
                        getGame().getInterfaceConfiguration(InterfaceConfiguration.HAS_FAMILIAR) ? 6 : 5,
                        13, 24657, 24681,
                        172, 131,
                        180, 140,
                        0,
                        30, 138,
                        61, 147, 1},
        };
    }

    /**
     * Loads all of the status orbs.
     *
     * @param xOffset The x offset.
     * @throws Exception Thrown at level parsing exceptions.
     */
    private void loadAllOrbs(int xOffset) throws Exception {
        int index = 0;
        for (int[] spriteData : getSpriteData()) {
            int level = StatusOrb.getLevel(RSComponent.forId(spriteData[3]));
            int maximumLevel = StatusOrb.getMaximumLevel(RSComponent.forId(spriteData[4]));
            int x = Game.isFixed() ? spriteData[5] : spriteData[10];
            int y = Game.isFixed() ? spriteData[6] : spriteData[11];
            /**
             * Draw orb holders.
             */
            orbs[(spriteData[0] + (Game.isFixed() ? 0 : 7))].drawSprite(x + xOffset, y);
            /**
             * Draw coloured orbs.
             */
            orbs[spriteData[1]].drawSprite(x + (Game.isFixed() ? 3 : 26) + xOffset, y + 4);
            /**
             * Draw the filling.
             */
            // TODO Sprite orbBack = this.orbBack; ?
            //Sprite orbBack = orbBack;
            orbBack.myHeight = StatusOrb.getFillPercentage(level, maximumLevel, 26);
            orbBack.drawSprite(x + (Game.isFixed() ? 3 : 26) + xOffset, y + 4);
            /**
             * Draw icons.
             */
            Sprite orbIcon = orbs[(index == 2 && getGame().getInterfaceConfiguration(InterfaceConfiguration.STAMINA_POTION)) ? 16 : spriteData[2]];
            if (spriteData[9] == 1) {
                orbIcon.drawARGBSprite(spriteData[Game.isFixed() ? 7 : 12] + xOffset, spriteData[Game.isFixed() ? 8 : 13],
                        (StatusOrb.isFlashing(level, maximumLevel)) ? getGame().spriteAlphaValues[1][0] : 255);
            } else {
                orbIcon.drawSprite(spriteData[Game.isFixed() ? 7 : 12] + xOffset, spriteData[Game.isFixed() ? 8 : 13]);
            }
            /**
             * Draw status text.
             */
            getGame().smallFont.drawCenteredString(String.valueOf(level), x + 42 - (Game.isFixed() ? 0 : 28) + xOffset,
                    y + 26, StatusOrb.getColour(level, maximumLevel), 0);
            index++;
        }
    }
}
