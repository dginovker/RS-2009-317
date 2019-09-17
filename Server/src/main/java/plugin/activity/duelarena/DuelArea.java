package plugin.activity.duelarena;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents areas in which players will duel within the Duel Arena.
 * Handles their locations for different duel types (such as obstacles).
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DuelArea {

    /**
     * Obstacle duel areas.
     */
    public static final ZoneBorders[] OBSTACLE_AREA = new ZoneBorders[]{
        new ZoneBorders(3367, 3246, 3385, 3256),
        new ZoneBorders(3336, 3227, 3355, 3237),
        new ZoneBorders(3367, 3208, 3386, 3218)
    };

    /**
     * Normal fighting areas.
     */
    public static final ZoneBorders[] NORMAL_AREA = new ZoneBorders[]{
        new ZoneBorders(3337, 3245, 3355, 3256),
        new ZoneBorders(3366, 3227, 3386, 3237),
        new ZoneBorders(3337, 3207, 3354, 3218)
    };

    /**
     * Gets a random spawn location for the duel fight.
     *
     * @param obstacles Whether or not to use an obstacle.
     * @return The random spawn area.
     */
    public static ZoneBorders[] getRandomSpawn(boolean obstacles) {
        return obstacles ? OBSTACLE_AREA : NORMAL_AREA;
    }

    /**
     * Gets the spawn locations for the beginning of the duel.
     *
     * @param obstacles  Whether or not obstacles are enabled.
     * @param noMovement Whether or not movement is disabled.
     * @return The locations.
     */
    public static List<Location> getSpawnLocations(boolean obstacles, boolean noMovement) {
        List<Location> locationList = new ArrayList<>();
        ZoneBorders[] zoneBorders = getRandomSpawn(obstacles);
        ZoneBorders zoneBorder = zoneBorders[RandomUtil.random(0, (zoneBorders.length - 1))];
        locationList.add(Location.getRandomLocation(zoneBorder));
        locationList.add(noMovement ? locationList.get(0).getNextToCombat() : Location.getRandomLocation(zoneBorder));
        return locationList;
    }


    /**
     * Gets a random location within the Duel Arena.
     *
     * @return The location.
     */
    public static Location getRandomDuelArenaLocation() {
        return Location.getRandomLocation(new ZoneBorders(3356, 3268, 3378, 3278));
    }
}
