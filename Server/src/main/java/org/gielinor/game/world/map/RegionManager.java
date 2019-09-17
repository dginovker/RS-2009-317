package org.gielinor.game.world.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectGroup;
import org.gielinor.game.node.object.ObjectType;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.utilities.misc.RandomUtil;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * Manages the regions.
 *
 * @author Emperor
 *
 */
public final class RegionManager {

    /**
     * The total number of {@link Region}s that are cached when the game begins.
     */
    private static final int NUM_REGIONS = 1263;

    /**
     * The region cache mapping.
     */
    private static final Int2ObjectMap<Region> REGION_CACHE = new Int2ObjectOpenHashMap<>(NUM_REGIONS);

    /**
     * Removed game objects in the game.
     */
    public static final Int2ObjectMap<GameObject> REMOVED_OBJECTS = new Int2ObjectOpenHashMap<>();

    /**
     * Gets the region for the given region id.
     *
     * @param regionId
     *            The region id.
     * @return The region.
     */
    public static Region forId(int regionId) {
        Region region = REGION_CACHE.get(regionId);

        if (region == null) {
            region = new Region(regionId >> 8 & 0xFF, regionId & 0xFF);

            REGION_CACHE.put(regionId, region);
        }

        return region;
    }

    /**
     * Pulses the active regions.
     */
    public static void pulse() {
        REGION_CACHE.values().stream().filter(r -> r != null && r.isActive()).forEach(r -> {
            for (RegionPlane plane : r.getPlanes()) {
                plane.pulse();
            }
        });
    }

    /**
     * Gets the clipping flag on the given location.
     *
     * @param l
     *            The location.
     * @return The clipping flag.
     */
    public static int getClippingFlag(Location l) {
        return getClippingFlag(l.getZ(), l.getX(), l.getY());
    }

    /**
     * Gets the clipping flag.
     *
     * @param z
     *            The plane.
     * @param x
     *            The absolute x-coordinate.
     * @param y
     *            The absolute y-coordinate.
     * @return The clipping flags.
     */
    public static int getClippingFlag(int z, int x, int y) {
        Region region = forId((x >> 6) << 8 | y >> 6);

        Region.load(region);

        if (!region.isHasFlags()) {
            return -1;
        }

        x -= (x >> 6) << 6;
        y -= (y >> 6) << 6;

        return region.getPlanes()[z].getFlags().getClippingFlags()[x][y];
    }

    /**
     * Checks if the tile is part of the landscape.
     *
     * @param l
     *            The location.
     * @return <code>True</code> if so.
     */
    public static boolean isLandscape(Location l) {
        return isLandscape(l.getZ(), l.getX(), l.getY());
    }

    /**
     * Checks if the tile is part of the landscape.
     *
     * @param z
     *            The plane.
     * @param x
     *            The absolute x-coordinate.
     * @param y
     *            The absolute y-coordinate.
     * @return <code>True</code> if so.
     */
    public static boolean isLandscape(int z, int x, int y) {
        Region region = forId((x >> 6) << 8 | y >> 6);

        Region.load(region);

        if (true) { // TODO 317
            return true;
        }

        if (!region.isHasFlags() || region.getPlanes()[z].getFlags().getLandscape() == null) { // TODO
            // 317
            return false;
        }

        x -= (x >> 6) << 6;
        y -= (y >> 6) << 6;

        return region.getPlanes()[z].getFlags().getLandscape()[x][y];
    }

    /**
     * Sets the clipping flag on the given location.
     *
     * @param l
     *            The location.
     * @param flag
     *            The flag to set.
     */
    public static void setClippingFlag(Location l, int flag) {
        int x = l.getX();
        int y = l.getY();
        int z = l.getZ();

        Region region = forId((x >> 6) << 8 | y >> 6);

        Region.load(region);

        if (!region.isHasFlags()) {
            return;
        }

        x -= (x >> 6) << 6;
        y -= (y >> 6) << 6;

        region.getPlanes()[z].getFlags().getClippingFlags()[x][y] = flag;
    }

    /**
     * Adds a clipping flag.
     *
     * @param z
     *            The plane.
     * @param x
     *            The absolute x-coordinate.
     * @param y
     *            The absolute y-coordinate.
     * @param projectile
     *            If the flag is being set for projectile pathfinding.
     * @param flag
     *            The clipping flag.
     */
    public static void addClippingFlag(int z, int x, int y, boolean projectile, int flag) {
        Region region = forId((x >> 6) << 8 | y >> 6);

        Region.load(region);

        if (!region.isHasFlags()) {
            return;
        }

        x -= (x >> 6) << 6;
        y -= (y >> 6) << 6;

        if (projectile) {
            region.getPlanes()[z].getProjectileFlags().flag(x, y, flag);
        } else {
            region.getPlanes()[z].getFlags().flag(x, y, flag);
        }
    }

    /**
     * Adds a clipping flag.
     *
     * @param z
     *            The plane.
     * @param x
     *            The absolute x-coordinate.
     * @param y
     *            The absolute y-coordinate.
     * @param projectile
     *            If the flag is being set for projectile pathfinding.
     * @param flag
     *            The clipping flag.
     */
    public static void removeClippingFlag(int z, int x, int y, boolean projectile, int flag) {
        Region region = forId((x >> 6) << 8 | y >> 6);
        Region.load(region);
        if (!region.isHasFlags()) {
            return;
        }
        x -= (x >> 6) << 6;
        y -= (y >> 6) << 6;
        if (projectile) {
            region.getPlanes()[z].getProjectileFlags().unflag(x, y, flag);
        } else {
            region.getPlanes()[z].getFlags().unflag(x, y, flag);
        }
    }

    /**
     * Gets the clipping flag.
     *
     * @param z
     *            The plane.
     * @param x
     *            The absolute x-coordinate.
     * @param y
     *            The absolute y-coordinate.
     * @return The clipping flags.
     */
    public static int getProjectileFlag(int z, int x, int y) {
        Region region = forId((x >> 6) << 8 | y >> 6);
        Region.load(region);
        if (!region.isHasFlags()) {
            return -1;
        }
        x -= (x >> 6) << 6;
        y -= (y >> 6) << 6;
        return region.getPlanes()[z].getProjectileFlags().getClippingFlags()[x][y];
    }

    /**
     * Gets the clipping flag
     *
     * @param location
     *            the Location
     * @return the clipping flag
     */
    public static boolean isTeleportPermitted(Location location) {
        return isTeleportPermitted(location.getZ(), location.getX(), location.getY());
    }

    /**
     * Gets the clipping flag.
     *
     * @param z
     *            The plane.
     * @param x
     *            The absolute x-coordinate.
     * @param y
     *            The absolute y-coordinate.
     * @return The clipping flags.
     */
    public static boolean isTeleportPermitted(int z, int x, int y) {
        if (!isLandscape(z, x, y)) {
            return false;
        }
        int flag = getClippingFlag(z, x, y);
        return (flag & 0x12c0102) == 0 || (flag & 0x12c0108) == 0 || (flag & 0x12c0120) == 0 || (flag & 0x12c0180) == 0;
    }

    /**
     * Checks if the location has any clipping flags.
     *
     * @param location
     *            The location.
     * @return <code>True</code> if a clipping flag disables access for this
     *         location.
     */
    public static boolean isClipped(Location location) {
        return isClipped(location.getZ(), location.getX(), location.getY());
    }

    /**
     * Checks if the location has any clipping flags.
     *
     * @param z
     *            The plane.
     * @param x
     *            The x-coordinate.
     * @param y
     *            The y-coordinate.
     * @return <code>True</code> if a clipping flag disables access for this
     *         location.
     */
    public static boolean isClipped(int z, int x, int y) {
        if (!isLandscape(z, x, y)) {
            return true;
        }
        int flag = getClippingFlag(z, x, y);
        return (flag & 0x12c0102) != 0 || (flag & 0x12c0108) != 0 || (flag & 0x12c0120) != 0 || (flag & 0x12c0180) != 0;
    }

    /**
     * Gets the game object on the current location.
     *
     * @param l
     *            The location.
     * @return The game object, or {@code null} if no object was found.
     */
    public static GameObject getObject(Location l) {
        return getObject(l.getZ(), l.getX(), l.getY());
    }

    /**
     * Gets the game object on the current location.
     *
     * @param l
     *            The location.
     * @return The game object, or {@code null} if no object was found.
     */
    public static GameObject getObject(Location l, ObjectGroup objectGroup) {
        int x = l.getX();
        int y = l.getY();
        int z = l.getZ();
        int regionId = ((x >> 6) << 8) | y >> 6;
        x -= ((x >> 6) << 6);
        y -= ((y >> 6) << 6);
        Region region = forId(regionId);
        Region.load(region);
        for (GameObject[] objects : region.getPlanes()[z].getObjects()) {
            for (GameObject object : objects) {
                if (object == null) {
                    continue;
                }
                if (object.getLocation().equals(x, y, z)
                    && ObjectType.forId(object.getType()).getObjectGroup().getId() == objectGroup.getId()) {
                    return object;
                }
            }
        }
        return null;
    }

    /**
     * Gets the game object on the current location, and id.
     *
     * @param id
     *            The id of the object.
     * @param l
     *            The location.
     * @return The game object, or {@code null} if no object was found.
     */
    public static GameObject getObject(int id, Location l) {
        return getObject(id, l.getZ(), l.getX(), l.getY());
    }

    /**
     * Gets the game object on the current absolute coordinates, and the objects
     * id.
     *
     * @param id
     *            The id of the object.
     * @param z
     *            The height.
     * @param x
     *            The x-coordinate.
     * @param y
     *            The y-coordinate.
     * @return The game object, or {@code null} if no object was found.
     */
    public static GameObject getObject(int id, int z, int x, int y) {
        int regionId = ((x >> 6) << 8) | y >> 6;
        x -= ((x >> 6) << 6);
        y -= ((y >> 6) << 6);
        Region region = forId(regionId);
        Region.load(region);
        for (GameObject[] objects : region.getPlanes()[z].getObjects()) {
            for (GameObject object : objects) {
                if (object == null) {
                    continue;
                }
                if (object.getId() == id) {
                    return object;
                }
            }
        }
        return null;
    }

    /**
     * Gets the game object on the current absolute coordinates, and the objects
     * id.
     *
     * @param id
     *            The id of the object.
     * @param z
     *            The height.
     * @param x
     *            The x-coordinate.
     * @param y
     *            The y-coordinate.
     * @return The game object, or {@code null} if no object was found.
     */
    public static GameObject getObject(int id, int z, int x, int y, int rotation) {
        int regionId = ((x >> 6) << 8) | y >> 6;
        x -= ((x >> 6) << 6);
        y -= ((y >> 6) << 6);
        Region region = forId(regionId);
        Region.load(region);
        for (GameObject[] objects : region.getPlanes()[z].getObjects()) {
            for (GameObject object : objects) {
                if (object == null) {
                    continue;
                }
                if (object.getId() == id && object.getRotation() == rotation) {
                    return object;
                }
            }
        }
        return null;
    }

    /**
     * Gets the game object on the current absolute coordinates, and the objects
     * id.
     *
     * @param id
     *            The id of the object.
     * @param z
     *            The height.
     * @param x
     *            The x-coordinate.
     * @param y
     *            The y-coordinate.
     * @return The game object, or {@code null} if no object was found.
     */
    public static GameObject getObject(int id, int z, int x, int y, int rotation, int type) {
        int regionId = ((x >> 6) << 8) | y >> 6;
        x -= ((x >> 6) << 6);
        y -= ((y >> 6) << 6);
        Region region = forId(regionId);
        Region.load(region);
        for (GameObject[] objects : region.getPlanes()[z].getObjects()) {
            for (GameObject object : objects) {
                if (object == null) {
                    continue;
                }
                if (object.getId() == id && object.getRotation() == rotation && object.getType() == type) {
                    return object;
                }
            }
        }
        return null;
    }

    /**
     * Gets the game object on the current absolute coordinates.
     *
     * @param z
     *            The height.
     * @param x
     *            The x-coordinate.
     * @param y
     *            The y-coordinate.
     * @return The game object, or {@code null} if no object was found.
     */
    public static GameObject getObject(int z, int x, int y) {
        int regionId = ((x >> 6) << 8) | y >> 6;
        x -= ((x >> 6) << 6);
        y -= ((y >> 6) << 6);
        Region region = forId(regionId);
        Region.load(region);
        GameObject object = region.getPlanes()[z].getChunkObject(x, y);
        if (object != null && !object.isRenderable()) {
            return null;
        }
        return object;
    }

    /**
     * Gets the game object on the current absolute coordinates.
     *
     * @param z
     *            The height.
     * @param x
     *            The x-coordinate.
     * @param y
     *            The y-coordinate.
     * @return The game object, or {@code null} if no object was found.
     */
    public static GameObject getDeleteObject(int x, int y, int z) {
        int regionId = ((x >> 6) << 8) | y >> 6;
        x -= ((x >> 6) << 6);
        y -= ((y >> 6) << 6);
        Region region = forId(regionId);
        Region.load(region);
        GameObject object = region.getPlanes()[z].getChunkObject(x, y);
        if (object != null && !object.isRenderable()) {
            return null;
        }
        if (object != null && object.getLocation().getX() == x && object.getLocation().getY() == y
            && object.getLocation().getZ() == z) {
            return object;
        }
        return object;
    }

    /**
     * Gets the surrounding objects.
     *
     * @param node
     *            The node the players should be surrounding.
     * @param id
     *            The id of the objects to look for.
     * @param maximum
     *            The maximum distance from the node.
     * @return The list of players.
     */
    public static List<GameObject> getSurroundingObjects(Node node, int id, int maximum) {
        List<GameObject> gameObjects = new ArrayList<>();

        int x = node.getLocation().getX();
        int y = node.getLocation().getY();
        int regionId = ((x >> 6) << 8) | y >> 6;
        x -= ((x >> 6) << 6);
        y -= ((y >> 6) << 6);
        Region region = forId(regionId);
        Region.load(region);

        for (GameObject[] objects : region.getPlanes()[node.getLocation().getZ()].getObjects()) {
            for (GameObject object : objects) {
                if (object == null) {
                    continue;
                }
                if (object.getId() == id && object.getLocation().getDistance(node.getLocation()) <= maximum) {
                    gameObjects.add(object);
                }
            }
        }
        return gameObjects;
    }

    /**
     * Gets the surrounding objects by name.
     *
     * @param node
     *            The node the players should be surrounding.
     * @param name
     *            The name of the objects to look for.
     * @param maximum
     *            The maximum distance from the node.
     * @return The list of players.
     */
    public static List<GameObject> getSurroundingObjects(Node node, String name, int maximum) {
        List<GameObject> gameObjects = new ArrayList<>();

        int x = node.getLocation().getX();
        int y = node.getLocation().getY();
        int regionId = ((x >> 6) << 8) | y >> 6;
        x -= ((x >> 6) << 6);
        y -= ((y >> 6) << 6);
        Region region = forId(regionId);
        Region.load(region);

        for (GameObject[] objects : region.getPlanes()[node.getLocation().getZ()].getObjects()) {
            for (GameObject object : objects) {
                if (object == null) {
                    continue;
                }
                if (object.getDefinition().getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())
                    && object.getLocation().getDistance(node.getLocation()) <= maximum) {
                    gameObjects.add(object);
                }
            }
        }
        return gameObjects;
    }

    /**
     * Gets the surrounding objects by name and one of the options.
     *
     * @param node
     *            The node the players should be surrounding.
     * @param name
     *            The name of the objects to look for.
     * @param option
     *            The option it should contain.
     * @param maximum
     *            The maximum distance from the node.
     * @return The list of players.
     */
    public static List<GameObject> getSurroundingObjects(Node node, String name, String option, int maximum) {
        List<GameObject> gameObjects = new ArrayList<>();

        int x = node.getLocation().getX();
        int y = node.getLocation().getY();
        int regionId = ((x >> 6) << 8) | y >> 6;
        x -= ((x >> 6) << 6);
        y -= ((y >> 6) << 6);
        Region region = forId(regionId);
        Region.load(region);

        for (GameObject[] objects : region.getPlanes()[node.getLocation().getZ()].getObjects()) {
            for (GameObject object : objects) {
                if (object == null) {
                    continue;
                }
                if (!object.getDefinition().hasAction(option.toLowerCase())) {
                    continue;
                }
                if (object.getDefinition().getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())
                    && object.getLocation().getDistance(node.getLocation()) <= maximum) {
                    gameObjects.add(object);
                }
            }
        }
        return gameObjects;
    }

    /**
     * Gets the spawn location of a node.
     *
     * @param owner
     *            the owner.
     * @param node
     *            the node.
     * @return the location.
     */
    public static Location getSpawnLocation(Player owner, Node node) {
        if (owner == null || node == null) {
            return null;
        }
        Location destination = null;
        for (int i = 0; i < 4; i++) {
            Direction dir = Direction.get(i);
            Location l = owner.getLocation().transform(dir, dir.toInteger() < 2 ? 1 : node.size());
            boolean success = true;
            loop:
            {
                for (int x = 0; x < node.size(); x++) {
                    for (int y = 0; y < node.size(); y++) {
                        if (RegionManager.isClipped(l.transform(x, y, 0))) {
                            success = false;
                            break loop;
                        }
                    }
                }
            }
            if (success) {
                destination = l;
                break;
            }
        }
        return destination;
    }

    /**
     * Gets a list of players in the given region.
     *
     * @param regionId
     *            The region id.
     * @return The list of players in this region.
     */
    public static List<Player> getRegionPlayers(int regionId) {
        Region r = forId(regionId);
        List<Player> players = new ArrayList<>();
        for (RegionPlane plane : r.getPlanes()) {
            players.addAll(plane.getPlayers());
        }
        return players;
    }

    /**
     * Gets the region plane for this location.
     *
     * @param l
     *            The location.
     * @return The region plane.
     */
    public static RegionPlane getRegionPlane(Location l) {
        int regionId = ((l.getX() >> 6) << 8) | l.getY() >> 6;
        return forId(regionId).getPlanes()[l.getZ()];
    }

    /**
     * Gets a region chunk.
     *
     * @param l
     *            The location.
     * @return The region chunk.
     */
    public static RegionChunk getRegionChunk(Location l) {
        RegionPlane plane = getRegionPlane(l);
        return plane.getRegionChunk(l.getLocalX() / RegionChunk.SIZE, l.getLocalY() / RegionChunk.SIZE);
    }

    /**
     * Moves the entity from the current region to the new one.
     *
     * @param entity
     *            The entity.
     */
    public static void move(Entity entity) {
        boolean player = entity instanceof Player;
        int regionId = ((entity.getLocation().getRegionX() + 6) >> 3) << 8
            | ((entity.getLocation().getRegionY() + 6) >> 3);
        Viewport viewport = entity.getViewport();
        Region current = forId(regionId);
        int z = entity.getLocation().getZ();
        RegionPlane plane = current.getPlanes()[z];
        viewport.updateViewport(entity);
        if (plane == viewport.getCurrentPlane()) {
            Location lastLocation = entity.getWalkingQueue().getFootPrint();
            // Fixes https://github.com/Vitality186/GielinorGS/issues/261
            if (lastLocation != null) {
                entity.getZoneMonitor().updateLocation(entity.getWalkingQueue().getFootPrint());
            }
            return;
        }
        viewport.remove(entity);
        if (player) {
            current.add((Player) entity);
        } else {
            current.add((NPC) entity);
        }
        viewport.setRegion(current);
        viewport.setCurrentPlane(plane);
        List<RegionPlane> view = new LinkedList<>();
        for (int regionX = (entity.getLocation().getRegionX() >> 3)
            - 1; regionX <= (entity.getLocation().getRegionX() >> 3) + 1; regionX++) {
            for (int regionY = (entity.getLocation().getRegionY() >> 3)
                - 1; regionY <= (entity.getLocation().getRegionY() >> 3) + 1; regionY++) {
                if (regionX < 0 || regionY < 0) {
                    continue;
                }
                Region region = forId(regionX << 8 | regionY);
                RegionPlane p = region.getPlanes()[z];
                if (player) {
                    region.flagActive();
                }
                view.add(p);
            }
        }
        viewport.setViewingPlanes(view);
        Location lastLocation = entity.getWalkingQueue().getFootPrint();
        // Fixes https://github.com/Vitality186/GielinorGS/issues/261
        if (lastLocation != null) {
            entity.getZoneMonitor().updateLocation(lastLocation);
        }
    }

    /**
     * Gets the list of local NPCs with a maximum distance of 16.
     *
     * @param n
     *            The entity.
     * @return The list of local NPCs.
     */
    public static List<NPC> getLocalNpcs(Entity n) {
        return getLocalNpcs(n, MapDistance.RENDERING.getDistance());
    }

    /**
     * Gets the location entitys.
     *
     * @param location
     *            the location.
     * @param distance
     *            the distance.
     * @return the list.
     */
    public static List<Entity> getLocalEntitys(Location location, int distance) {
        List<Entity> entitys = new ArrayList<>();
        entitys.addAll(getLocalNpcs(location, distance));
        entitys.addAll(getLocalPlayers(location, distance));
        return entitys;
    }

    /**
     * Gets the location entitys.
     *
     * @param entity
     *            the entity.
     * @param distance
     *            the distance.
     * @return the list.
     */
    public static List<Entity> getLocalEntitys(Entity entity, int distance) {
        return getLocalEntitys(entity.getLocation(), distance);
    }

    /**
     * Gets the local entitys.
     *
     * @param entity
     *            the entity.
     * @return the entitys.
     */
    public static List<Entity> getLocalEntitys(Entity entity) {
        return getLocalEntitys(entity.getLocation(), MapDistance.RENDERING.getDistance());
    }

    /**
     * Gets the list of local NPCs.
     *
     * @param n
     *            The entity.
     * @param distance
     *            The distance to the entity.
     * @return The list of local NPCs.
     */
    public static List<NPC> getLocalNpcs(Entity n, int distance) {
        List<NPC> npcs = new LinkedList<>();
        for (RegionPlane r : n.getViewport().getViewingPlanes()) {
            for (NPC npc : r.getNpcs()) {
                if (npc.getLocation().withinDistance(n.getLocation(), distance)) {
                    npcs.add(npc);
                }
            }
        }
        return npcs;
    }

    /**
     * Gets the list of local players with a maximum distance of 15.
     *
     * @param n
     *            The entity.
     * @return The list of local players.
     */
    public static List<Player> getLocalPlayers(Entity n) {
        return getLocalPlayers(n, MapDistance.RENDERING.getDistance());
    }

    /**
     * Gets the list of local players.
     *
     * @param n
     *            The entity.
     * @param distance
     *            The distance to the entity.
     * @return The list of local players.
     */
    public static List<Player> getLocalPlayers(Entity n, int distance) {
        List<Player> players = new LinkedList<>();
        for (RegionPlane r : n.getViewport().getViewingPlanes()) {
            for (Player p : r.getPlayers()) {
                if (p.getLocation().withinDistance(n.getLocation(), distance)) {
                    players.add(p);
                }
            }
        }
        return players;
    }

    /**
     * Gets the surrounding players.
     *
     * @param n
     *            The node the players should be surrounding.
     * @param ignore
     *            The nodes not to add to the list.
     * @return The list of players.
     */
    public static List<Player> getSurroundingPlayers(Node n, Node... ignore) {
        return getSurroundingPlayers(n, 9, ignore);
    }

    /**
     * Gets the surrounding players.
     *
     * @param n
     *            The node the players should be surrounding.
     * @param ignore
     *            The nodes not to add to the list.
     * @return The list of players.
     */
    public static List<Player> getSurroundingPlayers(Node n, int maximum, Node... ignore) {
        List<Player> players = getLocalPlayers(n.getLocation(), 1);
        int count = 0;
        for (Iterator<Player> it = players.iterator(); it.hasNext(); ) {
            Player p = it.next();
            if (++count >= maximum) {
                it.remove();
                continue;
            }
            for (Node node : ignore) {
                if (p == node) {
                    count--;
                    it.remove();
                    break;
                }
            }
        }
        return players;
    }

    /**
     * Gets the surrounding players.
     *
     * @param n
     *            The node the players should be surrounding.
     * @param ignore
     *            The nodes not to add to the list.
     * @return The list of players.
     */
    public static List<NPC> getSurroundingNPCs(Node n, Node... ignore) {
        return getSurroundingNPCs(n, 9, ignore);
    }

    /**
     * Gets the surrounding players.
     *
     * @param n
     *            The node the npcs should be surrounding.
     * @param ignore
     *            The nodes not to add to the list.
     * @return The list of npcs.
     */
    public static List<NPC> getSurroundingNPCs(Node n, int maximum, Node... ignore) {
        List<NPC> npcs = getLocalNpcs(n.getLocation(), 1);
        int count = 0;
        for (Iterator<NPC> it = npcs.iterator(); it.hasNext(); ) {
            NPC p = it.next();
            if (++count > maximum) {
                it.remove();
                continue;
            }
            for (Node node : ignore) {
                if (p == node) {
                    count--;
                    it.remove();
                    break;
                }
            }
        }
        return npcs;
    }

    /**
     * Gets a random teleport location in the radius around the given location.
     *
     * @param location
     *            The centre location.
     * @param radius
     *            The radius.
     * @return A random teleport location.
     */
    public static Location getTeleportLocation(Location location, int radius) {
        Location destination = location;
        int x = -radius + RandomUtil.random(1 + (radius << 1));
        int y = -radius + RandomUtil.random(1 + (radius << 1));
        int count = 0;
        while (!isTeleportPermitted(destination = location.transform(x, y, 0))) {
            x = -radius + RandomUtil.random(1 + (radius << 1));
            y = -radius + RandomUtil.random(1 + (radius << 1));
            if (count++ == radius * 2) {
                // This would be able to keep looping for several seconds
                // otherwise (this actually happens).
                for (x = -radius; x < radius + 1; x++) {
                    for (y = -radius; y < radius + 1; y++) {
                        if (isTeleportPermitted(destination = location.transform(x, y, 0))) {
                            return destination;
                        }
                    }
                }
                break;
            }
        }
        return destination;
    }

    /**
     * Gets a random teleport location in the radius around the given location.
     *
     * @param location
     *            The centre location.
     * @return A random teleport location.
     */
    public static Location getTeleportLocation(Location location, int areaX, int areaY) {
        Location destination = location;
        int x = RandomUtil.random(1 + areaX);
        int y = RandomUtil.random(1 + areaY);
        int count = 0;
        while (!isTeleportPermitted(destination = location.transform(x, y, 0))) {
            x = RandomUtil.random(1 + areaX);
            y = RandomUtil.random(1 + areaY);
            if (count++ >= areaX * 2) {
                // This would be able to keep looping for several seconds
                // otherwise (this actually happens).
                for (x = 0; x < areaX + 1; x++) {
                    for (y = 0; y < areaY + 1; y++) {
                        if (isTeleportPermitted(destination = location.transform(x, y, 0))) {
                            return destination;
                        }
                    }
                }
                break;
            }
        }
        return destination;
    }

    /**
     * Gets the current viewport for the location.
     *
     * @param l
     *            The location.
     * @return The viewport.
     */
    public static List<Player> getViewportPlayers(Location l) {
        List<Player> players = new LinkedList<>();
        l = l.getChunkBase().transform(-8, -8, 0);
        ZoneBorders b = new ZoneBorders(l.getX(), l.getY(), l.getX() + 24, l.getY() + 24);
        for (int regionX = (l.getRegionX() - 6) >> 3; regionX <= (l.getRegionX() + 6) >> 3; regionX++) {
            for (int regionY = (l.getRegionY() - 6) >> 3; regionY <= (l.getRegionY() + 6) >> 3; regionY++) {
                for (Player player : forId(regionX << 8 | regionY).getPlanes()[l.getZ()].getPlayers()) {
                    l = player.getLocation();
                    if (b.insideBorder(l.getX(), l.getY())) {
                        players.add(player);
                    }
                }
            }
        }
        return players;
    }

    /**
     * Gets a list of local players within rendering distance of the location.
     *
     * @param l
     *            The location.
     * @return The list of players.
     */
    public static List<Player> getLocalPlayers(Location l) {
        return getLocalPlayers(l, MapDistance.RENDERING.getDistance());
    }

    /**
     * Gets a list of local players.
     *
     * @param l
     *            The location.
     * @param distance
     *            The distance to that location.
     * @return The list of players.
     */
    public static List<Player> getLocalPlayers(Location l, int distance) {
        List<Player> players = new LinkedList<>();
        for (int regionX = (l.getRegionX() - 6) >> 3; regionX <= (l.getRegionX() + 6) >> 3; regionX++) {
            for (int regionY = (l.getRegionY() - 6) >> 3; regionY <= (l.getRegionY() + 6) >> 3; regionY++) {
                for (Player player : forId(regionX << 8 | regionY).getPlanes()[l.getZ()].getPlayers()) {
                    if (player.getLocation().withinDistance(l, distance)) {
                        players.add(player);
                    }
                }
            }
        }
        return players;
    }

    /**
     * Gets a list of local players within 16 tile distance of the location.
     *
     * @param l
     *            The location.
     * @return The list of players.
     */
    public static List<NPC> getLocalNpcs(Location l) {
        return getLocalNpcs(l, MapDistance.RENDERING.getDistance());
    }

    /**
     * Gets the npc.
     *
     * @param entity
     *            the entity.
     * @param id
     *            the id.
     */
    public static NPC getNpc(final Entity entity, final int id) {
        return getNpc(entity, id, 16);
    }

    /**
     * Gets an npc near the entity.
     *
     * @param entity
     *            the entity.
     * @param id
     *            the id,
     * @param distance
     *            the dinstance.
     * @return the npc.
     */
    public static NPC getNpc(final Entity entity, int id, int distance) {
        List<NPC> npcs = getLocalNpcs(entity.getLocation(), distance);
        for (NPC n : npcs) {
            if (n.getId() == id) {
                return n;
            }
        }
        return null;
    }

    /**
     * Gets an npc near the entity.
     *
     * @param location
     *            The location.
     * @param id
     *            The id,
     * @param distance
     *            The dinstance.
     * @return The npc.
     */
    public static NPC getNpc(final Location location, int id, int distance) {
        List<NPC> npcs = getLocalNpcs(location, distance);
        for (NPC n : npcs) {
            if (n.getId() == id) {
                return n;
            }
        }
        return null;
    }

    /**
     * Gets a list of local npcs.
     *
     * @param l
     *            The location.
     * @param distance
     *            The distance to that location.
     * @return The list of players.
     */
    public static List<NPC> getLocalNpcs(Location l, int distance) {
        List<NPC> npcs = new LinkedList<>();
        for (int regionX = (l.getRegionX() - 6) >> 3; regionX <= (l.getRegionX() + 6) >> 3; regionX++) {
            for (int regionY = (l.getRegionY() - 6) >> 3; regionY <= (l.getRegionY() + 6) >> 3; regionY++) {
                for (NPC n : forId(regionX << 8 | regionY).getPlanes()[l.getZ()].getNpcs()) {
                    if (n.getLocation().withinDistance(l, (n.size() >> 1) + distance)) {
                        npcs.add(n);
                    }
                }
            }
        }
        return npcs;
    }

    /**
     * Gets a list of local npcs by id.
     *
     * @param l
     *            The location.
     * @param distance
     *            The distance to that location.
     * @return The list of players.
     */
    public static List<NPC> getLocalNpcs(Location l, int distance, int npcId) {
        List<NPC> npcs = new LinkedList<>();
        for (int regionX = (l.getRegionX() - 6) >> 3; regionX <= (l.getRegionX() + 6) >> 3; regionX++) {
            for (int regionY = (l.getRegionY() - 6) >> 3; regionY <= (l.getRegionY() + 6) >> 3; regionY++) {
                for (NPC n : forId(regionX << 8 | regionY).getPlanes()[l.getZ()].getNpcs()) {
                    if (n.getId() == npcId && n.getLocation().withinDistance(l, (n.size() >> 1) + distance)) {
                        npcs.add(n);
                    }
                }
            }
        }
        return npcs;
    }

    /**
     * Gets the regionCache.
     *
     * @return The regionCache.
     */
    public static Map<Integer, Region> getRegionCache() {
        return REGION_CACHE;
    }

}
