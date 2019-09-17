package org.gielinor.game.content.skill.member.construction;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.BuildRegionChunk;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Region;
import org.gielinor.game.world.map.RegionChunk;
import org.gielinor.game.world.map.RegionManager;

/**
 * Represents a room.
 *
 * @author Emperor
 */
public final class Room {

    /**
     * The room properties.
     */
    private final RoomProperties properties;

    /**
     * The region chunk.
     */
    private RegionChunk chunk;

    /**
     * The hotspots.
     */
    private Hotspot[] hotspots;

    /**
     * The current rotation of the room.
     */
    private Direction rotation = Direction.NORTH;

    /**
     * If this room is new.
     */
    private boolean newRoom;

    /**
     * Constructs a new {@code Room} {@code Object}.
     *
     * @param properties The room properties.
     */
    public Room(RoomProperties properties) {
        this.properties = properties;
    }

    /**
     * Creates a new room.
     *
     * @param player     The player.
     * @param properties The room properties.
     * @return The room.
     */
    public static Room create(Player player, RoomProperties properties) {
        Room room = new Room(properties);
        room.configure(player.getHouseManager().getStyle());
        return room;
    }

    /**
     * Configures the room.
     */
    public void configure(HousingStyle style) {
        this.hotspots = new Hotspot[properties.getHotspots().length];
        for (int i = 0; i < hotspots.length; i++) {
            hotspots[i] = properties.getHotspots()[i].copy();
        }
        decorate(style);
    }

    /**
     * Redecorates the room.
     *
     * @param style The house style.
     */
    public void decorate(HousingStyle style) {
        Region region = RegionManager.forId(style.getRegionId());
        Region.load(region, true);
        chunk = region.getPlanes()[style.getPlane()].getRegionChunk(properties.getChunkX(), properties.getChunkY());
    }

    /**
     * Loads all the decorations.
     *
     * @param buildRegionChunk The chunk used in the dynamic region.
     * @param buildingMode     If building mode is enabled.
     */
    public void loadDecorations(BuildRegionChunk buildRegionChunk, boolean buildingMode) {
        for (Hotspot hotspot : hotspots) {
            int x = hotspot.getChunkX();
            int y = hotspot.getChunkY();
            if (hotspot.getDecorationIndex() > -1) {
                GameObject[][] objects = buildRegionChunk.getObjects(buildRegionChunk.getIndex(x, y, hotspot.getHotspot().getObjectId()));
                if (objects[x][y] != null) {
                    objects[x][y] = objects[x][y].transform(hotspot.getHotspot().getDecorations()[hotspot.getDecorationIndex()].getObjectId());
                }
            }
        }
        if (rotation != Direction.NORTH && buildRegionChunk.getRotation() == 0) {
            buildRegionChunk.rotate(rotation);
        }
        if (!buildingMode) {
            for (GameObject[] objects : buildRegionChunk.getObjects()) {
                for (GameObject o : objects) {
                    if (o != null) {
                        if (o.getDefinition().hasAction("build")) {
                            buildRegionChunk.remove(o);
                        }
                        if (o.getId() == 13830) {// || o.getId() == BuildHotspot.CURTAINS.getObjectId()) {
                            buildRegionChunk.add(o.transform(13099, o.getRotation(), 0));
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets the exit directions.
     *
     * @return The directions at which you can exit the room (0=east, 1=south,
     * 2=west, 3=north).
     */
    public boolean[] getExits() {
        boolean[] exits = { isExit(7, 3), isExit(3, 0), isExit(0, 3), isExit(3, 7) };
        if (rotation != Direction.NORTH && chunk.getRotation() == 0) {
            for (int i = 0; i < RoomBuilder.DIRECTIONS.length; i++) {
                if (rotation == RoomBuilder.DIRECTIONS[i]) {
                    break;
                }
                boolean b = exits[0];
                for (int j = 0; j < exits.length - 1; j++) {
                    exits[j] = exits[j + 1];
                }
                exits[exits.length - 1] = b;
            }
        }
        return exits;
    }

    /**
     * Checks if the object on the given chunk coordinates is a door.
     *
     * @param chunkX The x location in the chunk.
     * @param chunkY The y location in the chunk.
     * @return {@code True} if so.
     */
    private boolean isExit(int chunkX, int chunkY) {
        GameObject object = chunk.getObjects()[chunkX][chunkY];
        if (object != null && object.getType() < 4 && object.getName() != null) {
            return true;
        }
        return false;
    }

    /**
     * Gets the chunk.
     *
     * @return The chunk.
     */
    public RegionChunk getChunk() {
        return chunk;
    }

    /**
     * Sets the chunk.
     *
     * @param chunk The chunk to set.
     */
    public void setChunk(RegionChunk chunk) {
        this.chunk = chunk;
    }

    /**
     * Gets the hotspots.
     *
     * @return The hotspots.
     */
    public Hotspot[] getHotspots() {
        return hotspots;
    }

    /**
     * Sets the hotspots.
     *
     * @param hotspots The hotspots to set.
     */
    public void setHotspots(Hotspot[] hotspots) {
        this.hotspots = hotspots;
    }

    /**
     * Gets the properties.
     *
     * @return The properties.
     */
    public RoomProperties getProperties() {
        return properties;
    }

    /**
     * Sets the room rotation.
     *
     * @param rotation The rotation.
     */
    public void setRotation(Direction rotation) {
        this.rotation = rotation;
    }

    /**
     * Gets the rotation.
     *
     * @return The rotation.
     */
    public Direction getRotation() {
        return rotation;
    }

    /**
     * If this room is new.
     */
    public boolean isNewRoom() {
        return newRoom;
    }

    public void setNewRoom(boolean newRoom) {
        this.newRoom = newRoom;
    }
}
