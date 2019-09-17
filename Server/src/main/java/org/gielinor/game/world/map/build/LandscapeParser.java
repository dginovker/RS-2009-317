package org.gielinor.game.world.map.build;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.Region;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.RegionPlane;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.utilities.buffer.ByteBufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * A utility class used for parsing landscapes.
 *
 * @author Emperor
 */
public final class LandscapeParser {

    private static final Logger log = LoggerFactory.getLogger(LandscapeParser.class);

    /**
     * Parses the landscape.
     *
     * @param region            The region.
     * @param mapscape     The mapscape data.
     * @param buffer       The buffer.
     * @param storeObjects If all objects should be stored (rather than just the objects with options).
     */
    public static void parse(Region region, byte[][][] mapscape, ByteBuffer buffer, boolean storeObjects) {
        int objectId = -1;
        for (; ; ) {
            int offset = ByteBufferUtils.getBigSmart(buffer);
            if (offset == 0) {
                break;
            }
            objectId += offset;
            int location = 0;
            for (; ; ) {
                offset = ByteBufferUtils.getSmart(buffer);
                if (offset == 0) {
                    break;
                }
                location += offset - 1;
                int y = location & 0x3f;
                int x = location >> 6 & 0x3f;
                int z = location >> 12;
                int configuration = buffer.get() & 0xFF;
                int type = configuration >> 2;
                int rotation = configuration & 0x3;
                region.setObjectCount(region.getObjectCount() + 1);
                if (x >= 0 && y >= 0 && x < 64 && y < 64) { // TODO 103 on client?
                    if ((mapscape[1][x][y] & 0x2) == 2) {
                        z--;
                    }
                    if (z >= 0 && z <= 3) {
                        GameObject gameObject = new GameObject(objectId, Location.create((region.getX() << 6) + x, (region.getY() << 6) + y, z), type, rotation);
                        flagGameObject(region.getPlanes()[z], x, y, gameObject, true, storeObjects);
                    }
                } else {
                    log.warn("Object out of bounds: [{}] at [{}, {}, {}].", objectId, x, y, z);
                }
            }
        }
    }

    /**
     * Adds a game object temporarily.
     *
     * @param object The object to add.
     */
    public static void addGameObject(GameObject object) {
        addGameObject(object, false);
    }

    /**
     * Adds a game object.
     *
     * @param object    The object to add.
     * @param landscape If the object should be added permanent.
     */
    public static void addGameObject(GameObject object, boolean landscape) {
        Location l = object.getLocation();
        flagGameObject(RegionManager.getRegionPlane(l), l.getLocalX(), l.getLocalY(), object, landscape, false);
    }

    static final ZoneBorders SEARCHING_AREA = new ZoneBorders(3264, 3712, 3332, 3845);

    /**
     * Flags a game object on the plane's clipping flags.
     *
     * @param plane        The plane.
     * @param object       The object.
     * @param landscape    If we are adding this game object permanent.
     * @param storeObjects If all objects should be stored (rather than just the objects with options).
     */
    public static void flagGameObject(RegionPlane plane, int localX, int localY, GameObject object, boolean landscape, boolean storeObjects) {
        Region.load(plane.getRegion());
        ObjectDefinition def = object.getDefinition();
        int sizeX;
        int sizeY;
        if (object.getRotation() % 2 == 0) {
            sizeX = def.sizeX;
            sizeY = def.sizeY;
        } else {
            sizeX = def.sizeY;
            sizeY = def.sizeX;
        }
        object.setActive(true);
        boolean add = def.getChildObject(null).hasActions() || !landscape;
        if (add) {
            addPlaneObject(plane, object, localX, localY, landscape);
        }
        int type = object.getType();
        if (type == 22) { //Tile
            plane.getFlags().getLandscape()[localX][localY] = true;
            if (def.isInteractive != 0 || def.clipType == 1 || def.secondBool) {
                if (def.clipType == 1) {
                    plane.getFlags().flagTileObject(localX, localY);
                    if (def.isProjectileClipped()) {
                        plane.getProjectileFlags().flagTileObject(localX, localY);
                    }
                }
            }
        } else if (type >= 9) { //Default objects
            if (def.clipType != 0) {
                plane.getFlags().flagSolidObject(localX, localY, sizeX, sizeY, def.projectileClipped);
                if (def.isProjectileClipped()) {
                    plane.getProjectileFlags().flagSolidObject(localX, localY, sizeX, sizeY, def.projectileClipped);
                }
            }
        } else if (type >= 0 && type <= 3) { //Doors/walls
            if (object.getLocation().inArea(SEARCHING_AREA)) { // TODO REMOVE
                //    DefaultCommand.dumpObject(object);
            }
            if (def.clipType != 0) {
                plane.getFlags().flagDoorObject(localX, localY, object.getRotation(), type, def.projectileClipped);
                if (def.isProjectileClipped()) {
                    plane.getProjectileFlags().flagDoorObject(localX, localY, object.getRotation(), type, def.projectileClipped);
                }
            }
        } else {
            return;
        }
        if (!add && (!def.getChildObject(null).getName().equals("null") || (object.getType() != 22 && storeObjects))) {
            addPlaneObject(plane, object, localX, localY, landscape);
        }
    }

    /**
     * Adds an object to the region plane.
     *
     * @param plane     The region plane.
     * @param object    The object to add.
     * @param localX    The local x-coordinate.
     * @param localY    The local y-coordinate.
     * @param landscape The landscape.
     */
    private static void addPlaneObject(RegionPlane plane, GameObject object, int localX, int localY, boolean landscape) {
        if (landscape) {
            GameObject current = plane.getObjects()[localX][localY];
            if (current != null && current.getDefinition().getChildObject(null).hasOptions(false)) {
                return;
            }
        }
        plane.add(object, localX, localY, landscape);
    }

    /**
     * Removes a game object.
     *
     * @param object The object.
     * @return The removed game object.
     */
    public static GameObject removeGameObject(GameObject object) {
        if (!object.isRenderable()) {
            return null;
        }
        RegionPlane plane = RegionManager.getRegionPlane(object.getLocation());
        Region.load(plane.getRegion());
        int localX = object.getLocation().getLocalX();
        int localY = object.getLocation().getLocalY();
        GameObject current = plane.getChunkObject(localX, localY);
        if (current == null || current.getId() != object.getId()) {
            // System.err.println(current + " is game object [x=" + localX + ", y=" + localY + "].");
            return null;
        }
        current.setActive(false);
        object.remove();
        object.setActive(false);
        plane.remove(localX, localY);
        ObjectDefinition def = object.getDefinition();
        int sizeX;
        int sizeY;
        if (object.getRotation() % 2 == 0) {
            sizeX = def.sizeX;
            sizeY = def.sizeY;
        } else {
            sizeX = def.sizeY;
            sizeY = def.sizeX;
        }
        int type = object.getType();
        if (type == 22) { //Tile
            if (def.isInteractive != 0 || def.clipType == 1 || def.secondBool) {
                if (def.clipType == 1) {
                    plane.getFlags().unflagTileObject(localX, localY);
                    if (def.isProjectileClipped()) {
                        plane.getProjectileFlags().unflagTileObject(localX, localY);
                    }
                }
            }
        } else if (type >= 9) { //Default objects
            if (def.clipType != 0) {
                plane.getFlags().unflagSolidObject(localX, localY, sizeX, sizeY, def.projectileClipped);
                if (def.isProjectileClipped()) {
                    plane.getProjectileFlags().unflagSolidObject(localX, localY, sizeX, sizeY, def.projectileClipped);
                }
            }
        } else if (type >= 0 && type <= 3) { //Doors/walls
            if (def.clipType != 0) {
                plane.getFlags().unflagDoorObject(localX, localY, object.getRotation(), type, def.projectileClipped);
                if (def.isProjectileClipped()) {
                    plane.getProjectileFlags().unflagDoorObject(localX, localY, object.getRotation(), type, def.projectileClipped);
                }
            }
        }
        return current;
    }
}