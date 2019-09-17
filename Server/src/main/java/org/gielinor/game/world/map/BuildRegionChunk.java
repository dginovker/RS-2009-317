package org.gielinor.game.world.map;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.Constructed;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.build.LandscapeParser;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.out.ClearObject;
import org.gielinor.net.packet.out.ConstructGroundItem;
import org.gielinor.net.packet.out.ConstructObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A region chunk, used for easely modifying objects.
 *
 * @author Emperor
 */
public final class BuildRegionChunk extends RegionChunk {

    private static final Logger log = LoggerFactory.getLogger(BuildRegionChunk.class);

    /**
     * The list of changes made.
     */
    private final GameObject[][][] objects;

    /**
     * Constructs a new {@code BuildRegionChunk} {@code Object}
     *
     * @param base     The base location.
     * @param rotation The rotation.
     * @param plane    The region plane.
     */
    public BuildRegionChunk(Location base, int rotation, RegionPlane plane) {
        super(base, rotation, plane);
        this.objects = new GameObject[3][8][8];
        this.objects[0] = super.objects;
    }

    @Override
    protected boolean appendUpdate(Player player, PacketBuilder packetBuilder) {
        boolean updated = false;//super.appendUpdate(player, buffer);
        for (int i = 0; i < objects.length; i++) {
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    GameObject o = objects[i][x][y];
                    if (o instanceof Constructed) {
                        ConstructObject.write(packetBuilder, o);
                    } else if (o != null && !o.isRenderable()) {
                        ClearObject.write(packetBuilder, o);
                    } else {
                        continue;
                    }
                    updated = true;
                }
            }
        }
        if (items != null) {
            for (Item item : items) {
                if (item != null && item.isActive() && item.getLocation() != null) {
                    GroundItem g = (GroundItem) item;
                    if (!g.isPrivate() || g.droppedBy(player)) {
                        ConstructGroundItem.write(packetBuilder, item);
                        updated = true;
                    }
                }
            }
        }
        return updated;
    }

    @Override
    public void rotate(Direction direction) {
        if (rotation != 0) {
            log.warn("Region chunk was already rotated (attempted {}).", direction);
            return;
        }
        GameObject[][][] copy = new GameObject[objects.length][SIZE][SIZE];
        int baseX = currentBase.getLocalX();
        int baseY = currentBase.getLocalY();
        for (int i = 0; i < objects.length; i++) {
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    copy[i][x][y] = objects[i][x][y];
                    objects[i][x][y] = plane.getObjects()[baseX + x][baseY + y] = null;
                    plane.getFlags().getClippingFlags()[baseX + x][baseY + y] = 0;
                }
            }
        }
        rotation = direction.toInteger();
        for (int i = 0; i < objects.length; i++) {
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    GameObject object = copy[i][x][y];
                    if (object != null) {
                        int[] pos = getRotatedPosition(x, y, object.getDefinition().getSizeX(), object.getDefinition().getSizeY(), object.getRotation(), rotation);
                        object = object.transform(object.getId(), (object.getRotation() + rotation) % 4, object.getLocation().transform(pos[0] - x, pos[1] - y, 0));
                        LandscapeParser.flagGameObject(plane, baseX + pos[0], baseY + pos[1], object, true, true);
                    }
                }
            }
        }
    }

    @Override
    public BuildRegionChunk copy(RegionPlane plane) {
        BuildRegionChunk chunk = new BuildRegionChunk(base, rotation, plane);
        for (int i = 0; i < chunk.objects.length; i++) {
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    GameObject o = objects[i][x][y];
                    if (o instanceof Constructed) {
                        chunk.objects[i][x][y] = o.transform(o.getId()).asConstructed();
                    } else if (o != null) {
                        chunk.objects[i][x][y] = o.transform(o.getId());
                        chunk.objects[i][x][y].setActive(o.isActive());
                        chunk.objects[i][x][y].setRenderable(o.isRenderable());
                    }
                }
            }
        }
        return chunk;
    }

    /**
     * Removes the game object.
     *
     * @param object The object to remove.
     */
    public void remove(GameObject object) {
        int chunkX = object.getLocation().getChunkOffsetX();
        int chunkY = object.getLocation().getChunkOffsetY();
        GameObject current = null;
        int index = -1;
        int i = 0;
        while ((current == null || current.getId() != object.getId()) && i < objects.length) {
            current = objects[i++][chunkX][chunkY];
            if (current == null && index == -1) {
                index = i - 1;
            }
        }
        if (current != null && current.equals(object)) {
            current.setActive(false);
            object.setRenderable(false);
        } else {
            objects[index][chunkX][chunkY] = object;
        }
        object.setActive(false);
        object.setRenderable(false);
    }

    /**
     * Adds the game object.
     *
     * @param object The object to add.
     */
    public void add(GameObject object) {
        int chunkX = object.getLocation().getChunkOffsetX();
        int chunkY = object.getLocation().getChunkOffsetY();
        GameObject current = null;
        int index = -1;
        int i = 0;
        while ((current == null || current.getId() != object.getId()) && i < objects.length) {
            current = objects[i++][chunkX][chunkY];
            if (current == null && index == -1) {
                index = i - 1;
            }
        }
        if (current != null && current.equals(object)) {
            current.setActive(true);
            object.setRenderable(true);
        } else {
            objects[index][chunkX][chunkY] = object = object.asConstructed();
        }
        object.setActive(true);
        object.setRenderable(true);
    }

    /**
     * Stores an object on the region chunk.
     *
     * @param object The object.
     */
    public void store(GameObject object) {
        if (object == null) {
            return;
        }
        int chunkX = object.getLocation().getChunkOffsetX();
        int chunkY = object.getLocation().getChunkOffsetY();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i][chunkX][chunkY] == null) {
                objects[i][chunkX][chunkY] = object;
                object.setActive(true);
                object.setRenderable(true);
                return;
            }
        }
        log.error("Insufficient array length for storing all objects! (len={})", objects.length);
    }

    /**
     * Gets the objects index for the given object id.
     *
     * @param x        The x-coordinate on the region chunk.
     * @param y        The y-coordinate on the region chunk.
     * @param objectId The object id.
     */
    public int getIndex(int x, int y, int objectId) {
        for (int i = 0; i < objects.length; i++) {
            GameObject o = get(x, y, i);
            if (o != null && o.getId() == objectId) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Gets a game object.
     *
     * @param x     The chunk x-coordinate.
     * @param y     The chunk y-coordinate.
     * @param index The index (0 = default).
     * @return The object.
     */
    public GameObject get(int x, int y, int index) {
        return objects[index][x][y];
    }

    /**
     * Gets the objects.
     *
     * @param index The index.
     * @return The objects array.
     */
    public GameObject[][] getObjects(int index) {
        return objects[index];
    }

}
