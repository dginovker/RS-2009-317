package plugin.npc.osrs.zulrah;

import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;

/**
 * Represents a zulrah spot.
 *
 * @author Empathy
 */
public class ZulrahSpot {

    /**
     * The location for Zulrah to spawn.
     */
    private final Location location;

    /**
     * The type of Zulrah to spawn.
     */
    private final ZulrahType type;

    /**
     * The direction of Zulrah to face when spawned.
     */
    private final Direction direction;

    /**
     * The locations of toxic clouds.
     */
    private Location[] toxicClouds;

    /**
     * The locations of the snakelingLocations.
     */
    private Location[] snakelingLocations;

    /**
     * Additional data if needed for the spawn.
     */
    private Object[] data;

    /**
     * Constructs a new {@code ZulrahSpot} object.
     *
     * @param type     The type
     * @param location The location
     */
    public ZulrahSpot(ZulrahType type, Location location, Direction direction) {
        this.type = type;
        this.location = location;
        this.direction = direction;
    }

    /**
     * Constructs a new @{Code ZulrahSpot} object.
     *
     * @param type
     * @param location
     * @param direction
     * @param toxicClouds
     */
    public ZulrahSpot(ZulrahType type, Location location, Direction direction, Location[] toxicClouds) {
        this(type, location, direction);
        this.toxicClouds = toxicClouds;
    }

    /**
     * Constructs a new @{Code ZulrahSpot} object.
     *
     * @param type
     * @param location
     * @param direction
     * @param toxicClouds
     * @param snakelingLocations
     */
    public ZulrahSpot(ZulrahType type, Location location, Direction direction, Location[] toxicClouds, Location[] snakelingLocations) {
        this(type, location, direction, toxicClouds);
        this.snakelingLocations = snakelingLocations;
    }

    /**
     * Constructs a new {@code ZulrahSpot} object.
     *
     * @param type     The type
     * @param location The location
     * @param data     The data
     */
    public ZulrahSpot(ZulrahType type, Location location, Direction direction, Location[] toxicClouds, Location[] snakelingLocations, Object... data) {
        this(type, location, direction, toxicClouds, snakelingLocations);
        this.data = data;
    }

    /**
     * @return The zulrah type.
     */
    public ZulrahType getType() {
        return type;
    }

    /**
     * @return The location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the location using offsets by the Zulrah NPC.
     *
     * @param npc The npc.
     * @return The location.
     */
    public Location getLocation(ZulrahNPC npc) {
        return getLocation(npc.getXOffset(), npc.getYOffset());
    }

    /**
     * Gets the location using an offset.
     *
     * @param offsetX The x offset.
     * @param offSetY The y offset.
     * @return The location.
     */
    public Location getLocation(int offsetX, int offSetY) {
        return Location.create(location.getX() - offsetX, location.getY() - offSetY, 0);
    }

    /**
     * @return The direction.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @return The toxic clouds.
     */
    public Location[] getToxicClouds() {
        return toxicClouds;
    }

    /**
     * @return The snakelingLocations.
     */
    public Location[] getSnakelingLocations() {
        return snakelingLocations;
    }

    /**
     * @return The data.
     */
    public Object getData() {
        return data;
    }

    /**
     * Returns the new location
     *
     * @param loc     The loc.
     * @param offsetX The offset x.
     * @param offsetY The offset y.
     * @return The location.
     */
    public static Location getLocation(Location loc, int offsetX, int offsetY) {
        return Location.create(loc.getX() - offsetX, loc.getY() - offsetY, loc.getZ());
    }

    /**
     * Returns the new location
     *
     * @param loc     The loc.
     * @param offsetX The offset x.
     * @param offsetY The offset y.
     * @return The location.
     */
    public static Location getLocation(Location loc, ZulrahNPC npc) {
        return getLocation(loc, npc.getXOffset(), npc.getYOffset());
    }

}
