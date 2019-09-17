package plugin.activity.zulrah;

import org.gielinor.game.world.map.Location;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum SpawnLocation {

    NORTH(new Location(2266, 3072)),
    SOUTH(new Location(2267, 3064)),
    WEST(new Location(2258, 3072)),
    EAST(new Location(2277, 3072));

    /**
     * The location.
     */
    private final Location location;

    /**
     * Creates a new <code>SpawnLocation</code>.
     *
     * @param location The location.
     */
    private SpawnLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets the location.
     *
     * @return The location.
     */
    public Location getLocation() {
        return location;
    }

}
